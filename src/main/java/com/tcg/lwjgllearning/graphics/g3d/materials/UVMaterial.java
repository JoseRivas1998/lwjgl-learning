package com.tcg.lwjgllearning.graphics.g3d.materials;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.Texture;

import java.util.Objects;

import static org.lwjgl.opengl.GL20.glUniform1f;

public class UVMaterial extends Material {

    public float diffuse;
    public float specular;
    public float ambient;
    public float shininess;
    private final int diffuseUniformLocation;
    private final int specularUniformLocation;
    private final int ambientUniformLocation;
    private final int shininessUniformLocation;

    public UVMaterial(ShaderProgram shaderProgram, float diffuse, float specular, float ambient, float shininess) {
        super(shaderProgram);

        this.diffuse = diffuse;
        this.specular = specular;
        this.ambient = ambient;
        this.shininess = shininess;

        this.diffuseUniformLocation = this.shaderProgram.getUniformLocation("material.diffuse");
        this.specularUniformLocation = this.shaderProgram.getUniformLocation("material.specular");
        this.ambientUniformLocation = this.shaderProgram.getUniformLocation("material.ambient");
        this.shininessUniformLocation = this.shaderProgram.getUniformLocation("material.shininess");
    }

    public UVMaterial(ShaderProgram shaderProgram) {
        this(shaderProgram, 1f, 1f, 1f, 1f);
    }

    @Override
    public void activate() {
        glUniform1f(this.diffuseUniformLocation, this.diffuse);
        glUniform1f(this.specularUniformLocation, this.specular);
        glUniform1f(this.ambientUniformLocation, this.ambient);
        glUniform1f(this.shininessUniformLocation, this.shininess);
    }

    @Override
    public void deactivate() {
    }
}
