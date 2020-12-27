package com.tcg.lwjgllearning.graphics.g3d.lighting;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;

import java.util.Arrays;
import java.util.Collection;

import static org.lwjgl.opengl.GL20.glUniform3fv;

public class PointLight extends PhongLight<PointLight.PointProgramUniformLocations> {

    private final Vector3 position;
    private boolean hasChangedPosition;

    public PointLight(ShaderProgram[] programs, int index, Vector3 position,
                      Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        this(Arrays.asList(programs), index, position, diffuse, specular, ambient);
    }

    public PointLight(Collection<ShaderProgram> programs, int index, Vector3 position,
                      Vector3 diffuse, Vector3 specular, Vector3 ambient) {
        super(programs, index, diffuse, specular, ambient);

        this.position = position.copy();
        this.hasChangedPosition = true;
    }

    public void setPosition(Vector3 position) {
        this.position.set(position);
        this.hasChangedPosition = true;
        this.hasChanged = true;
    }

    public void translate(Vector3 translation) {
        this.position.addInPlace(translation);
        this.hasChangedPosition = true;
        this.hasChanged = true;
    }

    public void rotateAround(Vector3 point, Quaternion quaternion) {
        this.position.subInPlace(point);
        this.position.rotateInPlace(quaternion);
        this.position.addInPlace(point);
        this.hasChangedPosition = true;
        this.hasChanged = true;
    }

    @Override
    protected String uniformObjectName() {
        return String.format("pointLights[%d]", this.index);
    }

    @Override
    protected PointProgramUniformLocations generateInitialProgramUniforms(ShaderProgram shaderProgram,
                                                                          String uniformObjectName) {
        final PointProgramUniformLocations uniformLocations = new PointProgramUniformLocations();

        final String positionUniformName = String.format("%s.position", uniformObjectName);
        uniformLocations.positionUniformLocation = shaderProgram.getUniformLocation(positionUniformName);

        return uniformLocations;
    }

    @Override
    protected void setFlagsToValue(boolean value) {
        this.hasChangedPosition = value;
    }

    @Override
    protected void updateUniforms(PointProgramUniformLocations programUniforms) {
        if (this.hasChangedPosition) {
            glUniform3fv(programUniforms.positionUniformLocation, this.position.asArray());
        }
    }

    protected static class PointProgramUniformLocations extends PhongLight.ProgramUniformLocations {
        public int positionUniformLocation;
    }

}
