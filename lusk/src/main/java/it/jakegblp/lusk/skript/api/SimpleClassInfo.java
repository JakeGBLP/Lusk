package it.jakegblp.lusk.skript.api;

import ch.njol.skript.classes.ClassInfo;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class SimpleClassInfo<T, C extends SimpleClassInfo<T, C>> extends ClassInfo<T> {

    protected final boolean canParse;

    /**
     * @param c        The class
     * @param codeName The name used in patterns
     * @param canParse Whether this classinfo can be parsed
     */
    public SimpleClassInfo(Class<T> c, String codeName, boolean canParse) {
        super(c, codeName);
        this.canParse = canParse;
    }

    /**
     * @param c        The class
     * @param codeName The name used in patterns
     */
    public SimpleClassInfo(Class<T> c, String codeName) {
        super(c, codeName);
        this.canParse = false;
    }

    @SuppressWarnings("unchecked")
    public C toString(Function<T, String> toString) {
        return (C) parser(new SimpleParser<>(canParse) {
            @Override
            public @NotNull String toString(T t) {
                return toString.apply(t);
            }
        });
    }



}
