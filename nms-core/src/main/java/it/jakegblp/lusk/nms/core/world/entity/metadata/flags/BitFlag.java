package it.jakegblp.lusk.nms.core.world.entity.metadata.flags;

import org.jetbrains.annotations.Nullable;

public interface BitFlag<T> {

    int getMask();

    int getShift();

    @Nullable
    T decode(int bits);

    int encode(T value);

    Class<? extends T> getValueClass();
}
