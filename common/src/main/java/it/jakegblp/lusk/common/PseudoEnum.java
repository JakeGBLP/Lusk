package it.jakegblp.lusk.common;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static it.jakegblp.lusk.common.CommonUtils.getKnownClass;

public abstract class PseudoEnum<S extends PseudoEnum<S>> {
    private static final Map<Class<?>, Map<String, PseudoEnum<?>>> REGISTRY = new LinkedHashMap<>();

    private final String name;
    private final int ordinal;
    protected final @Nullable S previous;

    @SuppressWarnings("unchecked")
    protected PseudoEnum(@NotNull String name) {
        this.name = Preconditions.checkNotNull(name, "name cannot be null");
        if (this instanceof Validatable<?> validatable && !validatable.check()) {
            this.ordinal = -1;
            this.previous = null;
        } else {
            Class<?> clazz = getKnownClass(this);
            Map<String, PseudoEnum<?>> map = REGISTRY.computeIfAbsent(clazz, _ -> new LinkedHashMap<>());
            this.ordinal = map.size();
            this.previous = map.isEmpty() ? null : (S) Iterables.getLast(map.values());
            map.put(name, this);
            REGISTRY.put(clazz, map);
        }
    }

    public final String name() {
        return name;
    }

    public final int ordinal() {
        return ordinal;
    }

    @SuppressWarnings("unchecked")
    public static <E extends PseudoEnum<?>> @NotNull E[] values(Class<E> type) {
        Collection<E> values = (Collection<E>) REGISTRY.getOrDefault(type, new LinkedHashMap<>()).values();
        return values.toArray((E[]) Array.newInstance(type, values.size()));
    }

    @SuppressWarnings("unchecked")
    public static <E extends PseudoEnum<?>> @NotNull E valueOf(Class<E> type, String name) {
        E value = (E) REGISTRY.getOrDefault(type, Map.of()).get(name);
        if (value == null) throw new IllegalArgumentException("No constant " + name + " for " + type.getSimpleName());
        return value;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return name;
    }
}