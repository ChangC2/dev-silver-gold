package com.cam8.icsapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class LatoLightTextView extends androidx.appcompat.widget.AppCompatTextView {

    public LatoLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoLightTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/lato-light.ttf");
            setTypeface(tf);
        }
    }

}