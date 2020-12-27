package com.tcg.lwjgllearning.graphics.g3d.lighting;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.glUniform3fv;

public class DirectionalLight {

    private final List<ProgramUniforms> programs = new ArrayList<>();

    private final float[] direction = new float[3];
    private final float[] diffuse = new float[3];
    private final float[] specular = new float[3];
    private final float[] ambient = new float[3];

    private final int index;
    private boolean hasChanged;
    private boolean hasChangedDirection;
    private boolean hasChangedDiffuse;
    private boolean hasChangedSpecular;
    private boolean hasChangedAmbient;

    public DirectionalLight(ShaderProgram[] programs, int index, Vector3 direction, Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        this(Arrays.asList(programs), index, direction, diffuse, specular, ambient);
    }

    public DirectionalLight(Collection<ShaderProgram> programs, int index, Vector3 direction, Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        this.index = index;

        System.arraycopy(direction.normalizeOutPlace().asArray(), 0,
                this.direction, 0, this.direction.length);

        System.arraycopy(diffuse.asArray(), 0, this.diffuse, 0, this.diffuse.length);

        System.arraycopy(specular.asArray(), 0, this.specular, 0, this.specular.length);

        System.arraycopy(ambient.asArray(), 0, this.ambient, 0, this.ambient.length);

        this.programs.addAll(
                programs.stream()
                        .map(this::buildProgramUniforms)
                        .collect(Collectors.toUnmodifiableList())
        );

        this.hasChanged = false;
        this.hasChangedDirection = false;
        this.hasChangedDiffuse = false;
        this.hasChangedSpecular = false;
        this.hasChangedAmbient = false;

    }

    private ProgramUniforms buildProgramUniforms(ShaderProgram shaderProgram) {
        final ProgramUniforms programUniforms = new ProgramUniforms();

        shaderProgram.bind();
        programUniforms.shaderProgram = shaderProgram;
        final String directionalLight = String.format("directionalLights[%d]", this.index);

        final String direction = String.format("%s.direction", directionalLight);
        programUniforms.directionUniformLocation = shaderProgram.getUniformLocation(direction);

        final String diffuse = String.format("%s.diffuse", directionalLight);
        programUniforms.diffuseUniformLocation = shaderProgram.getUniformLocation(diffuse);

        final String specular = String.format("%s.specular", directionalLight);
        programUniforms.specularUniformLocation = shaderProgram.getUniformLocation(specular);

        final String ambient = String.format("%s.ambient", directionalLight);
        programUniforms.ambientUniformLocation = shaderProgram.getUniformLocation(ambient);

        glUniform3fv(programUniforms.directionUniformLocation, this.direction);
        glUniform3fv(programUniforms.diffuseUniformLocation, this.diffuse);
        glUniform3fv(programUniforms.specularUniformLocation, this.specular);
        glUniform3fv(programUniforms.ambientUniformLocation, this.ambient);

        shaderProgram.unbind();
        return programUniforms;
    }

    private static class ProgramUniforms {
        public ShaderProgram shaderProgram;
        public int directionUniformLocation;
        public int diffuseUniformLocation;
        public int specularUniformLocation;
        public int ambientUniformLocation;
    }

    public void setDirection(Vector3 direction) {
        System.arraycopy(direction.normalizeOutPlace().asArray(), 0,
                this.direction, 0, this.direction.length);
        this.hasChangedDirection = true;
        this.hasChanged = true;
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

    private void updatePrograms() {
        this.programs.forEach(this::updateProgram);
        this.hasChanged = false;
        this.hasChangedDirection = false;
        this.hasChangedDiffuse = false;
        this.hasChangedSpecular = false;
        this.hasChangedAmbient = false;
    }

    private void updateProgram(ProgramUniforms programUniforms) {
        programUniforms.shaderProgram.bind();
        if (this.hasChangedDirection) {
            glUniform3fv(programUniforms.directionUniformLocation, this.direction);
        }
        if (this.hasChangedDiffuse) {
            glUniform3fv(programUniforms.diffuseUniformLocation, this.diffuse);
        }
        if (this.hasChangedSpecular) {
            glUniform3fv(programUniforms.specularUniformLocation, this.specular);
        }
        if (this.hasChangedAmbient) {
            glUniform3fv(programUniforms.ambientUniformLocation, this.ambient);
        }
        programUniforms.shaderProgram.unbind();
    }

}
