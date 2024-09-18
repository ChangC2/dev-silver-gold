package com.cam8.mmsapp.utils;

import android.graphics.Color;

public class ChartColorUtils {

    public static final String[] chartColorStrings = new String[] {
            "#ff0000",
            "#46c392",
            "#ffec00",
            "#549afc",
            "#c000db",
            "#9898db",
            "#B0E0E6",
            "#6aa786",
            "#c0a0c0",
            "#808080",
            "#000000"
    };

    public static final int[] getCharColors() {
        int[] colors = new int[chartColorStrings.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Color.parseColor(chartColorStrings[i]);
        }
        return colors;
    }
}
