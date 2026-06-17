package it.jakegblp.lusk.common.reflection;

import it.jakegblp.lusk.common.CommonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record SimpleConstructor<T>(@Nullable Constructor<T> constructor) {

    public static final Map<Key, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();

    public record Key(Class<?> clazz, List<Class<?>> params, boolean declared) {
        public Key(Class<?> clazz) {
            this(clazz, List.of(), false);
        }
    }

    public boolean isDeprecated() {
        return constructor != null && constructor.isAnnotationPresent(Deprecated.class);
    }

    public @Nullable T newInstance(Object... args) {
        if (constructor == null) return null;
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> @Nullable T quickNewInstance(Class<T> clazz) {
        return new SimpleClass<>(clazz).getConstructor(false, true).newInstance();
    }

    public static <T> @Nullable T quickNewInstance(Class<T> clazz, Object @NotNull ... args) {
        return new SimpleClass<>(clazz).getConstructor(false, true, CommonUtils.map(args, Object::getClass)).newInstance(args);
    }
}