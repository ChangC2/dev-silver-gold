package com.cam8.mmsapp.alarm;

public class AlarmSettings {
    public static final int INTERVAL = 1000 * 60 * 60; // 60 mins
    public static final String ACTION_VALID_STATUS_UPDATED = "com.cam8.mmsapp.validstatuschecker";

    public static final int STATUS_VALIDATE_USAGE = 100;
    public static final int STATUS_NEW_VESION = 200;
    public static final int STATUS_NEW_SETTINGS = 300;
    public static final int STATUS_SERVER_CONN_STATUS = 400;
    public static final int STATUS_DEVICE_STATUS = 500;
    public static final int STATUS_REPORT_TANK_TIME = 600;

    public static final int STATUS_PROXY_LOGIN_LOGOUT = 700;            // This means login/logout status in custom screens
    public static final int STATUS_MAIN_LOGIN_LOGOUT = 800;             // This means login/logout status in oee dashboard

    public static final int STATUS_PROXY_PARTS_COUNT_CHANGED = 900;     // This means part counts were changed in custom screens
    public static final int STATUS_MAIN_PARTS_COUNT_CHANGED = 1000;     // This means part counts were changed in oee dashboard

    public static final int STATUS_NEW_DAILY_GOAL_TARGET = 1100;        // This means the new value of daily goal target

    public static final int STATUS_NEW_PARTID = 1200;                   // This means the new part id inputted
}
