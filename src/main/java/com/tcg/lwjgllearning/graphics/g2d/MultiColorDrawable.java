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

public class MultiColorDrawable extends Drawable{

    private final float[] colorArray;

    public MultiColorDrawable(ShaderProgram shaderProgram, float[] positionArray, int[] indexArray, Color[] colors, Vector2 position, float rotation, Vector2 scale) {
        super(shaderProgram, positionArray, indexArray, position, rotation, scale);
        this.colorArray = colorArrayToFloatArray(colors);

        createColorBuffer();
    }


    public MultiColorDrawable(ShaderProgram shaderProgram, float[] positionArray, int[] indexArray, Color[] colors) {
        this(shaderProgram, positionArray, indexArray, colors, new Vector2(0, 0), 0f, new Vector2(1, 1));
    }

    private float[] colorArrayToFloatArray(Color[] colors) {
        final float[] colorArrayAsFloatArray = new float[colors.length * 4];
        for (int i = 0; i < colors.length; i++) {
            Color color = colors[i];
            final int colorStart = i * 4;
            colorArrayAsFloatArray[colorStart] = color.r;
            colorArrayAsFloatArray[colorStart + 1] = color.g;
            colorArrayAsFloatArray[colorStart + 2] = color.b;
            colorArrayAsFloatArray[colorStart + 3] = color.a;
        }
        return colorArrayAsFloatArray;
    }

    private void createColorBuffer() {
        FloatBuffer colorBuffer = null;

        try {
            glBindVertexArray(this.vaoId);

            colorBuffer = MemoryUtil.memAllocFloat(this.colorArray.length);
            colorBuffer.put(this.colorArray).flip();

            final int colorVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
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



}
