package com.storyboard.geosparkexam.presistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GeosparkDB extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;
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
            "SPEED" + " TEXT NULL, " +
            "DATETIME" + " TEXT NULL " +
            ")";

    private static final String USER_LOG_QUERY = "SELECT " +
            "ID" + " , " +
            "NAME" + " , " +
            "MSG" +
            " FROM " + " VIEWLOGS ";

    private static final String GEO_LOG_QUERY = "SELECT " +
            "ID" + " , " +
            "LAT" + " , " +
            "LNG" + " , " +
            "SPEED" + " , " +
            "DATETIME" +
            " FROM " + " LATLNG ";

    private static final String GEO_LOG_REVERSEQUERY = "SELECT " +
            "ID" + " , " +
            "LAT" + " , " +
            "LNG" + " , " +
            "SPEED" + " , " +
            "DATETIME" +
            " FROM " + " LATLNG ORDER BY ID DESC ";

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

    private void openDB() {
        sqLiteDatabase = this.getWritableDatabase();
    }

    private void closeDB() {
        sqLiteDatabase.close();
    }

    public void createLog(String name, String comments) {
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("MSG", comments);
        sqLiteDatabase.insert("VIEWLOGS", null, contentValues);
        closeDB();
    }

    public List<GeosparkLog> getUserList() {
        openDB();
        List<GeosparkLog> commentsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(USER_LOG_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                GeosparkLog geosparkLog = new GeosparkLog();
                geosparkLog.setmId(cursor.getString(cursor.getColumnIndex("ID")));
                geosparkLog.setmName(cursor.getString(cursor.getColumnIndex("NAME")));
                geosparkLog.setmComments(cursor.getString(cursor.getColumnIndex("MSG")));
                commentsList.add(geosparkLog);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return commentsList;
    }

    public void clearUserTable() {
        openDB();
        sqLiteDatabase.delete("VIEWLOGS", null, null);
        closeDB();
    }

    public void createLatLngLog(GeosparkLog geosparkLog) {
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LAT", geosparkLog.getmLat());
        contentValues.put("LNG", geosparkLog.getmLng());
        contentValues.put("SPEED", geosparkLog.getmSpeed());
        contentValues.put("DATETIME", geosparkLog.getmDateTime());
        sqLiteDatabase.insert("LATLNG", null, contentValues);
        closeDB();
    }

    public List<GeosparkLog> getGeoList() {
        openDB();
        List<GeosparkLog> commentsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(GEO_LOG_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                GeosparkLog geosparkLog = new GeosparkLog();
                geosparkLog.setmId(cursor.getString(cursor.getColumnIndex("ID")));
                geosparkLog.setmLat(cursor.getString(cursor.getColumnIndex("LAT")));
                geosparkLog.setmLng(cursor.getString(cursor.getColumnIndex("LNG")));
                geosparkLog.setmSpeed(cursor.getString(cursor.getColumnIndex("SPEED")));
                geosparkLog.setmDateTime(cursor.getString(cursor.getColumnIndex("DATETIME")));
                commentsList.add(geosparkLog);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return commentsList;
    }

    public List<GeosparkLog> getGeoReverseList() {
        openDB();
        List<GeosparkLog> commentsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(GEO_LOG_REVERSEQUERY, null);
        if (cursor.moveToFirst()) {
            do {
                GeosparkLog geosparkLog = new GeosparkLog();
                geosparkLog.setmId(cursor.getString(cursor.getColumnIndex("ID")));
                geosparkLog.setmLat(cursor.getString(cursor.getColumnIndex("LAT")));
                geosparkLog.setmLng(cursor.getString(cursor.getColumnIndex("LNG")));
                geosparkLog.setmSpeed(cursor.getString(cursor.getColumnIndex("SPEED")));
                geosparkLog.setmDateTime(cursor.getString(cursor.getColumnIndex("DATETIME")));
                commentsList.add(geosparkLog);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return commentsList;
    }

    public void clearGeoTable() {
        openDB();
        sqLiteDatabase.delete("LATLNG", null, null);
        closeDB();
    }

}

