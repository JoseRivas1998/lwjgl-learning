package com.tcg.lwjgllearning.samplesfromgraphics;

import com.tcg.lwjgllearning.application.ApplicationAdapter;
import com.tcg.lwjgllearning.application.LWJGLApplication;
import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.g3d.mesh.RGBMesh;
import com.tcg.lwjgllearning.graphics.g3d.mesh.Shapes3D;
import com.tcg.lwjgllearning.graphics.g3d.mesh.UniformColorMesh;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Matrix4;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;
import com.tcg.lwjgllearning.utils.FileUtils;

import java.util.stream.Stream;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Lab04 extends ApplicationAdapter {

    private int width;
    private int height;
    private Matrix4 viewMatrix;
    private Matrix4 projectionMatrix;
    private ShaderProgram uniformShader;
    private ShaderProgram rgbShader;
    private final Vector3 ambientLight = new Vector3();
    private final Vector3 lightDirection = new Vector3();
    private final Vector3 lightIntensity = new Vector3();
    private UniformColorMesh meshCube;
    private float angle;
    private Vector3 origin;
    private Quaternion orbit;
    private Quaternion localRot;
    private RGBMesh rgbCube;

    @Override
    public void create() {
        super.create();
        this.ambientLight.set(0.2f, 0.3f, 0.2f);
        this.lightDirection.set(1f, -1f, -0.25f);
        this.lightIntensity.set(0.9f, 0.8f, 0.6f);

        this.uniformShader = ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab04/vert.uniform.glsl"),
                FileUtils.readFile("sample_shaders/lab04/frag.uniform.glsl")
        );

        this.rgbShader = ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab04/vert.rgb.glsl"),
                FileUtils.readFile("sample_shaders/lab04/frag.rgb.glsl")
        );

        this.updateMatricesAndLighting(300, 300);
        final float[] cubePositionArray = Shapes3D.Box.positionArray();
        final float[] cubeNormalArray = Shapes3D.Box.normalArray();
        final int[] cubeIndexArray = Shapes3D.Box.indexArray();
        this.meshCube = new UniformColorMesh(
                this.uniformShader,
                cubePositionArray, cubeNormalArray, cubeIndexArray,
                Color.rgb888(0x8F43FF)
        );
        this.meshCube.translate(new Vector3(5, 0, 0));

        final Color green = Color.rgb888(0x00FF00);
        final Color red = Color.rgb888(0xFF0000);
        final Color blue = Color.rgb888(0x0000FF);
        final Color[] cubeColorArray = Shapes3D.Box.colors(green, green, red, red, blue, blue);
        this.rgbCube = new RGBMesh(
                this.rgbShader,
                cubePositionArray,
                cubeNormalArray,
                cubeIndexArray,
                cubeColorArray
        );

        this.angle = MathUtils.PI / 100f;
        this.origin = Vector3.origin();
        this.orbit = Quaternion.ofRotation(this.angle / 2f, 0f, 1f, 0f, false);
        this.localRot = Quaternion.ofRotation(4f * this.angle, 0f, 0f, 1f, false);
        glClearColor(0.75f, 0.85f, 0.8f, 1.0f);
    }

    @Override
    public void update() {
        this.meshCube.rotateAround(this.origin, this.orbit, false);
        this.meshCube.localRotate(this.localRot);

        this.rgbCube.localRotate(this.localRot);
        this.rgbCube.rotateAround(this.origin, this.orbit, false);

    }

    @Override
    public void draw() {
        this.meshCube.draw();
        this.rgbCube.draw();
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        this.updateMatricesAndLighting(width, height);
    }

    @Override
    public void dispose() {
        this.rgbCube.dispose();
        this.meshCube.dispose();
        this.uniformShader.dispose();
        this.rgbShader.dispose();
    }

    private void updateMatricesAndLighting(int width, int height) {
        this.updateViewMatrix();
        this.updateProjectionMatrix(width, height);
        this.bindMatricesAndLightingToAllShaders(this.uniformShader, this.rgbShader);
    }

    private void bindMatricesAndLightingToAllShaders(ShaderProgram... shaders) {
        Stream.of(shaders)
                .forEach(this::bindMatricesAndLightingToShader);
    }

    private void bindMatricesAndLightingToShader(ShaderProgram shader) {
        shader.bind();

        final int matViewUniformLocation = shader.getUniformLocation("mView");
        glUniformMatrix4fv(matViewUniformLocation, false, this.viewMatrix.asArray());

        final int matProjUniformLocation = shader.getUniformLocation("mProj");
        glUniformMatrix4fv(matProjUniformLocation, false, this.projectionMatrix.asArray());

        final int ambientLightUniformLocation = shader.getUniformLocation("ambientLight");
        glUniform3fv(ambientLightUniformLocation, this.ambientLight.asArray());

        final int lightDirectionUniformLocation = shader.getUniformLocation("lightDirection");
        glUniform3fv(lightDirectionUniformLocation, this.lightDirection.asArray());

        final int lightIntensityUniformLocation = shader.getUniformLocation("lightIntensity");
        glUniform3fv(lightIntensityUniformLocation, this.lightIntensity.asArray());

        shader.unbind();
    }

    private void updateViewMatrix() {
        final Vector3 cameraPosition = new Vector3(0, 5, 15);
        final Vector3 lookAtPosition = Vector3.origin();
        final Vector3 cameraUpDirection = Vector3.y();
        this.viewMatrix = Matrix4.view(cameraPosition, lookAtPosition, cameraUpDirection);
    }

    private void updateProjectionMatrix(int width, int height) {
        this.width = width;
        this.height = height;
        final float fieldOfView = MathUtils.PI / 4;
        final float aspect = (float) this.width / this.height;
        final float near = 0.01f;
        final float far = 1000f;
        this.projectionMatrix = Matrix4.perspective(fieldOfView, aspect, near, far);
    }

    public static void main(String[] args) {
        new LWJGLApplication(new Lab04());
    }

}
