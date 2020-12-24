package com.tcg.lwjgllearning.utils;

import java.util.List;

public interface ListUtils {

    static int[] intListToArray(List<Integer> intList) {
        final int[] listAsArray = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            listAsArray[i] = intList.get(i);
        }
        return listAsArray;
    }

    static float[] floatListToArray(List<Float> floatList) {
        final float[] listAsArray = new float[floatList.size()];
        for (int i = 0; i < floatList.size(); i++) {
            listAsArray[i] = floatList.get(i);
        }
        return listAsArray;
    }

}
