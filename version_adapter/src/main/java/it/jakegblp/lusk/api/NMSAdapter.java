package it.jakegblp.lusk.api;

import io.netty.channel.*;
import it.jakegblp.lusk.api.enums.EntityAnimation;
import it.jakegblp.lusk.api.packets.NMSPlayer;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static it.jakegblp.lusk.utils.ReflectionUtils.getFieldValue;

/**
 * Main class for NMS-related methods.
 * @param <NMSMinecraftServer> NMS Minecraft Server class
 * @param <NMSPacket> NMS Packet class
 * @param <NMSEntityType> NMS Entity Type class
 * @param <NMSWorld> NMS World class
 * @param <NMSServerPlayer> NMS Server Player class
 * @param <NMSIronGolem> NMS Iron Golem class
 * @param <NMSEntity> NMS Entity class
 * @param <NMSGameProfile> NMS Game Profile class
 * @param <NMSVector3> NMS Vector class
 * @param <NMSVector3i> NMS Vector (int) class
 * @param <NMSBlockPos> NMS Block Location class
 * @param <NMSBlock> NMS Block class
 */
public interface NMSAdapter<
        NMSNamespacedKey,
        NMSMinecraftServer,
        NMSPacket,
        NMSEntityType,
        NMSWorld,
        NMSServerPlayer extends NMSEntity,
        NMSIronGolem extends NMSEntity,
        NMSLivingEntity extends NMSEntity,
        NMSEntity,
        NMSGameProfile,
        NMSVector3,
        NMSVector3i,
        NMSBlockPos extends NMSVector3i,
        NMSBlock,
        NMSConnection extends SimpleChannelInboundHandler<NMSPacket>,
        NMSCommonPacketListener
        > {

    record ObfuscatedFields(String playerPacketListener) {}

    /*
    Methods related to obfuscation.
    */
    @NotNull
    ObfuscatedFields getObfuscatedFields();

    /*
    Handles Bukkit to NMS conversions.
    */
    NMSNamespacedKey getNMSNamespacedKey(NamespacedKey key);
    NMSWorld getNMSWorld(World world);
    NMSBlock getNMSBlock(Block block);
    NMSEntity getNMSEntity(Entity entity);
    NMSLivingEntity getNMSLivingEntity(LivingEntity entity);
    NMSIronGolem getNMSIronGolem(IronGolem ironGolem);
    NMSVector3 getNMSVector(Vector vector);
    NMSBlockPos getNMSBlockPos(BlockVector blockVector);
    NMSEntityType getNMSEntityType(EntityType entityType);
    NMSServerPlayer getNMSServerPlayer(Player player);
    default NMSCommonPacketListener getPlayerPacketListener(Player player) {
        return getPlayerPacketListener(getNMSServerPlayer(player));
    }
    NMSCommonPacketListener getPlayerPacketListener(NMSServerPlayer serverPlayer);
    default NMSConnection getPlayerConnection(Player player) {
        return getPlayerConnection(getNMSServerPlayer(player));
    }
    @SuppressWarnings("unchecked")
    default NMSConnection getPlayerConnection(NMSServerPlayer serverPlayer) {
        NMSCommonPacketListener packetListener = getPlayerPacketListener(serverPlayer);
        return (NMSConnection) getFieldValue(packetListener, packetListener.getClass().getSuperclass(), getObfuscatedFields().playerPacketListener, Object.class);
    }
    @Nullable
    default Channel getConnectionChannel(NMSServerPlayer serverPlayer) {
        return getConnectionChannel(getPlayerConnection(serverPlayer));
    }
    @Nullable
    default Channel getConnectionChannel(Player player) {
        return getConnectionChannel(getPlayerConnection(player));
    }
    @Nullable
    Channel getConnectionChannel(@NotNull NMSConnection connection);

    /*
    Handles getting and setting NMS properties of Bukkit objects (or static-like).
    */
    NMSMinecraftServer getNMSServer();
    int getEntityId(Entity entity);
    void setEntityId(Entity entity, int id);

    /*
    Handles creating new NMS-related objects.
    */
    NMSServerPlayer ServerPlayer(NMSMinecraftServer server, NMSWorld level, NMSGameProfile gameProfile);
    @NotNull NMSPlayer<NMSServerPlayer> NMSPlayer(@NotNull String name, @NotNull Location loc, boolean update);
    NMSGameProfile GameProfile(UUID uuid, String name);

    /*
    Handles actions only (or preferably) possible with NMS.
    */
    default void offerFlower(IronGolem ironGolem, boolean shouldOffer) {
        offerFlower(getNMSIronGolem(ironGolem), shouldOffer);
    }
    void offerFlower(NMSIronGolem ironGolem, boolean shouldOffer);
    default void travel(LivingEntity livingEntity, Vector vector) {
        travel(getNMSLivingEntity(livingEntity), getNMSVector(vector));
    }
    void travel(NMSLivingEntity nmsLivingEntity, NMSVector3 nmsVector3);

    /*
    Mostly packet related methods.
    */
    default void sendPacket(NMSPacket packet, Player... players) {
        for (Player player : players) sendPacket(packet, getNMSServerPlayer(player));
    }

    void sendPacket(NMSPacket packet, NMSServerPlayer player);

    void sendEntityAnimationPacket(Entity entity, EntityAnimation animation, Player... players);

    /**
     * Sends a block destruction packet to the given player.
     * @param id packet id, null for random
     * @param block block
     * @param progress progress 0-9, any other value removes it
     */
    void sendBlockDestructionPacket(@Nullable Integer id, Block block, int progress, Player... players);

    default void sendEntitySpawnPacket(@Nullable Integer id, UUID uuid, EntityType entityType, Vector location, float pitch, float yaw, double headPitch, int data, Vector velocity, Player... players) {
        sendEntitySpawnPacket(id, uuid, getNMSEntityType(entityType), getNMSVector(location), pitch, yaw, headPitch, data, getNMSVector(velocity), players);
    }

    void sendEntitySpawnPacket(@Nullable Integer id, UUID uuid, NMSEntityType entityType, NMSVector3 location, float pitch, float yaw, double headPitch, int data, NMSVector3 velocity, Player... players);

    default void sendEntitySpawnPacket(Entity entity, int data, Vector vector, Player... players) {
        sendEntitySpawnPacket(getNMSEntity(entity), data, getNMSVector(vector), players);
    }

    void sendEntitySpawnPacket(NMSEntity entity, int data, NMSVector3 blockVector, Player... players);
    //void sendEntitySpawnPacket(NMSEntity entity, int data, NMSVector3 blockVector, List<NMSServerPlayer> players);

    @ApiStatus.Experimental
    void sendEntityMetadataPacket(int id, Player... players);

    @ApiStatus.Experimental
    void sendPlayerActionPacket(Player... players);

    default NMSPlayer<NMSServerPlayer> spawnPlayer(@NotNull String name, @NotNull Location location, boolean update, Player... players) {
        NMSPlayer<NMSServerPlayer> nmsPlayer = NMSPlayer(name, location, update);
        for (Player player : players) {
            nmsPlayer.update(player);
        }
        return nmsPlayer;
    }

    void handleIncomingPacket(ChannelHandlerContext ctx, Object msg);

    void handleOutgoingPacket(ChannelHandlerContext ctx, Object msg, ChannelPromise promise);

    default void injectPlayer(Player player) {
        Channel channel = getConnectionChannel(getNMSServerPlayer(player));
        if (channel == null) return;
        channel.pipeline().addBefore("packet_handler", player.getName(),
                new ChannelDuplexHandler() {

                    @Override
                    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
                        handleIncomingPacket(ctx, msg);
                        super.channelRead(ctx, msg);
                    }

                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        handleOutgoingPacket(ctx, msg, promise);
                        super.write(ctx, msg, promise);
                    }

                });
    }
}
