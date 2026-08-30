package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@NullMarked
public class SplitEntityDataKey<BaseEntity extends Entity, InternalType> extends EntityDataKey<BaseEntity, InternalType, InternalType, InternalType, InternalType> {

    public SplitEntityDataKey(
            String name,
            @Range(from = 0, to = 255) int id,
            EntityClasses<BaseEntity> entityClasses,
            EntityDataSerializer<?, ?> serializer,
            Class<InternalType> type,
            Function<BaseEntity, InternalType> getter,
            BiConsumer<BaseEntity, InternalType> setter,
            InternalType defaultValue
    ) {
        super(name, id, entityClasses, serializer, type, getter, setter, defaultValue);
    }

    public <ComponentType> SplitComponentEntityDataKey<BaseEntity, InternalType, ComponentType> component(
            String name,
            Class<ComponentType> componentType,
            Function<InternalType, ComponentType> extractor,
            BiFunction<InternalType, ComponentType, InternalType> inserter,
            ComponentType defaultValue
    ) {
        return new SplitComponentEntityDataKey<>(this, name, componentType, extractor, inserter, defaultValue);
    }

    @Override
    public String toString() {
        return "SplitEntityDataKey{type=" + prettyType + "}";
    }
}