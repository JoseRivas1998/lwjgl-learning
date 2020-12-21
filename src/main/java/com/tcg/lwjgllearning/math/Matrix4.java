package com.tcg.lwjgllearning.math;

import java.util.Arrays;
import java.util.Objects;

public class Matrix4 {

    private static final int MATRIX_LENGTH = 4 * 4;
    private final float[] mat = new float[MATRIX_LENGTH];

    private Matrix4(float[] values) {
        Objects.requireNonNull(values);
        if (values.length != MATRIX_LENGTH) throw new IllegalArgumentException("Value array must be a 4x4 matrix.");
        System.arraycopy(values, 0, this.mat, 0, MATRIX_LENGTH);
    }

    public static Matrix4 identity() {
        return new Matrix4(new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });
    }

    public static Matrix4 mul(Matrix4 mat1, Matrix4 mat2) {
        final float[] product = new float[MATRIX_LENGTH];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float sum = 0;
                for (int offset = 0; offset < 4; offset++) {
                    final int index1 = rowColToColumnMajorFormIndex(i, offset);
                    final int index2 = rowColToColumnMajorFormIndex(offset, j);
                    sum += mat1.mat[index1] * mat2.mat[index2];
                }
                final int index = rowColToColumnMajorFormIndex(i, j);
                product[index] = sum;
            }
        }
        return new Matrix4(product);
    }

    private static int rowColToColumnMajorFormIndex(int row, int col) {
        return col * 4 + row;
    }

    public static Matrix4 prod(Matrix4... mats) {
        if (mats.length == 0) throw new IllegalArgumentException("This method expects at least one matrix.");
        Matrix4 result = Matrix4.identity();
        for (Matrix4 mat : mats) {
            result = Matrix4.mul(result, mat);
        }
        return result;
    }

    public static Matrix4 translation(Vector3 translation) {
        return new Matrix4(new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                translation.x, translation.y, translation.z, 1
        });
    }

    public static Matrix4 rotation(Quaternion rotation) {
        final Vector3 localX = Vector3.x();
        final Vector3 localY = Vector3.y();
        final Vector3 localZ = Vector3.z();
        localX.rotateInPlace(rotation);
        localY.rotateInPlace(rotation);
        localZ.rotateInPlace(rotation);
        return new Matrix4(new float[]{
                localX.x, localX.y, localX.z, 0,
                localY.x, localY.y, localY.z, 0,
                localZ.x, localZ.y, localZ.z, 0,
                0, 0, 0, 1
        });
    }

    public static Matrix4 scale(Vector3 scale) {
        return new Matrix4(new float[]{
                scale.x, 0, 0, 0,
                0, scale.y, 0, 0,
                0, 0, scale.z, 0,
                0, 0, 0, 1
        });
    }

    public static Matrix4 worldMatrix(Matrix4 translation, Matrix4 rotation, Matrix4 scale) {
        return Matrix4.prod(translation, rotation, scale);
    }

    public static Matrix4 view(Vector3 eye, Vector3 target, Vector3 up) {
        final Vector3 back = eye.subOutPlace(target).normalizeOutPlace();
        final Vector3 right = up.cross(back).normalizeOutPlace();
        final Vector3 up1 = back.cross(right);

        final float rightDotEye = right.dot(eye);
        final float upDotEye = up1.dot(eye);
        final float backDotEye = back.dot(eye);

        return new Matrix4(new float[]{
                right.x, up1.x, back.x, 0f,
                right.y, up1.y, back.y, 0f,
                right.z, up1.z, back.z, 0f,
                -rightDotEye, -upDotEye, -backDotEye, 1f
        });
    }

    public static Matrix4 perspective(float viewRadians, float aspect, float near, float far) {
        final float oneOverTanThetaOverTwo = 1f / MathUtils.tan(viewRadians / 2f);
        final float nPlusF = near + far;
        final float nMinusF = near - far;
        final float twoNF = 2f * near * far;
        return new Matrix4(new float[]{
                oneOverTanThetaOverTwo / aspect, 0f, 0f, 0f,
                0f, oneOverTanThetaOverTwo, 0f, 0f,
                0f, 0f, nPlusF / nMinusF, -1f,
                0f, 0f, twoNF / nMinusF, 0f
        });
    }

    public static Matrix4 orthographic(float near, float far, float bottom, float top, float right, float left) {
        final float rightMinusLeft = right - left;
        final float topMinusBottom = top - bottom;
        final float farMinusNear = far - near;
        final float topPlusBottom = top + bottom;
        final float farPlusNear = far + near;
        return new Matrix4(new float[]{
                2f / rightMinusLeft, 0f, 0f, 0f,
                0, 2f / topMinusBottom, 0f, 0f,
                0f, 0f, -2f / farMinusNear, 0f,
                -(right + left) / rightMinusLeft, -topPlusBottom / topMinusBottom, -farPlusNear / farMinusNear, 1f
        });
    }

    public float[] asArray() {
        return Arrays.copyOf(this.mat, MATRIX_LENGTH);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        if (!result) {
            if (obj == null || obj.getClass() != this.getClass()) {
                result = false;
            } else {
                final Matrix4 other = (Matrix4) obj;
                result = Arrays.equals(this.mat, other.mat);
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.mat);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.mat);
    }
}
