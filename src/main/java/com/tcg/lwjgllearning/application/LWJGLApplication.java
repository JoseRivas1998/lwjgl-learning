package com.tcg.lwjgllearning.application;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public class LWJGLApplication {

    private Window window;

    public void run() {
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        this.window = new Window(300, 300, "Hello World", true);

        this.window.init();
        loop();

        this.window.close();
    }

    private void loop() {

        GL.createCapabilities();

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        while (!this.window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            this.window.update();
        }

    }

}
