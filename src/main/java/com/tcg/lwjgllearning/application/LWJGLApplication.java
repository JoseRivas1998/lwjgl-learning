package com.tcg.lwjgllearning.application;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL;

import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

public class LWJGLApplication implements Runnable {

    private final String mainThreadName;
    private final Thread mainThread;
    private final ApplicationListener applicationListener;

    private Window window;

    public LWJGLApplication(ApplicationListener applicationListener) {
        this.applicationListener = applicationListener;
        this.mainThreadName = String.format("game-thread-%s", UUID.randomUUID().toString());
        mainThread = new Thread(this, mainThreadName);
        mainThread.start();
    }

    @Override
    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        this.window = new Window(300, 300, "Hello World", true);

        this.window.init();

        applicationListener.create();
        loop();

        applicationListener.close();
        this.window.close();
    }

    private void loop() {

        GL.createCapabilities();

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        while (!this.window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (this.window.isResized()) {
                applicationListener.resize(this.window.getWidth(), this.window.getHeight());
                this.window.setResized(false);
            }

            applicationListener.update();
            applicationListener.draw();

            this.window.update();
        }

    }

}
