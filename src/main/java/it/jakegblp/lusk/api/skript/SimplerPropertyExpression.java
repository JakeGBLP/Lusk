package it.jakegblp.lusk.api.skript;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class SimplerPropertyExpression<F,T> extends SimplePropertyExpression<F, T> {
    public boolean allowSet() {
        return false;
    }
    public boolean allowReset() {
        return false;
    }
    public boolean allowAdd() {
        return false;
    }
    public boolean allowRemove() {
        return false;
    }
    public boolean allowRemoveAll() {
        return false;
    }
    public boolean allowDelete() {
        return false;
    }

    public void set(F from, T to) {}
    public void add(F from, T to) {}
    public void remove(F from, T to) {}
    public void removeAll(F from, T to) {}
    public void delete(F from) {}
    public void reset(F from) {}

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
        if (mode == Changer.ChangeMode.RESET) {
            for (F from : getExpr().getAll(event)) {
                reset(from);
            }
        } else if (mode == Changer.ChangeMode.DELETE) {
            for (F from : getExpr().getAll(event)) {
                delete(from);
            }
        } else {
            T t = delta == null ? null : (T) delta[0];
            if (mode == Changer.ChangeMode.SET) {
                for (F from : getExpr().getAll(event)) {
                    set(from, t);
                }
            } else if (mode == Changer.ChangeMode.ADD) {
                for (F from : getExpr().getAll(event)) {
                    add(from, t);
                }
            } else if (mode == Changer.ChangeMode.REMOVE) {
                for (F from : getExpr().getAll(event)) {
                    remove(from, t);
                }
            } else if (mode == Changer.ChangeMode.REMOVE_ALL) {
                for (F from : getExpr().getAll(event)) {
                    removeAll(from, t);
                }
            }
        }
    }
}
