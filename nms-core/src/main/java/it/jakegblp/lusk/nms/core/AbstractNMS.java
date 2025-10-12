package it.jakegblp.lusk.nms.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.adapters.PlayerPositionPacketAdapter;
import it.jakegblp.lusk.nms.core.adapters.PlayerRotationPacketAdapter;
import it.jakegblp.lusk.nms.core.adapters.SetEquipmentPacketAdapter;
import it.jakegblp.lusk.nms.core.adapters.SharedBehaviorAdapter;
import it.jakegblp.lusk.nms.core.injection.InjectionListener;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKey;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import lombok.Getter;
import lombok.experimental.Delegate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
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
    protected final SetEquipmentPacketAdapter setEquipmentPacketAdapter;
    @Delegate
    @SuppressWarnings("rawtypes")
    protected final PlayerPositionPacketAdapter playerPositionPacketAdapter;
    @Getter
    protected final JavaPlugin plugin;
    @Getter
    protected final Version version;

    public AbstractNMS(
            JavaPlugin plugin,
            Version version,
            @SuppressWarnings("rawtypes")
            SharedBehaviorAdapter sharedBehaviorAdapter,
            PlayerRotationPacketAdapter playerRotationPacketAdapter,
            SetEquipmentPacketAdapter<?> setEquipmentPacketAdapter,
            PlayerPositionPacketAdapter<?, ?> playerPositionPacketAdapter
    ) {
        this.plugin = plugin;
        this.version = version;
        this.sharedBehaviorAdapter = sharedBehaviorAdapter;
        this.playerRotationPacketAdapter = playerRotationPacketAdapter;
        this.setEquipmentPacketAdapter = setEquipmentPacketAdapter;
        this.playerPositionPacketAdapter = playerPositionPacketAdapter;
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
        if (Component.class.isAssignableFrom(clazz)) return getNMSComponentClass();
        else if (FlagByte.class.isAssignableFrom(clazz)) return Byte.class;
        else if (Pose.class.isAssignableFrom(clazz)) return getNMSPoseClass();
        else if (BlockVector.class.isAssignableFrom(clazz)) return getNMSBlockVectorClass();
        else if (Vector.class.isAssignableFrom(clazz)) return getNMSVectorClass();
        else if (ItemStack.class.isAssignableFrom(clazz)) return getNMSItemStackClass();
        return clazz;
    }

    public @Nullable Object toNMSObject(@Nullable Object object) {
        if (object instanceof NMSObject<?> nmsObject) return nmsObject.asNMS();
        else if (object instanceof net.kyori.adventure.text.Component component) return asNMSComponent(component);
        else if (object instanceof Pose pose) return asNMSPose(pose);
        else if (object instanceof BlockVector blockVector) return asNMSBlockVector(blockVector);
        else if (object instanceof Player player) return asServerPlayer(player);
        else if (object instanceof ItemStack itemStack) return asNMSItemStack(itemStack);
        return object;
    }

    /**
     * This method takes an NMS Packet and returns a non NMS Packet
     *
     * @param object an NMS Packet
     * @return the packet in a common form
     */
    public @Nullable Packet fromNMSPacket(@Nullable Object object) {
        if (isNMSEntityAnimationPacket(object)) return fromNMSEntityAnimationPacket(object);
        else if (isNMSSetEquipmentPacket(object)) return fromNMSSetEquipmentPacket(object);
        else if (isNMSBlockDestructionPacket(object)) return fromNMSBlockDestructionPacket(object);
        else if (isNMSClientBundlePacket(object)) return fromNMSClientBundlePacket(object);
        else if (isNMSEntityMetadataPacket(object)) return fromNMSEntityMetadataPacket(object);
        else if (isNMSAddEntityPacket(object)) return fromNMSAddEntityPacket(object);
        else if (isNMSPlayerRotationPacket(object)) return fromNMSPlayerRotationPacket(object);
        else if (isNMSPlayerPositionPacket(object)) return fromNMSPlayerPositionPacket(object);
        else if (isNMSRemoveEntitiesPacket(object)) return fromNMSRemoveEntitiesPacket(object);
        return null;
    }

    public @Nullable Object fromNMSObject(@Nullable Object object) {
        if (isNMSEntityType(object)) return fromNMSEntityType(object);
        else if (isNMSPacket(object)) return fromNMSPacket(object);
        return null;
    }

}