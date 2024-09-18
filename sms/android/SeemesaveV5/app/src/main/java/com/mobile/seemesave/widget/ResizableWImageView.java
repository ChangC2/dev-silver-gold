package com.mobile.seemesave.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class ResizableWImageView extends AppCompatImageView {

    public ResizableWImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int height = MeasureSpec.getSize(widthMeasureSpec);
            int width = (int) Math.ceil((float) height
                    * (float) d.getIntrinsicWidth()
                    / (float) d.getIntrinsicHeight());
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}