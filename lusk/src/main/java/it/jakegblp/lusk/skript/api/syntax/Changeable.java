package it.jakegblp.lusk.skript.api.syntax;

import ch.njol.skript.classes.Changer;
import org.jetbrains.annotations.Nullable;

public interface Changeable<F, T> {

    /**
     * Sets this property for {@link F} to {@link T}.
     *
     * @param from the origin of this property
     * @param to   the new value of this property
     *             {@inheritDoc}
     */
    default void set(F from, T to) {
    }

    /**
     * Adds {@link T} to this property for {@link F}.
     *
     * @param from the origin of this property
     * @param to   the value to add to this property
     *             {@inheritDoc}
     */
    default void add(F from, T to) {
    }

    /**
     * Removes {@link T} from this property for {@link F}.
     *
     * @param from the origin of this property
     * @param to   the value to remove from this property
     *             {@inheritDoc}
     */
    default void remove(F from, T to) {
    }

    /**
     * Removes all of {@link T} from this property for {@link F}.
     *
     * @param from the origin of this property
     * @param to   the value to mass remove from this property
     *             {@inheritDoc}
     */
    default void removeAll(F from, T to) {
    }

    /**
     * Deletes this property for {@link F}.
     *
     * @param from the origin of this property
     *             {@inheritDoc}
     */
    default void delete(F from) {
    }

    /**
     * Resets this property for {@link F}.
     *
     * @param from the origin of this property
     *             {@inheritDoc}
     */
    default void reset(F from) {
    }

    default void change(Changer.ChangeMode mode, F[] fArray, @Nullable T tArray) {
        switch (mode) {
            case RESET -> {
                for (F from : fArray)
                    reset(from);
            }
            case DELETE -> {
                for (F from : fArray)
                    delete(from);
            }
            case SET -> {
                for (F from : fArray)
                    set(from, tArray);
            }
            case ADD -> {
                for (F f : fArray)
                    add(f, tArray);
            }
            case REMOVE -> {
                for (F from : fArray)
                    remove(from, tArray);
            }
            case REMOVE_ALL -> {
                for (F from : fArray)
                    removeAll(from, tArray);
            }
        }
    }
}