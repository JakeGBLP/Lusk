package it.jakegblp.lusk.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public class ClassUtils {
    public static String getClassStringWithoutGenerics(String className) {
        return className.replaceAll("<.+>$", "");
    }

    public static String[] getFieldClassGenerics(Field field) {
        return Arrays.stream(((ParameterizedType) field.getGenericType()).getActualTypeArguments())
                .map(type -> getClassStringWithoutGenerics(type.getTypeName()))
                .toArray(String[]::new);
    }
}