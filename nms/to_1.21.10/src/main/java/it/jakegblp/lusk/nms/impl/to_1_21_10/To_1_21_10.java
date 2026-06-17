package it.jakegblp.lusk.nms.impl.to_1_21_10;

import it.jakegblp.lusk.nms.core.serialization.BufferCodecs;
import it.jakegblp.lusk.nms.core.serialization.Mapper;
import it.jakegblp.lusk.nms.core.serialization.Mappings;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftServer;

public class To_1_21_10 {
    public static void registerCodecs(Mappings mappings, BufferCodecs codecs) {
        var registryAccess = (((CraftServer) Bukkit.getServer()).getServer()).registryAccess();

        var villagerDataMapper = mappings.register(
                VillagerData.class, it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData.class,
                villagerData -> {
                    Mapper<ResourceLocation, NamespacedKey> mapper = (Mapper<ResourceLocation, NamespacedKey>) mappings.getMapperFromForwardClass(NamespacedKey.class);
                    return new it.jakegblp.lusk.nms.core.world.entity.villager.VillagerData(
                            Registry.VILLAGER_TYPE.getOrThrow(mapper.to(villagerData.type().unwrapKey().orElseThrow().location())),
                            Registry.VILLAGER_PROFESSION.getOrThrow(mapper.to(villagerData.profession().unwrapKey().orElseThrow().location())),
                            villagerData.level());
                },
                villagerData -> {
                    Mapper<ResourceLocation, NamespacedKey> mapper = (Mapper<ResourceLocation, NamespacedKey>) mappings.getMapperFromForwardClass(NamespacedKey.class);
                    return new VillagerData(
                            registryAccess.lookupOrThrow(Registries.VILLAGER_TYPE).getOrThrow(ResourceKey.create(Registries.VILLAGER_TYPE, mapper.from(villagerData.type().getKey()))),
                            registryAccess.lookupOrThrow(Registries.VILLAGER_PROFESSION).getOrThrow(ResourceKey.create(Registries.VILLAGER_PROFESSION, mapper.from(villagerData.profession().getKey()))),
                            villagerData.level());
                });
        codecs.register(
                villagerDataMapper,
                (buffer, value) -> VillagerData.STREAM_CODEC.encode(new RegistryFriendlyByteBuf(buffer.unwrap(), registryAccess), value),
                buffer -> VillagerData.STREAM_CODEC.decode(new RegistryFriendlyByteBuf(buffer.unwrap(), registryAccess))
        );

    }
}