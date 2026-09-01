package it.jakegblp.lusk.nms.core.serialization.key;

import java.util.List;

public record ListKey<T>(TypeKey<T> inner, int variant) implements TypeKey<List<T>>, WrapperKey<T> {
    @SuppressWarnings("unchecked")
    @Override
    public Class<List<T>> type() {
        return (Class<List<T>>) (Class<?>) List.class;
    }

    @Override
    public Class<?> rawType() {
        return inner.rawType();
    }
}