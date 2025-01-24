package it.jakegblp.lusk.utils;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.util.Timespan;
import org.bukkit.event.Event;

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
}
