package com.cam8.mmsapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class LatoTextView extends androidx.appcompat.widget.AppCompatTextView {

    public LatoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoTextView(Context context) {
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