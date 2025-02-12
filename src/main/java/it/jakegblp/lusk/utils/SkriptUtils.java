package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.api.enums.GenericRelation;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;
import static it.jakegblp.lusk.utils.Constants.SKRIPT;

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

    public static Timespan fromTicks(long ticks) {
        return SKRIPT.fromTicks(ticks);
    }

    public static long getTicks(Timespan timespan) {
        return SKRIPT.getTicks(timespan);
    }

    public static long getMilliseconds(Timespan timespan) {
        return SKRIPT.getMillisecond(timespan);
    }

    public static <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate) {
        return SKRIPT.test(expr, event, predicate);
    }

    public static <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate, boolean isNegated) {
        return SKRIPT.test(expr, event, predicate, isNegated);
    }

    public static <T, E extends Event> void registerEventValue(Class<E> event, Class<T> type, Function<E,T> function, int time) {
        SKRIPT.registerEventValue(event, type, function, time);
    }

    public static <F, T> void registerConverter(Class<F> from, Class<T> to, Function<F, T> converter) {
        SKRIPT.registerConverter(from, to, converter);
    }

    public static <From, To> To[] convertUnsafe(From[] from, Class<?> toType, Function<? super From, ? extends To> converter) {
        return SKRIPT.convertUnsafe(from, toType, converter);
    }

    public static <T1, T2> void registerComparator(Class<T1> firstType, Class<T2> secondType, BiFunction<T1, T2, GenericRelation> comparator) {
        SKRIPT.registerComparator(firstType, secondType, comparator);
    }

    public static boolean exactComparatorExists(Class<?> firstType, Class<?> secondType) {
        return SKRIPT.exactComparatorExists(firstType, secondType);
    }

    @Nullable
    public static <T> T getSingle(@Nullable Expression<T> expr, Event event) {
        if (expr == null) return null;
        return expr.getSingle(event);
    }

}
