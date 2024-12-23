package it.jakegblp.lusk.utils;

import java.util.Arrays;
import java.util.HashSet;

public class ArrayUtils {

    public static <T> boolean haveSameElements(T[] array1, T[] array2) {
        if (array1 == null || array2 == null) return false;
        return new HashSet<>(Arrays.asList(array1)).equals(new HashSet<>(Arrays.asList(array2)));
    }
}
