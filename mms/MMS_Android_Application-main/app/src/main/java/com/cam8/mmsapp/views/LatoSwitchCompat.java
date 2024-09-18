package com.cam8.mmsapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class LatoSwitchCompat extends androidx.appcompat.widget.SwitchCompat {

    public LatoSwitchCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoSwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoSwitchCompat(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/lato.ttf");
            setTypeface(tf);
        }
    }

}