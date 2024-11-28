package it.jakegblp.lusk.utils;

import ch.njol.skript.util.Timespan;
import org.bukkit.attribute.Attribute;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import static it.jakegblp.lusk.utils.Constants.*;

public class DeprecationUtils {

    static {
        Function<Timespan, Long> GET_TICKS_LOCAL;
        if (HAS_TIMESPAN_GET_AS) {
            GET_TICKS_LOCAL = timespan -> timespan.getAs(Timespan.TimePeriod.TICK);
        } else {
            try {
                Method method = Timespan.class.getMethod("getTicks");
                GET_TICKS_LOCAL = timespan -> {
                    try {
                        return (Long) method.invoke(timespan);
                    } catch (IllegalAccessException | InvocationTargetException ignored) {

                    }
                    return null;
                };
            } catch (NoSuchMethodException ignored) {
                GET_TICKS_LOCAL = null;
            }
        }
        GET_TICKS = GET_TICKS_LOCAL;
    }

    public static final Function<Timespan, Long> GET_TICKS;


    @Nullable
    public static Attribute getScaleAttribute() {
        try {
            return (Attribute) Attribute.class.getDeclaredField("GENERIC_SCALE").get(null);
        } catch (final NoSuchFieldException | SecurityException | IllegalAccessException ignored) {}
        if (HAS_SCALE_ATTRIBUTE) {
            return Attribute.SCALE;
        }
        return null;
    }

    /**
     * Replaces deprecated methods and avoid reflection for pre-TimePeriod implementation.
     */
    public static Timespan fromTicks(long ticks) {
        return new Timespan(ticks * 50L);
    }

    public static long getTicks(Timespan timespan) {
        return GET_TICKS.apply(timespan);
    }
}
