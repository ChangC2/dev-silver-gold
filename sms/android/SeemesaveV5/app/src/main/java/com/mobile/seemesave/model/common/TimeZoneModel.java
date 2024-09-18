package com.mobile.seemesave.model.common;

public class TimeZoneModel {
    private String time_zone = "";
    private int time_offset = 0;

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public int getTime_offset() {
        return time_offset;
    }

    public void setTime_offset(int time_offset) {
        this.time_offset = time_offset;
    }
    @Override
    public String toString() {
        return time_zone;
    }
}
