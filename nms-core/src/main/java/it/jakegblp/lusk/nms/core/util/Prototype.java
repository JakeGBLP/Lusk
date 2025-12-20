package it.jakegblp.lusk.nms.core.util;

import org.jspecify.annotations.NullMarked;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

@NullMarked
public class Prototype<F, C, T> {

    private final F original;
    private final UnaryOperator<F> copier;
    private final BiFunction<? super F, ? super C, T> mutator;

    protected Prototype(F original,
                      UnaryOperator<F> copier,
                      BiFunction<? super F, ? super C, T> mutator) {
        this.original = original;
        this.copier = copier;
        this.mutator = mutator;
    }

    public static <F, C, T> Prototype<F, C, T> of(
            F original,
            UnaryOperator<F> copier,
            BiFunction<? super F, ? super C, T> mutator
    ) {
        return new Prototype<>(original, copier, mutator);
    }

    public static <F, C> Prototype<F, C, F> unary(
            F original,
            UnaryOperator<F> copier,
            BiFunction<? super F, ? super C, F> mutator
    ) {
        return new Prototype<>(original, copier, mutator);
    }


    public T apply(C context) {
        return mutator.apply(copier.apply(original), context);
    }
}