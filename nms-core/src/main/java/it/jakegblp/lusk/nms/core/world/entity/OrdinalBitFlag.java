package it.jakegblp.lusk.nms.core.world.entity;

public interface OrdinalBitFlag<T> extends BitFlag<T> {
    @Override
    default int getShift() {
        if (this instanceof Enum<?> e)
            return e.ordinal();
        throw new IllegalStateException("This interface can only be implemented by enums.");
    }

    @Override
    default int getMask() {
        return 1 << getShift();
    }
}
