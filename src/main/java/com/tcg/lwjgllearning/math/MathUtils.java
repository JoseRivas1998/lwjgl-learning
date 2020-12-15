package com.tcg.lwjgllearning.math;

public final class MathUtils {

    // Right now this class is just wrapping all the JDK math functions, but is used to allow for later optimization.

    public static float sin(float radians) {
        return (float) Math.sin(radians);
    }

    public static float cos(float radians) {
        return (float) Math.cos(radians);
    }

    public static float acos(float x) {
        return (float) Math.acos(x);
    }

    public static float tan(float radians) {
        return (float) Math.tan(radians);
    }

    public static float sqrt(float x) {
        return (float) Math.sqrt(x);
    }

}
