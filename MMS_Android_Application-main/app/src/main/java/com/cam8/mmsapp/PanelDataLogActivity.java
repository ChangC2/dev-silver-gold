package com.cam8.mmsapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;

import com.cam8.mmsapp.model.PanelDataLogInfo;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.utils.DateUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class PanelDataLogActivity extends BaseActivity implements View.OnClickListener {

    Calendar calDateTime;

    TextView tvDate;
    TextView tvTime;
    RadioButton optionPass;
    RadioButton optionFail;
    EditText tvGrade;

    EditText tvPhosed1;
    EditText tvStriped1;
    EditText tvMg1;

    EditText tvPhosed2;
    EditText tvStriped2;
    EditText tvMg2;

    EditText tvPhosed3;
    EditText tvStriped3;
    EditText tvMg3;

    TextView tvAverage;

    PanelDataLogInfo panelDataLogInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide Status Bar
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(R.layout.activity_paneldatalog);

        // Set Title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Panel Data Log");

        panelDataLogInfo = new PanelDataLogInfo();
        panelDataLogInfo.loadDataFrom(appSettings.getDataLogInfo());

        calDateTime = Calendar.getInstance();

        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvDate.setText(DateUtil.toStringFormat_13(calDateTime.getTime()));
        tvTime.setText(DateUtil.toStringFormat_10(calDateTime.getTime()));

        optionPass = findViewById(R.id.optionPass);
        optionFail = findViewById(R.id.optionFail);

        tvGrade = findViewById(R.id.tvGrade);

        tvPhosed1 = findViewById(R.id.tvPhosed1);
        tvStriped1 = findViewById(R.id.tvStriped1);
        tvMg1 = findViewById(R.id.tvMg1);

        tvPhosed2 = findViewById(R.id.tvPhosed2);
        tvStriped2 = findViewById(R.id.tvStriped2);
        tvMg2 = findViewById(R.id.tvMg2);

        tvPhosed3 = findViewById(R.id.tvPhosed3);
        tvStriped3 = findViewById(R.id.tvStriped3);
        tvMg3 = findViewById(R.id.tvMg3);

        tvAverage = findViewById(R.id.tvAverage);

        findViewById(R.id.btnSave).setOnClickListener(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                float fMg1 = 0;
                try{
                    fMg1 = Float.parseFloat(tvMg1.getText().toString().trim());
                } catch (Exception e) {e.printStackTrace();}

                float fMg2 = 0;
                try{
                    fMg2 = Float.parseFloat(tvMg2.getText().toString().trim());
                } catch (Exception e) {e.printStackTrace();}

                float fMg3 = 0;
                try{
                    fMg3 = Float.parseFloat(tvMg3.getText().toString().trim());
                } catch (Exception e) {e.printStackTrace();}

                tvAverage.setText(String.format("%.1f", (fMg1 + fMg2 + fMg3) / 3));
            }
        };

        tvMg1.addTextChangedListener(textWatcher);
        tvMg2.addTextChangedListener(textWatcher);
        tvMg3.addTextChangedListener(textWatcher);

        if (!TextUtils.isEmpty(panelDataLogInfo.getDate())) {
            tvDate.setText(panelDataLogInfo.getDate());
        }
        if (!TextUtils.isEmpty(panelDataLogInfo.getTime())) {
            tvTime.setText(panelDataLogInfo.getTime());
        }

        optionPass.setSelected(panelDataLogInfo.getBreak_pass_fail().equalsIgnoreCase("Pass"));
        optionFail.setSelected(panelDataLogInfo.getBreak_pass_fail().equalsIgnoreCase("Fail"));
        tvGrade.setText(panelDataLogInfo.getGrade());

        tvPhosed1.setText(panelDataLogInfo.getPhose1());
        tvStriped1.setText(panelDataLogInfo.getStriped1());
        tvMg1.setText(panelDataLogInfo.getMg1());

        tvPhosed2.setText(panelDataLogInfo.getPhose2());
        tvStriped2.setText(panelDataLogInfo.getStriped2());
        tvMg2.setText(panelDataLogInfo.getMg2());

        tvPhosed3.setText(panelDataLogInfo.getPhose3());
        tvStriped3.setText(panelDataLogInfo.getStriped3());
        tvMg3.setText(panelDataLogInfo.getMg3());

        tvAverage.setText(panelDataLogInfo.getAverage());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnSave) {
            saveDataSheet();
        } else if (viewId == R.id.tvDate) {
            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                    calDateTime.set(Calendar.YEAR, year);
                                    calDateTime.set(Calendar.MONTH, monthOfYear);
                                    calDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    tvDate.setText(DateUtil.toStringFormat_13(calDateTime.getTime()));
                                }
                            },
                            calDateTime.get(Calendar.YEAR),
                            calDateTime.get(Calendar.MONTH),
                            calDateTime.get(Calendar.DAY_OF_MONTH));
            //datePickerDialog.getDatePicker().setMinDate(minDateCalendar.getTime().getTime());
            datePickerDialog.show();
        } else if (viewId == R.id.tvTime) {
            TimePickerDialog timePickerDialog =
                    new TimePickerDialog(mContext,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                    calDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calDateTime.set(Calendar.MINUTE, minute);

                                    tvTime.setText(DateUtil.toStringFormat_10(calDateTime.getTime()));
                                }
                            },
                            calDateTime.get(Calendar.HOUR_OF_DAY),
                            calDateTime.get(Calendar.MINUTE),
                            false);
            timePickerDialog.show();
        }
    }

    private void saveDataSheet() {
        String date = tvDate.getText().toString().trim();
        String time = tvTime.getText().toString().trim();
        String grade = tvGrade.getText().toString().trim();

        String phosed1 = tvPhosed1.getText().toString().trim();
        String striped1 = tvStriped1.getText().toString().trim();
        String mg1 = tvMg1.getText().toString().trim();

        String phosed2 = tvPhosed2.getText().toString().trim();
        String striped2 = tvStriped2.getText().toString().trim();
        String mg2 = tvMg2.getText().toString().trim();

        String phosed3 = tvPhosed3.getText().toString().trim();
        String striped3 = tvStriped3.getText().toString().trim();
        String mg3 = tvMg3.getText().toString().trim();

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(grade)) {
            showToastMessage("Please input fields.");
            return;
        }

        if (TextUtils.isEmpty(phosed1) || TextUtils.isEmpty(striped1) || TextUtils.isEmpty(mg1)) {
            showToastMessage("Please input Panel-1 fields.");
            return;
        }

        if (TextUtils.isEmpty(phosed2) || TextUtils.isEmpty(striped2) || TextUtils.isEmpty(mg2)) {
            showToastMessage("Please input Panel-2 fields.");
            return;
        }

        if (TextUtils.isEmpty(phosed3) || TextUtils.isEmpty(striped3) || TextUtils.isEmpty(mg3)) {
            showToastMessage("Please input Panel-3 fields.");
            return;
        }

        panelDataLogInfo.setDate(date);
        panelDataLogInfo.setTime(time);
        panelDataLogInfo.setBreak_pass_fail(optionPass.isChecked() ? "Pass": "Fail");
        panelDataLogInfo.setGrade(grade);

        panelDataLogInfo.setPhose1(phosed1);
        panelDataLogInfo.setPhose2(phosed2);
        panelDataLogInfo.setPhose3(phosed3);

        panelDataLogInfo.setStriped1(striped1);
        panelDataLogInfo.setStriped2(striped2);
        panelDataLogInfo.setStriped3(striped3);

        panelDataLogInfo.setMg1(mg1);
        panelDataLogInfo.setMg2(mg2);
        panelDataLogInfo.setMg3(mg3);

        panelDataLogInfo.setAverage(tvAverage.getText().toString());
        panelDataLogInfo.setNotes("");

        appSettings.setDataLogInfo(panelDataLogInfo.toString());

        finish();

        // Save Panel Data Log, but we use another way to save all Stage and Panel Logs at the same time.
        /*RequestParams requestParams = new RequestParams();
        requestParams.put("date", date);
        requestParams.put("time", time);
        requestParams.put("customer_id", appSettings.getCustomerID());
        requestParams.put("machine_id", appSettings.getMachineName());
        requestParams.put("operator", appSettings.getUserName());

        Date currTime = new Date();
        requestParams.put("datetime", DateUtil.toStringFormat_20(currTime));
        requestParams.put("timestamp", currTime.getTime());

        requestParams.put("break_pass_fail", optionPass.isChecked() ? "Pass": "Fail");
        requestParams.put("grade", grade);
        requestParams.put("phose1", phosed1);
        requestParams.put("phose2", phosed2);
        requestParams.put("phose3", phosed3);

        requestParams.put("striped1", striped1);
        requestParams.put("striped2", striped2);
        requestParams.put("striped3", striped3);

        requestParams.put("mg1", mg1);
        requestParams.put("mg2", mg2);
        requestParams.put("mg3", mg3);

        requestParams.put("average", tvAverage.getText().toString());
        requestParams.put("notes", "");

        showProgressDialog();
        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "postPlanDataLog", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (weakReference != null && weakReference.get() != null) {
                    weakReference.get().hideProgressDialog();
                }

                // If the response is JSONObject instead of expected JSONArray
                Log.e("DateSheetInfo", response.toString());
                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        showToastMessage("Success to save!");
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = "Please check network connection!";
                        }

                        showToastMessage(message);
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
                    showToastMessage("Please check network connection!");
                } else {
                    showToastMessage(responseString);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if (weakReference != null && weakReference.get() != null) {
                    weakReference.get().hideProgressDialog();
                }

                if (TextUtils.isEmpty(throwable.getMessage())) {
                    showToastMessage("Please check network connection!");
                } else {
                    showToastMessage(throwable.getMessage());
                }
            }
        });*/

    }

}
