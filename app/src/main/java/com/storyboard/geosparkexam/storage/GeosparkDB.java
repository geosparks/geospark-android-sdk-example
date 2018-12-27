package com.storyboard.geosparkexam.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.storyboard.geosparkexam.util.DateTime;

import java.util.ArrayList;
import java.util.List;

public class GeosparkDB extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;
    private static final String DATABASE_NAME = "geodb";
    private static final int DATABASE_VERSION = 4;

    private static String LOGS = "VIEWLOGS";
    private static String LOCATIONLOGS = "LATLNG";
    private static String ID = "ID";
    private static String NAME = "NAME";
    private static String MSG = "MSG";
    private static String LAT = "LAT";
    private static String LNG = "LNG";
    private static String SPEED = "SPEED";
    private static String ACC = "ACC";
    private static String ACTIVITY = "ACTIVITY";
    private static String FILTERDATE = "FILTERDATE";
    private static String DATETIME = "DATETIME";

    private static final String TABLE_VIEWLOGS = "CREATE TABLE " + LOGS + " ( " +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT NULL, " +
            MSG + " TEXT NULL " +
            ")";

    private static final String TABLE_LATLNG = "CREATE TABLE  " + LOCATIONLOGS + "( " +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LAT + " TEXT NULL, " +
            LNG + " TEXT NULL, " +
            SPEED + " TEXT NULL, " +
            ACC + " TEXT NULL, " +
            ACTIVITY + " TEXT NULL, " +
            FILTERDATE + " TEXT NULL, " +
            DATETIME + " TEXT NULL " +
            ")";

    private static final String USER_LOG_QUERY = "SELECT " +
            ID + " , " +
            NAME + " , " +
            MSG +
            " FROM " + LOGS;

    private static final String GEO_LOG_QUERY = "SELECT " +
            ID + " , " +
            LAT + " , " +
            LNG + " , " +
            SPEED + " , " +
            ACC + " , " +
            ACTIVITY + " , " +
            FILTERDATE + " , " +
            DATETIME +
            " FROM " + LOCATIONLOGS + " WHERE STRFTIME('%Y-%m-%d', FILTERDATE ) = ?";

    private static final String GEO_LOG_REVERSEQUERY = "SELECT " +
            ID + " , " +
            LAT + " , " +
            LNG + " , " +
            SPEED + " , " +
            ACC + " , " +
            ACTIVITY + " , " +
            FILTERDATE + " , " +
            DATETIME +
            " FROM " + LOCATIONLOGS + " ORDER BY " + ID + " DESC ";

    public GeosparkDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        checkTable(sqLiteDatabase, LOGS, TABLE_VIEWLOGS);
        checkTable(sqLiteDatabase, LOCATIONLOGS, TABLE_LATLNG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOGS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOCATIONLOGS);
        onCreate(sqLiteDatabase);

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

    private void openDB() {
        sqLiteDatabase = this.getWritableDatabase();
    }

    private void closeDB() {
        sqLiteDatabase.close();
    }

    public void applicationLog(String name, String comments) {
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name + "  " + DateTime.calendarDate());
        contentValues.put(MSG, comments);
        sqLiteDatabase.insert(LOGS, null, contentValues);
        closeDB();
    }

    public void locationLog(Logs logs) {
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LAT, logs.getmLat());
        contentValues.put(LNG, logs.getmLng());
        contentValues.put(SPEED, logs.getmSpeed());
        contentValues.put(ACC, logs.getmAccuracy());
        contentValues.put(ACTIVITY, logs.getmActivityType());
        contentValues.put(FILTERDATE, logs.getmFilterDate());
        contentValues.put(DATETIME, logs.getmDateTime());
        sqLiteDatabase.insert(LOCATIONLOGS, null, contentValues);
        closeDB();
    }

    public List<Logs> getAppLogs() {
        openDB();
        List<Logs> commentsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(USER_LOG_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                Logs logs = new Logs();
                logs.setmId(cursor.getString(cursor.getColumnIndex(ID)));
                logs.setmName(cursor.getString(cursor.getColumnIndex(NAME)));
                logs.setmComments(cursor.getString(cursor.getColumnIndex(MSG)));
                commentsList.add(logs);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return commentsList;
    }

    public List<Logs> getLocationLogs(String date) {
        openDB();
        List<Logs> logsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(GEO_LOG_QUERY, new String[]{date});
        if (cursor.moveToFirst()) {
            do {
                Logs logs = new Logs();
                logs.setmId(cursor.getString(cursor.getColumnIndex(ID)));
                logs.setmLat(cursor.getString(cursor.getColumnIndex(LAT)));
                logs.setmLng(cursor.getString(cursor.getColumnIndex(LNG)));
                logs.setmSpeed(cursor.getString(cursor.getColumnIndex(SPEED)));
                logs.setmAccuracy(cursor.getString(cursor.getColumnIndex(ACC)));
                logs.setmActivityType(cursor.getString(cursor.getColumnIndex(ACTIVITY)));
                logs.setmFilterDate(cursor.getString(cursor.getColumnIndex(FILTERDATE)));
                logs.setmDateTime(cursor.getString(cursor.getColumnIndex(DATETIME)));
                logsList.add(logs);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return logsList;
    }

    public List<Logs> getLocationLogs() {
        openDB();
        List<Logs> logsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(GEO_LOG_REVERSEQUERY, null);
        if (cursor.moveToFirst()) {
            do {
                Logs logs = new Logs();
                logs.setmId(cursor.getString(cursor.getColumnIndex(ID)));
                logs.setmLat(cursor.getString(cursor.getColumnIndex(LAT)));
                logs.setmLng(cursor.getString(cursor.getColumnIndex(LNG)));
                logs.setmSpeed(cursor.getString(cursor.getColumnIndex(SPEED)));
                logs.setmAccuracy(cursor.getString(cursor.getColumnIndex(ACC)));
                logs.setmActivityType(cursor.getString(cursor.getColumnIndex(ACTIVITY)));
                logs.setmFilterDate(cursor.getString(cursor.getColumnIndex(FILTERDATE)));
                logs.setmDateTime(cursor.getString(cursor.getColumnIndex(DATETIME)));
                logsList.add(logs);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return logsList;
    }

    public void clearLocationLogs() {
        openDB();
        sqLiteDatabase.delete(LOCATIONLOGS, null, null);
        closeDB();
    }

    public void clearAppLogs() {
        openDB();
        sqLiteDatabase.delete(LOGS, null, null);
        closeDB();
    }

}

