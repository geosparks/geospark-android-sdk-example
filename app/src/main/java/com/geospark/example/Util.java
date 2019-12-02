package com.geospark.example;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Util {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String utcToLocal(final String dateString) throws ParseException {
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = utcFormat.parse(dateString);
        DateFormat currentTFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        currentTFormat.setTimeZone(TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().getID()));
        return currentTFormat.format(date);
    }
}