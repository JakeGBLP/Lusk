package it.jakegblp.lusk.utils;

import static it.jakegblp.lusk.utils.Constants.EPSILON;

public class NumberUtils {
    public static boolean areDoublesRoughlyEqual(double a, double b) {
        return Math.abs(a - b) < Math.min(a, b) * EPSILON;
    }

    public static double roundFloatPrecision(float f) {
        return Math.round(f/EPSILON)*EPSILON;
    }
}
