package com.tcg.lwjgllearning.models;

public class Model {
    protected static final int FLOATS_PER_TRIANGLE = 9;
    protected static final int VERTICES_PER_TRIANGLE = 3;
    private static final int FLOATS_PER_VERTEX = 3;
    protected float[] vertices;
    protected float[] normals;
    protected float[] textureCoordinates;
    protected int[] indices;
}
