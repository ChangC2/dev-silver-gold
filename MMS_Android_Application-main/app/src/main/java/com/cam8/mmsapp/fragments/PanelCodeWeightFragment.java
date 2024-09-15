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

public class PanelCodeWeightFragment extends BaseFragment implements View.OnClickListener {

    // User Information
    ImageView ivAvatar;
    TextView tvUserName;

    ImageLoader imageLoader;
    DisplayImageOptions imageOptionsWithUserAvatar;

    DatabaseHelper dbHelper;

    public static PanelCodeWeightFragment newInstance(String text) {
        PanelCodeWeightFragment mFragment = new PanelCodeWeightFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_panelcodeweight, container, false);

        TAG = "panelcodeweight";

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

        // User Info
        ivAvatar = parentView.findViewById(R.id.ivAvatar);
        tvUserName = parentView.findViewById(R.id.tvUserName);
        parentView.findViewById(R.id.btnScanUser).setOnClickListener(this);
        parentView.findViewById(R.id.btnLogoutUser).setOnClickListener(this);

        // Stage Button Options
        parentView.findViewById(R.id.btnStage1).setOnClickListener(this);
        parentView.findViewById(R.id.btnStage2).setOnClickListener(this);
        parentView.findViewById(R.id.btnStage3).setOnClickListener(this);
        parentView.findViewById(R.id.btnStage4).setOnClickListener(this);
        parentView.findViewById(R.id.btnStage5).setOnClickListener(this);
        parentView.findViewById(R.id.btnStage6).setOnClickListener(this);
        parentView.findViewById(R.id.btnStage7).setOnClickListener(this);
        parentView.findViewById(R.id.btnStage8).setOnClickListener(this);
        parentView.findViewById(R.id.btnStage9).setOnClickListener(this);

        parentView.findViewById(R.id.btnDataLogSheet).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        showUserInfo();
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

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        // Part ID Login / Logout
        if (viewId == R.id.btnScanUser) {
            gotoLogin();
        } else if (viewId == R.id.btnLogoutUser) {
            logoutUser();
        } else if (viewId == R.id.btnStage1) {

        } else if (viewId == R.id.btnStage2) {

        } else if (viewId == R.id.btnStage3) {

        } else if (viewId == R.id.btnStage4) {

        } else if (viewId == R.id.btnStage5) {

        } else if (viewId == R.id.btnStage6) {

        } else if (viewId == R.id.btnStage7) {

        } else if (viewId == R.id.btnStage8) {

        } else if (viewId == R.id.btnStage9) {

        }
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


}
