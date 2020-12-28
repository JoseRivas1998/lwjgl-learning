package com.tcg.lwjgllearning.application.input;

import java.util.ArrayList;
import java.util.List;

public class Input {

    private final List<InputListener> inputListeners = new ArrayList<>();

    public void addInputListener(InputListener inputListener) {
        this.inputListeners.add(inputListener);
    }

    public void keyDown(int keycode) {
        this.inputListeners.forEach(inputListener -> inputListener.keyDown(keycode));
    }

    public void keyUp(int keycode) {
        this.inputListeners.forEach(inputListener -> inputListener.keyUp(keycode));
    }

}
