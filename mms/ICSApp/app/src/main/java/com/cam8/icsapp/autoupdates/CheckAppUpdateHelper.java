package com.cam8.icsapp.autoupdates;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.cam8.icsapp.AppSettings;
import com.cam8.icsapp.BaseActivity;
import com.cam8.icsapp.MyApplication;
import com.cam8.icsapp.network.ITSRestClient;
import com.cam8.icsapp.utils.DateUtil;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class CheckAppUpdateHelper {

    public interface SearchRestaurantCallback {
        void onFailed(String message);

        void onSuccess(boolean haveUpdates);
    }

    BaseActivity activity;
    MyApplication application;
    AppSettings appSettings;

    SearchRestaurantCallback callback;

    boolean useAPIMode = true;

    public CheckAppUpdateHelper(BaseActivity _activity, SearchRestaurantCallback _callback) {
        this.activity = _activity;
        this.application = (MyApplication) this.activity.getApplication();
        this.appSettings = new AppSettings(this.activity);

        this.callback = _callback;
    }

    public void execute() {

        PackageInfo pInfo = null;
        int curVerionCode = 0;
        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            String version = pInfo.versionName;

            int curMajor = Integer.parseInt(version.substring(0, version.indexOf(".")));
            int curMinor = Integer.parseInt(version.substring(version.indexOf(".") + 1));
            curVerionCode = curMajor * 1000 + curMinor;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            sendError("Couldn't check app updates!");
            return;
        }

        final int finalCurVerionCode = curVerionCode;
        if (useAPIMode) {
            RequestParams requestParams = new RequestParams();
            requestParams.put("deviceId", application.getAndroidId());
            requestParams.put("date", DateUtil.toStringFormat_22(new Date()));

            ITSRestClient.getFullUrl("https://www.slymms.com/Terminals/appICS.ver", requestParams, new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {

                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("VerData", response.toString());

                    if (TextUtils.isEmpty(response)) {
                        sendError("Version Info is not defined!");
                        return;
                    }

                    int verMajor = 0;
                    int verMiner = 0;
                    int lastVerionCode = 0;

                    String[] versionsInfo = response.split("\\.");
                    try {
                        if (versionsInfo != null && versionsInfo.length > 0) {
                            verMajor = Integer.parseInt(versionsInfo[0]);
                        }

                        if (versionsInfo != null && versionsInfo.length > 1) {
                            verMiner = Integer.parseInt(versionsInfo[1]);
                        }

                        lastVerionCode = verMajor * 1000 + verMiner;
                    } catch (Exception e) {
                    }
                    if (lastVerionCode > finalCurVerionCode) {
                        sendResult(true);
                    } else {
                        sendResult(false);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    activity.hideProgressDialog();
                    if (TextUtils.isEmpty(responseString)) {
                        sendError("Server error!");
                    } else {
                        sendError(responseString);
                    }
                }
            });
        }
    }

    private void sendResult(boolean haveUpdates) {
        if (callback != null) {
            callback.onSuccess(haveUpdates);
        }
    }

    private void sendError(String error) {
        if (callback != null) {
            callback.onFailed(error);
        }
    }
}
