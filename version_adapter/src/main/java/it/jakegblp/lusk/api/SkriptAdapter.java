package it.jakegblp.lusk.api;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.api.enums.GenericRelation;
import org.bukkit.event.Event;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface SkriptAdapter {

    boolean exactComparatorExists(Class<?> firstType, Class<?> secondType);

    <T1, T2> void registerComparator(Class<T1> firstType, Class<T2> secondType, BiFunction<T1, T2, GenericRelation> comparator);

    <From, To> To[] convertUnsafe(From[] from, Class<?> toType, Function<? super From, ? extends To> converter);

    <F, T> void registerConverter(Class<F> from, Class<T> to, Function<F, T> converter);

    <T, E extends Event> void registerEventValue(Class<E> event, Class<T> type, Function<E, T> function, int time);

    <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate);

    <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate, boolean isNegated);

    long getTicks(Timespan timespan);

    long getMillisecond(Timespan timespan);

    Timespan fromTicks(long ticks);
}
