package com.tcg.lwjgllearning.math;

import java.util.Objects;

public class Quaternion {

    public float w;
    public float x;
    public float y;
    public float z;

    public Quaternion() {
        this.set(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public static Quaternion ofRotation(float theta, Vector3 rotationAxis, boolean normalized) {
        Objects.requireNonNull(rotationAxis);
        return Quaternion.ofRotation(theta, rotationAxis.x, rotationAxis.y, rotationAxis.z, normalized);
    }

    public static Quaternion ofRotation(float theta, float x, float y, float z, boolean normalized) {
        float xNorm = x;
        float yNorm = y;
        float zNorm = z;
        if (!normalized) {
            final float mag = MathUtils.sqrt(x * x + y * y + z * z);
            xNorm = x / mag;
            yNorm = y / mag;
            zNorm = z / mag;
        }
        final float halfTheta = theta * 0.5f;
        final float cosHalfTheta = MathUtils.cos(halfTheta);
        final float sinHalfTheta = MathUtils.sin(halfTheta);
        final Quaternion quaternion = new Quaternion();
        quaternion.set(cosHalfTheta, sinHalfTheta * xNorm, sinHalfTheta * yNorm, sinHalfTheta * zNorm);
        return quaternion;
    }

    public static Quaternion fromVector(Vector3 v) {
        final Quaternion quaternion = new Quaternion();
        quaternion.set(0f, v.x, v.y, v.z);
        return quaternion;
    }

    public static Quaternion composition(Quaternion... quaternions) {
        if (quaternions.length == 0) throw new IllegalArgumentException("Must pass in at least one quaternion.");
        final Quaternion result = quaternions[0].copy();
        for (int i = 1; i < quaternions.length; i++) {
            result.composeInPlace(quaternions[i], false);
        }
        return result;
    }

    public Quaternion copy() {
        final Quaternion quaternion = new Quaternion();
        quaternion.set(this.w, this.x, this.y, this.z);
        return quaternion;
    }

    public void set(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion inverse() {
        final Quaternion q = new Quaternion();
        q.set(this.w, -this.x, -this.y, -this.z);
        return q;
    }

    public void renormalize() {
        final float sinTheta = MathUtils.sqrt(1 - this.w * this.w);
        final float mag = MathUtils.sqrt(this.x * this.x + this.y * this.y + this.z * this.z) / sinTheta;
        this.x /= mag;
        this.y /= mag;
        this.z /= mag;
    }

    public Quaternion composeInPlace(Quaternion quaternion, boolean renormalize) {
        final float w = quaternion.w * this.w - quaternion.x * this.x - quaternion.y * this.y - quaternion.z * this.z;
        final float x = quaternion.w * this.x + quaternion.x * this.w + quaternion.y * this.z - quaternion.z * this.y;
        final float y = quaternion.w * this.y + quaternion.y * this.w - quaternion.x * this.z + quaternion.z * this.x;
        final float z = quaternion.w * this.z + quaternion.x * this.y - quaternion.y * this.x + quaternion.z * this.w;
        this.set(w, x, y, z);
        if (renormalize) this.renormalize();
        return this;
    }

    public Quaternion composeInPlace(Quaternion quaternion) {
        return this.composeInPlace(quaternion, true);
    }

    public Quaternion composeOutPlace(Quaternion quaternion, boolean renormalize) {
        return this.copy().composeInPlace(quaternion, renormalize);
    }

    public Quaternion composeOutPlace(Quaternion quaternion) {
        return this.composeOutPlace(quaternion, true);
    }

    public Quaternion applyRotation(Quaternion quaternion) {
        return Quaternion.composition(this.inverse(), quaternion, this);
    }

    public Quaternion localComposeInPlace(Quaternion q) {
        return this.composeInPlace(this.applyRotation(q));
    }

    public Quaternion localComposeOutPlace(Quaternion q) {
        return this.composeOutPlace(this.applyRotation(q));
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f, %f)", this.w, this.x, this.y, this.z);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        if (!result) {
            if (obj == null || obj.getClass() != this.getClass()) {
                result = false;
            } else {
                final Quaternion other = (Quaternion) obj;
                result = Float.compare(this.w, other.w) == 0
                        && Float.compare(this.x, other.x) == 0
                        && Float.compare(this.y, other.y) == 0
                        && Float.compare(this.z, other.z) == 0;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.w, this.x, this.y, this.z);
    }
}
