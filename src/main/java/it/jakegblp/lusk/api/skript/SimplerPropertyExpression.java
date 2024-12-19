package it.jakegblp.lusk.api.skript;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class SimplerPropertyExpression<F,T> extends SimplePropertyExpression<F, T> implements Changeable<F,T> {

    ///**
    // * @return whether this property can be set.
    // * {@inheritDoc}
    // */
    //public boolean allowSet() {
    //    return false;
    //}
    ///**
    // * @return whether this property can be reset.
    // * {@inheritDoc}
    // */
    //public boolean allowReset() {
    //    return false;
    //}
    ///**
    // * @return whether this property can be added to.
    // * {@inheritDoc}
    // */
    //public boolean allowAdd() {
    //    return false;
    //}
    ///**
    // * @return whether this property can be removed from.
    // * {@inheritDoc}
    // */
    //public boolean allowRemove() {
    //    return false;
    //}
    ///**
    // * @return whether this property can be mass removed from.
    // * {@inheritDoc}
    // */
    //public boolean allowRemoveAll() {
    //    return false;
    //}
    ///**
    // * @return whether this property can be deleted.
    // * {@inheritDoc}
    // */
    //public boolean allowDelete() {
    //    return false;
    //}
//
    ///**
    // * Sets this property for {@link F} to {@link T}.
    // * @param from the origin of this property
    // * @param to the new value of this property
    // * {@inheritDoc}
    // */
    //public void set(F from, T to) {}
    ///**
    // * Adds {@link T} to this property for {@link F}.
    // * @param from the origin of this property
    // * @param to the value to add to this property
    // * {@inheritDoc}
    // */
    //public void add(F from, T to) {}
    ///**
    // * Removes {@link T} from this property for {@link F}.
    // * @param from the origin of this property
    // * @param to the value to remove from this property
    // * {@inheritDoc}
    // */
    //public void remove(F from, T to) {}
    ///**
    // * Removes all of {@link T} from this property for {@link F}.
    // * @param from the origin of this property
    // * @param to the value to mass remove from this property
    // * {@inheritDoc}
    // */
    //public void removeAll(F from, T to) {}
    ///**
    // * Deletes this property for {@link F}.
    // * @param from the origin of this property
    // * {@inheritDoc}
    // */
    //public void delete(F from) {}
    ///**
    // * Resets this property for {@link F}.
    // * @param from the origin of this property
    // * {@inheritDoc}
    // */
    //public void reset(F from) {}

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> allowSet() ? new Class[] {getReturnType()} : null;
            case ADD -> allowAdd() ? new Class[] {getReturnType()} : null;
            case REMOVE -> allowRemove() ? new Class[] {getReturnType()} : null;
            case REMOVE_ALL -> allowRemoveAll() ? new Class[] {getReturnType()} : null;
            case RESET -> allowReset() ? new Class[0] : null;
            case DELETE -> allowDelete() ? new Class[0] : null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        change(mode, getExpr().getAll(event), (T) delta[0]);
        //if (mode == Changer.ChangeMode.RESET) {
        //    for (F from : getExpr().getAll(event)) {
        //        reset(from);
        //    }
        //} else if (mode == Changer.ChangeMode.DELETE) {
        //    for (F from : getExpr().getAll(event)) {
        //        delete(from);
        //    }
        //} else {
        //    T t = delta == null ? null : (T) delta[0];
        //    if (mode == Changer.ChangeMode.SET) {
        //        for (F from : getExpr().getAll(event)) {
        //            set(from, t);
        //        }
        //    } else if (mode == Changer.ChangeMode.ADD) {
        //        for (F from : getExpr().getAll(event)) {
        //            add(from, t);
        //        }
        //    } else if (mode == Changer.ChangeMode.REMOVE) {
        //        for (F from : getExpr().getAll(event)) {
        //            remove(from, t);
        //        }
        //    } else if (mode == Changer.ChangeMode.REMOVE_ALL) {
        //        for (F from : getExpr().getAll(event)) {
        //            removeAll(from, t);
        //        }
        //    }
        //}
    }
}
