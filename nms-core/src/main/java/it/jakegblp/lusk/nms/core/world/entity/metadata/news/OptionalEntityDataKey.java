package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public class OptionalEntityDataKey<BaseEntity extends Entity, Type> extends EntityDataKey<BaseEntity, Optional<Type>, Optional<Type>, Type, Type> {

    public OptionalEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, @Nullable Type> getter, BiConsumer<BaseEntity, @Nullable Type> setter, @Nullable Type defaultValue) {
        super(name, id, entityClasses, serializer, prettyType, entity -> Optional.ofNullable(getter.apply(entity)), (entity, type) -> setter.accept(entity, type.orElse(null)), Optional.ofNullable(defaultValue));
    }

    public OptionalEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, @Nullable Type> getter, BiConsumer<BaseEntity, @Nullable Type> setter) {
        super(name, id, entityClasses, serializer, prettyType, entity -> Optional.ofNullable(getter.apply(entity)), (entity, type) -> setter.accept(entity, type.orElse(null)), Optional.empty());
    }

    @Override
    public String toString() {
        return "OptionalEntityDataKey{type=" + prettyType + "}";
    }
}
