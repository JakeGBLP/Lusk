package it.jakegblp.lusk.nms.impl.allversions;

import io.netty.buffer.Unpooled;
import it.jakegblp.lusk.common.Instances;
import it.jakegblp.lusk.nms.core.adapters.SharedBiomeAdapter;
import lombok.SneakyThrows;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundChunksBiomesPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BiomeAllVersions implements SharedBiomeAdapter {


    @Override
    public void sendBiomePacket(Set<Player> viewers, Location corner1, Location corner2, String biomeName) {
        if (!corner1.getWorld().equals(corner2.getWorld()) || !corner1.getWorld().equals(corner1.getWorld())) return;

        final JavaPlugin plugin = Instances.LUSK;

        Bukkit.getScheduler().runTask(plugin, () -> {
            ServerLevel level = ((CraftWorld) corner1.getWorld()).getHandle();

            int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
            int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
            int minY = Math.max(level.getMinY(), Math.min(corner1.getBlockY(), corner2.getBlockY()));
            int maxY = Math.min(level.getMaxY() - 1, Math.max(corner1.getBlockY(), corner2.getBlockY()));
            int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
            int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

            Holder<Biome> TARGET = resolveBiomeHolder(level, biomeName);

            int cMinX = Math.floorDiv(minX, 16), cMaxX = Math.floorDiv(maxX, 16);
            int cMinZ = Math.floorDiv(minZ, 16), cMaxZ = Math.floorDiv(maxZ, 16);

            int worldMinY = level.getMinY();

            List<ChunkWork> work = new ArrayList<>();
            for (int cx = cMinX; cx <= cMaxX; cx++) {
                for (int cz = cMinZ; cz <= cMaxZ; cz++) {
                    LevelChunk chunk = level.getChunk(cx, cz);
                    LevelChunkSection[] sections = chunk.getSections();

                    List<PalettedContainer<Holder<Biome>>> copies = new ArrayList<>(sections.length);

                    for (LevelChunkSection sec : sections) {
                        PalettedContainer<Holder<Biome>> copy;
                        if (sec != null) {
                            PalettedContainerRO<Holder<Biome>> ro = sec.getBiomes();
                            copy = ro.copy();
                        } else {
                            copy = newEmptyBiomeContainer(TARGET);
                        }
                        copies.add(copy);
                    }

                    work.add(new ChunkWork(new ChunkPos(cx, cz), copies));
                }
            }

            Constructor<?> cbdListCtor  = getChunkBiomeDataListCtor();
            Constructor<?> cbdBytesCtor = getChunkBiomeDataBytesCtor();

            CompletableFuture
                    .supplyAsync(() -> buildChunkBiomeDatasAsync(work, TARGET,
                            minX, minY, minZ, maxX, maxY, maxZ, worldMinY, cbdListCtor, cbdBytesCtor))
                    .thenAccept(chunkDatas -> {
                        if (chunkDatas.isEmpty()) return;
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            ClientboundChunksBiomesPacket pkt =
                                    new ClientboundChunksBiomesPacket(castUncheckedList(chunkDatas));

                            for (Player p : viewers) {
                                ServerPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
                                nmsPlayer.connection.send(pkt);
                            }

                        });
                    });
        });
    }

    private static List<Object> buildChunkBiomeDatasAsync(
            List<ChunkWork> work,
            Holder<Biome> TARGET,
            int minX, int minY, int minZ, int maxX, int maxY, int maxZ,
            int worldMinY,
            Constructor<?> cbdListCtor,
            Constructor<?> cbdBytesCtor
    ) {
        List<Object> out = new ArrayList<>(work.size());

        for (ChunkWork w : work) {
            int cx = w.pos.x;
            int cz = w.pos.z;

            List<PalettedContainer<Holder<Biome>>> copies = w.sectionCopies;

            for (int si = 0; si < copies.size(); si++) {
                PalettedContainer<Holder<Biome>> copy = copies.get(si);
                int secMinY = worldMinY + si * 16;
                int secMaxY = secMinY + 15;
                if (secMaxY < minY || secMinY > maxY) continue;

                int boxMinX = Math.max(minX, cx * 16);
                int boxMaxX = Math.min(maxX, cx * 16 + 15);
                int boxMinY = Math.max(minY, secMinY);
                int boxMaxY = Math.min(maxY, secMaxY);
                int boxMinZ = Math.max(minZ, cz * 16);
                int boxMaxZ = Math.min(maxZ, cz * 16 + 15);

                int sMinX = (boxMinX & 15) >> 2, sMaxX = (boxMaxX & 15) >> 2;
                int sMinY = (boxMinY - secMinY) >> 2, sMaxY = (boxMaxY - secMinY) >> 2;
                int sMinZ = (boxMinZ & 15) >> 2, sMaxZ = (boxMaxZ & 15) >> 2;

                for (int sx = sMinX; sx <= sMaxX; sx++)
                    for (int sy = sMinY; sy <= sMaxY; sy++)
                        for (int sz = sMinZ; sz <= sMaxZ; sz++)
                            copy.set(sx, sy, sz, TARGET);
            }

            Object cbd;
            if (cbdListCtor != null) {
                List<PalettedContainerRO<Holder<Biome>>> asRO = new ArrayList<>(copies);
                try {
                    cbd = cbdListCtor.newInstance(w.pos, asRO);
                } catch (Exception e) {
                    throw new IllegalStateException("ChunkBiomeData(list) failed for " + w.pos, e);
                }
            } else if (cbdBytesCtor != null) {
                byte[] payload = serializeBiomeContainers(copies);
                try {
                    cbd = cbdBytesCtor.newInstance(w.pos, payload);
                } catch (Exception e) {
                    throw new IllegalStateException("ChunkBiomeData(bytes) failed for " + w.pos, e);
                }
            } else {
                throw new IllegalStateException("No usable ChunkBiomeData constructor found.");
            }

            out.add(cbd);
        }

        return out;
    }

    @SneakyThrows
    private static Holder<Biome> resolveBiomeHolder(ServerLevel level, String biomeName) {
        String idStr = (biomeName == null ? "" : biomeName.trim())
                .toLowerCase(Locale.ROOT)
                .replace(' ', '_');

        if (!idStr.contains(":")) {
            idStr = "minecraft:" + idStr;
        }

        net.minecraft.resources.ResourceLocation id = net.minecraft.resources.ResourceLocation.tryParse(idStr);
        if (id == null) {
            throw new IllegalArgumentException("Invalid biome id: " + biomeName + " (must be lowercase a-z0-9/._-)");
        }

        ResourceKey<Biome> key =
                ResourceKey.create(Registries.BIOME, id);

        Object regOrLookup;
        try {
            Method m = level.registryAccess().getClass()
                    .getMethod("lookupOrThrow", ResourceKey.class);
            regOrLookup = m.invoke(level.registryAccess(), Registries.BIOME);
        } catch (NoSuchMethodException e) {
            try {
                Method m = level.registryAccess().getClass()
                        .getMethod("registryOrThrow", ResourceKey.class);
                regOrLookup = m.invoke(level.registryAccess(), Registries.BIOME);
            } catch (Throwable t) {
                try {
                    Method m = level.registryAccess().getClass()
                            .getMethod("registry", ResourceKey.class);
                    regOrLookup = ((java.util.Optional<?>) m.invoke(level.registryAccess(), Registries.BIOME))
                            .orElseThrow();
                } catch (Throwable t2) {
                    throw new IllegalStateException("Cannot access biome registry/lookup", t2);
                }
            }
        }

        try {
            Method getOrThrow = regOrLookup.getClass().getMethod("getOrThrow", ResourceKey.class);
            @SuppressWarnings("unchecked")
            Holder<Biome> h =
                    (Holder<Biome>) getOrThrow.invoke(regOrLookup, key);
            return h;
        } catch (NoSuchMethodException ignored) { }

        try {
            Method getHolder = regOrLookup.getClass().getMethod("getHolder", ResourceKey.class);
            Object opt = getHolder.invoke(regOrLookup, key);
            @SuppressWarnings("unchecked")
            java.util.Optional<Holder<Biome>> oh =
                    (java.util.Optional<Holder<Biome>>) opt;
            String finalIdStr = idStr;
            return oh.orElseThrow(() -> new IllegalArgumentException("Biome not found: " + finalIdStr));
        } catch (NoSuchMethodException e) {
            try {
                Method get = regOrLookup.getClass().getMethod("get", ResourceKey.class);
                Object biome = get.invoke(regOrLookup, key);
                if (biome == null) throw new IllegalArgumentException("Biome not found: " + idStr);
                Method direct = Holder.class.getMethod("direct", Object.class);
                @SuppressWarnings("unchecked")
                Holder<Biome> h =
                        (Holder<Biome>) direct.invoke(null, biome);
                return h;
            } catch (Throwable t) {
                throw new IllegalStateException("Failed to resolve biome holder for " + idStr, t);
            }
        } catch (Throwable t) {
            throw new IllegalStateException("Failed to resolve biome holder (registry path)", t);
        }
    }

    @SuppressWarnings("unchecked")
    private static PalettedContainer<Holder<Biome>>
    newEmptyBiomeContainer(Holder<Biome> fill) {
        try {
            var cfg   = Class.forName("net.minecraft.world.level.chunk.PalettedContainer$Configuration")
                    .getField("BIOME").get(null);
            var strat = Class.forName("net.minecraft.world.level.chunk.PalettedContainer$Strategy")
                    .getField("SECTION_BIOMES").get(null);
            return (PalettedContainer<Holder<Biome>>) Class
                    .forName("net.minecraft.world.level.chunk.PalettedContainer")
                    .getConstructor(cfg.getClass(), Object.class, strat.getClass())
                    .newInstance(cfg, fill, strat);
        } catch (Throwable t) {
            throw new IllegalStateException("Unable to construct biome container", t);
        }
    }

    private static Constructor<?> getChunkBiomeDataListCtor() {
        try {
            return ClientboundChunksBiomesPacket.ChunkBiomeData.class
                    .getDeclaredConstructor(ChunkPos.class, List.class);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private static Constructor<?> getChunkBiomeDataBytesCtor() {
        try {
            return ClientboundChunksBiomesPacket.ChunkBiomeData.class
                    .getDeclaredConstructor(ChunkPos.class, byte[].class);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private static byte[] serializeBiomeContainers(
            List<PalettedContainer<Holder<Biome>>> containers) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        for (PalettedContainer<Holder<Biome>> pc : containers) {
            pc.write(buf);
        }
        byte[] out = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), out);
        return out;
    }

    @SuppressWarnings("unchecked")
    private static List<ClientboundChunksBiomesPacket.ChunkBiomeData>
    castUncheckedList(List<Object> list) {
        return (List<ClientboundChunksBiomesPacket.ChunkBiomeData>)(List<?>) list;
    }

    private static final class ChunkWork {
        final ChunkPos pos;
        final List<PalettedContainer<Holder<Biome>>> sectionCopies;
        ChunkWork(ChunkPos pos, List<PalettedContainer<Holder<Biome>>> sectionCopies) {
            this.pos = pos;
            this.sectionCopies = sectionCopies;
        }
    }
}
