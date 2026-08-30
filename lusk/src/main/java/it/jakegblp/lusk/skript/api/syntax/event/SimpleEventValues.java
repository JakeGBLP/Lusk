package it.jakegblp.lusk.skript.api.syntax.event;

import ch.njol.skript.classes.Changer;
import it.jakegblp.lusk.common.Instances;
import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValueRegistry;

import java.util.function.*;

public final class SimpleEventValues {

    @NullMarked
    public static <E extends Event, T> void registerEventValue(
            Class<E> eventClass,
            Class<T> valueClass,
            Function<E, T> getter
    ) {
        Instances.SKRIPT_ADDON.registry(EventValueRegistry.class).register(EventValue.simple(eventClass, valueClass, getter::apply));
    }

    @NullMarked
    public static <E extends Event, T> void registerEventValue(
            Class<E> eventClass,
            Class<T> valueClass,
            Function<E, T> getter,
            BiConsumer<E, T> setter
    ) {
        Instances.SKRIPT_ADDON.registry(EventValueRegistry.class).register(
                EventValue.builder(eventClass, valueClass)
                        .getter(getter::apply)
                        .registerChanger(Changer.ChangeMode.SET, setter::accept)
                        .build());
    }

    @NullMarked
    public static <E extends Event, T> void registerEventValue(
            Class<E> eventClass,
            Class<T> valueClass,
            Function<E, T> getter,
            BiConsumer<E, T> setter,
            BiFunction<T, T, T> add,
            BiFunction<T, T, T> remove
    ) {
        Instances.SKRIPT_ADDON.registry(EventValueRegistry.class).register(
                EventValue.builder(eventClass, valueClass)
                        .getter(getter::apply)
                        .registerChanger(Changer.ChangeMode.SET, setter::accept)
                        .registerChanger(Changer.ChangeMode.ADD, (event, value) -> setter.accept(event, add.apply(getter.apply(event), value)))
                        .registerChanger(Changer.ChangeMode.REMOVE, (event, value) -> setter.accept(event, remove.apply(getter.apply(event), value)))
                        .build());
    }
}
