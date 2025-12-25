package it.jakegblp.lusk.nms.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.adapters.*;
import it.jakegblp.lusk.nms.core.injection.InjectionListener;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNMS<
        EntityDataSerializer
        > {

    public static AbstractNMS<?> NMS;
    @SuppressWarnings("rawtypes")
    protected final BiMap<EntitySerializerKey, EntityDataSerializer> entityDataSerializerMap = HashBiMap.create();
    @Delegate
    @SuppressWarnings("rawtypes")
    protected final SharedBehaviorAdapter sharedBehaviorAdapter;
    @Delegate
    protected final PlayerRotationPacketAdapter playerRotationPacketAdapter;
    @Delegate
    @SuppressWarnings("rawtypes")
    protected final PlayerPositionPacketAdapter playerPositionPacketAdapter;
    @Getter
    protected final JavaPlugin plugin;
    @Getter
    protected final Version version;
    @Delegate
    protected final SharedBiomeAdapter sharedBiomeAdapter;

    public AbstractNMS(
            JavaPlugin plugin,
            Version version,
            @SuppressWarnings("rawtypes")
            SharedBehaviorAdapter sharedBehaviorAdapter,
            PlayerRotationPacketAdapter playerRotationPacketAdapter,
            PlayerPositionPacketAdapter<?, ?> playerPositionPacketAdapter,
            SharedBiomeAdapter sharedBiomeAdapter
    ) {
        this.plugin = plugin;
        this.version = version;
        this.sharedBehaviorAdapter = sharedBehaviorAdapter;
        this.playerRotationPacketAdapter = playerRotationPacketAdapter;
        this.playerPositionPacketAdapter = playerPositionPacketAdapter;
        this.sharedBiomeAdapter = sharedBiomeAdapter;
        Bukkit.getPluginManager().registerEvents(new InjectionListener(), plugin);
        init();
    }

    public abstract void init();

    public void registerEntityDataSerializer(
            @SuppressWarnings("rawtypes")
            EntitySerializerKey info,
            EntityDataSerializer entityDataSerializer
    ) {
        entityDataSerializerMap.put(info, entityDataSerializer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void registerEntityDataSerializer(
            @NotNull Class<?> serializerClass,
            @NotNull EntitySerializerKey.Type serializerType,
            @NotNull EntityDataSerializer entityDataSerializer
    ) {
        entityDataSerializerMap.put(new EntitySerializerKey(serializerClass, serializerType), entityDataSerializer);
    }

    /**
     * This method must only be used for unimplemented serializers or ones without an easy implementation.<br>
     * This is not to be kept final.
     */
    @SuppressWarnings("rawtypes")
    public void registerUnknownEntityDataSerializer(
            EntitySerializerKey info,
            EntityDataSerializer entityDataSerializer
    ) {
        entityDataSerializerMap.put(info, entityDataSerializer);
    }

    @SuppressWarnings("unchecked")
    public EntitySerializerKey<?> getEntityDataSerializerKey(Object serializer) {
        EntitySerializerKey<?> entityDataSerializerInfo = entityDataSerializerMap.inverse().get((EntityDataSerializer) serializer);
        Preconditions.checkNotNull(entityDataSerializerInfo, "Could not find entityDataSerializerInfo for Serializer " + serializer);
        return entityDataSerializerInfo;
    }

    public EntityDataSerializer getEntityDataSerializer(MetadataKey<?, ?> key) {
        EntityDataSerializer entityDataSerializer = entityDataSerializerMap.get(key.serializerKey());
        Preconditions.checkNotNull(entityDataSerializer, "Could not find EntityDataSerializer for " + key.serializerType() + " " + key.valueClass().getName());
        return entityDataSerializer;
    }

    public EntityDataSerializer getEntityDataSerializer(EntitySerializerKey<?> entitySerializerKey) {
        EntityDataSerializer entityDataSerializer = entityDataSerializerMap.get(entitySerializerKey);
        Preconditions.checkNotNull(entityDataSerializer, "Could not find EntityDataSerializer for " + entitySerializerKey.serializerType() + " " + entitySerializerKey.serializeableClass().getName());
        return entityDataSerializer;
    }

    public EntityDataSerializer getEntityDataSerializer(Class<?> clazz, EntitySerializerKey.Type type) {
        EntityDataSerializer entityDataSerializer = entityDataSerializerMap.get(new EntitySerializerKey<>(clazz, type));
        Preconditions.checkNotNull(entityDataSerializer, "Could not find EntityDataSerializer for " + type + " " + clazz.getName());
        return entityDataSerializer;
    }

    public EntityDataSerializer getEntityDataSerializer(Class<?> clazz) {
        return entityDataSerializerMap.get(EntitySerializerKey.normal(clazz));
    }

    public Class<?> getSerializableClass(Class<?> clazz) {
        if (FlagByte.class.isAssignableFrom(clazz)) return Byte.class;
        else if (clazz == org.bukkit.entity.Display.Billboard.class) return Byte.class;
        var codec = getCodec(clazz);
        if (codec == null) return clazz;
        else return codec.getToClass();
    }

    public @Nullable Object toNMSObject(@Nullable Object object) {
        if (object instanceof NMSObject<?> nmsObject) return nmsObject.asNMS();
        else if (object instanceof Player player) return asServerPlayer(player);
        if (object instanceof org.bukkit.entity.Display.Billboard bb)
            return (byte) bb.ordinal();
        return object;
    }


    /**
     * This method takes an NMS Packet and returns a non NMS Packet
     *
     * @param object an NMS Packet
     * @return the packet in a common form
     */
    public @NotNull Packet fromNMSPacket(@NotNull Object object) {
        if (isNMSSetEquipmentPacket(object)) return fromNMSSetEquipmentPacket(object);
        else if (isNMSClientBundlePacket(object)) return fromNMSClientBundlePacket(object);
        else if (isNMSEntityMetadataPacket(object)) return fromNMSEntityMetadataPacket(object);
        else if (isNMSPlayerInfoUpdatePacket(object)) return fromNMSPlayerInfoUpdatePacket(object);
        else if (isNMSPlayerRotationPacket(object)) return fromNMSPlayerRotationPacket(object);
        else if (isNMSPlayerPositionPacket(object)) return fromNMSPlayerPositionPacket(object);
        else if (isNMSSystemChatPacket(object)) return fromNMSSystemChatPacket(object);
        else if (isNMSLevelParticle(object)) return fromNMSLevelParticle(object);
        else if (isNMSAttributePacket(object)) return fromNMSAttributePacket(object);
        else if (isNMSSetPlayerTeamPacket(object)) return fromNMSSetPlayerTeamPacket(object);
        else if (isNMSSetCameraPacket(object)) return fromNMSSetCameraPacket(object);
        else if (isNMSTeleportPacket(object)) return fromNMSTeleportPacket(object);
        else if (isNMSBlockUpdatePacket(object)) return fromNMSBlockUpdatePacket(object);
        else if (isNMSSoundPacket(object)) return fromNMSSoundPacket(object);
        else if (isNMSSoundEntityPacket(object)) return fromNMSSoundEntityPacket(object);
        throw new IllegalArgumentException("Could not convert nms packet " + object.getClass().getName());
    }

    public @NotNull Object fromNMSObject(@NotNull Object object) {
        if (isNMSTeamParameters(object)) return fromNMSTeamParameters(object);
        else if (isNMSPlayerInfoUpdatePacketAction(object)) return fromNMSPlayerInfoUpdatePacketAction(object);
        else if (isNMSPacket(object)) return fromNMSPacket(object);
        throw new IllegalArgumentException("Could not convert nms object " + object.getClass().getName());

    }

}