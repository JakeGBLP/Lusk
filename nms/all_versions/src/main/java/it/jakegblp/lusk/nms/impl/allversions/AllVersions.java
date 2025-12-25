package it.jakegblp.lusk.nms.impl.allversions;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.papermc.paper.adventure.PaperAdventure;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.adapters.SharedBehaviorAdapter;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.util.BufferCodec;
import it.jakegblp.lusk.nms.core.util.CompositeBufferCodec;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataItem;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import it.jakegblp.lusk.nms.core.world.player.ChatSessionData;
import it.jakegblp.lusk.nms.core.world.player.PlayerInfo;
import it.jakegblp.lusk.nms.core.world.player.TeamParameters;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.VarInt;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.bukkit.*;
import org.bukkit.Registry;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.*;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

import static it.jakegblp.lusk.common.StructureTranslation.fromMapToPairList;
import static it.jakegblp.lusk.common.StructureTranslation.fromPairListToMap;
import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;
import static it.jakegblp.lusk.nms.core.util.NullabilityUtils.convertIfNotNull;
import static it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey.Type.OPTIONAL;
import static net.minecraft.network.codec.ByteBufCodecs.ROTATION_BYTE;

@Getter
public class AllVersions implements
        SharedBehaviorAdapter<
                ServerPlayer,
                Packet,
                RemoteChatSession.Data,
                ServerGamePacketListenerImpl,
                Connection,
                Team.Visibility,
                Team.CollisionRule,
                DedicatedServer,
                ClientboundBundlePacket,
                ClientboundSetEntityDataPacket,
                ClientboundPlayerInfoUpdatePacket,
                ClientboundPlayerInfoUpdatePacket.Action,
                ClientboundSystemChatPacket,
                ClientboundLevelParticlesPacket,
                ClientboundUpdateAttributesPacket,
                ClientboundSetCameraPacket,
                ClientboundSetPlayerTeamPacket,
                ClientboundSetPlayerTeamPacket.Parameters,
                ClientboundEntityEventPacket,
                ClientboundEntityPositionSyncPacket, // todo add teleport packet for 1.21.2 and below (the packet changed for higher to this)
                ClientboundBlockUpdatePacket,
                SoundEvent,
                Holder<SoundEvent>,
                ClientboundSoundPacket,
                ClientboundSoundEntityPacket,
                ClientboundSetEquipmentPacket
                > {

    public <T> StreamCodec<SimpleByteBuf, T> registryCodec(final ResourceKey<? extends net.minecraft.core.Registry<T>> registryKey) {
        return registryCodec(registryKey, (registry) -> registry);
    }

    public <T, R> StreamCodec<SimpleByteBuf, R> registryCodec(final ResourceKey<? extends net.minecraft.core.Registry<T>> registryKey, final Function<net.minecraft.core.Registry<T>, IdMap<R>> idGetter) {
        return new StreamCodec<>() {
            private IdMap<R> getRegistryOrThrow() {
                return idGetter.apply(AllVersions.this.getDedicatedServer().registryAccess().lookupOrThrow(registryKey));
            }

            public @NotNull R decode(@NotNull SimpleByteBuf buffer) {
                return this.getRegistryOrThrow().byIdOrThrow(VarInt.read(buffer.unwrap()));
            }

            public void encode(@NotNull SimpleByteBuf buffer, @NotNull R value) {
                VarInt.write(buffer.unwrap(), this.getRegistryOrThrow().getIdOrThrow(value));
            }
        };
    }

    public final BufferCodec<ResourceLocation, NamespacedKey> RESOURCE_LOCATION_256_CODEC =
            registerCodec(ResourceLocation.class, NamespacedKey.class,
                    (buffer, rl) -> buffer.writeString(rl.toString()),
                    buffer -> ResourceLocation.parse(buffer.readString(256)),
                    (buffer, key) -> buffer.writeString(key.toString()),
                    buffer -> Preconditions.checkNotNull(NamespacedKey.fromString(buffer.readString(256)), "Invalid namespaced key codec conversion")
            );

    public final BufferCodec<Pose, org.bukkit.entity.Pose> POSE_CODEC =
            registerCodec(BufferCodec.simple(
                    Pose.class, org.bukkit.entity.Pose.class,
                    nms -> Pose.CROUCHING.equals(nms) ? org.bukkit.entity.Pose.SNEAKING : org.bukkit.entity.Pose.valueOf(nms.name()),
                    bukkit -> org.bukkit.entity.Pose.SNEAKING.equals(bukkit) ? Pose.CROUCHING : Pose.valueOf(bukkit.name()),
                    (buffer, value) -> Pose.STREAM_CODEC.encode(buffer.unwrap(), value),
                    buffer -> Pose.STREAM_CODEC.decode(buffer.unwrap()))
            );
    public final BufferCodec<Component, net.kyori.adventure.text.Component> COMPONENT_CODEC =
            registerCodec(BufferCodec.simple(
                    Component.class, net.kyori.adventure.text.Component.class,
                    PaperAdventure::asAdventure,
                    PaperAdventure::asVanilla,
                    (buffer, value) -> ComponentSerialization.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
                    buffer -> ComponentSerialization.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)))
            );

    public final BufferCodec<EquipmentSlot, org.bukkit.inventory.EquipmentSlot> EQUIPMENTSLOT_CODEC =
            registerCodec(BufferCodec.simple(
                    EquipmentSlot.class, org.bukkit.inventory.EquipmentSlot.class,
                    CraftEquipmentSlot::getSlot,
                    CraftEquipmentSlot::getNMS,
                    (buffer, value) -> EquipmentSlot.STREAM_CODEC.encode(buffer.unwrap(), value),
                    buffer -> EquipmentSlot.STREAM_CODEC.decode(buffer.unwrap()))
            );

    public final BufferCodec<EntityType, org.bukkit.entity.EntityType> ENTITY_TYPE_CODEC =
            BufferCodec.simple(EntityType.class, org.bukkit.entity.EntityType.class,
                    entityType -> Registry.ENTITY_TYPE.getOrThrow(CraftNamespacedKey.fromMinecraft(BuiltInRegistries.ENTITY_TYPE.getKey(entityType))),
                    entityType -> BuiltInRegistries.ENTITY_TYPE.getValue(CraftNamespacedKey.toMinecraft(entityType.getKey())),
                    (buffer, entityType) -> EntityType.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), entityType),
                    buffer -> EntityType.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer))
            );
    public final BufferCodec<GameProfile, PlayerProfile> PLAYER_PROFILE_CODEC =
            BufferCodec.simple(GameProfile.class, PlayerProfile.class,
                    CraftPlayerProfile::asBukkitCopy,
                    CraftPlayerProfile::asAuthlibCopy,
                    (buffer, profile) ->  ByteBufCodecs.GAME_PROFILE.encode(buffer.unwrap(), profile),
                    buffer -> ByteBufCodecs.GAME_PROFILE.decode(buffer.unwrap())
            );
    public final BufferCodec<Integer, Integer> UNSIGNED_BYTE_CODEC =
            registerCodec(BufferCodec.identity(Integer.class, SimpleByteBuf::writeUnsignedByte, SimpleByteBuf::readUnsignedByte));

    public final BufferCodec<Byte, Byte> BYTE_CODEC = registerCodec(BufferCodec.identity(Byte.class, SimpleByteBuf::writeByte, SimpleByteBuf::readByte));
    public final BufferCodec<Integer, Integer> VAR_INT_CODEC = registerCodec(BufferCodec.identity(Integer.class, SimpleByteBuf::writeVarInt, SimpleByteBuf::readVarInt));
    public final BufferCodec<Long, Long> LONG_CODEC = registerCodec(BufferCodec.identity(Long.class, SimpleByteBuf::writeLong, SimpleByteBuf::readLong));
    public final BufferCodec<Double, Double> DOUBLE_CODEC = registerCodec(Double.class, Double.class, BufferCodec.identity(SimpleByteBuf::writeDouble, SimpleByteBuf::readDouble));
    public final BufferCodec<UUID, UUID> UUID_CODEC = registerCodec(UUID.class, UUID.class, BufferCodec.identity(SimpleByteBuf::writeUUID, SimpleByteBuf::readUUID));

    public final BufferCodec<Vec3, Vector> VECTOR_CODEC =
            registerCodec(Vec3.class, Vector.class, BufferCodec.of(
                    (buffer, vector) -> FriendlyByteBuf.writeVec3(buffer.unwrap(), vector),
                    buffer -> FriendlyByteBuf.readVec3(buffer.unwrap()),
                    SimpleByteBuf::writeVector,
                    SimpleByteBuf::readVector)
            );

    public final BufferCodec<BlockPos, BlockVector> BLOCK_VECTOR_CODEC =
            registerCodec(BlockPos.class, BlockVector.class, BufferCodec.of(
                    (buffer, vector) -> FriendlyByteBuf.writeBlockPos(buffer.unwrap(), vector),
                    buffer -> FriendlyByteBuf.readBlockPos(buffer.unwrap()),
                    SimpleByteBuf::writeBlockVector,
                    SimpleByteBuf::readBlockVector)
            );

    public final BufferCodec<Float, Float> DEGREES_CODEC =
            registerCodec(Float.class, Float.class, BufferCodec.identity((buffer, value) -> ROTATION_BYTE.encode(buffer.unwrap(), value), buffer -> ROTATION_BYTE.decode(buffer.unwrap())));
    public final BufferCodec<IntList, IntList> INT_LIST_CODEC =
            registerCodec(IntList.class, IntList.class, BufferCodec.identity(SimpleByteBuf::writeIntIdList, SimpleByteBuf::readIntIdList));
    public final BufferCodec<ClientboundAddEntityPacket, AddEntityPacket> ADD_ENTITY_PACKET_CODEC = registerCodec(
            CompositeBufferCodec.builder(ClientboundAddEntityPacket.class, AddEntityPacket.class)
                    .with(VAR_INT_CODEC, ClientboundAddEntityPacket::getId, AddEntityPacket::getId)
                    .with(UUID_CODEC, ClientboundAddEntityPacket::getUUID, AddEntityPacket::getEntityUUID)
                    .with(ENTITY_TYPE_CODEC, ClientboundAddEntityPacket::getType, AddEntityPacket::getEntityType)
                    .with(DOUBLE_CODEC, ClientboundAddEntityPacket::getX, AddEntityPacket::getX)
                    .with(DOUBLE_CODEC, ClientboundAddEntityPacket::getY, AddEntityPacket::getY)
                    .with(DOUBLE_CODEC, ClientboundAddEntityPacket::getZ, AddEntityPacket::getZ)
                    .with(VECTOR_CODEC, ClientboundAddEntityPacket::getMovement, AddEntityPacket::getVelocity)
                    .with(DEGREES_CODEC, ClientboundAddEntityPacket::getXRot, AddEntityPacket::getPitch)
                    .with(DEGREES_CODEC, ClientboundAddEntityPacket::getYRot, AddEntityPacket::getYaw)
                    .with(DEGREES_CODEC, ClientboundAddEntityPacket::getYHeadRot, AddEntityPacket::getHeadYaw)
                    .with(VAR_INT_CODEC, ClientboundAddEntityPacket::getData, AddEntityPacket::getData)
                    .build(buffer -> ClientboundAddEntityPacket.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)), AddEntityPacket::new)
            );

    public final BufferCodec<ClientboundBlockDestructionPacket, BlockDestructionPacket> BLOCK_DESTRUCTION_PACKET_CODEC = registerCodec(
            CompositeBufferCodec.builder(ClientboundBlockDestructionPacket.class, BlockDestructionPacket.class)
                    .with(VAR_INT_CODEC, ClientboundBlockDestructionPacket::getId, BlockDestructionPacket::getId)
                    .with(BLOCK_VECTOR_CODEC, ClientboundBlockDestructionPacket::getPos, BlockDestructionPacket::getPosition)
                    .with(BYTE_CODEC, packet -> (byte)packet.getProgress(), packet -> (byte)packet.getBlockDestructionStage())
                    .build(buffer -> ClientboundBlockDestructionPacket.STREAM_CODEC.decode(toFriendlyByteBuf(buffer)), BlockDestructionPacket::new)
    );
    public final BufferCodec<ClientboundAnimatePacket, EntityAnimationPacket> ENTITY_ANIMATION_PACKET_CODEC = registerCodec(
            CompositeBufferCodec.builder(ClientboundAnimatePacket.class, EntityAnimationPacket.class)
                    .with(VAR_INT_CODEC, ClientboundAnimatePacket::getId, EntityAnimationPacket::getId)
                    .with(UNSIGNED_BYTE_CODEC, ClientboundAnimatePacket::getAction, EntityAnimationPacket::getEntityAnimationId)
                    .build(buffer -> ClientboundAnimatePacket.STREAM_CODEC.decode(toFriendlyByteBuf(buffer)), EntityAnimationPacket::new)
    );
    public final BufferCodec<ClientboundRemoveEntitiesPacket, RemoveEntitiesPacket> REMOVE_ENTITIES_PACKET_CODEC = registerCodec(
            CompositeBufferCodec.builder(ClientboundRemoveEntitiesPacket.class, RemoveEntitiesPacket.class)
                    .with(INT_LIST_CODEC, ClientboundRemoveEntitiesPacket::getEntityIds, RemoveEntitiesPacket::getEntityIds)
                    .build(buffer -> ClientboundRemoveEntitiesPacket.STREAM_CODEC.decode(toFriendlyByteBuf(buffer)), RemoveEntitiesPacket::new)
    );
    public final BufferCodec<ItemStack, org.bukkit.inventory.ItemStack> ITEMSTACK_CODEC =
            registerCodec(BufferCodec.simple(ItemStack.class, org.bukkit.inventory.ItemStack.class,
                    CraftItemStack::asBukkitCopy, CraftItemStack::asNMSCopy,
                    (buffer, value) -> ItemStack.STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
                    buffer -> ItemStack.STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)))
            );
    public final BufferCodec<ItemStack, org.bukkit.inventory.ItemStack> OPTIONAL_ITEMSTACK_CODEC =
            registerCodec(BufferCodec.simple(ItemStack.class, org.bukkit.inventory.ItemStack.class,
                    CraftItemStack::asBukkitCopy, CraftItemStack::asNMSCopy,
                    (buffer, value) -> ItemStack.OPTIONAL_STREAM_CODEC.encode(toRegistryFriendlyByteBuf(buffer), value),
                    buffer -> ItemStack.OPTIONAL_STREAM_CODEC.decode(toRegistryFriendlyByteBuf(buffer)))
            );
    public final BufferCodec<GameType, GameMode> GAMEMODE_CODEC =
            registerCodec(BufferCodec.simple(GameType.class, GameMode.class,
                    gameType -> GameMode.getByValue(gameType.getId()), gameMode -> GameType.byId(gameMode.getValue()),
                    (buffer, value) -> GameType.STREAM_CODEC.encode(buffer.unwrap(), value),
                    buffer -> GameType.STREAM_CODEC.decode(buffer.unwrap()))
            );

    public RegistryFriendlyByteBuf toRegistryFriendlyByteBuf(SimpleByteBuf simpleByteBuf) {
        return new RegistryFriendlyByteBuf(simpleByteBuf.unwrap(), getDedicatedServer().registryAccess());
    }

    public RegistryFriendlyByteBuf toRegistryFriendlyByteBuf(FriendlyByteBuf simpleByteBuf) {
        return new RegistryFriendlyByteBuf(simpleByteBuf.unwrap(), getDedicatedServer().registryAccess());
    }

    public FriendlyByteBuf toFriendlyByteBuf(SimpleByteBuf simpleByteBuf) {
        return new FriendlyByteBuf(simpleByteBuf.unwrap());
    }

    @Override
    public Class<Packet> getNMSPacketClass() {
        return Packet.class;
    }

    @Override
    public Player asPlayer(ServerPlayer serverPlayer) {
        return serverPlayer.getBukkitEntity();
    }

    @Override
    @Nullable
    public Entity getEntityFromId(int id, World world) {
        final net.minecraft.world.entity.Entity entity = ((CraftWorld) world).getHandle().moonrise$getEntityLookup().get(id);
        if (entity == null)
            return null;
        return entity.getBukkitEntity();
    }

    @Override
    public Object rewriteMetadataPacketForGlow(Object metadataPacket) {
        ClientboundSetEntityDataPacket packet = (ClientboundSetEntityDataPacket) metadataPacket;

        final ArrayList<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>(packet.packedItems());
        if (dataValues.stream()
                .map(SynchedEntityData.DataValue::value)
                .filter(Byte.class::isInstance)
                .map(Byte.class::cast)
                .noneMatch(aByte -> aByte == (byte) 0x40))

            dataValues.add(new SynchedEntityData.DataValue<>(
                    0, EntityDataSerializers.BYTE, (byte) 0x40
            ));

        return new ClientboundSetEntityDataPacket(packet.id(), dataValues);
    }

    @Override
    public void setPlayerSpinAttack(Player player, int ticks) {
        ((CraftPlayer) player).getHandle().startAutoSpinAttack(ticks, 0, ItemStack.fromBukkitCopy(new org.bukkit.inventory.ItemStack(Material.TRIDENT)));
    }

    @Override
    public void setCubeFast(Location corner1, Location corner2, BlockData blockData) {

        if (corner1 == null || corner2 == null || blockData == null) return;
        if (corner1.getWorld() == null || corner2.getWorld() == null) return;
        if (!corner1.getWorld().equals(corner2.getWorld())) return;

        final Map<LevelChunk, List<BlockPos>> chunkMap = getChunkBlockPositions(corner1, corner2);
        if (chunkMap.isEmpty()) return;

        final BlockState state = ((CraftBlockData) blockData).getState();
        final World bukkitWorld = corner1.getWorld();

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        final ArrayList<int[]> touched = new ArrayList<>(chunkMap.size());

        for (Map.Entry<LevelChunk, List<BlockPos>> entry : chunkMap.entrySet()) {
            final LevelChunk chunk = entry.getKey();
            final List<BlockPos> blockPositions = entry.getValue();
            if (blockPositions == null || blockPositions.isEmpty()) continue;

            final int cx = chunk.locX;
            final int cz = chunk.locZ;

            for (int i = 0, size = blockPositions.size(); i < size; i++) {
                final BlockPos p = blockPositions.get(i);
                mutable.set(p.getX(), p.getY(), p.getZ());
                chunk.setBlockState(mutable, state, 206);
            }

            touched.add(new int[]{cx, cz});
        }

        for (int i = 0, size = touched.size(); i < size; i++) {
            final int[] c = touched.get(i);
            bukkitWorld.refreshChunk(c[0], c[1]);
        }
    }

    @Override
    public void replaceFast(Location corner1, Location corner2, BlockData oldBlockData, BlockData newBlockData) {
        if (corner1 == null || corner2 == null || oldBlockData == null || newBlockData == null) return;
        if (corner1.getWorld() == null || corner2.getWorld() == null) return;
        if (!corner1.getWorld().equals(corner2.getWorld())) return;

        final Map<LevelChunk, List<BlockPos>> chunkMap = getChunkBlockPositions(corner1, corner2);
        if (chunkMap.isEmpty()) return;

        final BlockState fromState = ((CraftBlockData) oldBlockData).getState();
        final BlockState toState   = ((CraftBlockData) newBlockData).getState();
        final World bukkitWorld = corner1.getWorld();

        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (Map.Entry<LevelChunk, List<BlockPos>> entry : chunkMap.entrySet()) {
            final LevelChunk chunk = entry.getKey();
            final List<BlockPos> positions = entry.getValue();
            if (positions == null || positions.isEmpty()) continue;

            final int cx = chunk.locX;
            final int cz = chunk.locZ;

            for (int i = 0, size = positions.size(); i < size; i++) {
                final BlockPos p = positions.get(i);

                if (chunk.getBlockState(p) != fromState) continue;

                mutable.set(p.getX(), p.getY(), p.getZ());

                chunk.setBlockState(mutable, toState, 260);
            }
            bukkitWorld.refreshChunk(cx, cz);
        }

    }

    @Override
    public ServerPlayer asServerPlayer(Player player) {
        return ((CraftPlayer)player).getHandle();
    }

    private static Map<LevelChunk, List<BlockPos>> getChunkBlockPositions(Location loc1, Location loc2) {
        final Map<LevelChunk, List<BlockPos>> chunkMap = new HashMap<>();

        final int x1 = loc1.getBlockX();
        final int y1 = loc1.getBlockY();
        final int z1 = loc1.getBlockZ();

        final int x2 = loc2.getBlockX();
        final int y2 = loc2.getBlockY();
        final int z2 = loc2.getBlockZ();

        final int minX = Math.min(x1, x2);
        final int minY = Math.min(y1, y2);
        final int minZ = Math.min(z1, z2);

        final int maxX = Math.max(x1, x2);
        final int maxY = Math.max(y1, y2);
        final int maxZ = Math.max(z1, z2);

        final org.bukkit.World bw = loc1.getWorld();
        final net.minecraft.world.level.Level nmsLevel = ((org.bukkit.craftbukkit.CraftWorld) bw).getHandle();

        final int minChunkX = minX >> 4;
        final int maxChunkX = maxX >> 4;
        final int minChunkZ = minZ >> 4;
        final int maxChunkZ = maxZ >> 4;

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            final int wx0 = Math.max(minX, cx << 4);
            final int wx1 = Math.min(maxX, (cx << 4) + 15);

            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                final int wz0 = Math.max(minZ, cz << 4);
                final int wz1 = Math.min(maxZ, (cz << 4) + 15);

                final net.minecraft.world.level.chunk.LevelChunk chunk =
                        nmsLevel.getChunk(cx, cz);

                final int cap = Math.max(16, (wx1 - wx0 + 1) * (maxY - minY + 1) * (wz1 - wz0 + 1));
                final ArrayList<BlockPos> list = new ArrayList<>(cap);
                chunkMap.put(chunk, list);

                for (int x = wx0; x <= wx1; x++) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = wz0; z <= wz1; z++) {
                            list.add(new BlockPos(x, y, z));
                        }
                    }
                }
            }
        }
        return chunkMap;
    }


    @Override
    public Class<ServerPlayer> getNMSServerPlayerClass() {
        return ServerPlayer.class;
    }

    @Override
    public ServerGamePacketListenerImpl getPlayerConnection(ServerPlayer serverPlayer) {
        return serverPlayer.connection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundBundlePacket toNMSClientBundlePacket(ClientBundlePacket from) {
        List<Packet<? super ClientGamePacketListener>> nmsPackets = new ArrayList<>();
        for (ClientboundPacket packet : from.getPackets())
            nmsPackets.add((Packet<ClientGamePacketListener>) packet.asNMS());
        return new ClientboundBundlePacket(nmsPackets);
    }

    @Override
    public ClientBundlePacket fromNMSClientBundlePacket(ClientboundBundlePacket from) {
        List<ClientboundPacket> packetList = new ArrayList<>();
        for (Packet<? super ClientGamePacketListener> subPacket : from.subPackets()) {
            it.jakegblp.lusk.nms.core.protocol.packets.Packet packet = NMS.fromNMSPacket(subPacket);
            if (packet instanceof ClientboundPacket clientboundPacket)
                packetList.add(clientboundPacket);
        }
        return new ClientBundlePacket(packetList);
    }

    @Override
    public Class<ClientboundBundlePacket> getNMSClientBundlePacketClass() {
        return ClientboundBundlePacket.class;
    }

    @Override
    public void sendPacketInternal(ServerGamePacketListenerImpl serverGamePacketListener, Packet packet) {
        serverGamePacketListener.send(packet);
    }

    @Override
    public Connection getConnection(ServerGamePacketListenerImpl serverGamePacketListener) {
        return serverGamePacketListener.connection;
    }

    @Override
    public Channel getChannel(Connection connection) {
        return connection.channel;
    }

    @Override
    public Team.Visibility toNMSTeamVisibility(org.bukkit.scoreboard.Team.OptionStatus optionStatus) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeByte(optionStatus.ordinal());
        return Team.Visibility.STREAM_CODEC.decode(friendlyByteBuf);
    }

    @Override
    public Team.CollisionRule toNMSTeamCollisionRule(org.bukkit.scoreboard.Team.OptionStatus optionStatus) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeByte(optionStatus.ordinal());
        return Team.CollisionRule.STREAM_CODEC.decode(friendlyByteBuf);
    }

    @Override
    public org.bukkit.scoreboard.Team.OptionStatus fromNMSTeamVisibility(Team.Visibility visibility) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        Team.Visibility.STREAM_CODEC.encode(friendlyByteBuf, visibility);
        return org.bukkit.scoreboard.Team.OptionStatus.values()[friendlyByteBuf.readInt()];
    }

    @Override
    public org.bukkit.scoreboard.Team.OptionStatus fromNMSTeamCollisionRule(Team.CollisionRule collisionRule) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        Team.CollisionRule.STREAM_CODEC.encode(friendlyByteBuf, collisionRule);
        return org.bukkit.scoreboard.Team.OptionStatus.values()[friendlyByteBuf.readInt()];
    }

    @Override
    public DedicatedServer getDedicatedServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ClientboundSetEntityDataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from) {
        return new ClientboundSetEntityDataPacket(
                from.getId(),
                CommonUtils.map(from.getEntityMetadata().items(),
                        item -> {
                            Object nmsValue = NMS.toNMSObject(item.value());
                            EntitySerializerKey.Type type = item.serializerType();
                            return new SynchedEntityData.DataValue<>(
                                    item.id(),
                                    (EntityDataSerializer<Object>) NMS.getEntityDataSerializer(
                                            NMS.getSerializableClass(item.valueClass()), type
                                    ), type == OPTIONAL ? Optional.ofNullable(nmsValue) : nmsValue);
                        })
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public EntityMetadataPacket fromNMSEntityMetadataPacket(ClientboundSetEntityDataPacket from) {
        List<SynchedEntityData.DataValue<?>> dataValueList = from.packedItems();
        EntityMetadata entityMetadata = new EntityMetadata();
        for (SynchedEntityData.DataValue<?> dataValue : dataValueList) {
            int id = dataValue.id();
            entityMetadata.setInternal(id, new MetadataItem<>(id, dataValue.value(), (EntitySerializerKey<Object>) NMS.getEntityDataSerializerKey(dataValue.serializer())));
        }
        return new EntityMetadataPacket(from.id(), entityMetadata);
    }

    @Override
    public Class<ClientboundSetEntityDataPacket> getNMSEntityMetadataPacketClass() {
        return ClientboundSetEntityDataPacket.class;
    }

    @Override
    public ClientboundPlayerInfoUpdatePacket toNMSPlayerInfoUpdatePacket(PlayerInfoUpdatePacket from) {
        EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = CommonUtils.copyOrEmptyEnumSet(CommonUtils.map(from.getActions(), this::toNMSPlayerInfoUpdatePacketAction), ClientboundPlayerInfoUpdatePacket.Action.class);
        return new ClientboundPlayerInfoUpdatePacket(actions,
                (List<ClientboundPlayerInfoUpdatePacket.Entry>) CommonUtils.map(from.getPlayerInfos(), playerInfo -> new ClientboundPlayerInfoUpdatePacket.Entry(
                        playerInfo.getUUID(),
                        convertIfNotNull(playerInfo.getPlayerProfile().getPlayerProfile(), PLAYER_PROFILE_CODEC::decode),
                        playerInfo.isListed(),
                        playerInfo.getLatency(),
                        convertIfNotNull(playerInfo.getGameMode(), GAMEMODE_CODEC::decode),
                        convertIfNotNull(playerInfo.getDisplayName(), COMPONENT_CODEC::decode),
                        playerInfo.isShowHat(),
                        playerInfo.getListOrder(),
                        convertIfNotNull(playerInfo.getChatSession(), this::toNMSChatSessionData)
                ))
        );
    }

    @Override
    public PlayerInfoUpdatePacket fromNMSPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket from) {
        var actions = CommonUtils.map(from.actions(), this::fromNMSPlayerInfoUpdatePacketAction);
        return new PlayerInfoUpdatePacket(Set.copyOf(actions), CommonUtils.map(from.entries(), entry -> new PlayerInfo(
                entry.profileId(),
                convertIfNotNull(entry.profile(), PLAYER_PROFILE_CODEC::encode),
                entry.listed(),
                entry.latency(),
                convertIfNotNull(entry.gameMode(), GAMEMODE_CODEC::encode),
                convertIfNotNull(entry.displayName(), COMPONENT_CODEC::encode),
                entry.showHat(),
                entry.listOrder(),
                convertIfNotNull(entry.chatSession(), this::fromNMSChatSessionData)
        )));
    }

    @Override
    public Class<ClientboundPlayerInfoUpdatePacket> getNMSPlayerInfoUpdatePacketClass() {
        return ClientboundPlayerInfoUpdatePacket.class;
    }

    @Override
    public ClientboundPlayerInfoUpdatePacket.Action toNMSPlayerInfoUpdatePacketAction(PlayerInfoUpdatePacket.Action<?> from) {
        return ClientboundPlayerInfoUpdatePacket.Action.valueOf(from.name());
    }

    @Override
    public PlayerInfoUpdatePacket.Action<?> fromNMSPlayerInfoUpdatePacketAction(ClientboundPlayerInfoUpdatePacket.Action from) {
        return PlayerInfoUpdatePacket.Action.valueOf(from.name());
    }

    @Override
    public Class<ClientboundPlayerInfoUpdatePacket.Action> getNMSPlayerInfoUpdatePacketActionClass() {
        return ClientboundPlayerInfoUpdatePacket.Action.class;
    }

    @Override
    public ClientboundSystemChatPacket toNMSSystemChatPacket(SystemChatPacket from) {
        return null;
    }

    @Override
    public SystemChatPacket fromNMSSystemChatPacket(ClientboundSystemChatPacket from) {
        return null;
    }

    @Override
    public TeamPacket fromNMSSetPlayerTeamPacket(ClientboundSetPlayerTeamPacket from) {
        ClientboundSetPlayerTeamPacket.Action
                teamAction = from.getTeamAction(),
                playerAction = from.getPlayerAction();
        int method;
        if (teamAction != null) {
            method = switch (teamAction) {
                case ADD -> 0;
                case REMOVE -> 1;
            };
        } else if (playerAction != null) {
            method = switch (playerAction) {
                case ADD -> 3;
                case REMOVE -> 4;
            };
        } else
            method = 0;
        return TeamPacket.fromMethod(method, from.getName(), fromNMSTeamParameters(from.getParameters().orElse(null)), new HashSet<>(from.getPlayers()));
    }

    @Override
    public Class<ClientboundSetPlayerTeamPacket> getNMSSetPlayerTeamPacketClass() {
        return ClientboundSetPlayerTeamPacket.class;
    }

    @Override
    @Contract("null -> null")
    public TeamParameters fromNMSTeamParameters(@Nullable ClientboundSetPlayerTeamPacket.Parameters from) {
        return from == null ? null : new TeamParameters(
                COMPONENT_CODEC.encode(from.getDisplayName()),
                COMPONENT_CODEC.encode(from.getPlayerPrefix()),
                COMPONENT_CODEC.encode(from.getPlayerSuffix()),
                fromNMSTeamVisibility(from.getNametagVisibility()),
                fromNMSTeamCollisionRule(from.getCollisionRule()),
                NamedTextColor.NAMES.value(from.getColor().getName()),
                from.getOptions());
    }

    @Override
    public ClientboundSetCameraPacket toNMSSetCameraPacket(SetCameraPacket from) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeVarInt(from.getCameraId());
        return ClientboundSetCameraPacket.STREAM_CODEC.decode(friendlyByteBuf);
    }

    @Override
    public SetCameraPacket fromNMSSetCameraPacket(ClientboundSetCameraPacket from) {
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        ClientboundSetCameraPacket.STREAM_CODEC.encode(friendlyByteBuf, from);
        return new SetCameraPacket(friendlyByteBuf.readInt());
    }

    @Override
    public Class<ClientboundSetCameraPacket> getNMSSetCameraPacketClass() {
        return ClientboundSetCameraPacket.class;
    }


    // leave space here for Poa to read


    @Override
    public ClientboundSetPlayerTeamPacket toNMSSetPlayerTeamPacket(TeamPacket from) {
        var registryFriendlyByteBuf = new RegistryFriendlyByteBuf(Unpooled.buffer(), getDedicatedServer().registryAccess());
        registryFriendlyByteBuf.writeUtf(from.getName());
        registryFriendlyByteBuf.writeByte(from.getMethod());
        if (from instanceof TeamPacket.ChangeTeamInfo changeTeamInfo)
            toNMSTeamParameters(changeTeamInfo.getParameters()).write(registryFriendlyByteBuf);
        if (from instanceof TeamPacket.WithMembers withMembers)
            registryFriendlyByteBuf.writeCollection(withMembers.getMembers(), FriendlyByteBuf::writeUtf);
        return ClientboundSetPlayerTeamPacket.STREAM_CODEC.decode(registryFriendlyByteBuf);
    }


    @Override
    public Class<ClientboundSystemChatPacket> getNMSSystemChatPacketClass() {
        return ClientboundSystemChatPacket.class;
    }


    @Override
    public ClientboundUpdateAttributesPacket toNMSAttributePacket(AttributePacket from) {
        final List<AttributeInstance> instances = new ArrayList<>();


        for (AttributeSnapshot attribute : from.getAttributes()) {
            final Holder<Attribute> holder = CraftAttribute.bukkitToMinecraftHolder(attribute.getAttribute()); //todo test on 1.19.4
            final AttributeInstance attributeInstance = new AttributeInstance(holder, a -> {
            });

            Collection<net.minecraft.world.entity.ai.attributes.AttributeModifier> modifiers =
                    attribute.getModifiers()
                            .stream()
                            .map(CraftAttributeInstance::convert)
                            .toList();
            attributeInstance.addPermanentModifiers(modifiers);
            attributeInstance.setBaseValue(attribute.getBase());
            instances.add(attributeInstance);
        }


        return new ClientboundUpdateAttributesPacket(from.getId(), instances);
    }

    @Override
    public ClientboundSetPlayerTeamPacket.Parameters toNMSTeamParameters(TeamParameters from) {
        var registryFriendlyByteBuf = new RegistryFriendlyByteBuf(Unpooled.buffer(), getDedicatedServer().registryAccess());
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(registryFriendlyByteBuf, COMPONENT_CODEC.decode(from.getDisplayName()));
        registryFriendlyByteBuf.writeByte(from.getOptions());
        Team.Visibility.STREAM_CODEC.encode(registryFriendlyByteBuf, toNMSTeamVisibility(from.getNametagVisibility()));
        Team.CollisionRule.STREAM_CODEC.encode(registryFriendlyByteBuf, toNMSTeamCollisionRule(from.getCollisionRule()));
        registryFriendlyByteBuf.writeVarInt(ChatFormatting.getByName(NamedTextColor.NAMES.key(from.getColor())).getId());
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(registryFriendlyByteBuf, COMPONENT_CODEC.decode(from.getPlayerPrefix()));
        ComponentSerialization.TRUSTED_STREAM_CODEC.encode(registryFriendlyByteBuf, COMPONENT_CODEC.decode(from.getPlayerSuffix()));
        return new ClientboundSetPlayerTeamPacket.Parameters(registryFriendlyByteBuf);
    }


    @SneakyThrows
    @Override
    public AttributePacket fromNMSAttributePacket(ClientboundUpdateAttributesPacket from) {
        List<AttributeSnapshot> list = new ArrayList<>();
        for (ClientboundUpdateAttributesPacket.AttributeSnapshot value : from.getValues()) {
            final org.bukkit.attribute.Attribute attribute = CraftAttribute.minecraftHolderToBukkit(value.attribute());

            List<org.bukkit.attribute.AttributeModifier> modifiers = value.modifiers().stream().map(CraftAttributeInstance::convert).toList();

            list.add(new AttributeSnapshot(attribute, value.base(), modifiers));
        }
        return new AttributePacket(from.getEntityId(), list);
    }

    @Override
    public Class<ClientboundUpdateAttributesPacket> getNMSAttributePacketClass() {
        return ClientboundUpdateAttributesPacket.class;
    }

    public Class<ClientboundSetPlayerTeamPacket.Parameters> getNMSTeamParametersClass() {
        return ClientboundSetPlayerTeamPacket.Parameters.class;
    }


    @Override
    public ClientboundEntityEventPacket toNMSEntityEventPacket(EntityEventPacket from) {
        final FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        friendlyByteBuf.writeInt(from.getEntityID());
        friendlyByteBuf.writeByte(from.getEventID());

        return ClientboundEntityEventPacket.STREAM_CODEC.decode(friendlyByteBuf);
    }


    @Override
    public EntityEventPacket fromNMSEntityEventPacket(ClientboundEntityEventPacket from) {
        final FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        ClientboundEntityEventPacket.STREAM_CODEC.encode(friendlyByteBuf, from);

        return new EntityEventPacket(friendlyByteBuf.readInt(), from.getEventId());
    }

    @Override
    public Class<ClientboundEntityEventPacket> getNMSEntityEventPacketClass() {
        return ClientboundEntityEventPacket.class;
    }


    @Override
    public ClientboundEntityPositionSyncPacket toNMSTeleportPacket(TeleportPacket from) {

        final PositionMoveRotation positionMoveRotation = new PositionMoveRotation(new Vec3(from.getX(), from.getY(), from.getZ()), new Vec3(0, 0, 0), from.getYaw(), from.getPitch());

        return new ClientboundEntityPositionSyncPacket(from.getEntityID(), positionMoveRotation, from.isOnGround());
    }

    @Override
    public TeleportPacket fromNMSTeleportPacket(ClientboundEntityPositionSyncPacket from) {
        final PositionMoveRotation values = from.values();
        final Vec3 position = values.position();
        return new TeleportPacket(from.id(), position.x(), position.y(), position.z(), values.yRot(), values.xRot(), from.onGround());
    }


    @Override
    public Class<ClientboundEntityPositionSyncPacket> getNMSTeleportPacketClass() {
        return ClientboundEntityPositionSyncPacket.class;
    }


    @Override
    public ClientboundBlockUpdatePacket toNMSBlockUpdatePacket(BlockUpdatePacket from) {
        return new ClientboundBlockUpdatePacket(BLOCK_VECTOR_CODEC.decode(from.getBlockPos()), ((CraftBlockData) from.getBlockState()).getState());
    }

    @Override
    public BlockUpdatePacket fromNMSBlockUpdatePacket(ClientboundBlockUpdatePacket from) {
        final BlockPos pos = from.getPos();
        return new BlockUpdatePacket(new BlockVector(pos.getX(), pos.getY(), pos.getZ()), CraftBlockData.fromData(from.getBlockState()));
    }

    @Override
    public Class<ClientboundBlockUpdatePacket> getNMSBlockUpdatePacketClass() {
        return ClientboundBlockUpdatePacket.class;
    }

    @Override
    public SoundEvent toNMSSound(Sound from) {
        return toNMSSoundHolder(from).value();
    }

    @Override
    public Sound fromNMSSound(SoundEvent from) {
        return CraftSound.minecraftToBukkit(from);
    }

    @Override
    public Holder<SoundEvent> toNMSSoundHolder(Sound from) {
        return CraftSound.bukkitToMinecraftHolder(from);
    }

    @Override
    public Sound fromNMSSoundHolder(Holder<SoundEvent> from) {
        return CraftSound.minecraftHolderToBukkit(from);
    }

    @Override
    public Class<SoundEvent> getNMSSoundClass() {
        return SoundEvent.class;
    }

    @Override
    public ClientboundSoundPacket toNMSSoundPacket(SoundPacket from) {
        return new ClientboundSoundPacket(
                toNMSSoundHolder(from.getSound()),
                SoundSource.valueOf(from.getSoundSource().name()),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getVolume(),
                from.getPitch(),
                from.getSeed());
    }

    @Override
    public SoundPacket fromNMSSoundPacket(ClientboundSoundPacket from) {
        return new SoundPacket(
                fromNMSSoundHolder(from.getSound()),
                SoundCategory.valueOf(from.getSource().getName().toUpperCase()),
                from.getX(),
                from.getY(),
                from.getZ(),
                from.getVolume(),
                from.getPitch(),
                from.getSeed());
    }

    @Override
    public Class<ClientboundSoundPacket> getNMSSoundPacketClass() {
        return ClientboundSoundPacket.class;
    }


    @Override
    public ClientboundSoundEntityPacket toNMSSoundEntityPacket(SoundEntityPacket from) {
        return new ClientboundSoundEntityPacket(CraftSound.bukkitToMinecraftHolder(from.getSound()),
                SoundSource.valueOf(from.getSoundSource().toString().toUpperCase()),
                ((CraftEntity) from.getEntity()).getHandle(),
                from.getVolume(),
                from.getPitch(),
                from.getSeed()
        );
    }

    @Override
    public SoundEntityPacket fromNMSSoundEntityPacket(ClientboundSoundEntityPacket from) {
        return new SoundEntityPacket(CraftSound.minecraftHolderToBukkit(from.getSound()),
                SoundCategory.valueOf(from.getSource().toString().toUpperCase()),
                from.getId(),
                from.getVolume(),
                from.getPitch(),
                from.getSeed()
        );
    }

    @Override
    public Class<ClientboundSoundEntityPacket> getNMSSoundEntityPacketClass() {
        return ClientboundSoundEntityPacket.class;
    }

    @Override
    public ClientboundSetEquipmentPacket toNMSSetEquipmentPacket(SetEquipmentPacket from) {
        List<Pair<EquipmentSlot, ItemStack>> list = fromMapToPairList(from.getEquipment(), EQUIPMENTSLOT_CODEC::decode, ITEMSTACK_CODEC::decode);
        return new ClientboundSetEquipmentPacket(from.getId(), list);
    }

    @Override
    public SetEquipmentPacket fromNMSSetEquipmentPacket(ClientboundSetEquipmentPacket from) {
        return new SetEquipmentPacket(from.getEntity(), fromPairListToMap(from.getSlots(), EQUIPMENTSLOT_CODEC::encode, ITEMSTACK_CODEC::encode));
    }

    @Override
    public Class<ClientboundSetEquipmentPacket> getNMSSetEquipmentPacketClass() {
        return ClientboundSetEquipmentPacket.class;
    }


    @Override
    public ClientboundLevelParticlesPacket toNMSLevelParticle(LevelParticlePacket from) {
        return new ClientboundLevelParticlesPacket((ParticleOptions) from.getParticle(), from.isOverrideLimiter(), from.isAlwaysShow(), from.getX(), from.getY(), from.getZ(), from.getXDist(), from.getYDist(), from.getZDist(), from.getMaxSpeed(), from.getCount());
    }

    @Override
    public LevelParticlePacket fromNMSLevelParticle(ClientboundLevelParticlesPacket from) {
        final ParticleType<?> particle = from.getParticle().getType();
        final Particle bukkitParticle = CraftParticle.minecraftToBukkit(particle);

        return new LevelParticlePacket(from.getX(), from.getY(), from.getZ(), from.getXDist(), from.getYDist(), from.getZDist(), from.getMaxSpeed(), from.getCount(), from.isOverrideLimiter(), from.alwaysShow(), new it.jakegblp.lusk.nms.core.world.level.ParticleOptions(bukkitParticle));
    }

    @Override
    public Class<ClientboundLevelParticlesPacket> getNMSLevelParticleClass() {
        return ClientboundLevelParticlesPacket.class;
    }

    @Override
    public RemoteChatSession.Data toNMSChatSessionData(ChatSessionData from) {
        var key = from.profilePublicKey();
        return new RemoteChatSession.Data(from.sessionId(), new ProfilePublicKey.Data(key.timestamp(), key.publicKey(), key.signature()));
    }

    @Override
    public ChatSessionData fromNMSChatSessionData(RemoteChatSession.Data from) {
        var data = from.profilePublicKey();
        return new ChatSessionData(from.sessionId(), new it.jakegblp.lusk.nms.core.world.player.ProfilePublicKey(data.expiresAt(), data.key(), data.keySignature()));
    }

    @Override
    public Class<RemoteChatSession.Data> getNMSChatSessionDataClass() {
        return RemoteChatSession.Data.class;
    }

}
