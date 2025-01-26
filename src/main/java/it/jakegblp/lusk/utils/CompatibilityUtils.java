package it.jakegblp.lusk.utils;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.api.GenericRelation;
import org.bukkit.event.Event;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static it.jakegblp.lusk.utils.Constants.ADAPTER_SKRIPT;

public class CompatibilityUtils {

    public static Timespan fromTicks(long ticks) {
        return ADAPTER_SKRIPT.fromTicks(ticks);
    }

    public static long getTicks(Timespan timespan) {
        return ADAPTER_SKRIPT.getTicks(timespan);
    }

    public static long getMilliseconds(Timespan timespan) {
        return ADAPTER_SKRIPT.getMillisecond(timespan);
    }

    public static <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate) {
        return ADAPTER_SKRIPT.test(expr, event, predicate);
    }

    public static <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate, boolean isNegated) {
        return ADAPTER_SKRIPT.test(expr, event, predicate, isNegated);
    }

    public static <T, E extends Event> void registerEventValue(Class<E> event, Class<T> type, Function<E,T> function, int time) {
        ADAPTER_SKRIPT.registerEventValue(event, type, function, time);
    }

    public static <F, T> void registerConverter(Class<F> from, Class<T> to, Function<F, T> converter) {
        ADAPTER_SKRIPT.registerConverter(from, to, converter);
    }

    public static <From, To> To[] convertUnsafe(From[] from, Class<?> toType, Function<? super From, ? extends To> converter) {
        return ADAPTER_SKRIPT.convertUnsafe(from, toType, converter);
    }

    public static <T1, T2> void registerComparator(Class<T1> firstType, Class<T2> secondType, BiFunction<T1, T2, GenericRelation> comparator) {
        ADAPTER_SKRIPT.registerComparator(firstType, secondType, comparator);
    }

    public static boolean exactComparatorExists(Class<?> firstType, Class<?> secondType) {
        return ADAPTER_SKRIPT.exactComparatorExists(firstType, secondType);
    }
}
