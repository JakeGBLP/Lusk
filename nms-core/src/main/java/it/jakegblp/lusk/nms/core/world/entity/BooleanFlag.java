package it.jakegblp.lusk.nms.core.world.entity;

import org.jetbrains.annotations.NotNull;

public interface BooleanFlag extends OrdinalBitFlag<Boolean> {

    static boolean decodeBoolean(int bits) {
        return (bits & 0x1) != 0;
    }

    static int encodeBoolean(boolean value) {
        return value ? 1 : 0;
    }

    @Override
    @NotNull
    default Boolean decode(int bits) {
        return decodeBoolean(bits);
    }

    @Override
    default int encode(Boolean value) {
        return encodeBoolean(value);
    }

    @Override
    default Class<Boolean> getValueClass() {
        return Boolean.class;
    }
}
