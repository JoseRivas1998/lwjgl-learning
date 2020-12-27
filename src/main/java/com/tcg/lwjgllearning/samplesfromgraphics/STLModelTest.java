package com.tcg.lwjgllearning.samplesfromgraphics;

import com.tcg.lwjgllearning.application.ApplicationAdapter;
import com.tcg.lwjgllearning.application.LWJGLApplication;
import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.g3d.mesh.UniformColorMesh;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Matrix4;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;
import com.tcg.lwjgllearning.models.STLModel;
import com.tcg.lwjgllearning.utils.FileUtils;

import java.util.stream.Stream;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class STLModelTest extends ApplicationAdapter {

    private int width;
    private int height;
    private Matrix4 viewMatrix;
    private Matrix4 projectionMatrix;
    private ShaderProgram uniformShader;
    private ShaderProgram rgbShader;
    private final Vector3 ambientLight = new Vector3();
    private final Vector3 lightDirection = new Vector3();
    private final Vector3 lightIntensity = new Vector3();
    private UniformColorMesh binarySpaceInvader;
    private UniformColorMesh asciiSpaceInvader;
    private UniformColorMesh cashFloatGamePiece;

    @Override
    public void create() {
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


        final Color red = Color.rgb888(0xFF0000);
        final Color green = Color.rgb888(0x00FF00);
        final Color blue = Color.rgb888(0x0000FF);
        this.binarySpaceInvader = STLModel.buildStlMesh("models/space_invader_magnet_binary.stl",
                this.uniformShader, red);
        this.binarySpaceInvader.setScale(new Vector3(0.05f, 0.05f, 0.05f));
        this.binarySpaceInvader.translate(new Vector3(-5, 0, 0));

        this.asciiSpaceInvader = STLModel.buildStlMesh("models/space_invader_magnet_ascii.stl",
                this.uniformShader, green);
        this.asciiSpaceInvader.setScale(new Vector3(0.05f, 0.05f, 0.05f));
        this.asciiSpaceInvader.translate(new Vector3(5, 0, 0));

        this.cashFloatGamePiece = STLModel.buildStlMesh("models/CashFlowGamePiece.stl",
                this.uniformShader, blue);
        this.cashFloatGamePiece.setScale(new Vector3(0.1f, 0.1f, 0.1f));
        this.cashFloatGamePiece.localRotate(Quaternion.ofRotation(-MathUtils.PI / 2, Vector3.x(), true));
        this.cashFloatGamePiece.translate(new Vector3(-1, -1, -1));


        glClearColor(0.75f, 0.85f, 0.8f, 1.0f);
    }

    @Override
    public void update() {
        final Quaternion rotation = Quaternion.ofRotation(MathUtils.PI / 180f, Vector3.y(), true);
        this.binarySpaceInvader.localRotate(rotation);
        this.asciiSpaceInvader.localRotate(rotation);
        this.cashFloatGamePiece.rotateAround(Vector3.origin(), rotation, false);
    }

    @Override
    public void draw() {
        this.binarySpaceInvader.draw();
        this.asciiSpaceInvader.draw();
        this.cashFloatGamePiece.draw();
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        this.updateMatricesAndLighting(width, height);
    }

    @Override
    public void dispose() {
        this.cashFloatGamePiece.dispose();
        this.binarySpaceInvader.dispose();
        this.asciiSpaceInvader.dispose();
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
        new LWJGLApplication(new STLModelTest());
    }

}
