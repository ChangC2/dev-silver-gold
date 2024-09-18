package com.cam8.mmsapp.report;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.cam8.mmsapp.AppSettings;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class StatusReporter {
    private static StatusReporter mInstance;

    Context context;
    AppSettings appSettings;


    public static StatusReporter getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new StatusReporter(context.getApplicationContext());
        }
        return mInstance;
    }

    private StatusReporter(Context context) {
        this.context = context;
        this.appSettings = new AppSettings(context);
    }

    public void reportStatus(String status) {
        // Device configure was not done, then return
        if (TextUtils.isEmpty(appSettings.getCustomerID()) || TextUtils.isEmpty(appSettings.getMachineName())) {
            return;
        }

        appSettings.setMachineStatus(status);

        execute();
    }

    public void execute() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("customerId", appSettings.getCustomerID());
        requestParams.put("machineId", appSettings.getMachineName());
        requestParams.put("status", appSettings.getMachineStatus());

        GoogleCertProvider.install(context);
        ITSRestClient.post(context, "setMachineStatus", requestParams, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                // If the response is JSONObject instead of expected JSONArray
                Log.e("setMachineStatus", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // In case of error, try to recall it again.
                /*try {
                    Thread.sleep(3000);
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }
}
