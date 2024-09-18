package com.cam8.mmsapp.views;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PercentageGauge extends View {

    // Axis Paint
    private Paint drawPaint;
    private float percentage = 0f;
    private float ARC_ANGLE = 150f;

    public PercentageGauge(Context context) {
        super(context);
        init(context);
    }

    public PercentageGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PercentageGauge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setColor(Color.argb(240, 255, 255, 255));
        drawPaint.setStrokeWidth(15);
    }

    private float dpToPx(float dp) {
        return getResources().getDisplayMetrics().density * dp;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage, long duration, long startDelay) {

        if (this.percentage == percentage)
            return;

        ValueAnimator va = ValueAnimator.ofObject(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return startValue + fraction * (endValue-startValue);
            }
        }, Float.valueOf(getPercentage()), Float.valueOf(percentage));

        va.setDuration(duration);
        va.setStartDelay(startDelay);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                if (value != null)
                    setPercentage(value);
            }
        });
        va.start();
    }

    public void setPercentage(float percentage) {
        if (percentage < 0)
            throw new IllegalArgumentException("Non-positive value specified as a speed.");
        if (percentage > 100)
            percentage = 100;
        this.percentage = percentage;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // The compass is a circle that fills as much space as possible.
        // Set the measured dimensions by figuring out the shortest boundary,
        // height or width.

        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);

        //int measuredWidth = getMeasuredWidth();
        //int measuredHeight = getMeasuredHeight();

        int d = Math.min(measuredWidth, measuredHeight);

        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        int result = 0;

        // Decode the measurement specifications.
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            result = 200;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float width = getMeasuredWidth();
        float height = getMeasuredHeight();

        float centerX = width / 2;
        float centerY = height / 2;

        float R = Math.min(centerX, centerY);

        // 0 % ~ 100 % => -ARC_ANGLE ~ ARC_ANGLE
        // x2 = a2 + (b2 - a2) * (x1 - a1) / (b1 - a1)
        float degreeToRotate = -ARC_ANGLE + 2 * ARC_ANGLE * percentage / 100f;

        canvas.save();
        canvas.rotate(degreeToRotate, centerX, centerY);
        canvas.drawLine(centerX, R * 0.65f, centerX, R * 0.45f, drawPaint);
        canvas.restore();
    }
}
