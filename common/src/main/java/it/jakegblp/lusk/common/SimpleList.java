package it.jakegblp.lusk.common;

import java.util.List;

public interface SimpleList<T> {

    List<T> get();

    default boolean isEmpty() {
        return get().isEmpty();
    }

    default int size() {
        return get().size();
    }

    default void set(List<T> values) {
        clear();
        add(values);
    }

    default void set(T[] values) {
        clear();
        add(values);
    }

    default void add(List<T> values) {
        get().addAll(values);
    }
    default void add(T[] values) {
        for (T value : values)
            get().add(value);
    }

    default void remove(List<T> values) {
        get().removeAll(values);
    }
    default void remove(T[] values) {
        for (T value : values)
            get().remove(value);
    }

    default void clear() {
        get().clear();
    }
}
