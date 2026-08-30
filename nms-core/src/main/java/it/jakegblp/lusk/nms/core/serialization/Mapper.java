package it.jakegblp.lusk.nms.core.serialization;

import it.jakegblp.lusk.common.Mapping;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

public interface Mapper<F, T> extends Mapping<F, T> {

    static <F, T> Mapper<F, T> of(
            Class<F> fromClass,
            Class<T> toClass,
            Function<@NotNull F, T> to,
            Function<@NotNull T, F> from,
            int variant
    ) {
        return new Mapper<>() {
            @Override
            public @NotNull Class<F> fromClass() {
                return fromClass;
            }

            @Override
            public @NotNull Class<T> toClass() {
                return toClass;
            }

            @Override
            public @NotNull T to(@NotNull F fromValue) {
                return to.apply(fromValue);
            }

            @Override
            public @NotNull F from(@NotNull T toValue) {
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

    @SuppressWarnings("unchecked")
    default Mapper<Optional<F>, Optional<T>> optional() {
        Mapper<F, T> self = this;
        return new Mapper<>() {
            @Override
            public @NotNull Class<Optional<F>> fromClass() {
                return (Class<Optional<F>>) (Class<?>) Optional.class;
            }

            @Override
            public @NotNull Class<Optional<T>> toClass() {
                return (Class<Optional<T>>) (Class<?>) Optional.class;
            }

            @Override
            public @NotNull Optional<T> to(@NotNull Optional<F> from) {
                return from.map(self::to);
            }

            @Override
            public @NotNull Optional<F> from(@NotNull Optional<T> to) {
                return to.map(self::from);
            }

            @Override
            public int variant() {
                return self.variant();
            }
        };
    }

    int variant();
}
