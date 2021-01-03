package com.tcg.lwjgllearning.samplesfromgraphics;

import com.tcg.lwjgllearning.application.ApplicationAdapter;
import com.tcg.lwjgllearning.application.LWJGLApplication;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.Texture;
import com.tcg.lwjgllearning.graphics.g3d.mesh.TexturedMesh;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Matrix4;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;
import com.tcg.lwjgllearning.models.OBJModel;
import com.tcg.lwjgllearning.utils.FileUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Lab06 extends ApplicationAdapter {

    private final Vector3 cameraPosition = new Vector3(0, 3, 7);
    private final Vector3 lookAtPosition = Vector3.origin();
    private final Vector3 cameraUpDirection = Vector3.y();

    private final Vector3 ambientLight = new Vector3(0.2f, 0.3f, 0.2f);
    private final Vector3 lightDirection = new Vector3(1, 1, -1);
    private final Vector3 lightIntensity = new Vector3(0.9f, 0.8f, 0.6f);

    private Matrix4 viewMatrix;
    private Matrix4 projMatrix;

    private final Map<String, ShaderProgram> shaders = new HashMap<>();
    private Texture suzyTexture;
    private TexturedMesh suzyMesh;
    private Texture cubeTexture;
    private TexturedMesh cubeMesh;
    private Texture r2Texture;
    private TexturedMesh r2Mesh;
    private Texture stopSignTexture;
    private TexturedMesh stopSignMesh;

    @Override
    public void create() {
        this.shaders.put("uvProgram", ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab05/vert.uv.glsl"),
                FileUtils.readFile("sample_shaders/lab05/frag.uv.glsl")
        ));

        this.updateLightingAndMatrices(300, 300);

        this.suzyTexture = new Texture("textures/lab06/suzy2-texture.png", true);
        this.suzyMesh = OBJModel.textureOBJ("models/lab06/suzy2.obj",
                this.shaders.get("uvProgram"), this.suzyTexture);

        this.cubeTexture = new Texture("textures/lab06/cube-texture.png", true);
        this.cubeMesh = OBJModel.textureOBJ("models/lab06/cube.obj", this.shaders.get("uvProgram"), this.cubeTexture);

        this.r2Texture = new Texture("textures/lab06/r2d2-texture.png", true);
        this.r2Mesh = OBJModel.textureOBJ("models/lab06/r2unit.obj", this.shaders.get("uvProgram"), this.r2Texture);

        this.stopSignTexture = new Texture("textures/lab06/stopSign-texture.png", true);
        this.stopSignMesh = OBJModel.textureOBJ("models/lab06/stopSign.obj",
                this.shaders.get("uvProgram"), this.stopSignTexture);

        this.suzyMesh.translate(new Vector3(-2, 0, 0));
        this.cubeMesh.translate(new Vector3(2, 0, 0));

        this.r2Mesh.setScale(new Vector3(0.25f, 0.25f, 0.25f));
        this.r2Mesh.translate(new Vector3(0, 1.5f, 0));

        this.stopSignMesh.translate(new Vector3(0f, -1.5f, 0f));

        glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
    }

    @Override
    public void update() {
        final float angle = MathUtils.PI / 100;
        final Quaternion rotation = Quaternion.ofRotation(angle, Vector3.y(), true);
        this.suzyMesh.rotate(rotation);
        this.cubeMesh.rotate(rotation);
        this.r2Mesh.rotate(rotation);
        this.stopSignMesh.rotate(rotation);
    }

    @Override
    public void draw() {
        this.suzyMesh.draw();
        this.cubeMesh.draw();
        this.r2Mesh.draw();
        this.stopSignMesh.draw();
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        this.updateLightingAndMatrices(width, height);
    }

    private void updateLightingAndMatrices(int width, int height) {
        this.viewMatrix = Matrix4.view(this.cameraPosition, this.lookAtPosition, this.cameraUpDirection);
        final float aspect = (float) width / height;
        float fieldOfView = MathUtils.PI / 4;
        float near = 0.01f;
        float far = 1000.0f;
        this.projMatrix = Matrix4.perspective(fieldOfView, aspect, near, far);
        this.shaders.values().forEach(this::bindLightingAndMatricesToShader);
    }

    private void bindLightingAndMatricesToShader(ShaderProgram shader) {
        shader.bind();

        final int matViewUniformLocation = shader.getUniformLocation("mView");
        glUniformMatrix4fv(matViewUniformLocation, false, this.viewMatrix.asArray());

        final int matProjUniformLocation = shader.getUniformLocation("mProj");
        glUniformMatrix4fv(matProjUniformLocation, false, this.projMatrix.asArray());

        final int ambientLightUniformLocation = shader.getUniformLocation("ambientLight");
        glUniform3fv(ambientLightUniformLocation, this.ambientLight.asArray());

        final int lightDirectionUniformLocation = shader.getUniformLocation("lightDirection");
        glUniform3fv(lightDirectionUniformLocation, this.lightDirection.asArray());

        final int lightIntensityUniformLocation = shader.getUniformLocation("lightIntensity");
        glUniform3fv(lightIntensityUniformLocation, this.lightIntensity.asArray());

        shader.unbind();
    }

    @Override
    public void dispose() {
        this.suzyMesh.dispose();
        this.suzyTexture.dispose();
        this.cubeMesh.dispose();
        this.cubeTexture.dispose();
        this.r2Mesh.dispose();
        this.r2Texture.dispose();
        this.stopSignTexture.dispose();
        this.stopSignMesh.dispose();
        this.shaders.values().forEach(ShaderProgram::dispose);
    }

    public static void main(String[] args) {
        new LWJGLApplication(new Lab06());
    }

}
