package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public abstract class EnumEntityDataKey<BaseEntity extends Entity, Type extends Enum<Type>, Internal> extends EntityDataKey<BaseEntity, Type, Internal, Type, Internal> {

    protected EnumEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Class<Internal> internalType, Function<BaseEntity, Type> getter, BiConsumer<BaseEntity, Type> setter, Function<Internal, Type> prettifier, Function<Type, Internal> internalizer, Type defaultValue) {
        super(name, id, entityClasses, serializer, prettyType, internalType, entity -> internalizer.apply(getter.apply(entity)), (entity, internal) -> setter.accept(entity, prettifier.apply(internal)), prettifier, internalizer, defaultValue);
    }

    protected EnumEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, Internal> getter, @Nullable BiConsumer<BaseEntity, Internal> setter, Type defaultValue) {
        super(name, id, entityClasses, serializer, prettyType, getter, setter, defaultValue);
    }
}
