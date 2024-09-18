package com.cam8.mmsapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.cam8.mmsapp.AppSettings;
import com.cam8.mmsapp.R;
import com.cam8.mmsapp.model.GanttPlotModel;
import com.cam8.mmsapp.utils.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ThinGanttChart extends View {

    AppSettings appSettings;

    // Text Paint
    private Paint textPaintGanttTimeTitle;
    private float textHeight;

    // Axis Paint
    private Paint axisPaint;

    ArrayList<GanttPlotModel> ganttPlotModels;
    HashMap<Integer, Paint> mapColorPaint = new HashMap<>();

    String startTimeText = "00:00:00";
    String endTimeText = "23:59:59";

    float fontSizeRulerInSP = 15;
    float fontSizeValueInSP = 20;
    int hPaddingInDP = 10;
    int vPaddingInDP = 4;

    // Daily Goal Target
    private Paint textPaintDGTRed;
    private Paint textPaintDGTOrange;
    private Paint textPaintDGTGreen;
    private Paint textPaintDGTVal;

    public ThinGanttChart(Context context) {
        super(context);
        init(context);
    }

    public ThinGanttChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ThinGanttChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        appSettings = new AppSettings(context);

        //float fontSizeRuler = spToPx(fontSizeRulerInSP, context);
        //float fontSizeValue = spToPx(fontSizeValueInSP, context);
        float fontSizeRuler = getResources().getDimension(R.dimen.font_9);
        float fontSizeValue = getResources().getDimension(R.dimen.font_7);

        // Paints for the Gantt Chart
        textPaintGanttTimeTitle = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintGanttTimeTitle.setColor(Color.WHITE);
        textPaintGanttTimeTitle.setTextSize(fontSizeRuler);
        textHeight = (int) textPaintGanttTimeTitle.measureText("08");

        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setColor(Color.argb(30, 255, 255, 255));

        // Paints for the Daily Goal Target
        textPaintDGTRed = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintDGTRed.setColor(Color.RED);
        textPaintDGTRed.setTextSize(fontSizeRuler);

        textPaintDGTOrange = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintDGTOrange.setColor(Color.rgb(0xff, 0x66, 0x00));
        textPaintDGTOrange.setTextSize(fontSizeRuler);

        textPaintDGTGreen = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintDGTGreen.setColor(Color.GREEN);
        textPaintDGTGreen.setTextSize(fontSizeRuler);

        textPaintDGTVal = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintDGTVal.setColor(Color.WHITE);
        textPaintDGTVal.setTextSize(fontSizeValue);
        textPaintDGTVal.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    private float dpToPx(float dp) {
        //return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return getResources().getDisplayMetrics().density * dp;
    }

    public static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // The compass is a circle that fills as much space as possible.
        // Set the measured dimensions by figuring out the shortest boundary,
        // height or width.
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);

        int d = Math.min(measuredWidth, measuredHeight);

        setMeasuredDimension(measuredWidth, measuredHeight);
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

    public void setGanttData(ArrayList<GanttPlotModel> models) {
        Log.e("ganttCurT", (System.currentTimeMillis() / 1000) + "");
        for (GanttPlotModel item : models) {
            Log.e("ganttData", String.format("%d-%d-%s", item.getStart(), item.getEnd(), item.getColor()));
        }
        ganttPlotModels = models;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        boolean showDailyGoalTarget = appSettings.getChartOption() == 2;
        if (showDailyGoalTarget) {
            // Show Daily Goal Target
            float dgtValue = appSettings.getDGTValue();
            float dgtMaxValue = appSettings.getDGTMaxValue();

            float width = getMeasuredWidth();
            float height = getMeasuredHeight();

            float pxPerDP = dpToPx(1);
            float startX = pxPerDP * hPaddingInDP;
            float startY = pxPerDP * 5;
            float endX = width - pxPerDP * hPaddingInDP;
            float endY = height - pxPerDP * vPaddingInDP;

            int widthStartText = (int) textPaintDGTRed.measureText("0%");
            int widthEndText = (int) textPaintDGTGreen.measureText("150%");

            // Draw 0% text
            canvas.drawText("0%", startX, startY + textHeight / 2, textPaintDGTRed);

            // Draw 100% text
            canvas.drawText("150%", endX - widthEndText, startY + textHeight / 2, textPaintDGTGreen);

            // Draw 50%
            canvas.drawText("50%", (endX - startX) / 3 - widthEndText / 2, startY + textHeight / 2, textPaintDGTOrange);

            // Draw 100%
            canvas.drawText("100%", (endX - startX) * 2 / 3 - widthEndText / 2, startY + textHeight / 2, textPaintDGTGreen);

            // Show Axis bar
            canvas.drawRect(startX, startY + textHeight, endX, endY, axisPaint);

            float scaleX = (endX - startX)  / dgtMaxValue / 1.5f;

            float barStartX = startX;
            float barEndX = barStartX + (scaleX * dgtValue);

            float percentage = dgtValue * 100.0f / dgtMaxValue;
            String strDGTValue = "";
            if (appSettings.getDGTDispMode() == 0) {
                strDGTValue = String.format("%.1f %s", dgtValue, appSettings.getDGTUnit()).trim();
            } else {
                strDGTValue = String.format("%d %%", (int) percentage);
            }
            widthEndText = (int) textPaintDGTVal.measureText(strDGTValue);

            float yPOSTextUnit = endY - textHeight * 0.6f;

            if (percentage < 50) {
                canvas.drawRect(barStartX, startY + textHeight, barEndX, endY, textPaintDGTRed);
                canvas.drawText(strDGTValue, barEndX / 2, yPOSTextUnit, textPaintDGTVal);
            } else if(percentage < 100) {
                canvas.drawRect(barStartX, startY + textHeight, barEndX, endY, textPaintDGTOrange);
                canvas.drawText(strDGTValue, barEndX / 2 - widthEndText / 2, yPOSTextUnit, textPaintDGTVal);
            } else {
                if ((barEndX / 2) > endX) {
                    barEndX = endX;
                }
                canvas.drawRect(barStartX, startY + textHeight, barEndX, endY, textPaintDGTGreen);
                canvas.drawText(strDGTValue, barEndX - widthEndText, yPOSTextUnit, textPaintDGTVal);
            }
        } else {
            // Show Gantt Chart
            // If there is no Gantt Data, then don't draw it.
            if (ganttPlotModels == null || ganttPlotModels.isEmpty()) {
                return;
            }

            float width = getMeasuredWidth();
            float height = getMeasuredHeight();

            float pxPerDP = dpToPx(1);
            float startX = pxPerDP * hPaddingInDP;
            float startY = pxPerDP * 5;
            float endX = width - pxPerDP * hPaddingInDP;
            float endY = height - pxPerDP * vPaddingInDP;

            int widthStartText = (int) textPaintGanttTimeTitle.measureText(startTimeText);
            int widthEndText = (int) textPaintGanttTimeTitle.measureText(endTimeText);


            long startSecondsForGantt;
            long totalSecondsForGantt;
            boolean isGant24Hours = appSettings.getChartOption() == 0;

            Calendar calStartTimeToday = Calendar.getInstance();
            calStartTimeToday.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
            calStartTimeToday.clear(Calendar.MINUTE);
            calStartTimeToday.clear(Calendar.SECOND);
            calStartTimeToday.clear(Calendar.MILLISECOND);

            startSecondsForGantt = calStartTimeToday.getTimeInMillis() / 1000;
            if (isGant24Hours) {
                totalSecondsForGantt = 86400;

                endTimeText = "23:59:59";
            } else {
                Calendar currnetTime = Calendar.getInstance();
                totalSecondsForGantt = (currnetTime.getTimeInMillis() - calStartTimeToday.getTimeInMillis()) / 1000;

                endTimeText = DateUtil.toStringFormat_23(currnetTime.getTime());
            }

            // Draw start time text
            canvas.drawText(startTimeText, startX, startY + textHeight / 2, textPaintGanttTimeTitle);

            // Draw end time text
            canvas.drawText(endTimeText, endX - widthEndText, startY + textHeight / 2, textPaintGanttTimeTitle);

            // Show Axis bar
            canvas.drawRect(startX, startY + textHeight, endX, endY, axisPaint);

            float scaleX = (endX - startX) / totalSecondsForGantt;

            for (int i = 0; i < ganttPlotModels.size(); i++) {
                GanttPlotModel model = ganttPlotModels.get(i);

                if (model.getLength() <= 0) {
                    continue;
                }

                Paint segmentPaint = mapColorPaint.get(model.getColor());
                if (segmentPaint == null) {
                    segmentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    segmentPaint.setColor(model.getColor());
                    segmentPaint.setStrokeWidth(10);

                    mapColorPaint.put(model.getColor(), segmentPaint);
                }

                //String segmentStartDate = DateUtil.toStringFormat_12(new Date(model.getStart() * 1000));

                long startDeltaMilis = model.getStart() - startSecondsForGantt;


                float rectStartX = startX + startDeltaMilis * scaleX;
                float rectEndX = rectStartX + model.getLength() * scaleX;

                // Exception Check for exceeding the Time range
                if (rectEndX <= endX ) {
                    canvas.drawRect(rectStartX, startY + textHeight, rectEndX, endY, segmentPaint);
                }
            }
        }
    }
}
