package it.jakegblp.lusk.api.skript;

import ch.njol.skript.classes.Changer;
import org.jetbrains.annotations.Nullable;

public interface Changeable<F,T> {
    /**
     * @return whether this property can be set.
     * {@inheritDoc}
     */
    default boolean allowSet() {
        return false;
    }
    /**
     * @return whether this property can be reset.
     * {@inheritDoc}
     */
    default boolean allowReset() {
        return false;
    }
    /**
     * @return whether this property can be added to.
     * {@inheritDoc}
     */
    default boolean allowAdd() {
        return false;
    }
    /**
     * @return whether this property can be removed from.
     * {@inheritDoc}
     */
    default boolean allowRemove() {
        return false;
    }
    /**
     * @return whether this property can be mass removed from.
     * {@inheritDoc}
     */
    default boolean allowRemoveAll() {
        return false;
    }
    /**
     * @return whether this property can be deleted.
     * {@inheritDoc}
     */
    default boolean allowDelete() {
        return false;
    }

    /**
     * Sets this property for {@link F} to {@link T}.
     * @param from the origin of this property
     * @param to the new value of this property
     * {@inheritDoc}
     */
    default void set(F from, T to) {}
    /**
     * Adds {@link T} to this property for {@link F}.
     * @param from the origin of this property
     * @param to the value to add to this property
     * {@inheritDoc}
     */
    default void add(F from, T to) {}
    /**
     * Removes {@link T} from this property for {@link F}.
     * @param from the origin of this property
     * @param to the value to remove from this property
     * {@inheritDoc}
     */
    default void remove(F from, T to) {}
    /**
     * Removes all of {@link T} from this property for {@link F}.
     * @param from the origin of this property
     * @param to the value to mass remove from this property
     * {@inheritDoc}
     */
    default void removeAll(F from, T to) {}
    /**
     * Deletes this property for {@link F}.
     * @param from the origin of this property
     * {@inheritDoc}
     */
    default void delete(F from) {}
    /**
     * Resets this property for {@link F}.
     * @param from the origin of this property
     * {@inheritDoc}
     */
    default void reset(F from) {}

    default void change(Changer.ChangeMode mode, F[] fArray, @Nullable Object delta) {
        if (mode == Changer.ChangeMode.RESET) {
            for (F from : fArray) {
                reset(from);
            }
        } else if (mode == Changer.ChangeMode.DELETE) {
            for (F from : fArray) {
                delete(from);
            }
        } else {
            T t = (T) delta;
            if (mode == Changer.ChangeMode.SET) {
                for (F from : fArray) {
                    set(from, t);
                }
            } else if (mode == Changer.ChangeMode.ADD) {
                for (F from : fArray) {
                    add(from, t);
                }
            } else if (mode == Changer.ChangeMode.REMOVE) {
                for (F from : fArray) {
                    remove(from, t);
                }
            } else if (mode == Changer.ChangeMode.REMOVE_ALL) {
                for (F from : fArray) {
                    removeAll(from, t);
                }
            }
        }
    }
}
