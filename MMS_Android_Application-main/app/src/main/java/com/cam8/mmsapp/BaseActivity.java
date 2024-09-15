/*
 * Copyright 2018 ShineStar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cam8.mmsapp;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate;
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener;
import com.cam8.mmsapp.network.APIServerUtil;
import com.cam8.mmsapp.report.CheckAppUpdateHelper;
import com.cam8.mmsapp.report.DownloadFileFromURL;
import com.cam8.mmsapp.utils.DateUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity implements OnLocaleChangedListener {

    Context mContext;
    MyApplication mMyApp;

    WeakReference<BaseActivity> weakReference;

    public MyApplication getmMyApp() {
        return mMyApp;
    }

    Typeface mAppFont;
    protected ProgressDialog mProgress;

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    protected Bitmap mBitmap;

    SharedPreferences mSettings;
    protected AppSettings appSettings;

    public AppSettings getAppSettings() {
        return appSettings;
    }

    private boolean activityIsRunning = false;
    protected boolean isActivityRunning() {
        return activityIsRunning;
    }

    String mUserId;
    int mUserType;

    public static final String GLOBAL_SETTING = "spindustry";

    protected static final int REQUEST_ENABLE_BT = 100;
    protected static final int REQUEST_REGISTER = 200;
    protected static final int REQUEST_LOCATION = 300;
    protected static final int REQUEST_PROFILE = 400;

    protected static final int REQUEST_SCAN_JOBID = 500;
    protected static final int REQUEST_SCAN_USER = 600;
    protected static final int REQUEST_SCAN_PARTID = 700;

    // Permission Requests
    protected static final int PERMISSION_REQUEST_CODE_GALLERY = 101;
    protected static final String[] PERMISSION_REQUEST_GALLERY_STRING = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    protected static final int PERMISSION_REQUEST_CODE_LOCATION = 102;
    protected static final String[] PERMISSION_REQUEST_LOCATION_STRING = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static final int PERMISSION_REQUEST_CODE_QRSCAN = 103;
    public static final String[] PERMISSION_REQUEST_QRSCAN_STRING = {Manifest.permission.CAMERA};

    protected static final int PERMISSION_REQUEST_CODE_FILELOGGER = 104;
    protected static final String[] PERMISSION_REQUEST_FILELOGGER_STRING = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final int PERMISSION_REQUEST_CODE_IMAGE = 105;
    public static final String[] PERMISSION_REQUEST_IMAGE_STRING = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final int PERMISSION_REQUEST_CODE_BLUETOOTH = 106;
    public static final String[] PERMISSION_REQUEST_BLUETOOTH_STRING = {Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN};

    public static final int PERMISSION_REQUEST_CODE_BLUETOOTH_CONNECT= 107;
    public static final String[] PERMISSION_REQUEST_BLUETOOTH_CONNECT_STRING = {Manifest.permission.BLUETOOTH_CONNECT};

    // For the Rate Driver after 1 mins
    protected static Activity frontActivityContext;
    protected static Handler rateDriverActionHandler;

    protected String TAG = "AppCommon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        localizationDelegate.addOnLocaleChangedListener(this);
        localizationDelegate.onCreate();

        super.onCreate(savedInstanceState);

        mContext = this;
        mMyApp = (MyApplication) getApplication();

        weakReference = new WeakReference<>(this);

        mSettings = getPreferences(this);
        appSettings = new AppSettings(mContext);

        mProgress = new ProgressDialog(mContext, R.style.DialogTheme);
        mProgress.setMessage(getString(R.string.loading));
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

        activityIsRunning = true;

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase));
        super.attachBaseContext(newBase);
    }

    protected void checkAppVersion(CheckAppUpdateHelper.onVersionCheckCallback callback) {
        // Start App Updates Daily Checker
        final String todayString = DateUtil.toStringFormat_13(new Date());

        //if (!appSettings.getLastUpdateDate().equalsIgnoreCase(todayString)) {

        if (callback == null) {
            // Process App Update in itself
            callback = new CheckAppUpdateHelper.onVersionCheckCallback() {
                @Override
                public void onFailed(String message) {

                    showToastMessage(message);
                }

                @Override
                public void onSuccess(boolean haveUpdates, String newVer) {
                    if (weakReference.get() == null || !isActivityRunning())
                        return;

                    if (haveUpdates) {

                        androidx.appcompat.app.AlertDialog confirmDialog = new androidx.appcompat.app.AlertDialog.Builder(mContext).create();
                        confirmDialog.setIcon(R.mipmap.ic_launcher);
                        confirmDialog.setTitle("Confirm");
                        confirmDialog.setMessage("There is an update, would you download new version?");
                        confirmDialog.setCanceledOnTouchOutside(false);
                        confirmDialog.setCancelable(false);
                        confirmDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", (dialog, which) -> dialog.dismiss());
                        confirmDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", (dialog, which) -> {
                            dialog.dismiss();

                            // Download
                            downloadNewBuild();
                        });
                        confirmDialog.show();
                    }

                    appSettings.setLastUpdateDate(todayString);
                }
            };
        }

        new CheckAppUpdateHelper(BaseActivity.this, callback).execute();
        //}
    }

    protected void downloadNewBuild() {
        // Check Permissions
        if (checkPermissions(mContext, PERMISSION_REQUEST_GALLERY_STRING, true, PERMISSION_REQUEST_CODE_GALLERY)) {
            // Trigger App download Task Here
            File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (MyApplication.TEST_VERSION) {
                // Testing Version
                //new DownloadFileFromURL(mContext, downloadFolder, "appMMS").execute("https://api.slymms.com/Terminals/appMMSTest.apk");
                new DownloadFileFromURL(mContext, downloadFolder, "appMMS").execute(APIServerUtil.getUrl(mContext, APIServerUtil.APIType.updatePackage, "appMMSTest.apk"));
            } else {
                // Product Version
                //new DownloadFileFromURL(mContext, downloadFolder, "appMMS").execute("https://api.slymms.com/Terminals/appMMS.apk");
                new DownloadFileFromURL(mContext, downloadFolder, "appMMS").execute(APIServerUtil.getUrl(mContext, APIServerUtil.APIType.updatePackage, "appMMS.apk"));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        activityIsRunning = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences(GLOBAL_SETTING, Context.MODE_PRIVATE);
    }

    public void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String loadPreferences(String key) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(GLOBAL_SETTING, MODE_PRIVATE);
            String strSavedMemo = sharedPreferences.getString(key, "");
            return strSavedMemo;
        } catch (NullPointerException nullPointerException) {

            return null;
        }
    }

    public String getVersionName() {
        PackageInfo pInfo = null;
        int curVerionCode = 0;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return getString(R.string.app_version);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showProgressDialog() {
        if (mProgress.isShowing())
            return;

        mProgress.show();
        mProgress.setContentView(R.layout.dialog_loading);
    }

    public void hideProgressDialog() {
        if (mProgress.isShowing())
            mProgress.dismiss();
    }

    // Remove EditText Keyboard
    public void hideKeyboard(EditText et) {
        if (et != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = BaseActivity.this.getCurrentFocus();
        if (view == null) {
            view = new View(BaseActivity.this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /****** CHECK NETWORK CONNECTION *******/
    public static boolean isOnline(Context conn) {
        ConnectivityManager cm = (ConnectivityManager) conn.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }

        return false;
    }

    public void msg(int resId) {
        String msg = getString(resId);
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setMessage(msg);
        alert.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    public void msg(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        alert.setMessage(msg);
        alert.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alert.show();
    }

    public void closeMsg(int msgId) {
        closeMsg(getString(msgId));
    }

    public void closeMsg(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        alert.setMessage(msg);
        alert.setPositiveButton(getString(R.string.alert_close),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alert.show();
    }

    public void showToastMessage(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToastMessage(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public void showToastMessage(int msgId) {
        Toast.makeText(mContext, msgId, Toast.LENGTH_SHORT).show();
    }

    public void showLongToastMessage(int msgId) {
        Toast.makeText(mContext, msgId, Toast.LENGTH_LONG).show();
    }

    public void showAlert(int resId) {
        String alertMsg = getString(resId);
        showAlert(alertMsg, null);
    }

    public void showAlert(int resId, View.OnClickListener clickListener) {
        String alertMsg = getString(resId);
        showAlert(alertMsg, clickListener);
    }

    public void showAlert(String message) {
        showAlert(message, null);
    }

    public void showAlert(String message, final View.OnClickListener clickListener) {

        if (!isActivityRunning())
            return;

        if (TextUtils.isEmpty(message))
            return;

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_error, null);

        final AlertDialog errorDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        TextView tvAlert = (TextView) dialogView.findViewById(R.id.tvAlert);
        tvAlert.setText(message);
        dialogView.findViewById(R.id.btnCloseAlert).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                errorDlg.dismiss();
                if (clickListener != null) {
                    clickListener.onClick(v);
                }
            }
        });

        errorDlg.show();
        errorDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //*****************************************************************
    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public boolean isPasswordValid(String password) {
        return password.length() >= 5;
    }

    public boolean isEmailValid(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    protected void openLink(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    protected String getAndroidId() {
        TelephonyManager tm =
                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("ID", "Android ID: " + androidId);
        return androidId;
    }

    protected boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (gps_enabled || network_enabled) {
            return true;
        } else {
            return false;
        }
    }

    public Uri getFileUri(File file) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Uri fileUri = Uri.fromFile(file);
            return fileUri;
        } else {
            Uri fileUri = FileProvider.getUriForFile(
                    getApplicationContext(),
                    "com.cam8.mmsapp.provider", // (use your app signature + ".provider" )
                    file);
            return fileUri;
        }
    }

    /**
     * Function to show settings alert dialog
     */
    public void showLocationSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUEST_LOCATION);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int pxToDp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

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

    protected String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd,yy  HH:mm");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek + " " + dateFormat.format(date);
    }

    // User Location
    private static final boolean DEV_MODE = false;

    protected boolean isBluetoothPermissionAllowedOverAndroid12() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkPermissions(mContext, PERMISSION_REQUEST_BLUETOOTH_STRING, true, PERMISSION_REQUEST_CODE_BLUETOOTH)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
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

        if (bAllGranted) {
        } else {
            //showAlert("Need permissions to use function.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        localizationDelegate.onResume(this);

        frontActivityContext = this;
    }

    private LocalizationActivityDelegate localizationDelegate = new LocalizationActivityDelegate(this);

    @Override
    public void onAfterLocaleChanged() {}

    @Override
    public void onBeforeLocaleChanged() {}

    @Override
    public Context getApplicationContext() {
        return localizationDelegate.getApplicationContext(super.getApplicationContext());
    }

    @Override
    public Resources getResources() {
        return localizationDelegate.getResources(super.getResources());
    }

    protected void setLanguage(Locale locale) {
        localizationDelegate.setLanguage(this, locale);
    }
}
