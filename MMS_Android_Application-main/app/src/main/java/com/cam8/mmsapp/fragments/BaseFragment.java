package com.cam8.mmsapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cam8.mmsapp.AppSettings;
import com.cam8.mmsapp.BaseActivity;
import com.cam8.mmsapp.FullScannerActivity;
import com.cam8.mmsapp.MyApplication;
import com.cam8.mmsapp.R;
import com.cam8.mmsapp.alarm.AlarmSettings;
import com.cam8.mmsapp.model.RegimeData;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;


public class BaseFragment extends Fragment {

    protected static final String TEXT_FRAGMENT = "BaseFrg";
    protected static final String DATA_FRAGMENT = "DataFrg";

    protected String TAG = "base";

    protected Context mContext;
    protected BaseActivity parentActivity;
    protected MyApplication mMyApp;
    protected AppSettings appSettings;

    protected static final int PERMISSION_REQUEST_CODE_LOCATION = 101;
    protected static final String[] PERMISSION_REQUEST_LOCATION_STRING = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    protected static final int PERMISSION_REQUEST_CODE_QRSCAN = 103;
    protected static final String[] PERMISSION_REQUEST_QRSCAN_STRING = {Manifest.permission.CAMERA};

    protected static final int REQUEST_SCAN_PARTID = 700;
    protected static final int REQUEST_SCAN_USERID = 800;
    protected static final int REQUEST_SCAN_DATA = 900;
    protected static final int REQUEST_SCAN_VALUE = 1000;


    public static BaseFragment newInstance(String text) {
        BaseFragment mFragment = new BaseFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    protected void init(Bundle bundle) {

        mContext = getActivity();
        parentActivity = (BaseActivity) getActivity();
        mMyApp = (MyApplication) parentActivity.getApplication();
        appSettings = new AppSettings(mContext);

        Log.e("FragClasses", this.getClass().getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.activity_splash, container, false);

        init(getArguments());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateFields();

        LocalBroadcastManager.getInstance(mContext).
                registerReceiver(appUsageBroadcastReceiver, new IntentFilter(AlarmSettings.ACTION_VALID_STATUS_UPDATED));
    }

    @Override
    public void onStop() {
        super.onStop();
        saveFields();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LocalBroadcastManager.getInstance(mContext).
                unregisterReceiver(appUsageBroadcastReceiver);
    }

    //--------------------------------------App Disable Feature ---------------------------------------
    BroadcastReceiver appUsageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra("STATUS", AlarmSettings.STATUS_VALIDATE_USAGE);

            if (action == AlarmSettings.STATUS_MAIN_LOGIN_LOGOUT) {
                onUserInfoUpdated();
            } else if(action == AlarmSettings.STATUS_MAIN_PARTS_COUNT_CHANGED) {
                onPartsCountUpdated();
            }
        }
    };

    // Abstract Functions, DO NOT REMOVE -----------------------------------------------------------
    protected void updateFields() {
        // Abstract Function
    }

    public void saveFields() {
        // Abstract Function
    }

    // This is Called After get new part ID in the Current Fragment
    protected void onPartID(String partID) {
        // Abstract Function
        // Notify InCycle Status
    }

    // Record part id in the current shift data
    protected void notifyPartID(String partID) {
        Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
        intent.putExtra("STATUS", AlarmSettings.STATUS_NEW_PARTID);
        intent.putExtra("PARTID", partID);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    // This is Called After login/logout in the OEE Dashboard(MainActivity)
    protected void onUserInfoUpdated() {
        // Abstract Function
    }

    // This is Called After login in the Current Fragment
    protected void onLogined() {
        // Abstract Function
    }

    // This is Called After logout in the Current Fragment
    protected void onLogout() {
        // Abstract Function
    }

    // This is Called After parts count is changes in the OEE Dashboard(MainActivity)
    protected void onPartsCountUpdated() {
        // Abstract Function
    }

    // ---------------------------------------------------------------------------------------------

    public boolean isAllValidField() {
        // Abstract Function
        return true;
    }

    protected void fillEditTextWithValue(TextView edt, String value) {
        if (edt != null && !TextUtils.isEmpty(value.trim())) {
            edt.setText(value.trim());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "onResume");
        // Set title bar
    }

    protected void showProgressDialog() {
        parentActivity.showProgressDialog();
    }

    protected void hideProgressDialog() {
        parentActivity.hideProgressDialog();
    }

    protected void showToastMessage(String msg) {
        parentActivity.showToastMessage(msg);
    }

    protected void showToastMessage(int msgId) {
        parentActivity.showToastMessage(msgId);
    }

    protected void showAlert(String msg) {
        parentActivity.showAlert(msg);
    }

    protected void showAlert(String msg, View.OnClickListener listener) {
        parentActivity.showAlert(msg, listener);
    }

    protected void showAlert(int msgId) {
        parentActivity.showAlert(msgId);
    }

    // Remove EditText Keyboard
    protected void hideKeyboard(EditText et) {
        if (et != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    public static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    // This will be used in Android6.0(Marshmallow) or above
    public static boolean checkPermissions(Context context, String[] permissions, boolean showHintMessage, int requestCode) {

        if (permissions == null || permissions.length == 0)
            return true;

        boolean allPermissionSetted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionSetted = false;
                break;
            }
        }

        if (allPermissionSetted)
            return true;

        // Should we show an explanation?
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                shouldShowRequestPermissionRationale = true;
                break;
            }
        }

        if (showHintMessage && shouldShowRequestPermissionRationale) {
            // Show an expanation to the user *asynchronously* -- don't
            // block
            // this thread waiting for the user's response! After the
            // user
            // sees the explanation, try again to request the
            // permission.
            String strPermissionHint = context.getString(R.string.request_permission_hint);
            Toast.makeText(context, strPermissionHint, Toast.LENGTH_SHORT).show();
        }

        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);

        return false;
    }

    public void updateRegimeData(RegimeData regimeData) {}


    protected String getElapsedTimeMinutesSecondsString(long miliseconds) {
        long elapsedTime = miliseconds;
        String format = String.format("%%0%dd", 2);

        elapsedTime = elapsedTime / 1000;

        int hour = (int) (elapsedTime / 3600);
        int min = (int) ((elapsedTime - hour * 3600) / 60);
        int sec = (int) (elapsedTime - hour * 3600 - min * 60);

        /*String seconds = String.format(format, sec);
        String hours = String.format(format, hour);
        String minutes = String.format(format, min);
        return hours + ":" + minutes + ":" + seconds;
        */

        return String.format("%02d:%02d:%02d", hour, min, sec);
    }

    // Part ID Login Module ------------------------------------------------------------------------
    protected void getPartID() {
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
                showPartIDCodeInputDialog();
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

    private void showPartIDCodeInputDialog() {
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

                    onPartID(loginID);
                }
            }
        });

        loginIdInputDlg.setCanceledOnTouchOutside(false);
        loginIdInputDlg.setCancelable(false);
        loginIdInputDlg.show();
        loginIdInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        edtIdInput.requestFocus();
    }
    // ---------------------------------------------------------------------------------------------


    // User Login Module ---------------------------------------------------------------------------
    protected void gotoLogin() {
        // JobID Input Channel
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_id_select, null);
        AlertDialog idInputDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        TextView tvTitle = dialogView.findViewById(R.id.title);
        tvTitle.setText(R.string.dialog_select_login_mode);
        dialogView.findViewById(R.id.inputCode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                idInputDlg.dismiss();

                // Login ID Input Dialog
                showUserIDCodeInputDialog();
            }
        });
        dialogView.findViewById(R.id.scanCode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                idInputDlg.dismiss();

                if (checkPermissions(mContext, PERMISSION_REQUEST_QRSCAN_STRING, false, PERMISSION_REQUEST_CODE_QRSCAN)) {
                    startActivityForResult(new Intent(mContext, FullScannerActivity.class), REQUEST_SCAN_USERID);
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

    private void showUserIDCodeInputDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_id_input, null);
        TextView main_title = dialogView.findViewById(R.id.main_title);
        main_title.setText(R.string.title_input_login_id);

        TextView sub_content = dialogView.findViewById(R.id.sub_content);
        sub_content.setText(R.string.msg_input_login_id);

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
                    getUserInfo(loginID);
                }
            }
        });

        loginIdInputDlg.setCanceledOnTouchOutside(false);
        loginIdInputDlg.setCancelable(false);
        loginIdInputDlg.show();
        loginIdInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        edtIdInput.requestFocus();
    }

    private void getUserInfo(String userId) {

        // Call Web API
        Log.e(TAG, "Login");

        // To Do List
        // API name loginWithCustomerId.php
        // Params : customerid, deviceid
        // Response : {status : true, data : {username : "User1", avatar : "http://..."}}

        showProgressDialog();

        RequestParams requestParams = new RequestParams();
        requestParams.put("userId", userId);
        requestParams.put("deviceId", mMyApp.getAndroidId());

        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "loginWithUserId", requestParams, new JsonHttpResponseHandler() {
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
                        String name = jsonData.getString("username_full");
                        String logo = jsonData.getString("user_picture");
                        int userLevel = jsonData.optInt("security_level");

                        // Save User Information
                        appSettings.setUserId(userId);
                        appSettings.setUserName(name);
                        appSettings.setUserAvatar(logo/*"https://i1.sndcdn.com/avatars-000002504134-byor70-t300x300.jpg"*/);
                        appSettings.setUserLevel(userLevel);

                        appSettings.setLoggedIn();

                        onLogined();
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

    // Logout
    protected void logoutUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Confirm logout");
        alertDialogBuilder.setMessage("Would you like to logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                logout();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void logout() {
        // User Information
        appSettings.setUserId("0");
        appSettings.setUserName("Unattended");
        appSettings.setUserAvatar("");
        appSettings.setUserLevel(0);

        appSettings.logOut();

        onLogout();
    }

    protected void broadcastLoginLogoutStatusToParent() {
        // Notify InCycle Status
        Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
        intent.putExtra("STATUS", AlarmSettings.STATUS_PROXY_LOGIN_LOGOUT);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    protected void broadcastPartsCountStatusToParent() {
        // Notify InCycle Status
        Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
        intent.putExtra("STATUS", AlarmSettings.STATUS_PROXY_PARTS_COUNT_CHANGED);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Log.e(TAG, "" + requestCode + "-" + resultCode);

            if (requestCode == REQUEST_SCAN_PARTID) {
                onPartID(data.getStringExtra("code"));
            } else if (requestCode == REQUEST_SCAN_USERID) {
                getUserInfo(data.getStringExtra("code"));
            }
        }
    }
}
