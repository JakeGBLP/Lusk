package it.jakegblp.lusk.api.skript;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class SimplerPropertyExpression<F,T> extends SimplePropertyExpression<F, T> implements Changeable<F,T> {

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
    @SuppressWarnings("unchecked")
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        change(mode, getExpr().getAll(event), delta == null ? null : (T) delta[0]);
    }
}
