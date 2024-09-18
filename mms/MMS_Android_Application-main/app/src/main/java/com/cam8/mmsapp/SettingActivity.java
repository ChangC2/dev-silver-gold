package com.cam8.mmsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.ViewCompat;

import com.cam8.mmsapp.model.DGTFormula;
import com.cam8.mmsapp.model.ShiftTime;
import com.cam8.mmsapp.model.ShiftTimeManager;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.report.CheckAppUpdateHelper;
import com.cam8.mmsapp.views.timedurationpicker.PickerDialogFragment;
import com.cam8.mmsapp.views.timedurationpicker.TimeDurationUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SettingActivity extends BaseActivity implements View.OnClickListener, PickerDialogFragment.TimeDurationListener {

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:setting";

    ViewGroup settingPanel;

    TextView tvAppVersion;
    Button btnUpdateApp;
    TextView tvVerUpToDate;

    // Modbus TCP information for the modbus PLC, but this is not used.
    EditText tvIP, tvPort, tvMemory;

    // Server Information
    SwitchCompat switchLocalServer;
    View panelLocalIP;
    EditText tvLocalServerIP;

    //Job Information
    SwitchCompat switchJobAutoLogout;

    // InCycle Signal
    RadioButton radioInCyclePLC;
    RadioButton radioInCycleTimeLogger;

    RadioGroup radioGroupPM;
    RadioButton radioPMNone;
    RadioButton radioPMTimeLogger;
    RadioButton radioPMCleaning;
    RadioButton radioPMBlast;
    RadioButton radioPMPaint;
    RadioButton radioPMAssembly137;
    RadioButton radioPMAssembly136;
    RadioButton radioPMAssembly3;
    RadioButton radioPMQuality;

    // Temperature Data
    RadioButton radioTempInput;
    RadioButton radioTempIIot;
    RadioButton radioTempPLC;

    // Machine Information
    EditText tvMachineName;
    TextView tvBTDeviceAddr;
    TextView tvAccountId;
    TextView tvJobId;

    // Downtime information
    EditText edtDownTimeLimit;
    EditText edtDownTimeReason1;
    EditText edtDownTimeReason2;
    EditText edtDownTimeReason3;
    EditText edtDownTimeReason4;
    EditText edtDownTimeReason5;
    EditText edtDownTimeReason6;
    EditText edtDownTimeReason7;
    EditText edtDownTimeReason8;

    // Aux Data Title Settings
    EditText tvAux1Data;
    EditText tvAux2Data;
    EditText tvAux3Data;

    SwitchCompat switchUseCSLock;
    SwitchCompat switchReverseCSLock;
    TextView tvReverseCSLockTitle;
    SwitchCompat switchGuestLock;
    SwitchCompat switchCSLockSound;

    // Stop Time Setting
    TextView tvStopTimeValueInSec;
    TextView tvStopTimeValue;

    // Time Settings
    TextView tvPlannedProductionTime;

    // Shift Planned Production Time Settings
    ArrayList<ShiftTime> shiftTimesInfo;
    TextView tvShift1PPTTitle;
    TextView tvShift1PPTValue;

    TextView tvShift2PPTTitle;
    TextView tvShift2PPTValue;

    TextView tvShift3PPTTitle;
    TextView tvShift3PPTValue;

    // Alert Setting
    SwitchCompat switchAlert;
    EditText tvAlertEmail1;
    EditText tvAlertEmail2;
    EditText tvAlertEmail3;

    // Automatic Parts Counter
    SwitchCompat switchAutoCount;
    TextView tvMinElaspedCycleTime;
    SeekBar seekMinElapsedCycleTime;
    TextView tvPartsPerCycle;

    // Gantt Time Format
    RadioButton radioShowTime24Hours;
    RadioButton radioShowTimeNow;
    RadioButton radioShowDailyGoalChart;

    View panelDGT;
    EditText tvDailyGoalTargetTitle;
    Spinner spinnerDailyGoalFormula;
    ArrayList<DGTFormula> formularDataList = new ArrayList<>();
    ArrayAdapter<DGTFormula> formulaArrayAdapter;

    Spinner spinnerDailyGoalParam;
    EditText tvDailyGoalTargetDisplayUnit;
    RadioButton radioGoalUnit;
    RadioButton radioGoalPercentage;
    EditText tvDailyGoalTargetMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.title_settings);

        settingPanel = findViewById(R.id.settingPanel);
        ViewCompat.setTransitionName(settingPanel, VIEW_NAME_HEADER_IMAGE);

        mMyApp.setAppSettingsStatus(true);

        // App Version information
        tvAppVersion = findViewById(R.id.tvAppVersion);
        tvAppVersion.setText("V" + getVersionName());
        btnUpdateApp = findViewById(R.id.btnUpdateApp);
        btnUpdateApp.setVisibility(View.GONE);
        btnUpdateApp.setOnClickListener(this);

        tvVerUpToDate = findViewById(R.id.tvVerUpToDate);
        tvVerUpToDate.setVisibility(View.GONE);

        findViewById(R.id.btnOpenWIFISettings).setOnClickListener(this);

        // Modbus TCP Settings using WIFI
        //appSettings.setDeviceIP("192.168.1.51");
        //appSettings.setDevicePort(502);
        tvIP = findViewById(R.id.tvIP);
        tvPort = findViewById(R.id.tvPort);
        tvMemory = findViewById(R.id.tvMemory);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) +
                            source.subSequence(start, end) +
                            destTxt.substring(dend);
                    if (!resultingTxt.matches("^\\d{1,3}(\\." +
                            "(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };

        tvIP.setFilters(filters);

        tvIP.setText(appSettings.getDeviceIP());
        tvPort.setText(String.valueOf(appSettings.getDevicePort()));
        tvMemory.setText(String.valueOf(appSettings.getDeviceMemory()));

        // Server Information
        switchLocalServer = findViewById(R.id.switchLocalServer);
        panelLocalIP = findViewById(R.id.panelLocalIP);
        tvLocalServerIP = findViewById(R.id.tvLocalServerIP);
        //tvLocalServerIP.setFilters(filters);
        switchLocalServer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                panelLocalIP.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });

        switchLocalServer.setChecked(appSettings.isUsingLocalServer());
        tvLocalServerIP.setText(appSettings.getLocalServerIP());

        // Job Information
        switchJobAutoLogout = findViewById(R.id.switchJobAutoLogout);
        switchJobAutoLogout.setChecked(appSettings.isUsingJobAutoLogout());

        // InCycle Signal
        radioInCyclePLC = findViewById(R.id.radioInCyclePLC);
        radioInCycleTimeLogger = findViewById(R.id.radioInCycleTimeLogger);
        if (appSettings.isInCycleFromPLC()) {
            radioInCyclePLC.setChecked(true);
        } else {
            radioInCycleTimeLogger.setChecked(true);
        }

        // Process Monitor Type
        radioGroupPM = findViewById(R.id.radioGroupPM);
        radioPMNone = findViewById(R.id.radioPMNone);
        radioPMTimeLogger = findViewById(R.id.radioPMTimeLogger);
        radioPMCleaning = findViewById(R.id.radioPMCleaning);
        radioPMBlast = findViewById(R.id.radioPMBlast);
        radioPMPaint = findViewById(R.id.radioPMPaint);
        radioPMAssembly137 = findViewById(R.id.radioPMAssembly137);
        radioPMAssembly136 = findViewById(R.id.radioPMAssembly136);
        radioPMAssembly3 = findViewById(R.id.radioPMAssembly3);
        radioPMQuality = findViewById(R.id.radioPMQuality);

        int processMonitorType = appSettings.getProcessMonitorType();
        if (processMonitorType == 0) {
            radioPMNone.setChecked(true);
        } else if (processMonitorType == 1) {
            radioPMTimeLogger.setChecked(true);
        } else if (processMonitorType == 2) {
            radioPMCleaning.setChecked(true);
        } else if (processMonitorType == 3) {
            radioPMBlast.setChecked(true);
        } else if (processMonitorType == 4) {
            radioPMPaint.setChecked(true);
        } else if (processMonitorType == 5) {
            radioPMAssembly137.setChecked(true);
        } else if (processMonitorType == 6) {
            radioPMAssembly136.setChecked(true);
        } else if (processMonitorType == 7) {
            radioPMAssembly3.setChecked(true);
        } else if (processMonitorType == 8) {
            radioPMQuality.setChecked(true);
        }

        // Temperature Data
        radioTempInput = findViewById(R.id.radioTempInput);
        radioTempIIot = findViewById(R.id.radioTempIIot);
        radioTempPLC = findViewById(R.id.radioTempPLC);
        int temperatureDataSourceType = appSettings.getTemperatureDataSource();
        if (temperatureDataSourceType == 0) {
            radioTempInput.setChecked(true);
        } else if (temperatureDataSourceType == 1) {
            radioTempIIot.setChecked(true);
        } else {
            radioTempPLC.setChecked(true);
        }

        // Machine and Device information
        tvMachineName = findViewById(R.id.tvMachineName);
        tvBTDeviceAddr = findViewById(R.id.tvBTDeviceAddr);

        tvMachineName.setText(appSettings.getMachineName());
        tvBTDeviceAddr.setText(appSettings.getBTDeviceAddr());

        tvBTDeviceAddr.setOnClickListener(this);

        // Job Id and Customer
        tvJobId = findViewById(R.id.tvJobId);
        tvJobId.setText(appSettings.getJobId());
        findViewById(R.id.btnQRJobId).setOnClickListener(this);

        tvAccountId = findViewById(R.id.tvAccountId);
        tvAccountId.setText(appSettings.getCustomerID());
        findViewById(R.id.tvAccountId).setOnClickListener(this);
        findViewById(R.id.btnCheckCustomerID).setOnClickListener(this);


        // Downtime Reasons
        edtDownTimeReason1 = findViewById(R.id.tvDowntime1);
        edtDownTimeReason2 = findViewById(R.id.tvDowntime2);
        edtDownTimeReason3 = findViewById(R.id.tvDowntime3);
        edtDownTimeReason4 = findViewById(R.id.tvDowntime4);
        edtDownTimeReason5 = findViewById(R.id.tvDowntime5);
        edtDownTimeReason6 = findViewById(R.id.tvDowntime6);
        edtDownTimeReason7 = findViewById(R.id.tvDowntime7);
        edtDownTimeReason8 = findViewById(R.id.tvDowntime8);

        edtDownTimeReason1.setText(appSettings.getDownTimeReason1());
        edtDownTimeReason2.setText(appSettings.getDownTimeReason2());
        edtDownTimeReason3.setText(appSettings.getDownTimeReason3());
        edtDownTimeReason4.setText(appSettings.getDownTimeReason4());
        edtDownTimeReason5.setText(appSettings.getDownTimeReason5());
        edtDownTimeReason6.setText(appSettings.getDownTimeReason6());
        edtDownTimeReason7.setText(appSettings.getDownTimeReason7());
        edtDownTimeReason8.setText(appSettings.getDownTimeReason8());

        // Aux Data Titles
        tvAux1Data = findViewById(R.id.tvAux1Data);
        tvAux2Data = findViewById(R.id.tvAux2Data);
        tvAux3Data = findViewById(R.id.tvAux3Data);
        tvAux1Data.setText(appSettings.getAux1DataTitle());
        tvAux2Data.setText(appSettings.getAux2DataTitle());
        tvAux3Data.setText(appSettings.getAux3DataTitle());

        // CSLock Controls
        switchUseCSLock = findViewById(R.id.switchUseCSLock);
        switchReverseCSLock = findViewById(R.id.switchReverseCSLock);
        tvReverseCSLockTitle = findViewById(R.id.tvReverseCSLockTitle);
        switchGuestLock = findViewById(R.id.switchGuestLock);
        switchCSLockSound = findViewById(R.id.switchCSLockSound);

        switchUseCSLock.setChecked(appSettings.isUseCSLock());
        switchReverseCSLock.setChecked(appSettings.isReverseCSLock());
        if (appSettings.isReverseCSLock()) {
            tvReverseCSLockTitle.setText("CS Lock Normally Closed");
        } else {
            tvReverseCSLockTitle.setText("CS Lock Normally Open");
        }

        switchGuestLock.setChecked(appSettings.isGuestLock());
        switchCSLockSound.setChecked(appSettings.isCSLockSoundEnabled());

        CompoundButton.OnCheckedChangeListener switchCSLockListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == switchUseCSLock) {
                    appSettings.setUseCSLock(isChecked);
                } else if (buttonView == switchReverseCSLock) {
                    appSettings.setReverseCSLock(isChecked);
                    if (appSettings.isReverseCSLock()) {
                        tvReverseCSLockTitle.setText("CS Lock Normally Closed");
                    } else {
                        tvReverseCSLockTitle.setText("CS Lock Normally Open");
                    }
                } else if (buttonView == switchGuestLock) {
                    appSettings.setGuestLock(isChecked);
                } else if (buttonView == switchCSLockSound) {
                    appSettings.setCSLockSoundEnabled(isChecked);
                }
            }
        };

        switchUseCSLock.setOnCheckedChangeListener(switchCSLockListener);
        switchReverseCSLock.setOnCheckedChangeListener(switchCSLockListener);
        switchGuestLock.setOnCheckedChangeListener(switchCSLockListener);
        switchCSLockSound.setOnCheckedChangeListener(switchCSLockListener);

        // Stop Time bar
        tvStopTimeValueInSec = findViewById(R.id.tvStopTimeValueInSec);
        tvStopTimeValue = findViewById(R.id.tvStopTime);
        int stopTime = (int) (appSettings.getStopTimeLimit() / 1000);
        tvStopTimeValueInSec.setText(String.format("Stop Time Limit (%ds)", stopTime));
        tvStopTimeValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(appSettings.getStopTimeLimit()));
        tvStopTimeValue.setOnClickListener(this);

        tvPlannedProductionTime = findViewById(R.id.tvPlannedProductionTime);
        tvPlannedProductionTime.setText(TimeDurationUtil.formatHoursMinutesSeconds(appSettings.getPlannedProductionTime()));
        tvPlannedProductionTime.setOnClickListener(this);


        // Shifts Planned Production Time Settings
        tvShift1PPTTitle = findViewById(R.id.tvShift1PPTTitle);
        tvShift1PPTValue = findViewById(R.id.tvShift1PPTValue);
        tvShift1PPTValue.setOnClickListener(this);

        tvShift2PPTTitle = findViewById(R.id.tvShift2PPTTitle);
        tvShift2PPTValue = findViewById(R.id.tvShift2PPTValue);
        tvShift2PPTValue.setOnClickListener(this);

        tvShift3PPTTitle = findViewById(R.id.tvShift3PPTTitle);
        tvShift3PPTValue = findViewById(R.id.tvShift3PPTValue);
        tvShift3PPTValue.setOnClickListener(this);

        shiftTimesInfo = ShiftTimeManager.getInstance().getShiftTimes();

        if (shiftTimesInfo.size() == 0) {
            tvShift1PPTValue.setText("Not Set");
            tvShift1PPTValue.setEnabled(false);

            tvShift2PPTValue.setText("Not Set");
            tvShift2PPTValue.setEnabled(false);

            tvShift3PPTValue.setText("Not Set");
            tvShift3PPTValue.setEnabled(false);
        } else if (shiftTimesInfo.size() == 1) {
            tvShift1PPTTitle.setText(String.format("Shift1(%s)", shiftTimesInfo.get(0).toString()));
            tvShift1PPTValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(appSettings.getShift1PPT()));

            tvShift2PPTValue.setText("Not Set");
            tvShift2PPTValue.setEnabled(false);

            tvShift3PPTValue.setText("Not Set");
            tvShift3PPTValue.setEnabled(false);
        } else if (shiftTimesInfo.size() == 2) {
            tvShift1PPTTitle.setText(String.format("Shift1(%s)", shiftTimesInfo.get(0).toString()));
            tvShift1PPTValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(appSettings.getShift1PPT()));

            tvShift2PPTTitle.setText(String.format("Shift2(%s)", shiftTimesInfo.get(1).toString()));
            tvShift2PPTValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(appSettings.getShift2PPT()));

            tvShift3PPTValue.setText("Not Set");
            tvShift3PPTValue.setEnabled(false);
        } else if (shiftTimesInfo.size() == 3) {
            tvShift1PPTTitle.setText(String.format("Shift1(%s)", shiftTimesInfo.get(0).toString()));
            tvShift1PPTValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(appSettings.getShift1PPT()));

            tvShift2PPTTitle.setText(String.format("Shift2(%s)", shiftTimesInfo.get(1).toString()));
            tvShift2PPTValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(appSettings.getShift2PPT()));

            tvShift3PPTTitle.setText(String.format("Shift3(%s)", shiftTimesInfo.get(2).toString()));
            tvShift3PPTValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(appSettings.getShift3PPT()));
        }

        // ----------- Alert ----------------------------------------
        switchAlert = findViewById(R.id.switchAlert);
        tvAlertEmail1 = findViewById(R.id.tvAlertEmail1);
        tvAlertEmail2 = findViewById(R.id.tvAlertEmail2);
        tvAlertEmail3 = findViewById(R.id.tvAlertEmail3);
        findViewById(R.id.btnInfoAboutStopAlert).setOnClickListener(this);

        switchAlert.setChecked(appSettings.isCycleStopAlert());
        switchAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSettings.setCycleStopAlert(isChecked);
            }
        });
        tvAlertEmail1.setText(appSettings.getAlertEmail1());
        tvAlertEmail2.setText(appSettings.getAlertEmail2());
        tvAlertEmail3.setText(appSettings.getAlertEmail3());
        // ----------------------------------------------------------

        // Automatic Parts Counter
        switchAutoCount = findViewById(R.id.switchAutoCount);
        tvMinElaspedCycleTime = findViewById(R.id.tvMinElaspedCycleTime);
        seekMinElapsedCycleTime = findViewById(R.id.seekMinElapsedCycleTime);
        tvPartsPerCycle = findViewById(R.id.tvPartsPerCycle);

        switchAutoCount.setChecked(appSettings.isAutomaticPartsCounter());
        switchAutoCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSettings.setAutomaticPartsCounter(isChecked);
            }
        });

        int minStopTime = (int) (appSettings.getMinElapsedStopTime() / 1000);
        tvMinElaspedCycleTime.setText(String.format("Min Elapsed Cycle Time(%ds)", minStopTime));
        seekMinElapsedCycleTime.setProgress(minStopTime);
        seekMinElapsedCycleTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    tvMinElaspedCycleTime.setText(String.format("Min Elapsed Cycle Time(%ds)", progress));
                    appSettings.setMinElapsedStopTime(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        tvPartsPerCycle.setText(String.valueOf(appSettings.getPartsPerCycle()));
        tvPartsPerCycle.setOnClickListener(this);

        // ----------------------------------------------------------

        // ----------- Radio Show Time -------------------------------
        radioShowTime24Hours = findViewById(R.id.radioShowTime24Hours);
        radioShowTimeNow = findViewById(R.id.radioShowTimeNow);
        radioShowDailyGoalChart = findViewById(R.id.radioShowDailyGoalChart);

        panelDGT = findViewById(R.id.panelDGT);
        tvDailyGoalTargetTitle = findViewById(R.id.tvDailyGoalTargetTitle);
        spinnerDailyGoalFormula = findViewById(R.id.spinnerDailyGoalFormula);
        spinnerDailyGoalParam = findViewById(R.id.spinnerDailyGoalParam);
        tvDailyGoalTargetDisplayUnit = findViewById(R.id.tvDailyGoalTargetDisplayUnit);
        radioGoalUnit = findViewById(R.id.radioGoalUnit);
        radioGoalPercentage = findViewById(R.id.radioGoalPercentage);
        tvDailyGoalTargetMax = findViewById(R.id.tvDailyGoalTargetMax);

        RadioGroup radioGroupChartOption = findViewById(R.id.radioGroupChartOption);
        radioGroupChartOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioShowDailyGoalChart.isChecked()) {
                    panelDGT.setVisibility(View.VISIBLE);
                } else {
                    panelDGT.setVisibility(View.GONE);
                }
            }
        });

        if (appSettings.getChartOption() == 0) {
            radioShowTime24Hours.setChecked(true);
        } else if(appSettings.getChartOption() == 1) {
            radioShowTimeNow.setChecked(true);
        } else if(appSettings.getChartOption() == 2) {
            radioShowDailyGoalChart.setChecked(true);
        }

        tvDailyGoalTargetTitle.setText(appSettings.getDGTTitle());
        tvDailyGoalTargetDisplayUnit.setText(appSettings.getDGTUnit());
        if (appSettings.getDGTDispMode() == 0) {
            radioGoalUnit.setChecked(true);
        } else {
            radioGoalPercentage.setChecked(true);
        }
        tvDailyGoalTargetMax.setText(String.valueOf(appSettings.getDGTMaxValue()));

        // Formula Spinner
        formulaArrayAdapter = new ArrayAdapter<DGTFormula>(this,   R.layout.spinneritem_daily_goal_target, formularDataList);
        formulaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerDailyGoalFormula.setAdapter(formulaArrayAdapter);
        spinnerDailyGoalFormula.setSelection(appSettings.getDGTOption());
        getFormularInfo();

        // Param Spinner
        ArrayAdapter<String> paramArrayAdapter = new ArrayAdapter<String>(this,   R.layout.spinneritem_daily_goal_target, new String[]{"None", "Current Operator", "Current Job ID"});
        paramArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerDailyGoalParam.setAdapter(paramArrayAdapter);
        spinnerDailyGoalParam.setSelection(appSettings.getDGTOption());

        // ------------------------------------------------------------

        findViewById(R.id.btnSaveSettings).setOnClickListener(this);

        checkAppVersion(new CheckAppUpdateHelper.onVersionCheckCallback() {
            @Override
            public void onFailed(String message) {
                tvVerUpToDate.setVisibility(View.VISIBLE);
                tvVerUpToDate.setText(R.string.msg_version_check_error);
            }

            @Override
            public void onSuccess(boolean haveUpdates, String newVersion) {
                if (haveUpdates) {
                    btnUpdateApp.setVisibility(View.VISIBLE);
                    btnUpdateApp.setText(getString(R.string.msg_new_version_info, newVersion));
                } else {
                    tvVerUpToDate.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // It should be refresed here again after change the device
        tvBTDeviceAddr.setText(appSettings.getBTDeviceAddr());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMyApp.setAppSettingsStatus(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btnOpenWIFISettings) {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        } else if (viewId == R.id.tvBTDeviceAddr) {
            startActivity(new Intent(mContext, DeviceScanActivity.class));
        } else if (viewId == R.id.btnSaveSettings) {
            saveSettings();
        } else if (viewId == R.id.btnQRJobId) {
            if (checkPermissions(mContext, PERMISSION_REQUEST_QRSCAN_STRING, false, PERMISSION_REQUEST_CODE_QRSCAN)) {
                startActivityForResult(new Intent(mContext, FullScannerActivity.class), REQUEST_SCAN_JOBID);
            }
        } else if (viewId == R.id.tvAccountId) {
            inputCustomerID();
        } else if (viewId == R.id.btnCheckCustomerID) {
            checkAccountID();
        } else if (viewId == R.id.tvStopTime) {
            new PickerDialogFragment(appSettings.getStopTimeLimit(), TIMEPICK_TAG_STOPTIME,this).show(getSupportFragmentManager(), "timeDurationPicker");
        } else if (viewId == R.id.tvPlannedProductionTime) {
            new PickerDialogFragment(appSettings.getPlannedProductionTime(), TIMEPICK_TAG_PPT,this).show(getSupportFragmentManager(), "timeDurationPicker");
        } else if (viewId == R.id.tvShift1PPTValue) {
            new PickerDialogFragment(appSettings.getShift1PPT(), TIMEPICK_TAG_SHIFT1PPT,this).show(getSupportFragmentManager(), "timeDurationPicker");
        } else if (viewId == R.id.tvShift2PPTValue) {
            new PickerDialogFragment(appSettings.getShift2PPT(), TIMEPICK_TAG_SHIFT2PPT,this).show(getSupportFragmentManager(), "timeDurationPicker");
        } else if (viewId == R.id.tvShift3PPTValue) {
            new PickerDialogFragment(appSettings.getShift3PPT(), TIMEPICK_TAG_SHIFT3PPT,this).show(getSupportFragmentManager(), "timeDurationPicker");
        } else if (viewId == R.id.btnUpdateApp) {
            downloadNewBuild();
        } else if (viewId == R.id.btnInfoAboutStopAlert) {
            Intent intent = new Intent(mContext, AboutAlertActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.tvPartsPerCycle) {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
            builderSingle.setIcon(R.mipmap.ic_launcher);
            builderSingle.setTitle("Select Value");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
            for (int i = 1; i <= 100; i++) {
                arrayAdapter.add(String.valueOf(i));
            }

            builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strValue = arrayAdapter.getItem(which);
                    tvPartsPerCycle.setText(strValue);
                    appSettings.setPartsPerCycle(Integer.parseInt(strValue));
                }
            });
            builderSingle.show();
        }
    }

    private static final int TIMEPICK_TAG_STOPTIME = 0;
    private static final int TIMEPICK_TAG_PPT = 1;
    private static final int TIMEPICK_TAG_SHIFT1PPT = 2;
    private static final int TIMEPICK_TAG_SHIFT2PPT = 3;
    private static final int TIMEPICK_TAG_SHIFT3PPT = 4;

    @Override
    public void onDurationSet(int tag, long duration) {
        if (tag == TIMEPICK_TAG_STOPTIME) {
            appSettings.setStopTimeLimit(duration);
            tvStopTimeValue.setText(String.format("Stop Time Limit (%ds)", duration / 1000));
        } else if(tag == TIMEPICK_TAG_PPT) {
            if (duration == 0) {
                showToastMessage("Planned Time is invalid");
                return;
            }

            appSettings.setPlannedProductionTime(duration);
            tvPlannedProductionTime.setText(TimeDurationUtil.formatHoursMinutesSeconds(duration));
        } else if(tag == TIMEPICK_TAG_SHIFT1PPT) {
            if (duration == 0) {
                showToastMessage("Shift1 P.P.T is invalid");
                return;
            }

            appSettings.setShift1PPT(duration);
            tvShift1PPTValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(duration));
        } else if(tag == TIMEPICK_TAG_SHIFT2PPT) {
            if (duration == 0) {
                showToastMessage("Shift2 P.P.T is invalid");
                return;
            }

            appSettings.setShift2PPT(duration);
            tvShift2PPTValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(duration));
        } else if(tag == TIMEPICK_TAG_SHIFT3PPT) {
            if (duration == 0) {
                showToastMessage("Shift3 P.P.T is invalid");
                return;
            }

            appSettings.setShift3PPT(duration);
            tvShift3PPTValue.setText(TimeDurationUtil.formatHoursMinutesSeconds(duration));
        }
    }

    private void saveSettings() {

        // We don't use the Modbus Com and the following setting block is not used.---------------------
        String strIP = tvIP.getText().toString().trim();
        String strPort = tvPort.getText().toString().trim();
        String strMemory = tvMemory.getText().toString().trim();

        int portNum = 0;
        try {
            portNum = Integer.parseInt(strPort);
        } catch (Exception e) {
        }

        int memAddr = -1;
        try {
            memAddr = Integer.parseInt(strMemory);
        } catch (Exception e) {
        }

        if (!Patterns.IP_ADDRESS.matcher(strIP).matches() || portNum < 0 || portNum > 65535 || memAddr < 0) {
            showToastMessage("Address is invalid");
            return;
        }

        appSettings.setDeviceIP(strIP);
        appSettings.setDevicePort(portNum);
        appSettings.setDeviceMemory(memAddr);
        //------------------------------------------------------------------------------------------------

        // Server Information
        String localServerIP = tvLocalServerIP.getText().toString().trim();
        if (switchLocalServer.isChecked() && TextUtils.isEmpty(localServerIP)/*!Patterns.IP_ADDRESS.matcher(localServerIP).matches()*/) {
            showToastMessage("Invalid Local Server IP.");
            return;
        }
        appSettings.setUsingLocalServer(switchLocalServer.isChecked());
        appSettings.setLocalServerIP(localServerIP);

        // Job Information
        appSettings.setUsingJobAutoLogout(switchJobAutoLogout.isChecked());

        // InCycle Status Source
        appSettings.setInCycleFromPLC(radioInCyclePLC.isChecked());

        // Process Monitor Type
        if (radioGroupPM.getCheckedRadioButtonId() == R.id.radioPMTimeLogger) {
            appSettings.setProcessMonitorType(1);
        } else if (radioGroupPM.getCheckedRadioButtonId() == R.id.radioPMCleaning) {
            appSettings.setProcessMonitorType(2);
        } else if (radioGroupPM.getCheckedRadioButtonId() == R.id.radioPMBlast) {
            appSettings.setProcessMonitorType(3);
        } else if (radioGroupPM.getCheckedRadioButtonId() == R.id.radioPMPaint) {
            appSettings.setProcessMonitorType(4);
        } else if (radioGroupPM.getCheckedRadioButtonId() == R.id.radioPMAssembly137) {
            appSettings.setProcessMonitorType(5);
        } else if (radioGroupPM.getCheckedRadioButtonId() == R.id.radioPMAssembly136) {
            appSettings.setProcessMonitorType(6);
        } else if (radioGroupPM.getCheckedRadioButtonId() == R.id.radioPMAssembly3) {
            appSettings.setProcessMonitorType(7);
        } else if (radioGroupPM.getCheckedRadioButtonId() == R.id.radioPMQuality) {
            appSettings.setProcessMonitorType(8);
        } else {
            appSettings.setProcessMonitorType(0);
        }

        // Temperature Data Source
        if (radioTempInput.isChecked()) {
            appSettings.setTemperatureDataSource(0);
        } else if (radioTempIIot.isChecked()) {
            appSettings.setTemperatureDataSource(1);
        } else {
            appSettings.setTemperatureDataSource(2);
        }

        // Machine Info
        String machineName = tvMachineName.getText().toString().trim();
        if (TextUtils.isEmpty(machineName)) {
            showToastMessage("Please input machine name");
            return;
        }
        appSettings.setMachineName(machineName);

        // Factory Info(Account ID)
        if (TextUtils.isEmpty(appSettings.getCustomerID())) {
            showToastMessage("Please input valid account ID.");
            return;
        }

        // DownTime Reason
        String downTimeReason1 = edtDownTimeReason1.getText().toString().trim();
        String downTimeReason2 = edtDownTimeReason2.getText().toString().trim();
        String downTimeReason3 = edtDownTimeReason3.getText().toString().trim();
        String downTimeReason4 = edtDownTimeReason4.getText().toString().trim();
        String downTimeReason5 = edtDownTimeReason5.getText().toString().trim();
        String downTimeReason6 = edtDownTimeReason6.getText().toString().trim();
        String downTimeReason7 = edtDownTimeReason7.getText().toString().trim();
        String downTimeReason8 = edtDownTimeReason8.getText().toString().trim();

        // Check all is empty
        if (TextUtils.isEmpty(downTimeReason1) &&
                TextUtils.isEmpty(downTimeReason2) &&
                TextUtils.isEmpty(downTimeReason3) &&
                TextUtils.isEmpty(downTimeReason4) &&
                TextUtils.isEmpty(downTimeReason5) &&
                TextUtils.isEmpty(downTimeReason6) &&
                TextUtils.isEmpty(downTimeReason7) &&
                TextUtils.isEmpty(downTimeReason8)) {
            showToastMessage("Please input downtime reasons");
            return;
        }

        appSettings.setDowntimeReason1(downTimeReason1);
        appSettings.setDowntimeReason2(downTimeReason2);
        appSettings.setDowntimeReason3(downTimeReason3);
        appSettings.setDowntimeReason4(downTimeReason4);
        appSettings.setDowntimeReason5(downTimeReason5);
        appSettings.setDowntimeReason6(downTimeReason6);
        appSettings.setDowntimeReason7(downTimeReason7);
        appSettings.setDowntimeReason8(downTimeReason8);

        // Save Aux Data Title Settings
        String aux1DataTitle = tvAux1Data.getText().toString().trim();
        String aux2DataTitle = tvAux2Data.getText().toString().trim();
        String aux3DataTitle = tvAux3Data.getText().toString().trim();
        if (TextUtils.isEmpty(aux1DataTitle) && TextUtils.isEmpty(aux2DataTitle) && TextUtils.isEmpty(aux3DataTitle)) {
            showToastMessage("Please input titles for AuxData");
            return;
        }

        appSettings.setAux1DataTitle(aux1DataTitle);
        appSettings.setAux2DataTitle(aux2DataTitle);
        appSettings.setAux3DataTitle(aux3DataTitle);

        // Save CSLock Settings
        appSettings.setUseCSLock(switchUseCSLock.isChecked());
        appSettings.setReverseCSLock(switchReverseCSLock.isChecked());

        // Alert
        appSettings.setCycleStopAlert(switchAlert.isChecked());
        appSettings.setAlertEmail1(tvAlertEmail1.getText().toString().trim());
        appSettings.setAlertEmail2(tvAlertEmail2.getText().toString().trim());
        appSettings.setAlertEmail3(tvAlertEmail3.getText().toString().trim());

        // Automatic Parts Counter
        appSettings.setAutomaticPartsCounter(switchAutoCount.isChecked());
        appSettings.setMinElapsedStopTime(seekMinElapsedCycleTime.getProgress() * 1000);
        appSettings.setPartsPerCycle(Integer.parseInt(tvPartsPerCycle.getText().toString().trim()));

        // Save Gantt Chart Time Values
        if (radioShowTime24Hours.isChecked()) {
            appSettings.setChartOption(0);
        } else if (radioShowTimeNow.isChecked()) {
            appSettings.setChartOption(1);
        } else if (radioShowDailyGoalChart.isChecked()) {
            appSettings.setChartOption(2);
        }

        // Job ID and Customer
        // We don't set job id in Settings, It's in the main screen
        /*String jobId = tvJobId.getText().toString().trim();
        if (TextUtils.isEmpty(jobId)) {
            showToastMessage("Please input Job Id");
            return;
        }
        appSettings.setJobId(jobId);*/

        String dgtTitle = tvDailyGoalTargetTitle.getText().toString().trim();
        int formularID = 0;
        if (formularDataList.size() > 0) {
            formularID = formularDataList.get(spinnerDailyGoalFormula.getSelectedItemPosition()).getId();
        }
        int paramID = spinnerDailyGoalParam.getSelectedItemPosition();

        String dgtUnit = tvDailyGoalTargetDisplayUnit.getText().toString().trim();
        int optionDisplayUnit = radioGoalUnit.isChecked() ? 0 : 1;
        float dgtMax = 100;
        try {
            dgtMax = Float.parseFloat(tvDailyGoalTargetMax.getText().toString());
        } catch (Exception e) {}
        if (dgtMax<=0) {
            dgtMax = 100;
        }

        if (radioShowDailyGoalChart.isChecked() && (TextUtils.isEmpty(dgtTitle) || TextUtils.isEmpty(dgtUnit))) {
            showToastMessage("Please input Goal Chart Options.");
            return;
        }

        appSettings.setDGTTitle(dgtTitle);
        appSettings.setDGTFomular(formularID);
        appSettings.setDGTOption(paramID);
        appSettings.setDGTUnit(dgtUnit);
        appSettings.setDGTDispMode(optionDisplayUnit);
        appSettings.setDGTMaxValue(dgtMax);

        // Upload current machine settings to the online
        if (!TextUtils.isEmpty(appSettings.getCustomerID()) && !TextUtils.isEmpty(appSettings.getMachineName())) {
            RequestParams requestParams = new RequestParams();
            requestParams.put("factory_id", appSettings.getCustomerID());
            requestParams.put("machine_id", appSettings.getMachineName());

            // Downtime Reason
            requestParams.put("downtime_reason1", appSettings.getDownTimeReason1());
            requestParams.put("downtime_reason2", appSettings.getDownTimeReason2());
            requestParams.put("downtime_reason3", appSettings.getDownTimeReason3());
            requestParams.put("downtime_reason4", appSettings.getDownTimeReason4());
            requestParams.put("downtime_reason5", appSettings.getDownTimeReason5());
            requestParams.put("downtime_reason6", appSettings.getDownTimeReason6());
            requestParams.put("downtime_reason7", appSettings.getDownTimeReason7());
            requestParams.put("downtime_reason8", appSettings.getDownTimeReason8());

            // CSLock Settings
            requestParams.put("cslock_cycle", appSettings.isUseCSLock() ? 1 : 0);
            requestParams.put("cslock_reverse", appSettings.isReverseCSLock() ? 1 : 0);
            requestParams.put("cslock_guest", appSettings.isGuestLock() ? 1 : 0);
            requestParams.put("cslock_alert", appSettings.isCSLockSoundEnabled() ? 1 : 0);

            // Time Settings
            requestParams.put("time_stop", appSettings.getStopTimeLimit() / 1000);
            requestParams.put("time_production", appSettings.getPlannedProductionTime() / 1000);

            // Alert Emails Setting
            requestParams.put("cycle_send_alert", appSettings.isCycleStopAlert() ? 1 : 0);
            requestParams.put("cycle_email1", appSettings.getAlertEmail1());
            requestParams.put("cycle_email2", appSettings.getAlertEmail2());
            requestParams.put("cycle_email3", appSettings.getAlertEmail3());

            // Automatic Parts Counter
            requestParams.put("automatic_part", appSettings.isAutomaticPartsCounter() ? 1 : 0);
            requestParams.put("automatic_min_time", appSettings.getMinElapsedStopTime() / 1000);
            requestParams.put("automatic_part_per_cycle", appSettings.getPartsPerCycle());

            // Gantt Display Option
            requestParams.put("gantt_chart_display", appSettings.getChartOption());


            // Goal Chart Options
            requestParams.put("calc_chart_title", dgtTitle);
            requestParams.put("calc_chart_formula", formularID);
            requestParams.put("calc_chart_option", paramID);
            requestParams.put("calc_chart_unit", dgtUnit);
            requestParams.put("calc_chart_disp_mode", optionDisplayUnit);
            requestParams.put("calc_chart_max_value", dgtMax);

            showProgressDialog();

            GoogleCertProvider.install(getApplicationContext());
            ITSRestClient.post(mContext, "updateAppSetting", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    hideProgressDialog();

                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("updateAppSetting", response.toString());

                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);

                    hideProgressDialog();

                    String errorMsg = throwable.getMessage();
                    if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                        errorMsg = getString(R.string.error_connection_timeout);
                    }

                    Log.e("updateAppSetting", errorMsg);

                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                    hideProgressDialog();

                    String errorMsg = throwable.getMessage();
                    if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                        errorMsg = getString(R.string.error_connection_timeout);
                    }

                    Log.e("updateAppSetting", errorMsg);

                    finish();
                }
            });
        }
    }

    private void inputCustomerID() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_id_input, null);
        TextView main_title = dialogView.findViewById(R.id.main_title);
        main_title.setText(R.string.title_account_id);

        TextView sub_content = dialogView.findViewById(R.id.sub_content);
        sub_content.setText(R.string.msg_input_account_id);

        final EditText edtIdInput = dialogView.findViewById(R.id.edtIDInput);
        edtIdInput.setText(appSettings.getCustomerID());

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

                // In case of new input, show input value to the
                tvAccountId.setText(loginID);

                getCustomerInfo(loginID);
            }
        });

        loginIdInputDlg.setCanceledOnTouchOutside(false);
        loginIdInputDlg.setCancelable(false);
        loginIdInputDlg.show();
        loginIdInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        edtIdInput.requestFocus();
    }

    private void checkAccountID() {
        String accountId = tvAccountId.getText().toString().trim();
        getCustomerInfo(accountId);
    }

    private void getCustomerInfo(final String accountId) {

        if (TextUtils.isEmpty(accountId)) {
            showToastMessage("Please input Account ID.");
            return;
        }

        // Call Web API
        Log.e(TAG, "Login");

        // To Do List
        // API name loginWithCustomerId.php
        // Params : customerid, deviceid
        // Response : {status : true, data : {username : "User1", avatar : "http://..."}}

        showProgressDialog();

        RequestParams requestParams = new RequestParams();
        requestParams.put("customerId", accountId);
        requestParams.put("deviceId", mMyApp.getAndroidId());

        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "loginWithCustomerId", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("Login", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        // User Information is exist
                        JSONObject jsonData = response.getJSONObject("data");
                        String name = jsonData.getString("name");
                        String logo = jsonData.getString("logo");

                        // Save User Information
                        appSettings.setCustomerID(accountId);
                        appSettings.setCustomerName(name);
                        appSettings.setCustomerAvatar(logo/*"https://i1.sndcdn.com/avatars-000002504134-byor70-t300x300.jpg"*/);

                        showToastMessage("Success to get Account Info.");

                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = getString(R.string.error_server_busy);
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

                showAlert(errorMsg);
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

                showAlert(errorMsg);
                Log.e("Login", errorMsg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SCAN_JOBID) {
                getJobInfo(data.getStringExtra("code"));
            }
        }
    }

    // MainActivity Also use this module, when you change this module, please check it too.
    private void getJobInfo(String jobId) {

        String accountID = appSettings.getCustomerID();
        if (TextUtils.isEmpty(accountID)) {
            showToastMessage("Please setup the factory to get JOB Info");
            return;
        }

        // Call Web API
        Log.e(TAG, "jobID");

        showProgressDialog();

        RequestParams requestParams = new RequestParams();
        requestParams.put("customerId", accountID);
        requestParams.put("jobId", jobId);

        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "getJobData", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("jobID", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        // User Information is exist
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            JSONObject jsonData = jsonArray.getJSONObject(0);

                            String jobId = jsonData.getString("jobID");
                            String jobDetails = jsonData.toString();

                            // Save User Information
                            appSettings.setJobId(jobId);
                            appSettings.setJobDetails(jobDetails);

                            tvJobId.setText(jobId);

                            // Refresh the setting values
                            try {
                                JSONObject jsonObject = new JSONObject(jobDetails);
                                // Parts Per Cycle
                                int partsPerCycle = jsonObject.optInt("partsPerCycle");
                                if (partsPerCycle > 0) {
                                    appSettings.setPartsPerCycle(partsPerCycle);
                                }

                                // Planned Production Time(targetCycleTime)
                                String targetCycleTimeStr = jsonObject.optString("targetCycleTime");
                                if (targetCycleTimeStr.contains(":")) {
                                    // Process as time format string
                                    String units[] = targetCycleTimeStr.split(":");
                                    int h = 0;
                                    int m = 0, s = 0;
                                    if (units.length == 2) {
                                        // mm:ss
                                        m = Integer.parseInt(units[0]);
                                        s = Integer.parseInt(units[1]);
                                    } else if (units.length == 3) {
                                        // hh:mm:ss
                                        h = Integer.parseInt(units[0]);
                                        m = Integer.parseInt(units[1]);
                                        s = Integer.parseInt(units[2]);
                                    }

                                    int targetCycleTimeInSec = h * 3600 + m * 60 + s;
                                    appSettings.setTargetCycleTimeSeconds(targetCycleTimeInSec); // seconds to miliseconds
                                } else {
                                    int targetCycleTimeInSec = jsonObject.optInt("targetCycleTime");
                                    appSettings.setTargetCycleTimeSeconds(targetCycleTimeInSec); // seconds to miliseconds
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            showToastMessage("There is no data matched by input");
                        }
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = getString(R.string.error_server_busy);
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

                showAlert(errorMsg);
                Log.e("JOB", errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showAlert(errorMsg);
                Log.e("JOB", errorMsg);
            }
        });
    }

    private void getFormularInfo() {
        RequestParams requestParams = new RequestParams();
        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "getAppSettingFormulas", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("jobID", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        // User Information is exist
                        JSONArray jsonArray = response.getJSONArray("data");
                        int formulaIndex = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject formulaObj = jsonArray.getJSONObject(i);

                            int id = formulaObj.getInt("id");
                            String name = formulaObj.getString("name");
                            String formula = formulaObj.getString("formula");

                            formularDataList.add(new DGTFormula(id, name, formula));

                            if (id == appSettings.getDGTFormula()) {
                                formulaIndex = i;
                            }
                        }

                        // Show in Spinner
                        formulaArrayAdapter.notifyDataSetChanged();
                        spinnerDailyGoalFormula.setSelection(formulaIndex);
                    } else {
                        radioShowDailyGoalChart.setEnabled(false);

                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = getString(R.string.error_server_busy);
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

                showAlert(errorMsg);
                Log.e("JOB", errorMsg);

                radioShowDailyGoalChart.setEnabled(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                hideProgressDialog();

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showAlert(errorMsg);
                Log.e("JOB", errorMsg);

                radioShowDailyGoalChart.setEnabled(false);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check All Permission was granted
        boolean bAllGranted = true;
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                bAllGranted = false;
                break;
            }
        }

        if (requestCode == PERMISSION_REQUEST_CODE_GALLERY) {
            if (bAllGranted) {
                downloadNewBuild();
            } else {
                showAlert(R.string.msg_permission_download);
            }
        }
    }
}
