package com.tcg.lwjgllearning.samplesfromgraphics;

import com.tcg.lwjgllearning.application.ApplicationAdapter;
import com.tcg.lwjgllearning.application.ApplicationListener;
import com.tcg.lwjgllearning.application.LWJGLApplication;
import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.g2d.UniformColorDrawable;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Vector2;
import com.tcg.lwjgllearning.utils.FileUtils;

import static org.lwjgl.opengl.GL11.glViewport;

public class Matrix2DLab extends ApplicationAdapter {

    private ShaderProgram uniformColorShader;
    private UniformColorDrawable greenBox;

    @Override
    public void create() {
        uniformColorShader = ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/matrix2DLab/uniformColor.vert.glsl"),
                FileUtils.readFile("sample_shaders/matrix2DLab/uniformColor.frag.glsl")
        );

        greenBox = new UniformColorDrawable(uniformColorShader,
                boxPositionArray(),
                boxIndexArray(),
                Color.rgb888(0x00FF00),
                new Vector2(0.8f, 0.8f),
                MathUtils.PI / 4f,
                new Vector2(0.1f, 0.1f));
    }

    private int[] boxIndexArray() {
        return new int[]{
                0, 1, 2,
                2, 3, 0
        };
    }

    private float[] boxPositionArray() {
        return new float[]{
                -1f, -1f, // bottom left
                1f, -1f, // bottom right
                1f, 1f,  // top right
                -1f, 1f,  // top left
        };
    }

    @Override
    public void update() {
        this.greenBox.rotate(MathUtils.PI / 60f);
    }

    @Override
    public void draw() {

        this.greenBox.draw();

    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void close() {
        this.uniformColorShader.cleanUp();
    }

    public static void main(String[] args) {
        new LWJGLApplication(new Matrix2DLab());
    }
}
