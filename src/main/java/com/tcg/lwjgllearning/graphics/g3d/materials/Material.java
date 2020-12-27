package com.tcg.lwjgllearning.graphics.g3d.materials;

import com.tcg.lwjgllearning.graphics.ShaderProgram;

public abstract class Material {
    public final ShaderProgram shaderProgram;

    public Material(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public abstract void activate();
    public abstract void deactivate();

}
