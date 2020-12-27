package com.tcg.lwjgllearning.graphics.g3d.mesh;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.g3d.materials.Material;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Transform3D;
import com.tcg.lwjgllearning.math.Vector3;
import com.tcg.lwjgllearning.utils.Disposable;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh extends Transform3D implements Disposable {

    protected final float[] positionArray;
    protected final float[] normalArray;
    protected final int[] indexArray;
    protected final Material material;
    protected int mWorldUniformLocation;
    protected int mNormalUniformLocation;
    protected int positionAttribLocation;
    protected int normalAttribLocation;

    protected int vaoId;
    protected int positionVboId;
    protected int normalVboId;
    protected int indexVboId;

    public Mesh(Material material, float[] positionArray, float[] normalArray, int[] indexArray, Vector3 position, Quaternion rotation, Vector3 scale) {
        super(position, rotation, scale);
        this.material = material;
        this.positionArray = Arrays.copyOf(Objects.requireNonNull(positionArray), positionArray.length);
        this.normalArray = Arrays.copyOf(Objects.requireNonNull(normalArray), normalArray.length);
        this.indexArray = Arrays.copyOf(Objects.requireNonNull(indexArray), indexArray.length);

        this.createBuffers();
    }

    public Mesh(Material material, float[] positionArray, float[] normalArray, int[] indexArray) {
        this(material, positionArray, normalArray, indexArray,
                Vector3.origin(), new Quaternion(), new Vector3(1, 1, 1));
    }

    private void createBuffers() {
        FloatBuffer positionBuffer = null;
        FloatBuffer normalBuffer = null;
        IntBuffer indexBuffer = null;

        try {
            this.mWorldUniformLocation = this.material.shaderProgram.getUniformLocation("mWorld");
            this.mNormalUniformLocation = this.material.shaderProgram.getUniformLocation("mNormal");


            this.vaoId = glGenVertexArrays();
            glBindVertexArray(this.vaoId);

            positionBuffer = MemoryUtil.memAllocFloat(this.positionArray.length);
            positionBuffer.put(this.positionArray).flip();

            this.positionAttribLocation = this.material.shaderProgram.getAttribLocation("vertPosition");
            this.positionVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, this.positionVboId);
            glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(this.positionAttribLocation);
            glVertexAttribPointer(this.positionAttribLocation, 3, GL_FLOAT, false, 0, 0);

            normalBuffer = MemoryUtil.memAllocFloat(this.normalArray.length);
            normalBuffer.put(this.normalArray).flip();

            this.normalAttribLocation = this.material.shaderProgram.getAttribLocation("vertNormal");
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
        this.material.shaderProgram.bind();
        glBindVertexArray(this.vaoId);
        glUniformMatrix4fv(this.mWorldUniformLocation, false, this.worldMatrix());
        glUniformMatrix3fv(this.mNormalUniformLocation, false, this.normalMatrix());
        this.material.activate();
    }

    public void draw() {
        this.activate();
        glDrawElements(GL_TRIANGLES, this.indexArray.length, GL_UNSIGNED_INT, 0);
        this.deactivate();
    }

    public void deactivate() {
        this.material.deactivate();
        glBindVertexArray(0);
        this.material.shaderProgram.unbind();
    }

    @Override
    public void dispose() {
        glDeleteBuffers(this.positionVboId);
        glDeleteBuffers(this.normalVboId);
        glDeleteBuffers(this.indexVboId);
        glDeleteVertexArrays(this.vaoId);
    }
}
