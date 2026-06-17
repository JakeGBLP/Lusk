package it.jakegblp.lusk.nms.core.serialization;

import java.util.Optional;

public record OptionalKey<T>(TypeKey<T> inner, int variant) implements TypeKey<Optional<T>>, WrapperKey<T> {

    @Override
    public Class<Optional<T>> type() {
        return (Class<Optional<T>>) (Class<?>) Optional.class;
    }

    @Override
    public Class<?> rawType() {
        return inner.rawType();
    }
}