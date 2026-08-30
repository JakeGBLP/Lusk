package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public class EnumByteEntityDataKey<BaseEntity extends Entity, Type extends Enum<Type>> extends EnumEntityDataKey<BaseEntity, Type, Byte> {

    public EnumByteEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, Type> getter, BiConsumer<BaseEntity, Type> setter, Type defaultValue) {
        super(name, id, entityClasses, serializer, prettyType, Byte.class, getter, setter, aByte -> prettyType.getEnumConstants()[aByte], type -> (byte) type.ordinal(), defaultValue);
    }

    public EnumByteEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, Type> getter, BiConsumer<BaseEntity, Type> setter) {
        this(name, id, entityClasses, serializer, prettyType, getter, setter, prettyType.getEnumConstants()[0]);
    }

    @Override
    public String toString() {
        return "EnumByteEntityDataKey{type=" + prettyType + "}";
    }
}
