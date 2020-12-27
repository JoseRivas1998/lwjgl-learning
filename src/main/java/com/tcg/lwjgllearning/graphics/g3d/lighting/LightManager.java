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

    private static final int DEFAULT_MAX_PER_LIGHT = 16;
    private static final Vector3 DEFAULT_AMBIENT_LIGHT = new Vector3(0.2f, 0.3f, 0.2f);

    private final List<ShaderAmbientUniformLocation> programs = new ArrayList<>();
    private final float[] ambientLight = new float[3];

    private final int maxPerLight;
    private final List<PhongLight> lights = new ArrayList<>();

    private int pointLightIndex;
    private int directionalLightIndex;

    public LightManager(ShaderProgram[] shaderPrograms) {
        this(DEFAULT_MAX_PER_LIGHT, Arrays.asList(shaderPrograms), DEFAULT_AMBIENT_LIGHT);
    }

    public LightManager(Collection<ShaderProgram> shaderPrograms) {
        this(DEFAULT_MAX_PER_LIGHT, shaderPrograms, DEFAULT_AMBIENT_LIGHT);
    }

    public LightManager(ShaderProgram[] shaderPrograms, Vector3 ambientLight) {
        this(DEFAULT_MAX_PER_LIGHT, Arrays.asList(shaderPrograms), ambientLight);
    }

    public LightManager(Collection<ShaderProgram> shaderPrograms, Vector3 ambientLight) {
        this(DEFAULT_MAX_PER_LIGHT, shaderPrograms, ambientLight);
    }

    public LightManager(int maxPerLight, ShaderProgram[] shaderPrograms) {
        this(maxPerLight, Arrays.asList(shaderPrograms), DEFAULT_AMBIENT_LIGHT);
    }

    public LightManager(int maxPerLight, Collection<ShaderProgram> shaderPrograms) {
        this(maxPerLight, shaderPrograms, DEFAULT_AMBIENT_LIGHT);
    }

    public LightManager(int maxPerLight, ShaderProgram[] shaderPrograms, Vector3 ambientLight) {
        this(maxPerLight, Arrays.asList(shaderPrograms), ambientLight);
    }

    public LightManager(int maxPerLight, Collection<ShaderProgram> shaderPrograms, Vector3 ambientLight) {
        this.maxPerLight = maxPerLight;

        System.arraycopy(ambientLight.asArray(), 0, this.ambientLight, 0, this.ambientLight.length);

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
        this.enforcePointLightCapacity();
        final PointLight pointLight = new PointLight(this.mapUniformLocationsToPrograms(), this.pointLightIndex++,
                position, diffuse, specular, ambient);
        this.lights.add(pointLight);
        return pointLight;
    }

    public PointLight addPointLight(Vector3 position, Vector3 diffuse, Vector3 specular) {
        return this.addPointLight(position, diffuse, specular, Vector3.origin());
    }

    public DirectionalLight addDirectionalLight(Vector3 direction, Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        this.enforceDirectionalLightCapacity();
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

    private void enforceDirectionalLightCapacity() {
        if (this.directionalLightIndex >= this.maxPerLight) {
            throw new IllegalStateException("Cannot add anymore directional lights.");
        }
    }

    private List<ShaderProgram> mapUniformLocationsToPrograms() {
        return this.programs.stream()
                .map(shaderAmbientUniformLocation -> shaderAmbientUniformLocation.program)
                .collect(Collectors.toUnmodifiableList());
    }

    private void enforcePointLightCapacity() {
        if (this.pointLightIndex >= this.maxPerLight){
            throw new IllegalStateException("Cannot add any more point lights.");
        }
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


}
