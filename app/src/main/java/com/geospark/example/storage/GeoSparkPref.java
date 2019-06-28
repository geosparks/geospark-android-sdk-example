package com.geospark.example.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class GeoSparkPref {

    private static final String INIT = "INIT";
    private static final String USER_ID = "USERID";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String CRAETEUSER = "CRAETEUSER";
    private static final String GETUSER = "GETUSER";
    private static final String STARTTRACK = "STARTTRACK";
    private static final String STOPTRACK = "STOPTRACK";
    private static final String LOGOUT = "LOGOUT";

    private static SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences("GEOSPARK", Context.MODE_PRIVATE);
    }

    private static void setString(Context context, String tagName, String value) {
        SharedPreferences.Editor mEditor = getInstance(context).edit();
        mEditor.putString(tagName, value);
        mEditor.apply();
        mEditor.commit();
    }

    private static String getString(Context context, String tagName) {
        return getInstance(context).getString(tagName, null);
    }

    private static void setBoolean(Context context, String tagName, boolean value) {
        SharedPreferences.Editor mEditor = getInstance(context).edit();
        mEditor.putBoolean(tagName, value);
        mEditor.apply();
        mEditor.commit();
    }

    private static boolean getBoolean(Context context, String tagName) {
        return getInstance(context).getBoolean(tagName, false);
    }

    public static void removeItem(Context context, String name) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.remove(name);
        editor.apply();
        editor.commit();
    }

    public static void setUserCreated(Context context) {
        GeoSparkPref.setBoolean(context, "INIT", false);
    }

    public static boolean isUserCreated(Context context) {
        return getBoolean(context, INIT);
    }

    public static void setUserId(Context context, String userId) {
        setString(context, USER_ID, userId);
    }

    public static String getUserId(Context context) {
        return getString(context, USER_ID);
    }

    public static void setDescription(Context context, String desc) {
        setString(context, DESCRIPTION, desc);
    }

    public static String getDescription(Context context) {
        return getString(context, DESCRIPTION);
    }

    public static void setButtonStatus(Context context, boolean createUser, boolean getUser, boolean startTrack, boolean logout) {
        setBoolean(context, INIT, true);
        setBoolean(context, CRAETEUSER, createUser);
        setBoolean(context, GETUSER, getUser);
        setBoolean(context, STARTTRACK, startTrack);
        setBoolean(context, STOPTRACK, false);
        setBoolean(context, LOGOUT, logout);
    }

    public static boolean getCreateButtonStatus(Context context) {
        return getBoolean(context, CRAETEUSER);
    }

    public static boolean getUserButtonStatus(Context context) {
        return getBoolean(context, GETUSER);
    }

    public static boolean getStartTrackButtonStatus(Context context) {
        return getBoolean(context, STARTTRACK);
    }

    public static boolean getStopTrackButtonStatus(Context context) {
        return getBoolean(context, STOPTRACK);
    }

    public static boolean getLogout(Context context) {
        return getBoolean(context, LOGOUT);
    }

    public static void trackStatus(Context context, boolean startTrack, boolean stopTrack) {
        setBoolean(context, STARTTRACK, startTrack);
        setBoolean(context, STOPTRACK, stopTrack);
    }
}



