package com.cam8.mmsapp.report;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.cam8.mmsapp.BaseActivity;
import com.cam8.mmsapp.MyApplication;
import com.cam8.mmsapp.network.APIServerUtil;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.utils.DateUtil;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.ref.WeakReference;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class CheckAppUpdateHelper {

    public interface onVersionCheckCallback {
        void onFailed(String message);

        void onSuccess(boolean haveUpdates, String newVersion);
    }

    Context context;
    WeakReference<BaseActivity> weakReference;
    MyApplication application;

    onVersionCheckCallback callback;

    public CheckAppUpdateHelper(Context _context, onVersionCheckCallback _callback) {
        context = _context;
        application = (MyApplication) context.getApplicationContext();
        callback = _callback;

        try {
            if (_context instanceof BaseActivity) {
                weakReference = new WeakReference<>((BaseActivity) _context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {

        PackageInfo pInfo = null;
        int curVerionCode = 0;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = pInfo.versionName;

            int curMajor = Integer.parseInt(version.substring(0, version.indexOf(".")));
            int curMinor = Integer.parseInt(version.substring(version.indexOf(".") + 1));
            curVerionCode = curMajor * 1000 + curMinor;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            sendError("Couldn't check app updates!");
            return;
        }

        int finalCurVerionCode = curVerionCode;
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceId", application.getAndroidId());
        requestParams.put("date", DateUtil.toStringFormat_22(new Date()));

        if (weakReference != null && weakReference.get() != null) {
            weakReference.get().showProgressDialog();
        }

        GoogleCertProvider.install(context);
        //ITSRestClient.getFullUrl("https://new.slymms.com/Terminals/appMMS.ver", requestParams, new TextHttpResponseHandler() {
        //ITSRestClient.getFullUrl("https://slymms.com/Terminals/appMMS.ver", requestParams, new TextHttpResponseHandler() {
        //String versionCheckURL = MyApplication.TEST_VERSION ? "https://api.slymms.com/Terminals/appMMSTest.ver" : "https://api.slymms.com/Terminals/appMMS.ver";

        String versionCheckURL = APIServerUtil.getUrl(context, APIServerUtil.APIType.updatePackage, MyApplication.TEST_VERSION ? "appMMSTest.ver" : "appMMS.ver");

        ITSRestClient.getFullUrl(versionCheckURL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                if (weakReference != null && weakReference.get() != null) {
                    weakReference.get().hideProgressDialog();
                }

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
                    sendResult(true, response);
                } else {
                    sendResult(false, "");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (weakReference != null && weakReference.get() != null) {
                    weakReference.get().hideProgressDialog();
                }

                if (TextUtils.isEmpty(responseString)) {
                    sendError("Server error!");
                } else {
                    sendError(responseString);
                }
            }
        });
    }

    private void sendResult(boolean haveUpdates, String newVer) {
        if (callback != null) {
            callback.onSuccess(haveUpdates, newVer);
        }
    }

    private void sendError(String error) {
        if (callback != null) {
            callback.onFailed(error);
        }
    }
}
