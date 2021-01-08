package com.tcg.lwjgllearning.graphics.g3d.lighting;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.glUniform3fv;

public class LightManager {

    private static final Vector3 DEFAULT_AMBIENT_LIGHT = new Vector3(0.2f, 0.3f, 0.2f);

    private final List<ShaderAmbientUniformLocation> programs = new ArrayList<>();
    private final float[] ambientLight = new float[3];

    private final List<PhongLight<?>> lights = new ArrayList<>();

    private int pointLightIndex;
    private int directionalLightIndex;

    public LightManager() {
        this(DEFAULT_AMBIENT_LIGHT);
    }

    public LightManager(Vector3 ambientLight) {
        System.arraycopy(ambientLight.asArray(), 0, this.ambientLight, 0, this.ambientLight.length);
    }

    public LightManager(ShaderProgram[] shaderPrograms) {
        this(Arrays.asList(shaderPrograms), DEFAULT_AMBIENT_LIGHT);
    }

    public LightManager(Collection<ShaderProgram> shaderPrograms) {
        this(shaderPrograms, DEFAULT_AMBIENT_LIGHT);
    }

    public LightManager(ShaderProgram[] shaderPrograms, Vector3 ambientLight) {
        this(Arrays.asList(shaderPrograms), ambientLight);
    }

    public LightManager(Collection<ShaderProgram> shaderPrograms, Vector3 ambientLight) {

        this(ambientLight);

        this.programs.addAll(shaderPrograms.stream()
                .map(this::buildShaderUniformLocation)
                .collect(Collectors.toUnmodifiableList()));

        this.pointLightIndex = 0;
        this.directionalLightIndex = 0;
    }

    public void setAmbientLight(Vector3 ambientLight) {
        System.arraycopy(ambientLight.asArray(), 0, this.ambientLight, 0, this.ambientLight.length);
        this.programs.forEach(this::updateAmbientLight);
    }

    public void addProgram(ShaderProgram program) {
        this.programs.add(this.buildShaderUniformLocation(program));
        this.lights.forEach(phongLight -> phongLight.addProgram(program));
    }

    public PointLight addPointLight(Vector3 position, Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        final PointLight pointLight = new PointLight(this.mapUniformLocationsToPrograms(), this.pointLightIndex++,
                position, diffuse, specular, ambient);
        this.lights.add(pointLight);
        return pointLight;
    }

    public PointLight addPointLight(Vector3 position, Vector3 diffuse, Vector3 specular) {
        return this.addPointLight(position, diffuse, specular, Vector3.origin());
    }

    public DirectionalLight addDirectionalLight(Vector3 direction, Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        final DirectionalLight directionalLight = new DirectionalLight(this.mapUniformLocationsToPrograms(),
                this.directionalLightIndex++, direction, diffuse, specular, ambient);
        this.lights.add(directionalLight);
        return directionalLight;
    }

    public DirectionalLight addDirectionalLight(Vector3 direction, Vector3 diffuse, Vector3 specular) {
        return this.addDirectionalLight(direction, diffuse, specular, Vector3.origin());
    }

    public void update() {
        this.lights.forEach(PhongLight::update);
    }

    private List<ShaderProgram> mapUniformLocationsToPrograms() {
        return this.programs.stream()
                .map(shaderAmbientUniformLocation -> shaderAmbientUniformLocation.program)
                .collect(Collectors.toUnmodifiableList());
    }

    private void updateAmbientLight(ShaderAmbientUniformLocation shaderAmbientUniformLocation) {
        shaderAmbientUniformLocation.program.bind();
        this.updateShaderAmbientLight(shaderAmbientUniformLocation);
        shaderAmbientUniformLocation.program.unbind();
    }

    private ShaderAmbientUniformLocation buildShaderUniformLocation(ShaderProgram shaderProgram) {
        shaderProgram.bind();
        final ShaderAmbientUniformLocation uniformLocation = new ShaderAmbientUniformLocation();
        uniformLocation.program = shaderProgram;
        uniformLocation.ambientUniformLocation = shaderProgram.getUniformLocation("ambientLight");
        this.updateShaderAmbientLight(uniformLocation);
        shaderProgram.unbind();
        return uniformLocation;
    }

    private void updateShaderAmbientLight(ShaderAmbientUniformLocation uniformLocation) {
        glUniform3fv(uniformLocation.ambientUniformLocation, this.ambientLight);
    }

    private static class ShaderAmbientUniformLocation {
        public ShaderProgram program;
        public int ambientUniformLocation;
    }

    public String processShaderString(String shaderString) {
        return shaderString.replaceAll(
            "#POINT_LIGHT_COUNT", "" + Math.max(this.pointLightIndex, 1)
        ).replaceAll(
            "#DIRECTIONAL_LIGHT_COUNT", "" + Math.max(this.directionalLightIndex, 1)
        );
    }
}
