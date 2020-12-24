package com.tcg.lwjgllearning.application;

import com.tcg.lwjgllearning.utils.Disposable;

public interface ApplicationListener extends Disposable {

    void create();
    void update();
    void draw();
    void resize(int width, int height);


}
