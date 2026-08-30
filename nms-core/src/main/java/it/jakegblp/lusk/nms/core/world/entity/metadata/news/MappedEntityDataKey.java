package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public class MappedEntityDataKey<BaseEntity extends Entity, PrettyType, InternalType> extends EntityDataKey<BaseEntity, PrettyType, InternalType, PrettyType, InternalType> {

    public MappedEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<PrettyType> prettyType, Class<InternalType> internalType, Function<BaseEntity, PrettyType> getter, BiConsumer<BaseEntity, PrettyType> setter, Function<InternalType, PrettyType> prettifier, Function<PrettyType, InternalType> internalizer, PrettyType defaultValue) {
        super(name, id, entityClasses, serializer, prettyType, internalType, entity -> internalizer.apply(getter.apply(entity)), (entity, internal) -> setter.accept(entity, prettifier.apply(internal)), prettifier, internalizer, defaultValue);
    }

    @Override
    public String toString() {
        return "MappedEntityDataKey{type=" + prettyType + "}";
    }

}
