package it.jakegblp.lusk.utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ReflectionUtils {

    @Nullable
    public static <F extends E, E, T> T getFieldValue(E obj, Class<F> superClass, String fieldName, Class<T> type) {
        try {
            Field field = superClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return type.cast(field.get(obj));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    @Nullable
    public static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
