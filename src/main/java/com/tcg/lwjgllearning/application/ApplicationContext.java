package com.tcg.lwjgllearning.application;

import com.tcg.lwjgllearning.application.input.Input;

public class ApplicationContext {

    private static ApplicationContext instance = null;

    public Window window;
    public LWJGLApplication application;
    public final Input input;

    private ApplicationContext() {
        this.input = new Input();
    }

    public static ApplicationContext context() {
        if (instance == null) {
            synchronized (ApplicationContext.class) {
                if (instance == null) {
                    instance = new ApplicationContext();
                }
            }
        }
        return instance;
    }

}
