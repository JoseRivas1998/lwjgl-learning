package com.tcg.lwjgllearning.graphics.g3d.materials;

import com.tcg.lwjgllearning.graphics.ShaderProgram;

import static org.lwjgl.opengl.GL20.glUniform1f;

public class ScalarPhongMaterial extends PhongMaterial {

    public float diffuse;
    public float specular;
    public float ambient;

    public ScalarPhongMaterial(ShaderProgram shaderProgram, float diffuse, float specular, float ambient, float shininess) {
        super(shaderProgram, shininess);

        this.diffuse = diffuse;
        this.specular = specular;
        this.ambient = ambient;
    }

    public ScalarPhongMaterial(ShaderProgram shaderProgram) {
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
