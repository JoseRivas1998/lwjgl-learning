package com.tcg.lwjgllearning.graphics;

import com.tcg.lwjgllearning.application.ApplicationContext;
import com.tcg.lwjgllearning.application.input.InputListener;
import com.tcg.lwjgllearning.math.MathUtils;
import com.tcg.lwjgllearning.math.Quaternion;
import com.tcg.lwjgllearning.math.Vector3;

import java.util.Collection;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardFPSCamera extends PerspectiveCamera implements InputListener {

    private static final float DEFAULT_MOVE_SPEED = 0.1f;
    private static final float DEFAULT_TURN_SPEED = 0.015f;
    private final float moveSpeed;
    private final float turnSpeed;
    private float rotX;
    private float rotY;
    private final float rotLimitX;
    private final float rotLimitY;
    private boolean W;
    private boolean A;
    private boolean S;
    private boolean D;
    private boolean Q;
    private boolean E;
    private boolean L;
    private boolean U;
    private boolean R;
    private boolean Do;


    public KeyboardFPSCamera(Collection<ShaderProgram> programs, float moveSpeed, float turnSpeed) {
        super(programs);

        this.moveSpeed = moveSpeed;
        this.turnSpeed = turnSpeed;

        ApplicationContext.context().input.addInputListener(this);

        this.rotX = 0f;
        this.rotY = 0f;
        this.rotLimitX = MathUtils.PI / 2;
        this.rotLimitY = MathUtils.PI;
        this.setRots();

        this.W = false;
        this.A = false;
        this.S = false;
        this.D = false;
        this.Q = false;
        this.E = false;

        this.L = false;
        this.U = false;
        this.R = false;
        this.Do = false;
    }

    public KeyboardFPSCamera(Collection<ShaderProgram> programs) {
        this(programs, DEFAULT_MOVE_SPEED, DEFAULT_TURN_SPEED);
    }

    @Override
    public void keyDown(int keycode) {
        this.setToValue(keycode, true);
    }

    @Override
    public void keyUp(int keycode) {
        this.setToValue(keycode, false);
    }

    private void setToValue(int keycode, boolean value) {
        switch (keycode) {
            case GLFW_KEY_W:
                this.W = value;
                break;
            case GLFW_KEY_A:
                this.A = value;
                break;
            case GLFW_KEY_S:
                this.S = value;
                break;
            case GLFW_KEY_D:
                this.D = value;
                break;
            case GLFW_KEY_Q:
                this.Q = value;
                break;
            case GLFW_KEY_E:
                this.E = value;
                break;
            case GLFW_KEY_LEFT:
                this.L = value;
                break;
            case GLFW_KEY_UP:
                this.U = value;
                break;
            case GLFW_KEY_RIGHT:
                this.R = value;
                break;
            case GLFW_KEY_DOWN:
                this.Do = value;
                break;
            default:
                break;
        }
    }

    private void move() {
        // left/right
        float dx = 0;
        if (this.A) {
            dx -= 1;
        }
        if (this.D) {
            dx += 1;
        }

        dx *= this.moveSpeed;
        Vector3 dxVector = this.localRight.scaleOutPlace(new Vector3(dx, dx, dx));

        // up/down
        float dy = 0;
        if (this.Q) {
            dy -= 1;
        }
        if (this.E) {
            dy += 1;
        }

        dy *= this.moveSpeed;
        Vector3 dyVector = this.localUp.scaleOutPlace(new Vector3(dy, dy, dy));

        // forward/back
        float dz = 0;
        if (this.W) {
            dz += 1;
        }
        if (this.S) {
            dz -= 1;
        }

        dz *= this.moveSpeed;
        Vector3 dzVector = this.forward.scaleOutPlace(new Vector3(dz, dz, dz));

        dxVector.addInPlace(dyVector);
        dxVector.addInPlace(dzVector);
        this.translate(dxVector);
    }

    private void turn() {
        float dy = 0;
        if (this.L) {
            dy += 1;
        }
        if (this.R) {
            dy -= 1;
        }

        dy *= this.turnSpeed;

        float dx = 0;
        if (this.U) {
            dx += 1;
        }
        if (this.Do) {
            dx -= 1;
        }

        dx *= this.turnSpeed;

        if (dx != 0 || dy != 0) {
            this.rotX += dx;
            if (this.rotX > this.rotLimitX) {
                this.rotX = this.rotLimitX;
            } else if (this.rotX < -this.rotLimitX) {
                this.rotX = -this.rotLimitX;
            }

            this.rotY += dy;
            if (this.rotY > this.rotLimitY) {
                this.rotY -= 2f * this.rotLimitY;
            } else if (this.rotY < -this.rotLimitY) {
                this.rotY += 2f * this.rotLimitY;
            }

            Quaternion rot = Quaternion.ofRotation(this.rotX, Vector3.x(), true);
            rot.composeInPlace(Quaternion.ofRotation(this.rotY, Vector3.y(), true));

            this.setRotation(rot);
        }
    }

    @Override
    public void update() {
        this.move();
        this.turn();
        super.update();
    }

    @Override
    public void lookAt(Vector3 target, Vector3 up) {
        super.lookAt(target, up);
        this.setRots();
    }

    public void setRots() {
        final float sinX = -this.forward.y;
        this.rotX = -MathUtils.asin(sinX);
        final float cosX = MathUtils.sqrt(1f - sinX * sinX);
        if (Math.abs(cosX) < 0.01) {
            this.rotY = -MathUtils.asin(this.localUp.x / sinX);
            if (Float.compare(this.localUp.z, 0f) < 0) {
                if (Float.compare(-this.forward.y, 0f) < 0) {
                    this.rotY = MathUtils.PI - this.rotY;
                }
            } else {
                if (Float.compare(-this.forward.y, 0f) > 0) {
                    this.rotY = MathUtils.PI - this.rotY;
                }
            }
        } else {
            this.rotY = MathUtils.asin(-this.forward.x / cosX);
            if (Float.compare(-this.forward.z, 0f) < 0) {
                this.rotY = MathUtils.PI - this.rotY;
            }
        }
    }

}
