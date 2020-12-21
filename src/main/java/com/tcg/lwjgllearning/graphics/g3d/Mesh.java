package com.tcg.lwjgllearning.graphics.g3d;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Transform3D;
import com.tcg.lwjgllearning.math.Vector3;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh extends Transform3D {

    protected final ShaderProgram shaderProgram;
    protected final float[] positionArray;
    protected final float[] normalArray;
    protected final int[] indexArray;
    private int mWorldUniformLocation;
    private int mNormalUniformLocation;
    private int positionAttribLocation;
    private int normalAttribLocation;
    private int vaoId;
    private int positionVboId;
    private int normalVboId;
    private int indexVboId;

    public Mesh(ShaderProgram shaderProgram, float[] positionArray, float[] normalArray, int[] indexArray, Vector3 position, Quaternion rotation, Vector3 scale) {
        super(position, rotation, scale);
        this.shaderProgram = Objects.requireNonNull(shaderProgram);
        this.positionArray = Arrays.copyOf(Objects.requireNonNull(positionArray), positionArray.length);
        this.normalArray = Arrays.copyOf(Objects.requireNonNull(normalArray), normalArray.length);
        this.indexArray = Arrays.copyOf(Objects.requireNonNull(indexArray), indexArray.length);

        this.createBuffers();
    }

    public Mesh(ShaderProgram shaderProgram, float[] positionArray, float[] normalArray, int[] indexArray) {
        this(shaderProgram, positionArray, normalArray, indexArray,
                Vector3.origin(), new Quaternion(), new Vector3(1, 1, 1));
    }

    private void createBuffers() {
        FloatBuffer positionBuffer = null;
        FloatBuffer normalBuffer = null;
        IntBuffer indexBuffer = null;

        try {
            this.mWorldUniformLocation = this.shaderProgram.getUniformLocation("mWorld");
            this.mNormalUniformLocation = this.shaderProgram.getUniformLocation("mNormal");


            this.vaoId = glGenVertexArrays();
            glBindVertexArray(this.vaoId);

            positionBuffer = MemoryUtil.memAllocFloat(this.positionArray.length);
            positionBuffer.put(this.positionArray).flip();

            this.positionAttribLocation = this.shaderProgram.getAttribLocation("vertPosition");
            this.positionVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, this.positionVboId);
            glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(this.positionAttribLocation);
            glVertexAttribPointer(this.positionAttribLocation, 3, GL_FLOAT, false, 0, 0);

            normalBuffer = MemoryUtil.memAllocFloat(this.normalArray.length);
            normalBuffer.put(this.normalArray).flip();

            this.normalAttribLocation = this.shaderProgram.getAttribLocation("vertNormal");
            this.normalVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, this.normalVboId);
            glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(this.normalAttribLocation);
            glVertexAttribPointer(this.normalAttribLocation, 3, GL_FLOAT, false, 0, 0);

            indexBuffer = MemoryUtil.memAllocInt(this.indexArray.length);
            indexBuffer.put(this.indexArray).flip();

            this.indexVboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            glBindVertexArray(0);

        } finally {
            if (positionBuffer != null) {
                MemoryUtil.memFree(positionBuffer);
            }
            if (normalBuffer != null) {
                MemoryUtil.memFree(normalBuffer);
            }
            if (indexBuffer != null) {
                MemoryUtil.memFree(indexBuffer);
            }
        }
    }

    public void activate() {
        this.update();
        this.shaderProgram.bind();
        glBindVertexArray(this.vaoId);
        glUniformMatrix4fv(this.mWorldUniformLocation, false, this.worldMatrix());
        glUniformMatrix3fv(this.mNormalUniformLocation, false, this.normalMatrix());
    }

    public void draw() {
        this.activate();
        glDrawElements(GL_TRIANGLES, this.indexArray.length, GL_UNSIGNED_INT, 0);
        this.deactivate();
    }

    public void deactivate() {
        glBindVertexArray(0);
        this.shaderProgram.unbind();
    }

}
