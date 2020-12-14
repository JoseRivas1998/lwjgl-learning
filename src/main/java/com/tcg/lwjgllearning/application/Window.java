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
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);

        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the glfw window");
        }

        glfwSetKeyCallback(windowHandle, (window1, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(windowHandle, true);
            }
        });

        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.resized = true;
        });

        GLFWVidMode vidMode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));

        glfwSetWindowPos(
                windowHandle,
                (vidMode.width() - this.width) / 2,
                (vidMode.height() - this.height) / 2
        );

        glfwMakeContextCurrent(windowHandle);

        if (vSync) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        clearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public boolean isResized() {
        return resized;
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(this.windowHandle, title);
        this.title = title;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.windowHandle);
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    /*
     * PRIVATE
     */
}
