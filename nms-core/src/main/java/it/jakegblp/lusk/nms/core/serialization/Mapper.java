package it.jakegblp.lusk.nms.core.serialization;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public interface Mapper<F, T> {

    static <F, T> Mapper<F, T> of(
            Class<F> fromClass,
            Class<T> toClass,
            Function<@NotNull F, T> to,
            Function<@NotNull T, F> from,
            int variant
    ) {
        return new Mapper<>() {
            @Override
            public Class<F> fromClass() {
                return fromClass;
            }

            @Override
            public Class<T> toClass() {
                return toClass;
            }

            @Override
            public T to(@NotNull F fromValue) {
                return to.apply(fromValue);
            }

            @Override
            public F from(@NotNull T toValue) {
                return from.apply(toValue);
            }

            @Override
            public int variant() {
                return variant;
            }
        };
    }

    static <F, T> Mapper<F, T> of(
            Class<F> fromClass,
            Class<T> toClass,
            Function<@NotNull F, T> to,
            Function<@NotNull T, F> from
    ) {
        return of(fromClass, toClass, to, from, 0);
    }

    default Mapper<F, T> withVariant(int variant) {
        return Mapper.of(fromClass(), toClass(), this::to, this::from, variant);
    }

    @SuppressWarnings("unchecked") // todo: make standalone class
    default Mapper<Optional<F>, Optional<T>> optional() {
        Mapper<F, T> self = this;
        return new Mapper<>() {
            @Override
            public Class<Optional<F>> fromClass() {
                return (Class<Optional<F>>) (Class<?>) Optional.class;
            }

            @Override
            public Class<Optional<T>> toClass() {
                return (Class<Optional<T>>) (Class<?>) Optional.class;
            }

            @Override
            public Optional<T> to(@NotNull Optional<F> from) {
                return from.map(self::to);
            }

            @Override
            public Optional<F> from(@NotNull Optional<T> to) {
                return to.map(self::from);
            }

            @Override
            public int variant() {
                return self.variant();
            }
        };
    }

    Class<F> fromClass();

    Class<T> toClass();

    T to(@NotNull F from);

    F from(@NotNull T to);

    int variant();
}
