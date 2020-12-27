package com.tcg.lwjgllearning.graphics.g3d.lighting;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.math.Vector3;

import java.util.Arrays;
import java.util.Collection;

import static org.lwjgl.opengl.GL20.glUniform3fv;

public class DirectionalLight extends PhongLight<DirectionalLight.DirectionalProgramUniformLocations> {

    private final float[] direction = new float[3];

    private boolean hasChangedDirection;

    public DirectionalLight(ShaderProgram[] programs, int index, Vector3 direction,
                            Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        this(Arrays.asList(programs), index, direction, diffuse, specular, ambient);
    }

    public DirectionalLight(Collection<ShaderProgram> programs, int index, Vector3 direction,
                            Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        super(programs, index, diffuse, specular, ambient);

        System.arraycopy(direction.normalizeOutPlace().asArray(), 0,
                this.direction, 0, this.direction.length);

        this.hasChangedDirection = true;
    }

    public void updateDirection(Vector3 direction) {
        System.arraycopy(direction.normalizeOutPlace().asArray(), 0,
                this.direction, 0, this.direction.length);
        this.hasChangedDirection = true;
        this.hasChanged = true;
    }

    @Override
    protected String uniformObjectName() {
        return String.format("directionalLights[%d]", this.index);
    }

    @Override
    protected DirectionalProgramUniformLocations generateInitialProgramUniforms(ShaderProgram shaderProgram,
                                                                                String uniformObjectName) {
        final DirectionalProgramUniformLocations uniformLocations = new DirectionalProgramUniformLocations();

        final String directionUniformName = String.format("%s.direction", uniformObjectName);
        uniformLocations.directionUniformLocation = shaderProgram.getUniformLocation(directionUniformName);

        return uniformLocations;
    }

    @Override
    protected void setFlagsToValue(boolean value) {
        this.hasChangedDirection = value;
    }

    @Override
    protected void updateUniforms(DirectionalProgramUniformLocations programUniforms) {
        if (this.hasChangedDirection) {
            glUniform3fv(programUniforms.directionUniformLocation, this.direction);
        }
    }

    protected static class DirectionalProgramUniformLocations extends PhongLight.ProgramUniformLocations {
        public int directionUniformLocation;
    }


}
