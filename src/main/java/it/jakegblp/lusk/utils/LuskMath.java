package it.jakegblp.lusk.utils;

/**
 * The 'fit' methods are taken from {@link ch.njol.util.Math2}
 */
public class LuskMath {

    /**
     * Fits an 'integer' into the given interval. The method's behavior when min > max is unspecified.
     *
     * @return An int in between min and max
     */
    public static int fit(int min, int value, int max) {
        assert min <= max : min + "," + value + "," + max;
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Fits a 'long' into the given interval. The method's behavior when min > max is unspecified.
     *
     * @return A long in between min and max
     */
    public static long fit(long min, long value, long max) {
        assert min <= max : min + "," + value + "," + max;
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Fits a 'float' into the given interval. The method's behavior when min > max is unspecified.
     *
     * @return A float in between min and max
     */
    public static float fit(float min, float value, float max) {
        assert min <= max : min + "," + value + "," + max;
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Fits a 'double' into the given interval. The method's behavior when min > max is unspecified.
     *
     * @return A double in between min and max
     */
    public static double fit(double min, double value, double max) {
        assert min <= max : min + "," + value + "," + max;
        return Math.min(Math.max(value, min), max);
    }

}
