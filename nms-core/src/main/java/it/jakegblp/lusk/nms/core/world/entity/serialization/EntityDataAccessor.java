package it.jakegblp.lusk.nms.core.world.entity.serialization;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("rawtypes")
public record EntityDataAccessor<T>(int id, EntityDataSerializer serializer) {
    public EntityDataAccessor {
        if (id < 0)
            throw new IllegalArgumentException("id must not be negative");
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EntityDataAccessor<?> other && this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Contract(pure = true)
    @Override
    public String toString() {
        return "EntityDataAccessor(id=" + id + ")";
    }
}
