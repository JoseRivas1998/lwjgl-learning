package com.tcg.lwjgllearning.math;

import java.util.Arrays;
import java.util.Objects;

public class Matrix3 {

    private static final int MATRIX_LENGTH = 9;
    private final float[] mat = new float[9];

    private Matrix3(float[] values) {
        Objects.requireNonNull(values);
        if (values.length != 9) throw new IllegalArgumentException("Given array must be a 3x3 matrix.");
        System.arraycopy(values, 0, mat, 0, MATRIX_LENGTH);
    }

    public static Matrix3 identity() {
        return new Matrix3(new float[]{
                1, 0, 0,
                0, 1, 0,
                0, 0, 1
        });
    }

    public static Matrix3 mul(Matrix3 mat1, Matrix3 mat2) {
        final float M00 = mat1.mat[0] * mat2.mat[0] + mat1.mat[3] * mat2.mat[1] + mat1.mat[6] * mat2.mat[2];
        final float M10 = mat1.mat[1] * mat2.mat[0] + mat1.mat[4] * mat2.mat[1] + mat1.mat[7] * mat2.mat[2];
        final float M20 = mat1.mat[2] * mat2.mat[0] + mat1.mat[5] * mat2.mat[1] + mat1.mat[8] * mat2.mat[2];
        final float M01 = mat1.mat[0] * mat2.mat[3] + mat1.mat[3] * mat2.mat[4] + mat1.mat[6] * mat2.mat[5];
        final float M11 = mat1.mat[1] * mat2.mat[3] + mat1.mat[4] * mat2.mat[4] + mat1.mat[7] * mat2.mat[5];
        final float M21 = mat1.mat[2] * mat2.mat[3] + mat1.mat[5] * mat2.mat[4] + mat1.mat[8] * mat2.mat[5];
        final float M02 = mat1.mat[0] * mat2.mat[6] + mat1.mat[3] * mat2.mat[7] + mat1.mat[6] * mat2.mat[8];
        final float M12 = mat1.mat[1] * mat2.mat[6] + mat1.mat[4] * mat2.mat[7] + mat1.mat[7] * mat2.mat[8];
        final float M22 = mat1.mat[2] * mat2.mat[6] + mat1.mat[5] * mat2.mat[7] + mat1.mat[8] * mat2.mat[8];
        return new Matrix3(new float[]{
                M00, M10, M20,
                M01, M11, M21,
                M02, M12, M22
        });
    }

    public static Matrix3 prod(Matrix3... matrices) {
        if (matrices.length == 0) throw new IllegalArgumentException("At least one matrix is expected.");
        Matrix3 result = matrices[0];
        for (int i = 1; i < matrices.length; i++) {
            result = Matrix3.mul(result, matrices[i]);
        }
        return result;
    }

    public static Matrix3 translation(Vector2 v) {
        return new Matrix3(new float[]{
                1, 0, 0,
                0, 1, 0,
                v.x, v.y, 1
        });
    }

    public static Matrix3 rotation(float theta) {
        final float sinTheta = MathUtils.sin(theta);
        final float cosTheta = MathUtils.cos(theta);
        return new Matrix3(new float[]{
                cosTheta, sinTheta, 0,
                -sinTheta, cosTheta, 0,
                0, 0, 1
        });
    }

    public static Matrix3 scale(Vector2 v) {
        return new Matrix3(new float[]{
                v.x, 0, 0,
                0, v.y, 0,
                0, 0, 1
        });
    }

    public static Matrix3 worldMatrix(Vector2 position, float rotation, Vector2 scale) {
        final Matrix3 T = Matrix3.translation(position);
        final Matrix3 R = Matrix3.rotation(rotation);
        final Matrix3 S = Matrix3.scale(scale);
        return Matrix3.prod(T, R, S);
    }

    public static Matrix3 normal(Matrix4 rotation, Vector3 scale) {
        final float x = (Float.compare(scale.x, 0) == 0 ? 1f : 1f / scale.x);
        final float y = (Float.compare(scale.y, 0) == 0 ? 1f : 1f / scale.y);
        final float z = (Float.compare(scale.z, 0) == 0 ? 1f : 1f / scale.z);
        final float[] R = rotation.asArray();
        return new Matrix3(new float[]{
                R[0] * x, R[1] * x, R[2] * x,
                R[4] * y, R[5] * y, R[6] * y,
                R[8] * z, R[9] * z, R[10] * z
        });
    }

    public float[] asArray() {
        return Arrays.copyOf(this.mat, MATRIX_LENGTH);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        if (!result) {
            if (obj == null || this.getClass() != obj.getClass()) {
                result = false;
            } else {
                final Matrix3 other = (Matrix3) obj;
                result = Arrays.equals(this.mat, other.mat);
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mat);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.mat);
    }
}
