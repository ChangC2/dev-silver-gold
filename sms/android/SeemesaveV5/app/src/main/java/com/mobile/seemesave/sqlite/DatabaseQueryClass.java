package com.mobile.seemesave.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.mobile.seemesave.application.App;

import org.json.JSONException;

public class DatabaseQueryClass {
    private Context context;
    static DatabaseQueryClass instance;

    public DatabaseQueryClass(Context context) {
        this.context = context;
    }

    public static DatabaseQueryClass getInstance(){
        if (instance == null){
            instance = new DatabaseQueryClass(App.getInstance());
        }

        return instance;
    }

    public long insertData(String user_id, String page, String data, String type, String status ) {
        long unixTime = System.currentTimeMillis() / 1000L;
        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConfig.RECORD_USER_ID, user_id);
        contentValues.put(DBConfig.RECORD_PAGE, page);
        contentValues.put(DBConfig.RECORD_DATA, data);
        contentValues.put(DBConfig.RECORD_TYPE, type);
        contentValues.put(DBConfig.RECORD_STATUS, status);
        contentValues.put(DBConfig.RECORD_TIMESTAMP, String.valueOf(unixTime));


        String selection = "user_id = ? and page = ?";
        String[] whereClause = new String[]{user_id, page};
        if (!type.equalsIgnoreCase("")) {
            selection = "user_id = ? and page = ? and type = ?";
            whereClause = new String[]{user_id, page, type};
        }

        try {
            int count = (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, DBConfig.TABLE_MAIN, selection, whereClause);
            if (count == 0)
                id = sqLiteDatabase.insertOrThrow(DBConfig.TABLE_MAIN, null, contentValues);
            else
                id = sqLiteDatabase.update(DBConfig.TABLE_MAIN, contentValues, selection, whereClause);

        } catch (SQLiteException e) {

        } finally {
            if (id == -1) {
                String CREATE_MAIN_TABLE = "CREATE TABLE " + DBConfig.TABLE_MAIN + "("
                        + DBConfig.RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + DBConfig.RECORD_USER_ID + " TEXT , "
                        + DBConfig.RECORD_PAGE + " TEXT , "
                        + DBConfig.RECORD_DATA + " TEXT , "
                        + DBConfig.RECORD_TYPE + " TEXT , "
                        + DBConfig.RECORD_STATUS + " TEXT , "
                        + DBConfig.RECORD_TIMESTAMP + " TEXT "
                        + ")";

                sqLiteDatabase.execSQL(CREATE_MAIN_TABLE);
                id = sqLiteDatabase.insertOrThrow(DBConfig.TABLE_MAIN, null, contentValues);
            }
            sqLiteDatabase.close();
        }
        return id;
    }
    public String getData(String user_id, String page, String type) throws JSONException {
        Cursor cursor = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        try {
            String selectQuery = "";
            if (type.equalsIgnoreCase("")) {
                selectQuery = "SELECT  * FROM " + DBConfig.TABLE_MAIN + " WHERE user_id = " + user_id + " and page = '"+ page +"' ORDER BY ID DESC LIMIT 1";
            } else {
                selectQuery = "SELECT  * FROM " + DBConfig.TABLE_MAIN + " WHERE user_id = " + user_id + " and page = '"+ page +"' and type = '"+ type +"' ORDER BY ID DESC LIMIT 1";
            }

            cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(DBConfig.RECORD_DATA));
                return data;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
