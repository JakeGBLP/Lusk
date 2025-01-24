package it.jakegblp.lusk.version.skript.v2_8_7;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.api.SkriptAdapter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converters;

import java.util.function.Function;
import java.util.function.Predicate;

public class Skript_2_8_7 implements SkriptAdapter {

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
        return timespan.getTicks();
    }

    @Override
    public long getMillisecond(Timespan timespan) {
        return timespan.getMilliSeconds();
    }

    @Override
    public Timespan fromTicks(long ticks) {
        return Timespan.fromTicks(ticks);
    }

}
