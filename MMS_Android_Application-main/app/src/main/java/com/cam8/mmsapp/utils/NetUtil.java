package com.cam8.mmsapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

public class NetUtil {

    public static boolean isInternetAvailable(Context activity) {

        ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = conMgr.getActiveNetwork();
            NetworkCapabilities networkCapabilities = conMgr.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            } else return false;
        } else {
            // below API Level 23
            return conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected();
        }
    }
}
