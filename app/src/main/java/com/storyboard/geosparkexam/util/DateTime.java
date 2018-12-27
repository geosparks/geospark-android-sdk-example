package com.storyboard.geosparkexam.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTime {

    public static String filterCalendarDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    public static String filterCalendarDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String calendarDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        return sdf.format(Calendar.getInstance().getTime());
    }
}
