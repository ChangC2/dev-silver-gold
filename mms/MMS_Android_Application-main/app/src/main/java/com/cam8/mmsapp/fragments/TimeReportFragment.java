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

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cam8.mmsapp.PanelCodeWeightActivity;
import com.cam8.mmsapp.R;
import com.cam8.mmsapp.alarm.AlarmSettings;
import com.cam8.mmsapp.model.DatabaseHelper;
import com.cam8.mmsapp.model.TankTemperatureData;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.utils.DateUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.InputStreamEntity;

public class TimeReportFragment extends BaseFragment implements View.OnClickListener {

    public static TimeReportFragment newInstance(String text) {
        TimeReportFragment mFragment = new TimeReportFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    TextView tvPartID;
    EditText tvRMLotNum;

    TextView[] tvTempData = new TextView[8];
    Button[] btnTimeStatus = new Button[9]; // Include oven additionally
    Button btnTimerStart;
    Button btnTimerStop;
    Button btnDataLog;

    private long timeLastTemperatureData = 0;

    private static final int DEVICE_TIME_UNCATE = -1;
    private static final int DEVICE_TIME1 = 0;
    private static final int DEVICE_TIME2 = 1;
    private static final int DEVICE_TIME3 = 2;
    private static final int DEVICE_TIME4 = 3;
    private static final int DEVICE_TIME5 = 4;
    private static final int DEVICE_TIME6 = 5;
    private static final int DEVICE_TIME7 = 6;
    private static final int DEVICE_TIME8 = 7;
    private static final int DEVICE_TIMEOVEN = 8;

    private int currentTimeStatus = DEVICE_TIME_UNCATE;
    private boolean isTimeCounting = false;
    private long timeCurrentMilis = 0;
    private long timePrevStatusMilis = 0;

    long[] elapsedSeconds = new long[9];
    TankTemperatureData tankTemperatureData;

    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timereport, container, false);

        TAG = "timereport";

        init(getArguments());

        initLayout(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initLayout(View parentView) {

        // Record Test Info
        parentView.findViewById(R.id.tvTestInfo).setOnClickListener(this);

        // Part ID
        tvPartID = parentView.findViewById(R.id.tvPartID);
        tvPartID.setOnClickListener(this);

        // RM LOT#
        tvRMLotNum = parentView.findViewById(R.id.tvRMLotNum);

        // Temperature Data and Value Labels
        tankTemperatureData = mMyApp.getTankTemperatureData();

        tvTempData[0] = parentView.findViewById(R.id.tvTemp1);
        tvTempData[1] = parentView.findViewById(R.id.tvTemp2);
        tvTempData[2] = parentView.findViewById(R.id.tvTemp3);
        tvTempData[3] = parentView.findViewById(R.id.tvTemp4);
        tvTempData[4] = parentView.findViewById(R.id.tvTemp5);
        tvTempData[5] = parentView.findViewById(R.id.tvTemp6);
        tvTempData[6] = parentView.findViewById(R.id.tvTemp7);
        tvTempData[7] = parentView.findViewById(R.id.tvTemp8);

        // TIme Button Action
        View.OnClickListener tempTextClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTemperature();
            }
        };

        for (int i = 0; i < 8; i++) {
            tvTempData[i].setTag(i);
            tvTempData[i].setOnClickListener(tempTextClickListener);
        }

        // TIme Button Action
        View.OnClickListener timeButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < 9; i++) {
                    btnTimeStatus[i].setSelected(false);
                }

                v.setSelected(true);
                currentTimeStatus = (int) v.getTag();
            }
        };

        btnTimeStatus[0] = parentView.findViewById(R.id.btnTime1);
        btnTimeStatus[1] = parentView.findViewById(R.id.btnTime2);
        btnTimeStatus[2] = parentView.findViewById(R.id.btnTime3);
        btnTimeStatus[3] = parentView.findViewById(R.id.btnTime4);
        btnTimeStatus[4] = parentView.findViewById(R.id.btnTime5);
        btnTimeStatus[5] = parentView.findViewById(R.id.btnTime6);
        btnTimeStatus[6] = parentView.findViewById(R.id.btnTime7);
        btnTimeStatus[7] = parentView.findViewById(R.id.btnTime8);
        btnTimeStatus[8] = parentView.findViewById(R.id.btnTimeOven);

        btnTimeStatus[0].setTag(DEVICE_TIME1);
        btnTimeStatus[1].setTag(DEVICE_TIME2);
        btnTimeStatus[2].setTag(DEVICE_TIME3);
        btnTimeStatus[3].setTag(DEVICE_TIME4);
        btnTimeStatus[4].setTag(DEVICE_TIME5);
        btnTimeStatus[5].setTag(DEVICE_TIME6);
        btnTimeStatus[6].setTag(DEVICE_TIME7);
        btnTimeStatus[7].setTag(DEVICE_TIME8);
        btnTimeStatus[8].setTag(DEVICE_TIMEOVEN);

        for (int i = 0; i < 9; i++) {
            btnTimeStatus[i].setOnClickListener(timeButtonClickListener);
        }

        // Start/Stop Button
        btnTimerStart = parentView.findViewById(R.id.btnTimerStart);
        btnTimerStop = parentView.findViewById(R.id.btnTimerStop);
        btnDataLog = parentView.findViewById(R.id.btnDataLog);

        btnTimerStart.setOnClickListener(this);
        btnTimerStop.setOnClickListener(this);
        btnDataLog.setOnClickListener(this);

        dbHelper = new DatabaseHelper(mContext);

        // Temperature Update Handler
        mTempDataHandler.sendEmptyMessage(0);

        // To prevent the calls of gantt bar click, add bottom view click action
        parentView.findViewById(R.id.panelTankTimes).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
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

        mTempDataHandler.removeMessages(0);
        mTimerHandler.removeMessages(0);

        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    Handler mTempDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            showTemperatureData();

            if (appSettings.getTemperatureDataSource() == 1) {
                // If Temperature Data Source is IIot Link, get temperature data
                getTemperatureData();
            }

            mTempDataHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    private void inputTemperature() {
        // Temperature Input Channel
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_temperatures_input, null);
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
        final EditText edtTemp7 = dialogView.findViewById(R.id.edtTemp7);
        final EditText edtTemp8 = dialogView.findViewById(R.id.edtTemp8);

        edtTemp1.setText(String.valueOf(tankTemperatureData.getTempInfo(0)));
        edtTemp2.setText(String.valueOf(tankTemperatureData.getTempInfo(1)));
        edtTemp3.setText(String.valueOf(tankTemperatureData.getTempInfo(2)));
        edtTemp4.setText(String.valueOf(tankTemperatureData.getTempInfo(3)));
        edtTemp5.setText(String.valueOf(tankTemperatureData.getTempInfo(4)));
        edtTemp6.setText(String.valueOf(tankTemperatureData.getTempInfo(5)));
        edtTemp7.setText(String.valueOf(tankTemperatureData.getTempInfo(6)));
        edtTemp8.setText(String.valueOf(tankTemperatureData.getTempInfo(7)));

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(edtTemp1);
                hideKeyboard(edtTemp2);
                hideKeyboard(edtTemp3);
                hideKeyboard(edtTemp4);
                hideKeyboard(edtTemp5);
                hideKeyboard(edtTemp6);
                hideKeyboard(edtTemp7);
                hideKeyboard(edtTemp8);

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
                hideKeyboard(edtTemp7);
                hideKeyboard(edtTemp8);

                try {
                    tankTemperatureData.setTempInfo(0, Float.parseFloat(edtTemp1.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    tankTemperatureData.setTempInfo(1, Float.parseFloat(edtTemp2.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    tankTemperatureData.setTempInfo(2, Float.parseFloat(edtTemp3.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    tankTemperatureData.setTempInfo(3, Float.parseFloat(edtTemp4.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    tankTemperatureData.setTempInfo(4, Float.parseFloat(edtTemp5.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    tankTemperatureData.setTempInfo(5, Float.parseFloat(edtTemp6.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    tankTemperatureData.setTempInfo(6, Float.parseFloat(edtTemp7.getText().toString().trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    tankTemperatureData.setTempInfo(7, Float.parseFloat(edtTemp8.getText().toString().trim()));
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

    private void getTemperatureData() {

        long currTime = System.currentTimeMillis();
        if (Math.abs(currTime - timeLastTemperatureData) < 300000) { // 5mins = 5 * 60 * 1000
            // Get Temperature Data per mins
            return;
        }

        timeLastTemperatureData = currTime;

        RequestParams requestParams = new RequestParams();
        requestParams.put("customerId", /*"faxon"*/appSettings.getCustomerID());
        requestParams.put("activeMin", "30");

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
                            if (!sensorName.toLowerCase().contains("tank")) {
                                continue;
                            }

                            sensorName = sensorName.replace("Tank", "").trim();
                            int sensorID = 0;
                            try {
                                sensorID = Integer.parseInt(sensorName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (sensorID > 0 & sensorID <= 8) {
                                tankTemperatureData.setTempInfo(sensorID - 1, (float) sensorData.optDouble("value1"));
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
        for (int i = 0; i < 8; i++) {
            tvTempData[i].setText(String.format("%.1f °F", tankTemperatureData.getTempInfo(i)));
        }
    }

    Handler mTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            timeCurrentMilis = System.currentTimeMillis();
            long timeGap = (timeCurrentMilis > timePrevStatusMilis ? timeCurrentMilis - timePrevStatusMilis : 0);

            if (currentTimeStatus != DEVICE_TIME_UNCATE) {
                elapsedSeconds[currentTimeStatus] += timeGap;
                btnTimeStatus[currentTimeStatus].setText(getElapsedTimeMinutesSecondsString(elapsedSeconds[currentTimeStatus]));
            }

            timePrevStatusMilis = timeCurrentMilis;

            if (isTimeCounting) {
                mTimerHandler.sendEmptyMessageDelayed(0, 100);
            }
        }
    };

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.tvTestInfo) {
            inputPhosphateTestInfo();
        } else if (viewId == R.id.tvPartID) {
            getPartID();
        } else if (viewId == R.id.btnTimerStart) {
            if (isTimeCounting) {
                return;
            }

            String partID = tvPartID.getText().toString().trim();
            if (TextUtils.isEmpty(partID)) {
                showToastMessage("Please input Part ID to record times.");
                return;
            }

            if (currentTimeStatus == DEVICE_TIME_UNCATE) {
                showToastMessage("Please choose tank to start timer.");
                return;
            }

            timePrevStatusMilis = System.currentTimeMillis();
            isTimeCounting = true;
            mTimerHandler.sendEmptyMessageDelayed(0, 500);

            // Notify InCycle Status
            Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
            intent.putExtra("STATUS", AlarmSettings.STATUS_DEVICE_STATUS);
            intent.putExtra("INCYCLE", true);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        } else if (viewId == R.id.btnTimerStop) {
            reportTimes();

            isTimeCounting = false;
            mTimerHandler.removeMessages(0);

            // Notify UnCat Status
            Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
            intent.putExtra("STATUS", AlarmSettings.STATUS_DEVICE_STATUS);
            intent.putExtra("INCYCLE", false);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        } else if (viewId == R.id.btnDataLog) {
            Intent intent = new Intent(mContext, PanelCodeWeightActivity.class);
            startActivity(intent);
        }
    }

    private void inputPhosphateTestInfo() {
        // JobID Input Channel
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_record_testinfo, null);
        AlertDialog idInputDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        final TextView edtDate = dialogView.findViewById(R.id.edtDate);
        final TextView edtTime = dialogView.findViewById(R.id.edtTime);
        final TextView edtWeight = dialogView.findViewById(R.id.edtWeight);
        final RadioButton radioPass = dialogView.findViewById(R.id.radioPass);
        final RadioButton radioFail = dialogView.findViewById(R.id.radioFail);

        final Calendar calendarDate = Calendar.getInstance();
        edtDate.setText(DateUtil.toStringFormat_1(calendarDate.getTime()));
        edtTime.setText(DateUtil.toStringFormat_23(calendarDate.getTime()));

        dialogView.findViewById(R.id.edtDate).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(mContext,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                        calendarDate.set(Calendar.YEAR, year);
                                        calendarDate.set(Calendar.MONTH, monthOfYear);
                                        calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                        edtDate.setText(DateUtil.toStringFormat_1(calendarDate.getTime()));
                                    }
                                },
                                calendarDate.get(Calendar.YEAR),
                                calendarDate.get(Calendar.MONTH),
                                calendarDate.get(Calendar.DAY_OF_MONTH));
                //datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTime().getTime());
                datePickerDialog.show();
            }
        });

        dialogView.findViewById(R.id.edtTime).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog =
                        new TimePickerDialog(mContext,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                        calendarDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendarDate.set(Calendar.MINUTE, minute);

                                        edtTime.setText(DateUtil.toStringFormat_23(calendarDate.getTime()));
                                    }
                                },
                                calendarDate.get(Calendar.HOUR_OF_DAY),
                                calendarDate.get(Calendar.MINUTE),
                                false);
                timePickerDialog.show();
            }
        });

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                idInputDlg.dismiss();
            }
        });

        dialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                RequestParams requestParams = new RequestParams();
                requestParams.put("customer_id", /*"faxon"*/appSettings.getCustomerID());
                requestParams.put("machine_id", appSettings.getMachineName());
                requestParams.put("operator", appSettings.getUserName());

                requestParams.put("part_id", tvPartID.getText().toString().trim());

                Date date = calendarDate.getTime();
                requestParams.put("created_at", DateUtil.toStringFormat_20(date));
                requestParams.put("timestamp", date.getTime());
                requestParams.put("date", DateUtil.toStringFormat_1(date));
                requestParams.put("time", DateUtil.toStringFormat_23(date));

                float weight = 0;
                try {
                    weight = Float.parseFloat(edtWeight.getText().toString().trim());
                } catch (Exception e) {e.printStackTrace();}

                requestParams.put("weight", weight);

                if (radioPass.isChecked()) {
                    requestParams.put("water_break", "Pass");
                } else if (radioFail.isChecked()) {
                    requestParams.put("water_break", "Fail");
                } else {
                    requestParams.put("water_break", "");
                }

                GoogleCertProvider.install(mContext);
                ITSRestClient.post(mContext, "postPhos_test_data", requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        hideProgressDialog();
                        // If the response is JSONObject instead of expected JSONArray
                        Log.e("phosphateInfo", response.toString());

                        try {
                            if (response.has("status") && response.getBoolean("status")) {

                                showToastMessage("Success to save test info!");

                                idInputDlg.dismiss();
                            } else {
                                String message = "";
                                if (response.has("message") && !response.isNull("message")) {
                                    message = response.getString("message");
                                }

                                if (TextUtils.isEmpty(message)) {
                                    message = "Failed to save Data!";
                                }

                                showToastMessage(message);
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

                        showAlert(errorMsg);
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

                        showAlert(errorMsg);
                    }
                });
            }
        });

        idInputDlg.setCanceledOnTouchOutside(false);
        idInputDlg.setCancelable(false);
        idInputDlg.show();
        idInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    protected void onPartID(String partID) {
        super.onPartID(partID);

        String currPartID = tvPartID.getText().toString().trim();
        if (!partID.equals(currPartID)) {
            tvPartID.setText(partID);
            resetTimers();

            getPartInfo();
        }
    }

    private void resetTimers() {
        isTimeCounting = false;
        mTimerHandler.removeMessages(0);

        for (int i = 0; i < 9; i++) {
            elapsedSeconds[i] = 0;
            btnTimeStatus[i].setText("00:00:00");
            btnTimeStatus[i].setSelected(false);
        }

        tvRMLotNum.setText("");

        currentTimeStatus = DEVICE_TIME_UNCATE;
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
        ITSRestClient.post(mContext, "getTankTime", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("tankTime", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        JSONObject jsonStation = response.getJSONObject("tank_time");

                        elapsedSeconds[0] = jsonStation.optLong("ttime1");
                        elapsedSeconds[1] = jsonStation.optLong("ttime2");
                        elapsedSeconds[2] = jsonStation.optLong("ttime3");
                        elapsedSeconds[3] = jsonStation.optLong("ttime4");
                        elapsedSeconds[4] = jsonStation.optLong("ttime5");
                        elapsedSeconds[5] = jsonStation.optLong("ttime6");
                        elapsedSeconds[6] = jsonStation.optLong("ttime7");
                        elapsedSeconds[7] = jsonStation.optLong("ttime8");
                        elapsedSeconds[8] = jsonStation.optLong("toven");

                        for (int i = 0; i<=8; i++) {
                            btnTimeStatus[i].setText(getElapsedTimeMinutesSecondsString(elapsedSeconds[i]));
                        }

                        int rm_lot = jsonStation.optInt("rm_lot");

                        // Get RM Lot#
                        tvRMLotNum.setText(String.valueOf(rm_lot));
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

    private void reportTimes() {
        String partID = tvPartID.getText().toString().trim();
        String rmLot = tvRMLotNum.getText().toString().trim();
        if (TextUtils.isEmpty(partID)) {
            showToastMessage("Please input Part ID to save times.");
            return;
        }

        if (TextUtils.isEmpty(rmLot)) {
            showToastMessage("Please input RM LOT# to save times.");
            return;
        }

        if (isTimeCounting && currentTimeStatus == DEVICE_TIMEOVEN) {
            RequestParams requestParams = new RequestParams();
            requestParams.put("customer_id", /*"faxon"*/appSettings.getCustomerID());
            requestParams.put("machine_id", appSettings.getMachineName());

            Date date = new Date();
            requestParams.put("created_at", DateUtil.toStringFormat_20(date));
            requestParams.put("timestamp", date.getTime());
            requestParams.put("part_id", partID);
            requestParams.put("date", DateUtil.toStringFormat_1(date));
            requestParams.put("time", DateUtil.toStringFormat_23(date));

            requestParams.put("ttime1", elapsedSeconds[0]);
            requestParams.put("ttemp1", tankTemperatureData.getTempInfo(0));
            requestParams.put("ttime2", elapsedSeconds[1]);
            requestParams.put("ttemp2", tankTemperatureData.getTempInfo(1));
            requestParams.put("ttime3", elapsedSeconds[2]);
            requestParams.put("ttemp3", tankTemperatureData.getTempInfo(2));
            requestParams.put("ttime4", elapsedSeconds[3]);
            requestParams.put("ttemp4", tankTemperatureData.getTempInfo(3));
            requestParams.put("ttime5", elapsedSeconds[4]);
            requestParams.put("ttemp5", tankTemperatureData.getTempInfo(4));
            requestParams.put("ttime6", elapsedSeconds[5]);
            requestParams.put("ttemp6", tankTemperatureData.getTempInfo(5));
            requestParams.put("ttime7", elapsedSeconds[6]);
            requestParams.put("ttemp7", tankTemperatureData.getTempInfo(6));
            requestParams.put("ttime8", elapsedSeconds[7]);
            requestParams.put("ttemp8", tankTemperatureData.getTempInfo(7));

            // Extra field
            requestParams.put("toven", elapsedSeconds[8]);
            requestParams.put("operator", appSettings.getUserName());
            requestParams.put("rm_lot", rmLot);

            GoogleCertProvider.install(mContext);
            ITSRestClient.post(mContext, "postTankTime", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    hideProgressDialog();
                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("tankTime", response.toString());

                    try {
                        if (response.has("status") && response.getBoolean("status")) {

                            showToastMessage("Success to report times!");

                            // Send goods signal
                            notifyGoodsSignal();

                            resetTimers();
                        } else {
                            String message = "";
                            if (response.has("message") && !response.isNull("message")) {
                                message = response.getString("message");
                            }

                            if (TextUtils.isEmpty(message)) {
                                message = "No Temperature Data!";
                            }

                            showToastMessage(message);

                            dbHelper.insertTempData(DateUtil.toStringFormat_20(date), date.getTime(), partID, DateUtil.toStringFormat_1(date), DateUtil.toStringFormat_23(date),
                                    elapsedSeconds[0], tankTemperatureData.getTempInfo(0),
                                    elapsedSeconds[1], tankTemperatureData.getTempInfo(1),
                                    elapsedSeconds[2], tankTemperatureData.getTempInfo(2),
                                    elapsedSeconds[3], tankTemperatureData.getTempInfo(3),
                                    elapsedSeconds[4], tankTemperatureData.getTempInfo(4),
                                    elapsedSeconds[5], tankTemperatureData.getTempInfo(5),
                                    elapsedSeconds[6], tankTemperatureData.getTempInfo(6),
                                    elapsedSeconds[7], tankTemperatureData.getTempInfo(7),
                                    elapsedSeconds[8], "");

                            // Send goods signal
                            notifyGoodsSignal();

                            resetTimers();
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

                    dbHelper.insertTempData(DateUtil.toStringFormat_20(date), date.getTime(), partID, DateUtil.toStringFormat_1(date), DateUtil.toStringFormat_23(date),
                            elapsedSeconds[0], tankTemperatureData.getTempInfo(0),
                            elapsedSeconds[1], tankTemperatureData.getTempInfo(1),
                            elapsedSeconds[2], tankTemperatureData.getTempInfo(2),
                            elapsedSeconds[3], tankTemperatureData.getTempInfo(3),
                            elapsedSeconds[4], tankTemperatureData.getTempInfo(4),
                            elapsedSeconds[5], tankTemperatureData.getTempInfo(5),
                            elapsedSeconds[6], tankTemperatureData.getTempInfo(6),
                            elapsedSeconds[7], tankTemperatureData.getTempInfo(7),
                            elapsedSeconds[8], "");

                    // Send goods signal
                    notifyGoodsSignal();

                    resetTimers();

                    showAlert("Success to save in local db!\nThis data will be uploaded when the network is available.");
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

                    dbHelper.insertTempData(DateUtil.toStringFormat_20(date), date.getTime(), partID, DateUtil.toStringFormat_1(date), DateUtil.toStringFormat_23(date),
                            elapsedSeconds[0], tankTemperatureData.getTempInfo(0),
                            elapsedSeconds[1], tankTemperatureData.getTempInfo(1),
                            elapsedSeconds[2], tankTemperatureData.getTempInfo(2),
                            elapsedSeconds[3], tankTemperatureData.getTempInfo(3),
                            elapsedSeconds[4], tankTemperatureData.getTempInfo(4),
                            elapsedSeconds[5], tankTemperatureData.getTempInfo(5),
                            elapsedSeconds[6], tankTemperatureData.getTempInfo(6),
                            elapsedSeconds[7], tankTemperatureData.getTempInfo(7),
                            elapsedSeconds[8], "");

                    // Send goods signal
                    notifyGoodsSignal();

                    resetTimers();

                    showAlert("Success to save in local db!\nThis data will be uploaded when the network is available.");
                }
            });

            // Record part id in the current shift data
            notifyPartID(partID);
        }
    }

    private void notifyGoodsSignal() {
        // Notify InCycle Status
        Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
        intent.putExtra("STATUS", AlarmSettings.STATUS_REPORT_TANK_TIME);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

}
