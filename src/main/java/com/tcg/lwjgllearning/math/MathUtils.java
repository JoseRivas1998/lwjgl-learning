package com.tcg.lwjgllearning.math;

public final class MathUtils {

    // Right now this class is just wrapping all the JDK math functions, but is used to allow for later optimization.

    public static final float PI = (float) Math.PI;
    public static final float PI2 = MathUtils.PI * 2;
    public static final float HALF_PI = MathUtils.PI / 2;

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

    public static float clamp(float x, float min, float max) {
        return Math.max(min, Math.min(x, max));
    }

    public static float lerp(float a, float b, float t) {
        return a + (t * (b - a));
    }

}
