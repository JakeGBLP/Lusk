package it.jakegblp.lusk.nms.core.serialization;

public record HolderKey<T>(TypeKey<T> inner, int variant) implements TypeKey<T>, WrapperKey<T> {

    @Override
    public Class<T> type() {
        return inner.type();
    }

    @Override
    public Class<?> rawType() {
        return inner.rawType();
    }
}