package it.jakegblp.lusk.common;

public interface Immutable<T extends Immutable<T>> extends Copyable<T> {

    @SuppressWarnings("unchecked")
    @Override
    default T copy() {
        return (T) this;
    }
}
