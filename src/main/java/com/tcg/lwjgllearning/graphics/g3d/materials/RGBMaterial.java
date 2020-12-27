package com.tcg.lwjgllearning.graphics.g3d.materials;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.math.Vector3;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3fv;

public class RGBMaterial extends Material{

    private final float[] diffuse = new float[3];
    private final float[] specular = new float[3];
    private final float[] ambient = new float[3];
    public float shininess = 0.3f;

    private final int diffuseUniformLocation;
    private final int specularUniformLocation;
    private final int ambientUniformLocation;
    private final int shininessUniformLocation;

    public RGBMaterial(ShaderProgram shaderProgram,
                       Vector3 diffuse, Vector3 specular, Vector3 ambient, float shininess) {
        super(shaderProgram);
        this.setDiffuse(diffuse);
        this.setSpecular(specular);
        this.setAmbient(ambient);
        this.shininess = shininess;
        this.diffuseUniformLocation = this.shaderProgram.getUniformLocation("material.diffuse");
        this.specularUniformLocation = this.shaderProgram.getUniformLocation("material.specular");
        this.ambientUniformLocation = this.shaderProgram.getUniformLocation("material.ambient");
        this.shininessUniformLocation = this.shaderProgram.getUniformLocation("material.shininess");
    }

    public RGBMaterial(ShaderProgram shaderProgram) {
        this(shaderProgram, new Vector3(1, 1, 1), new Vector3(1, 1, 1),
                new Vector3(1, 1, 1), 0.3f);
    }

    public Vector3 getDiffuse() {
        return new Vector3(this.diffuse[0], this.diffuse[1], this.diffuse[2]);
    }

    public void setDiffuse(Vector3 diffuse) {
        System.arraycopy(diffuse.asArray(), 0, this.diffuse, 0, this.diffuse.length);
    }

    public Vector3 getSpecular() {
        return new Vector3(this.specular[0], this.specular[1], this.specular[2]);
    }

    public void setSpecular(Vector3 specular) {
        System.arraycopy(specular.asArray(), 0, this.specular, 0, this.specular.length);
    }

    public Vector3 getAmbient() {
        return new Vector3(this.ambient[0], this.ambient[1], this.ambient[2]);
    }

    public void setAmbient(Vector3 ambient) {
        System.arraycopy(ambient.asArray(), 0, this.ambient, 0, this.ambient.length);
    }


    @Override
    public void activate() {
        glUniform3fv(this.diffuseUniformLocation, this.diffuse);
        glUniform3fv(this.specularUniformLocation, this.specular);
        glUniform3fv(this.ambientUniformLocation, this.ambient);
        glUniform1f(this.shininessUniformLocation, this.shininess);
    }

    @Override
    public void deactivate() {

    }
}
