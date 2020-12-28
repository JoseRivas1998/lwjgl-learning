package com.tcg.lwjgllearning.graphics;

import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Matrix4;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;

import java.util.Arrays;
import java.util.Collection;

/**
 * float aspectRatio, float viewRaians, float near, float far,
 */

public class PerspectiveCamera extends Camera {

    private static final float DEFAULT_ASPECT = 1f;
    private static final float DEFAULT_VIEW_RADIANS = MathUtils.PI / 4f;
    private static final float DEFAULT_NEAR = 0.01f;
    private static final float DEFAULT_FAR = 1000.0f;
    private float aspectRatio;
    private float viewRadians;
    private float near;
    private float far;

    public PerspectiveCamera(Collection<ShaderProgram> programs,
                             float aspectRatio, float viewRadians, float near, float far,
                             Vector3 position, Quaternion rotation) {
        super(programs, position, rotation);

        this.aspectRatio = aspectRatio;
        this.viewRadians = viewRadians;
        this.near = near;
        this.far = far;

        this.update();

    }

    public PerspectiveCamera(Collection<ShaderProgram> programs, Vector3 position, Quaternion rotation) {
        this(programs, DEFAULT_ASPECT, DEFAULT_VIEW_RADIANS, DEFAULT_NEAR, DEFAULT_FAR, position, rotation);
    }

    public PerspectiveCamera(ShaderProgram[] programs,
                             float aspectRatio, float viewRadians, float near, float far,
                             Vector3 position, Quaternion rotation) {
        this(Arrays.asList(programs), aspectRatio, viewRadians, near, far, position, rotation);
    }

    public PerspectiveCamera(ShaderProgram[] programs, Vector3 position, Quaternion rotation) {
        this(Arrays.asList(programs), DEFAULT_ASPECT, DEFAULT_VIEW_RADIANS, DEFAULT_NEAR, DEFAULT_FAR,
                position, rotation);
    }

    public PerspectiveCamera(Collection<ShaderProgram> programs,
                             float aspectRatio, float viewRadians, float near, float far) {
        this(programs, aspectRatio, viewRadians, near, far, Vector3.origin(), new Quaternion());
    }

    public PerspectiveCamera(Collection<ShaderProgram> programs) {
        this(programs, DEFAULT_ASPECT, DEFAULT_VIEW_RADIANS, DEFAULT_NEAR, DEFAULT_FAR,
                Vector3.origin(), new Quaternion());
    }

    public PerspectiveCamera(ShaderProgram[] programs,
                             float aspectRatio, float viewRadians, float near, float far) {
        this(Arrays.asList(programs), aspectRatio, viewRadians, near, far, Vector3.origin(), new Quaternion());
    }

    public PerspectiveCamera(ShaderProgram[] programs) {
        this(Arrays.asList(programs), DEFAULT_ASPECT, DEFAULT_VIEW_RADIANS, DEFAULT_NEAR, DEFAULT_FAR,
                Vector3.origin(), new Quaternion());
    }

    public void setFieldOfView(float viewRadians) {
        this.viewRadians = viewRadians;
        this.needsProjectionUpdate = true;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        this.needsProjectionUpdate = true;
    }

    public void setNearClip(float distance) {
        this.near = distance;
        this.needsProjectionUpdate = true;
    }

    public void setFarClip(float distance) {
        this.far = distance;
        this.needsProjectionUpdate = true;
    }

    public void setPerspective(float viewRadians, float aspectRatio, float near, float far) {
        this.viewRadians = viewRadians;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;
        this.needsProjectionUpdate = true;
    }

    @Override
    protected Matrix4 generateProjectionMatrix() {
        return Matrix4.perspective(this.viewRadians, this.aspectRatio, this.near, this.far);
    }
}
