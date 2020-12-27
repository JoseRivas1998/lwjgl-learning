package com.tcg.lwjgllearning.graphics.g3d.lighting;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.glUniform3fv;

public abstract class PhongLight<T extends PhongLight.ProgramUniformLocations> {

    private final List<T> programs = new ArrayList<>();

    private final float[] diffuse = new float[3];
    private final float[] specular = new float[3];
    private final float[] ambient = new float[3];

    protected final int index;
    protected boolean hasChanged;
    private boolean hasChangedDiffuse;
    private boolean hasChangedSpecular;
    private boolean hasChangedAmbient;

    public PhongLight(ShaderProgram[] programs, int index, Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        this(Arrays.asList(programs), index, diffuse, specular, ambient);
    }

    public PhongLight(Collection<ShaderProgram> programs, int index, Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        this.index = index;

        System.arraycopy(diffuse.asArray(), 0, this.diffuse, 0, this.diffuse.length);

        System.arraycopy(specular.asArray(), 0, this.specular, 0, this.specular.length);

        System.arraycopy(ambient.asArray(), 0, this.ambient, 0, this.ambient.length);

        this.programs.addAll(
                programs.stream()
                        .map(this::buildProgramUniforms)
                        .collect(Collectors.toUnmodifiableList())
        );

        this.hasChanged = true;
        this.hasChangedDiffuse = true;
        this.hasChangedSpecular = true;
        this.hasChangedAmbient = true;

    }

    public void setDiffuse(Vector3 diffuse) {
        System.arraycopy(diffuse.asArray(), 0, this.diffuse, 0, this.diffuse.length);
        this.hasChangedDiffuse = true;
        this.hasChanged = true;
    }

    public void setSpecular(Vector3 specular) {
        System.arraycopy(specular.asArray(), 0, this.specular, 0, this.specular.length);
        this.hasChangedSpecular = true;
        this.hasChanged = true;
    }

    public void setAmbient(Vector3 ambient) {
        System.arraycopy(ambient.asArray(), 0, this.ambient, 0, this.ambient.length);
        this.hasChangedAmbient = true;
        this.hasChanged = true;
    }

    public void update() {
        if (this.hasChanged) {
            this.updatePrograms();
        }
    }

    public void addProgram(ShaderProgram program) {
        final T programUniforms = this.buildProgramUniforms(program);
        this.programs.add(programUniforms);
        this.hasChanged = true;
        this.hasChangedDiffuse = true;
        this.hasChangedSpecular = true;
        this.hasChangedAmbient = true;
        this.setFlagsToValue(true);
        this.update();
    }

    protected abstract String uniformObjectName();

    protected abstract T generateInitialProgramUniforms(ShaderProgram shaderProgram, String uniformObjectName);

    protected abstract void setFlagsToValue(boolean value);

    protected abstract void updateUniforms(T programUniforms);

    private T buildProgramUniforms(ShaderProgram shaderProgram) {
        shaderProgram.bind();
        final String uniformObjectName = this.uniformObjectName();

        final T programUniforms = this.generateInitialProgramUniforms(shaderProgram, uniformObjectName);
        programUniforms.shaderProgram = shaderProgram;

        final String diffuse = String.format("%s.diffuse", uniformObjectName);
        programUniforms.diffuseUniformLocation = shaderProgram.getUniformLocation(diffuse);

        final String specular = String.format("%s.specular", uniformObjectName);
        programUniforms.specularUniformLocation = shaderProgram.getUniformLocation(specular);

        final String ambient = String.format("%s.ambient", uniformObjectName);
        programUniforms.ambientUniformLocation = shaderProgram.getUniformLocation(ambient);

        shaderProgram.unbind();
        return programUniforms;
    }

    private void updatePrograms() {
        this.programs.forEach(this::updateProgram);
        this.hasChanged = false;
        this.hasChangedDiffuse = false;
        this.hasChangedSpecular = false;
        this.hasChangedAmbient = false;
        this.setFlagsToValue(false);
    }

    private void updateProgram(T programUniforms) {
        programUniforms.shaderProgram.bind();
        if (this.hasChangedDiffuse) {
            glUniform3fv(programUniforms.diffuseUniformLocation, this.diffuse);
        }
        if (this.hasChangedSpecular) {
            glUniform3fv(programUniforms.specularUniformLocation, this.specular);
        }
        if (this.hasChangedAmbient) {
            glUniform3fv(programUniforms.ambientUniformLocation, this.ambient);
        }
        this.updateUniforms(programUniforms);
        programUniforms.shaderProgram.unbind();
    }

    protected static class ProgramUniformLocations {
        public ShaderProgram shaderProgram;
        public int diffuseUniformLocation;
        public int specularUniformLocation;
        public int ambientUniformLocation;
    }

}
