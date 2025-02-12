package it.jakegblp.lusk.version.skript.v2_6_4;

import ch.njol.skript.classes.Comparator;
import ch.njol.skript.classes.Converter;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.registrations.Comparators;
import ch.njol.skript.registrations.Converters;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.api.enums.GenericRelation;
import it.jakegblp.lusk.api.SkriptAdapter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Skript_2_6_4 implements SkriptAdapter {

    @Override
    public boolean exactComparatorExists(Class<?> firstType, Class<?> secondType) {
        // This is not exact, but there doesn't seem to be a better way.
        return Comparators.getComparator(firstType, secondType) != null;
    }

    @Override
    public <T1, T2> void registerComparator(Class<T1> firstType, Class<T2> secondType, BiFunction<T1, T2, GenericRelation> comparator) {
        Comparators.registerComparator(firstType, secondType, new Comparator<>() {
            @Override
            public @NotNull Relation compare(T1 o1, T2 o2) {
                return Relation.valueOf(comparator.apply(o1, o2).toString());
            }

            @Override
            public boolean supportsOrdering() {
                return true;
            }
        });
    }

    @Override
    public <From, To> To[] convertUnsafe(From[] from, Class<?> toType, Function<? super From, ? extends To> converter) {
        return Converters.convertUnsafe(from, toType, (Converter<From, To>) converter::apply);
    }

    @Override
    public <F, T> void registerConverter(Class<F> from, Class<T> to, Function<F, T> converter) {
        Converters.registerConverter(from, to, converter::apply);
    }

    @Override
    public <T, E extends Event> void registerEventValue(Class<E> event, Class<T> type, Function<E, T> function, int time) {
        EventValues.registerEventValue(event, type, new Getter<>() {
            @Override
            public @Nullable T get(E arg) {
                return function.apply(arg);
            }
        }, time);
    }

    @Override
    public <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate) {
        return expr.check(event, predicate::test);
    }

    @Override
    public <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate, boolean isNegated) {
        return expr.check(event, predicate::test, isNegated);
    }

    @Override
    public long getTicks(Timespan timespan) {
        return timespan.getTicks_i();
    }

    @Override
    public long getMillisecond(Timespan timespan) {
        return timespan.getMilliSeconds();
    }

    @Override
    public Timespan fromTicks(long ticks) {
        return Timespan.fromTicks_i(ticks);
    }


}
