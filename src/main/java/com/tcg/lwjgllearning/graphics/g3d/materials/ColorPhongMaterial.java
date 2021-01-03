package com.tcg.lwjgllearning.graphics.g3d.materials;

import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3fv;

public class ColorPhongMaterial extends PhongMaterial {

    private final float[] diffuse = new float[3];
    private final float[] specular = new float[3];
    private final float[] ambient = new float[3];

    public ColorPhongMaterial(ShaderProgram shaderProgram,
                       Color diffuse, Color specular, Color ambient, float shininess) {
        super(shaderProgram, shininess);
        this.setDiffuse(diffuse);
        this.setSpecular(specular);
        this.setAmbient(ambient);
    }

    public ColorPhongMaterial(ShaderProgram shaderProgram) {
        this(shaderProgram, Color.rgb888(0xFFFFFF), Color.rgb888(0xFFFFFF), Color.rgb888(0xFFFFFF), 0.3f);
    }

    public Color getDiffuse() {
        return new Color(this.diffuse[0], this.diffuse[1], this.diffuse[2], 1f);
    }

    public void setDiffuse(Color diffuse) {
        System.arraycopy(diffuse.asArray(), 0, this.diffuse, 0, this.diffuse.length);
    }

    public Color getSpecular() {
        return new Color(this.specular[0], this.specular[1], this.specular[2], 1f);
    }

    public void setSpecular(Color specular) {
        System.arraycopy(specular.asArray(), 0, this.specular, 0, this.specular.length);
    }

    public Color getAmbient() {
        return new Color(this.ambient[0], this.ambient[1], this.ambient[2], 1f);
    }

    public void setAmbient(Color ambient) {
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
