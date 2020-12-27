package com.tcg.lwjgllearning.graphics.g3d.mesh;

import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.g3d.materials.RGBMaterial;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;

import static org.lwjgl.opengl.GL20.glUniform4fv;

public class UniformColorMesh extends Mesh {

    private final int uColorUniformLocation;
    private final Color uColor;

    public UniformColorMesh(RGBMaterial material, float[] positionArray, float[] normalArray, int[] indexArray,
                            Color color, Vector3 position, Quaternion rotation, Vector3 scale) {
        super(material, positionArray, normalArray, indexArray, position, rotation, scale);
        this.uColorUniformLocation = this.material.shaderProgram.getUniformLocation("u_color");
        this.uColor = color.copy();
    }

    public UniformColorMesh(ShaderProgram shaderProgram, float[] positionArray, float[] normalArray, int[] indexArray,
                            Color color, Vector3 position, Quaternion rotation, Vector3 scale) {
        this(new RGBMaterial(shaderProgram), positionArray, normalArray, indexArray, color, position, rotation, scale);
    }

    public UniformColorMesh(RGBMaterial material, float[] positionArray, float[] normalArray, int[] indexArray,
                            Color color) {
        this(material, positionArray, normalArray, indexArray, color,
                Vector3.origin(), new Quaternion(), new Vector3(1, 1, 1));
    }

    public UniformColorMesh(ShaderProgram shaderProgram, float[] positionArray, float[] normalArray, int[] indexArray,
                            Color color) {
        this(new RGBMaterial(shaderProgram), positionArray, normalArray, indexArray, color);
    }

    @Override
    public void activate() {
        super.activate();
        glUniform4fv(this.uColorUniformLocation, this.uColor.asArray());
    }
}
