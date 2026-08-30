package it.jakegblp.lusk.common;

public interface Copyable<T extends Copyable<T>> {

    T copy();

}
