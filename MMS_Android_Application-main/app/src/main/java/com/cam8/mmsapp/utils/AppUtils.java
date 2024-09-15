package com.cam8.mmsapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import static android.content.Context.BATTERY_SERVICE;

public class AppUtils {
    public static int getBatteryPercentage(Context context) {

        if (Build.VERSION.SDK_INT >= 21) {

            BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);

            int butteryProp = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            if (butteryProp < 0 || butteryProp > 100) {
                butteryProp = 100;
            }

            return butteryProp;
        } else {

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            double batteryPct = level / (double) scale;
            if (batteryPct < 0 || batteryPct > 1) {
                batteryPct = 1;
            }

            return (int) (batteryPct * 100);
        }
    }
}
