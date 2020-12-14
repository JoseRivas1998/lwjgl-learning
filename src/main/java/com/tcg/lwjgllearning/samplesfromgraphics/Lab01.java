package com.tcg.lwjgllearning.samplesfromgraphics;

import com.tcg.lwjgllearning.application.ApplicationAdapter;
import com.tcg.lwjgllearning.application.LWJGLApplication;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.utils.FileUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Lab01 extends ApplicationAdapter {

    private ShaderProgram shaderProgram;
    private int vaoId;

    @Override
    public void create() {
        final String vertexShader = FileUtils.readFile("sample_shaders/lab01/vert.glsl");
        final String fragShader = FileUtils.readFile("sample_shaders/lab01/frag.glsl");
        this.shaderProgram = ShaderProgram.buildShader(vertexShader, fragShader);

        final float[] trianglePositions = {
                // top
                0.0f, 0.5f, 0.0f,

                // bottom left
                -0.5f, -0.5f, 0.0f,

                // bottom right
                0.5f, -0.5f, 0.0f
        };

        final float[] triangleColors = {
                // Top (Blue)
                0.0f, 0.0f, 1.0f,

                // bottom left (Green)
                0.0f, 1.0f, 0.0f,

                // Bottom right (Red)
                1.0f, 0.0f, 0.0f
        };

        final int[] triangleIndex = {0, 1, 2};

        FloatBuffer trianglePositionsBuffer = null;
        FloatBuffer triangleColorsBuffer = null;
        IntBuffer triangleIndexBuffer = null;

        try {

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            trianglePositionsBuffer = MemoryUtil.memAllocFloat(trianglePositions.length);
            trianglePositionsBuffer.put(trianglePositions).flip();

            int positionsVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, positionsVboId);
            glBufferData(GL_ARRAY_BUFFER, trianglePositionsBuffer, GL_STATIC_DRAW);
            int positionAttribLocation = shaderProgram.getAttribLocation("vertPosition");
            glEnableVertexAttribArray(positionAttribLocation);
            glVertexAttribPointer(positionAttribLocation, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            triangleColorsBuffer = MemoryUtil.memAllocFloat(triangleColors.length);
            triangleColorsBuffer.put(triangleColors).flip();

            int colorsVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, colorsVboId);
            glBufferData(GL_ARRAY_BUFFER, triangleColorsBuffer, GL_STATIC_DRAW);
            int colorAttribLocation = shaderProgram.getAttribLocation("vertColor");
            glEnableVertexAttribArray(colorAttribLocation);
            glVertexAttribPointer(colorAttribLocation, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);


            triangleIndexBuffer = MemoryUtil.memAllocInt(triangleIndex.length);
            triangleIndexBuffer.put(triangleIndex).flip();

            int indexVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, triangleIndexBuffer, GL_STATIC_DRAW);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

            glBindVertexArray(0);

        } finally {
            if (trianglePositionsBuffer != null) {
                MemoryUtil.memFree(trianglePositionsBuffer);
            }
            if (triangleColorsBuffer != null) {
                MemoryUtil.memFree(triangleColorsBuffer);
            }
            if (triangleIndexBuffer != null) {
                MemoryUtil.memFree(triangleIndexBuffer);
            }
        }


    }

    @Override
    public void draw() {

        this.shaderProgram.bind();

        glBindVertexArray(this.vaoId);

        glDrawArrays(GL_TRIANGLES, 0, 3);

        glBindVertexArray(0);

        this.shaderProgram.unbind();

    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void close() {
        this.shaderProgram.cleanUp();
    }

    public static void main(String[] args) {
        new LWJGLApplication(new Lab01());
    }

}
