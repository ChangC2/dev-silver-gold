package com.cam8.mmsapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;

import com.cam8.mmsapp.fragments.StageInfoFragment;
import com.cam8.mmsapp.model.FaxonStage1;
import com.cam8.mmsapp.model.FaxonStage2;
import com.cam8.mmsapp.model.FaxonStage3;
import com.cam8.mmsapp.model.FaxonStage4;
import com.cam8.mmsapp.model.FaxonStage5;
import com.cam8.mmsapp.model.FaxonStage6;
import com.cam8.mmsapp.model.FaxonStage7;
import com.cam8.mmsapp.model.FaxonStage8;
import com.cam8.mmsapp.model.FaxonStage9;
import com.cam8.mmsapp.model.FaxonStageRowInfo;
import com.cam8.mmsapp.model.PanelDataLogInfo;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.utils.DateUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class PanelCodeWeightActivity extends BaseActivity implements View.OnClickListener, StageInfoFragment.StageInputsInterface {

    Button btnStage1;
    Button btnStage2;
    Button btnStage3;
    Button btnStage4;
    Button btnStage5;
    Button btnStage6;
    Button btnStage7;
    Button btnStage8;
    Button btnStage9;
    Button btnDataLogSheet;

    FaxonStage1 faxonStage1;
    FaxonStage2 faxonStage2;
    FaxonStage3 faxonStage3;
    FaxonStage4 faxonStage4;
    FaxonStage5 faxonStage5;
    FaxonStage6 faxonStage6;
    FaxonStage7 faxonStage7;
    FaxonStage8 faxonStage8;
    FaxonStage9 faxonStage9;
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

        setContentView(R.layout.activity_panelcodeweight);

        // Set Title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Panel Code Weight");

        // Stage Button Options
        btnStage1 = findViewById(R.id.btnStage1);
        btnStage2 = findViewById(R.id.btnStage2);
        btnStage3 = findViewById(R.id.btnStage3);
        btnStage4 = findViewById(R.id.btnStage4);
        btnStage5 = findViewById(R.id.btnStage5);
        btnStage6 = findViewById(R.id.btnStage6);
        btnStage7 = findViewById(R.id.btnStage7);
        btnStage8 = findViewById(R.id.btnStage8);
        btnStage9 = findViewById(R.id.btnStage9);

        btnStage1.setOnClickListener(this);
        btnStage2.setOnClickListener(this);
        btnStage3.setOnClickListener(this);
        btnStage4.setOnClickListener(this);
        btnStage5.setOnClickListener(this);
        btnStage6.setOnClickListener(this);
        btnStage7.setOnClickListener(this);
        btnStage8.setOnClickListener(this);
        btnStage9.setOnClickListener(this);

        // Data Log Sheet
        btnDataLogSheet = findViewById(R.id.btnDataLogSheet);
        btnDataLogSheet.setOnClickListener(this);

        findViewById(R.id.btnSaveAll).setOnClickListener(this);

        faxonStage1 = new FaxonStage1();
        faxonStage2 = new FaxonStage2();
        faxonStage3 = new FaxonStage3();
        faxonStage4 = new FaxonStage4();
        faxonStage5 = new FaxonStage5();
        faxonStage6 = new FaxonStage6();
        faxonStage7 = new FaxonStage7();
        faxonStage8 = new FaxonStage8();
        faxonStage9 = new FaxonStage9();

        panelDataLogInfo = new PanelDataLogInfo();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        // Part ID Login / Logout
        if (viewId == R.id.btnStage1) {
            new StageInfoFragment(this, FaxonStage1.STAGE_ID, this).show(getSupportFragmentManager(), "StageInfo");
        } else if (viewId == R.id.btnStage2) {
            new StageInfoFragment(this, FaxonStage2.STAGE_ID, this).show(getSupportFragmentManager(), "StageInfo");
        } else if (viewId == R.id.btnStage3) {
            new StageInfoFragment(this, FaxonStage3.STAGE_ID, this).show(getSupportFragmentManager(), "StageInfo");
        } else if (viewId == R.id.btnStage4) {
            new StageInfoFragment(this, FaxonStage4.STAGE_ID, this).show(getSupportFragmentManager(), "StageInfo");
        } else if (viewId == R.id.btnStage5) {
            new StageInfoFragment(this, FaxonStage5.STAGE_ID, this).show(getSupportFragmentManager(), "StageInfo");
        } else if (viewId == R.id.btnStage6) {
            new StageInfoFragment(this, FaxonStage6.STAGE_ID, this).show(getSupportFragmentManager(), "StageInfo");
        } else if (viewId == R.id.btnStage7) {
            new StageInfoFragment(this, FaxonStage7.STAGE_ID, this).show(getSupportFragmentManager(), "StageInfo");
        } else if (viewId == R.id.btnStage8) {
            new StageInfoFragment(this, FaxonStage8.STAGE_ID, this).show(getSupportFragmentManager(), "StageInfo");
        } else if (viewId == R.id.btnStage9) {
            new StageInfoFragment(this, FaxonStage9.STAGE_ID, this).show(getSupportFragmentManager(), "StageInfo");
        } else if (viewId == R.id.btnDataLogSheet) {
            startActivity(new Intent(mContext, PanelDataLogActivity.class));
        } else if (viewId == R.id.btnSaveAll) {
            // Check Data
            if (!faxonStage1.isValidInput() || !faxonStage2.isValidInput() || !faxonStage3.isValidInput()
                    || !faxonStage4.isValidInput() || !faxonStage5.isValidInput() || !faxonStage6.isValidInput()
                    || !faxonStage7.isValidInput() || !faxonStage8.isValidInput() || !faxonStage9.isValidInput()) {
                showToastMessage("Please input Stages and Panel Data Log Info.");
                return;
            }

            Date currTime = new Date();


            RequestParams requestParams = new RequestParams();
            requestParams.put("customer_id", appSettings.getCustomerID());
            requestParams.put("datetime", DateUtil.toStringFormat_20(currTime));
            requestParams.put("machine_id", appSettings.getMachineName());
            requestParams.put("operator", appSettings.getUserName());
            requestParams.put("timestamp", currTime.getTime());

            faxonStage1.fillParams(requestParams);
            faxonStage2.fillParams(requestParams);
            faxonStage3.fillParams(requestParams);
            faxonStage4.fillParams(requestParams);
            faxonStage5.fillParams(requestParams);
            faxonStage6.fillParams(requestParams);
            faxonStage7.fillParams(requestParams);
            faxonStage8.fillParams(requestParams);
            faxonStage9.fillParams(requestParams);
            panelDataLogInfo.fillParams(requestParams);

            showProgressDialog();
            GoogleCertProvider.install(mContext);
            ITSRestClient.post(mContext, "postStage", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (weakReference != null && weakReference.get() != null) {
                        weakReference.get().hideProgressDialog();
                    }

                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("DateStageInfo", response.toString());
                    try {
                        if (response.has("status") && response.getBoolean("status")) {

                            showToastMessage("Success to save!");

                            appSettings.setStage1Info("");
                            appSettings.setStage2Info("");
                            appSettings.setStage3Info("");
                            appSettings.setStage4Info("");
                            appSettings.setStage5Info("");
                            appSettings.setStage6Info("");
                            appSettings.setStage7Info("");
                            appSettings.setStage8Info("");
                            appSettings.setStage9Info("");

                            appSettings.setDataLogInfo("");

                            checkInputs();
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
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkInputs();
    }

    private void checkInputs() {
        faxonStage1.loadDataFrom(appSettings.getStage1Info());
        updateButtonStatus(btnStage1, faxonStage1.isValidInput());

        faxonStage2.loadDataFrom(appSettings.getStage2Info());
        updateButtonStatus(btnStage2, faxonStage2.isValidInput());

        faxonStage3.loadDataFrom(appSettings.getStage3Info());
        updateButtonStatus(btnStage3, faxonStage3.isValidInput());

        faxonStage4.loadDataFrom(appSettings.getStage4Info());
        updateButtonStatus(btnStage4, faxonStage4.isValidInput());

        faxonStage5.loadDataFrom(appSettings.getStage5Info());
        updateButtonStatus(btnStage5, faxonStage5.isValidInput());

        faxonStage6.loadDataFrom(appSettings.getStage6Info());
        updateButtonStatus(btnStage6, faxonStage6.isValidInput());

        faxonStage7.loadDataFrom(appSettings.getStage7Info());
        updateButtonStatus(btnStage7, faxonStage7.isValidInput());

        faxonStage8.loadDataFrom(appSettings.getStage8Info());
        updateButtonStatus(btnStage8, faxonStage8.isValidInput());

        faxonStage9.loadDataFrom(appSettings.getStage9Info());
        updateButtonStatus(btnStage9, faxonStage9.isValidInput());

        panelDataLogInfo.loadDataFrom(appSettings.getDataLogInfo());
        updateButtonStatus(btnDataLogSheet, panelDataLogInfo.isValidInput());
    }

    private void updateButtonStatus(Button button, boolean select) {
        button.setSelected(select);
    }

    @Override
    public void onInputFinished() {
        checkInputs();
    }
}
