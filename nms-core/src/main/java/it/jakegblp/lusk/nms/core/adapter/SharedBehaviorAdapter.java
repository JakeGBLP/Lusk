package it.jakegblp.lusk.nms.core.adapter;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.serialization.BufferCodecs;
import it.jakegblp.lusk.nms.core.serialization.Mappings;
import it.jakegblp.lusk.nms.core.serialization.TypeKey;
import it.jakegblp.lusk.nms.core.world.entity.effect.InternalEntityEffect;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

public interface SharedBehaviorAdapter<
        NMSEntityDataSerializer,
        NMSServerPlayer,
        NMSPacket,
        NMSServerGamePacketListenerImpl,
        NMSConnection
        > {

    void init(Version serverVersion);

    Mappings getMappings();

    BufferCodecs getCodecs();

    @NullMarked
    void registerEntityDataSerializer(EntityDataSerializer<?> entityDataSerializer, NMSEntityDataSerializer nmsEntityDataSerializer);

    @NullMarked
    default void registerEntityDataSerializer(AbstractNMS nms, Class<?> fromClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.simple(nms, getFrontEndClass(fromClass)), nmsEntityDataSerializer);
    }

    @NullMarked
    default void registerOptionalEntityDataSerializer(AbstractNMS nms, Class<?> fromClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.optional(nms, getFrontEndClass(fromClass)), nmsEntityDataSerializer);
    }

    @NullMarked
    default void registerListEntityDataSerializer(AbstractNMS nms, Class<?> fromClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.list(nms, getFrontEndClass(fromClass)), nmsEntityDataSerializer);
    }

    @NullMarked
    default void registerHolderEntityDataSerializer(AbstractNMS nms, Class<?> fromClass, NMSEntityDataSerializer nmsEntityDataSerializer) {
        registerEntityDataSerializer(EntityDataSerializer.holder(nms, getFrontEndClass(fromClass)), nmsEntityDataSerializer);
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

    default <T> EntityDataSerializer<T> getEntityDataSerializer(Class<T> toClass) {
        for (EntityDataSerializer<?> entityDataSerializer : getEntityDataSerializers().keySet())
            if (entityDataSerializer.codec().test(toClass))
                return (EntityDataSerializer<T>) entityDataSerializer;
        throw new IllegalArgumentException("No EntityDataSerializer found for " + toClass);
    }

    default <T> @Nullable EntityDataSerializer<T> getEntityDataSerializer(TypeKey<T> typeKey) {
        for (EntityDataSerializer<?> entityDataSerializer : getEntityDataSerializers().keySet())
            if (entityDataSerializer.codec().key().equals(typeKey))
                return (EntityDataSerializer<T>) entityDataSerializer;
        throw new IllegalArgumentException("No EntityDataSerializer found for " + typeKey);
    }

    Class<?> getSerializableClass(Class<?> clazz);

    Class<?> getFrontEndClass(Class<?> clazz);

    Entity getEntityFromId(int id, World world);

    void setCubeFast(Location corner1, Location corner2, BlockData blockData);

    void replaceFast(Location corner1, Location corner2, BlockData oldBlockData, BlockData newBlockData);

    NMSServerPlayer asServerPlayer(Player player);

    NMSServerGamePacketListenerImpl getPlayerConnection(NMSServerPlayer serverPlayer);

    default void sendPacket(Player player, NMSPacket packet) {
        sendPacket(asServerPlayer(player), packet);
    }

    default void sendPacket(NMSServerPlayer player, NMSPacket packet) {
        sendPacketInternal(getPlayerConnection(player), packet);
    }

    void sendPacketInternal(NMSServerGamePacketListenerImpl packetListener, NMSPacket packet);

    NMSConnection getConnection(NMSServerGamePacketListenerImpl packetListener);

    Channel getChannel(NMSConnection connection);

    default void uninjectPlayer(Player player) {
        uninjectPlayer(asServerPlayer(player));
    }

    default void uninjectPlayer(NMSServerPlayer serverPlayer) {
        ChannelPipeline pipeline = getChannel(getConnection(getPlayerConnection(serverPlayer))).pipeline();
        if (pipeline.get("packet_interceptor") != null) {
            pipeline.remove("packet_interceptor");
        }
    }

    void injectPlayer(Player player, JavaPlugin plugin);

    default void playInternalEntityEffect(Entity entity, InternalEntityEffect internalEntityEffect) {
        Preconditions.checkArgument(internalEntityEffect != null, "Internal Entity effect cannot be null");
        Preconditions.checkArgument(internalEntityEffect.isApplicableTo(entity), "Internal Entity effect cannot be applied to this entity");
        playEntityEffect(entity, internalEntityEffect.getData());
    }

    void playEntityEffect(Entity entity, byte data);

}