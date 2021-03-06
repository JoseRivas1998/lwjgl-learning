package com.tcg.lwjgllearning.graphics.g3d.mesh;

import com.tcg.lwjgllearning.graphics.ShaderProgram;
import com.tcg.lwjgllearning.graphics.Texture;
import com.tcg.lwjgllearning.graphics.g3d.materials.ScalarPhongMaterial;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Objects;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TexturedMesh extends MaterialMesh {

    private final float[] uvArray;
    private final Texture texture;
    private final int textureUniformLocation;
    private final int texCoordAttribLocation;
    private final int texCoordVboId;

    public TexturedMesh(ScalarPhongMaterial material, float[] positionArray, float[] normalArray, int[] indexArray, float[] uvArray,
                  Texture texture, Vector3 position, Quaternion rotation, Vector3 scale) {
        super(material, positionArray, normalArray, indexArray, position, rotation, scale);
        this.uvArray = Arrays.copyOf(Objects.requireNonNull(uvArray), uvArray.length);
        this.texture = Objects.requireNonNull(texture);
        this.textureUniformLocation = this.material.shaderProgram.getUniformLocation("texture");
        this.texCoordAttribLocation = this.material.shaderProgram.getAttribLocation("vertTexCoord");

        this.texCoordVboId = this.createBuffer();
    }

    public TexturedMesh(ShaderProgram shaderProgram, float[] positionArray, float[] normalArray, int[] indexArray,
                  float[] uvArray, Texture texture, Vector3 position, Quaternion rotation, Vector3 scale) {
        this(new ScalarPhongMaterial(shaderProgram), positionArray, normalArray,
                indexArray, uvArray, texture, position, rotation, scale);
    }

    public TexturedMesh(ScalarPhongMaterial material, float[] positionArray, float[] normalArray, int[] indexArray,
                  float[] uvArray, Texture texture) {
        this(material, positionArray, normalArray, indexArray, uvArray, texture,
                Vector3.origin(), new Quaternion(), new Vector3(1, 1, 1));
    }

    public TexturedMesh(ShaderProgram shaderProgram, float[] positionArray, float[] normalArray, int[] indexArray,
                  float[] uvArray, Texture texture) {
        this(new ScalarPhongMaterial(shaderProgram), positionArray, normalArray, indexArray, uvArray, texture);
    }

    private int createBuffer() {
        int texCoordVboId = -1;
        FloatBuffer texCoordBuffer = null;
        try {
            glBindVertexArray(this.vaoId);

            texCoordBuffer = MemoryUtil.memAllocFloat(this.uvArray.length);
            texCoordBuffer.put(this.uvArray).flip();

            texCoordVboId = this.createAttributeBuffer(Attribute.TEXTURE_COORDINATE);
            glBindBuffer(GL_ARRAY_BUFFER, texCoordVboId);
            glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(this.texCoordAttribLocation);
            glVertexAttribPointer(this.texCoordAttribLocation, 2, GL_FLOAT, false, 0, 0);

            glBindVertexArray(0);
        } finally {
            if (texCoordBuffer != null) {
                MemoryUtil.memFree(texCoordBuffer);
            }
        }
        return texCoordVboId;
    }

    public void activateAttribute(Attribute attribute, int attributeLocation) {
        this.activateAttribute2f(attribute, attributeLocation);
    }

    @Override
    public void activate() {
        super.activate();
        glUniform1i(this.textureUniformLocation, 0);
        glActiveTexture(GL_TEXTURE0);
        this.texture.bind();
    }

    @Override
    public void deactivate() {
        this.texture.unbind();
        super.deactivate();
    }

    @Override
    public void dispose() {
        glDeleteBuffers(this.texCoordVboId);
        super.dispose();
    }

    public enum Attribute implements AbstractAttribute {
        TEXTURE_COORDINATE
    }
}
