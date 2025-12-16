package it.jakegblp.lusk.skript.api.parser;

import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class SimpleParser<T> extends Parser<T> {

    private final boolean canParse;
    private final Function<String, T> parseFunction;

    public SimpleParser(boolean canParse) {
        this.canParse = canParse;
        this.parseFunction = null;
    }

    public SimpleParser(@NotNull Function<String, T> parseFunction) {
        this.canParse = true;
        this.parseFunction = parseFunction;
    }

    public SimpleParser() {
        this(false);
    }

    @Override
    public @Nullable T parse(String s, ParseContext context) {
        if (canParse) {
            assert parseFunction != null;
            return parseFunction.apply(s);
        } else {
            return null;
        }
    }

    @Override
    public boolean canParse(ParseContext context) {
        return canParse;
    }

    @Override
    public @NotNull String toString(T t, int flags) {
        return toString(t);
    }

    @Override
    public @NotNull String toVariableNameString(T t) {
        return toString(t, 0);
    }

    public abstract @NotNull String toString(T t);
}
