package it.jakegblp.lusk.utils;
import java.util.Arrays;

public class ClassUtils {
    /**
     * Checks whether a class has a specific method without checking for parameters.
     * @param clazz the class to search for the method name
     * @param methodName the method name to search for
     * @return whether the method exists in the class
     */
    public static boolean hasMethod(Class<?> clazz, String methodName) {
        return Arrays.stream(clazz.getMethods()).anyMatch(method -> method.getName().equals(methodName));
    }
}