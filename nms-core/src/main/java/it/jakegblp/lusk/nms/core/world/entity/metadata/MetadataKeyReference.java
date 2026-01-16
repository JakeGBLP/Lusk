package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MetadataKeyReference<E extends Entity, T> {
    @Range(from = 0, to = 255) int id();

    Class<E> entityClass();

    EntityDataSerializer<T> serializer();

    default Class<T> valueClass() {
        return serializer().codec().getFromClass();
    }

    Class<T> rawValueClass();

    default boolean matches(MetadataKeyReference<? extends Entity, ?> other) {
        return other.id() == id() && serializer().equals(other.serializer());
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
        return new MetadataItem<>(id(), value, serializer());
    }

    default MetadataKey<E, T> asKey() {
        return new MetadataKey<>(id(), entityClass(), serializer(), rawValueClass());
    }
}