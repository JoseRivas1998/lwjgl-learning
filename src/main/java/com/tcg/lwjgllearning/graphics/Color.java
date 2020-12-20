package com.tcg.lwjgllearning.graphics;

import com.tcg.lwjgllearning.math.MathUtils;

import java.util.Objects;

public class Color {

    public float r;
    public float g;
    public float b;
    public float a;

    public Color() {
        this(0f, 0f, 0f, 0f);
    }

    public Color(float r, float g, float b, float a) {
        this.set(r, g, b, a);
    }

    public static Color rgb888(int rgb888) {
        final float r = ((rgb888 >> 16) & 0xFF) / 255f;
        final float g = ((rgb888 >> 8) & 0xFF) / 255f;
        final float b = (rgb888 & 0xFF) / 255f;
        return new Color(r, g, b, 1);
    }

    public static Color rgba8888(int rgba8888) {
        final float r = ((rgba8888 >> 24) & 0xFF) / 255f;
        final float g = ((rgba8888 >> 16) & 0xFF) / 255f;
        final float b = ((rgba8888 >> 8) & 0xFF) / 255f;
        final float a = (rgba8888 & 0xFF) / 255f;
        return new Color(r, g, b, a);
    }

    public static Color lerp(Color c1, Color c2, float t) {
        final float r = MathUtils.lerp(c1.r, c2.r, t);
        final float g = MathUtils.lerp(c1.g, c2.g, t);
        final float b = MathUtils.lerp(c1.b, c2.b, t);
        final float a = MathUtils.lerp(c1.a, c2.a, t);
        return new Color(r, b, b, a);
    }

    public Color copy() {
        return new Color(this.r, this.g, this.b, this.a);
    }

    public void set(Color color) {
        this.set(color.r, color.g, color.b, color.a);
    }

    public void set(float r, float g, float b, float a) {
        this.r = MathUtils.clamp(r, 0f, 1f);
        this.g = MathUtils.clamp(g, 0f, 1f);
        this.b = MathUtils.clamp(b, 0f, 1f);
        this.a = MathUtils.clamp(a, 0f, 1f);
    }

    public int toRGBA8888() {
        final int r = (int) (this.r * 255);
        final int g = (int) (this.g * 255);
        final int b = (int) (this.b * 255);
        final int a = (int) (this.a * 255);
        return (r << 24) | (g << 16) | (b << 8) | a;
    }

    public float[] asArray() {
        return new float[]{this.r, this.g, this.b, this.a};
    }

    private void clamp() {
        this.r = MathUtils.clamp(this.r, 0f, 1f);
        this.g = MathUtils.clamp(this.g, 0f, 1f);
        this.b = MathUtils.clamp(this.b, 0f, 1f);
        this.a = MathUtils.clamp(this.a, 0f, 1f);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        if (!result) {
            if (obj == null || obj.getClass() != this.getClass()) {
                result = false;
            } else {
                final Color other = (Color) obj;
                result = Float.compare(this.r, other.r) == 0
                        && Float.compare(this.g, other.g) == 0
                        && Float.compare(this.b, other.b) == 0
                        && Float.compare(this.a, other.a) == 0;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.r, this.g, this.b, this.a);
    }

    @Override
    public String toString() {
        return String.format("#%08X", this.toRGBA8888());
    }

}
