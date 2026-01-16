package it.jakegblp.lusk.common.reflection;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record SimpleMethod(@Nullable Method method) {

    public static final Map<Key, Method> METHOD_CACHE = new ConcurrentHashMap<>();

    public record Key(Class<?> clazz, String name, boolean isStatic, boolean declared, List<Class<?>> params) {}

    public boolean isDeprecated() {
        return method != null && method.isAnnotationPresent(Deprecated.class);
    }

    @SuppressWarnings("unchecked")
    public <F,T> T invoke(F instance, Object... args) {
        if (method == null) return null;
        try {
            return (T) method.invoke(instance, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}