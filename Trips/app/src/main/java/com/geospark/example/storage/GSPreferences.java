package com.geospark.example.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class GSPreferences {

    private static final String INIT = "INIT";
    public static final String USER_ID = "USERID";
    public static final String DESCRIPTION = "DESCRIPTION";
    private static final String CREATE_USER = "CREATEUSER";
    private static final String GET_USER = "GETUSER";
    private static final String START_TRACK = "STARTTRACK";
    private static final String STOP_TRACK = "STOPTRACK";
    private static final String LOGOUT = "LOGOUT";
    private static final String PREF_NAME = "GEOSPARK";

    private static SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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

    public static void setInit(Context context) {
        GSPreferences.setBoolean(context, "INIT", false);
    }

    public static boolean isInitialized(Context context) {
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

    public static void setViewStatus(Context context, boolean createUser, boolean getUser, boolean startTrack, boolean logout) {
        setBoolean(context, INIT, true);
        setBoolean(context, CREATE_USER, createUser);
        setBoolean(context, GET_USER, getUser);
        setBoolean(context, START_TRACK, startTrack);
        setBoolean(context, STOP_TRACK, false);
        setBoolean(context, LOGOUT, logout);
    }

    public static void setTrackingView(Context context, boolean startTrack, boolean stopTrack) {
        setBoolean(context, START_TRACK, startTrack);
        setBoolean(context, STOP_TRACK, stopTrack);
    }

    public static boolean isCreateViewEnabled(Context context) {
        return getBoolean(context, CREATE_USER);
    }

    public static boolean isGetUserViewEnabled(Context context) {
        return getBoolean(context, GET_USER);
    }

    public static boolean isStartTrackingEnabled(Context context) {
        return getBoolean(context, START_TRACK);
    }

    public static boolean isStopTrackingEnabled(Context context) {
        return getBoolean(context, STOP_TRACK);
    }

    public static boolean isLogoutViewEnabled(Context context) {
        return getBoolean(context, LOGOUT);
    }
}