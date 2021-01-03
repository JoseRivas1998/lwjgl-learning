package com.tcg.lwjgllearning.graphics.g2d;

import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.math.Vector2;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class MultiColorMesh2D extends Mesh2D {

    private final float[] colorArray;
    private int colorVboId;

    public MultiColorMesh2D(ShaderProgram shaderProgram, float[] positionArray, int[] indexArray, Color[] colors, Vector2 position, float rotation, Vector2 scale) {
        super(shaderProgram, positionArray, indexArray, position, rotation, scale);
        this.colorArray = Color.colorArrayToFloatArray(colors);
        this.createColorBuffer();
    }


    public MultiColorMesh2D(ShaderProgram shaderProgram, float[] positionArray, int[] indexArray, Color[] colors) {
        this(shaderProgram, positionArray, indexArray, colors, new Vector2(0, 0), 0f, new Vector2(1, 1));
    }

    private void createColorBuffer() {
        FloatBuffer colorBuffer = null;

        try {
            glBindVertexArray(this.vaoId);

            colorBuffer = MemoryUtil.memAllocFloat(this.colorArray.length);
            colorBuffer.put(this.colorArray).flip();

            this.colorVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, this.colorVboId);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            int colorAttribLocation = this.shaderProgram.getAttribLocation("vertColor");
            glEnableVertexAttribArray(colorAttribLocation);
            glVertexAttribPointer(colorAttribLocation, 4, GL_FLOAT, false, 0, 0);

            glBindVertexArray(0);
        } finally {
            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
        }
    }

    @Override
    public void dispose() {
        glDeleteBuffers(this.colorVboId);
        super.dispose();
    }
}
