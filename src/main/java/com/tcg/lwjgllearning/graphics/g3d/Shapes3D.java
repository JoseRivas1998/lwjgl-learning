package com.tcg.lwjgllearning.graphics.g3d;

import com.tcg.lwjgllearning.graphics.Color;

public class Shapes3D {

    public static class Box {
        public static float[] positionArray() {
            return new float[]{
                    // top (+y)
                    -0.5f, 0.5f, -0.5f,
                    -0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f,
                    0.5f, 0.5f, -0.5f,

                    // bottom (-y)
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, -0.5f, 0.5f,

                    // left (-x)
                    -0.5f, 0.5f, -0.5f,
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, -0.5f, 0.5f,
                    -0.5f, 0.5f, 0.5f,

                    // right (+x)
                    0.5f, 0.5f, 0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, -0.5f, -0.5f,
                    0.5f, 0.5f, -0.5f,

                    // back (-z)
                    0.5f, 0.5f, -0.5f,
                    0.5f, -0.5f, -0.5f,
                    -0.5f, -0.5f, -0.5f,
                    -0.5f, 0.5f, -0.5f,

                    // front (+z)
                    -0.5f, 0.5f, 0.5f,
                    -0.5f, -0.5f, 0.5f,
                    0.5f, -0.5f, 0.5f,
                    0.5f, 0.5f, 0.5f
            };
        }

        public static int[] indexArray() {
            return new int[]{
                    // top
                    0, 1, 2,
                    0, 2, 3,
                    // bottom
                    4, 5, 6,
                    4, 6, 7,
                    // right
                    8, 9, 10,
                    8, 10, 11,
                    // left
                    12, 13, 14,
                    12, 14, 15,
                    // back
                    16, 17, 18,
                    16, 18, 19,
                    // front
                    20, 21, 22,
                    20, 22, 23
            };
        }

        public static float[] normalArray() {
            return new float[]{
                    // top
                    0f, 1f, 0f,
                    0f, 1f, 0f,
                    0f, 1f, 0f,
                    0f, 1f, 0f,

                    // bottom
                    0f, -1f, 0f,
                    0f, -1f, 0f,
                    0f, -1f, 0f,
                    0f, -1f, 0f,

                    // left
                    -1f, 0f, 0f,
                    -1f, 0f, 0f,
                    -1f, 0f, 0f,
                    -1f, 0f, 0f,

                    // right
                    1f, 0f, 0f,
                    1f, 0f, 0f,
                    1f, 0f, 0f,
                    1f, 0f, 0f,

                    // back
                    0f, 0f, -1f,
                    0f, 0f, -1f,
                    0f, 0f, -1f,
                    0f, 0f, -1f,

                    // front
                    0f, 0f, 1f,
                    0f, 0f, 1f,
                    0f, 0f, 1f,
                    0f, 0f, 1f
            };
        }

        public static Color[] colors(Color top, Color bottom, Color left, Color right, Color back, Color front) {
            return new Color[]{
                    top.copy(),
                    top.copy(),
                    top.copy(),
                    top.copy(),

                    bottom.copy(),
                    bottom.copy(),
                    bottom.copy(),
                    bottom.copy(),

                    left.copy(),
                    left.copy(),
                    left.copy(),
                    left.copy(),

                    right.copy(),
                    right.copy(),
                    right.copy(),
                    right.copy(),

                    back.copy(),
                    back.copy(),
                    back.copy(),
                    back.copy(),

                    front.copy(),
                    front.copy(),
                    front.copy(),
                    front.copy()
            };
        }
    }

}
