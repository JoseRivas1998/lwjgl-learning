package com.tcg.lwjgllearning.math;

import java.util.Objects;

public class Vector3 implements Vector<Vector3> {

    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        set(x, y, z);
    }

    public Vector3() {
        this(0f, 0f, 0f);
    }

    public static Vector3 x() {
        return new Vector3(1, 0, 0);
    }

    public static Vector3 y() {
        return new Vector3(0, 1, 0);
    }

    public static Vector3 z() {
        return new Vector3(0, 0, 1);
    }

    public static Vector3 origin() {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Vector3 copy() {
        return new Vector3(this.x, this.y, this.z);
    }

    @Override
    public void set(Vector3 v) {
        this.set(v.x, v.y, v.z);
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vector3 addInPlace(Vector3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.y;
        return this;
    }

    @Override
    public Vector3 addOutPlace(Vector3 v) {
        return new Vector3(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    @Override
    public Vector3 subInPlace(Vector3 v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
        return this;
    }

    @Override
    public Vector3 subOutPlace(Vector3 v) {
        return new Vector3(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    @Override
    public Vector3 scaleInPlace(Vector3 v) {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    @Override
    public Vector3 scaleOutPlace(Vector3 v) {
        return new Vector3(this.x * v.x, this.y * v.y, this.z * v.z);
    }

    @Override
    public Vector3 scalarScaleInPlace(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }

    @Override
    public Vector3 scalarScaleOutPlace(float scalar) {
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    @Override
    public Vector3 scaleAndAddInPlace(float scalar, Vector3 v) {
        this.x += scalar * v.x;
        this.y += scalar * v.y;
        this.z += scalar * v.z;
        return this;
    }

    @Override
    public Vector3 scaleAndAddOutPlace(float scalar, Vector3 v) {
        return new Vector3(this.x + (scalar * v.x), this.y + (scalar * v.y), this.z + (scalar * v.z));
    }

    @Override
    public Vector3 inverse() {
        return new Vector3(-this.x, -this.y, -this.z);
    }

    @Override
    public float squareMagnitude() {
        return (this.x * this.x) + (this.y * this.y) + (this.z * this.z);
    }

    @Override
    public float magnitude() {
        return MathUtils.sqrt(this.squareMagnitude());
    }

    @Override
    public Vector3 normalizeInPlace() {
        final float magnitude = this.magnitude();
        this.x /= magnitude;
        this.y /= magnitude;
        this.z /= magnitude;
        return this;
    }

    @Override
    public Vector3 normalizeOutPlace() {
        final float magnitude = this.magnitude();
        return new Vector3(this.x / magnitude, this.y / magnitude, this.z / magnitude);
    }

    @Override
    public Vector3 rotateInPlace(Quaternion q) {
        final Quaternion rotated = q.applyRotation(Quaternion.fromVector(this));
        this.set(rotated.x, rotated.y, rotated.z);
        return this;
    }

    @Override
    public Vector3 rotateOutPlace(Quaternion q) {
        return this.copy().rotateInPlace(q);
    }

    @Override
    public Vector3 cross(Vector3 v) {
        final float x = this.y * v.z - this.z * v.y;
        final float y = this.z * v.x - this.x * v.z;
        final float z = this.x * v.y - this.y * v.x;
        return new Vector3(x, y, z);
    }

    @Override
    public float dot(Vector3 v) {
        return (this.x * v.x) + (this.y * v.y) + (this.z * v.z);
    }

    @Override
    public float[] asArray() {
        return new float[]{this.x, this.y, this.z};
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        if (!result) {
            if (obj == null || obj.getClass() != this.getClass()) {
                result = false;
            } else {
                Vector3 other = (Vector3) obj;
                result = Float.compare(this.x, other.x) == 0
                        && Float.compare(this.y, other.y) == 0
                        && Float.compare(this.z, other.z) == 0;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", this.x, this.y, this.z);
    }
}
