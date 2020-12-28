package com.tcg.lwjgllearning.samplesfromgraphics;

import com.tcg.lwjgllearning.application.ApplicationAdapter;
import com.tcg.lwjgllearning.application.ApplicationContext;
import com.tcg.lwjgllearning.application.LWJGLApplication;
import com.tcg.lwjgllearning.application.Window;
import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.KeyboardFPSCamera;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.Texture;
import com.tcg.lwjgllearning.graphics.g3d.lighting.LightManager;
import com.tcg.lwjgllearning.graphics.g3d.lighting.PointLight;
import com.tcg.lwjgllearning.graphics.g3d.materials.RGBMaterial;
import com.tcg.lwjgllearning.graphics.g3d.materials.UVMaterial;
import com.tcg.lwjgllearning.graphics.g3d.mesh.Mesh;
import com.tcg.lwjgllearning.graphics.g3d.mesh.Shapes3D;
import com.tcg.lwjgllearning.graphics.g3d.mesh.UVMesh;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;
import com.tcg.lwjgllearning.models.OBJModel;
import com.tcg.lwjgllearning.utils.FileUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glViewport;

public class Lab08 extends ApplicationAdapter {

    private final Map<String, ShaderProgram> shaderPrograms = new HashMap<>();
    private KeyboardFPSCamera camera;
    private LightManager lightManager;
    private PointLight pointLight1;
    private PointLight pointLight2;
    private Mesh emeraldSuzy;
    private Texture earthTexture;
    private UVMesh uvEarth;
    private Mesh lightSuzy1;
    private Mesh lightSuzy2;
    private Instant startTime;
    private final Vector3 lightPosition = new Vector3();

    @Override
    public void create() {

        this.shaderPrograms.put("uvProgram", ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab08/vert.uv.glsl"),
                FileUtils.readFile("sample_shaders/lab08/frag.uv.glsl")
        ));
        this.shaderPrograms.put("rgbProgram", ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab08/vert.rgb.glsl"),
                FileUtils.readFile("sample_shaders/lab08/frag.rgb.glsl")
        ));

        this.camera = new KeyboardFPSCamera(this.shaderPrograms.values());
        final Window window = ApplicationContext.context().window;
        this.camera.setPerspective(MathUtils.PI / 4f,
                (float) window.getWidth() / window.getHeight(), 0.01f, 1000.0f);
        this.camera.translate(new Vector3(5f, 3f, 7f));
        this.camera.lookAt(Vector3.origin(), Vector3.y());

        final Vector3 ambientLight = new Vector3(0.4f, 0.5f, 0.4f);

        final Vector3 pointLightPosition = Vector3.origin();
        final Vector3 pointLightDiffuse = new Vector3(1f, 1f, 4f);
        final Vector3 pointLightSpecular = new Vector3(1f, 4f, 1f);

        this.lightManager = new LightManager(this.shaderPrograms.values(), ambientLight);
        this.pointLight1 = this.lightManager.addPointLight(pointLightPosition, pointLightDiffuse, pointLightSpecular);
        this.pointLight2 = this.lightManager.addPointLight(pointLightPosition, pointLightDiffuse, pointLightSpecular);
        this.lightManager.update();

        final Color emeraldDiffuse = Color.rgb888(0x139C13);
        final Color emeraldSpecular = Color.rgb888(0xA1B9A1);
        final Color emeraldAmbient = Color.rgb888(0x052C05);
        final float emeraldShininess = 0.2f;

        final RGBMaterial emeraldMaterial = new RGBMaterial(
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
        final float earthShininess = 0.1f;
        this.earthTexture = new Texture("textures/lab07/earth-texture.png");

        final UVMaterial earthMaterial = new UVMaterial(
                this.shaderPrograms.get("uvProgram"),
                earthDiffuse,
                earthSpecular,
                earthAmbient,
                earthShininess
        );

        final int latLongBands = 30;
        this.uvEarth = new UVMesh(
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
        this.camera.update();
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
        this.camera.setAspectRatio((float) width / height);
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

    public static void main(String[] args) {
        new LWJGLApplication(new Lab08());
    }

}
