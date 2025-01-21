package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Timespan;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.jakegblp.lusk.utils.Constants.*;
import static it.jakegblp.lusk.utils.LuskUtils.warning;

public class DeprecationUtils {

    static {
        Function<Timespan, Long> GET_TICKS_LOCAL;
        if (SKRIPT_HAS_TIMESPAN_TIMEPERIOD) {
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

    /**
     * Replaces deprecated methods and avoids reflection for pre-TimePeriod implementation.
     */
    public static Timespan fromTicks(long ticks) {
        return new Timespan(ticks * 50L);
    }

    public static long getTicks(Timespan timespan) {
        return GET_TICKS.apply(timespan);
    }

    public static long getMilliseconds(Timespan timespan) {
        return getTicks(timespan) * 50;
    }

    @SuppressWarnings("all")
    public static <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate, Class<T> type) {
        if (SKRIPT_2_10) {
            return expr.check(event, predicate);
        } else {
            try {
                Class<?> checkerClass = Class.forName("ch.njol.util.Checker");

                Object checkerInstance = Proxy.newProxyInstance(
                        checkerClass.getClassLoader(),
                        new Class<?>[]{checkerClass},
                        (proxy, method, args) -> {
                            if ("check".equals(method.getName()) && args != null && args.length == 1) {
                                Object arg = args[0];
                                if (type.isInstance(arg)) return predicate.test(type.cast(arg));
                            }
                            return null;
                        }
                );

                return (boolean) expr.getClass().getMethod("check", Event.class, checkerClass).invoke(expr, event, checkerInstance);
            } catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
                warning("Something went wrong with a condition: {0}", e.getMessage());
            }
        }
        return false;
    }

    public static <T> boolean test(Expression<T> expr, Event event, Predicate<T> predicate, Class<T> type, @Nullable Boolean negated) {
        boolean bool = test(expr, event, predicate, type);
        if (negated != null) return bool ^ negated;
        return bool;
    }

    @SuppressWarnings("all")
    public static <T, E extends Event> void registerEventValue(Class<E> event, Class<T> type, Function<E,T> function, int time) {
        if (SKRIPT_2_10) {
            EventValues.registerEventValue(event, type, function::apply, time);
        } else {
            try {
                Class<?> getterClass = Class.forName("ch.njol.skript.util.Getter");

                ProxyFactory factory = new ProxyFactory();
                factory.setSuperclass(getterClass);
                factory.setFilter(new MethodFilter() {
                    @Override
                    public boolean isHandled(Method method) {
                        return method.getName().equals("get");
                    }
                });
                MethodHandler handler = new MethodHandler() {
                    @Override
                    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                        if (args != null && args.length == 1) {
                            Object arg = args[0];
                            if (type.isInstance(arg)) return function.apply(event.cast(arg));
                        }
                        return null;
                    }
                };

                Object getterInstance = factory.create(new Class<?>[0], new Object[0], handler);

                Method registerMethod = EventValues.class.getDeclaredMethod(
                        "registerEventValue",
                        Class.class,
                        Class.class,
                        getterClass,
                        int.class
                );
                registerMethod.invoke(null, event, type, getterInstance, time);
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                     IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("all")
    public static <F, T> void registerConverter(Class<F> from, Class<T> to, Function<F, T> converter) {
        if (SKRIPT_2_7) {
            Converters.registerConverter(from, to, converter::apply);
        } else {
            Logger skriptLogger = Skript.getInstance().getLogger();
            Level logLevel = skriptLogger.getLevel();
            skriptLogger.setLevel(Level.OFF);
            try {
                Class<?> converterClass = Class.forName("ch.njol.skript.classes.Converter");

                Object converterInstance = Proxy.newProxyInstance(
                        converterClass.getClassLoader(),
                        new Class<?>[]{converterClass},
                        (proxy, method, args) -> {
                            if ("convert".equals(method.getName()) && args != null && args.length == 1) {
                                Object arg = args[0];
                                if (from.isInstance(arg)) return converter.apply(from.cast(arg));
                            }
                            return null;
                        }
                );

                Method registerConverterMethod = Class.forName("ch.njol.skript.registrations.Converters").getMethod("registerConverter", Class.class, Class.class, converterClass);
                registerConverterMethod.invoke(from, to, converterInstance);
                skriptLogger.setLevel(logLevel);
            } catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
                Skript.warning("Something went wrong with converters: " + e.getMessage());
            }
        }
    }
}
