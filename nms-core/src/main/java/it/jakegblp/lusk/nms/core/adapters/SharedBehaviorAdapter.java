package it.jakegblp.lusk.nms.core.adapters;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import io.netty.channel.*;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.events.*;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.util.BufferCodec;
import it.jakegblp.lusk.nms.core.util.SimpleBufferCodec;
import it.jakegblp.lusk.nms.core.world.entity.effect.InternalEntityEffect;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import it.jakegblp.lusk.nms.core.world.level.LevelUtil;
import it.jakegblp.lusk.nms.core.world.player.GlowMap;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface SharedBehaviorAdapter<
        NMSEntityDataSerializer,
        NMSServerPlayer,
        NMSPacket,
        NMSServerGamePacketListenerImpl,
        NMSConnection,
        NMSDedicatedServer,
        NMSClientBundlePacket
        > {

    @NotNull List<SimpleBufferCodec<?, ?>> getCodecsInternal();

    @NullMarked
    void registerEntityDataSerializer(EntityDataSerializer<?> entityDataSerializer, NMSEntityDataSerializer nmsEntityDataSerializer);

    @NullMarked
    default void registerEntityDataSerializer(AbstractNMS nms, Class<?> toClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.simple(nms, toClass), nmsEntityDataSerializer);
    }
    @NullMarked
    default void registerOptionalEntityDataSerializer(AbstractNMS nms, Class<?> toClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.optional(nms, toClass), nmsEntityDataSerializer);
    }
    @NullMarked
    default void registerListEntityDataSerializer(AbstractNMS nms, Class<?> toClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.list(nms, toClass), nmsEntityDataSerializer);
    }
    @NullMarked
    default void registerHolderEntityDataSerializer(AbstractNMS nms, Class<?> toClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.holder(nms, toClass), nmsEntityDataSerializer);
    }

    int getNMSSerializerId(NMSEntityDataSerializer nmsEntityDataSerializer);

    NMSEntityDataSerializer getNMSSerializer(int id);

    default EntityDataSerializer<?> getSerializer(int id) {
        return getEntityDataSerializers().inverse().get(getNMSSerializer(id));
    }

    default int getSerializerId(EntityDataSerializer<?> entityDataSerializer) {
        return getNMSSerializerId(getEntityDataSerializers().get(entityDataSerializer));
    }

    BiMap<EntityDataSerializer<?>, NMSEntityDataSerializer> getEntityDataSerializers();

    <F, T> SimpleBufferCodec<F, T> getNMSSerializerCodec(NMSEntityDataSerializer serializer);

    default <T> EntityDataSerializer<T> getEntityDataSerializer(Class<T> toClass) {
        toClass = (Class<T>) getSerializableClass(toClass);
        for (EntityDataSerializer<?> entityDataSerializer : getEntityDataSerializers().keySet())
            if (entityDataSerializer.codec().getFromClass().isAssignableFrom(toClass))
                return (EntityDataSerializer<T>) entityDataSerializer;
        throw new IllegalArgumentException("No EntityDataSerializer found for " + toClass);
    }

    default <F> EntityDataSerializer<F> getEntityDataSerializerFromNMS(Class<F> fromClass) {
        for (EntityDataSerializer<?> entityDataSerializer : getEntityDataSerializers().keySet())
            if (entityDataSerializer.codec().getFromClass().isAssignableFrom(fromClass))
                return (EntityDataSerializer<F>) entityDataSerializer;
        throw new IllegalArgumentException("No EntityDataSerializer found for " + fromClass);
    }

    @NullMarked
    default <F, T> SimpleBufferCodec<F, T> registerCodec(
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
    default <C extends SimpleBufferCodec<?, ?>> C registerCodec(C codec) {
        getCodecsInternal().add(codec);
        return codec;
    }

    Class<?> getSerializableClass(Class<?> clazz);

    default boolean isSerializableClassOf(Class<?> hypotheticallySerializableClass, Class<?> clazz) {
        return getFirstCodec(clazz).getFromClass().isAssignableFrom(hypotheticallySerializableClass);
    }

    default boolean isSerializableInstanceOf(Object hypotheticallySerializableObject, Class<?> clazz) {
        return getFirstCodec(clazz).getFromClass().isInstance(hypotheticallySerializableObject);
    }

    default boolean isCodecFromClass(Class<?> type) {
        for (SimpleBufferCodec<?, ?> codec : getCodecsInternal())
            if (codec.getFromClass().isAssignableFrom(type))
                return true;
        return false;
    }

    default boolean isCodecToClass(Class<?> type) {
        for (SimpleBufferCodec<?, ?> codec : getCodecsInternal())
            if (codec.getFromClass().isAssignableFrom(type))
                return true;
        return false;
    }

    default <F> @NotNull SimpleBufferCodec<F, ?> getFirstNMSCodec(Class<F> type) {
        return getNMSCodec(type).findFirst().orElseThrow(() ->
                new IllegalStateException(
                        "No codec registered for: " + type.getName() +
                                " total codecs=" + getCodecsInternal().size()
                ));
    }

    default <F> @NotNull Stream<SimpleBufferCodec<F, ?>> getNMSCodec(Class<F> type) {
        return Arrays.stream(getNmsCodecs(type));
    }

    default <T> @NotNull SimpleBufferCodec<?, T> getFirstCodec(Class<T> type) {
        return getCodec(type).findFirst().orElseThrow(() ->
                new IllegalStateException(
                        "No codec registered for: " + type.getName() +
                                " total codecs=" + getCodecsInternal().size()
                ));
    }

    default <T> @NotNull Stream<SimpleBufferCodec<?, T>> getCodec(Class<T> type) {
        return Arrays.stream(getCodecs(type));
    }

    @SuppressWarnings("unchecked")
    default <T> SimpleBufferCodec<?, T>[] getCodecs(Class<T> type) {
        List<SimpleBufferCodec<?, T>> list = new ArrayList<>();

        for (SimpleBufferCodec<?, ?> codec : getCodecsInternal())
            if (codec.getToClass().isAssignableFrom(type))
                list.add((SimpleBufferCodec<?, T>) codec);

        list.sort((a, b) -> {
            Class<?> ac = a.getToClass();
            Class<?> bc = b.getToClass();
            if (ac == bc) return 0;
            return ac.isAssignableFrom(bc) ? 1 : -1;
        });

        return list.toArray(new SimpleBufferCodec[0]);
    }


    @SuppressWarnings("unchecked")
    default <F> SimpleBufferCodec<F, ?>[] getNmsCodecs(Class<F> type) {
        List<SimpleBufferCodec<F, ?>> list = new ArrayList<>();

        for (SimpleBufferCodec<?, ?> codec : getCodecsInternal())
            if (codec.getFromClass().isAssignableFrom(type))
                list.add((SimpleBufferCodec<F, ?>) codec);

        list.sort((a, b) -> {
            Class<?> ac = a.getFromClass();
            Class<?> bc = b.getFromClass();
            if (ac == bc) return 0;
            return ac.isAssignableFrom(bc) ? 1 : -1;
        });

        return list.toArray(new SimpleBufferCodec[0]);
    }



    @SuppressWarnings("unchecked")
    default <F, T> @NotNull T fromNMS(@NotNull F f) {
        SimpleBufferCodec<F, T> codec = (SimpleBufferCodec<F, T>) getFirstCodec(f.getClass());
        return codec.encode(f);
    }

    @SuppressWarnings("unchecked")
    default <F, T> @NotNull F toNMS(@NotNull T t) {
        SimpleBufferCodec<F, T> codec = (SimpleBufferCodec<F, T>) getFirstCodec(t.getClass());
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

    default void sendPacket(Player player, NMSPacket packet) {
        sendPacket(asServerPlayer(player), packet);
    }

    default void sendPacket(NMSServerPlayer player, NMSPacket packet) {
        sendPacketInternal(getPlayerConnection(player), packet);
    }

    void sendPacketInternal(NMSServerGamePacketListenerImpl packetListener, NMSPacket packet);

    NMSConnection getConnection(NMSServerGamePacketListenerImpl packetListener);

    Channel getChannel(NMSConnection connection);

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

            @SuppressWarnings("ExtractMethodRecommender")
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


                var optionalCodec = getCodec(EntityMetadataPacket.class).findFirst();

                if (isSerializableInstanceOf(msg, LevelParticlesPacket.class)) {
                    final LevelParticlesPacket packet = fromNMS(msg);

                    final ParticleSendEvent particleSendEvent = new ParticleSendEvent(player, true);
                    particleSendEvent.setX(packet.getX());
                    particleSendEvent.setY(packet.getY());
                    particleSendEvent.setZ(packet.getZ());
                    particleSendEvent.setCount(packet.getCount());
                    particleSendEvent.setMaxSpeed(packet.getMaxSpeed());
                    particleSendEvent.setWorld(player.getWorld());
                    particleSendEvent.setXOffset(packet.getXOffset());
                    particleSendEvent.setYOffset(packet.getYOffset());
                    particleSendEvent.setZOffset(packet.getZOffset());

                    particleSendEvent.setParticle(packet.getParticle());

                    pluginManager.callEvent(particleSendEvent);

                    if (particleSendEvent.isCancelled())
                        return;
                } else if (isSerializableInstanceOf(msg, BlockUpdatePacket.class)) {
                    final BlockUpdatePacket packet = fromNMS(msg);

                    final BlockUpdateEvent event = new BlockUpdateEvent(player, true);
                    final BlockVector blockPos = packet.getPosition();
                    event.setX(blockPos.getBlockX());
                    event.setY(blockPos.getBlockY());
                    event.setZ(blockPos.getBlockZ());

                    final BlockData blockData = packet.getBlockData().clone();
                    event.setMaterial(blockData.getMaterial());
                    event.setBlockData(blockData);
                    event.setOriginalBlock(event.getLocation().getBlock());

                    pluginManager.callEvent(event);

                    if(event.isCancelled())
                        return;

                    if(!event.getBlockData().equals(blockData)) {
                        super.write(ctx, new BlockUpdatePacket(packet.getPosition(), event.getBlockData().clone()).asNMS(), promise);
                        return;
                    }
                } else if (isSerializableInstanceOf(msg, EntityMetadataPacket.class)) {
                    final EntityMetadataPacket packet = fromNMS(msg);
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
                } else if (isSerializableInstanceOf(msg, SoundPacket.class)) {
                    final SoundPacket soundPacket = fromNMS(msg);

                    final SoundEvent event = new SoundEvent(player, true, soundPacket);

                    pluginManager.callEvent(event);
                    if (event.isCancelled())
                        return;
                } else if (isSerializableInstanceOf(msg, SoundEntityPacket.class)) {
                    final SoundEntityPacket soundEntityPacket = fromNMS(msg);

                    final SoundEvent event = new SoundEvent(player, true, soundEntityPacket);

                    final int id = soundEntityPacket.getId();

                    if (id != 0)
                        event.setEntityID(id);
                    else
                        event.setEntity(getEntityFromId(id, player.getWorld()));
                    pluginManager.callEvent(event);
                    if (event.isCancelled())
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

    default void playInternalEntityEffect(Entity entity, InternalEntityEffect internalEntityEffect) {
        Preconditions.checkArgument(internalEntityEffect != null, "Internal Entity effect cannot be null");
        Preconditions.checkArgument(internalEntityEffect.isApplicableTo(entity), "Internal Entity effect cannot apply to this entity");
        playEntityEffect(entity, internalEntityEffect.getData());
    }

    void playEntityEffect(Entity entity, byte data);

}