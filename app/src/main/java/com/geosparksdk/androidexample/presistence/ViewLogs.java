package com.geosparksdk.androidexample.presistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;



public class ViewLogs {

    private GeosparkDB mHoloDB;
    private SQLiteDatabase sqLiteDatabase;

    private static final String QUERY = "SELECT " +
            "ID" + " , " +
            "NAME" + " , " +
            "MSG" +
            " FROM " + " VIEWLOGS ";

    public ViewLogs(Context mContext) {
        mHoloDB = new GeosparkDB(mContext);
    }

    private void openDB() {
        sqLiteDatabase = mHoloDB.getWritableDatabase();
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

    public List<GeoConstant> getList() {
        openDB();
        List<GeoConstant> commentsList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                GeoConstant geoConstant = new GeoConstant();
                geoConstant.setmId(cursor.getString(cursor.getColumnIndex("ID")));
                geoConstant.setmName(cursor.getString(cursor.getColumnIndex("NAME")));
                geoConstant.setmComments(cursor.getString(cursor.getColumnIndex("MSG")));
                commentsList.add(geoConstant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDB();
        return commentsList;
    }

    public void clearMainTable() {
        openDB();
        sqLiteDatabase.delete("VIEWLOGS", null, null);
        closeDB();
    }

}