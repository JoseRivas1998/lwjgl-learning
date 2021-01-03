package com.tcg.lwjgllearning.samplesfromgraphics;

import com.tcg.lwjgllearning.application.ApplicationAdapter;
import com.tcg.lwjgllearning.application.LWJGLApplication;
import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.Texture;
import com.tcg.lwjgllearning.graphics.g3d.mesh.AttributeColorMesh;
import com.tcg.lwjgllearning.graphics.g3d.mesh.Shapes3D;
import com.tcg.lwjgllearning.graphics.g3d.mesh.TexturedMesh;
import com.tcg.lwjgllearning.graphics.g3d.mesh.UniformColorMesh;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Matrix4;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;
import com.tcg.lwjgllearning.utils.FileUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Lab05 extends ApplicationAdapter {

    private final Vector3 cameraPosition = new Vector3(0, 7, 15);
    private final Vector3 lookAtPosition = new Vector3(0, 2, 0);
    private final Vector3 cameraUpDirection = new Vector3(0, 1, 0);

    private final Vector3 ambientLight = new Vector3(0.2f, 0.2f, 0.2f);
    private final Vector3 lightDirection = new Vector3(1f, -0.5f, -1f);
    private final Vector3 lightIntensity = new Vector3(1f, 1f, 1f);

    private final Vector3[] testPositions = {
            new Vector3(-3, 4, 0),
            new Vector3(-1, 4, 0),
            new Vector3(1, 4, 0),
            new Vector3(3, 4, 0),
            new Vector3(0, 6, 0),
            new Vector3(0, 2, 0),
            new Vector3(-3, 3, 2),
            new Vector3(3, 3, 2)
    };

    private final Quaternion[] testRotations = {
            Quaternion.ofRotation(MathUtils.PI / 2, 0, 1, 0, true),
            Quaternion.ofRotation(0, 0, 1, 0, true),
            Quaternion.ofRotation(-MathUtils.PI / 2, 0, 1, 0, true),
            Quaternion.ofRotation(-MathUtils.PI, 0, 1, 0, true),
            Quaternion.ofRotation(MathUtils.PI / 2, 1, 0, 0, true),
            Quaternion.ofRotation(-MathUtils.PI / 2, 1, 0, 0, true),
            new Quaternion(),
            new Quaternion()
    };

    private Matrix4 viewMatrix;
    private Matrix4 projMatrix;

    private final Map<String, ShaderProgram> shaders = new HashMap<>();
    private AttributeColorMesh rgbCube;
    private UniformColorMesh yellowCube;
    private UniformColorMesh purpleSphere;
    private Texture radioactiveCrateTexture;
    private TexturedMesh radioactiveCrate;
    private Texture unwrappedCubeTexture;
    private TexturedMesh testCube;

    @Override
    public void create() {
        this.shaders.put("uvProgram", ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab05/vert.uv.glsl"),
                FileUtils.readFile("sample_shaders/lab05/frag.uv.glsl")
        ));
        this.shaders.put("rgbProgram", ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab04/vert.rgb.glsl"),
                FileUtils.readFile("sample_shaders/lab04/frag.rgb.glsl")
        ));
        this.shaders.put("uniformProgram", ShaderProgram.buildShader(
                FileUtils.readFile("sample_shaders/lab04/vert.uniform.glsl"),
                FileUtils.readFile("sample_shaders/lab04/frag.uniform.glsl")
        ));

        this.updateLightingAndMatrices(300, 300);

        this.rgbCube = new AttributeColorMesh(
                this.shaders.get("rgbProgram"),
                Shapes3D.Box.positionArray(),
                Shapes3D.Box.normalArray(),
                Shapes3D.Box.indexArray(),
                Shapes3D.Box.defaultColorArray()
        );

        final Color yellow = Color.rgb888(0xFFFF00);
        this.yellowCube = new UniformColorMesh(
                this.shaders.get("uniformProgram"),
                Shapes3D.Box.positionArray(),
                Shapes3D.Box.normalArray(),
                Shapes3D.Box.indexArray(),
                yellow
        );

        final int latBands = 30;
        final int longBands = 30;
        final Color purple = Color.rgb888(0xFF00FF);
        this.purpleSphere = new UniformColorMesh(
                this.shaders.get("uniformProgram"),
                Shapes3D.Sphere.positionArray(latBands, longBands),
                Shapes3D.Sphere.normalArray(latBands, longBands),
                Shapes3D.Sphere.indexArray(latBands, longBands),
                purple
        );
        final float sphereScale = 1f / MathUtils.cbrt(2);
        this.purpleSphere.setScale(new Vector3(sphereScale, sphereScale, sphereScale));

        this.rgbCube.translate(new Vector3(0, 0, 5));
        this.yellowCube.translate(new Vector3(-5, 0, 0));
        this.purpleSphere.translate(new Vector3(0, 0, -5));

        this.radioactiveCrateTexture = new Texture("textures/lab05/radioactive-crate.png");
        this.radioactiveCrate = new TexturedMesh(
                this.shaders.get("uvProgram"),
                Shapes3D.Box.positionArray(),
                Shapes3D.Box.normalArray(),
                Shapes3D.Box.indexArray(),
                Shapes3D.Box.uvRepeatArray(),
                this.radioactiveCrateTexture
        );
        this.radioactiveCrate.translate(new Vector3(5, 0, 0));

        this.unwrappedCubeTexture = new Texture("textures/lab05/unwrapped-cube.png");
        this.testCube = new TexturedMesh(
                this.shaders.get("uvProgram"),
                Shapes3D.Box.positionArray(),
                Shapes3D.Box.normalArray(),
                Shapes3D.Box.indexArray(),
                Shapes3D.Box.uvUnwrappedArray(),
                this.unwrappedCubeTexture
        );

    }

    @Override
    public void update() {
        final Vector3 origin = Vector3.origin();
        final float angle = MathUtils.PI / 200f;
        final Quaternion yRotSlow = Quaternion.ofRotation(angle, Vector3.y(), true);
        final Quaternion xRotFast = Quaternion.ofRotation(angle * 3, Vector3.x(), true);
        final Quaternion xRotSlow = Quaternion.ofRotation(angle, Vector3.x(), true);
        final Quaternion zRot = Quaternion.ofRotation(angle * 3, Vector3.z(), true);

        this.rgbCube.rotateAround(origin, yRotSlow, false);
        this.rgbCube.localRotate(xRotFast);

        this.yellowCube.rotateAround(origin, yRotSlow, false);
        this.yellowCube.localRotate(zRot);

        this.purpleSphere.rotateAround(origin, yRotSlow, false);
        this.purpleSphere.localRotate(xRotFast);

        this.radioactiveCrate.rotateAround(origin, yRotSlow, false);
        this.radioactiveCrate.localRotate(zRot);

        this.testRotations[6].composeInPlace(yRotSlow);
        this.testRotations[7].composeInPlace(xRotSlow);

    }

    @Override
    public void draw() {
        this.rgbCube.draw();
        this.yellowCube.draw();
        this.purpleSphere.draw();
        this.radioactiveCrate.draw();
        for (int i = 0; i < this.testPositions.length; i++) {
            this.testCube.setPosition(this.testPositions[i]);
            this.testCube.setRotation(this.testRotations[i]);
            this.testCube.draw();
        }
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
        this.yellowCube.dispose();
        this.rgbCube.dispose();
        this.purpleSphere.dispose();
        this.radioactiveCrate.dispose();
        this.testCube.dispose();
        this.shaders.values().forEach(ShaderProgram::dispose);
        this.radioactiveCrateTexture.dispose();
        this.unwrappedCubeTexture.dispose();
    }

    public static void main(String[] args) {
        new LWJGLApplication(new Lab05());
    }

}
