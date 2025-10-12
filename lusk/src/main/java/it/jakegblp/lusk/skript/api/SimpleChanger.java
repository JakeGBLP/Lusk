package it.jakegblp.lusk.skript.api;

public interface SimpleChanger<C, V> extends GenericSimpleChanger<C, V> {

    @Override
    default boolean isSingle() {
        return true;
    }

    default void set(C change, V delta) {}
    default void add(C change, V delta) {}
    default void remove(C change, V delta) {}
    default void removeAll(C change, V delta) {}
}
