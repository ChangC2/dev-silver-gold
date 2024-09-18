package com.cam8.mmsapp.report;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.cam8.mmsapp.AppSettings;
import com.cam8.mmsapp.BaseActivity;
import com.cam8.mmsapp.MyApplication;
import com.cam8.mmsapp.R;
import com.cam8.mmsapp.model.FaxonStage1;
import com.cam8.mmsapp.model.FaxonStage2;
import com.cam8.mmsapp.model.FaxonStage3;
import com.cam8.mmsapp.model.FaxonStage4;
import com.cam8.mmsapp.model.FaxonStage5;
import com.cam8.mmsapp.model.FaxonStage6;
import com.cam8.mmsapp.model.FaxonStage7;
import com.cam8.mmsapp.model.FaxonStage8;
import com.cam8.mmsapp.model.FaxonStage9;
import com.cam8.mmsapp.model.FaxonStageBase;
import com.cam8.mmsapp.model.FaxonStageRowInfo;
import com.cam8.mmsapp.model.ShiftTime;
import com.cam8.mmsapp.model.ShiftTimeManager;
import com.cam8.mmsapp.network.APIServerUtil;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.utils.DateUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class StageInfoSaveHelper {

    public interface onStageInfoCallback {
        void onFailed(String message);

        void onSuccess(int id);
    }

    Context context;
    WeakReference<BaseActivity> weakReference;
    MyApplication application;
    FaxonStageBase stageInfo;
    AppSettings appSettings;

    onStageInfoCallback callback;

    public StageInfoSaveHelper(Context _context, onStageInfoCallback _callback, FaxonStageBase _stageInfo) {
        context = _context;
        application = (MyApplication) context.getApplicationContext();
        callback = _callback;
        stageInfo = _stageInfo;

        appSettings = new AppSettings(context);

        try {
            if (_context instanceof BaseActivity) {
                weakReference = new WeakReference<>((BaseActivity) _context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {

        String apiName = stageInfo.getApiName();
        RequestParams requestParams = new RequestParams();
        Date date = new Date();
        requestParams.put("datetime", DateUtil.toStringFormat_20(date));
        requestParams.put("timestamp", date.getTime());

        requestParams.put("customer_id", appSettings.getCustomerID());
        requestParams.put("machine_id", appSettings.getMachineName());
        requestParams.put("operator", appSettings.getUserName());

        // Stage Info Fields
        ArrayList<FaxonStageRowInfo> inputFields = stageInfo.getItemsList();
        for (FaxonStageRowInfo item: inputFields) {
            requestParams.put(item.getApiParam(), item.getResult());
        }
        // Stage Info Notes
        requestParams.put("notes", stageInfo.getNotes());

        if (weakReference != null && weakReference.get() != null) {
            weakReference.get().showProgressDialog();
        }

        GoogleCertProvider.install(context);
        ITSRestClient.post(context, apiName, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (weakReference != null && weakReference.get() != null) {
                    weakReference.get().hideProgressDialog();
                }

                // If the response is JSONObject instead of expected JSONArray
                Log.e("StageInfo", response.toString());
                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        sendResult(response.optInt("id"));
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = "Please check network connection!";
                        }

                        sendError(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (weakReference != null && weakReference.get() != null) {
                    weakReference.get().hideProgressDialog();
                }

                if (TextUtils.isEmpty(responseString)) {
                    sendError("Please check network connection!");
                } else {
                    sendError(responseString);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if (weakReference != null && weakReference.get() != null) {
                    weakReference.get().hideProgressDialog();
                }

                if (TextUtils.isEmpty(throwable.getMessage())) {
                    sendError("Please check network connection!");
                } else {
                    sendError(throwable.getMessage());
                }
            }
        });
    }

    private void sendResult(int recoreID) {
        if (callback != null) {
            callback.onSuccess(recoreID);
        }
    }

    private void sendError(String error) {
        if (callback != null) {
            callback.onFailed(error);
        }
    }
}
