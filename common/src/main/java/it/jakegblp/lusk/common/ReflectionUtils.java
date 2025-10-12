package it.jakegblp.lusk.common;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ReflectionUtils {

    Map<String, Class<?>> CACHED_CLASSES = new ConcurrentHashMap<>();
    Map<Class<?>, Constructor<?>> CACHED_CONSTRUCTORS = new ConcurrentHashMap<>();
    Map<MethodKey, Method> CACHED_METHODS = new ConcurrentHashMap<>();
    Map<FieldKey, Field> CACHED_FIELDS = new ConcurrentHashMap<>();
    Map<FieldKey, Field> CACHED_DECLARED_FIELDS = new ConcurrentHashMap<>();

    static void forceInit(Class<?> clazz) {
        try {
            Class.forName(clazz.getName(), true, clazz.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static Constructor<?> getDeclaredConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        return CACHED_CONSTRUCTORS.computeIfAbsent(clazz, key -> {
            try {
                return key.getDeclaredConstructor(parameterTypes);
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    static Constructor<?> getPrivateDeclaredConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        Constructor<?> constructor = getDeclaredConstructor(clazz, parameterTypes);
        if (constructor == null) return null;
        constructor.setAccessible(true);
        return constructor;
    }

    static Class<?> forClassName(String name) {
        return CACHED_CLASSES.computeIfAbsent(name, key -> {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    static Class<Enum<?>> forEnumClassName(String name) {
        return (Class<Enum<?>>) CACHED_CLASSES.computeIfAbsent(name, key -> {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    static Method getMethod(Class<?> clazz, String name, @Nullable Boolean isStatic, @Nullable Boolean declared, Class<?>... parameterTypes) {
        return CACHED_METHODS.computeIfAbsent(new MethodKey(clazz, name, isStatic, declared, parameterTypes), key -> {
            try {
                Method method = (!Boolean.TRUE.equals(declared)) ? clazz.getMethod(name, parameterTypes) : clazz.getDeclaredMethod(name, parameterTypes);
                if (isStatic == null || Modifier.isStatic(method.getModifiers()) == isStatic) return method;
                return null;
            } catch (NoSuchMethodException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    static Field getField(Class<?> clazz, String name) {
        return CACHED_FIELDS.computeIfAbsent(new FieldKey(clazz, name), key -> {
            try {
                return clazz.getField(name);
            } catch (NoSuchFieldException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    static Field getDeclaredField(Class<?> clazz, String name) {
        return getDeclaredField(clazz, name, false);
    }

    static Field getStaticDeclaredField(Class<?> clazz, String name) {
        return getDeclaredField(clazz, name, true);
    }

    static Field getDeclaredField(Class<?> clazz, String name, boolean isStatic) {
        return CACHED_DECLARED_FIELDS.computeIfAbsent(new FieldKey(clazz, name, isStatic), key -> {
            try {
                Field field = clazz.getDeclaredField(name);
                return Modifier.isStatic(field.getModifiers()) == isStatic ? field : null;
            } catch (NoSuchFieldException e) {
                Bukkit.getLogger().severe("REFLECTION ERROR: " + e.getMessage());
                return null;
            }
        });
    }

    static Object getFieldValueSafely(Field field, Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Object getPrivateFieldValueSafely(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void setPrivateFieldValueSafely(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static Object invokeSafely(Method method, Object object, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Object newInstance(Constructor<?> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Class<?> getFirstGenericType(Object object) {
        Type superclass = object.getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType parameterizedType) {
            Type typeArgument = parameterizedType.getActualTypeArguments()[0];
            if (typeArgument instanceof Class<?>)
                return (Class<?>) typeArgument;
        }
        return null;
    }

    record MethodKey(Class<?> clazz, String name, @Nullable Boolean isStatic, @Nullable Boolean declared, List<Class<?>> parameterTypes) {
        public MethodKey(Class<?> clazz, String name, @Nullable Boolean isStatic, @Nullable Boolean declared, Class<?>... parameterTypes) {
            this(clazz, name, isStatic, declared, Arrays.asList(parameterTypes.clone()));
        }
    }

    record FieldKey(Class<?> clazz, String name, boolean isStatic) {

        public FieldKey(Class<?> clazz, String name) {
            this(clazz, name, false);
        }
    }
}
