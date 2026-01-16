//package it.jakegblp.lusk.nms.core.world.entity.metadata;
//
//import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BitFlag;
//import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
//import lombok.EqualsAndHashCode;
//import org.bukkit.entity.Entity;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Range;
//
//import java.util.*;
//
///**
// * See: <a href="https://minecraft.wiki/w/Java_Edition_protocol/Entity_metadata#Entity">Minecraft Wiki – Entity Metadata</a>
// */
//@EqualsAndHashCode
//public class EntityMetadata implements EntityMetadataView, Cloneable {
//    // todo: cloning
//    private final List<MetadataItem<? extends Entity, ?>> items;
//
//    public EntityMetadata(Map<? extends MetadataKeyReference<? extends Entity, ?>, ?> metadata) {
//        this();
//        // temporary solution
//        metadata.forEach((metadataKeyReference, o) ->
//                set((MetadataKeyReference<Entity, Object>)metadataKeyReference, o)
//        );
//    }
//
//    public EntityMetadata() {
//        this(List.of());
//    }
//
//    protected EntityMetadata(List<MetadataItem<? extends Entity, ?>>  items) {
//        this.items = new ArrayList<>(items);
//    }
//
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public <E extends Entity, T> boolean setUnsafe(
//            @NotNull MetadataKeyReference<E, T> key,
//            Object value
//    ) {
//        System.out.println("'setUnsafe' with key: " + key + " and value: " + value);
//        if (key instanceof MetadataBitFlagKey bitFlagKey)
//            return setBitFlag(bitFlagKey, value);
//        if (key.canBeSetTo(value))
//            return setInternal(key.id(), key.asItem((T) value));
//        else
//            return set(key, (T) value);
//    }
//
//    @SuppressWarnings("unchecked")
//    public <
//            E extends Entity,
//            B extends Enum<B> & BitFlag<T>,
//            F extends FlagByte<B, E, ?>,
//            T>
//    boolean setBitFlag(
//            @NotNull MetadataBitFlagKey<E, B, F, T> key,
//            T value
//    ) {
//        System.out.println("'setBitFlag' with key: " + key + " and value: " + value);
//        var parentKey = key.getParentKey();
//        F byteFlag = null;
//        if (has(parentKey)) {
//            var item = get(parentKey);
//            byteFlag = item.value();
//        }
//        if (byteFlag == null)
//            byteFlag = (F) FlagByte.dynamic(parentKey.valueClass());
//        byteFlag.setUnsafe(key.getBitFlag(), value);
//        return setInternal(parentKey.id(), parentKey.asItem(byteFlag));
//    }
//
//    public <E extends Entity, T> EntityMetadata with(
//            @NotNull MetadataKeyReference<E, T> key,
//            T value
//    ) {
//        this.set(key, value);
//        return this;
//    }
//
//    public <E extends Entity, T> EntityMetadata without(
//            @NotNull MetadataKeyReference<E, T> key
//    ) {
//        this.remove(key);
//        return this;
//    }
//
//
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public <E extends Entity, T> boolean set(
//            @NotNull MetadataKeyReference<E, T> key,
//            T value
//    ) {
//        System.out.println("'set' with key reference: " + key + " and value: " + value);
//        assert key.canBeSetTo(value);
//        if (key instanceof MetadataBitFlagKey e)
//            return setBitFlag(e, value);
//        return setInternal(key.id(), key.asItem(value));
//    }
//
//
//    public <E extends Entity, T> boolean setInternal(
//            @Range(from = 0, to = 255) int id,
//            @NotNull MetadataItem<E, T> item
//    ) {
//        System.out.println("'setInternal' with id: " + id + " and item: " + item);
//        while (items.size() <= id)
//            items.add(null);
//        items.set(id, item);
//        return true;
//    }
//
//
//    public void remove(@Range(from = 0, to = 255) int id) {
//        int lastIndex = items.size() - 1;
//        if (id == lastIndex)
//            items.remove(id);
//        else if (0 < id && id < lastIndex)
//            items.set(id, null);
//    }
//
//    public <E extends Entity, T> void remove(@NotNull MetadataItem<E, T> item) {
//        int id = item.id();
//        if (item.matchesItem(items.get(id)))
//            remove(id);
//    }
//
//    public <E extends Entity, T> void remove(@NotNull MetadataKeyReference<E, T> item) {
//        int id = item.id();
//        if (item.matches(items.get(id)))
//            remove(id);
//    }
//
//
//    @Override
//    public List<MetadataItem<? extends Entity, ?>> items() {
//        return items;
//    }
//
//    @SuppressWarnings({"unchecked", "rawtypes"})
//    public Map<MetadataKeyReference<? extends Entity, ?>, Object> toMap() {
//        Map<MetadataKeyReference<? extends Entity, ?>, Object> map = new HashMap<>();
//
//        for (MetadataItem<? extends Entity, ?> item : items) {
//            if (item == null) continue;
//
//            MetadataKeyReference key = item.asKey();
//            Object value = item.value();
//
//            map.put(key, value);
//        }
//
//        return map;
//    }
//
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone(); // todo: finish
//    }
//