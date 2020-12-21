package com.tcg.lwjgllearning.math;

import java.util.Objects;

public class Transform2D {

    private final Vector2 position;
    private float rotation;
    private final Vector2 scale;

    protected Matrix3 mWorld;

    private boolean needsUpdate;

    public Transform2D(Vector2 position, float rotation, Vector2 scale) {
        this.position = Objects.requireNonNull(position).copy();
        this.rotation = rotation;
        this.scale = Objects.requireNonNull(scale).copy();

        this.mWorld = null;
        this.needsUpdate = true;
        this.update();
    }

    public void update() {
        if (this.needsUpdate) {
            this.mWorld = Matrix3.worldMatrix(this.position, this.rotation, this.scale);
            this.needsUpdate = false;
        }
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
        this.needsUpdate = true;
    }

    public void setRotation(float theta) {
        this.rotation = theta;
        this.needsUpdate = true;
    }

    public void setScale(Vector2 scale) {
        this.scale.set(scale);
        this.needsUpdate = true;
    }

    public void translate(Vector2 translation) {
        this.position.addInPlace(translation);
        this.needsUpdate = true;
    }

    public void rotate(float theta) {
        this.rotation = (this.rotation + theta) % MathUtils.PI2;
        this.needsUpdate = true;
    }

    public void rotateAround(Vector2 center, float theta, boolean holdOrientation) {
        this.position.subInPlace(center);
        this.position.rotateInPlace(theta);
        this.position.addInPlace(center);
        if (!holdOrientation) {
            this.rotate(theta);
        }
        this.needsUpdate = true;
    }

    public void scaleBy(Vector2 scale) {
        this.scale.scaleInPlace(scale);
        this.needsUpdate = true;
    }

    public float[] worldMatrix() {
        return this.mWorld.asArray();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = obj == this;
        if (!result) {
            if (obj == null || obj.getClass() != this.getClass()) {
                result = false;
            } else {
                final Transform2D other = (Transform2D) obj;
                result = this.position.equals(other.position)
                        && Float.compare(this.rotation, other.rotation) == 0
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
        return String.format("Position: %s, Rotation: %f, Scale: %s", this.position, this.rotation, this.scale);
    }
}
