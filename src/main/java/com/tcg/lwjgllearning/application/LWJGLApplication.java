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
        this.mainThread = new Thread(this, this.mainThreadName);
        this.mainThread.start();
    }

    @Override
    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        this.window = new Window(300, 300, "Hello World", true);

        this.window.init();

        this.applicationListener.create();
        this.loop();

        this.applicationListener.close();
        this.window.close();
    }

    private void loop() {


        while (!this.window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (this.window.isResized()) {
                this.applicationListener.resize(this.window.getWidth(), this.window.getHeight());
                this.window.setResized(false);
            }

            this.applicationListener.update();
            this.applicationListener.draw();

            this.window.update();
        }

    }

}
