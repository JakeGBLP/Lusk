package it.jakegblp.lusk.common;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;

public abstract class PseudoEnum {
    private static final Map<Class<?>, Map<String, PseudoEnum>> REGISTRY = new HashMap<>();

    private final String name;

    protected PseudoEnum(String name) {
        this.name = Objects.requireNonNull(name, "name");
        if (!(this instanceof Validatable<?> validatable) || validatable.check())
            REGISTRY.computeIfAbsent(getClass(), k -> new LinkedHashMap<>()).put(name, this);
    }

    public final String name() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public static <E extends PseudoEnum> @NotNull E[] values(Class<E> type) {
        Collection<E> values = (Collection<E>) REGISTRY
                .getOrDefault(type, new LinkedHashMap<>())
                .values();
        return values.toArray((E[]) Array.newInstance(type, values.size()));
    }

    @SuppressWarnings("unchecked")
    public static <E extends PseudoEnum> @NotNull E valueOf(Class<E> type, String name) {
        E value = (E) REGISTRY.getOrDefault(type, Map.of()).get(name);
        if (value == null) throw new IllegalArgumentException("No constant " + name + " for " + type.getSimpleName());
        return value;
    }

    @Override
    public final boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public final int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return name;
    }
}