package it.jakegblp.lusk.api.skript;

import ch.njol.skript.classes.Changer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleBooleanPropertyExpression<F> extends SimplerPropertyExpression<F, Boolean> {

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> allowSet() ? new Class[] {Boolean.class} : null;
            case RESET -> allowReset() ? new Class[0] : null;
            case ADD, DELETE, REMOVE, REMOVE_ALL -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET) {
            for (F from : getExpr().getAll(event)) {
                reset(from);
            }
        } else if (mode == Changer.ChangeMode.SET) {
            Boolean bool = (Boolean) delta[0];
            for (F from : getExpr().getAll(event)) {
                set(from,bool);
            }
        }
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}
