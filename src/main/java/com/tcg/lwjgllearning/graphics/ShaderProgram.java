package com.tcg.lwjgllearning.graphics;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ShaderProgram {

    private final int programId;

    private int vertexShaderId;
    private int fragmentShaderId;

    private boolean compiled;

    /*
     * CONSTRUCTORS
     */

    public ShaderProgram() {
        programId = crateProgram();
    }

    /*
     * STATIC CREATORS
     */
    public ShaderProgram buildShader(String vertexShader, String fragmentShader) {
        ShaderProgram shaderProgram = new ShaderProgram();
        shaderProgram.compile(vertexShader, fragmentShader);
        return shaderProgram;
    }

    /*
     * PUBLIC
     */

    public void bind() {
        verifyCompiled();
        glUseProgram(this.programId);
    }

    public void cleanUp() {
        unbind();
        deleteProgram();
    }

    public void compile(String vertexShader, String fragmentShader) {
        this.vertexShaderId = createShaderOfType(vertexShader, GL_VERTEX_SHADER);
        this.fragmentShaderId = createShaderOfType(fragmentShader, GL_FRAGMENT_SHADER);
    }

    public void unbind() {
        glUseProgram(0);
    }

    /*
     * PRIVATE
     */

    private void compileShader(String shaderCode, int shaderId) {
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);
        verifyShaderIsCompiled(shaderId);
    }

    private int crateProgram() {
        final int programId;
        programId = glCreateProgram();
        if (programId == NULL) {
            throw new RuntimeException("Unable to create shader");
        }
        return programId;
    }

    private int createShader(int shaderType) {
        final int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            String shaderTypeName = shaderType == GL_VERTEX_SHADER ? "vertex" : "fragment";
            throw new RuntimeException("Unable to create " + shaderTypeName + " shader.");
        }
        return shaderId;
    }

    private int createShaderOfType(String shaderCode, int shaderType) {
        verifyNotCompiled();
        final int shaderId = createShader(shaderType);
        compileShader(shaderCode, shaderId);
        glAttachShader(programId, shaderId);
        return shaderId;
    }

    private void deleteProgram() {
        if (programId != NULL) {
            glDeleteProgram(this.programId);
        }
    }

    private void verifyCompiled() {
        if (!compiled) throw new IllegalStateException("Shader must be compiled.");
    }

    private void verifyNotCompiled() {
        if (compiled) throw new IllegalStateException("Shader cannot be compiled twice");
    }

    private void verifyShaderIsCompiled(int shaderId) {
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == NULL) {
            final String shaderLog = glGetShaderInfoLog(shaderId);
            throw new RuntimeException(String.format("Error compiling shader: %s", shaderLog));
        }
    }

}