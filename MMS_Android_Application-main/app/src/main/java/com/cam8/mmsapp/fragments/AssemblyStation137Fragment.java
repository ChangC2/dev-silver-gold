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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cam8.mmsapp.FullScannerActivity;
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

public class AssemblyStation137Fragment extends BaseFragment implements View.OnClickListener {

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

    // Assembly Usage1
    View layoutUsage1;
    TextView tvCaseNumber;
    TextView tvSerial;
    TextView tvShipment;
    TextView tvEmptyWt;

    TextView tvCenterGrav;
    TextView tvAftAss;
    TextView tvAftRetainRing;
    TextView tvShippingCover;

    TextView tvShippingPlug;
    TextView tvEndCap;
    TextView tvLugs;
    TextView tvRetainRing;

    TextView tvAdaptRing;
    TextView tvImpactRing;
    TextView tvTotalWt;
    TextView tvAssCenterGrav;

    // Assembly Usage2
    View layoutUsage2;
    TextView tvDegreaseSol;
    TextView tvCorrosinPrevCompound;
    TextView tvShipCoverOring;
    TextView tvOringGrease;

    TextView tvProtectiveEndCap;
    TextView tvEndCapSetScrew;
    TextView tvLiftingLugBolt;
    TextView tvLiftingLugWasher;

    TextView tvStencilInk;

    DatabaseHelper dbHelper;

    RadioButton radioInputBarcode;
    EditText edtActiveArea;

    public static AssemblyStation137Fragment newInstance(String text) {
        AssemblyStation137Fragment mFragment = new AssemblyStation137Fragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assemblystation137, container, false);

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

        // Input Mode
        radioInputBarcode = parentView.findViewById(R.id.radioInputBarcode);

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

        parentView.findViewById(R.id.tvUsage1).setOnClickListener(this);
        parentView.findViewById(R.id.tvUsage2).setOnClickListener(this);

        layoutUsage1 = parentView.findViewById(R.id.layoutUsage1);
        layoutUsage1.setOnClickListener(this);
        parentView.findViewById(R.id.btnCloseUsage1).setOnClickListener(this);

        tvCaseNumber = parentView.findViewById(R.id.tvCaseNumber);
        tvSerial = parentView.findViewById(R.id.tvSerial);
        tvShipment = parentView.findViewById(R.id.tvShipment);
        tvEmptyWt = parentView.findViewById(R.id.tvEmptyWt);

        tvCenterGrav = parentView.findViewById(R.id.tvCenterGrav);
        tvAftAss = parentView.findViewById(R.id.tvAftAss);
        tvAftRetainRing = parentView.findViewById(R.id.tvAftRetainRing);
        tvShippingCover = parentView.findViewById(R.id.tvShippingCover);

        tvShippingPlug = parentView.findViewById(R.id.tvShippingPlug);
        tvEndCap = parentView.findViewById(R.id.tvEndCap);
        tvLugs = parentView.findViewById(R.id.tvLugs);
        tvRetainRing = parentView.findViewById(R.id.tvRetainRing);

        tvAdaptRing = parentView.findViewById(R.id.tvAdaptRing);
        tvImpactRing = parentView.findViewById(R.id.tvImpactRing);
        tvTotalWt = parentView.findViewById(R.id.tvTotalWt);
        tvAssCenterGrav = parentView.findViewById(R.id.tvAssCenterGrav);

        // Inputs
        /*tvCaseNumber.setTag(0);
        tvSerial.setTag(1);
        tvShipment.setTag(2);

        tvEmptyWt.setTag(3);
        tvCenterGrav.setTag(4);
        tvAftAss.setTag(5);

        tvAftRetainRing.setTag(6);
        tvShippingCover.setTag(7);
        tvShippingPlug.setTag(8);

        tvEndCap.setTag(9);
        tvLugs.setTag(10);
        tvRetainRing.setTag(11);

        tvAdaptRing.setTag(12);
        tvImpactRing.setTag(13);
        tvTotalWt.setTag(14);

        tvAssCenterGrav.setTag(15);

        // Click
        tvCaseNumber.setOnClickListener(this);
        tvSerial.setOnClickListener(this);
        tvShipment.setOnClickListener(this);
        tvEmptyWt.setOnClickListener(this);

        tvCenterGrav.setOnClickListener(this);
        tvAftAss.setOnClickListener(this);
        tvAftRetainRing.setOnClickListener(this);
        tvShippingCover.setOnClickListener(this);

        tvShippingPlug.setOnClickListener(this);
        tvEndCap.setOnClickListener(this);
        tvLugs.setOnClickListener(this);
        tvRetainRing.setOnClickListener(this);

        tvAdaptRing.setOnClickListener(this);
        tvImpactRing.setOnClickListener(this);
        tvTotalWt.setOnClickListener(this);
        tvAssCenterGrav.setOnClickListener(this);*/

        layoutUsage2 = parentView.findViewById(R.id.layoutUsage2);
        layoutUsage2.setOnClickListener(this);
        parentView.findViewById(R.id.btnCloseUsage2).setOnClickListener(this);

        tvDegreaseSol = parentView.findViewById(R.id.tvDegreaseSol);
        tvCorrosinPrevCompound = parentView.findViewById(R.id.tvCorrosinPrevCompound);
        tvShipCoverOring = parentView.findViewById(R.id.tvShipCoverOring);
        tvOringGrease = parentView.findViewById(R.id.tvOringGrease);

        tvProtectiveEndCap = parentView.findViewById(R.id.tvProtectiveEndCap);
        tvEndCapSetScrew = parentView.findViewById(R.id.tvEndCapSetScrew);
        tvLiftingLugBolt = parentView.findViewById(R.id.tvLiftingLugBolt);
        tvLiftingLugWasher = parentView.findViewById(R.id.tvLiftingLugWasher);

        tvStencilInk = parentView.findViewById(R.id.tvStencilInk);


        /*View.OnFocusChangeListener focusDetectListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v instanceof EditText && hasFocus && radioInputBarcode.isChecked()) {
                    edtActiveArea = (EditText) v;

                    hideKeyboard(edtActiveArea);

                    if (checkPermissions(mContext, PERMISSION_REQUEST_QRSCAN_STRING, false, PERMISSION_REQUEST_CODE_QRSCAN)) {
                        Intent intent = new Intent(mContext, FullScannerActivity.class);
                        startActivityForResult(intent, REQUEST_SCAN_VALUE);
                    }
                }
            }
        };*/

        View.OnTouchListener  touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof EditText && radioInputBarcode.isChecked()) {
                    if(MotionEvent.ACTION_UP == event.getAction()) {
                        edtActiveArea = (EditText) v;

                        hideKeyboard(edtActiveArea);

                        if (checkPermissions(mContext, PERMISSION_REQUEST_QRSCAN_STRING, false, PERMISSION_REQUEST_CODE_QRSCAN)) {
                            Intent intent = new Intent(mContext, FullScannerActivity.class);
                            startActivityForResult(intent, REQUEST_SCAN_VALUE);
                        }

                        return true;
                    }
                }

                return false; // return is important...
            }
        };

        // Assembly Usage 1 inputs
        tvCaseNumber.setOnTouchListener(touchListener);
        tvSerial.setOnTouchListener(touchListener);
        tvShipment.setOnTouchListener(touchListener);
        tvEmptyWt.setOnTouchListener(touchListener);

        tvCenterGrav.setOnTouchListener(touchListener);
        tvAftAss.setOnTouchListener(touchListener);
        tvAftRetainRing.setOnTouchListener(touchListener);
        tvShippingCover.setOnTouchListener(touchListener);

        tvShippingPlug.setOnTouchListener(touchListener);
        tvEndCap.setOnTouchListener(touchListener);
        tvLugs.setOnTouchListener(touchListener);
        tvRetainRing.setOnTouchListener(touchListener);

        tvAdaptRing.setOnTouchListener(touchListener);
        tvImpactRing.setOnTouchListener(touchListener);
        tvTotalWt.setOnTouchListener(touchListener);
        tvAssCenterGrav.setOnTouchListener(touchListener);


        // Assembly Usage 2 inputs
        tvDegreaseSol.setOnTouchListener(touchListener);
        tvCorrosinPrevCompound.setOnTouchListener(touchListener);
        tvShipCoverOring.setOnTouchListener(touchListener);
        tvOringGrease.setOnTouchListener(touchListener);

        tvProtectiveEndCap.setOnTouchListener(touchListener);
        tvEndCapSetScrew.setOnTouchListener(touchListener);
        tvLiftingLugBolt.setOnTouchListener(touchListener);
        tvLiftingLugWasher.setOnTouchListener(touchListener);

        tvStencilInk.setOnTouchListener(touchListener);
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
        } else if (viewId == R.id.tvCaseNumber || viewId == R.id.tvSerial || viewId == R.id.tvShipment ||
                viewId == R.id.tvEmptyWt || viewId == R.id.tvCenterGrav || viewId == R.id.tvAftAss ||
                viewId == R.id.tvAftRetainRing || viewId == R.id.tvShippingCover || viewId == R.id.tvShippingPlug ||
                viewId == R.id.tvEndCap || viewId == R.id.tvLugs || viewId == R.id.tvRetainRing ||
                viewId == R.id.tvAdaptRing || viewId == R.id.tvImpactRing || viewId == R.id.tvTotalWt || viewId == R.id.tvAssCenterGrav) {

            int tag = (int) view.getTag();

            showInputModeDialog(tag);
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
        } else if (viewId == R.id.tvUsage1) {
            layoutUsage1.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.tvUsage2) {
            layoutUsage2.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.btnCloseUsage1) {
            layoutUsage1.setVisibility(View.GONE);
        } else if (viewId == R.id.btnCloseUsage2) {
            layoutUsage2.setVisibility(View.GONE);
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

        // Reset Inputs
        tvCaseNumber.setText("");
        tvSerial.setText("");
        tvShipment.setText("");
        tvEmptyWt.setText("");

        tvCenterGrav.setText("");
        tvAftAss.setText("");
        tvAftRetainRing.setText("");
        tvShippingCover.setText("");

        tvShippingPlug.setText("");
        tvEndCap.setText("");
        tvLugs.setText("");
        tvRetainRing.setText("");

        tvAdaptRing.setText("");
        tvImpactRing.setText("");
        tvTotalWt.setText("");
        tvAssCenterGrav.setText("");
    }

    protected void showInputModeDialog(final int tag) {
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
                showFieldInputDialog(tag);
            }
        });
        dialogView.findViewById(R.id.scanCode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                idInputDlg.dismiss();

                if (checkPermissions(mContext, PERMISSION_REQUEST_QRSCAN_STRING, false, PERMISSION_REQUEST_CODE_QRSCAN)) {
                    Intent intent = new Intent(mContext, FullScannerActivity.class);
                    intent.putExtra("data_tag", tag);
                    startActivityForResult(intent, REQUEST_SCAN_DATA);
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

    private void showFieldInputDialog(final int tag) {
        String title = "Field Input";
        String content = "Please input Field value.";
        if (tag == 0) {
            title = "Case Number Input";
            content = "Please input case number.";
        } else if (tag == 1) {
            title = "Serial";
            content = "Please input Serial.";
        } else if (tag == 2) {
            title = "Shipment Input";
            content = "Please input Shipment.";
        } else if (tag == 3) {
            title = "Empty Wt Input";
            content = "Please input Empty Wt.";
        } else if (tag == 4) {
            title = "Center Grav Input";
            content = "Please input Center Grav.";
        } else if (tag == 5) {
            title = "Aft Ass Input";
            content = "Please input Aft Ass.";
        } else if (tag == 6) {
            title = "After Retain Ring Input";
            content = "Please input After Retain Ring.";
        } else if (tag == 7) {
            title = "Shipping Cover Input";
            content = "Please input Shipping Cover.";
        } else if (tag == 8) {
            title = "Shipping Plug Input";
            content = "Please input Shipping Plug.";
        } else if (tag == 9) {
            title = "End Cap Input";
            content = "Please input End Cap.";
        } else if (tag == 10) {
            title = "Lugs Input";
            content = "Please input Lugs.";
        } else if (tag == 11) {
            title = "Retain Ring Input";
            content = "Please input Retain Ring.";
        } else if (tag == 12) {
            title = "Adapt Ring Input";
            content = "Please input Adapt Ring.";
        } else if (tag == 13) {
            title = "Impact Ring Input";
            content = "Please input Impact Ring.";
        } else if (tag == 14) {
            title = "Total Wt Input";
            content = "Please input Total Wt.";
        } else if (tag == 15) {
            title = "Ass Center Grav";
            content = "Please input Ass Center Grav.";
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_id_input, null);
        TextView main_title = dialogView.findViewById(R.id.main_title);
        main_title.setText(title);

        TextView sub_content = dialogView.findViewById(R.id.sub_content);
        sub_content.setText(content);

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

                String fieldVal = edtIdInput.getText().toString().trim();
                if (!TextUtils.isEmpty(fieldVal)) {

                    onFieldInput(tag, fieldVal);
                }
            }
        });

        loginIdInputDlg.setCanceledOnTouchOutside(false);
        loginIdInputDlg.setCancelable(false);
        loginIdInputDlg.show();
        loginIdInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        edtIdInput.requestFocus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Log.e(TAG, "" + requestCode + "-" + resultCode);

            if (requestCode == REQUEST_SCAN_DATA) {
                int dataTag = data.getIntExtra("data_tag", -1);
                String dataCode = data.getStringExtra("code");

                onFieldInput(dataTag, dataCode);
            } else if (requestCode == REQUEST_SCAN_VALUE) {
                String dataCode = data.getStringExtra("code");

                if (edtActiveArea != null) {
                    edtActiveArea.setText(dataCode);
                }
            }
        }
    }

    private void onFieldInput(int tag, String value) {
        if (tag == 0) {
            tvCaseNumber.setText(value);
        } else if (tag == 1) {
            tvSerial.setText(value);
        } else if (tag == 2) {
            tvShipment.setText(value);
        } else if (tag == 3) {
            tvEmptyWt.setText(value);
        } else if (tag == 4) {
            tvCenterGrav.setText(value);
        } else if (tag == 5) {
            tvAftAss.setText(value);
        } else if (tag == 6) {
            tvAftRetainRing.setText(value);
        } else if (tag == 7) {
            tvShippingCover.setText(value);
        } else if (tag == 8) {
            tvShippingPlug.setText(value);
        } else if (tag == 9) {
            tvEndCap.setText(value);
        } else if (tag == 10) {
            tvLugs.setText(value);
        } else if (tag == 11) {
            tvRetainRing.setText(value);
        } else if (tag == 12) {
            tvAdaptRing.setText(value);
        } else if (tag == 13) {
            tvImpactRing.setText(value);
        } else if (tag == 14) {
            tvTotalWt.setText(value);
        } else if (tag == 15) {
            tvAssCenterGrav.setText(value);
        }
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
        ITSRestClient.post(mContext, "getAssembly1Station", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("assemblyProcTime", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        JSONObject jsonStation = response.getJSONObject("station");

                        // Retrieve Time
                        long processingTime = jsonStation.optLong("processing_time");
                        elapsedSeconds = processingTime;
                        btnTimeProcessing.setText(getElapsedTimeMinutesSecondsString(elapsedSeconds));

                        // Usage 1
                        tvCaseNumber.setText(jsonStation.optString("case_number"));
                        tvSerial.setText(jsonStation.optString("serial"));
                        tvShipment.setText(jsonStation.optString("shipment"));
                        tvEmptyWt.setText(jsonStation.optString("empty_wt"));

                        tvCenterGrav.setText(jsonStation.optString("center_grav"));
                        tvAftAss.setText(jsonStation.optString("aft_ass"));
                        tvAftRetainRing.setText(jsonStation.optString("aft_retain_ring"));
                        tvShippingCover.setText(jsonStation.optString("shipping_cover"));

                        tvShippingPlug.setText(jsonStation.optString("shipping_plug"));
                        tvEndCap.setText(jsonStation.optString("end_cap"));
                        tvLugs.setText(jsonStation.optString("lugs"));
                        tvRetainRing.setText(jsonStation.optString("retain_ring"));

                        tvAdaptRing.setText(jsonStation.optString("adapt_ring"));
                        tvImpactRing.setText(jsonStation.optString("impact_ring"));
                        tvTotalWt.setText(jsonStation.optString("total_wt"));
                        tvAssCenterGrav.setText(jsonStation.optString("ass_center_grav"));

                        // Usage 2
                        tvDegreaseSol.setText(jsonStation.optString("degrease_sol"));
                        tvCorrosinPrevCompound.setText(jsonStation.optString("corrosin_prev_compound"));
                        tvShipCoverOring.setText(jsonStation.optString("ship_cover_oring"));
                        tvOringGrease.setText(jsonStation.optString("oring_grease"));

                        tvProtectiveEndCap.setText(jsonStation.optString("protective_end_cap"));
                        tvEndCapSetScrew.setText(jsonStation.optString("end_cap_set_screw"));
                        tvLiftingLugBolt.setText(jsonStation.optString("lifting_lug_bolt"));
                        tvLiftingLugWasher.setText(jsonStation.optString("lifting_lug_washer"));

                        tvStencilInk.setText(jsonStation.optString("stencil_ink"));
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

            // Usage 1
            requestParams.put("case_number", tvCaseNumber.getText().toString().trim());
            requestParams.put("serial", tvSerial.getText().toString().trim());
            requestParams.put("shipment", tvShipment.getText().toString().trim());
            requestParams.put("empty_wt", tvEmptyWt.getText().toString().trim());

            requestParams.put("center_grav", tvCenterGrav.getText().toString().trim());
            requestParams.put("aft_ass", tvAftAss.getText().toString().trim());
            requestParams.put("aft_retain_ring", tvAftRetainRing.getText().toString().trim());
            requestParams.put("shipping_cover", tvShippingCover.getText().toString().trim());

            requestParams.put("shipping_plug", tvShippingPlug.getText().toString().trim());
            requestParams.put("end_cap", tvEndCap.getText().toString().trim());
            requestParams.put("lugs", tvLugs.getText().toString().trim());
            requestParams.put("retain_ring", tvRetainRing.getText().toString().trim());

            requestParams.put("adapt_ring", tvAdaptRing.getText().toString().trim());
            requestParams.put("impact_ring", tvImpactRing.getText().toString().trim());
            requestParams.put("total_wt", tvTotalWt.getText().toString().trim());
            requestParams.put("ass_center_grav", tvAssCenterGrav.getText().toString().trim());

            // Usage 2
            requestParams.put("degrease_sol", tvDegreaseSol.getText().toString().trim());
            requestParams.put("corrosin_prev_compound", tvCorrosinPrevCompound.getText().toString().trim());
            requestParams.put("ship_cover_oring", tvShipCoverOring.getText().toString().trim());
            requestParams.put("oring_grease", tvOringGrease.getText().toString().trim());

            requestParams.put("protective_end_cap", tvProtectiveEndCap.getText().toString().trim());
            requestParams.put("end_cap_set_screw", tvEndCapSetScrew.getText().toString().trim());
            requestParams.put("lifting_lug_bolt", tvLiftingLugBolt.getText().toString().trim());
            requestParams.put("lifting_lug_washer", tvLiftingLugWasher.getText().toString().trim());

            requestParams.put("stencil_ink", tvStencilInk.getText().toString().trim());

            GoogleCertProvider.install(mContext);
            ITSRestClient.post(mContext, "postAssembly1Station", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    hideProgressDialog();
                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("assemblyProcTime", response.toString());

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
