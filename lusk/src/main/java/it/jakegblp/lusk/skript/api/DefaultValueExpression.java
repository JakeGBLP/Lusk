package it.jakegblp.lusk.skript.api;

import ch.njol.skript.lang.DefaultExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.function.Supplier;

public class DefaultValueExpression<T> extends SimpleExpression<T> implements DefaultExpression<T> {

    private final Supplier<? extends T> supplier;
    private final Class<T> type;

    public DefaultValueExpression(Class<T> type, Supplier<? extends T> supplier) {
        this.type = type;
        this.supplier = supplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T @Nullable [] get(Event event) {
        T[] array = (T[]) Array.newInstance(type, 1);
        array[0] = supplier.get();
        return array;
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends T> getReturnType() {
        return type;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "default value expressions "+getReturnType().getSimpleName();
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}
