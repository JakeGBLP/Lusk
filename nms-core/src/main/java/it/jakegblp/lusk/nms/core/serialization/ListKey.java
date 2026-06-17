package it.jakegblp.lusk.nms.core.serialization;

import java.util.List;

public record ListKey<T>(TypeKey<T> inner, int variant) implements TypeKey<List<T>>, WrapperKey<T> {
    @Override
    public Class<List<T>> type() {
        return (Class<List<T>>) (Class<?>) List.class;
    }

    @Override
    public Class<?> rawType() {
        return inner.rawType();
    }
}