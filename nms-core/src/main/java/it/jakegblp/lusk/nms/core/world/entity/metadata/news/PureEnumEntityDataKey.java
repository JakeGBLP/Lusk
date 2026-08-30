package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public class PureEnumEntityDataKey<BaseEntity extends Entity, Type extends Enum<Type>> extends EnumEntityDataKey<BaseEntity, Type, Type> {

    public PureEnumEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, Type> getter, @Nullable BiConsumer<BaseEntity, Type> setter, Type defaultValue) {
        super(name, id, entityClasses, serializer, prettyType, getter, setter, defaultValue);
    }

    public PureEnumEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, Type> getter, @Nullable BiConsumer<BaseEntity, Type> setter) {
        this(name, id, entityClasses, serializer, prettyType, getter, setter, prettyType.getEnumConstants()[0]);
    }

    @Override
    public String toString() {
        return "PureEnumEntityDataKey{type=" + prettyType + "}";
    }

}
