package com.mobile.seemesave.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobile.seemesave.utils.G;

public class AlarmReceiver  extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        getGPS(context);
    }

    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 * 1, pi); // Millisec * Second * Minute
    }

    private void getGPS(Context context){
        Log.e("Alarm ---> 1", "##################");
        GpsTracker gpsTracker = new GpsTracker(context);
        if (gpsTracker.canGetLocation() && gpsTracker.getLocation() != null) {
            Log.e("Alarm ---> 2", "##################");
            G.location = gpsTracker.getLocation();
        }
        //apiCallUpdateLocation(G.location, context);
    }
}
