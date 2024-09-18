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

public class AssemblyStation136Fragment extends BaseFragment implements View.OnClickListener {

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
    TextView tvBasePlat11;
    TextView tv20199266_fwd_fuze_line;
    TextView tv20199367_aft_fuz_line;
    TextView tv1265394_fit_chrg_tube;

    TextView tv1252629_washer_lock_iternal_tooth2;
    TextView tv4902493_retainer_fuz_liner_aft;
    TextView tv1123646_nut_fit_charg_tube2;
    TextView tv20199361_fwd_chrg_tube;

    TextView tv20199361_030_aft_charg_tube;


    // Assembly Usage2
    View layoutUsage2;
    TextView tv4512424_cap_shipping;
    TextView tvnas1149f0832p_flat_washer8;
    TextView tvnas568_41_hex_head_bolt8;
    TextView tvx20173251_lug_shipping2;

    TextView tv20199362_charge_tube_plug;
    TextView tvnasm90725_31_screw_cap_hex_head2;
    TextView tvms35338_45_washer_lock_sprg4;
    TextView tvmil_dtl_450_bituminous;

    TextView tvas3582_236_o_ring_small2;

    // Assembly Usage3
    View layoutUsage3;
    TextView tv923as694_o_ring_rubber;
    TextView tvms51964_69_set_screw1;
    TextView tva_a_208_ink_marking_stencil;
    TextView tvmil_prf_63460_gun_oil;

    TextView tvmil_prf_16173_corrision_resistant_grease;
    TextView tvsae_as8660_silicone_lubricant;
    TextView tvmil_prf_680_degreasing_solvent;
    TextView tvShipping_plugs2;


    // Tail Fairing
    View layoutTailFairing;
    TextView tvJobAt;
    TextView tvscrew_lot6;
    TextView tvthreadlock_271_lot;
    TextView tvset_screw_lot_6;

    TextView tvams_s_8802_lot;
    TextView tvtwo_part_polysulfie_sealant;


    RadioButton radioInputBarcode;
    EditText edtActiveArea;

    public static AssemblyStation136Fragment newInstance(String text) {
        AssemblyStation136Fragment mFragment = new AssemblyStation136Fragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assemblystation136, container, false);

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
        parentView.findViewById(R.id.tvUsage3).setOnClickListener(this);
        parentView.findViewById(R.id.tvTailFairing).setOnClickListener(this);

        // Layout Usage1
        layoutUsage1 = parentView.findViewById(R.id.layoutUsage1);
        layoutUsage1.setOnClickListener(this);
        parentView.findViewById(R.id.btnCloseUsage1).setOnClickListener(this);

        tvBasePlat11 = parentView.findViewById(R.id.tvBasePlat11);
        tv20199266_fwd_fuze_line = parentView.findViewById(R.id.tv20199266_fwd_fuze_line);
        tv20199367_aft_fuz_line = parentView.findViewById(R.id.tv20199367_aft_fuz_line);
        tv1265394_fit_chrg_tube = parentView.findViewById(R.id.tv1265394_fit_chrg_tube);

        tv1252629_washer_lock_iternal_tooth2 = parentView.findViewById(R.id.tv1252629_washer_lock_iternal_tooth2);
        tv4902493_retainer_fuz_liner_aft = parentView.findViewById(R.id.tv4902493_retainer_fuz_liner_aft);
        tv1123646_nut_fit_charg_tube2 = parentView.findViewById(R.id.tv1123646_nut_fit_charg_tube2);
        tv20199361_fwd_chrg_tube = parentView.findViewById(R.id.tv20199361_fwd_chrg_tube);

        tv20199361_030_aft_charg_tube = parentView.findViewById(R.id.tv20199361_030_aft_charg_tube);

        // Layout Usage2
        layoutUsage2 = parentView.findViewById(R.id.layoutUsage2);
        layoutUsage2.setOnClickListener(this);
        parentView.findViewById(R.id.btnCloseUsage2).setOnClickListener(this);

        tv4512424_cap_shipping = parentView.findViewById(R.id.tv4512424_cap_shipping);
        tvnas1149f0832p_flat_washer8 = parentView.findViewById(R.id.tvnas1149f0832p_flat_washer8);
        tvnas568_41_hex_head_bolt8 = parentView.findViewById(R.id.tvnas568_41_hex_head_bolt8);
        tvx20173251_lug_shipping2 = parentView.findViewById(R.id.tvx20173251_lug_shipping2);

        tv20199362_charge_tube_plug = parentView.findViewById(R.id.tv20199362_charge_tube_plug);
        tvnasm90725_31_screw_cap_hex_head2 = parentView.findViewById(R.id.tvnasm90725_31_screw_cap_hex_head2);
        tvms35338_45_washer_lock_sprg4 = parentView.findViewById(R.id.tvms35338_45_washer_lock_sprg4);
        tvmil_dtl_450_bituminous = parentView.findViewById(R.id.tvmil_dtl_450_bituminous);

        tvas3582_236_o_ring_small2 = parentView.findViewById(R.id.tvas3582_236_o_ring_small2);

        // Layout Usage3
        layoutUsage3 = parentView.findViewById(R.id.layoutUsage3);
        layoutUsage3.setOnClickListener(this);
        parentView.findViewById(R.id.btnCloseUsage3).setOnClickListener(this);

        tv923as694_o_ring_rubber = parentView.findViewById(R.id.tv923as694_o_ring_rubber);
        tvms51964_69_set_screw1 = parentView.findViewById(R.id.tvms51964_69_set_screw1);
        tva_a_208_ink_marking_stencil = parentView.findViewById(R.id.tva_a_208_ink_marking_stencil);
        tvmil_prf_63460_gun_oil = parentView.findViewById(R.id.tvmil_prf_63460_gun_oil);

        tvmil_prf_16173_corrision_resistant_grease = parentView.findViewById(R.id.tvmil_prf_16173_corrision_resistant_grease);
        tvsae_as8660_silicone_lubricant = parentView.findViewById(R.id.tvsae_as8660_silicone_lubricant);
        tvmil_prf_680_degreasing_solvent = parentView.findViewById(R.id.tvmil_prf_680_degreasing_solvent);
        tvShipping_plugs2 = parentView.findViewById(R.id.tvShipping_plugs2);

        // Layout Tail Fairing
        layoutTailFairing = parentView.findViewById(R.id.layoutTailFairing);
        layoutTailFairing.setOnClickListener(this);
        parentView.findViewById(R.id.btnCloseUsage4).setOnClickListener(this);

        tvJobAt = parentView.findViewById(R.id.tvJobAt);
        tvscrew_lot6 = parentView.findViewById(R.id.tvscrew_lot6);
        tvthreadlock_271_lot = parentView.findViewById(R.id.tvthreadlock_271_lot);
        tvset_screw_lot_6 = parentView.findViewById(R.id.tvset_screw_lot_6);

        tvams_s_8802_lot = parentView.findViewById(R.id.tvams_s_8802_lot);
        tvtwo_part_polysulfie_sealant = parentView.findViewById(R.id.tvtwo_part_polysulfie_sealant);

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

        // Layout Usage1
        tvBasePlat11.setOnTouchListener(touchListener);
        tv20199266_fwd_fuze_line.setOnTouchListener(touchListener);
        tv20199367_aft_fuz_line.setOnTouchListener(touchListener);
        tv1265394_fit_chrg_tube.setOnTouchListener(touchListener);

        tv1252629_washer_lock_iternal_tooth2.setOnTouchListener(touchListener);
        tv4902493_retainer_fuz_liner_aft.setOnTouchListener(touchListener);
        tv1123646_nut_fit_charg_tube2.setOnTouchListener(touchListener);
        tv20199361_fwd_chrg_tube.setOnTouchListener(touchListener);

        tv20199361_030_aft_charg_tube.setOnTouchListener(touchListener);

        // Layout Usage2
        tv4512424_cap_shipping.setOnTouchListener(touchListener);
        tvnas1149f0832p_flat_washer8.setOnTouchListener(touchListener);
        tvnas568_41_hex_head_bolt8.setOnTouchListener(touchListener);
        tvx20173251_lug_shipping2.setOnTouchListener(touchListener);

        tv20199362_charge_tube_plug.setOnTouchListener(touchListener);
        tvnasm90725_31_screw_cap_hex_head2.setOnTouchListener(touchListener);
        tvms35338_45_washer_lock_sprg4.setOnTouchListener(touchListener);
        tvmil_dtl_450_bituminous.setOnTouchListener(touchListener);

        tvas3582_236_o_ring_small2.setOnTouchListener(touchListener);

        // Layout Usage3
        tv923as694_o_ring_rubber.setOnTouchListener(touchListener);
        tvms51964_69_set_screw1.setOnTouchListener(touchListener);
        tva_a_208_ink_marking_stencil.setOnTouchListener(touchListener);
        tvmil_prf_63460_gun_oil.setOnTouchListener(touchListener);

        tvmil_prf_16173_corrision_resistant_grease.setOnTouchListener(touchListener);
        tvsae_as8660_silicone_lubricant.setOnTouchListener(touchListener);
        tvmil_prf_680_degreasing_solvent.setOnTouchListener(touchListener);
        tvShipping_plugs2.setOnTouchListener(touchListener);

        // Layout Tail Fairing
        tvJobAt.setOnTouchListener(touchListener);
        tvscrew_lot6.setOnTouchListener(touchListener);
        tvthreadlock_271_lot.setOnTouchListener(touchListener);
        tvset_screw_lot_6.setOnTouchListener(touchListener);

        tvams_s_8802_lot.setOnTouchListener(touchListener);
        tvtwo_part_polysulfie_sealant.setOnTouchListener(touchListener);

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
        } else if (viewId == R.id.tvUsage1) {
            layoutUsage1.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.tvUsage2) {
            layoutUsage2.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.tvUsage3) {
            layoutUsage3.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.tvTailFairing) {
            layoutTailFairing.setVisibility(View.VISIBLE);
        } else if (viewId == R.id.btnCloseUsage1) {
            layoutUsage1.setVisibility(View.GONE);
        } else if (viewId == R.id.btnCloseUsage2) {
            layoutUsage2.setVisibility(View.GONE);
        } else if (viewId == R.id.btnCloseUsage3) {
            layoutUsage3.setVisibility(View.GONE);
        } else if (viewId == R.id.btnCloseUsage4) {
            layoutTailFairing.setVisibility(View.GONE);
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
        tvBasePlat11.setText("");
        tvJobAt.setText("");
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
        ITSRestClient.post(mContext, "getBlu136Assembly", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("assembly2ProcTime", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        JSONObject jsonStation = response.getJSONObject("station");

                        // Retrieve Time
                        long processingTime = jsonStation.optLong("processing_time");
                        elapsedSeconds = processingTime;
                        btnTimeProcessing.setText(getElapsedTimeMinutesSecondsString(elapsedSeconds));

                        // Usage 1
                        tvBasePlat11.setText(jsonStation.optString("base_plat11"));
                        tv20199266_fwd_fuze_line.setText(jsonStation.optString("20199266_fwd_fuze_line"));
                        tv20199367_aft_fuz_line.setText(jsonStation.optString("20199367_aft_fuz_line"));
                        tv1265394_fit_chrg_tube.setText(jsonStation.optString("1265394_fit_chrg_tube"));

                        tv1252629_washer_lock_iternal_tooth2.setText(jsonStation.optString("1252629_washer_lock_iternal_tooth2"));
                        tv4902493_retainer_fuz_liner_aft.setText(jsonStation.optString("4902493_retainer_fuz_liner_aft"));
                        tv1123646_nut_fit_charg_tube2.setText(jsonStation.optString("1123646_nut_fit_charg_tube2"));
                        tv20199361_fwd_chrg_tube.setText(jsonStation.optString("20199361_fwd_chrg_tube"));

                        tv20199361_030_aft_charg_tube.setText(jsonStation.optString("20199361_030_aft_charg_tube"));


                        // Usage 2
                        tv4512424_cap_shipping.setText(jsonStation.optString("4512424_cap_shipping"));
                        tvnas1149f0832p_flat_washer8.setText(jsonStation.optString("nas1149f0832p_flat_washer8"));
                        tvnas568_41_hex_head_bolt8.setText(jsonStation.optString("nas568_41_hex_head_bolt8"));
                        tvx20173251_lug_shipping2.setText(jsonStation.optString("x20173251_lug_shipping2"));

                        tv20199362_charge_tube_plug.setText(jsonStation.optString("20199362_charge_tube_plug"));
                        tvnasm90725_31_screw_cap_hex_head2.setText(jsonStation.optString("nasm90725_31_screw_cap_hex_head2"));
                        tvms35338_45_washer_lock_sprg4.setText(jsonStation.optString("ms35338_45_washer_lock_sprg4"));
                        tvmil_dtl_450_bituminous.setText(jsonStation.optString("mil_dtl_450_bituminous"));

                        tvas3582_236_o_ring_small2.setText(jsonStation.optString("as3582_236_o_ring_small2"));

                        // Usage 3
                        tv923as694_o_ring_rubber.setText(jsonStation.optString("923as694_o_ring_rubber"));
                        tvms51964_69_set_screw1.setText(jsonStation.optString("ms51964_69_set_screw1"));
                        tva_a_208_ink_marking_stencil.setText(jsonStation.optString("a_a_208_ink_marking_stencil"));
                        tvmil_prf_63460_gun_oil.setText(jsonStation.optString("mil_prf_63460_gun_oil"));

                        tvmil_prf_16173_corrision_resistant_grease.setText(jsonStation.optString("mil_prf_16173_corrision_resistant_grease"));
                        tvsae_as8660_silicone_lubricant.setText(jsonStation.optString("sae_as8660_silicone_lubricant"));
                        tvmil_prf_680_degreasing_solvent.setText(jsonStation.optString("mil_prf_680_degreasing_solvent"));
                        tvShipping_plugs2.setText(jsonStation.optString("shipping_plugs2"));

                        // Tail Fairing
                        tvJobAt.setText(jsonStation.optString("job_at"));
                        tvscrew_lot6.setText(jsonStation.optString("screw_lot6"));
                        tvthreadlock_271_lot.setText(jsonStation.optString("threadlock_271_lot"));
                        tvset_screw_lot_6.setText(jsonStation.optString("set_screw_lot_6"));

                        tvams_s_8802_lot.setText(jsonStation.optString("ams_s_8802_lot"));
                        tvtwo_part_polysulfie_sealant.setText(jsonStation.optString("two_part_polysulfie_sealant"));

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

            // Usage 1
            requestParams.put("base_plat11", tvBasePlat11.getText().toString().trim());
            requestParams.put("20199266_fwd_fuze_line", tv20199266_fwd_fuze_line.getText().toString().trim());
            requestParams.put("20199367_aft_fuz_line", tv20199367_aft_fuz_line.getText().toString().trim());
            requestParams.put("1265394_fit_chrg_tube", tv1265394_fit_chrg_tube.getText().toString().trim());

            requestParams.put("1252629_washer_lock_iternal_tooth2", tv1252629_washer_lock_iternal_tooth2.getText().toString().trim());
            requestParams.put("4902493_retainer_fuz_liner_aft", tv4902493_retainer_fuz_liner_aft.getText().toString().trim());
            requestParams.put("1123646_nut_fit_charg_tube2", tv1123646_nut_fit_charg_tube2.getText().toString().trim());
            requestParams.put("20199361_fwd_chrg_tube", tv20199361_fwd_chrg_tube.getText().toString().trim());

            requestParams.put("20199361_030_aft_charg_tube", tv20199361_030_aft_charg_tube.getText().toString().trim());


            // Usage 2
            requestParams.put("4512424_cap_shipping", tv4512424_cap_shipping.getText().toString().trim());
            requestParams.put("nas1149f0832p_flat_washer8", tvnas1149f0832p_flat_washer8.getText().toString().trim());
            requestParams.put("nas568_41_hex_head_bolt8", tvnas568_41_hex_head_bolt8.getText().toString().trim());
            requestParams.put("x20173251_lug_shipping2", tvx20173251_lug_shipping2.getText().toString().trim());

            requestParams.put("20199362_charge_tube_plug", tv20199362_charge_tube_plug.getText().toString().trim());
            requestParams.put("nasm90725_31_screw_cap_hex_head2", tvnasm90725_31_screw_cap_hex_head2.getText().toString().trim());
            requestParams.put("ms35338_45_washer_lock_sprg4", tvms35338_45_washer_lock_sprg4.getText().toString().trim());
            requestParams.put("mil_dtl_450_bituminous", tvmil_dtl_450_bituminous.getText().toString().trim());

            requestParams.put("as3582_236_o_ring_small2", tvas3582_236_o_ring_small2.getText().toString().trim());

            // usage 3
            requestParams.put("923as694_o_ring_rubber", tv923as694_o_ring_rubber.getText().toString().trim());
            requestParams.put("ms51964_69_set_screw1", tvms51964_69_set_screw1.getText().toString().trim());
            requestParams.put("a_a_208_ink_marking_stencil", tva_a_208_ink_marking_stencil.getText().toString().trim());
            requestParams.put("mil_prf_63460_gun_oil", tvmil_prf_63460_gun_oil.getText().toString().trim());

            requestParams.put("mil_prf_16173_corrision_resistant_grease", tvmil_prf_16173_corrision_resistant_grease.getText().toString().trim());
            requestParams.put("sae_as8660_silicone_lubricant", tvsae_as8660_silicone_lubricant.getText().toString().trim());
            requestParams.put("mil_prf_680_degreasing_solvent", tvmil_prf_680_degreasing_solvent.getText().toString().trim());
            requestParams.put("shipping_plugs2", tvShipping_plugs2.getText().toString().trim());

            // Tail Fairing
            requestParams.put("job_at", tvJobAt.getText().toString().trim());
            requestParams.put("screw_lot6", tvscrew_lot6.getText().toString().trim());
            requestParams.put("threadlock_271_lot", tvthreadlock_271_lot.getText().toString().trim());
            requestParams.put("set_screw_lot_6", tvset_screw_lot_6.getText().toString().trim());

            requestParams.put("ams_s_8802_lot", tvams_s_8802_lot.getText().toString().trim());
            requestParams.put("two_part_polysulfie_sealant", tvtwo_part_polysulfie_sealant.getText().toString().trim());

            GoogleCertProvider.install(mContext);
            ITSRestClient.post(mContext, "postBlu136Assembly", requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    hideProgressDialog();
                    // If the response is JSONObject instead of expected JSONArray
                    Log.e("assembly2ProcTime", response.toString());

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Log.e(TAG, "" + requestCode + "-" + resultCode);

            if (requestCode == REQUEST_SCAN_VALUE) {
                String dataCode = data.getStringExtra("code");

                if (edtActiveArea != null) {
                    edtActiveArea.setText(dataCode);
                }
            }
        }
    }
}
