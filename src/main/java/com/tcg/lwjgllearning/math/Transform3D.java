package com.tcg.lwjgllearning.math;

import java.util.Objects;

public class Transform3D {

    private final Vector3 position;
    private final Quaternion rotation;
    private final Vector3 scale;

    private Matrix4 mTranslate;
    private Matrix4 mRotate;
    private Matrix4 mScale;
    private Matrix4 mWorld;

    private boolean hasMoved;
    private boolean hasRotated;
    private boolean hasScaled;
    private boolean needsUpdate;

    public Transform3D(Vector3 position, Quaternion rotation, Vector3 scale) {
        this.position = Objects.requireNonNull(position).copy();
        this.rotation = Objects.requireNonNull(rotation).copy();
        this.scale = Objects.requireNonNull(scale).copy();
        mTranslate = Matrix4.translation(this.position);
        mRotate = Matrix4.rotation(this.rotation);
        mScale = Matrix4.scale(this.scale);
        mWorld = Matrix4.worldMatrix(this.mTranslate, this.mRotate, this.mScale);
        hasMoved = false;
        hasRotated = false;
        hasScaled = false;
        needsUpdate = false;
    }

    public Transform3D() {
        this(Vector3.origin(), new Quaternion(), new Vector3(1, 1, 1));
    }

    public void setPosition(Vector3 position) {
        this.position.set(position);
        this.hasMoved = true;
        this.needsUpdate = true;
    }

    public void setRotation(Quaternion quaternion) {
        this.rotation.set(quaternion.w, quaternion.x, quaternion.y, quaternion.z);
        this.hasRotated = true;
        this.needsUpdate = true;
    }

    public void setScale(Vector3 scale) {
        this.scale.set(scale);
        this.hasScaled = true;
        this.needsUpdate = true;
    }

    public void translate(Vector3 translation) {
        this.position.addInPlace(translation);
        this.hasMoved = true;
        this.needsUpdate = true;
    }

    public void rotate(Quaternion q) {
        this.rotation.composeInPlace(q);
        this.hasRotated = true;
        this.needsUpdate = true;
    }

    public void localRotate(Quaternion q) {
        this.rotation.localComposeInPlace(q);
        this.hasRotated = true;
        this.needsUpdate = true;
    }

    public void rotateAround(Vector3 point, Quaternion rot, boolean lockOrientation) {
        this.position.subInPlace(point);
        this.position.rotateInPlace(rot);
        this.position.addInPlace(point);
        if (!lockOrientation) {
            this.rotate(rot);
        }
        this.hasMoved = true;
        this.needsUpdate = true;
    }

    public void scaleBy(Vector3 scale) {
        this.scale.scaleInPlace(scale);
        this.hasScaled = true;
        this.needsUpdate = true;
    }

    private void updateTranslationMatrix() {
        if (this.hasMoved) {
            this.mTranslate = Matrix4.translation(this.position);
            this.hasMoved = false;
        }
    }

    private void updateRotationMatrix() {
        if (this.hasRotated) {
            this.mRotate = Matrix4.rotation(this.rotation);
            this.hasRotated = false;
        }
    }

    private void updateScaleMatrix() {
        if (this.hasScaled) {
            this.mScale = Matrix4.scale(this.scale);
            this.hasScaled = false;
        }
    }

    private void updateWorldMatrix() {
        if (this.needsUpdate) {
            this.mWorld = Matrix4.worldMatrix(this.mTranslate, this.mRotate, this.mScale);
            this.needsUpdate = false;
        }
    }

    public void update() {
        if (this.needsUpdate) {
            this.updateTranslationMatrix();
            this.updateRotationMatrix();
            this.updateScaleMatrix();
            this.updateWorldMatrix();
        }
    }

    public float[] worldMatrix() {
        return this.mWorld.asArray();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = this == obj;
        if (!result) {
            if (obj == null || obj.getClass() != this.getClass()) {
                result = false;
            } else {
                final Transform3D other = (Transform3D) obj;
                result = this.position.equals(other.position)
                        && this.rotation.equals(other.rotation)
                        && this.scale.equals(other.scale);
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.position, this.rotation, this.scale);
    }

    @Override
    public String toString() {
        return String.format("Position: %s, Rotation: %s, Scale: %s", this.position, this.rotation, this.scale);
    }
}
