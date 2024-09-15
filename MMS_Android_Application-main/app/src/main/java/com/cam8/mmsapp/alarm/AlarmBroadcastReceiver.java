package com.cam8.mmsapp.alarm;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.cam8.mmsapp.AppSettings;
import com.cam8.mmsapp.MyApplication;
import com.cam8.mmsapp.report.ReportService;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equalsIgnoreCase(intent.getAction())) {

            Log.e("BOOT", "Received BOOT_COMPLETED!!!");

            // Start Account check service
            // Flowing block crashes error, so we trigger Alaram manager here directly
            //Intent serviceIntent = new Intent(context, AlarmTriggerService.class);
            //context.startService(serviceIntent);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, AlarmSettings.INTERVAL, pendingIntent);

            // Start Report Service
            if (!isMyServiceRunning(context, ReportService.class)) {
                Log.e("report", "Report Service is not running");

                // Start Report check service
                Intent reportIntent = new Intent(context, ReportService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(reportIntent);
                } else {
                    context.startService(reportIntent);
                }
            } else {
                Log.e("report", "Report Service is now running");
            }
        } else if (true) {

            // Check Customer information
            final AppSettings appSettings = new AppSettings(context);
            if (TextUtils.isEmpty(appSettings.getUserId())) {
                return;
            }

            // check Valid Status And App version
            PendingResult pendingResult = goAsync();
            MyApplication myApp = MyApplication.getInstance();
            myApp.checkAppValidStatus();
            myApp.checkAppVersion();
            myApp.getMaintenanceTask();
            myApp.getDeviceSettings();

            myApp.getShiftsData();

            System.gc();

            pendingResult.finish();
        }
    }

    protected boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
