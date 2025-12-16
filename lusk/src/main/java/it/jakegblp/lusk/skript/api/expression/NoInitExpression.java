package it.jakegblp.lusk.skript.api.expression;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class NoInitExpression<T> extends SimpleExpression<T> {

    public abstract T[] get(Event event);

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends T> getReturnType() {
        return null;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        throw new IllegalStateException("Init called on a NoInitExpression");
    }

    /**
     * @return whether an error was encountered during the constructor (init phase), NOT the get method.
     */
    public boolean isError() {
        return false;
    }
}
