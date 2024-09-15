package com.cam8.mmsapp.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Xian on 19-03-2020.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    //private static final int DATABASE_VERSION = 5;    // 2022-07-22
    //private static final int DATABASE_VERSION = 6;    // 2022-07-29 02:57
    private static final int DATABASE_VERSION = 7;      // 2023-10-11 04:26

    // Database Name
    private static final String DATABASE_NAME = "mms_db_";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create Gantt data table
        sqLiteDatabase.execSQL(GanttDataModel.CREATE_GANTT_DATA_TABLE);

        // Create Temp date table
        sqLiteDatabase.execSQL(TemperatureDataModel.CREATE_TEMP_DATA_TABLE);

        // Create Temp date table
        sqLiteDatabase.execSQL(ShiftDataModel.CREATE_SHIFT_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // ---- Original way of removing original data and create new
        /*// Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GANTT_DATA_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);*/
        // ----------------------------------------------------------

        // Newly Add new fields
        if (!existsColumnInTable(sqLiteDatabase, GanttDataModel.TABLE_GANTT_DATA_NAME, GanttDataModel.COLUMN_JOBID)) {
            sqLiteDatabase.execSQL("ALTER TABLE " + GanttDataModel.TABLE_GANTT_DATA_NAME + " ADD COLUMN " + GanttDataModel.COLUMN_JOBID + " TEXT");
        }

        if (!existsColumnInTable(sqLiteDatabase, GanttDataModel.TABLE_GANTT_DATA_NAME, GanttDataModel.COLUMN_BATT)) {
            sqLiteDatabase.execSQL("ALTER TABLE " + GanttDataModel.TABLE_GANTT_DATA_NAME + " ADD COLUMN " + GanttDataModel.COLUMN_BATT + " INTEGER DEFAULT 0");
        }

        if (!existsColumnInTable(sqLiteDatabase, GanttDataModel.TABLE_GANTT_DATA_NAME, GanttDataModel.COLUMN_OTHER)) {
            sqLiteDatabase.execSQL("ALTER TABLE " + GanttDataModel.TABLE_GANTT_DATA_NAME + " ADD COLUMN " + GanttDataModel.COLUMN_OTHER + " TEXT");
        }

        // if (oldVersion < 4) {}
        if (!isTableExists(sqLiteDatabase, TemperatureDataModel.TABLE_TEMP_DATA_NAME)) {

            // Create Temp date table
            sqLiteDatabase.execSQL(TemperatureDataModel.CREATE_TEMP_DATA_TABLE);
        }

        // if (oldVersion < 5) {}
        if (!isTableExists(sqLiteDatabase, ShiftDataModel.TABLE_SHIFT_DATA_NAME)) {

            // Create Shift date table
            sqLiteDatabase.execSQL(ShiftDataModel.CREATE_SHIFT_DATA_TABLE);
        }

        // if (oldVersion < 6) {}  // Add new Column userid
        if (!existsColumnInTable(sqLiteDatabase, ShiftDataModel.TABLE_SHIFT_DATA_NAME, ShiftDataModel.COLUMN_JOBSEQNO)) {
            sqLiteDatabase.execSQL("ALTER TABLE " + ShiftDataModel.TABLE_SHIFT_DATA_NAME + " ADD COLUMN " + ShiftDataModel.COLUMN_JOBSEQNO + " TEXT");
        }
        if (!existsColumnInTable(sqLiteDatabase, ShiftDataModel.TABLE_SHIFT_DATA_NAME, ShiftDataModel.COLUMN_USERID)) {
            sqLiteDatabase.execSQL("ALTER TABLE " + ShiftDataModel.TABLE_SHIFT_DATA_NAME + " ADD COLUMN " + ShiftDataModel.COLUMN_USERID + " TEXT");
        }
        if (!existsColumnInTable(sqLiteDatabase, ShiftDataModel.TABLE_SHIFT_DATA_NAME, ShiftDataModel.COLUMN_REWORK)) {
            sqLiteDatabase.execSQL("ALTER TABLE " + ShiftDataModel.TABLE_SHIFT_DATA_NAME + " ADD COLUMN " + ShiftDataModel.COLUMN_REWORK + " TEXT");
        }
        if (!existsColumnInTable(sqLiteDatabase, ShiftDataModel.TABLE_SHIFT_DATA_NAME, ShiftDataModel.COLUMN_SETUP)) {
            sqLiteDatabase.execSQL("ALTER TABLE " + ShiftDataModel.TABLE_SHIFT_DATA_NAME + " ADD COLUMN " + ShiftDataModel.COLUMN_SETUP + " TEXT");
        }

        // if (oldVersion < 7) {}  // Add new Column partids in shift table
        if (!existsColumnInTable(sqLiteDatabase, ShiftDataModel.TABLE_SHIFT_DATA_NAME, ShiftDataModel.COLUMN_PARTIDS)) {
            sqLiteDatabase.execSQL("ALTER TABLE " + ShiftDataModel.TABLE_SHIFT_DATA_NAME + " ADD COLUMN " + ShiftDataModel.COLUMN_PARTIDS + " TEXT");
        }
        // ------------------------------------
    }

    public boolean isTableExists(SQLiteDatabase mDatabase, String tableName) {

        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'";
        try (Cursor cursor = mDatabase.rawQuery(query, null)) {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private boolean existsColumnInTable(SQLiteDatabase inDatabase, String inTable, String columnToCheck) {
        Cursor cursor = null;
        try {
            // Query 1 row
            cursor = inDatabase.rawQuery("SELECT * FROM " + inTable + " LIMIT 0", null);

            // getColumnIndex() gives us the index (0 to ...) of the column - otherwise we get a -1
            if (cursor.getColumnIndex(columnToCheck) != -1)
                return true;
            else
                return false;

        } catch (Exception Exp) {
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    //*** Gantt Data ------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------
    public long insertGanttData(String createdAt, String machineId, String operator, String status, String color,
                                long start, long end, String timeStamp, int timeStampMs, String jobId, int battLevel, String other) {

        //Log.e("report", String.format("%s, %s, %s, %s, %s, %d, %d", createdAt, machineId, operator, status, timeStamp, start, end));
        if (start == 0 || end == 0) {
            // Invalid Records
            return 1;
        }

        try {
            // get writable database as we want to write data
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(GanttDataModel.COLUMN_CREATEDAT, createdAt);
            values.put(GanttDataModel.COLUMN_MACHINEID, machineId);
            values.put(GanttDataModel.COLUMN_OPERATOR, operator);
            values.put(GanttDataModel.COLUMN_STATUS, status);
            values.put(GanttDataModel.COLUMN_COLOR, color);
            values.put(GanttDataModel.COLUMN_START, start);
            values.put(GanttDataModel.COLUMN_END, end);
            values.put(GanttDataModel.COLUMN_TIMESTAMP, timeStamp);
            values.put(GanttDataModel.COLUMN_TIMESTAMPMS, timeStampMs);
            values.put(GanttDataModel.COLUMN_JOBID, jobId);
            values.put(GanttDataModel.COLUMN_BATT, battLevel);
            values.put(GanttDataModel.COLUMN_OTHER, other);

            // insert row
            long id = db.insert(GanttDataModel.TABLE_GANTT_DATA_NAME, null, values);

            // close db connection
            db.close();

            // return newly inserted row id
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @SuppressLint("Range")
    public List<GanttDataModel> getAllGanttData() {
        List<GanttDataModel> sugarList = new ArrayList<>();

        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + GanttDataModel.TABLE_GANTT_DATA_NAME;

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        GanttDataModel ganttObj = new GanttDataModel();
                        ganttObj.setId(cursor.getInt(cursor.getColumnIndex(GanttDataModel.COLUMN_ID)));
                        ganttObj.setCreatedAt(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_CREATEDAT)));
                        ganttObj.setMachineId(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_MACHINEID)));
                        ganttObj.setOperator(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_OPERATOR)));
                        ganttObj.setStatus(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_STATUS)));
                        ganttObj.setColor(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_COLOR)));
                        ganttObj.setStart(cursor.getLong(cursor.getColumnIndex(GanttDataModel.COLUMN_START)));
                        ganttObj.setEnd(cursor.getLong(cursor.getColumnIndex(GanttDataModel.COLUMN_END)));
                        ganttObj.setTimeStamp(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_TIMESTAMP)));
                        ganttObj.setTimeStampMs(cursor.getInt(cursor.getColumnIndex(GanttDataModel.COLUMN_TIMESTAMPMS)));
                        ganttObj.setJobId(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_JOBID)));
                        ganttObj.setBattLev(cursor.getInt(cursor.getColumnIndex(GanttDataModel.COLUMN_BATT)));
                        ganttObj.setOther(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_OTHER)));

                        sugarList.add(ganttObj);
                    } while (cursor.moveToNext());
                }

                cursor.close();
            }

            // close db connection
            db.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return notes list
        return sugarList;
    }

    @SuppressLint("Range")
    public List<GanttDataModel> getTodayGanttData() {
        List<GanttDataModel> sugarList = new ArrayList<>();

        Calendar calStartTimeToday = Calendar.getInstance();
        calStartTimeToday.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calStartTimeToday.clear(Calendar.MINUTE);
        calStartTimeToday.clear(Calendar.SECOND);
        calStartTimeToday.clear(Calendar.MILLISECOND);
        long timeMidnight = calStartTimeToday.getTimeInMillis();

        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + GanttDataModel.TABLE_GANTT_DATA_NAME + " WHERE start >= " + timeMidnight;

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        GanttDataModel ganttObj = new GanttDataModel();
                        ganttObj.setId(cursor.getInt(cursor.getColumnIndex(GanttDataModel.COLUMN_ID)));
                        ganttObj.setCreatedAt(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_CREATEDAT)));
                        ganttObj.setMachineId(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_MACHINEID)));
                        ganttObj.setOperator(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_OPERATOR)));
                        ganttObj.setStatus(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_STATUS)));
                        ganttObj.setColor(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_COLOR)));
                        ganttObj.setStart(cursor.getLong(cursor.getColumnIndex(GanttDataModel.COLUMN_START)));
                        ganttObj.setEnd(cursor.getLong(cursor.getColumnIndex(GanttDataModel.COLUMN_END)));
                        ganttObj.setTimeStamp(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_TIMESTAMP)));
                        ganttObj.setTimeStampMs(cursor.getInt(cursor.getColumnIndex(GanttDataModel.COLUMN_TIMESTAMPMS)));
                        ganttObj.setJobId(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_JOBID)));
                        ganttObj.setBattLev(cursor.getInt(cursor.getColumnIndex(GanttDataModel.COLUMN_BATT)));
                        ganttObj.setOther(cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_OTHER)));

                        sugarList.add(ganttObj);
                    } while (cursor.moveToNext());
                }

                cursor.close();
            }

            // close db connection
            db.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return notes list
        return sugarList;
    }

    public void removeAllGanttData() {

        try {
            // Remove All Gantt
            String deleteQuery = "DELETE FROM " + GanttDataModel.TABLE_GANTT_DATA_NAME;

            Log.e("TAG", deleteQuery);
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(deleteQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GanttDataModel getGanttData(long id) {

        try {
            // get readable database as we are not inserting anything
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(GanttDataModel.TABLE_GANTT_DATA_NAME,
                    new String[]{
                            GanttDataModel.COLUMN_ID, GanttDataModel.COLUMN_CREATEDAT,
                            GanttDataModel.COLUMN_MACHINEID, GanttDataModel.COLUMN_OPERATOR,
                            GanttDataModel.COLUMN_STATUS, GanttDataModel.COLUMN_START,
                            GanttDataModel.COLUMN_END, GanttDataModel.COLUMN_TIMESTAMP,
                            GanttDataModel.COLUMN_TIMESTAMPMS, GanttDataModel.COLUMN_JOBID,
                            GanttDataModel.COLUMN_BATT, GanttDataModel.COLUMN_OTHER},
                    GanttDataModel.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            // prepare note object
            @SuppressLint("Range") GanttDataModel sugarobj = new GanttDataModel(
                    cursor.getInt(cursor.getColumnIndex(GanttDataModel.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_CREATEDAT)),
                    cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_MACHINEID)),
                    cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_OPERATOR)),
                    cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_STATUS)),
                    cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_COLOR)),
                    cursor.getLong(cursor.getColumnIndex(GanttDataModel.COLUMN_START)),
                    cursor.getLong(cursor.getColumnIndex(GanttDataModel.COLUMN_END)),
                    cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_TIMESTAMP)),
                    cursor.getInt(cursor.getColumnIndex(GanttDataModel.COLUMN_TIMESTAMPMS)),
                    cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_JOBID)),
                    cursor.getInt(cursor.getColumnIndex(GanttDataModel.COLUMN_BATT)),
                    cursor.getString(cursor.getColumnIndex(GanttDataModel.COLUMN_OTHER)));

            // close the db connection
            cursor.close();

            return sugarobj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getGanttCount() {
        String countQuery = "SELECT  * FROM " + GanttDataModel.TABLE_GANTT_DATA_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public int updateGanttData(GanttDataModel ganntData) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(GanttDataModel.COLUMN_CREATEDAT, ganntData.getCreatedAt());
            values.put(GanttDataModel.COLUMN_MACHINEID, ganntData.getMachineId());
            values.put(GanttDataModel.COLUMN_OPERATOR, ganntData.getOperator());
            values.put(GanttDataModel.COLUMN_STATUS, ganntData.getStatus());
            values.put(GanttDataModel.COLUMN_COLOR, ganntData.getColor());
            values.put(GanttDataModel.COLUMN_START, ganntData.getStart());
            values.put(GanttDataModel.COLUMN_END, ganntData.getEnd());
            values.put(GanttDataModel.COLUMN_TIMESTAMP, ganntData.getTimeStamp());
            values.put(GanttDataModel.COLUMN_TIMESTAMPMS, ganntData.getTimeStampMs());
            values.put(GanttDataModel.COLUMN_JOBID, ganntData.getJobId());
            values.put(GanttDataModel.COLUMN_BATT, ganntData.getBattLev());
            values.put(GanttDataModel.COLUMN_OTHER, ganntData.getOther());
            // updating row
            return db.update(GanttDataModel.TABLE_GANTT_DATA_NAME, values, GanttDataModel.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(ganntData.getId())});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteGantData(GanttDataModel ganttData) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(GanttDataModel.TABLE_GANTT_DATA_NAME, GanttDataModel.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(ganttData.getId())});
            db.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //*** Temperature Data ------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------------
    public long insertTempData(String createdAt, long timeStamp, String partId, String date, String time,
                               long time1, float temp1,  long time2, float temp2,  long time3, float temp3,  long time4, float temp4,
                               long time5, float temp5,  long time6, float temp6,  long time7, float temp7,  long time8, float temp8,
                               long timeOven, String other2) {

        try {
            // get writable database as we want to write data
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(TemperatureDataModel.COLUMN_CREATEDAT, createdAt);
            values.put(TemperatureDataModel.COLUMN_TIMESTAMP, timeStamp);
            values.put(TemperatureDataModel.COLUMN_PARTID, partId);
            values.put(TemperatureDataModel.COLUMN_DATE, date);
            values.put(TemperatureDataModel.COLUMN_TIME, time);

            values.put(TemperatureDataModel.COLUMN_TTIME1, time1);
            values.put(TemperatureDataModel.COLUMN_TTEMP1, temp1);
            values.put(TemperatureDataModel.COLUMN_TTIME2, time2);
            values.put(TemperatureDataModel.COLUMN_TTEMP2, temp2);
            values.put(TemperatureDataModel.COLUMN_TTIME3, time3);
            values.put(TemperatureDataModel.COLUMN_TTEMP3, temp3);
            values.put(TemperatureDataModel.COLUMN_TTIME4, time4);
            values.put(TemperatureDataModel.COLUMN_TTEMP4, temp4);

            values.put(TemperatureDataModel.COLUMN_TTIME5, time5);
            values.put(TemperatureDataModel.COLUMN_TTEMP5, temp5);
            values.put(TemperatureDataModel.COLUMN_TTIME6, time6);
            values.put(TemperatureDataModel.COLUMN_TTEMP6, temp6);
            values.put(TemperatureDataModel.COLUMN_TTIME7, time7);
            values.put(TemperatureDataModel.COLUMN_TTEMP7, temp7);
            values.put(TemperatureDataModel.COLUMN_TTIME8, time8);
            values.put(TemperatureDataModel.COLUMN_TTEMP8, temp8);

            values.put(TemperatureDataModel.COLUMN_OTHER1, timeOven);
            values.put(TemperatureDataModel.COLUMN_OTHER2, other2);

            // insert row
            long id = db.insert(TemperatureDataModel.TABLE_TEMP_DATA_NAME, null, values);

            // close db connection
            db.close();

            // return newly inserted row id
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @SuppressLint("Range")
    public List<TemperatureDataModel> getAllTempData() {
        List<TemperatureDataModel> sugarList = new ArrayList<>();

        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + TemperatureDataModel.TABLE_TEMP_DATA_NAME;

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        TemperatureDataModel tempObj = new TemperatureDataModel();

                        sugarList.add(tempObj);

                        tempObj.setId(cursor.getInt(cursor.getColumnIndex(TemperatureDataModel.COLUMN_ID)));
                        tempObj.setCreatedAt(cursor.getString(cursor.getColumnIndex(TemperatureDataModel.COLUMN_CREATEDAT)));
                        tempObj.setTimeStamp(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TIMESTAMP)));
                        tempObj.setPartId(cursor.getString(cursor.getColumnIndex(TemperatureDataModel.COLUMN_PARTID)));
                        tempObj.setDate(cursor.getString(cursor.getColumnIndex(TemperatureDataModel.COLUMN_DATE)));
                        tempObj.setTime(cursor.getString(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TIME)));

                        tempObj.setTtime1(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTIME1)));
                        tempObj.setTtemp1(cursor.getFloat(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTEMP1)));
                        tempObj.setTtime2(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTIME2)));
                        tempObj.setTtemp2(cursor.getFloat(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTEMP2)));
                        tempObj.setTtime3(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTIME3)));
                        tempObj.setTtemp3(cursor.getFloat(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTEMP3)));
                        tempObj.setTtime4(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTIME4)));
                        tempObj.setTtemp4(cursor.getFloat(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTEMP4)));

                        tempObj.setTtime5(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTIME5)));
                        tempObj.setTtemp5(cursor.getFloat(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTEMP5)));
                        tempObj.setTtime6(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTIME6)));
                        tempObj.setTtemp6(cursor.getFloat(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTEMP6)));
                        tempObj.setTtime7(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTIME7)));
                        tempObj.setTtemp7(cursor.getFloat(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTEMP7)));
                        tempObj.setTtime8(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTIME8)));
                        tempObj.setTtemp8(cursor.getFloat(cursor.getColumnIndex(TemperatureDataModel.COLUMN_TTEMP8)));

                        tempObj.settOven(cursor.getLong(cursor.getColumnIndex(TemperatureDataModel.COLUMN_OTHER1)));

                        tempObj.setOther2(cursor.getString(cursor.getColumnIndex(TemperatureDataModel.COLUMN_OTHER2)));

                    } while (cursor.moveToNext());
                }

                cursor.close();
            }

            // close db connection
            db.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return notes list
        return sugarList;
    }

    public void deleteTempData(TemperatureDataModel tempData) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TemperatureDataModel.TABLE_TEMP_DATA_NAME, TemperatureDataModel.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(tempData.getId())});
            db.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //*** Shift Data ------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    public List<ShiftDataModel> getUpdatedShiftDataList() {
        List<ShiftDataModel> sugarList = new ArrayList<>();

        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + ShiftDataModel.TABLE_SHIFT_DATA_NAME + " WHERE " + ShiftDataModel.COLUMN_UPDATED + " >= 1";

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        ShiftDataModel tempObj = new ShiftDataModel();

                        sugarList.add(tempObj);

                        tempObj.setId(cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_ID)));
                        tempObj.setShiftId(cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_SHIFTID)));

                        tempObj.setShiftStartTime(cursor.getInt(cursor.getColumnIndex(ShiftDataModel.COLUMN_SHIFT_STARTTIME)));
                        tempObj.setShiftStopTime(cursor.getInt(cursor.getColumnIndex(ShiftDataModel.COLUMN_SHIFT_STOPTIME)));

                        tempObj.setJobID(cursor.getString(cursor.getColumnIndex(ShiftDataModel.COLUMN_JOBID)));
                        tempObj.setJobSequenceNo(cursor.getString(cursor.getColumnIndex(ShiftDataModel.COLUMN_JOBSEQNO)));

                        tempObj.setMachine(cursor.getString(cursor.getColumnIndex(ShiftDataModel.COLUMN_MACHINE)));
                        tempObj.setOperator(cursor.getString(cursor.getColumnIndex(ShiftDataModel.COLUMN_OPERATOR)));
                        tempObj.setUserID(cursor.getString(cursor.getColumnIndex(ShiftDataModel.COLUMN_USERID)));

                        tempObj.setStartTime(cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_STARTTIME)));
                        tempObj.setStopTime(cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_STOPTIME)));

                        tempObj.setUtilization(cursor.getFloat(cursor.getColumnIndex(ShiftDataModel.COLUMN_UTILIZATION)));
                        tempObj.setOffLineT(cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_OFFLINE)));

                        tempObj.setOee(cursor.getFloat(cursor.getColumnIndex(ShiftDataModel.COLUMN_OEE)));
                        tempObj.setAvailablity(cursor.getFloat(cursor.getColumnIndex(ShiftDataModel.COLUMN_AVAILABILITY)));
                        tempObj.setPerformance(cursor.getFloat(cursor.getColumnIndex(ShiftDataModel.COLUMN_PERFORMANCE)));
                        tempObj.setQuality(cursor.getFloat(cursor.getColumnIndex(ShiftDataModel.COLUMN_QUALITY)));

                        tempObj.setGoodParts(cursor.getInt(cursor.getColumnIndex(ShiftDataModel.COLUMN_GOODS)));
                        tempObj.setBadParts(cursor.getInt(cursor.getColumnIndex(ShiftDataModel.COLUMN_BADS)));

                        tempObj.setElapsedTimeInMils(0, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_UNCAT)));
                        tempObj.setElapsedTimeInMils(1, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_INCYCLE)));
                        tempObj.setElapsedTimeInMils(2, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_R1T)));
                        tempObj.setElapsedTimeInMils(3, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_R2T)));
                        tempObj.setElapsedTimeInMils(4, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_R3T)));
                        tempObj.setElapsedTimeInMils(5, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_R4T)));
                        tempObj.setElapsedTimeInMils(6, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_R5T)));
                        tempObj.setElapsedTimeInMils(7, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_R6T)));
                        tempObj.setElapsedTimeInMils(8, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_R7T)));
                        tempObj.setElapsedTimeInMils(9, cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_R8T)));

                        tempObj.setAuxData1(cursor.getFloat(cursor.getColumnIndex(ShiftDataModel.COLUMN_AUXDATA1)));
                        tempObj.setAuxData2(cursor.getFloat(cursor.getColumnIndex(ShiftDataModel.COLUMN_AUXDATA2)));
                        tempObj.setAuxData3(cursor.getFloat(cursor.getColumnIndex(ShiftDataModel.COLUMN_AUXDATA3)));

                        tempObj.setCompleted(cursor.getInt(cursor.getColumnIndex(ShiftDataModel.COLUMN_COMPLETED)) > 0);
                        tempObj.setUpdated(cursor.getInt(cursor.getColumnIndex(ShiftDataModel.COLUMN_UPDATED)) > 0);

                        tempObj.setPartIds(cursor.getString(cursor.getColumnIndex(ShiftDataModel.COLUMN_PARTIDS)));

                        tempObj.setShiftSetting(cursor.getString(cursor.getColumnIndex(ShiftDataModel.COLUMN_EXT1)));
                        tempObj.setTargetCycleTimeSeconds(cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_EXT2)));
                        tempObj.setPlannedProductionTime(cursor.getLong(cursor.getColumnIndex(ShiftDataModel.COLUMN_EXT3)));

                        // Rework Status
                        String strRework = cursor.getString(cursor.getColumnIndex(ShiftDataModel.COLUMN_REWORK));
                        int rework = 0;
                        try{
                            rework = Integer.parseInt(strRework);
                        }catch (Exception e) {e.printStackTrace();}
                        tempObj.setStatusRework(rework);

                        String strSetup = cursor.getString(cursor.getColumnIndex(ShiftDataModel.COLUMN_SETUP));
                        int setup = 0;
                        try{
                            setup = Integer.parseInt(strSetup);
                        }catch (Exception e) {e.printStackTrace();}
                        tempObj.setStatusSetup(setup);
                    } while (cursor.moveToNext());
                }

                cursor.close();
            }

            // close db connection
            db.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return notes list
        return sugarList;
    }

    public long insertShiftData(ShiftDataModel shiftData) {

        if (TextUtils.isEmpty(shiftData.getMachine())) {
            return 0;
        }

        try {
            // get writable database as we want to write data
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(ShiftDataModel.COLUMN_SHIFTID, shiftData.getShiftId());

            values.put(ShiftDataModel.COLUMN_SHIFT_STARTTIME, shiftData.getShiftStartTime());
            values.put(ShiftDataModel.COLUMN_SHIFT_STOPTIME, shiftData.getShiftStopTime());

            values.put(ShiftDataModel.COLUMN_JOBID, shiftData.getJobID());
            values.put(ShiftDataModel.COLUMN_JOBSEQNO, shiftData.getJobSequenceNo());

            values.put(ShiftDataModel.COLUMN_MACHINE, shiftData.getMachine());

            values.put(ShiftDataModel.COLUMN_OPERATOR, shiftData.getOperator());
            values.put(ShiftDataModel.COLUMN_USERID, shiftData.getUserID());

            values.put(ShiftDataModel.COLUMN_STARTTIME, shiftData.getStartTime());
            values.put(ShiftDataModel.COLUMN_STOPTIME, shiftData.getStopTime());

            values.put(ShiftDataModel.COLUMN_UTILIZATION, shiftData.getUtilization());
            values.put(ShiftDataModel.COLUMN_OFFLINE, shiftData.getOffLineT());

            values.put(ShiftDataModel.COLUMN_OEE, shiftData.getOee());
            values.put(ShiftDataModel.COLUMN_AVAILABILITY, shiftData.getAvailablity());
            values.put(ShiftDataModel.COLUMN_PERFORMANCE, shiftData.getPerformance());
            values.put(ShiftDataModel.COLUMN_QUALITY, shiftData.getQuality());

            int shiftGoods = shiftData.getGoodParts() - shiftData.getPrevGoodParts();
            int shiftBads = shiftData.getBadParts() - shiftData.getPrevBadParts();
            values.put(ShiftDataModel.COLUMN_GOODS, shiftGoods >= 0 ? shiftGoods : 0);
            values.put(ShiftDataModel.COLUMN_BADS, shiftBads >= 0 ? shiftBads : 0);

            values.put(ShiftDataModel.COLUMN_UNCAT, shiftData.getElapsedTimeInMils(0));
            values.put(ShiftDataModel.COLUMN_INCYCLE, shiftData.getElapsedTimeInMils(1));
            values.put(ShiftDataModel.COLUMN_R1T, shiftData.getElapsedTimeInMils(2));
            values.put(ShiftDataModel.COLUMN_R2T, shiftData.getElapsedTimeInMils(3));
            values.put(ShiftDataModel.COLUMN_R3T, shiftData.getElapsedTimeInMils(4));
            values.put(ShiftDataModel.COLUMN_R4T, shiftData.getElapsedTimeInMils(5));
            values.put(ShiftDataModel.COLUMN_R5T, shiftData.getElapsedTimeInMils(6));
            values.put(ShiftDataModel.COLUMN_R6T, shiftData.getElapsedTimeInMils(7));
            values.put(ShiftDataModel.COLUMN_R7T, shiftData.getElapsedTimeInMils(8));
            values.put(ShiftDataModel.COLUMN_R8T, shiftData.getElapsedTimeInMils(9));

            values.put(ShiftDataModel.COLUMN_AUXDATA1, shiftData.getAuxData1());
            values.put(ShiftDataModel.COLUMN_AUXDATA2, shiftData.getAuxData2());
            values.put(ShiftDataModel.COLUMN_AUXDATA3, shiftData.getAuxData3());

            values.put(ShiftDataModel.COLUMN_COMPLETED, 0);
            values.put(ShiftDataModel.COLUMN_UPDATED, 1);

            values.put(ShiftDataModel.COLUMN_REWORK, shiftData.getStatusRework());
            values.put(ShiftDataModel.COLUMN_SETUP, shiftData.getStatusSetup());

            values.put(ShiftDataModel.COLUMN_PARTIDS, shiftData.getPartIds());

            values.put(ShiftDataModel.COLUMN_EXT1, shiftData.getShiftSetting());
            values.put(ShiftDataModel.COLUMN_EXT2, shiftData.getTargetCycleTimeSeconds());
            values.put(ShiftDataModel.COLUMN_EXT3, shiftData.getPlannedProductionTime());

            // insert row
            long id = db.insert(ShiftDataModel.TABLE_SHIFT_DATA_NAME, null, values);

            // close db connection
            db.close();

            shiftData.setId(id);

            // return newly inserted row id
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long updateShiftData(ShiftDataModel shiftData) {

        // In case of machine field is empty, this is invalid shift data and ignore all shift data
        if (shiftData.getId() == 0)
            return 0;

        try {
            // get writable database as we want to write data
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them

            //*** Don't update shift id, it is updated in the data upload module.
            // values.put(ShiftDataModel.COLUMN_SHIFTID, shiftData.getShiftId());

            values.put(ShiftDataModel.COLUMN_SHIFT_STARTTIME, shiftData.getShiftStartTime());
            values.put(ShiftDataModel.COLUMN_SHIFT_STOPTIME, shiftData.getShiftStopTime());

            values.put(ShiftDataModel.COLUMN_JOBID, shiftData.getJobID());
            values.put(ShiftDataModel.COLUMN_JOBSEQNO, shiftData.getJobSequenceNo());

            values.put(ShiftDataModel.COLUMN_MACHINE, shiftData.getMachine());

            values.put(ShiftDataModel.COLUMN_OPERATOR, shiftData.getOperator());
            values.put(ShiftDataModel.COLUMN_USERID, shiftData.getUserID());

            values.put(ShiftDataModel.COLUMN_STARTTIME, shiftData.getStartTime());
            values.put(ShiftDataModel.COLUMN_STOPTIME, shiftData.getStopTime());

            values.put(ShiftDataModel.COLUMN_UTILIZATION, shiftData.getUtilization());
            values.put(ShiftDataModel.COLUMN_OFFLINE, shiftData.getOffLineT());

            values.put(ShiftDataModel.COLUMN_OEE, shiftData.getOee());
            values.put(ShiftDataModel.COLUMN_AVAILABILITY, shiftData.getAvailablity());
            values.put(ShiftDataModel.COLUMN_PERFORMANCE, shiftData.getPerformance());
            values.put(ShiftDataModel.COLUMN_QUALITY, shiftData.getQuality());

            int shiftGoods = shiftData.getGoodParts() - shiftData.getPrevGoodParts();
            int shiftBads = shiftData.getBadParts() - shiftData.getPrevBadParts();
            values.put(ShiftDataModel.COLUMN_GOODS, shiftGoods >= 0 ? shiftGoods : 0);
            values.put(ShiftDataModel.COLUMN_BADS, shiftBads >= 0 ? shiftBads : 0);

            values.put(ShiftDataModel.COLUMN_UNCAT, shiftData.getElapsedTimeInMils(0));
            values.put(ShiftDataModel.COLUMN_INCYCLE, shiftData.getElapsedTimeInMils(1));
            values.put(ShiftDataModel.COLUMN_R1T, shiftData.getElapsedTimeInMils(2));
            values.put(ShiftDataModel.COLUMN_R2T, shiftData.getElapsedTimeInMils(3));
            values.put(ShiftDataModel.COLUMN_R3T, shiftData.getElapsedTimeInMils(4));
            values.put(ShiftDataModel.COLUMN_R4T, shiftData.getElapsedTimeInMils(5));
            values.put(ShiftDataModel.COLUMN_R5T, shiftData.getElapsedTimeInMils(6));
            values.put(ShiftDataModel.COLUMN_R6T, shiftData.getElapsedTimeInMils(7));
            values.put(ShiftDataModel.COLUMN_R7T, shiftData.getElapsedTimeInMils(8));
            values.put(ShiftDataModel.COLUMN_R8T, shiftData.getElapsedTimeInMils(9));

            values.put(ShiftDataModel.COLUMN_AUXDATA1, shiftData.getAuxData1());
            values.put(ShiftDataModel.COLUMN_AUXDATA2, shiftData.getAuxData2());
            values.put(ShiftDataModel.COLUMN_AUXDATA3, shiftData.getAuxData3());

            values.put(ShiftDataModel.COLUMN_COMPLETED, shiftData.isCompleted());
            values.put(ShiftDataModel.COLUMN_UPDATED, 1);

            values.put(ShiftDataModel.COLUMN_REWORK, shiftData.getStatusRework());
            values.put(ShiftDataModel.COLUMN_SETUP, shiftData.getStatusSetup());

            values.put(ShiftDataModel.COLUMN_PARTIDS, shiftData.getPartIds());

            values.put(ShiftDataModel.COLUMN_EXT1, shiftData.getShiftSetting());
            values.put(ShiftDataModel.COLUMN_EXT2, shiftData.getTargetCycleTimeSeconds());
            values.put(ShiftDataModel.COLUMN_EXT3, shiftData.getPlannedProductionTime());

            // updating row
            long ret = db.update(ShiftDataModel.TABLE_SHIFT_DATA_NAME, values, ShiftDataModel.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(shiftData.getId())});

            // close db connection
            db.close();

            // return newly inserted row id
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Refresh Shift ID after insert Server, this is tracking number for sync and update
    public long updateShiftId(ShiftDataModel shiftData) {

        if (shiftData.getId() == 0)
            return 0;

        try {
            // get writable database as we want to write data
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(ShiftDataModel.COLUMN_SHIFTID, shiftData.getShiftId());
            values.put(ShiftDataModel.COLUMN_UPDATED, 0);

            // updating row
            long ret = db.update(ShiftDataModel.TABLE_SHIFT_DATA_NAME, values, ShiftDataModel.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(shiftData.getId())});

            // close db connection
            db.close();

            // return newly inserted row id
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Refresh Shift ID after insert Server, this is tracking number for sync and update
    public long updateShiftStatusAsProcessed(ShiftDataModel shiftData) {

        if (shiftData.getId() == 0)
            return 0;

        try {
            // get writable database as we want to write data
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(ShiftDataModel.COLUMN_SHIFTID, shiftData.getShiftId());
            values.put(ShiftDataModel.COLUMN_UPDATED, 0);

            // updating row
            long ret = db.update(ShiftDataModel.TABLE_SHIFT_DATA_NAME, values, ShiftDataModel.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(shiftData.getId())});

            // close db connection
            db.close();

            // return newly inserted row id
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void deleteShiftData(ShiftDataModel shiftData) {

        if (shiftData.getId() == 0)
            return;

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(ShiftDataModel.TABLE_SHIFT_DATA_NAME, ShiftDataModel.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(shiftData.getId())});
            db.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeOldShiftData() {
        long timeValidData = System.currentTimeMillis() - 86400000 * 3; // Remove data since 3 days ago
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(ShiftDataModel.TABLE_SHIFT_DATA_NAME, ShiftDataModel.COLUMN_STOPTIME + " <= ? and " + ShiftDataModel.COLUMN_UPDATED + " = ?",
                    new String[]{String.valueOf(timeValidData), "0"});
            db.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
