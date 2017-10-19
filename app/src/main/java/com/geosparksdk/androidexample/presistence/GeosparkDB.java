package com.geosparksdk.androidexample.presistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GeosparkDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "geodb";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_VIEWLOGS = "CREATE TABLE VIEWLOGS ( " +
            "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NAME" + " TEXT NULL, " +
            "MSG" + " TEXT NULL " +
            ")";

    private static final String TABLE_LATLNG = "CREATE TABLE LATLNG ( " +
            "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "LAT" + " TEXT NULL, " +
            "LNG" + " TEXT NULL, " +
            "DATETIME" + " TEXT NULL " +
            ")";

    public GeosparkDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        checkTable(sqLiteDatabase, "VIEWLOGS", TABLE_VIEWLOGS);
        checkTable(sqLiteDatabase, "LATLNG", TABLE_LATLNG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    private void checkTable(SQLiteDatabase sqLiteDatabase, String tableName, String query) {
        int count = isTableExist(sqLiteDatabase, tableName);
        if (count == 0) {
            sqLiteDatabase.execSQL(query);
        }
    }

    private int isTableExist(SQLiteDatabase sqLiteDatabase, String tableName) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    private void checkColumn(SQLiteDatabase sqLiteDatabase, String tableName, String column, int oldVersion, int newVersion) {
        boolean checkColumn = isFieldExist(sqLiteDatabase, tableName, column);
        if (!checkColumn) {
            String upgradeQuery = "ALTER TABLE " + tableName + " ADD COLUMN " + column + " TEXT NULL";
            if (oldVersion == 1 && newVersion == 3) {
                sqLiteDatabase.execSQL(upgradeQuery);
            }
        }
    }

    private boolean isFieldExist(SQLiteDatabase sqLiteDatabase, String tableName, String fieldName) {
        boolean isExist = false;
        Cursor res = sqLiteDatabase.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        if (res.moveToFirst()) {
            do {
                int value = res.getColumnIndex("name");
                if (value != -1 && res.getString(value).equals(fieldName)) {
                    isExist = true;
                }
            } while (res.moveToNext());
        }
        res.close();
        return isExist;
    }
}

