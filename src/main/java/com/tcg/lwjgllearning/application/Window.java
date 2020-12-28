package com.tcg.lwjgllearning.application;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private String title;
    private int width;
    private int height;

    private long windowHandle;
    private final boolean vSync;
    private boolean resized;

    /*
     * CONSTRUCTORS
     */

    public Window(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
        this.resized = false;
    }

    /*
     * PUBLIC
     */

    public void clearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    public void close() {
        glfwFreeCallbacks(this.windowHandle);
        glfwDestroyWindow(this.windowHandle);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public int getHeight() {
        return this.height;
    }

    public String getTitle() {
        return this.title;
    }

    public int getWidth() {
        return this.width;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        this.windowHandle = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (this.windowHandle == NULL) {
            throw new RuntimeException("Failed to create the glfw window");
        }

        glfwSetKeyCallback(this.windowHandle, (window1, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(this.windowHandle, true);
            }
            final ApplicationContext context = ApplicationContext.context();
            if (action == GLFW_PRESS) {
                context.input.keyDown(key);
            }
            if (action == GLFW_RELEASE) {
                context.input.keyUp(key);
            }
        });

        glfwSetFramebufferSizeCallback(this.windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.resized = true;
        });

        GLFWVidMode vidMode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));

        glfwSetWindowPos(
                this.windowHandle,
                (vidMode.width() - this.width) / 2,
                (vidMode.height() - this.height) / 2
        );

        glfwMakeContextCurrent(this.windowHandle);

        if (this.vSync) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(this.windowHandle);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CCW);
        glCullFace(GL_BACK);

        this.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public boolean isResized() {
        return this.resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(this.windowHandle, title);
        this.title = title;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.windowHandle);
    }

    public void update() {
        glfwSwapBuffers(this.windowHandle);
        glfwPollEvents();
    }

    /*
     * PRIVATE
     */
}
