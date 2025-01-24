package it.jakegblp.lusk.api;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.util.Timespan;
import org.bukkit.event.Event;

import java.util.function.Function;
import java.util.function.Predicate;

public interface SkriptAdapter {

    <F, T> void registerConverter(Class<F> from, Class<T> to, Function<F, T> converter);

    <T, E extends Event> void registerEventValue(Class<E> event, Class<T> type, Function<E, T> function, int time);

    <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate);

    <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate, boolean isNegated);

    long getTicks(Timespan timespan);

    long getMillisecond(Timespan timespan);

    Timespan fromTicks(long ticks);
}
