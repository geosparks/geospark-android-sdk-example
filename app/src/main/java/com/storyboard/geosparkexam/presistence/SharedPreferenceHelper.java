package com.storyboard.geosparkexam.presistence;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    private static String shared_prefer = "GEOSPARK";

    private static void setStringText(Context context, String tagName, String value) {
        SharedPreferences mMainSharedPreferences = context.getSharedPreferences(shared_prefer, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mMainSharedPreferences.edit();
        mEditor.putString(tagName, value);
        mEditor.apply();
        mEditor.commit();
    }

    private static String getStringText(Context context, String tagName) {
        SharedPreferences mMainSharedPreferences = context.getSharedPreferences(shared_prefer, Context.MODE_PRIVATE);
        return mMainSharedPreferences.getString(tagName, null);
    }

    private static void setBoolean(Context context, String tagName, boolean value) {
        SharedPreferences mMainSharedPreferences = context.getSharedPreferences(shared_prefer, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mMainSharedPreferences.edit();
        mEditor.putBoolean(tagName, value);
        mEditor.apply();
        mEditor.commit();
    }

    private static boolean getBoolean(Context context, String tagName) {
        SharedPreferences mMainSharedPreferences = context.getSharedPreferences(shared_prefer, Context.MODE_PRIVATE);
        return mMainSharedPreferences.getBoolean(tagName, false);
    }

    public static void clearInfo(Context context, String tag) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(shared_prefer, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.remove(tag);
        mEditor.apply();
        mEditor.commit();
    }

    /************** Save details **************/
    public static void saveInit(Context context) {
        SharedPreferenceHelper.setBoolean(context, "INIT", false);
    }

    public static void saveDeviceToken(Context context, String token) {
        setStringText(context, "DEVICE_TOKEN", token);
    }

    public static void changeButtonStatus(Context context, boolean createUser,
                                          boolean getUser, boolean startTrack, boolean mockStartTrack, boolean logout) {
        setBoolean(context, "CRAETEUSER", createUser);
        setBoolean(context, "GETUSER", getUser);
        setBoolean(context, "STARTTRACK", startTrack);
        setBoolean(context, "STOPTRACK", false);
        setBoolean(context, "STARTMOCKTRACK", mockStartTrack);
        setBoolean(context, "STOPTMOCKTRACK", false);
        setBoolean(context, "LOGOUT", logout);
        setBoolean(context, "INIT", true);
    }

    public static void trackStatus(Context context, boolean startTrack, boolean stopTrack, boolean mockStartTrack) {
        setBoolean(context, "STARTTRACK", startTrack);
        setBoolean(context, "STOPTRACK", stopTrack);
        //Mock
        setBoolean(context, "STARTMOCKTRACK", mockStartTrack);
        setBoolean(context, "STOPTMOCKTRACK", false);
    }

    public static void trackMockStatus(Context context, boolean mockStartTrack, boolean mockStopTrack, boolean startTrack) {
        setBoolean(context, "STARTMOCKTRACK", mockStartTrack);
        setBoolean(context, "STOPTMOCKTRACK", mockStopTrack);
        //StartTracking
        setBoolean(context, "STARTTRACK", startTrack);
        setBoolean(context, "STOPTRACK", false);
    }

    /************** Get details **************/
    public static boolean getInit(Context context) {
        return getBoolean(context, "INIT");
    }

    public static String getDeviceToken(Context context) {
        return getStringText(context, "DEVICE_TOKEN");
    }

    public static boolean getCreateUser(Context context) {
        return getBoolean(context, "CRAETEUSER");
    }

    public static boolean getUser(Context context) {
        return getBoolean(context, "GETUSER");
    }

    public static boolean getStartTrack(Context context) {
        return getBoolean(context, "STARTTRACK");
    }

    public static boolean getStopTrack(Context context) {
        return getBoolean(context, "STOPTRACK");
    }

    public static boolean getMockStartTrack(Context context) {
        return getBoolean(context, "STARTMOCKTRACK");
    }

    public static boolean getMockStopTrack(Context context) {
        return getBoolean(context, "STOPTMOCKTRACK");
    }

    public static boolean getLogout(Context context) {
        return getBoolean(context, "LOGOUT");
    }

}



