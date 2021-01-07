package com.tcg.lwjgllearning.samplesfromgraphics;

import com.tcg.lwjgllearning.application.ApplicationAdapter;
import com.tcg.lwjgllearning.application.LWJGLApplication;
import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.Texture;
import com.tcg.lwjgllearning.graphics.g3d.lighting.DirectionalLight;
import com.tcg.lwjgllearning.graphics.g3d.lighting.LightManager;
import com.tcg.lwjgllearning.graphics.g3d.lighting.PointLight;
import com.tcg.lwjgllearning.graphics.g3d.materials.ColorPhongMaterial;
import com.tcg.lwjgllearning.graphics.g3d.materials.ScalarPhongMaterial;
import com.tcg.lwjgllearning.graphics.g3d.mesh.MaterialMesh;
import com.tcg.lwjgllearning.graphics.g3d.mesh.Shapes3D;
import com.tcg.lwjgllearning.graphics.g3d.mesh.TexturedMesh;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Matrix4;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;
import com.tcg.lwjgllearning.models.OBJModel;
import com.tcg.lwjgllearning.utils.FileUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUniform3fv;

public class Lab07_v2 extends ApplicationAdapter {

    private final Map<String, ShaderProgram> shaderPrograms = new HashMap<>();
    private Matrix4 viewMatrix;
    private Matrix4 projMatrix;
    private Matrix4 projViewMatrix;
    private LightManager lightManager;
    private PointLight pointLight1;
    private PointLight pointLight2;
    private DirectionalLight directionalLight;
    private MaterialMesh emeraldSuzy;
    private Texture earthTexture;
    private TexturedMesh uvEarth;
    private MaterialMesh lightSuzy1;
    private MaterialMesh lightSuzy2;
    private Instant startTime;
    private final Vector3 lightPosition = new Vector3();
    final Vector3 cameraPosition = new Vector3(0, 3, 7);
    final Vector3 lookAtPosition = Vector3.origin();
    final Vector3 cameraUpDirection = Vector3.y();


    @Override
    public void create() {

        final Vector3 ambientLight = new Vector3(0.4f, 0.5f, 0.4f);

        final Vector3 pointLightPosition = Vector3.origin();
        final Vector3 pointLightDiffuse = new Vector3(4f, 4f, 4f);
        final Vector3 pointLightSpecular = new Vector3(2f, 2f, 2f);

        this.lightManager = new LightManager(ambientLight);
        this.pointLight1 = this.lightManager.addPointLight(pointLightPosition, pointLightDiffuse, pointLightSpecular);
        this.pointLight2 = this.lightManager.addPointLight(pointLightPosition, pointLightDiffuse, pointLightSpecular);

        final Vector3 dirLightDirection = new Vector3(2f, -2f, -1f);
        final Vector3 dirLightDiffuse = new Vector3(2f, 0f, 0f);
        final Vector3 dirLightSpecular = new Vector3(0f, 0f, 4f);
        this.directionalLight = this.lightManager.addDirectionalLight(
                dirLightDirection, dirLightDiffuse, dirLightSpecular);

        this.shaderPrograms.put("uvProgram", ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab07_v2/vert.uv.glsl"),
                FileUtils.readFile("sample_shaders/lab07_v2/frag.uv.glsl"),
                this.lightManager
        ));
        this.shaderPrograms.put("rgbProgram", ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab07_v2/vert.rgb.glsl"),
                FileUtils.readFile("sample_shaders/lab07_v2/frag.rgb.glsl"),
                this.lightManager
        ));

        this.lightManager.update();

        final Color emeraldDiffuse = Color.rgb888(0x139C13);
        final Color emeraldSpecular = Color.rgb888(0xA1B9A1);
        final Color emeraldAmbient = Color.rgb888(0x052C05);
        final float emeraldShininess = 2f;

        final ColorPhongMaterial emeraldMaterial = new ColorPhongMaterial(
                this.shaderPrograms.get("rgbProgram"),
                emeraldDiffuse,
                emeraldSpecular,
                emeraldAmbient,
                emeraldShininess
        );
        final OBJModel suzyModelData = new OBJModel("models/lab07/suzy.obj");
        this.emeraldSuzy = OBJModel.meshOBJ(suzyModelData, emeraldMaterial);
        this.emeraldSuzy.translate(new Vector3(-2f, 0f, 0f));

        final float earthDiffuse = 0.7f;
        final float earthSpecular = 0.3f;
        final float earthAmbient = 0.0f;
        final float earthShininess = 10f;
        this.earthTexture = new Texture("textures/lab07/earth-texture.png");

        final ScalarPhongMaterial earthMaterial = new ScalarPhongMaterial(
                this.shaderPrograms.get("uvProgram"),
                earthDiffuse,
                earthSpecular,
                earthAmbient,
                earthShininess
        );

        final int latLongBands = 30;
        this.uvEarth = new TexturedMesh(
                earthMaterial,
                Shapes3D.Sphere.positionArray(latLongBands, latLongBands),
                Shapes3D.Sphere.normalArray(latLongBands, latLongBands),
                Shapes3D.Sphere.indexArray(latLongBands, latLongBands),
                Shapes3D.Sphere.uvArray(latLongBands, latLongBands),
                this.earthTexture
        );
        this.uvEarth.translate(new Vector3(2f, 0f, 0f));

        this.lightSuzy1 = OBJModel.meshOBJ(suzyModelData, emeraldMaterial);
        this.lightSuzy1.setScale(new Vector3(0.1f, 0.1f, 0.1f));

        this.lightSuzy2 = OBJModel.meshOBJ(suzyModelData, emeraldMaterial);
        this.lightSuzy2.setScale(new Vector3(0.1f, 0.1f, 0.1f));

        this.startTime = Instant.now();

    }

    @Override
    public void update() {
        final float k_theta = 1f / 1000f;
        final float k_alpha = 1f / 3101f;
        final float hr = 5f;
        final float vr = 2f;

        final long time = Duration.between(Instant.now(), this.startTime).toMillis();
        final float theta = time * k_theta;
        final float alpha = time * k_alpha;
        final float cosTheta = MathUtils.cos(theta);

        this.lightPosition.set(
                hr * cosTheta * MathUtils.sin(alpha),
                vr * MathUtils.sin(2 * theta),
                vr * cosTheta * MathUtils.cos(alpha)
        );
        this.pointLight1.setPosition(this.lightPosition);
        this.pointLight2.setPosition(this.lightPosition.inverse());
        this.lightManager.update();

        this.lightSuzy1.setPosition(this.lightPosition);
        this.lightSuzy2.setPosition(this.lightPosition.inverse());

        final Quaternion rotation = Quaternion.ofRotation(MathUtils.PI / 1000f, Vector3.y(), true);
        this.emeraldSuzy.rotate(rotation);
        this.uvEarth.rotate(rotation);
    }

    @Override
    public void draw() {
        this.lightSuzy1.draw();
        this.lightSuzy2.draw();
        this.emeraldSuzy.draw();
        this.uvEarth.draw();
    }

    @Override
    public void resize(int width, int height) {
        glViewport(0, 0, width, height);
        this.updateViewMatrix();
        this.updateProjectionMatrix(width, height);
        this.updateProjViewMatrix();
        this.bindCameraParamsToAllShaders();
    }

    @Override
    public void dispose() {
        this.emeraldSuzy.dispose();
        this.uvEarth.dispose();
        this.earthTexture.dispose();
        this.lightSuzy1.dispose();
        this.lightSuzy2.dispose();
        this.shaderPrograms.values().forEach(ShaderProgram::dispose);
    }

    private void bindCameraParamsToAllShaders() {
        this.shaderPrograms.values().forEach(this::bindCameraParamsToShader);
    }

    private void bindCameraParamsToShader(ShaderProgram shaderProgram) {
        shaderProgram.bind();
        final int mProjViewUniformLocation = shaderProgram.getUniformLocation("cam.mProjView");
        final int camPositionUniformLocation = shaderProgram.getUniformLocation("cam.position");

        glUniformMatrix4fv(mProjViewUniformLocation, false, this.projViewMatrix.asArray());
        glUniform3fv(camPositionUniformLocation, this.cameraPosition.asArray());

        shaderProgram.unbind();
    }

    private void updateProjViewMatrix() {
        this.projViewMatrix = Matrix4.mul(this.projMatrix, this.viewMatrix);
    }

    private void updateProjectionMatrix(int width, int height) {
        final float fieldOfView = MathUtils.PI / 4f;
        final float aspect = (float) width / height;
        final float near = 0.01f;
        final float far = 1000.0f;
        this.projMatrix = Matrix4.perspective(fieldOfView, aspect, near, far);
    }

    private void updateViewMatrix() {
        this.viewMatrix = Matrix4.view(this.cameraPosition, this.lookAtPosition, this.cameraUpDirection);
    }

    public static void main(String[] args) {
        new LWJGLApplication(new Lab07_v2());
    }

}
