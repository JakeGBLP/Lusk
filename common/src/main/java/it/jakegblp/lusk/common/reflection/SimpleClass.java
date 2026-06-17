package it.jakegblp.lusk.common.reflection;

import it.jakegblp.lusk.common.CommonUtils;
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

import static it.jakegblp.lusk.common.reflection.SimpleField.FIELD_CACHE;
import static it.jakegblp.lusk.common.reflection.SimpleMethod.METHOD_CACHE;

public record SimpleClass<T>(@Nullable Class<T> type) {

    static final Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

    public static <T> Class<T> classOf(@NotNull T object) {
        return (Class<T>) object.getClass();
    }

    public static <T> @Nullable T quickInstance(Class<T> clazz) {
        return new SimpleClass<>(clazz).getConstructor(false, true).newInstance();
    }

    public static <T> @Nullable T quickInstance(Class<T> clazz, Object @NotNull ... args) {
        return new SimpleClass<>(clazz).getConstructor(false, true, CommonUtils.map(args, Object::getClass)).newInstance(args);
    }

    public static <T> @Nullable T quickInstance(Class<T> clazz, boolean useCache, Object @NotNull ... args) {
        return new SimpleClass<>(clazz).getConstructor(useCache, true, CommonUtils.map(args, Object::getClass)).newInstance(args);
    }

    public static @NotNull SimpleClass<?> of(String className) {
        if (className == null) return new SimpleClass<>(null);
        Class<?> clazz = CLASS_CACHE.computeIfAbsent(className, key -> {
            try {
                return Class.forName(key);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return new SimpleClass<>(clazz);
    }

    public static <T> @NotNull SimpleClass<T> of(Class<T> clazz) {
        return new SimpleClass<>(clazz);
    }

    @SuppressWarnings("unchecked")
    public @NotNull SimpleConstructor<T> getConstructor(boolean useCache, boolean declared, Class<?> @NotNull ... params) {
        if (type == null) return new SimpleConstructor<>(null);
        SimpleConstructor.Key key = new SimpleConstructor.Key(type, Arrays.asList(params.clone()), declared);
        Function<SimpleConstructor.Key, Constructor<T>> function = k -> {
            try {
                Constructor<T> c = declared ? type.getDeclaredConstructor(params) : type.getConstructor(params);
                c.setAccessible(true);
                return c;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        Constructor<T> constructor = useCache
                ? (Constructor<T>) SimpleConstructor.CONSTRUCTOR_CACHE.computeIfAbsent(key, function)
                : function.apply(key);
        return new SimpleConstructor<>(constructor);
    }

    public @NotNull SimpleConstructor<T> getConstructor(Class<?> @NotNull ... params) {
        return getConstructor(true, false, params);
    }

    public @NotNull SimpleConstructor<T> getDeclaredConstructor(Class<?> @NotNull ... params) {
        return getConstructor(true, true, params);
    }

    public @NotNull SimpleConstructor<T> getDeclaredConstructor(boolean useCache, Class<?> @NotNull ... params) {
        return getConstructor(useCache, true, params);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull Class<T> quickClass(@NotNull String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull Class<T> firstInPackage(@NotNull String packageName, @NotNull String @NotNull ... classNames) {
        for (String className : classNames) {
            try {
                return (Class<T>) Class.forName(packageName + "." + className);
            } catch (ClassNotFoundException ignored) {}
        }
        throw new RuntimeException("No class from array '"+ Arrays.toString(classNames) +"' found in package '" + packageName+ "'");
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
                throw new RuntimeException(e);
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
                throw new RuntimeException("Could not find field '" + name +"' in class '" + type.getSimpleName() + "'", e);
            }
        });
        return new SimpleField<>(f);
    }

    @SuppressWarnings("unchecked")
    public @NotNull SimpleField<T, ?> @NotNull [] getDeclaredFields() {
        if (type == null) return new SimpleField[0];
        return CommonUtils.map(SimpleField.class, type.getDeclaredFields(), SimpleField::new);
    }

    @SuppressWarnings("unchecked")
    public @NotNull SimpleField<T, ?> @NotNull [] getFields() {
        if (type == null) return new SimpleField[0];
        return CommonUtils.map(SimpleField.class, type.getFields(), SimpleField::new);
    }
}

