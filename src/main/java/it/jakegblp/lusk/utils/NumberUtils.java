package it.jakegblp.lusk.utils;

import static it.jakegblp.lusk.utils.Constants.EPSILON;

public class NumberUtils {
    public static double roundFloatPrecision(float f) {
        return Math.round((double) f / EPSILON) * EPSILON;
    }
}
