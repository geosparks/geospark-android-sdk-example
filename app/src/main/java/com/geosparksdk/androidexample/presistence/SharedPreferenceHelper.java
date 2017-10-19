package com.geosparksdk.androidexample.presistence;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    private static String shared_prefer = "GEOSPARK";

    public static void setStringText(Context context, String tagName, String value) {
        SharedPreferences mMainSharedPreferences = context.getSharedPreferences(shared_prefer, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mMainSharedPreferences.edit();
        mEditor.putString(tagName, value);
        mEditor.apply();
        mEditor.commit();
    }

    public static String getStringText(Context context, String tagName) {
        SharedPreferences mMainSharedPreferences = context.getSharedPreferences(shared_prefer, Context.MODE_PRIVATE);
        return mMainSharedPreferences.getString(tagName, null);
    }

    public static void setBoolean(Context context, String tagName, boolean value) {
        SharedPreferences mMainSharedPreferences = context.getSharedPreferences(shared_prefer, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mMainSharedPreferences.edit();
        mEditor.putBoolean(tagName, value);
        mEditor.apply();
        mEditor.commit();
    }

    public static boolean getBoolean(Context context, String tagName) {
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
}

