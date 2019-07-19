package com.geospark.example;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.geospark.example.service.GeoSparkImplicitService;

public class Util {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void locationJob(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            try {
                JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo job = new JobInfo.Builder(1001, new ComponentName(context, GeoSparkImplicitService.class))
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