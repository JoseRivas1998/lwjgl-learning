package com.tcg.lwjgllearning;

import com.tcg.lwjgllearning.application.ApplicationListener;
import com.tcg.lwjgllearning.application.LWJGLApplication;

public class Main {

    public static void main(String[] args) {
        new LWJGLApplication(new ApplicationListener() {
            @Override
            public void create() {
                System.out.println("Create");
            }

            @Override
            public void update() {
                System.out.println("update");
            }

            @Override
            public void draw() {
                System.out.println("draw");
            }

            @Override
            public void resize(int width, int height) {
                System.out.printf("Resize (%d, %d)\n", width, height);
            }

            @Override
            public void dispose() {
                System.out.println("Close");
            }
        });
    }
}
