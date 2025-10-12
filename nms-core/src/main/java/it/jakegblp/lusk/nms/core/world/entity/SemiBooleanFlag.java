package it.jakegblp.lusk.nms.core.world.entity;

import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.nms.core.world.entity.BooleanFlag.decodeBoolean;
import static it.jakegblp.lusk.nms.core.world.entity.BooleanFlag.encodeBoolean;

public interface SemiBooleanFlag<T> extends OrdinalBitFlag<T> {

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    default T decode(int bits) {
        return (T)(Boolean)decodeBoolean(bits);
    }

    @Override
    default int encode(T value) {
        return encodeBoolean((Boolean) value);
    }

    @Override
    @SuppressWarnings("unchecked")
    default Class<T> getValueClass() {
        return (Class<T>) Boolean.class;
    }
}
