package it.jakegblp.lusk.nms.core.world.entity.serialization;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record EntityDataAccessor<T>(int id, EntityDataSerializer<T> serializer) {
    public EntityDataAccessor {
        if (id < 0)
            throw new IllegalArgumentException("id must not be negative");
    }
}
