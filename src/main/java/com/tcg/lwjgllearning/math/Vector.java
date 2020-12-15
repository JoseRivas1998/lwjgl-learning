package com.tcg.lwjgllearning.math;

public interface Vector<T extends Vector<T>> {

    T copy();

    void set(T v);

    T addInPlace(T v);

    T addOutPlace(T v);

    T subInPlace(T v);

    T subOutPlace(T v);

    T scaleInPlace(T v);

    T scaleOutPlace(T v);

    T scalarScaleInPlace(float scalar);

    T scalarScaleOutPlace(float scalar);

    T inverse();

    float squareMagnitude();

    float magnitude();

    T normalizeInPlace();

    T normalizeOutPlace();

    default T rotateInPlace(Quaternion q) {
        throw new UnsupportedOperationException("This vector does not support quaternion rotation.");
    }

    default T rotateOutPlace(Quaternion q) {
        throw new UnsupportedOperationException("This vector does not support quaternion rotation.");
    }

    default T rotateInPlace(float delta) {
        throw new UnsupportedOperationException("This vector does not support angle rotation.");
    }

    default T rotateOutPlace(float delta) {
        throw new UnsupportedOperationException("This vector does not support angle rotation.");
    }

    default T sum(T[] vectors) {
        if (vectors.length == 0) throw new IllegalArgumentException("Sum function must take at least one vector.");
        T totalSum = vectors[0].copy();
        for (int i = 1; i < vectors.length; i++) {
            totalSum.addInPlace(vectors[i]);
        }
        return totalSum;
    }

    T cross(T v);

    float dot(T v);

}
