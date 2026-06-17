
package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.common.Instances;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;

/**
 * See: <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
 */
@EqualsAndHashCode
@ToString
public class EntityMetadata implements EntityMetadataView, Copyable<EntityMetadata> {

    @Getter
    private boolean modified;

    private final List<MetadataItem<? extends Entity, ?>> items;

    public static <A, B, C, D, E> EntityMetadata of(
            MetadataKeyReference<? extends Entity, A> keyA, A valueA,
            MetadataKeyReference<? extends Entity, B> keyB, B valueB,
            MetadataKeyReference<? extends Entity, C> keyC, C valueC,
            MetadataKeyReference<? extends Entity, D> keyD, D valueD,
            MetadataKeyReference<? extends Entity, E> keyE, E valueE
    ) {
        return of(Map.of(keyA, valueA, keyB, valueB, keyC, valueC, keyD, valueD, keyE, valueE));
    }

    public static <A, B, C, D> EntityMetadata of(
            MetadataKeyReference<? extends Entity, A> keyA, A valueA,
            MetadataKeyReference<? extends Entity, B> keyB, B valueB,
            MetadataKeyReference<? extends Entity, C> keyC, C valueC,
            MetadataKeyReference<? extends Entity, D> keyD, D valueD
    ) {
        return of(Map.of(keyA, valueA, keyB, valueB, keyC, valueC, keyD, valueD));
    }

    public static <A, B, C> EntityMetadata of(
            MetadataKeyReference<? extends Entity, A> keyA, A valueA,
            MetadataKeyReference<? extends Entity, B> keyB, B valueB,
            MetadataKeyReference<? extends Entity, C> keyC, C valueC
    ) {
        return of(Map.of(keyA, valueA, keyB, valueB, keyC, valueC));
    }

    public static <A, B> EntityMetadata of(MetadataKeyReference<? extends Entity, A> keyA, A valueA, MetadataKeyReference<? extends Entity, B> keyB, B valueB) {
        return of(Map.of(keyA, valueA, keyB, valueB));
    }

    public static <A> EntityMetadata of(MetadataKeyReference<? extends Entity, A> keyA, A valueA) {
        return of(Map.of(keyA, valueA));
    }

    @SuppressWarnings("unchecked")
    public static EntityMetadata of(Map<? extends MetadataKeyReference<? extends Entity, ?>, ?> metadata) {
        var entityMetadata = new EntityMetadata();
        metadata.forEach((metadataKeyReference, o) -> entityMetadata.set((MetadataKeyReference<Entity, Object>)metadataKeyReference, NullabilityUtils.copyIfNotNull(o)));
        return entityMetadata;
    }

    @SuppressWarnings("unchecked")
    public static EntityMetadata of(List<? extends MetadataItem<? extends Entity, ?>> items) {
        var entityMetadata = new EntityMetadata();
        for (MetadataItem<? extends Entity, ?> item : items) {
            if (item == null) continue;
            MetadataItem<Entity, Object> copy = (MetadataItem<Entity, Object>) NullabilityUtils.copyIfNotNull(item);
            if (copy == null) {
                Instances.LUSK.getLogger().severe("Failed to copy metadata item " + item);
                continue;
            }
            entityMetadata.set(copy, copy.value());
        }
        return entityMetadata;
    }

    @SafeVarargs
    public static EntityMetadata of(MetadataItem<? extends Entity, ?>... items) {
        return of(Arrays.asList(items));
    }

    public EntityMetadata() {
        this.items = new ArrayList<>();
    }

    public void markAsModified() {
        this.modified = true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends Entity, T> boolean setUnsafe(
            @NotNull MetadataKeyReference<E, T> key,
            Object value
    ) {
        //System.out.println("'setUnsafe' with key: " + key + " and value: " + value);
        if (key instanceof MetadataBitFlagKey bitFlagKey)
            return setBitFlag(bitFlagKey, value);
        if (key.canBeSetTo(value))
            return setInternal(key.id(), key.asItem((T) value));
        else
            return set(key, (T) value);
    }

    @SuppressWarnings("unchecked")
    public <
            E extends Entity,
            B extends Enum<B> & BitFlag<T>,
            F extends FlagByte<B, E, ?>,
            T>
    boolean setBitFlag(
            @NotNull MetadataBitFlagKey<E, B, F, T> key,
            T value
    ) {
        //System.out.println("'setBitFlag' with key: " + key + " and value: " + value);
        var parentKey = key.getParentKey();
        F byteFlag = null;

        if (has(parentKey)) {
            var item = get(parentKey);
            Object existing = item.value();

            if (existing instanceof FlagByte<?, ?, ?> fb)
                byteFlag = (F) fb;
        }

        if (byteFlag == null)
            byteFlag = (F) FlagByte.dynamic(parentKey.frontEndClass());

        byteFlag.setUnsafe(key.getBitFlag(), value);
        return setInternal(parentKey.id(), parentKey.asItem(byteFlag));
    }

    public <E extends Entity, T> EntityMetadata with(
            @NotNull MetadataKeyReference<E, T> key,
            T value
    ) {
        this.set(key, value);
        return this;
    }

    public <E extends Entity, T> EntityMetadata without(
            @NotNull MetadataKeyReference<E, T> key
    ) {
        this.remove(key);
        return this;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends Entity, T> boolean set(
            @NotNull MetadataKeyReference<E, T> key,
            T value
    ) {
        //System.out.println("'set' with key reference: " + key + " and value: " + value);
        assert key.canBeSetTo(value);
        if (key instanceof MetadataBitFlagKey e)
            return setBitFlag(e, value);
        return setInternal(key.id(), key.asItem(value));
    }


    public <E extends Entity, T> boolean setInternal(
            @Range(from = 0, to = 255) int id,
            @NotNull MetadataItem<E, T> item
    ) {
        markAsModified();
        //System.out.println("'setInternal' with id: " + id + " and item: " + item);
        while (items.size() <= id)
            items.add(null);
        items.set(id, item);
        return true;
    }

    public void remove(@Range(from = 0, to = 255) int id) {
        int lastIndex = items.size() - 1;
        if (id == lastIndex)
            items.remove(id);
        else if (0 < id && id < lastIndex)
            items.set(id, null);
    }

    public <E extends Entity, T> void remove(@NotNull MetadataItem<E, T> item) {
        int id = item.id();
        if (item.matchesItem(items.get(id)))
            remove(id);
    }

    public <E extends Entity, T> void remove(@NotNull MetadataKeyReference<E, T> item) {
        int id = item.id();
        if (item.matches(items.get(id)))
            remove(id);
    }

    @Override
    public List<MetadataItem<? extends Entity, ?>> items() {
        return items;
    }

    @Override
    public List<MetadataKey<? extends Entity, ?>> keys() {
        return CommonUtils.map(items, MetadataKeyReference::asKey);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<MetadataKeyReference<? extends Entity, ?>, Object> toMap() {
        Map<MetadataKeyReference<? extends Entity, ?>, Object> map = new HashMap<>();

        for (MetadataItem<? extends Entity, ?> item : items) {
            if (item == null) continue;

            MetadataKeyReference key = item.asKey();
            Object value = item.value();

            map.put(key, value);
        }

        return map;
    }

    @Override
    public EntityMetadata copy() {
        return of(items);
    }
}