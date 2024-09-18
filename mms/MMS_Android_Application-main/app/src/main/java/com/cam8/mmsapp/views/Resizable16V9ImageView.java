package com.cam8.mmsapp.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class Resizable16V9ImageView extends androidx.appcompat.widget.AppCompatImageView {

	public Resizable16V9ImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) ((float) width * 9 / 16);
		setMeasuredDimension(width, height);
	}
}