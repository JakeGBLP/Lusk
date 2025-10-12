package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import lombok.ToString;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

@ToString
public class MetadataKey<E extends Entity, T> implements MetadataKeyReference<E, T> {
    private final @Range(from = 0, to = 255) int id;
    private final @NotNull Class<E> entityClass;
    private final @NotNull EntitySerializerKey<T> serializerKey;

    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            @NotNull Class<E> entityClass,
            @NotNull EntitySerializerKey<T> serializerKey) {
        this.id = id;
        this.entityClass = entityClass;
        this.serializerKey = serializerKey;
    }

    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            @NotNull Class<E> entityClass,
            @NotNull Class<T> valueClass,
            @NotNull EntitySerializerKey.Type serializerType) {
        this(id, entityClass, new EntitySerializerKey<>(valueClass, serializerType));
    }

    @Override
    public @Range(from = 0, to = 255) int id() {
        return id;
    }

    @Override
    public @NotNull Class<E> entityClass() {
        return entityClass;
    }

    @Override
    public @NotNull EntitySerializerKey<T> serializerKey() {
        return serializerKey;
    }

}