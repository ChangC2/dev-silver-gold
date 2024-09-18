package com.cam8.mmsapp.model;

import android.graphics.Color;
import android.util.Log;

public class GanttPlotModel {
    long start;
    long length;
    int color = 0xffff0000;


    public GanttPlotModel() {}

    public GanttPlotModel(long start, long length, int color) {
        this.start = start;
        this.length = length;
        this.color = color;
    }

    public GanttPlotModel(long start, long length, String color) {
        this.start = start;
        this.length = length;

        try {
            this.color = Color.parseColor(color);
        } catch (Exception e) {}
    }

    public long getStart() { return start; }
    public void setStart(long start) { this.start = start; }

    public long getLength() { return length; }
    public void setLength(long length) { this.length = length; }

    public long getEnd() { return start + length; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public void setColor(String color) {
        try {
            this.color = Color.parseColor(color);
        } catch (Exception e) {}
    }

    public boolean isPrevSameSegment(GanttPlotModel otherSegment) {
        if (otherSegment == null)
            return false;

        if((getEnd() - otherSegment.getStart()) < 2 && color == otherSegment.getColor()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNextSameSegment(GanttPlotModel otherSegment) {
        if (otherSegment == null)
            return false;

        //Log.e("segment", String.format("%d-%d, %d-%d", start, otherSegment.getEnd(), color, otherSegment.getColor()));
        if((start - otherSegment.getEnd()) < 2 && color == otherSegment.getColor()) {
            return true;
        } else {
            return false;
        }
    }
}
