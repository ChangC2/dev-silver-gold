package com.cam8.mmsapp.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Xian on 19-03-2020.
 */

public class TemperatureDataModel implements Serializable {
    public static final String TABLE_TEMP_DATA_NAME = "tbl_tempData";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATEDAT = "created_at";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_PARTID = "part_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";

    public static final String COLUMN_TTIME1 = "ttime1";
    public static final String COLUMN_TTEMP1 = "ttemp1";
    public static final String COLUMN_TTIME2 = "ttime2";
    public static final String COLUMN_TTEMP2 = "ttemp2";
    public static final String COLUMN_TTIME3 = "ttime3";
    public static final String COLUMN_TTEMP3 = "ttemp3";
    public static final String COLUMN_TTIME4 = "ttime4";
    public static final String COLUMN_TTEMP4 = "ttemp4";
    public static final String COLUMN_TTIME5 = "ttime5";
    public static final String COLUMN_TTEMP5 = "ttemp5";
    public static final String COLUMN_TTIME6 = "ttime6";
    public static final String COLUMN_TTEMP6 = "ttemp6";
    public static final String COLUMN_TTIME7 = "ttime7";
    public static final String COLUMN_TTEMP7 = "ttemp7";
    public static final String COLUMN_TTIME8 = "ttime8";
    public static final String COLUMN_TTEMP8 = "ttemp8";
    public static final String COLUMN_OTHER1 = "other1";
    public static final String COLUMN_OTHER2 = "other2";

    private int id;
    private String createdAt;
    private long timeStamp;
    private String partId;
    private String date;
    private String time;

    private long ttime1;
    private float ttemp1;

    private long ttime2;
    private float ttemp2;

    private long ttime3;
    private float ttemp3;

    private long ttime4;
    private float ttemp4;

    private long ttime5;
    private float ttemp5;

    private long ttime6;
    private float ttemp6;

    private long ttime7;
    private float ttemp7;

    private long ttime8;
    private float ttemp8;

    private long tOven;
    private String other2;

    // Create table SQL query
    public static final String CREATE_TEMP_DATA_TABLE =
            "CREATE TABLE " + TABLE_TEMP_DATA_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CREATEDAT + " TEXT,"
                    + COLUMN_TIMESTAMP + " INTEGER,"
                    + COLUMN_PARTID + " TEXT,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_TIME + " TEXT,"
                    + COLUMN_TTIME1 + " INTEGER,"
                    + COLUMN_TTEMP1 + " REAL,"
                    + COLUMN_TTIME2 + " INTEGER,"
                    + COLUMN_TTEMP2 + " REAL,"
                    + COLUMN_TTIME3 + " INTEGER,"
                    + COLUMN_TTEMP3 + " REAL,"
                    + COLUMN_TTIME4 + " INTEGER,"
                    + COLUMN_TTEMP4 + " REAL,"
                    + COLUMN_TTIME5 + " INTEGER,"
                    + COLUMN_TTEMP5 + " REAL,"
                    + COLUMN_TTIME6 + " INTEGER,"
                    + COLUMN_TTEMP6 + " REAL,"
                    + COLUMN_TTIME7 + " INTEGER,"
                    + COLUMN_TTEMP7 + " REAL,"
                    + COLUMN_TTIME8 + " INTEGER,"
                    + COLUMN_TTEMP8 + " REAL,"
                    + COLUMN_OTHER1 + " TEXT,"
                    + COLUMN_OTHER2 + " TEXT"
                    + ")";

    public TemperatureDataModel() {
    }

    public TemperatureDataModel(int id, String createdAt, long timeStamp, String partId, String date, String time,
                                long time1, float temp1,  long time2, float temp2,  long time3, float temp3,  long time4, float temp4,
                                long time5, float temp5,  long time6, float temp6,  long time7, float temp7,  long time8, float temp8,
                                long tOven, String other2) {
        this.id = id;
        this.createdAt = createdAt;
        this.timeStamp = timeStamp;
        this.partId = partId;
        this.date = date;
        this.time = time;

        this.ttime1 = time1;
        this.ttemp1 = temp1;
        this.ttime2 = time2;
        this.ttemp2 = temp2;
        this.ttime3 = time3;
        this.ttemp3 = temp3;
        this.ttime4 = time4;
        this.ttemp4 = temp4;
        this.ttime5 = time5;
        this.ttemp5 = temp5;
        this.ttime6 = time6;
        this.ttemp6 = temp6;
        this.ttime7 = time7;
        this.ttemp7 = temp7;
        this.ttime8 = time8;
        this.ttemp8 = temp8;

        this.tOven = tOven;
        this.other2 = other2;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        if (TextUtils.isEmpty(createdAt)) {
            return "0";
        } else {
            return createdAt;
        }
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getTimeStamp() { return timeStamp; }
    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }

    public String getPartId() {
        return partId;
    }
    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public long getTtime1() { return ttime1; }
    public void setTtime1(long value) { this.ttime1 = value; }

    public float getTtemp1() { return ttemp1; }
    public void setTtemp1(float value) { this.ttemp1 = value; }

    public long getTtime2() { return ttime2; }
    public void setTtime2(long value) { this.ttime2 = value; }

    public float getTtemp2() { return ttemp2; }
    public void setTtemp2(float value) { this.ttemp2 = value; }

    public long getTtime3() { return ttime3; }
    public void setTtime3(long value) { this.ttime3 = value; }

    public float getTtemp3() { return ttemp3; }
    public void setTtemp3(float value) { this.ttemp3 = value; }

    public long getTtime4() { return ttime4; }
    public void setTtime4(long value) { this.ttime4 = value; }

    public float getTtemp4() { return ttemp4; }
    public void setTtemp4(float value) { this.ttemp4 = value; }

    public long getTtime5() { return ttime5; }
    public void setTtime5(long value) { this.ttime5 = value; }

    public float getTtemp5() { return ttemp5; }
    public void setTtemp5(float value) { this.ttemp5 = value; }

    public long getTtime6() { return ttime6; }
    public void setTtime6(long value) { this.ttime6 = value; }

    public float getTtemp6() { return ttemp6; }
    public void setTtemp6(float value) { this.ttemp6 = value; }

    public long getTtime7() { return ttime7; }
    public void setTtime7(long value) { this.ttime7 = value; }

    public float getTtemp7() { return ttemp7; }
    public void setTtemp7(float value) { this.ttemp7 = value; }

    public long getTtime8() { return ttime8; }
    public void setTtime8(long value) { this.ttime8 = value; }

    public float getTtemp8() { return ttemp8; }
    public void setTtemp8(float value) { this.ttemp8 = value; }

    public long gettOven() { return tOven; }
    public void settOven(long other1) { this.tOven = other1; }

    public String getOther2() { return other2; }
    public void setOther2(String other2) { this.other2 = other2; }
}
