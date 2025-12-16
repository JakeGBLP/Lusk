package it.jakegblp.lusk.common.reflection;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record SimpleField<F, T>(Field field) {

    public static final Map<Key, Field> FIELD_CACHE = new ConcurrentHashMap<>();

    public record Key(Class<?> clazz, String name, boolean isStatic) {
        public Key(Class<?> clazz, String name) {
            this(clazz, name, false);
        }
    }

    @SuppressWarnings("unchecked")
    public T get(F instance) {
        try {
            return (T) field.get(instance);
        } catch (Exception ignored) {
            return null;
        }
    }

    public void set(F instance, T value) {
        try {
            field.set(instance, value);
        } catch (Exception ignored) {}
    }
}