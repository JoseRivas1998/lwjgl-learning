package com.tcg.lwjgllearning.graphics;

import com.tcg.lwjgllearning.math.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public abstract class Camera extends Transform3D {

    private final List<ShaderUniformLocations> shaders = new ArrayList<>();

    protected final Vector3 localRight;
    protected final Vector3 localUp;
    protected final Vector3 forward;

    protected Matrix4 view;
    protected Matrix4 projection;
    protected Matrix4 combined;

    protected boolean needsProjectionUpdate;

    public Camera(Collection<ShaderProgram> programs, Vector3 position, Quaternion rotation) {
        super(position, rotation, new Vector3(1f, 1f, 1f));

        this.shaders.addAll(this.buildAllShaderUniformLocations(programs));

        this.updatePositions();

        this.localRight = Vector3.x().rotateOutPlace(rotation);
        this.localUp = Vector3.y().rotateOutPlace(rotation);
        this.forward = new Vector3(0f, 0f, -1f).rotateOutPlace(rotation);

        this.updateViewMatrix();

        // defer updates to subclass constructors or first update
        this.needsProjectionUpdate = true;

    }

    public Camera(ShaderProgram[] programs, Vector3 position, Quaternion rotation) {
        this(Arrays.asList(programs), position, rotation);
    }

    public Camera(Collection<ShaderProgram> programs) {
        this(programs, Vector3.origin(), new Quaternion());
    }

    public Camera(ShaderProgram[] programs) {
        this(Arrays.asList(programs), Vector3.origin(), new Quaternion());
    }

    public void addShader(ShaderProgram shaderProgram) {
        this.shaders.add(this.buildShaderUniformLocations(shaderProgram));
        this.needsUpdate = true;
        this.needsProjectionUpdate = true;
    }

    public void lookAt(Vector3 target, Vector3 up) {
        this.forward.set(target.subOutPlace(this.position));
        this.forward.normalizeInPlace();

        this.localRight.set(this.forward.cross(up));
        this.localRight.normalizeInPlace();

        this.localUp.set(this.localRight.cross(this.forward));
        this.localUp.normalizeInPlace();

        final float tr = this.localRight.x + this.localUp.y - this.forward.z;

        float S;
        float w;
        float x;
        float y;
        float z;

        if (Float.compare(tr, 0f) > 0) {
            S = MathUtils.sqrt(tr + 1) * 2;
            w = S / 4;
            x = (this.localUp.z + this.forward.y) / S;
            y = (-this.forward.x - this.localRight.z) / S;
            z = (this.localRight.y - this.localUp.x) / S;
        } else if (Float.compare(this.localRight.x, this.localUp.y) > 0
                && Float.compare(this.localRight.x, -this.forward.z) > 0) {
            S = MathUtils.sqrt(1f + this.localRight.x - this.localUp.y + this.forward.z) * 2f;
            w = (this.localUp.z + this.forward.y) / S;
            x = S / 4f;
            y = (this.localUp.x + this.localRight.y) / S;
            z = (-this.forward.x + this.localRight.z) / S;
        } else if (Float.compare(this.localUp.y, -this.forward.z) > 0) {
            S = MathUtils.sqrt(1f + this.localUp.y - this.localRight.x + this.forward.z) * 2f;
            w = (-this.forward.x - this.localRight.z) / S;
            x = (this.localUp.x + this.localRight.y) / S;
            y = S / 4f;
            z = (-this.forward.y + this.localUp.z) / S;
        } else {
            S = MathUtils.sqrt(1f - this.forward.z - this.localRight.x - this.localUp.y) * 2f;
            w = (this.localRight.y - this.localUp.x) / S;
            x = (-this.forward.x + this.localRight.z) / S;
            y = (-this.forward.y + this.localUp.z) / S;
            z = S / 4f;
        }

        this.rotation.set(w, x, y, z);
        this.updateLocalDirections();

        this.updateViewMatrix();
        this.updateCombined();
    }

    public void lookAt(Vector3 target) {
        this.lookAt(target, this.localUp);
    }

    public Matrix4 getView() {
        return Matrix4.copyOf(this.view);
    }

    public Matrix4 getProjection() {
        return Matrix4.copyOf(this.projection);
    }

    public Matrix4 getCombined() {
        return Matrix4.copyOf(this.combined);
    }

    @Override
    public void setScale(Vector3 scale) {
        throw new UnsupportedOperationException("Scaling is not supported by Cameras");
    }

    @Override
    public void scaleBy(Vector3 scale) {
        throw new UnsupportedOperationException("Scaling is not supported by Cameras");
    }

    @Override
    public void update() {
        final boolean needsCombinedUpdate = this.needsUpdate || this.needsProjectionUpdate;
        if (this.needsUpdate) {
            if (this.hasRotated) {
                this.updateLocalDirections();
            }
            if (this.hasMoved) {
                this.updatePositions();
            }
            this.updateViewMatrix();
        }
        if (this.needsProjectionUpdate) {
            this.updateProjectionMatrix();
        }
        if (needsCombinedUpdate) {
            this.updateCombined();
        }
    }

    protected abstract Matrix4 generateProjectionMatrix();

    private List<ShaderUniformLocations> buildAllShaderUniformLocations(Collection<ShaderProgram> programs) {
        return programs.stream()
                .map(this::buildShaderUniformLocations)
                .collect(Collectors.toList());
    }

    private ShaderUniformLocations buildShaderUniformLocations(ShaderProgram shaderProgram) {
        shaderProgram.bind();
        final ShaderUniformLocations shaderUniforms = new ShaderUniformLocations();
        shaderUniforms.program = shaderProgram;
        shaderUniforms.projViewUniformLocation = shaderProgram.getUniformLocation("cam.mProjView");
        shaderUniforms.positionUniformLocation = shaderProgram.getUniformLocation("cam.position");
        shaderProgram.unbind();
        return shaderUniforms;
    }

    private void updateLocalDirections() {
        this.localRight.set(Vector3.x().rotateOutPlace(this.rotation));
        this.localUp.set(Vector3.y().rotateOutPlace(this.rotation));
        this.forward.set(new Vector3(0f, 0f, -1f).rotateOutPlace(this.rotation));
        this.hasRotated = false;
    }

    private void updateCombined() {
        this.combined = Matrix4.mul(this.projection, this.view);
        this.shaders.forEach(shaderUniformLocations -> {
            shaderUniformLocations.program.bind();
            glUniformMatrix4fv(shaderUniformLocations.projViewUniformLocation, false, this.combined.asArray());
            shaderUniformLocations.program.unbind();
        });
    }

    private void updateProjectionMatrix() {
        this.projection = this.generateProjectionMatrix();
        this.needsProjectionUpdate = false;
    }

    private void updateViewMatrix() {
        this.view = Matrix4.view(this.position, this.position.addOutPlace(this.forward), this.localUp);
        this.needsUpdate = false;
    }

    private void updatePositions() {
        this.shaders.forEach(shaderUniformLocations -> {
            shaderUniformLocations.program.bind();
            glUniform3fv(shaderUniformLocations.positionUniformLocation, this.position.asArray());
            shaderUniformLocations.program.unbind();
        });
        this.hasMoved = false;
    }

    private static class ShaderUniformLocations {
        public ShaderProgram program;
        public int projViewUniformLocation;
        public int positionUniformLocation;
    }

}
