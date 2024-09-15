package com.cam8.icsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.ViewCompat;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:setting";

    ViewGroup settingPanel;

    TextView tvAppVersion;

    TextView tvBTDeviceAddr;

    RadioButton radioUnitPsi;
    RadioButton radioSUnitBar;

    TextView tvPLCVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.title_settings);

        tvAppVersion = findViewById(R.id.tvAppVersion);
        tvAppVersion.setText("V" + getVersionName());

        settingPanel = findViewById(R.id.settingPanel);
        ViewCompat.setTransitionName(settingPanel, VIEW_NAME_HEADER_IMAGE);

        // Machine and Device information
        tvBTDeviceAddr = findViewById(R.id.tvBTDeviceAddr);

        tvBTDeviceAddr.setText(appSettings.getBTDeviceAddr());

        tvBTDeviceAddr.setOnClickListener(this);

        // Unit Settings
        radioUnitPsi = findViewById(R.id.radioUnitPsi);
        radioSUnitBar = findViewById(R.id.radioSUnitBar);
        if (appSettings.isUnitPSI()) {
            radioUnitPsi.setChecked(true);
        } else {
            radioSUnitBar.setChecked(true);
        }

        // PLC Version
        tvPLCVersion = findViewById(R.id.tvPLCVersion);
        findViewById(R.id.btnUpdatePLC).setOnClickListener(this);

        findViewById(R.id.btnSaveSettings).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // It should be refresed here again after change the device
        tvBTDeviceAddr.setText(appSettings.getBTDeviceAddr());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.tvBTDeviceAddr) {
            startActivityForResult(new Intent(mContext, DeviceScanActivity.class), REQUEST_ENABLE_BT);
        } else if (viewId == R.id.btnSaveSettings) {
            saveSettings();
        }
    }

    private void saveSettings() {

        // Save Gantt Chart Time Values
        appSettings.setUnitPSI(radioUnitPsi.isChecked());

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            // Bluetooth Address
            tvBTDeviceAddr.setText(appSettings.getBTDeviceAddr());
        }
    }
}
