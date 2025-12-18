package it.jakegblp.lusk.common.reflection;

import it.jakegblp.lusk.common.CommonUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static it.jakegblp.lusk.common.reflection.SimpleConstructor.CONSTRUCTOR_CACHE;
import static it.jakegblp.lusk.common.reflection.SimpleField.FIELD_CACHE;
import static it.jakegblp.lusk.common.reflection.SimpleMethod.METHOD_CACHE;

public record SimpleClass<T>(@Nullable Class<T> type) {

    static final Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

    public static <T> @Nullable T quickInstance(Class<T> clazz) {
        return new SimpleClass<>(clazz).getConstructor(false).newInstance();
    }

    public static <T> @Nullable T quickInstance(Class<T> clazz, Object @NotNull ... args) {
        return new SimpleClass<>(clazz).getConstructor(false, CommonUtils.map(args, Object::getClass)).newInstance(args);
    }

    public static <T> @Nullable T quickInstance(Class<T> clazz, boolean useCache, Object @NotNull ... args) {
        return new SimpleClass<>(clazz).getConstructor(useCache, CommonUtils.map(args, Object::getClass)).newInstance(args);
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable Class<T> quickClass(@NotNull String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    public static void forceInit(@NotNull Class<?> clazz) {
        try {
            Class.forName(clazz.getName(), true, clazz.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull SimpleClass<?> of(String className) {
        if (className == null) return new SimpleClass<>(null);
        Class<?> clazz = CLASS_CACHE.computeIfAbsent(className, key -> {
            try {
                return Class.forName(key);
            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
        return new SimpleClass<>(clazz);
    }

    @Contract("_ -> new")
    public static <T> @NotNull SimpleClass<T> of(Class<T> clazz) {
        return new SimpleClass<>(clazz);
    }

    @SuppressWarnings("unchecked")
    @Contract("_, _ -> new")
    public @NotNull SimpleConstructor<T> getConstructor(boolean useCache, Class<?> @NotNull ... params) {
        if (type == null) return new SimpleConstructor<>(null);
        SimpleConstructor.Key key = new SimpleConstructor.Key(type, Arrays.asList(params.clone()));
        Function<SimpleConstructor.Key, Constructor<T>> function = k -> {
            try {
                Constructor<T> c = type.getDeclaredConstructor(params);
                c.setAccessible(true);
                return c;
            } catch (Exception e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        };
        Constructor<T> constructor = useCache ? (Constructor<T>) CONSTRUCTOR_CACHE.computeIfAbsent(key, function) : function.apply(key);
        return new SimpleConstructor<>(constructor);
    }

    public @NotNull SimpleConstructor<T> getConstructor(Class<?> @NotNull ... params) {
        return getConstructor(true, params);
    }

    public @NotNull SimpleMethod getMethod(String name, boolean isStatic, boolean declared, Class<?>... params) {
        if (type == null) return new SimpleMethod(null);
        Method m = METHOD_CACHE.computeIfAbsent(new SimpleMethod.Key(type, name, isStatic, declared, Arrays.asList(params.clone())), k -> {
            try {
                Method method = declared ? type.getDeclaredMethod(name, params) : type.getMethod(name, params);
                if (Modifier.isStatic(method.getModifiers()) != isStatic)
                    return null;
                method.setAccessible(true);
                return method;
            } catch (Exception e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
        return new SimpleMethod(m);
    }

    public @NotNull SimpleMethod getMethod(String name, Class<?>... params) {
        return getMethod(name, false, false, params);
    }

    public @NotNull SimpleField<T, ?> getField(String name) {
        return getField(name, false);
    }

    public @NotNull SimpleField<T, ?> getField(String name, boolean isStatic) {
        if (type == null) return new SimpleField<>(null);
        Field f = FIELD_CACHE.computeIfAbsent(new SimpleField.Key(type, name, isStatic), k -> {
            try {
                Field field = type.getDeclaredField(name);
                if (Modifier.isStatic(field.getModifiers()) != isStatic)
                    return null;
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
        return new SimpleField<>(f);
    }

}
