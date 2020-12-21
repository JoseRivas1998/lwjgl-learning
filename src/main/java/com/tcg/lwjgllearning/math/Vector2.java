package com.tcg.lwjgllearning.math;

import java.util.Objects;

public class Vector2 implements Vector<Vector2> {

    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this(0f, 0f);
    }

    public static Vector2 origin() {
        return new Vector2(0, 0);
    }

    public static Vector2 x() {
        return new Vector2(1, 0);
    }

    public static Vector2 y() {
        return new Vector2(0, 1);
    }

    public static Vector2 direction(float theta) {
        return new Vector2(MathUtils.cos(theta), MathUtils.cos(theta));
    }

    @Override
    public Vector2 copy() {
        return new Vector2(this.x, this.y);
    }

    @Override
    public void set(Vector2 v) {
        this.set(v.x, v.y);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2 addInPlace(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    @Override
    public Vector2 addOutPlace(Vector2 v) {
        return new Vector2(this.x + v.x, this.y + v.y);
    }

    @Override
    public Vector2 subInPlace(Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    @Override
    public Vector2 subOutPlace(Vector2 v) {
        return new Vector2(this.x - v.x, this.y - v.y);
    }

    @Override
    public Vector2 scaleInPlace(Vector2 v) {
        this.x *= v.x;
        this.y *= v.y;
        return this;
    }

    @Override
    public Vector2 scaleOutPlace(Vector2 v) {
        return new Vector2(this.x * v.x, this.y * v.y);
    }

    @Override
    public Vector2 scalarScaleInPlace(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    @Override
    public Vector2 scalarScaleOutPlace(float scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    @Override
    public Vector2 scaleAndAddInPlace(float scalar, Vector2 v) {
        this.x += scalar * v.x;
        this.y *= scalar * v.y;
        return this;
    }

    @Override
    public Vector2 scaleAndAddOutPlace(float scalar, Vector2 v) {
        return new Vector2(this.x + (scalar * v.x), this.y + (scalar * v.y));
    }

    @Override
    public Vector2 inverse() {
        return new Vector2(-this.x, -this.y);
    }

    @Override
    public float squareMagnitude() {
        return (this.x * this.x) + (this.y * this.y);
    }

    @Override
    public float magnitude() {
        return MathUtils.sqrt(this.squareMagnitude());
    }

    @Override
    public Vector2 normalizeInPlace() {
        final float magnitude = this.magnitude();
        this.x /= magnitude;
        this.y /= magnitude;
        return this;
    }

    @Override
    public Vector2 normalizeOutPlace() {
        final float magnitude = this.magnitude();
        return new Vector2(this.x / magnitude, this.y / magnitude);
    }

    @Override
    public Vector2 rotateInPlace(float delta) {
        final float x = this.x * MathUtils.cos(delta) - this.y * MathUtils.sin(delta);
        final float y = this.x * MathUtils.sin(delta) + this.y * MathUtils.cos(delta);
        this.set(x, y);
        return this;
    }

    @Override
    public Vector2 rotateOutPlace(float delta) {
        final Vector2 copy = this.copy();
        return copy.rotateInPlace(delta);
    }

    @Override
    public float dot(Vector2 v) {
        return this.x * v.x + this.y * v.y;
    }

    @Override
    public float[] asArray() {
        return new float[]{this.x, this.y};
    }

    @Override
    public boolean equals(Object o) {
        boolean result = this == o;
        if (!result) {
            if (o == null || o.getClass() != this.getClass()) {
                result = false;
            } else {
                Vector2 other = (Vector2) o;
                result = Float.compare(this.x, other.x) == 0 && Float.compare(this.y, other.y) == 0;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", this.x, this.y);
    }

}
