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

    default void add(List<T> values) {
        get().addAll(values);
    }

    default void remove(List<T> values) {
        get().removeAll(values);
    }

    default void clear() {
        get().clear();
    }
}
