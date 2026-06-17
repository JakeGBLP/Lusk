package it.jakegblp.lusk.skript.api.syntax.event;

import ch.njol.skript.registrations.EventConverter;
import ch.njol.skript.registrations.EventValues;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public final class SimpleEventValues {
    public static <T, E extends Event> void registerEventValue(
            Class<E> eventClass,
            Class<T> valueClass,
            Function<E, @Nullable T> getter,
            BiConsumer<E, @Nullable T> setter,
            int time
    ) {
        EventValues.registerEventValue(eventClass, valueClass, new EventConverter<>() {
            @Override
            public void set(E event, @Nullable T value) {
                if (value != null) setter.accept(event, value);
            }

            @Override
            public @Nullable T convert(E from) {
                return getter.apply(from);
            }
        }, time);
    }
    public static <T, E extends Event> void registerEventValue(
            Class<E> eventClass,
            Class<T> valueClass,
            Function<E, @Nullable T> getter,
            BiConsumer<E, T> setter
    ) {
        registerEventValue(eventClass, valueClass, getter, setter, EventValues.TIME_NOW);
    }
}
