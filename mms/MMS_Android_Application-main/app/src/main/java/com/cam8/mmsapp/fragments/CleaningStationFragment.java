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
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class CleaningStationFragment extends BaseFragment implements View.OnClickListener {

    // Part ID
    TextView tvPartID;

    // User Information
    ImageView ivAvatar;
    TextView tvUserName;

    // Time Information
    Button btnTimeProcessing;

    EditText tvNotes;

    // Goods and Bad
    TextView tvGood;
    TextView tvBad;

    ImageLoader imageLoader;
    DisplayImageOptions imageOptionsWithUserAvatar;

    private boolean isTimeCounting = false;
    private long timeCurrentMilis = 0;
    private long timePrevStatusMilis = 0;
    private long elapsedSeconds = 0;

    DatabaseHelper dbHelper;

    public static CleaningStationFragment newInstance(String text) {
        CleaningStationFragment mFragment = new CleaningStationFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cleaningstation, container, false);

        TAG = "cleaningstation";

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

        // Notes
        tvNotes = parentView.findViewById(R.id.tvNotes);
        tvNotes.setImeOptions(EditorInfo.IME_ACTION_DONE);
        tvNotes.setRawInputType(InputType.TYPE_CLASS_TEXT);

        // Goods and Bads Part
        tvGood = parentView.findViewById(R.id.tvGood);
        tvGood.setOnClickListener(this);
        parentView.findViewById(R.id.iv_good_down).setOnClickListener(this);
        parentView.findViewById(R.id.iv_good_up).setOnClickListener(this);

        tvBad = parentView.findViewById(R.id.tvBad);
        tvBad.setOnClickListener(this);
        parentView.findViewById(R.id.iv_bad_down).setOnClickListener(this);
        parentView.findViewById(R.id.iv_bad_up).setOnClickListener(this);

        // To prevent the calls of gantt bar click, add bottom view click action
        parentView.findViewById(R.id.panelCleaningStation).setOnClickListener(this);

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

        tvNotes.setText("");
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
        ITSRestClient.post(mContext, "getCleaningStation", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("cleaningProcTime", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        JSONObject jsonStation = response.getJSONObject("station");

                        long processingTime = jsonStation.optLong("processing_time");
                        String notes = jsonStation.optString("notes");

                        elapsedSeconds = processingTime;
                        btnTimeProcessing.setText(getElapsedTimeMinutesSecondsString(elapsedSeconds));
                        tvNotes.setText(notes);
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

        String notes = tvNotes.getText().toString().trim();

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
            requestParams.put("notes", notes);

            GoogleCertProvider.install(mContext);
            ITSRestClient.post(mContext, "postCleaningStation", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    hideProgressDialog();
                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("cleaningProcTime", response.toString());

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
