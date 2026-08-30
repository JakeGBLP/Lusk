package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;

import static it.jakegblp.lusk.nms.core.util.NullabilityUtils.copyIfNotNull;

// todo: expand simple list api
// todo: do we even need this?
public interface EntityMetadataView {

    List<MetadataEntry<? extends Entity, ?>> items();

    default void copy(EntityMetadata to, EntityDataKey<? extends Entity, Object, ?, ?, ?> key) {
        to.set(key, copyIfNotNull(get(key)));
    }

    default int size() {
        return items().size();
    }

    default int nonNullSize() {
        return CommonUtils.nonNull(items()).size();
    }

    default boolean has(@Range(from = 0, to = 255) int id) {
        return items().get(id) != null;
    }

    default boolean has(@NotNull EntityDataKey<? extends Entity, ?, ?, ?, ?> key) {
        int id = key.getId();
        if (size() <= id) return false;
        MetadataEntry<? extends Entity, ?> item = get(id);
        return item != null && item.getKey().equals(key);
    }

    @SuppressWarnings("unchecked")
    @UnknownNullability
    default <E extends Entity, T> MetadataEntry<E, T> get(EntityDataKey<E, T, ?, ?, ?> key) {
        var value = get(key.getId());
        if (!key.canBeSetTo(value))
            return null;
        return (MetadataEntry<E, T>) value;
    }

    default MetadataEntry<?, ?> get(@Range(from = 0, to = 255) int id) {
        return items().get(id);
    }

}