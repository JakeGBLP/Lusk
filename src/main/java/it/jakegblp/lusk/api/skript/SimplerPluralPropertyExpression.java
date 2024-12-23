package it.jakegblp.lusk.api.skript;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static it.jakegblp.lusk.utils.ClassUtils.getArrayClass;

public abstract class SimplerPluralPropertyExpression<F,T> extends SimplePropertyExpression<F, T>  implements Changeable<F,T[]> {

    public abstract T[] get(F f);

    @Override
    public @Nullable T convert(F from) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T[] get(Event event, F[] source) {
        return (T[]) Arrays.stream(source).flatMap(f -> Arrays.stream(get(f))).toArray();
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> allowSet() ? new Class[] {getArrayClass(getReturnType())} : null;
            case ADD -> allowAdd() ? new Class[] {getArrayClass(getReturnType())} : null;
            case REMOVE -> allowRemove() ? new Class[] {getArrayClass(getReturnType())} : null;
            case REMOVE_ALL -> allowRemoveAll() ? new Class[] {getArrayClass(getReturnType())} : null;
            case RESET -> allowReset() ? new Class[0] : null;
            case DELETE -> allowDelete() ? new Class[0] : null;
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        change(mode, getExpr().getAll(event), (T[]) delta);
    }
}
