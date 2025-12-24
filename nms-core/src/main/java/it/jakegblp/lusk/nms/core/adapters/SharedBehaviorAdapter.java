package it.jakegblp.lusk.nms.core.adapters;

import io.netty.channel.*;
import it.jakegblp.lusk.nms.core.events.*;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.util.BufferCodec;
import it.jakegblp.lusk.nms.core.util.TypedBufferCodec;
import it.jakegblp.lusk.nms.core.world.level.LevelUtil;
import it.jakegblp.lusk.nms.core.world.player.ChatSessionData;
import it.jakegblp.lusk.nms.core.world.player.GlowMap;
import lombok.SneakyThrows;
import it.jakegblp.lusk.nms.core.world.player.TeamParameters;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface SharedBehaviorAdapter<
        NMSServerPlayer,
        NMSPacket,
        NMSChatSessionData,
        NMSServerGamePacketListenerImpl,
        NMSConnection,
        NMSTeamVisibility,
        NMSTeamCollisionRule,
        NMSDedicatedServer,
        NMSClientBundlePacket,
        NMSEntityMetadataPacket,
        NMSPlayerInfoUpdatePacket,
        NMSPlayerInfoUpdatePacketAction,
        NMSSystemChatPacket,
        NMSLevelParticlePacket,
        NMSAttributePacket,
        NMSSetCameraPacket,
        NMSSetPlayerTeamPacket,
        NMSTeamParameters,
        NMSEntityEventPacket,
        NMSTeleportPacket,
        NMSBlockUpdatePacket,
        NMSSound,
        NMSSoundHolder,
        NMSSoundPacket,
        NMSSoundEntityPacket,
        NMSSetEquipmentPacket
        > {

    Map<Class<?>, BufferCodec<?, ?>> fromNms = new HashMap<>();
    Map<Class<?>, BufferCodec<?, ?>> toNms = new HashMap<>();

    @NullMarked
    default <F, T> BufferCodec<F, T> registerCodec(
            Class<F> from,
            Class<T> to,
            BufferCodec.Writer<F> writeFrom,
            BufferCodec.Reader<F> readFrom,
            BufferCodec.Writer<T> writeTo,
            BufferCodec.Reader<T> readTo
    ) {
        return registerCodec(BufferCodec.of(from, to, writeFrom, readFrom, writeTo, readTo));
    }

    @NullMarked
    default <F> BufferCodec<F, F> registerCodec(Class<F> from, BufferCodec<F, F> codec) {
        fromNms.put(from, codec);
        toNms.put(from, codec);
        return codec;
    }

    @NullMarked
    default <F, T> BufferCodec<F, T> registerCodec(Class<F> from, Class<T> to, BufferCodec<F, T> codec) {
        fromNms.put(from, codec);
        toNms.put(to, codec);
        return codec;
    }

    @NullMarked
    default <C extends TypedBufferCodec<F, T>, F, T> C registerCodec(C codec) {
        fromNms.put(codec.getFromClass(), codec);
        toNms.put(codec.getToClass(), codec);
        return codec;
    }

    @SuppressWarnings("unchecked")
    default <F, T> BufferCodec<F, T> getCodec(Class<F> type) {
        return (BufferCodec<F, T>) fromNms.getOrDefault(type, toNms.get(type));
    }


    @SuppressWarnings("unchecked")
    default <F, T> @Nullable T fromNMS(@NotNull F f) {
        BufferCodec<F, T> codec = (BufferCodec<F, T>) fromNms.get(f.getClass());
        if (codec == null) return null;
        return codec.encode(f);
    }

    @SuppressWarnings("unchecked")
    default <F, T> F toNMS(@NotNull T t) {
        BufferCodec<F, T> codec = (BufferCodec<F, T>) toNms.get(t.getClass());
        if (codec == null) return null;
        return codec.decode(t);
    }

    Class<NMSPacket> getNMSPacketClass();

    default boolean isNMSPacket(Object object) {
        return getNMSPacketClass().isInstance(object);
    }

    Player asPlayer(NMSServerPlayer serverPlayer);

    Entity getEntityFromId(int id, World world);

    Object rewriteMetadataPacketForGlow(Object metadataPacket);

    void setPlayerSpinAttack(Player player, int ticks);

    void setCubeFast(Location corner1, Location corner2, BlockData blockData);

    void replaceFast(Location corner1, Location corner2, BlockData oldBlockData, BlockData newBlockData);

    NMSServerPlayer asServerPlayer(Player player);

    Class<NMSServerPlayer> getNMSServerPlayerClass();

    NMSServerGamePacketListenerImpl getPlayerConnection(NMSServerPlayer serverPlayer);

    default NMSServerGamePacketListenerImpl getPlayerConnection(Player player) {
        return getPlayerConnection(asServerPlayer(player));
    }

    NMSClientBundlePacket toNMSClientBundlePacket(ClientBundlePacket from);

    ClientBundlePacket fromNMSClientBundlePacket(NMSClientBundlePacket from);

    Class<NMSClientBundlePacket> getNMSClientBundlePacketClass();

    default boolean isNMSClientBundlePacket(Object object) {
        return getNMSClientBundlePacketClass().isInstance(object);
    }

    NMSEntityMetadataPacket toNMSEntityMetadataPacket(EntityMetadataPacket from);

    EntityMetadataPacket fromNMSEntityMetadataPacket(NMSEntityMetadataPacket from);

    Class<NMSEntityMetadataPacket> getNMSEntityMetadataPacketClass();

    default boolean isNMSEntityMetadataPacket(Object object) {
        return getNMSEntityMetadataPacketClass().isInstance(object);
    }

    NMSPlayerInfoUpdatePacket toNMSPlayerInfoUpdatePacket(PlayerInfoUpdatePacket from);

    PlayerInfoUpdatePacket fromNMSPlayerInfoUpdatePacket(NMSPlayerInfoUpdatePacket from);

    Class<NMSPlayerInfoUpdatePacket> getNMSPlayerInfoUpdatePacketClass();

    default boolean isNMSPlayerInfoUpdatePacket(Object object) {
        return getNMSPlayerInfoUpdatePacketClass().isInstance(object);
    }

    NMSPlayerInfoUpdatePacketAction toNMSPlayerInfoUpdatePacketAction(PlayerInfoUpdatePacket.Action<?> from);

    PlayerInfoUpdatePacket.Action<?> fromNMSPlayerInfoUpdatePacketAction(NMSPlayerInfoUpdatePacketAction from);

    Class<NMSPlayerInfoUpdatePacketAction> getNMSPlayerInfoUpdatePacketActionClass();

    default boolean isNMSPlayerInfoUpdatePacketAction(Object object) {
        return getNMSPlayerInfoUpdatePacketActionClass().isInstance(object);
    }


    //Please leave spaces so Poa can read this

    NMSSystemChatPacket toNMSSystemChatPacket(SystemChatPacket from);

    SystemChatPacket fromNMSSystemChatPacket(NMSSystemChatPacket from);

    Class<NMSSystemChatPacket> getNMSSystemChatPacketClass();

    default boolean isNMSSystemChatPacket(Object object) {
        return getNMSSystemChatPacketClass().isInstance(object);
    }

    //


    NMSAttributePacket toNMSAttributePacket(AttributePacket from);

    AttributePacket fromNMSAttributePacket(NMSAttributePacket from);

    Class<NMSAttributePacket> getNMSAttributePacketClass();

    default boolean isNMSAttributePacket(Object object) {
        return getNMSAttributePacketClass().isInstance(object);
    }


    NMSLevelParticlePacket toNMSLevelParticle(LevelParticlePacket from);

    LevelParticlePacket fromNMSLevelParticle(NMSLevelParticlePacket from);

    Class<NMSLevelParticlePacket> getNMSLevelParticleClass();

    default boolean isNMSLevelParticle(Object object) {
        return getNMSLevelParticleClass().isInstance(object);
    }


    NMSSetPlayerTeamPacket toNMSSetPlayerTeamPacket(TeamPacket from);

    TeamPacket fromNMSSetPlayerTeamPacket(NMSSetPlayerTeamPacket from);

    Class<NMSSetPlayerTeamPacket> getNMSSetPlayerTeamPacketClass();

    default boolean isNMSSetPlayerTeamPacket(Object object) {
        return getNMSSetPlayerTeamPacketClass().isInstance(object);
    }

    NMSTeamParameters toNMSTeamParameters(TeamParameters from);

    TeamParameters fromNMSTeamParameters(NMSTeamParameters from);

    Class<NMSTeamParameters> getNMSTeamParametersClass();

    default boolean isNMSTeamParameters(Object object) {
        return getNMSTeamParametersClass().isInstance(object);
    }


    NMSEntityEventPacket toNMSEntityEventPacket(EntityEventPacket from);

    EntityEventPacket fromNMSEntityEventPacket(NMSEntityEventPacket from);

    Class<NMSEntityEventPacket> getNMSEntityEventPacketClass();

    default boolean isNMSEntityEventPacket(Object object) {
        return getNMSEntityEventPacketClass().isInstance(object);
    }


    NMSTeleportPacket toNMSTeleportPacket(TeleportPacket from);

    TeleportPacket fromNMSTeleportPacket(NMSTeleportPacket from);

    Class<NMSTeleportPacket> getNMSTeleportPacketClass();

    default boolean isNMSTeleportPacket(Object object) {
        return getNMSTeleportPacketClass().isInstance(object);
    }


    NMSBlockUpdatePacket toNMSBlockUpdatePacket(BlockUpdatePacket from);

    BlockUpdatePacket fromNMSBlockUpdatePacket(NMSBlockUpdatePacket from);

    Class<NMSBlockUpdatePacket> getNMSBlockUpdatePacketClass();

    default boolean isNMSBlockUpdatePacket(Object object) {
        return getNMSBlockUpdatePacketClass().isInstance(object);
    }

    NMSSound toNMSSound(Sound from);

    Sound fromNMSSound(NMSSound from);

    NMSSoundHolder toNMSSoundHolder(Sound from);

    Sound fromNMSSoundHolder(NMSSoundHolder from);

    Class<NMSSound> getNMSSoundClass();

    default boolean isNMSSound(Object object) {
        return getNMSSoundClass().isInstance(object);
    }


    NMSSoundPacket toNMSSoundPacket(SoundPacket from);

    SoundPacket fromNMSSoundPacket(NMSSoundPacket from);

    Class<NMSSoundPacket> getNMSSoundPacketClass();

    default boolean isNMSSoundPacket(Object object) {
        return getNMSSoundPacketClass().isInstance(object);
    }


    NMSSoundEntityPacket toNMSSoundEntityPacket(SoundEntityPacket from);

    SoundEntityPacket fromNMSSoundEntityPacket(NMSSoundEntityPacket from);

    Class<NMSSoundEntityPacket> getNMSSoundEntityPacketClass();

    default boolean isNMSSoundEntityPacket(Object object) {
        return getNMSSoundEntityPacketClass().isInstance(object);
    }

    NMSSetEquipmentPacket toNMSSetEquipmentPacket(SetEquipmentPacket from);

    SetEquipmentPacket fromNMSSetEquipmentPacket(NMSSetEquipmentPacket from);

    Class<NMSSetEquipmentPacket> getNMSSetEquipmentPacketClass();

    default boolean isNMSSetEquipmentPacket(Object object) {
        return getNMSSetEquipmentPacketClass().isInstance(object);
    }

    NMSSetCameraPacket toNMSSetCameraPacket(SetCameraPacket from);

    SetCameraPacket fromNMSSetCameraPacket(NMSSetCameraPacket from);

    Class<NMSSetCameraPacket> getNMSSetCameraPacketClass();

    default boolean isNMSSetCameraPacket(Object object) {
        return getNMSSetCameraPacketClass().isInstance(object);
    }

    NMSChatSessionData toNMSChatSessionData(ChatSessionData from);

    ChatSessionData fromNMSChatSessionData(NMSChatSessionData from);

    Class<NMSChatSessionData> getNMSChatSessionDataClass();

    default boolean isNMSChatSessionData(Object object) {
        return getNMSChatSessionDataClass().isInstance(object);
    }

    default void sendPacket(Player player, NMSPacket packet) {
        sendPacket(asServerPlayer(player), packet);
    }

    default void sendPacket(NMSServerPlayer player, NMSPacket packet) {
        sendPacketInternal(getPlayerConnection(player), packet);
    }

    void sendPacketInternal(NMSServerGamePacketListenerImpl packetListener, NMSPacket packet);

    NMSConnection getConnection(NMSServerGamePacketListenerImpl packetListener);

    Channel getChannel(NMSConnection connection);

    NMSTeamVisibility toNMSTeamVisibility(Team.OptionStatus optionStatus);

    NMSTeamCollisionRule toNMSTeamCollisionRule(Team.OptionStatus optionStatus);

    Team.OptionStatus fromNMSTeamVisibility(NMSTeamVisibility teamVisibility);

    Team.OptionStatus fromNMSTeamCollisionRule(NMSTeamCollisionRule teamCollisionRule);

    NMSDedicatedServer getDedicatedServer();

    default void uninjectPlayer(Player player) {
        uninjectPlayer(asServerPlayer(player));
    }

    default void uninjectPlayer(NMSServerPlayer serverPlayer) {
        ChannelPipeline pipeline = getChannel(getConnection(getPlayerConnection(serverPlayer))).pipeline();
        if (pipeline.get("packet_interceptor") != null) {
            pipeline.remove("packet_interceptor");
        }
    }

    default void injectPlayer(Player player, JavaPlugin plugin) {
        injectPlayer(player, asServerPlayer(player), plugin);
    }

    @SneakyThrows
    default void injectPlayer(Player player, NMSServerPlayer serverPlayer, JavaPlugin plugin) {
        ChannelPipeline pipeline = getChannel(getConnection(getPlayerConnection(serverPlayer))).pipeline();
        if (pipeline.get("packet_interceptor") != null) return;

        pipeline.addBefore("packet_handler", "packet_interceptor", new ChannelDuplexHandler() {

            private final PluginManager pluginManager = Bukkit.getPluginManager();



            private boolean inactive(ChannelHandlerContext ctx) {
                return !ctx.channel().isActive() || Bukkit.isStopping();
            }


            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (inactive(ctx)) return;
                if (!getNMSPacketClass().isInstance(msg)) {
                    super.channelRead(ctx, msg);
                    return;
                }

                if (inactive(ctx)) return;

                if (PrePacketReceiveEvent.getHandlerList().getRegisteredListeners().length > 0) {
                    PrePacketReceiveEvent pre = new PrePacketReceiveEvent(player, true);
                    pluginManager.callEvent(pre);
                    if (pre.isCancelled()) return;
                }
                Object newMsg = msg;
//                if (PacketReceiveEvent.getHandlerList().getRegisteredListeners().length > 0) {
//                    PacketReceiveEvent<ServerboundPacket> event = new PacketReceiveEvent<>(msg, player);
//                    pluginManager.callEvent(event);
//                    if (event.isCancelled()) return;
//                    else if (event.isResolved())
//                        newMsg = event.getPacket().asNMS();
//                }

                //todo fix this above (make the event async)

                try {
                    super.channelRead(ctx, newMsg);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @SuppressWarnings({"unchecked", "ExtractMethodRecommender"})
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if (inactive(ctx)) {
                    promise.setSuccess();
                    return;
                }
                if (!getNMSPacketClass().isInstance(msg)) {
                    super.write(ctx, msg, promise);
                    return;
                }
                if (inactive(ctx)) {
                    promise.setSuccess();
                    return;
                }

                if (PrePacketSendEvent.getHandlerList().getRegisteredListeners().length > 0 && !isNMSClientBundlePacket(msg)) {
                    PrePacketSendEvent pre = new PrePacketSendEvent(player, true);
                    pluginManager.callEvent(pre);
                    if (pre.isCancelled()) {
                        promise.setSuccess();
                        return;
                    }
                }
                Object newMsg = msg;
//                if (PacketSendEvent.getHandlerList().getRegisteredListeners().length > 0) {
//                    PacketSendEvent<ClientboundPacket> event = new PacketSendEvent<>(msg, player);
//                    pluginManager.callEvent(event);
//                    if (event.isCancelled()) {
//                        promise.setSuccess();
//                        return;
//                    }
//                    if (event.isResolved()) {
//                        newMsg = event.getPacket().asNMS();
//                    }
//                }

                //todo fix this above (make the event async)


                if (isNMSLevelParticle(msg)) {
                    final LevelParticlePacket packet = fromNMSLevelParticle((NMSLevelParticlePacket) msg);

                    final ParticleSendEvent particleSendEvent = new ParticleSendEvent(player, true);
                    particleSendEvent.setX(packet.getX());
                    particleSendEvent.setY(packet.getY());
                    particleSendEvent.setZ(packet.getZ());
                    particleSendEvent.setCount(packet.getCount());
                    particleSendEvent.setMaxSpeed(packet.getMaxSpeed());
                    particleSendEvent.setWorld(player.getWorld());
                    particleSendEvent.setXOffset(packet.getXDist());
                    particleSendEvent.setYOffset(packet.getYDist());
                    particleSendEvent.setZOffset(packet.getZDist());

                    particleSendEvent.setParticle(packet.getParticle().getType());

                    pluginManager.callEvent(particleSendEvent);

                    if (particleSendEvent.isCancelled())
                        return;
                } else if (isNMSBlockUpdatePacket(msg)) {
                    final BlockUpdatePacket packet = fromNMSBlockUpdatePacket((NMSBlockUpdatePacket) msg);

                    final BlockUpdateEvent event = new BlockUpdateEvent(player, true);
                    final BlockVector blockPos = packet.getBlockPos();
                    event.setX(blockPos.getBlockX());
                    event.setY(blockPos.getBlockY());
                    event.setZ(blockPos.getBlockZ());

                    final BlockData blockData = packet.getBlockState().clone();
                    event.setMaterial(blockData.getMaterial());
                    event.setBlockData(blockData);
                    event.setOriginalBlock(event.getLocation().getBlock());

                    pluginManager.callEvent(event);

                    if(event.isCancelled())
                        return;

                    if(!event.getBlockData().equals(blockData)) {
                        super.write(ctx, new BlockUpdatePacket(packet.getBlockPos(), event.getBlockData().clone()).asNMS(), promise);
                        return;
                    }
                }
                else if (isNMSEntityMetadataPacket(msg)) {
                    final EntityMetadataPacket packet = fromNMSEntityMetadataPacket((NMSEntityMetadataPacket) msg);
                    final int targetId = packet.getId();

                    final Entity entity = LevelUtil.getEntityFromID(targetId, player.getWorld());
                    if (entity != null && entity.isGlowing()) {
                        super.write(ctx, msg, promise);
                        return;
                    }

                    final Set<Integer> list = GlowMap.glowMap.get(player);
                    if (list == null || !list.contains(targetId)) {
                        super.write(ctx, msg, promise);
                        return;
                    }



                    super.write(ctx, rewriteMetadataPacketForGlow(msg), promise);
                    return;
                } else if (isNMSSoundPacket(msg)) {
                    final SoundPacket soundPacket = fromNMSSoundPacket((NMSSoundPacket) msg);

                    final SoundEvent event = new SoundEvent(player, true);

                    event.setEntitySound(false);

                    event.setX(soundPacket.getX());
                    event.setY(soundPacket.getY());
                    event.setY(soundPacket.getY());
                    event.setPitch(soundPacket.getPitch());
                    event.setVolume(soundPacket.getVolume());
                    event.setSeed(soundPacket.getSeed());
                    event.setSound(soundPacket.getSound());
                    event.setSoundSource(soundPacket.getSoundSource());

                    pluginManager.callEvent(event);
                    if (event.isCancelled())
                        return;
                } else if (isNMSSoundEntityPacket(msg)) {
                    final SoundEntityPacket soundPacket = fromNMSSoundEntityPacket((NMSSoundEntityPacket) msg);

                    final SoundEvent event = new SoundEvent(player, true);

                    event.setEntitySound(true);

                    event.setPitch(soundPacket.getPitch());
                    event.setVolume(soundPacket.getVolume());
                    event.setSeed(soundPacket.getSeed());
                    event.setSound(soundPacket.getSound());
                    event.setSoundSource(soundPacket.getSoundSource());

                    final int id = soundPacket.getId();
                    final Entity entity = soundPacket.getEntity();

                    if(id != 0)
                        event.setEntityID(id);
                    if(entity != null)
                        event.setEntity(entity);
                    else
                        event.setEntity(getEntityFromId(id, player.getWorld()));


                    pluginManager.callEvent(event);
                    if(event.isCancelled())
                        return;
                }


                try {
                    super.write(ctx, newMsg, promise);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                if (cause instanceof ClosedChannelException) return;
                cause.printStackTrace();
                super.exceptionCaught(ctx, cause);
            }
        });
    }


}