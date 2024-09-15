package com.cam8.mmsapp.alarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

public class AlarmTriggerService extends IntentService {

    public AlarmTriggerService() {
        super("AlarmTriggerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, AlarmSettings.INTERVAL, pendingIntent);
    }
}
