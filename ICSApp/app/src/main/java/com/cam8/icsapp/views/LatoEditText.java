package com.cam8.icsapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class LatoEditText extends androidx.appcompat.widget.AppCompatEditText {

    public LatoEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoEditText(Context context) {
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