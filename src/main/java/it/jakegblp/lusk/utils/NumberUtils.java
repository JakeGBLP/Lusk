package it.jakegblp.lusk.utils;

import static it.jakegblp.lusk.utils.Constants.EPSILON;

public class NumberUtils {
    public static boolean areDoublesRoughlyEqual(double a, double b) {
        return Math.abs(a - b) < Math.min(a, b) * EPSILON;
    }

    public static float floorFloatPrecision(float f) {
        return (float) (Math.floor(f/EPSILON)*EPSILON);
    }
}
