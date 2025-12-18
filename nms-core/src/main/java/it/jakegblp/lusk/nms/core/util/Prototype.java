package it.jakegblp.lusk.nms.core.util;

import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

@NullMarked
public final class Prototype<T, C> {

    private final T original;
    private final UnaryOperator<T> copier;
    private final BiConsumer<? super T, ? super C> mutator;

    private Prototype(T original,
                      UnaryOperator<T> copier,
                      BiConsumer<? super T, ? super C> mutator) {
        this.original = original;
        this.copier = copier;
        this.mutator = mutator;
    }

    public static <T, C> Prototype<T, C> of(
            T original,
            UnaryOperator<T> copier,
            BiConsumer<? super T, ? super C> mutator
    ) {
        return new Prototype<>(original, copier, mutator);
    }

    public T apply(C context) {
        T copy = copier.apply(original);
        mutator.accept(copy, context);
        return copy;
    }
}