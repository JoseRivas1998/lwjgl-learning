package com.tcg.lwjgllearning.graphics.g3d;

import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

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

        public static Color[] defaultColorArray() {
            final Color green = Color.rgb888(0x00FF00);
            final Color red = Color.rgb888(0xFF0000);
            final Color blue = Color.rgb888(0x0000FF);
            return colors(green, green, red, red, blue, blue);
        }

        public static float[] uvRepeatArray() {
            return new float[]{
                    // top (+y)
                    0f, 0f,
                    0f, 1f,
                    1f, 1f,
                    1f, 0f,
                    // bottom (-y)
                    0f, 0f,
                    0f, 1f,
                    1f, 1f,
                    1f, 0f,
                    // // left (-x)
                    0f, 0f,
                    0f, 1f,
                    1f, 1f,
                    1f, 0f,
                    // right (+x)
                    0f, 0f,
                    0f, 1f,
                    1f, 1f,
                    1f, 0f,
                    // back (-z)
                    0f, 0f,
                    0f, 1f,
                    1f, 1f,
                    1f, 0f,
                    // front (+z)
                    0f, 0f,
                    0f, 1f,
                    1f, 1f,
                    1f, 0f
            };
        }

        public static float[] uvUnwrappedArray() {
            final float oneQuarter = 1f / 4f;
            final float oneThird = 1f / 3f;
            final float onHalf = 1f / 2f;
            final float twoThirds = 2f / 3f;
            final float threeQuarters = 3f / 4f;
            return new float[]{
                    // top (+y)
                    // -0.5, 0.5, -0.5,
                    oneQuarter, 0f,
                    // -0.5, 0.5, 0.5,
                    oneQuarter, oneThird,
                    // 0.5,  0.5, 0.5,
                    onHalf, oneThird,
                    // 0.5,  0.5, -0.5,
                    onHalf, 0f,

                    // bottom (-y)
                    // -0.5, -0.5, 0.5,
                    oneQuarter, twoThirds,
                    // -0.5, -0.5, -0.5,
                    oneQuarter, 1f,
                    // 0.5,  -0.5, -0.5,
                    onHalf, 1f,
                    // 0.5,  -0.5, 0.5,
                    onHalf, twoThirds,

                    // // left (-x)
                    0f, oneThird,
                    0f, twoThirds,
                    oneQuarter, twoThirds,
                    oneQuarter, oneThird,
                    // right (+x)
                    onHalf, oneThird,
                    onHalf, twoThirds,
                    threeQuarters, twoThirds,
                    threeQuarters, oneThird,
                    // back (-z)
                    threeQuarters, oneThird,
                    threeQuarters, twoThirds,
                    1f, twoThirds,
                    1f, oneThird,
                    // front (+z)
                    oneQuarter, oneThird,
                    oneQuarter, twoThirds,
                    onHalf, twoThirds,
                    onHalf, oneThird
            };
        }

    }

    public static class Sphere {
        public static float[] positionArray(int latBands, int longBands) {
            final List<Float> pos = new ArrayList<>();
            for (int lat = 0; lat <= latBands; lat++)
            {
                final float theta = (float) lat * MathUtils.PI / latBands;
                final float sinTheta = MathUtils.sin(theta);
                final float cosTheta = MathUtils.cos(theta);

                for (int lon = 0; lon <= longBands; lon++)
                {
                    final float phi = (float) lon * 2f * MathUtils.PI / longBands;
                    final float sinPhi = MathUtils.sin(phi);
                    final float cosPhi = MathUtils.cos(phi);

                    final float x = sinTheta * sinPhi;
                    final float y = cosTheta;
                    final float z = sinTheta * cosPhi;

                    pos.add(x);
                    pos.add(y);
                    pos.add(z);
                }
            }
            return ListUtils.floatListToArray(pos);
        }

        public static float[] normalArray(int latBands, int longBands) {
            return Sphere.positionArray(latBands, longBands);
        }

        public static int[] indexArray(int latBands, int longBands) {
            final List<Integer> ind = new ArrayList<>();
            for (var lat = 0; lat < latBands; lat++)
            {
                for (int lon = 0; lon < longBands; lon++)
                {
                    var topLeftIndex = lat * (longBands + 1) + lon;
                    var topRightIndex = topLeftIndex + 1;
                    var bottomLeftIndex = topLeftIndex + longBands + 1;
                    var bottomRightIndex = bottomLeftIndex + 1;

                    // top left triangle
                    ind.add(topLeftIndex);
                    ind.add(bottomLeftIndex);
                    ind.add(topRightIndex);

                    // bottom right triangle
                    ind.add(bottomLeftIndex);
                    ind.add(bottomRightIndex);
                    ind.add(topRightIndex);
                }
            }
            return ListUtils.intListToArray(ind);
        }

    }

}
