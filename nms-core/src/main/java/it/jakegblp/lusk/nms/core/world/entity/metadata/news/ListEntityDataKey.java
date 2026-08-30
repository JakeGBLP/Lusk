package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public class ListEntityDataKey<BaseEntity extends Entity, Type> extends EntityDataKey<BaseEntity, List<Type>, List<Type>, Type, Type> {

    public ListEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, List<Type>> getter, BiConsumer<BaseEntity, List<Type>> setter, List<Type> defaultValue) {
        super(name, id, entityClasses, serializer, prettyType, getter, setter, defaultValue);
    }

    public ListEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, List<Type>> getter, BiConsumer<BaseEntity, List<Type>> setter) {
        super(name, id, entityClasses, serializer, prettyType, getter, setter, List.of());
    }

    @Override
    public String toString() {
        return "ListEntityDataKey{type=" + prettyType + "}";
    }
}
