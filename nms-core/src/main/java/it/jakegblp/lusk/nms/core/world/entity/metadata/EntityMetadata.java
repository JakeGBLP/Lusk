package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.news.EntityDataKey;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * See: <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
 */
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class EntityMetadata implements EntityMetadataView, Copyable<EntityMetadata> {

    @Getter
    private boolean modified;

    private final Int2ObjectMap<MetadataEntry<? extends Entity, ?>> items = new Int2ObjectOpenHashMap<>();

    public static <A, B, C, D, E> @NotNull EntityMetadata of(
            EntityDataKey<? extends Entity, A, ?, ?, ?> keyA, A valueA,
            EntityDataKey<? extends Entity, B, ?, ?, ?> keyB, B valueB,
            EntityDataKey<? extends Entity, C, ?, ?, ?> keyC, C valueC,
            EntityDataKey<? extends Entity, D, ?, ?, ?> keyD, D valueD,
            EntityDataKey<? extends Entity, E, ?, ?, ?> keyE, E valueE
    ) {
        return of(Map.of(keyA, valueA, keyB, valueB, keyC, valueC, keyD, valueD, keyE, valueE));
    }

    public static <A, B, C, D> @NotNull EntityMetadata of(
            EntityDataKey<? extends Entity, A, ?, ?, ?> keyA, A valueA,
            EntityDataKey<? extends Entity, B, ?, ?, ?> keyB, B valueB,
            EntityDataKey<? extends Entity, C, ?, ?, ?> keyC, C valueC,
            EntityDataKey<? extends Entity, D, ?, ?, ?> keyD, D valueD
    ) {
        return of(Map.of(keyA, valueA, keyB, valueB, keyC, valueC, keyD, valueD));
    }

    public static <A, B, C> @NotNull EntityMetadata of(
            EntityDataKey<? extends Entity, A, ?, ?, ?> keyA, A valueA,
            EntityDataKey<? extends Entity, B, ?, ?, ?> keyB, B valueB,
            EntityDataKey<? extends Entity, C, ?, ?, ?> keyC, C valueC
    ) {
        return of(Map.of(keyA, valueA, keyB, valueB, keyC, valueC));
    }

    public static <A, B> @NotNull EntityMetadata of(EntityDataKey<? extends Entity, A, ?, ?, ?> keyA, A valueA, EntityDataKey<? extends Entity, B, ?, ?, ?> keyB, B valueB) {
        return of(Map.of(keyA, valueA, keyB, valueB));
    }

    public static <A> @NotNull EntityMetadata of(EntityDataKey<? extends Entity, A, ?, ?, ?> keyA, A valueA) {
        return of(Map.of(keyA, valueA));
    }

    @SuppressWarnings("unchecked")
    public static @NotNull EntityMetadata of(@NotNull Map<? extends EntityDataKey<? extends Entity, ?, ?, ?, ?>, ?> metadata) {
        var entityMetadata = new EntityMetadata();
        metadata.forEach((key, o) -> entityMetadata.set((EntityDataKey<? extends Entity, Object, ?, ?, ?>) key, NullabilityUtils.copyIfNotNull(o)));
        return entityMetadata;
    }

    public static @NotNull EntityMetadata of(@NotNull List<? extends MetadataEntry<? extends Entity, ?>> items) {
        var entityMetadata = new EntityMetadata();
        for (MetadataEntry<? extends Entity, ?> item : items) {
            if (item == null) continue;
            entityMetadata.setInternal(item.copy());
        }
        return entityMetadata;
    }

    @SafeVarargs
    public static @NotNull EntityMetadata of(MetadataEntry<? extends Entity, ?>... items) {
        return of(Arrays.asList(items));
    }

    public EntityMetadata(@NotNull Map<Integer, MetadataEntry<? extends Entity, ?>> metadata) {
        this();
        metadata.forEach((id, item) -> items.put((int) id, item.copy()));
    }

    public void markAsModified() {
        this.modified = true;
    }

    @SuppressWarnings({"unchecked"})
    public <E extends Entity, T> boolean setUnsafe(
            @NotNull EntityDataKey<E, T, ?, ?, ?> key,
            Object value
    ) {
        //System.out.println("'setUnsafe' with key: " + key + " and value: " + value);
        if (key.canBeSetTo(value))
            return setInternal(key.asItem((T) value));
        else
            throw new IllegalArgumentException("Cannot set key " + key + " to value " + value);
    }

    public <E extends Entity, T> EntityMetadata with(
            @NotNull EntityDataKey<E, T, ?, ?, ?> key,
            T value
    ) {
        this.set(key, value);
        return this;
    }

    public <E extends Entity, T> EntityMetadata without(
            @NotNull EntityDataKey<E, T, ?, ?, ?> key
    ) {
        this.remove(key);
        return this;
    }

    public <E extends Entity, T> boolean set(
            @NotNull EntityDataKey<E, T, ?, ?, ?> key,
            T value
    ) {
        //System.out.println("'set' with key reference: " + key + " and value: " + value);
        assert key.canBeSetTo(value);
        return setInternal(key.asItem(value));
    }

    public <E extends Entity, T> boolean setInternal(
            @NotNull MetadataEntry<E, T> item
    ) {
        markAsModified();
        //System.out.println("'setInternal' with id: " + id + " and item: " + item);
        items.put(item.getKey().getId(), item);
        return true;
    }

    public void remove(@Range(from = 0, to = 255) int id) {
        items.remove(id);
    }

    public <E extends Entity, T> void remove(@NotNull EntityDataKey<E, T, ?, ?, ?> key) {
        remove(key.getId());
    }

    public <E extends Entity, T> void remove(@NotNull MetadataEntry<E, T> item) {
        remove(item.getKey());
    }

    @Override
    public List<MetadataEntry<? extends Entity, ?>> items() {
        return new ArrayList<>(items.values());
    }

    @Override
    public EntityMetadata copy() {
        return new EntityMetadata(items);
    }
}