package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@ToString
@AllArgsConstructor
@NullMarked
public class MetadataKey<E extends Entity, T> implements MetadataKeyReference<E, T> {
    private final @Range(from = 0, to = 255) int id;
    private final Class<E> entityClass;
    private final EntityDataSerializer<T> serializer;
    private final Class<T> rawValueClass;

    public MetadataKey(
            @Range(from = 0, to = 255) int id,
            Class<E> entityClass,
            Class<T> valueClass) {
        this(id, entityClass, NMS.getEntityDataSerializer(valueClass), valueClass);
    }

    @Override
    public Class<T> rawValueClass() {
        return rawValueClass;
    }

    @Override
    public @Range(from = 0, to = 255) int id() {
        return id;
    }

    @Override
    public Class<E> entityClass() {
        return entityClass;
    }

    @Override
    public EntityDataSerializer<T> serializer() {
        return serializer;
    }

}