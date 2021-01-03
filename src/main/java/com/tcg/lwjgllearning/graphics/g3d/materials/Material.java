package com.tcg.lwjgllearning.graphics.g3d.materials;

import com.tcg.lwjgllearning.graphics.ShaderProgram;

public abstract class Material {

    final public ShaderProgram shaderProgram;

    protected Material (ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public abstract void activate();
    public abstract void deactivate();

}
