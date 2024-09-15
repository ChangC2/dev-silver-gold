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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cam8.mmsapp.R;
import com.cam8.mmsapp.alarm.AlarmSettings;
import com.cam8.mmsapp.model.DGTFormula;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.utils.DateUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class QualityFragment extends BaseFragment implements View.OnClickListener {

    // Part ID
    TextView tvPartID;

    // User Information
    ImageView ivAvatar;
    TextView tvUserName;

    // Time Information
    Button btnTimeProcessing;

    ImageLoader imageLoader;
    DisplayImageOptions imageOptionsWithUserAvatar;

    private boolean isTimeCounting = false;
    private long timeCurrentMilis = 0;
    private long timePrevStatusMilis = 0;
    private long elapsedSeconds = 0;

    // Goods and Bad
    TextView tvGood;
    TextView tvBad;

    Spinner spinnerQualityIssueStations;
    ArrayList<String> qualityIssueStations = new ArrayList<>();
    ArrayAdapter<String> adapterQualityIssueStations;
    Button btnSaveUpdateQualityData;

    // Assembly Usage1
    View layoutRejectReasons;

    // Reason Title and Switches
    TextView[] tvTitleReasons;
    SwitchCompat[] switchReasons;
    String[] titleReasons;

    // Comment
    EditText edtComments;

    public static QualityFragment newInstance(String text) {
        QualityFragment mFragment = new QualityFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quality, container, false);

        TAG = "assemblystation";

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

        // To prevent the calls of gantt bar click, add bottom view click action
        parentView.findViewById(R.id.panelAssemblyStation).setOnClickListener(this);

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

        // Goods and Bads Part
        tvGood = parentView.findViewById(R.id.tvGood);
        tvGood.setOnClickListener(this);
        parentView.findViewById(R.id.iv_good_down).setOnClickListener(this);
        parentView.findViewById(R.id.iv_good_up).setOnClickListener(this);

        tvBad = parentView.findViewById(R.id.tvBad);
        tvBad.setOnClickListener(this);
        parentView.findViewById(R.id.iv_bad_down).setOnClickListener(this);
        parentView.findViewById(R.id.iv_bad_up).setOnClickListener(this);

        parentView.findViewById(R.id.btnOpenReasons).setOnClickListener(this);

        spinnerQualityIssueStations = parentView.findViewById(R.id.spinnerQualityIssueStations);
        btnSaveUpdateQualityData = parentView.findViewById(R.id.btnSaveUpdateQualityData);
        btnSaveUpdateQualityData.setOnClickListener(this);

        spinnerQualityIssueStations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    btnSaveUpdateQualityData.setEnabled(false);
                } else {
                    btnSaveUpdateQualityData.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        qualityIssueStations = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.stations_quality_issue)));
        adapterQualityIssueStations = new ArrayAdapter<String>(getContext(), R.layout.spinneritem_daily_goal_target, qualityIssueStations);
        adapterQualityIssueStations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerQualityIssueStations.setAdapter(adapterQualityIssueStations);


        // Layout Usage1
        layoutRejectReasons = parentView.findViewById(R.id.layoutUsage1);
        layoutRejectReasons.setOnClickListener(this);
        parentView.findViewById(R.id.btnCloseReasons).setOnClickListener(this);

        // Reason Titles
        tvTitleReasons = new TextView[10];
        tvTitleReasons[0] = parentView.findViewById(R.id.tvTitleReason1);
        tvTitleReasons[1] = parentView.findViewById(R.id.tvTitleReason2);
        tvTitleReasons[2] = parentView.findViewById(R.id.tvTitleReason3);
        tvTitleReasons[3] = parentView.findViewById(R.id.tvTitleReason4);
        tvTitleReasons[4] = parentView.findViewById(R.id.tvTitleReason5);
        tvTitleReasons[5] = parentView.findViewById(R.id.tvTitleReason6);
        tvTitleReasons[6] = parentView.findViewById(R.id.tvTitleReason7);
        tvTitleReasons[7] = parentView.findViewById(R.id.tvTitleReason8);
        tvTitleReasons[8] = parentView.findViewById(R.id.tvTitleReason9);
        tvTitleReasons[9] = parentView.findViewById(R.id.tvTitleReason10);

        View.OnLongClickListener titleChangeListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final int titleIndex = (int) v.getTag();

                View dialogView = getLayoutInflater().inflate(R.layout.dialog_id_input, null);
                TextView main_title = dialogView.findViewById(R.id.main_title);
                main_title.setText(R.string.title_input_reason_title);

                TextView sub_content = dialogView.findViewById(R.id.sub_content);
                sub_content.setText(R.string.msg_input_reason_title);

                final EditText edtIdInput = dialogView.findViewById(R.id.edtIDInput);

                // Show Initial Test
                edtIdInput.setText(titleReasons[titleIndex]);

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
                        loginID = loginID.replace(",", "");
                        loginID = loginID.replace("|", "");

                        if (!TextUtils.isEmpty(loginID)) {

                            // Refresh Title Text
                            titleReasons[titleIndex] = loginID;
                            tvTitleReasons[titleIndex].setText(loginID);

                            // Save new reject reason titles
                            StringBuilder reasonStringBulder = new StringBuilder();
                            reasonStringBulder.append(titleReasons[0]);
                            for (int i = 1; i < 10; i++) {
                                reasonStringBulder.append("|||").append(titleReasons[i]);
                            }
                            appSettings.setRejectReasonTitles(reasonStringBulder.toString());
                        }
                    }
                });

                loginIdInputDlg.setCanceledOnTouchOutside(false);
                loginIdInputDlg.setCancelable(false);
                loginIdInputDlg.show();
                loginIdInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                edtIdInput.requestFocus();

                return false;
            }
        };

        titleReasons = new String[10];

        String rejectReasonTitles = appSettings.getRejectReasonTitles();
        if (TextUtils.isEmpty(rejectReasonTitles)) {
            titleReasons[0] = "FOD in paint";
            titleReasons[1] = "FOD in Bituminous";
            titleReasons[2] = "Bituminous Separation";
            titleReasons[3] = "Missing Phosphate";
            titleReasons[4] = "Damaged Product";
            titleReasons[5] = "Paperwork not completed";
            titleReasons[6] = "Paperwork incorrect";
            titleReasons[7] = "Heavy Coatings";
            titleReasons[8] = "Rust";
            titleReasons[9] = "Light Coatings";
        } else {
            String[] titles = rejectReasonTitles.split("\\|\\|\\|");
            int cntTitles = Math.min(10, titles.length);
            for (int i = 0; i < cntTitles; i++) {
                titleReasons[i] = titles[i];
            }
        }
        for (int i = 0; i < 10; i++) {
            tvTitleReasons[i].setText(titleReasons[i]);
            tvTitleReasons[i].setTag(i);
            tvTitleReasons[i].setOnLongClickListener(titleChangeListener);
        }

        // Reason Options
        switchReasons = new SwitchCompat[10];
        switchReasons[0] = parentView.findViewById(R.id.switchReason1);
        switchReasons[1] = parentView.findViewById(R.id.switchReason2);
        switchReasons[2] = parentView.findViewById(R.id.switchReason3);
        switchReasons[3] = parentView.findViewById(R.id.switchReason4);

        switchReasons[4] = parentView.findViewById(R.id.switchReason5);
        switchReasons[5] = parentView.findViewById(R.id.switchReason6);
        switchReasons[6] = parentView.findViewById(R.id.switchReason7);
        switchReasons[7] = parentView.findViewById(R.id.switchReason8);

        switchReasons[8] = parentView.findViewById(R.id.switchReason9);
        switchReasons[9] = parentView.findViewById(R.id.switchReason10);


        // Comments
        edtComments = parentView.findViewById(R.id.edtComments);
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
        } else if (viewId == R.id.btnOpenReasons) {
            layoutRejectReasons.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.btnCloseReasons) {
            layoutRejectReasons.setVisibility(View.GONE);
        } else if (viewId == R.id.btnSaveUpdateQualityData) {
            // Save & Update Quality Data

            // Check Part ID
            String partID = tvPartID.getText().toString().trim();
            if (TextUtils.isEmpty(partID)) {
                showToastMessage("Please input Part ID to update quality data.");
                return;
            }

            // Check Station
            if (spinnerQualityIssueStations.getSelectedItemPosition() == 0) {
                return;
            }

            showProgressDialog();

            String stationNamePrefix = qualityIssueStations.get(spinnerQualityIssueStations.getSelectedItemPosition());

            RequestParams requestParams = new RequestParams();

            requestParams.put("customer_id", /*"faxon"*/appSettings.getCustomerID());
            requestParams.put("machine_prefix", stationNamePrefix);
            requestParams.put("part_id", partID);

            GoogleCertProvider.install(mContext);
            ITSRestClient.post(mContext, "postUpdateOldShiftInfo", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    hideProgressDialog();
                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("postUpdateOldShiftInfo", response.toString());

                    try {
                        if (response.has("status") && response.getBoolean("status")) {

                            showToastMessage("Success to update!");
                            tvPartID.setText("");
                        } else {
                            String message = "";
                            if (response.has("message") && !response.isNull("message")) {
                                message = response.getString("message");
                            }

                            if (TextUtils.isEmpty(message)) {
                                message = "Failed to update quality data, please check connection!";
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

                    showAlert("Failed to update quality data, please check connection!");
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

                    showAlert("Failed to update quality data, please check connection!");
                }
            });
        }
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

        // Reset Switches
        for (int i = 0; i < 10; i++) {
            switchReasons[i].setChecked(false);
        }

        // Reset Notes
        edtComments.setText("");

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
        ITSRestClient.post(mContext, "getQualityStation", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("qualityStation", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        JSONObject jsonStation = response.getJSONObject("station");

                        // Retrieve Time
                        long processingTime = jsonStation.optLong("processing_time");
                        elapsedSeconds = processingTime;
                        btnTimeProcessing.setText(getElapsedTimeMinutesSecondsString(elapsedSeconds));

                        String reasonStrings = jsonStation.optString("scrap_code").toLowerCase();

                        if (!TextUtils.isEmpty(reasonStrings)) {
                            for (int i = 0; i < 10; i++) {
                                switchReasons[i].setChecked(reasonStrings.contains(titleReasons[i].toLowerCase()));
                            }
                        }

                        edtComments.setText(jsonStation.optString("comments"));
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

                showAlert("Failed to get part info, please check connection!");
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

                showAlert("Failed to get part info, please check connection!");
            }
        });
    }

    // 1. User press stop button
    // 2. User press "logout" for part_id
    // 3. User login to different part_id without logging out first
    private void reportTime() {
        String partID = tvPartID.getText().toString().trim();
        if (TextUtils.isEmpty(partID)) {
            showToastMessage("Please input Part ID to save times.");
            return;
        }

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

            String scrapCode = "";
            for (int i = 0; i < 10; i++) {
                if (switchReasons[i].isChecked()) {
                    if (TextUtils.isEmpty(scrapCode)) {
                        scrapCode = titleReasons[i];
                    } else {
                        scrapCode += ", " + titleReasons[i];
                    }
                }
            }

            requestParams.put("scrap_code", scrapCode.trim());
            requestParams.put("comments", edtComments.getText().toString().trim());

            GoogleCertProvider.install(mContext);
            ITSRestClient.post(mContext, "postQualityStation", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    hideProgressDialog();
                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("postQuality", response.toString());

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

            pauseTimer();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Log.e(TAG, "" + requestCode + "-" + resultCode);
            if (requestCode == REQUEST_SCAN_VALUE) {
                String dataCode = data.getStringExtra("code");
            }
        }
    }
}
