package it.jakegblp.lusk.nms.core.world.entity.metadata;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.Objects;

import static it.jakegblp.lusk.nms.core.util.NullabilityUtils.cloneIfNotNull;

public interface EntityMetadataView {

    List<MetadataItem<? extends Entity, ?>> items();

    default void copy(EntityMetadata to, MetadataKey<? extends Entity, Object> key) {
        to.set(key, cloneIfNotNull(get(key)));
    }

    default int size() {
        return items().size();
    }

    default int nonNullSize() {
        return items().stream().filter(Objects::nonNull).toList().size();
    }

    default boolean has(@Range(from = 0, to = 255) int id) {
        return items().get(id) != null;
    }

    default boolean has(@NotNull MetadataKeyReference<?, ?> key) {
        int id = key.id();
        if (size() <= id) return false;
        MetadataItem<? extends Entity, ?> item = get(id);
        if (item == null) return false;
        return item.matches(key);
    }

    @SuppressWarnings("unchecked")
    @UnknownNullability
    default <E extends Entity, T> MetadataItem<E, T> get(MetadataKeyReference<E, T> key) {
        var value = get(key.id());
        if (!key.matches(value))
            return null;
        return (MetadataItem<E, T>) value;
    }

    default MetadataItem<?, ?> get(@Range(from = 0, to = 255) int id) {
        return items().get(id);
    }

}