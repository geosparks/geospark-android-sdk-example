package com.geospark.example;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.geospark.example.service.GSImplicitService;

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

    public static void locationJob(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            try {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo job = new JobInfo.Builder(1001, new ComponentName(context, GSImplicitService.class))
                        .setMinimumLatency(1000)
                        .setOverrideDeadline(1000)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                        .setPersisted(true)
                        .build();
                jobScheduler.schedule(job);
            } catch (Exception e) {

            }
        }
    }
}