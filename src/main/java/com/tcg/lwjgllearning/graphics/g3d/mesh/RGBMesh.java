package com.tcg.lwjgllearning.graphics.g3d.mesh;

import com.tcg.lwjgllearning.graphics.Color;
import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.g3d.materials.RGBMaterial;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class RGBMesh extends Mesh {

    private final float[] colorArray;
    private int colorVboId;

    public RGBMesh(RGBMaterial material, float[] positionArray, float[] normalArray, int[] indexArray, Color[] colors,
                   Vector3 position, Quaternion rotation, Vector3 scale) {
        super(material, positionArray, normalArray, indexArray, position, rotation, scale);
        this.colorArray = Color.colorArrayToFloatArray(colors);
        this.createColorBuffer();
    }

    public RGBMesh(ShaderProgram shaderProgram, float[] positionArray, float[] normalArray, int[] indexArray,
                   Color[] colors, Vector3 position, Quaternion rotation, Vector3 scale) {
        this(new RGBMaterial(shaderProgram), positionArray, normalArray, indexArray, colors, position, rotation, scale);
    }

    public RGBMesh(RGBMaterial material, float[] positionArray, float[] normalArray, int[] indexArray, Color[] colors) {
        this(material, positionArray, normalArray, indexArray, colors,
                Vector3.origin(), new Quaternion(), new Vector3(1, 1, 1));
    }

    public RGBMesh(ShaderProgram shaderProgram, float[] positionArray, float[] normalArray, int[] indexArray,
                   Color[] colors) {
        this(new RGBMaterial(shaderProgram), positionArray, normalArray, indexArray, colors);
    }

    private void createColorBuffer() {
        FloatBuffer colorBuffer = null;

        try {
            glBindVertexArray(this.vaoId);

            colorBuffer = MemoryUtil.memAllocFloat(this.colorArray.length);
            colorBuffer.put(this.colorArray).flip();

            this.colorVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, this.colorVboId);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            int colorAttribLocation = this.material.shaderProgram.getAttribLocation("vertColor");
            glEnableVertexAttribArray(colorAttribLocation);
            glVertexAttribPointer(colorAttribLocation, 4, GL_FLOAT, false, 0, 0);

            glBindVertexArray(0);
        } finally {
            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
        }
    }

    @Override
    public void dispose() {
        glDeleteBuffers(this.colorVboId);
        super.dispose();
    }
}
