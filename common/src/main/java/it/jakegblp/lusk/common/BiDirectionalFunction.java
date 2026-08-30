package it.jakegblp.lusk.common;

import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

public interface BiDirectionalFunction<First, Second> {

    Second asSecond(First first);

    @NullMarked
    First asFirst(Second second);

    static <First, Second> BiDirectionalFunction<First, Second> of(
            Function<First, Second> to,
            Function<Second, First> from
    ) {
        return new BiDirectionalFunction<>() {

            @Override
            public Second asSecond(First fromValue) {
                return to.apply(fromValue);
            }

            @Override
            public First asFirst(Second toValue) {
                return from.apply(toValue);
            }
        };
    }
}
