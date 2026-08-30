package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public class BooleanEntityDataKey<BaseEntity extends Entity> extends SimpleEntityDataKey<BaseEntity, Boolean> {

    public BooleanEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Function<BaseEntity, Boolean> getter, @Nullable BiConsumer<BaseEntity, Boolean> setter, boolean defaultValue, boolean inverted) {
        super(name, id, entityClasses, serializer, Boolean.class, entity -> inverted != getter.apply(entity), setter == null ? null : (entity, aBoolean) -> setter.accept(entity, inverted != aBoolean), defaultValue);
    }

    public BooleanEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Function<BaseEntity, Boolean> getter, @Nullable BiConsumer<BaseEntity, Boolean> setter, boolean defaultValue) {
        super(name, id, entityClasses, serializer, Boolean.class, getter, setter, defaultValue);
    }

    @Override
    public String toString() {
        return "BooleanEntityDataKey{}";
    }
}
