package com.cam8.icsapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class LatoButton extends androidx.appcompat.widget.AppCompatButton {

    public LatoButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/lato-bold.ttf");
            setTypeface(tf);
        }
    }
}