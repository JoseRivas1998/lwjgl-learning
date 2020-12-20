package com.tcg.lwjgllearning.graphics.g2d;

import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.math.Vector2;

import static org.lwjgl.opengl.GL20.glUniform4fv;

public class UniformColorDrawable extends Drawable{

    private final Color color;
    private final int colorUniformLocation;

    public UniformColorDrawable(ShaderProgram shaderProgram, float[] positionArray, int[] indexArray, Color color, Vector2 position, float rotation, Vector2 scale) {
        super(shaderProgram, positionArray, indexArray, position, rotation, scale);

        this.color = color.copy();
        colorUniformLocation = this.shaderProgram.getUniformLocation("color");
    }

    public UniformColorDrawable(ShaderProgram shaderProgram, float[] positionArray, int[] indexArray, Color color) {
        this(shaderProgram, positionArray, indexArray, color, new Vector2(), 0f, new Vector2(1f, 1f));
    }

    @Override
    public void activate() {
        super.activate();
        glUniform4fv(this.colorUniformLocation, this.color.asArray());
    }
}
