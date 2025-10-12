package it.jakegblp.lusk.nms.core.world.entity;

import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.nms.core.world.entity.BooleanFlag.*;

public interface SemiBooleanFlag<T> extends OrdinalBitFlag<T> {

    @Override
    @Nullable
    default T decode(int bits) {
        return (T)(Boolean)decodeBoolean(bits);
    }

    @Override
    default int encode(T value) {
        return encodeBoolean((Boolean) value);
    }

    @Override
    default Class<T> getValueClass() {
        return (Class<T>) Boolean.class;
    }
}
