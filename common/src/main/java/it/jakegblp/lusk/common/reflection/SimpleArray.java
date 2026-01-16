package it.jakegblp.lusk.common.reflection;

import java.lang.reflect.Array;

public record SimpleArray<T>(T[] array) {
    @SuppressWarnings("unchecked")
    public static <T> T[] create(Class<T> componentType, int length) {
        return (T[]) Array.newInstance(componentType, length);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getComponentType(T[] array) {
        return (Class<T>) array.getClass().getComponentType();
    }

    public Class<T> getComponentType() {
        return getComponentType(array);
    }

}
