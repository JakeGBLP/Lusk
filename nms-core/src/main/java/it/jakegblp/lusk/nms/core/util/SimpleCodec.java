package it.jakegblp.lusk.nms.core.util;

import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

@NullMarked
public interface SimpleCodec<F, T> {

    Class<F> fromClass();

    Class<T> toClass();

    T to(F from);

    F from(T to);

    static <F, T> SimpleCodec<F, T> of(
            Class<F> fromClass,
            Class<T> toClass,
            Function<F, T> to,
            Function<T, F> from
    ) {
        return new SimpleCodec<>() {
            @Override
            public Class<F> fromClass() { return fromClass; }

            @Override
            public Class<T> toClass() { return toClass; }

            @Override
            public T to(F fromValue) { return to.apply(fromValue); }

            @Override
            public F from(T toValue) { return from.apply(toValue); }
        };
    }
}
