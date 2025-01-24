package it.jakegblp.lusk.version.skript.v2_10_0;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.api.SkriptAdapter;
import org.bukkit.event.Event;
import org.skriptlang.skript.lang.converter.Converters;

import java.util.function.Function;
import java.util.function.Predicate;

public class Skript_2_10_0 implements SkriptAdapter {

    @Override
    public <F, T> void registerConverter(Class<F> from, Class<T> to, Function<F, T> converter) {
        Converters.registerConverter(from, to, converter::apply);
    }

    @Override
    public <T, E extends Event> void registerEventValue(Class<E> event, Class<T> type, Function<E, T> function, int time) {
        EventValues.registerEventValue(event, type, function::apply, time);
    }

    public <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate) {
        return expr.check(event, predicate);
    }

    @Override
    public <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate, boolean isNegated) {
        return expr.check(event, predicate, isNegated);
    }

    @Override
    public long getTicks(Timespan timespan) {
        return timespan.getAs(Timespan.TimePeriod.TICK);
    }

    @Override
    public long getMillisecond(Timespan timespan) {
        return timespan.getAs(Timespan.TimePeriod.MILLISECOND);
    }

    @Override
    public Timespan fromTicks(long ticks) {
        return new Timespan(Timespan.TimePeriod.TICK,ticks);
    }

}
