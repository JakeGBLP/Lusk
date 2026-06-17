package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.serialization.TypeKey;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import lombok.ToString;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@ToString
@NullMarked
public class MetadataKey<E extends Entity, T> implements MetadataKeyReference<E, T> {
    private final @Range(from = 0, to = 255) int id;
    private final Class<E> entityClass;
    private final EntityDataSerializer<T> serializer;
    private final Class<T> frontEndClass;

    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            Class<E> entityClass,
            TypeKey<T> typeKey,
            AbstractNMS nms) {
        this(id, entityClass, nms.getEntityDataSerializer(typeKey));
    }

    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            Class<E> entityClass,
            Class<T> type) {
        this(id, entityClass, TypeKey.simple(type), NMS);
    }
    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            Class<E> entityClass,
            Class<T> type,
            AbstractNMS nms) {
        this(id, entityClass, TypeKey.simple(type), nms);
    }

    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            Class<E> entityClass,
            TypeKey<T> typeKey) {
        this(id, entityClass, typeKey, NMS);
    }

    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            Class<E> entityClass,
            EntityDataSerializer<T> serializer,
            Class<T> frontEndClass) {
        this.id = id;
        this.entityClass = entityClass;
        this.serializer = serializer;
        this.frontEndClass = frontEndClass;
    }

    @SuppressWarnings("unchecked")
    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            Class<E> entityClass,
            EntityDataSerializer<T> serializer) {
        this(id, entityClass, serializer, (Class<T>) serializer.codec().key().rawType());
    }

    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            Class<E> entityClass,
            Class<T> frontEndClass,
            Class<?> serializableClass) {
        this(id, entityClass, frontEndClass, serializableClass, NMS);
    }

    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            Class<E> entityClass,
            Class<T> frontEndClass,
            Class<?> serializableClass,
            AbstractNMS nms) {
        this(id, entityClass, (EntityDataSerializer<T>) nms.getEntityDataSerializer(serializableClass), frontEndClass);
    }

    @Override
    public @Range(from = 0, to = 255) int id() {
        return id;
    }

    @Override
    public Class<E> entityClass() {
        return entityClass;
    }

    public Class<T> frontEndClass() {
        return frontEndClass;
    }

    @Override
    public EntityDataSerializer<T> serializer() {
        return serializer;
    }



}