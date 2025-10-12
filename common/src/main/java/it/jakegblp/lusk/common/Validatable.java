package it.jakegblp.lusk.common;

import java.lang.reflect.Array;
import java.util.function.Supplier;

public interface Validatable<E extends Exception> {

    static <T extends Enum<T> & Validatable<?>> T[] filterValidatableEnum(Class<T> c) {
        return filterValidatableArray(c, c.getEnumConstants());
    }

    static <T extends Validatable<?>> T[] filterValidatableArray(Class<T> c, T[] array) {
        int count = 0;
        for (T t : array) if (t.check()) count++;
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(c, count);
        int idx = 0;
        for (T t : array) if (t.check()) result[idx++] = t;
        return result;
    }

    boolean check();

    Supplier<E> getExceptionSupplier();

    default E getException() {
        return getExceptionSupplier().get();
    }

    default void validate() throws E {
        if (!check()) throw getException();
    }
}
