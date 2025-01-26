package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

public class SkriptUtils {
    /**
     * Copied from {@link EventValueExpression}
     *
     * Registers an expression as {@link ExpressionType#EVENT} with the provided pattern.
     * This also adds '[the]' to the start of the pattern.
     *
     * @param expression The class that represents this EventValueExpression.
     * @param type The return type of the expression.
     * @param pattern The pattern for this syntax.
     */
    public static <T> void register(Class<? extends EventValueExpression<T>> expression, Class<T> type, String pattern) {
        Skript.registerExpression(expression, type, EVENT_OR_SIMPLE, "[the] " + pattern);
    }
}
