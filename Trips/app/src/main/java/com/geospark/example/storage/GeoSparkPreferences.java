package com.geospark.example.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class GeoSparkPreferences {

    private static final String signIn = "signIn";

    private static SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences("geospark", Context.MODE_PRIVATE);
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

    public static void setSignIn(Context context, boolean value) {
        GeoSparkPreferences.setBoolean(context, signIn, value);
    }

    public static boolean isSignedIn(Context context) {
        return getBoolean(context, signIn);
    }
}