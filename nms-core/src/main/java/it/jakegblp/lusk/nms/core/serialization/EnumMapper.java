package it.jakegblp.lusk.nms.core.serialization;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EnumMapper<F extends Enum<F>, T extends Enum<T>> extends Mapper<F, T> {

    static <F extends Enum<F>, T extends Enum<T>> EnumMapper<F, T> of(
            Class<F> fromClass,
            Class<T> toClass
    ) {
        return of(fromClass, toClass, 0);
    }
    static <F extends Enum<F>, T extends Enum<T>> EnumMapper<F, T> of(
            Class<F> fromClass,
            Class<T> toClass,
            int variant
    ) {
        F[] fromValues = fromClass.getEnumConstants();
        T[] toValues = toClass.getEnumConstants();
        int len = Math.min(fromValues.length, toValues.length);
        return new EnumMapper<>() {
            @Override
            public Class<F> fromClass() {
                return fromClass;
            }

            @Override
            public Class<T> toClass() {
                return toClass;
            }

            @Override
            public T to(F from) {
                int i = from.ordinal();
                return toValues[i < len ? i : 0];
            }

            @Override
            public F from(T to) {
                int i = to.ordinal();
                return fromValues[i < len ? i : 0];
            }

            @Override
            public int variant() {
                return variant;
            }
        };
    }

    @Override
    default EnumMapper<F, T> withVariant(int variant) {
        return new EnumMapper<>() {

            @Override
            public Class<F> fromClass() {
                return EnumMapper.this.fromClass();
            }

            @Override
            public Class<T> toClass() {
                return EnumMapper.this.toClass();
            }

            @Override
            public T to(F from) {
                return EnumMapper.this.to(from);
            }

            @Override
            public F from(T to) {
                return EnumMapper.this.from(to);
            }

            @Override
            public int variant() {
                return variant;
            }
        };
    }

}
