package com.tcg.lwjgllearning.graphics.g3d.materials;

import com.tcg.lwjgllearning.graphics.ShaderProgram;

public abstract class PhongMaterial extends Material {
    
    protected final int diffuseUniformLocation;
    protected final int specularUniformLocation;
    protected final int ambientUniformLocation;
    protected final int shininessUniformLocation;

    public float shininess = 0.3f;

    protected PhongMaterial(ShaderProgram shaderProgram, float shininess) {
        super(shaderProgram);
        this.diffuseUniformLocation = this.shaderProgram.getUniformLocation("material.diffuse");
        this.specularUniformLocation = this.shaderProgram.getUniformLocation("material.specular");
        this.ambientUniformLocation = this.shaderProgram.getUniformLocation("material.ambient");
        this.shininessUniformLocation = this.shaderProgram.getUniformLocation("material.shininess");
        this.shininess = shininess;
    }
}
