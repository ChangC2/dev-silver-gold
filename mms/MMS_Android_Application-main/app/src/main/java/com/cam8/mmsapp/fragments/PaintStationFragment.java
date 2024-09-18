/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.cam8.mmsapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cam8.mmsapp.R;
import com.cam8.mmsapp.alarm.AlarmSettings;
import com.cam8.mmsapp.model.DatabaseHelper;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.utils.DateUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class PaintStationFragment extends BaseFragment implements View.OnClickListener {

    // Part ID
    TextView tvPartID;

    // User Information
    ImageView ivAvatar;
    TextView tvUserName;

    // Time Information
    Button btnTimeProcessing;

    // Sensor values
    TextView tvAmbientTemp;
    TextView tvAmbientHumidity;
    TextView tvAmbientDewPoint;
    TextView tvPaintTemp;
    TextView tvPaintHumidity;
    TextView tvPaintDewPoint;

    // Blast Media Usage
    TextView tvBefore;
    TextView tvAfter;
    TextView tvUsed;

    // Goods and Bad
    TextView tvGood;
    TextView tvBad;

    ImageLoader imageLoader;
    DisplayImageOptions imageOptionsWithUserAvatar;

    // Processing time
    private boolean isTimeCounting = false;
    private long timeCurrentMilis = 0;
    private long timePrevStatusMilis = 0;
    private long elapsedSeconds = 0;

    // Sensor Data Time
    private long timeLastTemperatureData = 0;

    private float ambientTemp = 0;
    private float ambientHumidity = 0;
    private float ambientDewP = 0;
    private float boothTemp = 0;
    private float boothHumidity = 0;
    private float boothDewP = 0;

    DatabaseHelper dbHelper;

    public static PaintStationFragment newInstance(String text) {
        PaintStationFragment mFragment = new PaintStationFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paintstation, container, false);

        TAG = "paintstation";

        init(getArguments());

        initLayout(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initLayout(View parentView) {

        // Init Image Loader
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        imageOptionsWithUserAvatar = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.profile)
                .showImageOnFail(R.drawable.profile)
                .showImageForEmptyUri(R.drawable.profile)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        // Part ID
        tvPartID = parentView.findViewById(R.id.tvPartID);
        parentView.findViewById(R.id.btnLoginPart).setOnClickListener(this);
        parentView.findViewById(R.id.btnLogoutPart).setOnClickListener(this);

        // User Info
        ivAvatar = parentView.findViewById(R.id.ivAvatar);
        tvUserName = parentView.findViewById(R.id.tvUserName);
        parentView.findViewById(R.id.btnScanUser).setOnClickListener(this);
        parentView.findViewById(R.id.btnLogoutUser).setOnClickListener(this);

        // Time Processing
        btnTimeProcessing = parentView.findViewById(R.id.btnTimeProcessing);
        parentView.findViewById(R.id.btnTimerStart).setOnClickListener(this);
        parentView.findViewById(R.id.btnTimerStop).setOnClickListener(this);


        // Sensor
        tvAmbientTemp = parentView.findViewById(R.id.tvAmbientTemp);
        tvAmbientHumidity = parentView.findViewById(R.id.tvAmbientHumidity);
        tvAmbientDewPoint = parentView.findViewById(R.id.tvAmbientDewPoint);
        tvPaintTemp = parentView.findViewById(R.id.tvPaintTemp);
        tvPaintHumidity = parentView.findViewById(R.id.tvPaintHumidity);
        tvPaintDewPoint = parentView.findViewById(R.id.tvPaintDewPoint);

        tvAmbientTemp.setOnClickListener(this);
        tvAmbientHumidity.setOnClickListener(this);
        tvAmbientDewPoint.setOnClickListener(this);
        tvPaintTemp.setOnClickListener(this);
        tvPaintHumidity.setOnClickListener(this);
        tvPaintDewPoint.setOnClickListener(this);

        // Blast Media Usage
        tvBefore = parentView.findViewById(R.id.tvBefore);
        tvAfter = parentView.findViewById(R.id.tvAfter);
        tvUsed = parentView.findViewById(R.id.tvUsed);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                float beforeBlast = 0;
                try{
                    beforeBlast = Float.parseFloat(tvBefore.getText().toString().trim());
                } catch (Exception e) {e.printStackTrace();}

                float afterBlast = 0;
                try{
                    afterBlast = Float.parseFloat(tvAfter.getText().toString().trim());
                } catch (Exception e) {e.printStackTrace();}

                tvUsed.setText(String.format("%.1f", beforeBlast - afterBlast));
            }
        };

        tvBefore.addTextChangedListener(textWatcher);
        tvAfter.addTextChangedListener(textWatcher);


        // Goods and Bads Part
        tvGood = parentView.findViewById(R.id.tvGood);
        tvGood.setOnClickListener(this);
        parentView.findViewById(R.id.iv_good_down).setOnClickListener(this);
        parentView.findViewById(R.id.iv_good_up).setOnClickListener(this);

        tvBad = parentView.findViewById(R.id.tvBad);
        tvBad.setOnClickListener(this);
        parentView.findViewById(R.id.iv_bad_down).setOnClickListener(this);
        parentView.findViewById(R.id.iv_bad_up).setOnClickListener(this);

        // Temperature Update Handler
        mTempDataHandler.sendEmptyMessage(0);

        parentView.findViewById(R.id.panelPaintStation).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        showUserInfo();
        showPartsCountInfo();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    Handler mTempDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            showTemperatureData();

            getTemperatureData();

            mTempDataHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    private void getTemperatureData() {

        long currTime = System.currentTimeMillis();
        if (Math.abs(currTime - timeLastTemperatureData) < 300000) { // 5mins = 5 * 60 * 1000
            // Get Temperature Data per mins
            return;
        }

        timeLastTemperatureData = currTime;

        RequestParams requestParams = new RequestParams();
        requestParams.put("customerId", /*"faxon"*/appSettings.getCustomerID());
        requestParams.put("activeMin", "20");

        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "getTankTemperature", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("Temperature", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        // User Information is exist
                        JSONArray sensorsData = response.getJSONArray("sensors");
                        for (int i = 0; i < sensorsData.length(); i++) {
                            JSONObject sensorData = sensorsData.getJSONObject(i);

                            String sensorName = sensorData.optString("sensor_name");

                            if (sensorName.equals("Paint Booth - Internal")) {
                                ambientTemp = (float) sensorData.optDouble("value1");
                                ambientHumidity = (float) sensorData.optDouble("value2");
                                ambientDewP = (float) sensorData.optDouble("value3");
                            } else if (sensorName.equals("Paint Booth - Ambient")) {
                                boothTemp = (float) sensorData.optDouble("value1");
                                boothHumidity = (float) sensorData.optDouble("value2");
                                boothDewP = (float) sensorData.optDouble("value3");
                            }
                        }
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = "No Temperature Data!";
                        }

                        showToastMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToastMessage(e.getMessage());
                }

                showTemperatureData();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                hideProgressDialog();
                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                //showAlert(errorMsg);
                Log.e("Temperature", errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                //showAlert(errorMsg);
                Log.e("Temperature", errorMsg);
            }
        });
    }

    private void showTemperatureData() {
        tvAmbientTemp.setText(String.format("%.1f °F", ambientTemp));
        tvAmbientHumidity.setText(String.format("%.1f %%", ambientHumidity));
        tvAmbientDewPoint.setText(String.format("%.1f °F", ambientDewP));

        tvPaintTemp.setText(String.format("%.1f °F", boothTemp));
        tvPaintHumidity.setText(String.format("%.1f %%", boothHumidity));
        tvPaintDewPoint.setText(String.format("%.1f °F", boothDewP));
    }

    private void showUserInfo() {
        tvUserName.setText(appSettings.getUserName());
        imageLoader.displayImage(appSettings.getUserAvatar(), ivAvatar, imageOptionsWithUserAvatar);
    }

    private void showPartsCountInfo() {
        tvGood.setText(String.valueOf(appSettings.getShiftGoodParts()));
        tvBad.setText(String.valueOf(appSettings.getShiftBadParts()));
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        // Part ID Login / Logout
        if (viewId == R.id.btnLoginPart) {
            getPartID();
        } else if (viewId == R.id.btnLogoutPart) {
            reportTime();

            tvPartID.setText("");

            resetTimer();
            // user Login / Logout
        } else if (viewId == R.id.btnScanUser) {
            gotoLogin();
        } else if (viewId == R.id.btnLogoutUser) {
            logoutUser();
            // Time Start/Stop
        } else if (viewId == R.id.btnTimerStart) {
            if (isTimeCounting) {
                return;
            }

            String partID = tvPartID.getText().toString().trim();
            if (TextUtils.isEmpty(partID)) {
                showToastMessage("Please input Part ID to record times.");
                return;
            }

            timePrevStatusMilis = System.currentTimeMillis();
            isTimeCounting = true;
            mTimerHandler.sendEmptyMessageDelayed(0, 500);

            btnTimeProcessing.setSelected(true);

            // Notify InCycle Status
            Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
            intent.putExtra("STATUS", AlarmSettings.STATUS_DEVICE_STATUS);
            intent.putExtra("INCYCLE", true);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        } else if (viewId == R.id.btnTimerStop) {
            reportTime();

            // Notify UnCat Status
            Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
            intent.putExtra("STATUS", AlarmSettings.STATUS_DEVICE_STATUS);
            intent.putExtra("INCYCLE", false);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        } else if (viewId == R.id.iv_good_down) {
            int good_val = Integer.parseInt(tvGood.getText().toString());
            if (good_val > 0) {
                good_val--;
                tvGood.setText(String.valueOf(good_val));
                appSettings.setShiftGoodParts(good_val);

                // Daily Live Data
                if (appSettings.getGoodParts() > 0) {
                    appSettings.setGoodParts(appSettings.getGoodParts() - 1);
                }
            }

            broadcastPartsCountStatusToParent();
        } else if (viewId == R.id.iv_good_up) {
            int good_val = Integer.parseInt(tvGood.getText().toString());
            good_val++;
            tvGood.setText(String.valueOf(good_val));
            appSettings.setShiftGoodParts(good_val);

            // Daily Live Data
            appSettings.setGoodParts(appSettings.getGoodParts() + 1);

            broadcastPartsCountStatusToParent();
        } else if (viewId == R.id.tvGood) {
            gotoGoodBadPartsInput();
        } else if (viewId == R.id.iv_bad_down) {
            int bad_val = Integer.parseInt(tvBad.getText().toString());
            if (bad_val > 0) {
                bad_val--;
                tvBad.setText(String.valueOf(bad_val));
                appSettings.setShiftBadParts(bad_val);

                // Daily Live Data
                if (appSettings.getBadParts() > 0) {
                    appSettings.setBadParts(appSettings.getBadParts() - 1);
                }

                // Increase Good Part
                /*
                int good_val = Integer.parseInt(tvGood.getText().toString());
                good_val++;
                tvGood.setText(String.valueOf(good_val));
                appSettings.setShiftGoodParts(good_val);

                appSettings.setGoodParts(appSettings.getGoodParts() + 1);
                */
            }

            broadcastPartsCountStatusToParent();
        } else if (viewId == R.id.iv_bad_up) {
            // Increase Bad Part
            int bad_val = Integer.parseInt(tvBad.getText().toString());
            bad_val++;
            tvBad.setText(String.valueOf(bad_val));
            appSettings.setShiftBadParts(bad_val);

            // Daily Live Data
            appSettings.setBadParts(appSettings.getBadParts() + 1);


            // Decrease Good Part
            /*
            int good_val = Integer.parseInt(tvGood.getText().toString());
            if (good_val > 0) {
                good_val--;
                tvGood.setText(String.valueOf(good_val));
                appSettings.setShiftGoodParts(good_val);

                // Daily Live Data
                if (appSettings.getGoodParts() > 0) {
                    appSettings.setGoodParts(appSettings.getGoodParts() - 1);
                }
            }
            */

            broadcastPartsCountStatusToParent();
        } else if (viewId == R.id.tvBad) {
            gotoGoodBadPartsInput();
        } else if (viewId == R.id.tvAmbientTemp || viewId == R.id.tvAmbientHumidity || viewId == R.id.tvAmbientDewPoint ||
                viewId == R.id.tvPaintTemp || viewId == R.id.tvPaintHumidity || viewId == R.id.tvPaintDewPoint) {
            inputSensorData();
        }
    }

    private void inputSensorData() {
        // Temperature Input Channel
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_paintsensor_input, null);
        AlertDialog temperatureInputDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        final EditText edtTemp1 = dialogView.findViewById(R.id.edtTemp1);
        final EditText edtTemp2 = dialogView.findViewById(R.id.edtTemp2);
        final EditText edtTemp3 = dialogView.findViewById(R.id.edtTemp3);
        final EditText edtTemp4 = dialogView.findViewById(R.id.edtTemp4);
        final EditText edtTemp5 = dialogView.findViewById(R.id.edtTemp5);
        final EditText edtTemp6 = dialogView.findViewById(R.id.edtTemp6);

        edtTemp1.setText(String.valueOf(ambientTemp));
        edtTemp2.setText(String.valueOf(ambientHumidity));
        edtTemp3.setText(String.valueOf(ambientDewP));

        edtTemp4.setText(String.valueOf(boothTemp));
        edtTemp5.setText(String.valueOf(boothHumidity));
        edtTemp6.setText(String.valueOf(boothDewP));

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(edtTemp1);
                hideKeyboard(edtTemp2);
                hideKeyboard(edtTemp3);
                hideKeyboard(edtTemp4);
                hideKeyboard(edtTemp5);
                hideKeyboard(edtTemp6);

                temperatureInputDlg.dismiss();
            }
        });

        dialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                temperatureInputDlg.dismiss();

                hideKeyboard(edtTemp1);
                hideKeyboard(edtTemp2);
                hideKeyboard(edtTemp3);
                hideKeyboard(edtTemp4);
                hideKeyboard(edtTemp5);
                hideKeyboard(edtTemp6);

                try {
                    ambientTemp = Float.parseFloat(edtTemp1.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    ambientHumidity = Float.parseFloat(edtTemp2.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    ambientDewP = Float.parseFloat(edtTemp3.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    boothTemp = Float.parseFloat(edtTemp4.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    boothHumidity = Float.parseFloat(edtTemp5.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    boothDewP = Float.parseFloat(edtTemp6.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        temperatureInputDlg.setCanceledOnTouchOutside(false);
        temperatureInputDlg.setCancelable(false);
        temperatureInputDlg.show();
        temperatureInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    // This is Called After get new part ID in the Current Fragment
    @Override
    protected void onPartID(String partID) {
        super.onPartID(partID);

        reportTime();

        tvPartID.setText(partID);

        resetTimer();

        getPartInfo();
    }

    // This is Called After login/logout in the OEE Dashboard(MainActivity)
    @Override
    protected void onUserInfoUpdated() {
        super.onUserInfoUpdated();

        showUserInfo();
    }

    // This is Called After login in the Current Fragment
    @Override
    protected void onLogined() {
        super.onLogined();

        showUserInfo();
        broadcastLoginLogoutStatusToParent();
    }

    // This is Called After logout in the Current Fragment
    @Override
    protected void onLogout() {
        super.onLogout();

        showUserInfo();
        broadcastLoginLogoutStatusToParent();
    }

    @Override
    protected void onPartsCountUpdated() {
        super.onPartsCountUpdated();

        showPartsCountInfo();
    }

    // Manual Input for Goods/Bad Parts
    private void gotoGoodBadPartsInput() {
        // JobID Input Channel
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_parts_input, null);
        AlertDialog partsInputDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        final EditText edtGoodsInput = dialogView.findViewById(R.id.edtGoodsInput);
        final EditText edtBadInput = dialogView.findViewById(R.id.edtBadInput);

        edtGoodsInput.setText(tvGood.getText().toString());
        edtBadInput.setText(tvBad.getText().toString());

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(edtGoodsInput);
                hideKeyboard(edtBadInput);

                partsInputDlg.dismiss();
            }
        });

        dialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                partsInputDlg.dismiss();

                hideKeyboard(edtGoodsInput);
                hideKeyboard(edtBadInput);

                int goodPart = appSettings.getShiftGoodParts();
                int badPart = appSettings.getShiftBadParts();

                int newGoodPart = -1;
                int newBadPart = -1;
                try {
                    newGoodPart = Integer.parseInt(edtGoodsInput.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    newBadPart = Integer.parseInt(edtBadInput.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (newGoodPart > -1 && newBadPart > -1) {
                    // Show Good Value
                    appSettings.setShiftGoodParts(newGoodPart);
                    tvGood.setText(String.valueOf(newGoodPart));

                    // Show Bad Value
                    appSettings.setShiftBadParts(newBadPart);
                    tvBad.setText(String.valueOf(newBadPart));

                    // Daily Live Data
                    appSettings.setGoodParts(newGoodPart - goodPart > 0 ? newGoodPart - goodPart : 0);
                    appSettings.setBadParts(newBadPart - badPart > 0 ? newBadPart - badPart : 0);

                    broadcastPartsCountStatusToParent();
                } else {
                    showToastMessage("Invalid input!");
                }
            }
        });

        partsInputDlg.setCanceledOnTouchOutside(false);
        partsInputDlg.setCancelable(false);
        partsInputDlg.show();
        partsInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    Handler mTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            timeCurrentMilis = System.currentTimeMillis();
            long timeGap = (timeCurrentMilis > timePrevStatusMilis ? timeCurrentMilis - timePrevStatusMilis : 0);

            elapsedSeconds += timeGap;
            btnTimeProcessing.setText(getElapsedTimeMinutesSecondsString(elapsedSeconds));

            timePrevStatusMilis = timeCurrentMilis;

            if (isTimeCounting) {
                mTimerHandler.sendEmptyMessageDelayed(0, 100);
            }
        }
    };

    private void pauseTimer() {
        isTimeCounting = false;
        mTimerHandler.removeMessages(0);
        btnTimeProcessing.setSelected(false);
    }

    private void resetTimer() {
        isTimeCounting = false;
        mTimerHandler.removeMessages(0);

        elapsedSeconds = 0;
        btnTimeProcessing.setText("00:00:00");
        btnTimeProcessing.setSelected(false);

        tvBefore.setText("");
        tvAfter.setText("");
        tvUsed.setText("");
    }

    private void getPartInfo() {
        String partID = tvPartID.getText().toString().trim();
        if (TextUtils.isEmpty(partID)) {
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("customer_id", /*"faxon"*/appSettings.getCustomerID());
        requestParams.put("part_id", partID);

        showProgressDialog();
        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "getPaintStation", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("paintProcTime", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        JSONObject jsonStation = response.getJSONObject("station");

                        long processingTime = jsonStation.optLong("processing_time");

                        elapsedSeconds = processingTime;
                        btnTimeProcessing.setText(getElapsedTimeMinutesSecondsString(elapsedSeconds));

                        tvBefore.setText(jsonStation.optString("bitu_wt_before"));
                        tvAfter.setText(jsonStation.optString("bitu_wt_after"));
                        tvUsed.setText(jsonStation.optString("bitu_used"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToastMessage(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                hideProgressDialog();
                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                //showAlert(errorMsg);
                Log.e("NetErr", errorMsg);

                showAlert("Failed to save Processing Time, please check connection!");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                //showAlert(errorMsg);
                Log.e("NetErr", errorMsg);

                showAlert("Failed to save Processing Time, please check connection!");
            }
        });
    }

    private void reportTime() {
        String partID = tvPartID.getText().toString().trim();
        if (TextUtils.isEmpty(partID)) {
            showToastMessage("Please input Part ID to save times.");
            return;
        }

        float beforeVal = 0;
        try{
            beforeVal = Float.parseFloat(tvBefore.getText().toString().trim());
        } catch (Exception e) {e.printStackTrace();}

        float afterVal = 0;
        try{
            afterVal = Float.parseFloat(tvAfter.getText().toString().trim());
        } catch (Exception e) {e.printStackTrace();}

        float usedVal = 0;
        try{
            usedVal = Float.parseFloat(tvUsed.getText().toString().trim());
        } catch (Exception e) {e.printStackTrace();}

        if (isTimeCounting && elapsedSeconds > 0) {
            RequestParams requestParams = new RequestParams();
            Date date = new Date();
            requestParams.put("created_at", DateUtil.toStringFormat_20(date));
            requestParams.put("timestamp", date.getTime());
            requestParams.put("date", DateUtil.toStringFormat_1(date));
            requestParams.put("time", DateUtil.toStringFormat_23(date));

            requestParams.put("customer_id", /*"faxon"*/appSettings.getCustomerID());
            requestParams.put("machine_id", appSettings.getMachineName());
            requestParams.put("operator", appSettings.getUserName());

            requestParams.put("part_id", partID);
            requestParams.put("processing_time", elapsedSeconds);
            requestParams.put("bitu_wt_before", beforeVal);
            requestParams.put("bitu_wt_after", afterVal);
            requestParams.put("bitu_used", usedVal);

            requestParams.put("ambient_temp", ambientTemp);
            requestParams.put("ambient_humidity", ambientHumidity);
            requestParams.put("ambient_dewpoint", ambientDewP);

            requestParams.put("paintbooth_temp", boothTemp);
            requestParams.put("paintbooth_humidity", boothHumidity);
            requestParams.put("paintbooth_dewpoint", boothDewP);

            GoogleCertProvider.install(mContext);
            ITSRestClient.post(mContext, "postPaintStation", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    hideProgressDialog();
                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("paintProcTime", response.toString());

                    try {
                        if (response.has("status") && response.getBoolean("status")) {

                            showToastMessage("Success to report time!");
                        } else {
                            String message = "";
                            if (response.has("message") && !response.isNull("message")) {
                                message = response.getString("message");
                            }

                            if (TextUtils.isEmpty(message)) {
                                message = "Failed to save Processing Time, please check connection!";
                            }

                            showAlert(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToastMessage(e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);

                    hideProgressDialog();
                    String errorMsg = throwable.getMessage();
                    if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                        errorMsg = getString(R.string.error_connection_timeout);
                    }

                    //showAlert(errorMsg);
                    Log.e("NetErr", errorMsg);

                    showAlert("Failed to save Processing Time, please check connection!");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    hideProgressDialog();

                    String errorMsg = throwable.getMessage();
                    if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                        errorMsg = getString(R.string.error_connection_timeout);
                    }

                    //showAlert(errorMsg);
                    Log.e("NetErr", errorMsg);

                    showAlert("Failed to save Processing Time, please check connection!");
                }
            });

            // Record part id in the current shift data
            notifyPartID(partID);

            pauseTimer();
        }
    }
}
