package com.tcg.lwjgllearning.application;

public interface ApplicationListener {

    void create();
    void update();
    void draw();
    void resize(int width, int height);
    void close();


}
