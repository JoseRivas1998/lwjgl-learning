package com.tcg.lwjgllearning.samplesfromgraphics;

import com.tcg.lwjgllearning.application.ApplicationAdapter;
import com.tcg.lwjgllearning.application.LWJGLApplication;
import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.g2d.MultiColorMesh2D;
import com.tcg.lwjgllearning.graphics.g2d.UniformColorMesh2D;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Vector2;
import com.tcg.lwjgllearning.utils.FileUtils;

import static org.lwjgl.opengl.GL11.glViewport;

public class Matrix2DLab extends ApplicationAdapter {

    private ShaderProgram uniformColorShader;
    private ShaderProgram multiColorShader;
    private UniformColorMesh2D greenBox;
    private MultiColorMesh2D redBlueBox;

    @Override
    public void create() {
        this.uniformColorShader = ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/matrix2DLab/uniformColor.vert.glsl"),
                FileUtils.readFile("sample_shaders/matrix2DLab/uniformColor.frag.glsl")
        );

        this.multiColorShader = ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/matrix2DLab/multiColor.vert.glsl"),
                FileUtils.readFile("sample_shaders/matrix2DLab/multiColor.frag.glsl")
        );

        this.greenBox = new UniformColorMesh2D(this.uniformColorShader,
                this.boxPositionArray(),
                this.boxIndexArray(),
                Color.rgb888(0x00FF00),
                new Vector2(0.8f, 0.8f),
                MathUtils.PI / 4f,
                new Vector2(0.1f, 0.1f));

        final Color red = Color.rgb888(0xFF0000);
        final Color blue = Color.rgb888(0x0000FF);
        this.redBlueBox = new MultiColorMesh2D(this.multiColorShader,
                this.boxPositionArray(),
                this.boxIndexArray(),
                new Color[]{
                        red, blue,
                        red, blue
                },
                new Vector2(-0.5f, -0.5f),
                MathUtils.PI / 4f,
                new Vector2(0.2f, 0.1f));
    }

    private int[] boxIndexArray() {
        return new int[]{
                0, 1, 2,
                0, 2, 3
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
        this.redBlueBox.rotateAround(Vector2.origin(), MathUtils.PI / 100f, false);
    }

    @Override
    public void draw() {

        this.greenBox.draw();
        this.redBlueBox.draw();

    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void dispose() {
        this.greenBox.dispose();
        this.redBlueBox.dispose();
        this.uniformColorShader.dispose();
        this.multiColorShader.dispose();
    }

    public static void main(String[] args) {
        new LWJGLApplication(new Matrix2DLab());
    }
}
