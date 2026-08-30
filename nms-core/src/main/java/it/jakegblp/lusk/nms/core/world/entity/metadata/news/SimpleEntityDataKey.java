package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public class SimpleEntityDataKey<BaseEntity extends Entity, Type> extends EntityDataKey<BaseEntity, Type, Type, Type, Type> {

    public SimpleEntityDataKey(
            String name,
            @Range(from = 0, to = 255) int id,
            EntityClasses<BaseEntity> entityClasses,
            EntityDataSerializer<?, ?> serializer,
            Class<Type> prettyType,
            Function<BaseEntity, Type> getter,
            @Nullable BiConsumer<BaseEntity, Type> setter,
            Type defaultValue
    ) {
        super(name, id, entityClasses, serializer, prettyType, getter, setter, defaultValue);
    }

    @Override
    public String toString() {
        return "SimpleEntityDataKey{type=" + prettyType + "}";
    }

}
