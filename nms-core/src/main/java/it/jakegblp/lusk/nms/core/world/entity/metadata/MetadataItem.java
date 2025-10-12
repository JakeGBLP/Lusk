package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public record MetadataItem<E extends Entity, T>(
        @Range(from = 0, to = 255) int id,
        @Nullable T value,
        @NotNull Class<E> entityClass,
        @NotNull EntitySerializerKey<T> serializerKey) implements MetadataKeyReference<E, T> {

    public MetadataItem(
            @Range(from = 0, to = 255) int id,
            @Nullable T value,
            @NotNull Class<E> entityClass,
            @NotNull Class<T> valueClass,
            @NotNull EntitySerializerKey.Type serializerType) {
        this(id, value, entityClass, new EntitySerializerKey<>(valueClass, serializerType));
    }

    @SuppressWarnings("unchecked")
    public MetadataItem(
            @Range(from = 0, to = 255) int id,
            @Nullable T value,
            @NotNull EntitySerializerKey<T> serializerKey) {
        this(id, value, (Class<E>) Entity.class, serializerKey);
    }
}