package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface MetadataKeyReference<E extends Entity, T> {
    @Range(from = 0, to = 255)
    int id();

    @NotNull Class<E> entityClass();

    @NotNull EntitySerializerKey<T> serializerKey();

    default EntitySerializerKey.Type serializerType() {
        return serializerKey().serializerType();
    }

    default Class<T> valueClass() {
        return serializerKey().serializeableClass();
    }

    default boolean matches(MetadataKeyReference<? extends Entity, ?> other) {
        return other != null
                && other.id() == id()
                && serializerType().equals(other.serializerType());
    }

    default boolean matchesItem(MetadataItem<? extends Entity, ?> other) {
        return this instanceof MetadataItem<E, T> item
                && item.matches(other)
                && item.value() == other.value();
    }

    default boolean canBeSetTo(Object object) {
        return valueClass().isInstance(object);
    }

    default MetadataItem<E, T> asItem(T value) {
        return new MetadataItem<>(id(), value, entityClass(), serializerKey());
    }

    default MetadataKey<E, T> asKey() {
        return new MetadataKey<>(id(), entityClass(), serializerKey());
    }
}