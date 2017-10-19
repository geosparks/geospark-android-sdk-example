package com.geosparksdk.androidexample.presistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class LatLngLog {

    private GeosparkDB mHoloDB;
    private SQLiteDatabase sqLiteDatabase;

    private static final String QUERY = "SELECT " +
            "ID" + " , " +
            "LAT" + " , " +
            "LNG" + " , " +
            "DATETIME" +
            " FROM " + " LATLNG ";

    public LatLngLog(Context mContext) {
        mHoloDB = new GeosparkDB(mContext);
    }

    private void openDB() {
        sqLiteDatabase = mHoloDB.getWritableDatabase();
    }

    private void closeDB() {
        sqLiteDatabase.close();
    }

    public void createLatLngLog(GeoConstant geoConstant) {
        openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LAT", geoConstant.getmLat());
        contentValues.put("LNG", geoConstant.getmLng());
        contentValues.put("DATETIME", geoConstant.getmDateTime());
        sqLiteDatabase.insert("LATLNG", null, contentValues);
        closeDB();
    }

    public List<GeoConstant> getList() {
        openDB();
        List<GeoConstant> commentsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                GeoConstant geoConstant = new GeoConstant();
                geoConstant.setmId(cursor.getString(cursor.getColumnIndex("ID")));
                geoConstant.setmLat(cursor.getString(cursor.getColumnIndex("LAT")));
                geoConstant.setmLng(cursor.getString(cursor.getColumnIndex("LNG")));
                geoConstant.setmDateTime(cursor.getString(cursor.getColumnIndex("DATETIME")));
                commentsList.add(geoConstant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return commentsList;
    }

    public void clearMainTable() {
        openDB();
        sqLiteDatabase.delete("LATLNG", null, null);
        closeDB();
    }

}