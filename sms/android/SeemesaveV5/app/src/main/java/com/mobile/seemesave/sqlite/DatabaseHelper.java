package com.mobile.seemesave.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;
    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = DBConfig.DATABASE_NAME;

    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(databaseHelper==null){
            synchronized (DatabaseHelper.class) {
                if(databaseHelper==null)
                    databaseHelper = new DatabaseHelper(context);
            }
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAIN_TABLE = "CREATE TABLE " + DBConfig.TABLE_MAIN + "("
                + DBConfig.RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBConfig.RECORD_USER_ID + " TEXT , "
                + DBConfig.RECORD_PAGE + " TEXT , "
                + DBConfig.RECORD_DATA + " TEXT , "
                + DBConfig.RECORD_TYPE + " TEXT , "
                + DBConfig.RECORD_STATUS + " TEXT , "
                + DBConfig.RECORD_TIMESTAMP + " TEXT "
                + ")";
        db.execSQL(CREATE_MAIN_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        Log.e("sqlite:", "upgrade");
        db.execSQL("DROP TABLE IF EXISTS " + DBConfig.TABLE_MAIN);
        onCreate(db);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
