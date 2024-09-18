package com.cam8.mmsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cam8.mmsapp.alarm.AlarmSettings;
import com.cam8.mmsapp.model.DatabaseHelper;
import com.cam8.mmsapp.model.TankTemperatureData;
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

public class TimeReportActivity extends BaseActivity implements View.OnClickListener {

    TextView tvMachineName;
    TextView tvMachineCateStatus;
    ImageView ivCustomerAvatar;
    TextView txtPLCConnStatus;
    ImageView ivPLCConnStatus;
    TextView txtServerConnStatus;
    ImageView ivServerConnStatus;

    ImageLoader imageLoader;
    DisplayImageOptions imageOptionsWithCustomerAvatar;

    TextView tvPartID;
    TextView[] tvTempData = new TextView[8];
    Button[] btnTimeStatus = new Button[9];
    Button btnTimerStart;
    Button btnTimerStop;

    TextView tvDate, tvSyncTime, tvUnCatTime, tvIdleTime;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            // Hide status bar
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // Keep Screen On, already done in xml
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_timereport);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tvMachineName = findViewById(R.id.txtMachineName);
        tvMachineCateStatus = findViewById(R.id.txtMachineCateStatus);

        ivCustomerAvatar = findViewById(R.id.ivCustomerAvatar);

        // PLC Connection Status
        txtPLCConnStatus = findViewById(R.id.txtPLCConnStatus);
        ivPLCConnStatus = findViewById(R.id.ivPLCConnStatus);

        // Server Connection Status
        txtServerConnStatus = findViewById(R.id.txtServerConnStatus);
        ivServerConnStatus = findViewById(R.id.ivServerConnStatus);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        imageOptionsWithCustomerAvatar = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.ic_logo_small)
                .showImageOnFail(R.drawable.ic_logo_small)
                .showImageForEmptyUri(R.drawable.ic_logo_small)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        tvMachineName.setText(appSettings.getMachineName());
        imageLoader.displayImage(appSettings.getCustomerAvatar(), ivCustomerAvatar, imageOptionsWithCustomerAvatar);


        // Part ID
        tvPartID = findViewById(R.id.tvPartID);
        tvPartID.setOnClickListener(this);

        // Temperature Data and Value Labels
        tankTemperatureData = mMyApp.getTankTemperatureData();

        tvTempData[0] = findViewById(R.id.tvTemp1);
        tvTempData[1] = findViewById(R.id.tvTemp2);
        tvTempData[2] = findViewById(R.id.tvTemp3);
        tvTempData[3] = findViewById(R.id.tvTemp4);
        tvTempData[4] = findViewById(R.id.tvTemp5);
        tvTempData[5] = findViewById(R.id.tvTemp6);
        tvTempData[6] = findViewById(R.id.tvTemp7);
        tvTempData[7] = findViewById(R.id.tvTemp8);

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

        btnTimeStatus[0] = findViewById(R.id.btnTime1);
        btnTimeStatus[1] = findViewById(R.id.btnTime2);
        btnTimeStatus[2] = findViewById(R.id.btnTime3);
        btnTimeStatus[3] = findViewById(R.id.btnTime4);
        btnTimeStatus[4] = findViewById(R.id.btnTime5);
        btnTimeStatus[5] = findViewById(R.id.btnTime6);
        btnTimeStatus[6] = findViewById(R.id.btnTime7);
        btnTimeStatus[7] = findViewById(R.id.btnTime8);
        btnTimeStatus[8] = findViewById(R.id.btnTimeOven);

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
        btnTimerStart = findViewById(R.id.btnTimerStart);
        btnTimerStop = findViewById(R.id.btnTimerStop);
        btnTimerStart.setOnClickListener(this);
        btnTimerStop.setOnClickListener(this);

        tvDate = findViewById(R.id.tvDate);
        tvSyncTime = findViewById(R.id.tvSyncTime);
        tvUnCatTime = findViewById(R.id.tvUnCatTime);
        tvIdleTime = findViewById(R.id.tvIdleTime);

        dbHelper = new DatabaseHelper(mContext);

        // Temperature Update Handler
        mTempDataHandler.sendEmptyMessage(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mTempDataHandler.removeMessages(0);
        mTimerHandler.removeMessages(0);

        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    public void onBackPressed() {
        if (isTimeCounting) {
            showToastMessage("Timer is running!");
            return;
        } else {
            super.onBackPressed();
        }
    }

    Handler mTempDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // Current Status
            tvMachineCateStatus.setText(tankTemperatureData.getMachineStatus());

            // PLC Connect Status
            if (tankTemperatureData.isConnPLCStatus()) {
                txtPLCConnStatus.setText("Connected to PLC");
                ivPLCConnStatus.setImageResource(R.drawable.gradient_circle_on);
            } else {
                txtPLCConnStatus.setText("PLC Disconnected");
                ivPLCConnStatus.setImageResource(R.drawable.gradient_circle_off);
            }

            // Server Connect Status
            if (tankTemperatureData.isConnServerStatus()) {
                txtServerConnStatus.setText("Connected to Server");
                ivServerConnStatus.setImageResource(R.drawable.gradient_circle_on);
            } else {
                txtServerConnStatus.setText("Server Disconnected");
                ivServerConnStatus.setImageResource(R.drawable.gradient_circle_off);
            }

            // Time Values
            tvSyncTime.setText(getElapsedTimeMinutesSecondsString(tankTemperatureData.getTimeInCycle()));
            tvUnCatTime.setText(getElapsedTimeMinutesSecondsString(tankTemperatureData.getTimeUnCate()));
            tvIdleTime.setText(getElapsedTimeMinutesSecondsString(tankTemperatureData.getTimeIdle()));

            // Update Current Date
            tvDate.setText(getDateTime());

            showTemperatureData();

            if (appSettings.getTemperatureDataSource() == 1) {
                // If Temperature Data Source is IIot Link, get temperature data
                getTemperatureData();
            }

            mTempDataHandler.sendEmptyMessageDelayed(0, 500);
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
        if (Math.abs(currTime - timeLastTemperatureData) < 60000) {
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
                Log.e("Login", errorMsg);
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
                Log.e("Login", errorMsg);
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
        if (viewId == R.id.tvPartID) {
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
        }
    }

    private void getPartID() {
        // JobID Input Channel
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_id_select, null);
        AlertDialog idInputDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        TextView tvTitle = dialogView.findViewById(R.id.title);
        tvTitle.setText("Pleae select Input mode");
        dialogView.findViewById(R.id.inputCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idInputDlg.dismiss();
                // Login ID Input Dialog
                showLoginIDCodeInputDialog();
            }
        });
        dialogView.findViewById(R.id.scanCode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                idInputDlg.dismiss();

                if (checkPermissions(mContext, PERMISSION_REQUEST_QRSCAN_STRING, false, PERMISSION_REQUEST_CODE_QRSCAN)) {
                    startActivityForResult(new Intent(mContext, FullScannerActivity.class), REQUEST_SCAN_PARTID);
                }
            }
        });

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                idInputDlg.dismiss();
            }
        });

        idInputDlg.setCanceledOnTouchOutside(false);
        idInputDlg.setCancelable(false);
        idInputDlg.show();
        idInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void showLoginIDCodeInputDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_id_input, null);
        TextView main_title = dialogView.findViewById(R.id.main_title);
        main_title.setText("Part ID Input");

        TextView sub_content = dialogView.findViewById(R.id.sub_content);
        sub_content.setText("Please input Part ID");

        final EditText edtIdInput = dialogView.findViewById(R.id.edtIDInput);

        AlertDialog loginIdInputDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(edtIdInput);

                loginIdInputDlg.dismiss();
            }
        });

        dialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginIdInputDlg.dismiss();

                hideKeyboard(edtIdInput);

                String loginID = edtIdInput.getText().toString().trim();
                if (!TextUtils.isEmpty(loginID)) {
                    setPartID(loginID);
                }
            }
        });

        loginIdInputDlg.setCanceledOnTouchOutside(false);
        loginIdInputDlg.setCancelable(false);
        loginIdInputDlg.show();
        loginIdInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        edtIdInput.requestFocus();
    }

    private void setPartID(String partID) {
        String currPartID = tvPartID.getText().toString().trim();
        if (!partID.equals(currPartID)) {
            tvPartID.setText(partID);
            resetTimers();
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
        currentTimeStatus = DEVICE_TIME_UNCATE;

    }

    private void reportTimes() {
        String partID = tvPartID.getText().toString().trim();
        if (TextUtils.isEmpty(partID)) {
            showToastMessage("Please input Part ID to save times.");
            return;
        }

        if (isTimeCounting && currentTimeStatus == DEVICE_TIMEOVEN) {
            RequestParams requestParams = new RequestParams();
            requestParams.put("customer_id", /*"faxon"*/appSettings.getCustomerID());

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
        }
    }

    private void notifyGoodsSignal() {
        // Notify InCycle Status
        Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
        intent.putExtra("STATUS", AlarmSettings.STATUS_REPORT_TANK_TIME);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Log.e(TAG, "" + requestCode + "-" + resultCode);

            if (requestCode == REQUEST_SCAN_PARTID) {

                setPartID(data.getStringExtra("code"));
            }
        }
    }
}
