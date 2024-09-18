package com.cam8.mmsapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cam8.mmsapp.adapters.JobListAdapter;
import com.cam8.mmsapp.alarm.AlarmBroadcastReceiver;
import com.cam8.mmsapp.alarm.AlarmSettings;
import com.cam8.mmsapp.bluetooth.SerialListener;
import com.cam8.mmsapp.bluetooth.SerialService;
import com.cam8.mmsapp.bluetooth.SerialSocket;
import com.cam8.mmsapp.fragments.AssemblyStation136Fragment;
import com.cam8.mmsapp.fragments.AssemblyStation137Fragment;
import com.cam8.mmsapp.fragments.AssemblyStation3Fragment;
import com.cam8.mmsapp.fragments.BaseFragment;
import com.cam8.mmsapp.fragments.BlastStationFragment;
import com.cam8.mmsapp.fragments.CleaningStationFragment;
import com.cam8.mmsapp.fragments.GaugeFragment;
import com.cam8.mmsapp.fragments.PaintStationFragment;
import com.cam8.mmsapp.fragments.QualityFragment;
import com.cam8.mmsapp.fragments.TimeReportFragment;
import com.cam8.mmsapp.fragments.UtilizationFragment;
import com.cam8.mmsapp.logger.AndroidLogAdapter;
import com.cam8.mmsapp.logger.CsvFormatStrategy;
import com.cam8.mmsapp.logger.DiskLogAdapter;
import com.cam8.mmsapp.logger.FormatStrategy;
import com.cam8.mmsapp.logger.Logger;
import com.cam8.mmsapp.logger.PrettyFormatStrategy;
import com.cam8.mmsapp.mail.MailSender;
import com.cam8.mmsapp.model.DatabaseHelper;
import com.cam8.mmsapp.model.FaxonStage1;
import com.cam8.mmsapp.model.FaxonStage2;
import com.cam8.mmsapp.model.FaxonStage3;
import com.cam8.mmsapp.model.FaxonStage4;
import com.cam8.mmsapp.model.FaxonStage9;
import com.cam8.mmsapp.model.GanttDataModel;
import com.cam8.mmsapp.model.GanttPlotModel;
import com.cam8.mmsapp.model.MJob;
import com.cam8.mmsapp.model.Maintenance;
import com.cam8.mmsapp.model.RegimeData;
import com.cam8.mmsapp.model.RegimeUtils;
import com.cam8.mmsapp.model.ShiftDataModel;
import com.cam8.mmsapp.model.ShiftTime;
import com.cam8.mmsapp.model.ShiftTimeManager;
import com.cam8.mmsapp.model.TankTemperatureData;
import com.cam8.mmsapp.multilang.IconSelectDialog;
import com.cam8.mmsapp.multilang.SelectableIcon;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.report.ReportService;
import com.cam8.mmsapp.report.StatusReporter;
import com.cam8.mmsapp.utils.AppUtils;
import com.cam8.mmsapp.utils.ChartColorUtils;
import com.cam8.mmsapp.utils.ClickListener;
import com.cam8.mmsapp.utils.DateUtil;
import com.cam8.mmsapp.utils.NetUtil;
import com.cam8.mmsapp.views.PageNavigator;
import com.cam8.mmsapp.views.ThinGanttChart;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity implements View.OnClickListener, ServiceConnection, SerialListener, NavigationView.OnNavigationItemSelectedListener {

    PowerManager.WakeLock wakeLock;

    Timer appTimer;

    TextView tvMachineName;
    TextView tvMachineCateStatus;

    ImageView ivCustomerAvatar;
    TextView txtPLCConnStatus;
    ImageView ivPLCConnStatus;
    TextView txtServerConnStatus;
    ImageView ivServerConnStatus;

    ImageLoader imageLoader;
    DisplayImageOptions imageOptionsWithCustomerAvatar;
    DisplayImageOptions imageOptionsWithUserAvatar;
    ImageView ivAvatar;
    TextView tvUserName;

    View panelJobDetailsInfo;
    TextView tvJobId, tvJobReworkSetupStatus, tvJobDetails;

    ImageView btnJobInfo;
    ImageView btnJobFiles;

    ArrayList<GanttPlotModel> ganttDataList = new ArrayList<>();
    ThinGanttChart ganttChart;
    TextView tvGanttTitle;

    AlphaAnimation alphaAnimation;
    TextView tvSyncTime, tvUnCatTime, tvIdleTime;
    TextView tvDate, tvGood, tvBad;

    boolean mSettingIsAutoPartsCounting = false;
    long mSettingMinElapsedStopTimeForGoodParts = 0;
    int mSettingPartsPerCycle = 0;
    long elapsedStopTimeForPartsCounting = 0;
    boolean bOneTimeCounted = false;

    long mSettingStopTimeLimit = 90 * 1000; // 90 s
    boolean mSettingIsGuestLock = false;
    boolean mSettingIsLoggedIn = false;

    boolean mSettingIsUseCSLock;
    boolean mSettingIsReverseCSLock;
    boolean mSettingIsEnabledCSLockSound;

    int colorGreen = 0xff46c392;
    int colorOrange = 0xffffa300;
    int colorRed = 0xffff0000;

    // Downtime Reason Panel
    View panelIdleSetUp;
    TextView tvElapsedIdleTIme;
    View backCsLockStatus;
    AlphaAnimation blinkAnimation;

    TextView tvTitleStatusInterlocked;

    Button[] btnDownTimeStatus = new Button[8];
    Button btnCloseIdleStatus;
    View panelLoginMessage;

    // Disabled Panel
    View panelDisabled;

    // Maintenance Task Panel
    View panelMaintenance;
    TextView tvTaskMachine;
    TextView tvTaskName;
    TextView tvTaskCategory;
    ImageView ivTaskLogo;
    TextView tvTaskInstruction;
    TextView tvTaskFrequency;
    TextView tvTaskCycleStartInterlock;
    EditText tvTaskNotes;

    DatabaseHelper dbHelper;

    //-------------------------------- Modbus TCP Module -----------------------------------------------
    private static final int DEVICE_STATUS_UNCATEGORIZED = 0;
    private static final int DEVICE_STATUS_INCYCLE = 1;
    private static final int DEVICE_STATUS_IDLE1 = 2;
    private static final int DEVICE_STATUS_IDLE2 = 3;
    private static final int DEVICE_STATUS_IDLE3 = 4;
    private static final int DEVICE_STATUS_IDLE4 = 5;
    private static final int DEVICE_STATUS_IDLE5 = 6;
    private static final int DEVICE_STATUS_IDLE6 = 7;
    private static final int DEVICE_STATUS_IDLE7 = 8;
    private static final int DEVICE_STATUS_IDLE8 = 9;

    private int opDownTimeStatus = DEVICE_STATUS_UNCATEGORIZED;
    private int currentDeviceStatus = DEVICE_STATUS_UNCATEGORIZED;
    private int prevDeviceStatus = currentDeviceStatus;

    private long timeCurrentMilis = 0;
    private long timePrevStatusMilis = 0;

    // Used for Uncategorized Status And Downtime Reason Status
    // In case of Uncate Status, when user click downtime reason,
    // then all prev uncate status is moved to new downtime reason status
    private long timeLastUncatStartTime = 0;

    // Report times
    private static final long INTERVAL_1_SECOND = 800;             // 800 miliseconds
    private static final long INTERVAL_3_MINUTES = 1000 * 60 * 5;   // 5 mins
    private long INTERVAL_REPORT = INTERVAL_1_SECOND;
    private long timeLastReportMilis = 0;

    long[] elapsedMiliseconds = new long[10];
    String[] titleDownTimeReason = new String[8];
    String[] colorCharts = ChartColorUtils.chartColorStrings;

    // CSLock Status, Cycle Start Lock. This is output 1 on the PLC. Cycle Start Lock
    int statusCsLock = 0;
    long elapsedStopTime = 0;

    // “Elapsed idle time” is the time since the machine stopped In Cycle
    long elapsedIdleTime = 0;

    // Regime UI Update
    private RegimeData regimeData = new RegimeData();
    private long timeLastRegimeUpdateTime = 0;
    private static final long INTERVAL_REGIME_UPDATE = 1000 * 15;   // 15 secs

    // Gauge Controls
    ViewPager vpGauge;
    PageNavigator pageNavigator;
    GaugeFragment gaugeFragment;
    UtilizationFragment utilizationFragment;

    // ================= Device Connection management =================
    private static int DEVICE_CONN_CONNECTING = -1;
    private static int DEVICE_CONN_ON = 0;
    private static int DEVICE_CONN_OFF = 1;
    private static int DEVICE_CONN_RETRY = 2;

    private int connectStatus = DEVICE_CONN_CONNECTING;

    private static long DEVICE_RECONNECT_INTERVAL = 15000;

    private static final int MSG_SIGNALCHECKER_PER_SEC = 0;
    private static final int MSG_TIMECOUNTER_PER_SEC = 1;

    // “Elapsed idle time” is the time since the machine stopped In Cycle
    long timeJobDataUpdate = 0;
    private static final int INTERVAL_UPDATE_JOBINFO = 1000 * 60 * 5;

    // Tank Temperature Report
    TankTemperatureData tankTemperatureData;
    FrameLayout containerTimeReport;
    BaseFragment processMonitorFragment;

    // Shift Data
    ShiftDataModel shiftData = new ShiftDataModel();
    ShiftTime currShiftTimeInfo;
    private long timeLastShiftUpdateMilis = 0;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // If Current Dashboard is Time Report Screen, then hide it
            if (containerTimeReport.getVisibility() == View.VISIBLE) {
                hideTimeReportPanel();
            }
        } else if (id == R.id.nav_setting) {
            gotoSettingScreen(false);
        } else if (id == R.id.nav_install_config) {
            startActivity(new Intent(mContext, InstallConfigActivity.class));
        } else if (id == R.id.nav_maintenance) {
            startActivity(new Intent(mContext, MaintenancesActivity.class));
        } else if (id == R.id.nav_timereport) {
            // *Now we use Fragment mode in the main screen, so disable this activity mode
            //startActivity(new Intent(mContext, TimeReportActivity.class));
            showTimeReportPanel();
        } else if (id == R.id.nav_language) {
            ArrayList<SelectableIcon> selectionDialogsColors = new ArrayList<>();
            selectionDialogsColors.add(new SelectableIcon(0, getString(R.string.language_default), R.drawable.ic_lang_default));
            selectionDialogsColors.add(new SelectableIcon(1, getString(R.string.language_en), R.drawable.ic_lang_en));
            //selectionDialogsColors.add(new SelectableIcon(2, getString(R.string.language_cn), R.drawable.ic_lang_chn));
            selectionDialogsColors.add(new SelectableIcon(3, getString(R.string.language_sp), R.drawable.ic_lang_sp));
            new IconSelectDialog.Builder(mContext)
                    .setIcons(selectionDialogsColors)
                    .setTitle(R.string.title_select_lang)
                    .setSortIconsByName(true)
                    .setOnIconSelectedListener(new IconSelectDialog.OnIconSelectedListener() {
                        @Override
                        public void onIconSelected(SelectableIcon selectedItem) {
                            if (selectedItem.getId() == 0) {
                                setLanguage(Locale.ENGLISH);
                            } else if (selectedItem.getId() == 1) {
                                setLanguage(Locale.ENGLISH);
                            } else if (selectedItem.getId() == 2) {
                                setLanguage(Locale.CHINESE);
                            } else if (selectedItem.getId() == 3) {
                                setLanguage(new Locale("es", "ES"));
                            }
                        }
                    })
                    .build().show(getSupportFragmentManager(), "Lang_Select");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //-------------------------------------------------------------------------------------------------

    //----------------------------------- BLE Device Settings -----------------------------------------
    private enum Connected {False, Pending, True}

    String bleDeviceAddress = "";
    private String newline = "\r\n";

    private SerialSocket socket;
    private SerialService service;
    private boolean initialStart = true;
    private Connected connected = Connected.False;

    //-------------------------------------- Maintenance Task -----------------------------------------
    MaintenanceTaskManager maintenanceTaskManager;

    //---------------------------------------- Server Config ------------------------------------------
    boolean isUsingLocal = false;
    String localServerIP = "";

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);

        if (initialStart) {
            initialStart = false;
            runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }

    private void disconnectBLEDevice() {

        if (connected != Connected.False)
            disconnect();

        mBLESignalCheckerHandler.removeMessages(MSG_SIGNALCHECKER_PER_SEC);
        mBLESignalCheckerHandler.removeMessages(MSG_TIMECOUNTER_PER_SEC);

        try {
            // In case of Service is not registered, occurs the exception
            stopService(new Intent(mContext, SerialService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // In case of Service is not unbined, occurs the exception
            unbindService(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectBLEDevice() {
        try {
            Intent intent = new Intent(mContext, SerialService.class);
            bindService(intent, this, Context.BIND_AUTO_CREATE);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(R.string.error_bluetooth_not_connect, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private static final int MSG_DEVICE_CONNECTION_CHECK = 1;

    private long getCurrentTime() {
        //return new Date().getTime();
        return System.currentTimeMillis();
    }

    Object timeLock = new Object();

    Handler mBLEDeviceConnectionCheckHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            BaseActivity myActivity = weakReference.get();
            if (myActivity == null || myActivity.isFinishing()) {
                Log.e("report", "Died!");
                return;
            }

            if (msg.what == MSG_DEVICE_CONNECTION_CHECK) {
                if (connected == Connected.False) {
                    if (!TextUtils.isEmpty(bleDeviceAddress)) {
                        System.gc();

                        logE("DISCONN! Connecting to PLC");
                        connect();
                    }
                }

                removeMessages(MSG_DEVICE_CONNECTION_CHECK);
                sendEmptyMessageDelayed(MSG_DEVICE_CONNECTION_CHECK, DEVICE_RECONNECT_INTERVAL);
            }
        }
    };

    private String getCommand() {
        if (mSettingIsUseCSLock) {
            if (mSettingIsReverseCSLock) {
                //  if stop time > allowed stop time "0,0,0,0", otherwise, 1,0,0,0
                return String.format("%d,%d,%d,%d",
                        1 - statusCsLock,
                        0,
                        statusCsLock,
                        currentDeviceStatus == DEVICE_STATUS_UNCATEGORIZED ? 1 : 0);
            } else {
                //  if stop time > allowed stop time "1,0,0,0", otherwise, 0,0,0,0
                return String.format("%d,%d,%d,%d",
                        statusCsLock,
                        0,
                        statusCsLock,
                        currentDeviceStatus == DEVICE_STATUS_UNCATEGORIZED ? 1 : 0);
            }
        } else {
            // Default "1,0,0,0"
            return String.format("%d,%d,%d,%d",
                    1,
                    0,
                    statusCsLock,
                    currentDeviceStatus == DEVICE_STATUS_UNCATEGORIZED ? 1 : 0);
        }
    }

    Handler mBLESignalCheckerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            BaseActivity myActivity = weakReference.get();
            if (myActivity == null || myActivity.isFinishing()) {
                Log.e("report", "Died!");
                return;
            }

            if (msg.what == MSG_SIGNALCHECKER_PER_SEC) {
                if (connected == Connected.True) {
                    send(getCommand());
                }
                // We call this to avoid message duplication when change the device.
                removeMessages(MSG_SIGNALCHECKER_PER_SEC);
                sendEmptyMessageDelayed(MSG_SIGNALCHECKER_PER_SEC, 5000);
            } else if (msg.what == MSG_TIMECOUNTER_PER_SEC) {
                // Lock Status
                long startTime = getCurrentTime();

                synchronized (timeLock) {
                    timeCurrentMilis = getCurrentTime();

                    // Check Valid Status for Timer
                    long timeGap = (timeCurrentMilis > timePrevStatusMilis ? timeCurrentMilis - timePrevStatusMilis : 0);
                    if (timeLastReportMilis > timeCurrentMilis) {
                        timeLastReportMilis = timeCurrentMilis;
                    }

                    if (timeGap > 3000) {
                        logE("*** Exception Time Gap ***" + timeGap);
                    }

                    timePrevStatusMilis = timeCurrentMilis;

                    // Repeat counting
                    removeMessages(MSG_TIMECOUNTER_PER_SEC);
                    sendEmptyMessageDelayed(MSG_TIMECOUNTER_PER_SEC, 1000);

                    // Counting the Status Timer
                    elapsedMiliseconds[currentDeviceStatus] += timeGap;

                    // Shift Data Logic
                    ShiftTime newShiftInfo = ShiftTimeManager.getInstance().getShiftTime(timeCurrentMilis);
                    String newShiftInfoTime = "";
                    if (newShiftInfo != null) {
                        newShiftInfoTime = newShiftInfo.toString();
                    }
                    String curShiftInfoTime = shiftData.getShiftSetting();

                    boolean isNewShiftData = false;
                    if (!newShiftInfoTime.equals(curShiftInfoTime)) {
                        isNewShiftData = true;
                    }

                    /*if (currShiftTimeInfo != null) {
                        if (newShiftInfo == null) {
                            // Changed to Non-Shift Status, Need to reset shift
                            Log.e("Shift", "Changed to Non-Shift");
                            isNewShiftData = true;
                        } else if (!currShiftTimeInfo.equals(newShiftInfo)) {
                            // Changed to Non-Shift Status, Need to reset shift
                            Log.e("Shift", "Switched to the Shift " + newShiftInfo);
                            isNewShiftData = true;
                        }
                    } else {
                        if (newShiftInfo != null) {
                            // Changed to Shift Status, Need to reset shift
                            Log.e("Shift", "Enter to the Shift " + newShiftInfo);
                            isNewShiftData = true;
                        }
                    }*/

                    if (isNewShiftData) {
                        resetShiftData(true);

                        timeLastShiftUpdateMilis = timeCurrentMilis;

                        currShiftTimeInfo = newShiftInfo;
                    } else {
                        shiftData.setStopTime(timeCurrentMilis);
                        shiftData.setElapsedTimeInMils(currentDeviceStatus, shiftData.getElapsedTimeInMils(currentDeviceStatus) + timeGap);
                        if (timeCurrentMilis - timeLastShiftUpdateMilis > 30000) { // Every 30 seconds
                            shiftData.calcRegime(appSettings);
                            dbHelper.updateShiftData(shiftData);

                            timeLastShiftUpdateMilis = timeCurrentMilis;

                            System.gc();
                        }
                    }

                    // Refresh InCycle Time
                    long cycleSeconds = elapsedMiliseconds[DEVICE_STATUS_INCYCLE];
                    tvSyncTime.setText(getElapsedTimeMinutesSecondsString(cycleSeconds));
                    tankTemperatureData.setTimeInCycle(cycleSeconds);

                    // Refresh UnCate Time
                    long unCateSeconds = elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED];
                    tvUnCatTime.setText(getElapsedTimeMinutesSecondsString(unCateSeconds));
                    tankTemperatureData.setTimeUnCate(unCateSeconds);

                    // Calc total Idle time and show it.
                    int idleSeconds = 0;
                    for (int i = 0; i < 8; i++) {
                        idleSeconds += elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i];
                    }
                    tvIdleTime.setText(getElapsedTimeMinutesSecondsString(idleSeconds));
                    tankTemperatureData.setTimeIdle(idleSeconds);

                    // Down Time Reason Status
                    // Originally only used following for the current downtime reason, but it has occur when the reset, not changed the value
                    if (currentDeviceStatus >= DEVICE_STATUS_IDLE1) {
                        // Refresh Idle Time(Downtime Reason Time)
                        // Refresh only currently-selected DownTime Reason Status
                        int selectedDowntimeIndex = currentDeviceStatus - DEVICE_STATUS_IDLE1;
                        if (TextUtils.isEmpty(titleDownTimeReason[selectedDowntimeIndex])) {
                            btnDownTimeStatus[selectedDowntimeIndex].setEnabled(false);

                            elapsedMiliseconds[currentDeviceStatus] = 0;
                            String idleStatusText = String.format("Disabled - Reason Not Defined\n%s",
                                    getElapsedTimeMinutesSecondsString(elapsedMiliseconds[currentDeviceStatus]));
                            btnDownTimeStatus[selectedDowntimeIndex].setText(idleStatusText);
                        } else {
                            btnDownTimeStatus[selectedDowntimeIndex].setEnabled(true);

                            String idleStatusText = String.format("%s\n%s",
                                    titleDownTimeReason[selectedDowntimeIndex],
                                    getElapsedTimeMinutesSecondsString(elapsedMiliseconds[currentDeviceStatus]));
                            btnDownTimeStatus[selectedDowntimeIndex].setText(idleStatusText);
                        }
                    }

                    // Originally here we called
                    // saveTimeStatus();

                    if (currentDeviceStatus != prevDeviceStatus) {

                        logE(String.format("Status from %s to %s",
                                getStatusTitleFromCode(prevDeviceStatus),
                                getStatusTitleFromCode(currentDeviceStatus)));

                        // Record the status changes
                        reportStatus();

                        // Change current device status value and Switch Animation
                        tvSyncTime.clearAnimation();
                        tvUnCatTime.clearAnimation();
                        tvIdleTime.clearAnimation();

                        if (currentDeviceStatus == DEVICE_STATUS_INCYCLE) {
                            tvSyncTime.startAnimation(alphaAnimation);

                            tvMachineCateStatus.setText(appSettings.isJobSetupStatus() ? "Setup - In Cycle" : "In Cycle");
                            tvMachineCateStatus.setTextColor(colorGreen);

                            tankTemperatureData.setMachineStatus("In Cycle");

                            // Reset Last Uncate start time
                            timeLastUncatStartTime = 0;
                        } else if (currentDeviceStatus == DEVICE_STATUS_UNCATEGORIZED) {
                            tvUnCatTime.startAnimation(alphaAnimation);

                            tvMachineCateStatus.setText(appSettings.isJobSetupStatus() ? "Setup - UnCategorized" : "UnCategorized");
                            tvMachineCateStatus.setTextColor(colorRed);

                            tankTemperatureData.setMachineStatus("UnCategorized");

                            // Save Last Uncate start time
                            timeLastUncatStartTime = timeCurrentMilis;
                        } else {
                            tvIdleTime.startAnimation(alphaAnimation);

                            String idleStatus = titleDownTimeReason[currentDeviceStatus - DEVICE_STATUS_IDLE1];

                            tvMachineCateStatus.setText(String.format("%sIdle[%s]", appSettings.isJobSetupStatus() ? "Setup - " : "", idleStatus));
                            tvMachineCateStatus.setTextColor(colorOrange);

                            tankTemperatureData.setMachineStatus(String.format("Idle-%s", idleStatus));

                            // Reset Last Uncate start time
                            timeLastUncatStartTime = 0;
                        }

                        prevDeviceStatus = currentDeviceStatus;

                        // Status changed, so we reduce the report time for the current status
                        INTERVAL_REPORT = INTERVAL_1_SECOND;
                    } else if (timeCurrentMilis - timeLastReportMilis >= INTERVAL_REPORT) {
                        reportStatus();

                        // We return back to Report time to normal report time
                        if (INTERVAL_REPORT == INTERVAL_1_SECOND) {
                            INTERVAL_REPORT = INTERVAL_3_MINUTES;
                        }
                    }

                    // Check Elapsed Idle TIme
                    if (currentDeviceStatus != DEVICE_STATUS_INCYCLE) {
                        elapsedIdleTime += timeGap;
                    }

                    tvElapsedIdleTIme.setText("Elapsed Idle Time : " + getElapsedTimeMinutesSecondsString(elapsedIdleTime));

                    // Check Stop Time and Parts Counting Time
                    if (currentDeviceStatus == DEVICE_STATUS_UNCATEGORIZED) {
                        // Check Stop Time and CSLock Status
                        elapsedStopTime += timeGap;

                        if (elapsedStopTime > mSettingStopTimeLimit) {

                            if (statusCsLock == 0) {
                                logE("=> csLock Status : " + elapsedIdleTime + "_" + mSettingStopTimeLimit);
                                statusCsLock = 1;
                            }

                            // Send Alert
                            if (!sentStopTimeAlert) {
                                //sentStopTimeAlert = sendAlert(ALERT_TYPE_STOPTIMELIMIT);

                                sendAlert(ALERT_TYPE_STOPTIMELIMIT);
                                // We change the logic and regards as mail was already sent!!!
                                sentStopTimeAlert = true;
                            }
                        }

                        // Check Automatic Counting Timer, used to increase good part according the stop time
                        if (mSettingIsAutoPartsCounting && !bOneTimeCounted) {
                            elapsedStopTimeForPartsCounting += timeGap;
                            if (elapsedStopTimeForPartsCounting >= mSettingMinElapsedStopTimeForGoodParts) {
                                // Increase good value
                                int good_val = Integer.parseInt(tvGood.getText().toString());
                                good_val += mSettingPartsPerCycle;
                                // Show & Save Good
                                tvGood.setText(String.valueOf(good_val));
                                appSettings.setShiftGoodParts(good_val);
                                shiftData.setGoodParts(good_val);

                                // Daily Live Data
                                appSettings.setGoodParts(appSettings.getGoodParts() + mSettingPartsPerCycle);

                                elapsedStopTimeForPartsCounting = 0;
                                bOneTimeCounted = true;

                                broadcastPartCountStatusToFragments();
                            }
                        }
                    }

                    // Show CSLock Status
                    if (statusCsLock == 1 || (mSettingIsGuestLock && !mSettingIsLoggedIn)) {
                        if (panelIdleSetUp.getVisibility() != View.VISIBLE) {
                            showIdleSetupPanel();
                            backCsLockStatus.setVisibility(View.VISIBLE);
                            backCsLockStatus.startAnimation(blinkAnimation);
                            tvTitleStatusInterlocked.setVisibility(View.VISIBLE);

                            playChinChin();
                        }
                    }

                    // Update Regimes
                    if (timeCurrentMilis - timeLastRegimeUpdateTime >= INTERVAL_REGIME_UPDATE) {
                        updateRegime();

                        timeLastRegimeUpdateTime = timeCurrentMilis;
                    }

                    // Maintenance Logic
                    // Count the Incycle Time in case of InCycle Status
                    if (currentDeviceStatus == DEVICE_STATUS_INCYCLE) {
                        maintenanceTaskManager.addInCycleTime(timeGap);

                        if (panelMaintenance.getVisibility() == View.VISIBLE) {
                            // App already is in maintenacne status
                        } else {
                            Maintenance newMaintenanceInfo = maintenanceTaskManager.getNewMaintenanceItem();
                            if (newMaintenanceInfo != null) {
                                panelMaintenance.setVisibility(View.VISIBLE);
                                panelMaintenance.setTag(newMaintenanceInfo);

                                // Show Maintenance Details
                                tvTaskMachine.setText(newMaintenanceInfo.getMachineId());
                                tvTaskName.setText(newMaintenanceInfo.getTaskName());
                                tvTaskCategory.setText(newMaintenanceInfo.getTaskCategory());
                                tvTaskInstruction.setText(newMaintenanceInfo.getTaskInstruction());
                                tvTaskFrequency.setText(String.valueOf(newMaintenanceInfo.getFrequency()));
                                tvTaskCycleStartInterlock.setText(newMaintenanceInfo.getInterlock() > 0 ? "YES" : "NO");

                                // Show Maintenance Task Logo
                                imageLoader.displayImage(newMaintenanceInfo.getPicture(), ivTaskLogo, imageOptionsWithCustomerAvatar);
                            }
                        }
                    }

                    // Cycle Update JobInfo
                    if (timeCurrentMilis - timeJobDataUpdate >= INTERVAL_UPDATE_JOBINFO) {
                        String jobID = appSettings.getJobId();
                        if (!TextUtils.isEmpty(jobID)) {
                            getJobInfo(jobID, false);
                        }
                        timeJobDataUpdate = timeCurrentMilis;
                    }

                    // Finally Save current Timer Status in the storage
                    saveTimeStatus();
                }

                //Log.e("TIME", "" + (getCurrentTime() - startTime));
            }
        }
    };

    private String getStatusTitleFromCode(int code) {
        if (code == DEVICE_STATUS_UNCATEGORIZED) {
            return "UNCAT";
        } else if (code == DEVICE_STATUS_INCYCLE) {
            return "INCYCLE";
        } else {
            return titleDownTimeReason[code - DEVICE_STATUS_IDLE1];
        }
    }

    // Hide Idle Setup Panel
    private void showIdleSetupPanel() {
        panelIdleSetUp.setVisibility(View.VISIBLE);

        // Check GuestLock and Login status
        if (mSettingIsGuestLock && !mSettingIsLoggedIn) {
            // If user not loggined and guest lock status
            panelLoginMessage.setVisibility(View.VISIBLE);
        } else {
            panelLoginMessage.setVisibility(View.GONE);
        }

        boolean isDowntimeReasonSet = false;
        for (int i = 0; i < 8; i++) {
            if (!TextUtils.isEmpty(titleDownTimeReason[i])) {
                isDowntimeReasonSet = true;
                break;
            }
        }

        if (isDowntimeReasonSet && statusCsLock == 1) {
            btnCloseIdleStatus.setVisibility(View.GONE);
        } else {
            btnCloseIdleStatus.setVisibility(View.VISIBLE);
        }
    }

    // Show Idel Setup Panel
    private void hideIdleSetupPanel() {
        if (panelIdleSetUp.getVisibility() == View.VISIBLE) {
            panelIdleSetUp.setVisibility(View.GONE);
        }
    }

    private void resetStopTime() {
        logE("<= csLock Status");

        // Set StopTime = 0
        elapsedStopTime = 0;
        statusCsLock = 0;

        elapsedStopTimeForPartsCounting = 0;
        bOneTimeCounted = false;

        backCsLockStatus.clearAnimation();
        backCsLockStatus.setVisibility(View.GONE);

        tvTitleStatusInterlocked.setVisibility(View.GONE);

        btnCloseIdleStatus.setVisibility(View.VISIBLE);

        // Stop Audio
        stopChinChin();
    }

    private void resetElapsedIdleTime() {
        elapsedIdleTime = 0;
    }

    private void resetDownTimeReasonButtonStatus() {
        for (int i = 0; i < 8; i++) {
            btnDownTimeStatus[i].setSelected(false);
        }
    }

    private void reportStatus() {

        String accountID = appSettings.getCustomerID();
        String machineID = appSettings.getMachineName();
        if (TextUtils.isEmpty(accountID) || TextUtils.isEmpty(machineID)) {
            return;
        }

        // upload prevDevice Status
        String createdAt = DateUtil.toStringFormat_20(new Date(timeLastReportMilis));
        String operator = appSettings.getUserName();

        String status = "";
        String colorPie = "";
        if (prevDeviceStatus == DEVICE_STATUS_UNCATEGORIZED) {
            colorPie = colorCharts[DEVICE_STATUS_UNCATEGORIZED];   //  "#ff0000"

            if (appSettings.isJobSetupStatus()) {
                status = "Setup-Uncategorized";
            } else {
                status = "Idle-Uncategorized";
            }
        } else if (prevDeviceStatus == DEVICE_STATUS_INCYCLE) {
            colorPie = colorCharts[DEVICE_STATUS_INCYCLE];   // "#46c392"

            if (appSettings.isJobSetupStatus()) {
                status = "Setup-In Cycle";
            } else {
                status = "In Cycle";
            }
        } else {
            // In case of Idle, we apply same gantt color
            colorPie = colorCharts[prevDeviceStatus];              // "#ffa300";

            if (appSettings.isJobSetupStatus()) {
                status = String.format("Setup-Idle[%s]", titleDownTimeReason[prevDeviceStatus - DEVICE_STATUS_IDLE1]);
            } else {
                status = titleDownTimeReason[prevDeviceStatus - DEVICE_STATUS_IDLE1];
            }
        }

        // ------------------------------- Change Status Logic -----------------
        boolean needToChangeStatusFromUncat = false;
        if (prevDeviceStatus == DEVICE_STATUS_UNCATEGORIZED && currentDeviceStatus >= DEVICE_STATUS_IDLE1) {
            needToChangeStatusFromUncat = true;
        }

        if (needToChangeStatusFromUncat) {
            colorPie = colorCharts[currentDeviceStatus];
            status = titleDownTimeReason[currentDeviceStatus - DEVICE_STATUS_IDLE1];
        }
        // ----------------------------------------------------------------------

        int battLevel = AppUtils.getBatteryPercentage(mContext);
        String jobId = appSettings.getJobId();

        long start = timeLastReportMilis;
        long end = timeCurrentMilis;

        String timeStamp = DateUtil.toStringFormat_12(new Date(timeCurrentMilis));
        int timeStampMs = (int) (timeCurrentMilis % 1000);

        // GanttDataModel newGanttData = new GanttDataModel(createdAt, machineID, operator, status, color, start, end, timeStamp, timeStampMs);
        dbHelper.insertGanttData(createdAt, machineID, operator, status, colorPie, start, end, timeStamp, timeStampMs, jobId, battLevel, "");

        // ------------------------ Add New record for the db updates -----------------
        if (needToChangeStatusFromUncat && timeLastUncatStartTime > 0) {
            dbHelper.insertGanttData("UPDATE_STATUS", machineID, operator, status, colorPie, timeLastUncatStartTime, end, timeStamp, timeStampMs, jobId, battLevel, "");
        }
        // ----------------------------------------------------------------------------

        timeLastReportMilis = timeCurrentMilis;

        // Update Gantt Plot in local
        GanttPlotModel newGantt = new GanttPlotModel(start / 1000, (end - start) / 1000, colorPie);
        synchronized (ganttDataList) {

            if (ganttDataList.size() > 0) {
                GanttPlotModel prevGantt = ganttDataList.get(ganttDataList.size() - 1);
                if (newGantt.isNextSameSegment(prevGantt)) {
                    // Same status of neighborhood gantt
                    prevGantt.setLength(newGantt.getEnd() - prevGantt.getStart());
                    //Log.e("segment", "same seg!!!");
                } else {
                    // Different status of Gantt
                    ganttDataList.add(newGantt);
                    //Log.e("segment", "another seg!!!");
                }
            } else {
                Log.e("segment", "new seg!!!");
                ganttDataList.add(newGantt);
            }

            // ------------------------ Change Gantt Data -------------------------
            if (needToChangeStatusFromUncat) {
                long unCateStart = timeLastUncatStartTime / 1000;
                long unCateEnd = (end + 1) / 1000;

                for (int i = 0; i < ganttDataList.size(); i++) {
                    GanttPlotModel itemGantt = ganttDataList.get(i);
                    if (itemGantt.getStart() >= unCateStart && itemGantt.getEnd() <= unCateEnd) {
                        // Change to new color
                        itemGantt.setColor(colorPie);
                    }
                }
            }
            // --------------------------------------------------------------------

            updateGanttChart();
        }
    }

    private void updateRegime() {
        RegimeUtils.calcRegimes(appSettings, regimeData, false);

        // Refresh Regime Values
        if (gaugeFragment != null) {
            gaugeFragment.updateRegimeData(regimeData);
        }

        if (utilizationFragment != null) {
            utilizationFragment.updateRegimeData(regimeData);
        }
    }

    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(bleDeviceAddress);
            String deviceName = device.getName() != null ? device.getName() : device.getAddress();

            status("connecting...");

            connected = Connected.Pending;

            socket = new SerialSocket();
            service.connect(this, "Connected to " + deviceName);
            socket.connect(mContext, service, device);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        if (connected != Connected.False) {
            logE("Disconnect PLC...");

            connected = Connected.False;
            if (service != null) {
                service.disconnect();
            }

            if (socket != null) {
                socket.disconnect();
                socket = null;
            }
        }
    }

    private void send(String str) {
        if (connected != Connected.True) {
            return;
        }
        try {
            SpannableStringBuilder spn = new SpannableStringBuilder(str + '\n');
            spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSendText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            byte[] data = (str + newline).getBytes();
            socket.write(data);
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }

    private void receive(byte[] data) {

        final String receivedString = new String(data).trim();

        // In case of Signal is from PLC
        if (appSettings.isInCycleFromPLC()) {
            synchronized (timeLock) {
                int firstComma = receivedString.indexOf(",");
                if (firstComma > 0) {
                    String subString = receivedString.substring(firstComma - 1, firstComma);
                    if ("1".equals(subString)) {
                        processNewDeviceStatus(true);
                    } else if ("0".equals(subString)) {
                        processNewDeviceStatus(false);
                    }
                }
            }
            //receiveText.append(new String(data));
        }

        // Parse Temperature
        String[] cmdValues = receivedString.split(",");
        if (cmdValues.length >= 5) {
            int tankCnt = Math.min(8, cmdValues.length - 4);

            for (int i = 0; i < tankCnt; i++) {
                float tankTemp = 0;
                try {
                    tankTemp = Float.parseFloat(cmdValues[4 + i]);
                } catch (Exception e) {e.printStackTrace();}
                tankTemperatureData.setTempInfo(i, tankTemp);
            }
        }
    }

    private void processNewDeviceStatus(boolean isNewStatusInCycle) {
        if (isNewStatusInCycle) {
            if (currentDeviceStatus != DEVICE_STATUS_INCYCLE) {
                logE("To INCYCLE");

                resetStopTime();
                resetElapsedIdleTime();

                currentDeviceStatus = DEVICE_STATUS_INCYCLE;

                // Hide Stop Time Reason Screen
                hideIdleSetupPanel();
            }
        } else {
            if (currentDeviceStatus == DEVICE_STATUS_INCYCLE) {

                logE("From INCYCLE to UnCat!");
                opDownTimeStatus = DEVICE_STATUS_UNCATEGORIZED;
                sentStopTimeAlert = false; // ! Important !

                resetDownTimeReasonButtonStatus();

                // Out of Incycle
                // We don't use it now
                // sendAlert(ALERT_TYPE_OUTCYCLE);

                if (appSettings.isUsingJobAutoLogout() && appSettings.getUserName().equalsIgnoreCase("Unattended") && !TextUtils.isEmpty(appSettings.getJobId())){
                    jobIDLogout();
                }
            }

            currentDeviceStatus = opDownTimeStatus;
        }
    }

    private void status(String str) {
        SpannableStringBuilder spn = new SpannableStringBuilder(str + '\n');
        spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorStatusText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //receiveText.append(spn);
        logE(str);
    }

    /*
     * SerialListener
     */
    @Override
    public void onSerialConnect() {
        status("connected");
        connected = Connected.True;

        currentDeviceStatus = opDownTimeStatus;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToastMessage("Connected!");
            }
        });

        // Set PLC Connected Status
        setPLCConnStatus(true);
    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("Connection failed: " + e.getMessage());
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
        receive(data);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("Connection lost: " + e.getMessage());

        disconnect();

        // PLC Disconnected status
        setPLCConnStatus(false);


        // Reset Downtime reason status
        opDownTimeStatus = DEVICE_STATUS_UNCATEGORIZED;
        resetDownTimeReasonButtonStatus();
        if (currentDeviceStatus == DEVICE_STATUS_INCYCLE){
            if (appSettings.isUsingJobAutoLogout() && appSettings.getUserName().equalsIgnoreCase("Unattended") && !TextUtils.isEmpty(appSettings.getJobId())){
                jobIDLogout();
            }
        }
        currentDeviceStatus = DEVICE_STATUS_UNCATEGORIZED;

        // Changed to Uncategorized time, need to send mail again after stop time
        sentStopTimeAlert = false;

    }
    //-------------------------------------------------------------------------------------------------

    //--------------------------------------App Disable Feature ---------------------------------------
    BroadcastReceiver appUsageBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra("STATUS", AlarmSettings.STATUS_VALIDATE_USAGE);

            if (action == AlarmSettings.STATUS_VALIDATE_USAGE) {
                checkAppVaildStatus();
            } else if (action == AlarmSettings.STATUS_NEW_VESION) {
                String versionInfo = intent.getStringExtra("VERSION");
                showNewVersionInfo(versionInfo);
            } else if (action == AlarmSettings.STATUS_NEW_SETTINGS) {
                showSettings();
            } else if (action == AlarmSettings.STATUS_SERVER_CONN_STATUS) {
                boolean connected = intent.getBooleanExtra("SERVER_CONNECTED", false);
                setServerConnStatus(connected);
            } else if (action == AlarmSettings.STATUS_DEVICE_STATUS) {
                boolean isInCycle = intent.getBooleanExtra("INCYCLE", false);
                // Process Signal In case of InCycle Signal Source being Process Monitor
                if (appSettings.isInCycleFromPLC() == false) {
                    processNewDeviceStatus(isInCycle);
                }
            } else if (action == AlarmSettings.STATUS_REPORT_TANK_TIME) {
                int good_val = Integer.parseInt(tvGood.getText().toString());
                good_val++;
                tvGood.setText(String.valueOf(good_val));
                appSettings.setShiftGoodParts(good_val);
                shiftData.setGoodParts(good_val);

                // Daily Live Data
                appSettings.setGoodParts(appSettings.getGoodParts() + 1);

                broadcastPartCountStatusToFragments();
            } else if (action == AlarmSettings.STATUS_PROXY_LOGIN_LOGOUT) {
                updateUserInfo();
            } else if (action == AlarmSettings.STATUS_PROXY_PARTS_COUNT_CHANGED) {
                updatePartsCount();
            } else if (action == AlarmSettings.STATUS_NEW_DAILY_GOAL_TARGET) {
                // Update Daily Goal Target
                if (appSettings.getChartOption() == 2) {
                    ganttChart.invalidate();
                }
            } else if (action == AlarmSettings.STATUS_NEW_PARTID) {
                // User Inputted new part id
                String partId = intent.getStringExtra("PARTID");
                if (!TextUtils.isEmpty(partId)) {
                    shiftData.addPartId(partId);
                    shiftData.calcRegime(appSettings);
                    dbHelper.updateShiftData(shiftData);
                }
            }
        }
    };

    private boolean checkAppVaildStatus() {
        boolean validStatus = appSettings.isValidStatus();

        if (appSettings.isValidStatus()) {
            panelDisabled.setVisibility(View.GONE);
        } else {
            panelDisabled.setVisibility(View.VISIBLE);
        }

        return validStatus;
    }

    private void showNewVersionInfo(String versionInfo) {

        if (!versionInfo.equals(appSettings.getVersionNotified())) {
            appSettings.setVersionNotified(versionInfo);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
            alertDialogBuilder.setTitle(R.string.title_new_version);
            alertDialogBuilder.setMessage(getString(R.string.description_new_version, versionInfo))
                    .setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                    gotoSettingScreen(false);
                }
            }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    //-------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide Status Bar
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            // Hide status bar
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // Keep Screen On, already done in xml
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        if (getIntent().getBooleanExtra("crash", false)) {
            showToastMessage("App started after Mem reset!");
        }

        // Set Power-ON while running the app
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MMSApp::MyWakelockTag");
        wakeLock.acquire();
        //---------------------------------------------------------------------------

        // Report History DB
        dbHelper = new DatabaseHelper(mContext);
        //---------------------------------------------------------------------------

        // Init File Logger System
        initLogSystem();
        //---------------------------------------------------------------------------

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up DrawLayout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Define navigation Callback Handler
        navigationView.setNavigationItemSelectedListener(this);
        TextView tvVerInfo = findViewById(R.id.tvVersion);
        tvVerInfo.setText(String.format("MMS V%s%s", getVersionName(), (MyApplication.TEST_VERSION ? "(Beta)" : "")));

        // Init UI Elements
        setUpUI();

        // Tank Time Layout and Data
        tankTemperatureData = mMyApp.getTankTemperatureData();
        containerTimeReport = findViewById(R.id.containerTimeReport);
        containerTimeReport.setVisibility(View.GONE);

        // Get Elapsed Time
        startBottomTimer();

        // Load Alpha Animation
        alphaAnimation = new AlphaAnimation(1f, 0.3f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        tvUnCatTime.startAnimation(alphaAnimation);

        // Set App Running Status
        mMyApp.setAppRunningStatus(true);

        // Retrieve Time Status
        retrieveTimerStatus();

        startAlarm();
        LocalBroadcastManager.getInstance(mContext).
                registerReceiver(appUsageBroadcastReceiver, new IntentFilter(AlarmSettings.ACTION_VALID_STATUS_UPDATED));

        // Check App valid Status
        checkAppVaildStatus();

        // This comment block is used in case of simulation
        // showSettings();
        if (/*makeSimData == false && */true) { // Init Bluetooh Connect
            // Works with Modbus TCP Module
            // mStatusCheckerHandler.sendEmptyMessageDelayed(MSG_SIGNALCHECKER_PER_SEC, 1000);

            // Works with Bluetooth Device
            bleDeviceAddress = appSettings.getBTDeviceAddr();
            if (TextUtils.isEmpty(bleDeviceAddress) && appSettings.isInCycleFromPLC()) {
                logE("PLC was not selected. Open Device Scanning Screen.");
                startActivity(new Intent(mContext, DeviceScanActivity.class));
            }
            //else {

            // Enable Time counting even bluetooth device is not connected
            logE("Try to connect PLC...");

            connectBLEDevice();
            timePrevStatusMilis = getCurrentTime();
            timeLastReportMilis = timePrevStatusMilis;

            timeLastUncatStartTime = timePrevStatusMilis;

            timeLastShiftUpdateMilis = timePrevStatusMilis;

            mBLESignalCheckerHandler.sendEmptyMessageDelayed(MSG_SIGNALCHECKER_PER_SEC, 1000);
            mBLESignalCheckerHandler.sendEmptyMessageDelayed(MSG_TIMECOUNTER_PER_SEC, 1000);

            //}

            // Triggers Device Connection Check Handler
            mBLEDeviceConnectionCheckHandler.sendEmptyMessageDelayed(MSG_DEVICE_CONNECTION_CHECK, DEVICE_RECONNECT_INTERVAL);
        }

        // Start Report Service
        if (!isMyServiceRunning(ReportService.class)) {
            Log.e("report", "Report Service is not running");

            //startService(new Intent(mContext, ReportService.class));
            Intent reportIntent = new Intent(mContext, ReportService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(reportIntent);
            } else {
                startService(reportIntent);
            }
        } else {
            Log.e("report", "Report Service is now running");
        }

        // Make the device to be discoverable by other devices for 60 seconds
        // Don't use this code
        /*Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
        startActivity(discoverableIntent);*/
        //------------------------------------------------------------------------------------------

        getGanttDataHistory();

        // We check App version in the setting, null means auto update under user agreement using confirm dialog
        // checkAppVersion(null);

        Log.e("its", "onCreate");

        isUsingLocal = appSettings.isUsingLocalServer();
        localServerIP = appSettings.getLocalServerIP();

        // Intent intent = new Intent(this, StageInfoActivity.class);
        // intent.putExtra("STAGE_ID", FaxonStage1.STAGE_ID);
        // startActivity(intent);

        // Intent intent = new Intent(this, PanelDataLogActivity.class);
        // startActivity(intent);

        // For simulator
        //simulateTestData();
    }

    private boolean makeSimData = false;

    private void simulateTestData() {
        if (!makeSimData)
            return;

        String simStartTime = "2020-06-23 00:00:00";
        String simEndTime = "2020-06-24 00:00:00";

        String[] simMachineNames = new String[]{"NHX6300 #1", "NHX6300 #2", "NHX6300 #3", "NHX6300 #4"};

        Random simTimeRandom = new Random(System.currentTimeMillis());
        Random simStatusRandom = new Random();

        long simStartTimeMils = DateUtil.parseDataFromFormat12(simStartTime).getTime();
        long simEndTimeMils = DateUtil.parseDataFromFormat12(simEndTime).getTime();

        int minSimStepMils = 1000 * 60 * 5;
        int minSimStepDeltaMils = 1000 * 60 * 10;

        for (String machineName : simMachineNames) {
            long previousSimTimeMils = simStartTimeMils;
            long curSimTimeMils = 0;

            while (previousSimTimeMils < simEndTimeMils) {
                curSimTimeMils = previousSimTimeMils + (minSimStepMils + simTimeRandom.nextInt(minSimStepDeltaMils));

                int simCurStatus = simStatusRandom.nextInt(DEVICE_STATUS_IDLE8 + 1);

                // upload prevDevice Status
                String createdAt = DateUtil.toStringFormat_20(new Date(curSimTimeMils));
                String operator = "Unattended";

                String status = "";
                String color = "";

                if (simCurStatus >= DEVICE_STATUS_IDLE3) {
                    simCurStatus = DEVICE_STATUS_INCYCLE;
                }

                if (simCurStatus == DEVICE_STATUS_UNCATEGORIZED) {
                    color = "#ff0000";
                    status = "Idle-Uncategorized";
                } else if (simCurStatus == DEVICE_STATUS_INCYCLE) {
                    color = "#46c392";
                    status = "In Cycle";
                } else {
                    color = "#ffa300";
                    status = titleDownTimeReason[simCurStatus - DEVICE_STATUS_IDLE1];
                }

                String timeStamp = DateUtil.toStringFormat_12(new Date(curSimTimeMils));
                int timeStampMs = (int) (curSimTimeMils % 1000);

                dbHelper.insertGanttData(createdAt, machineName, operator, status, color, previousSimTimeMils, curSimTimeMils, timeStamp, timeStampMs, "100", 98, "");

                previousSimTimeMils = curSimTimeMils;
            }
        }
    }

    private String getCurrentTimeString() {
        return DateUtil.getDayTimeStringInUniformat();
    }

    private void retrieveTimerStatus() {

        // Manual Reset due to the over time and for checking new version
        if ("Makino".equals(appSettings.getMachineName()) && "09/17/2020".equals(appSettings.getLastTime())) {
            resetTimers();
        } else {
            String currentTimeString = getCurrentTimeString();

            if (currentTimeString.equals(appSettings.getLastTime())) {
                elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] = appSettings.getTimeUnCat();
                elapsedMiliseconds[DEVICE_STATUS_INCYCLE] = appSettings.getTimeIncycle();

                elapsedMiliseconds[DEVICE_STATUS_IDLE1] = appSettings.getTimeIdle1();
                elapsedMiliseconds[DEVICE_STATUS_IDLE2] = appSettings.getTimeIdle2();
                elapsedMiliseconds[DEVICE_STATUS_IDLE3] = appSettings.getTimeIdle3();
                elapsedMiliseconds[DEVICE_STATUS_IDLE4] = appSettings.getTimeIdle4();
                elapsedMiliseconds[DEVICE_STATUS_IDLE5] = appSettings.getTimeIdle5();
                elapsedMiliseconds[DEVICE_STATUS_IDLE6] = appSettings.getTimeIdle6();
                elapsedMiliseconds[DEVICE_STATUS_IDLE7] = appSettings.getTimeIdle7();
                elapsedMiliseconds[DEVICE_STATUS_IDLE8] = appSettings.getTimeIdle8();

                // Parts Good & Bad
                tvGood.setText(String.valueOf(0/*appSettings.getPartsGood()*/));    // We use shift counter, not daily counter
                tvBad.setText(String.valueOf(0/*appSettings.getPartsBad()*/));      // We use shift counter, not daily counter

                // Exception in case of negative value, not the normal logic
                if (elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] < 0) {
                    resetTimers();
                }
            } else {
                // Report previous date
                resetTimers();
            }
        }
    }

    private void saveTimeStatus() {

        String currentTimeString = getCurrentTimeString();
        if (!currentTimeString.equals(appSettings.getLastTime())) {
            // Report previous date
            //Log.e("MMS", "***" + lastRecordTime+ " " + currentTimeString);
            reportStatus();

            resetTimers();

            resetShiftData(true);
        } else {
            // Check Exception
            if (elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] < 0) {
                logE("Unexpected Negative Value, Reset status");
                resetTimers();
            } else {
                // Check Exception of the total sume is exceeding the 24 hours
                long totalTimeMilis = 0;
                for (int i = DEVICE_STATUS_UNCATEGORIZED; i <= DEVICE_STATUS_IDLE8; i++) {
                    totalTimeMilis += elapsedMiliseconds[i];
                }

                // Process exception
                if (totalTimeMilis >= 86400000) {
                    resetTimers();
                } else {
                    // Save current time status
                    appSettings.setTimeUnCat(elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED]);
                    appSettings.setTimeInCycle(elapsedMiliseconds[DEVICE_STATUS_INCYCLE]);
                    appSettings.setTimeIdle1(elapsedMiliseconds[DEVICE_STATUS_IDLE1]);
                    appSettings.setTimeIdle2(elapsedMiliseconds[DEVICE_STATUS_IDLE2]);
                    appSettings.setTimeIdle3(elapsedMiliseconds[DEVICE_STATUS_IDLE3]);
                    appSettings.setTimeIdle4(elapsedMiliseconds[DEVICE_STATUS_IDLE4]);
                    appSettings.setTimeIdle5(elapsedMiliseconds[DEVICE_STATUS_IDLE5]);
                    appSettings.setTimeIdle6(elapsedMiliseconds[DEVICE_STATUS_IDLE6]);
                    appSettings.setTimeIdle7(elapsedMiliseconds[DEVICE_STATUS_IDLE7]);
                    appSettings.setTimeIdle8(elapsedMiliseconds[DEVICE_STATUS_IDLE8]);

                    //Log.e("MMS", lastRecordTime+ " " + currentTimeString);
                    appSettings.setLastTime(currentTimeString);
                }
            }
        }
    }

    private void resetTimers() {
        Log.e("MMS", "ResetTimers");

        // Reset this value first, this occures the negative value in un-cat, when user select downtime reason.
        elapsedStopTime = 0;
        elapsedStopTimeForPartsCounting = 0;
        bOneTimeCounted = false;

        // Reset Data
        elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] = 0;
        elapsedMiliseconds[DEVICE_STATUS_INCYCLE] = 0;

        elapsedMiliseconds[DEVICE_STATUS_IDLE1] = 0;
        elapsedMiliseconds[DEVICE_STATUS_IDLE2] = 0;
        elapsedMiliseconds[DEVICE_STATUS_IDLE3] = 0;
        elapsedMiliseconds[DEVICE_STATUS_IDLE4] = 0;
        elapsedMiliseconds[DEVICE_STATUS_IDLE5] = 0;
        elapsedMiliseconds[DEVICE_STATUS_IDLE6] = 0;
        elapsedMiliseconds[DEVICE_STATUS_IDLE7] = 0;
        elapsedMiliseconds[DEVICE_STATUS_IDLE8] = 0;

        // Show new values
        refreshDowntimeReasons();

        // Save QTYCompleted, this value should be saved to *_jobdata table before reset the time daily
        String qtyCompleted = String.format("%d,%d", appSettings.getGoodParts(), appSettings.getBadParts());
        appSettings.setQtyCompleted(qtyCompleted);

        // Set Good and Bad Parts
        appSettings.setGoodParts(0);
        appSettings.setBadParts(0);

        //tvGood.setText("0");
        //tvBad.setText("0");

        // Update Timer to avoid duplication
        String currentTimeString = getCurrentTimeString();
        appSettings.setLastTime(currentTimeString);

        // Reset Gantt Chart
        ganttDataList.clear();
        updateGanttChart();

        // Garbage Collect
        System.gc();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void showSettings() {
        tvMachineName.setText(appSettings.getMachineName());

        imageLoader.displayImage(appSettings.getCustomerAvatar(), ivCustomerAvatar, imageOptionsWithCustomerAvatar);

        // Check User Name Changes
        String currUserName = tvUserName.getText().toString().trim();
        if (!currUserName.equals(appSettings.getUserName())) {
            tvUserName.setText(appSettings.getUserName());
        }
        imageLoader.displayImage(appSettings.getUserAvatar(), ivAvatar, imageOptionsWithUserAvatar);

        showJobInformation();

        // Update Downtime reasons
        titleDownTimeReason[0] = appSettings.getDownTimeReason1();
        titleDownTimeReason[1] = appSettings.getDownTimeReason2();
        titleDownTimeReason[2] = appSettings.getDownTimeReason3();
        titleDownTimeReason[3] = appSettings.getDownTimeReason4();
        titleDownTimeReason[4] = appSettings.getDownTimeReason5();
        titleDownTimeReason[5] = appSettings.getDownTimeReason6();
        titleDownTimeReason[6] = appSettings.getDownTimeReason7();
        titleDownTimeReason[7] = appSettings.getDownTimeReason8();

        refreshDowntimeReasons();

        // Login Status
        mSettingIsLoggedIn = appSettings.isLoggedIn();

        // Load Other Settings
        mSettingIsUseCSLock = appSettings.isUseCSLock();
        mSettingIsReverseCSLock = appSettings.isReverseCSLock();
        mSettingIsGuestLock = appSettings.isGuestLock();
        mSettingIsEnabledCSLockSound = appSettings.isCSLockSoundEnabled();

        mSettingStopTimeLimit = appSettings.getStopTimeLimit();

        mSettingIsAutoPartsCounting = appSettings.isAutomaticPartsCounter();
        mSettingMinElapsedStopTimeForGoodParts = appSettings.getMinElapsedStopTime();
        mSettingPartsPerCycle = appSettings.getPartsPerCycle();

        // In case of changing the time mode, need to refresh it onResume
        updateGanttChart();
    }

    private void refreshDowntimeReasons() {

        for (int i = 0; i < 8; i++) {
            //btnDownTimeStatus[i].setText(titleDownTimeReason[i]);

            if (TextUtils.isEmpty(titleDownTimeReason[i])) {
                btnDownTimeStatus[i].setEnabled(false);

                elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i] = 0;
                String idleStatusText = String.format("Disabled - Reason Not Defined\n%s",
                        getElapsedTimeMinutesSecondsString(elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i]));
                btnDownTimeStatus[i].setText(idleStatusText);
            } else {
                btnDownTimeStatus[i].setEnabled(true);

                String idleStatusText = String.format("%s\n%s",
                        titleDownTimeReason[i],
                        getElapsedTimeMinutesSecondsString(elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i]));
                btnDownTimeStatus[i].setText(idleStatusText);
            }
        }
    }

    private void showJobInformation() {

        tvJobId.setText(appSettings.getJobId());
        String jobDetails = appSettings.getJobDetails();

        Log.e("JobDetails", jobDetails);

        String customer = "";
        String partNumber = "";
        String description = "";
        String qtyRequired = "";
        String qtyCompleted = "";

        String aux1data = "";
        String aux2data = "";
        String aux3data = "";
        try {
            JSONObject jsonObject = new JSONObject(jobDetails);
            customer = jsonObject.optString("customer").replace("null", "");
            partNumber = jsonObject.optString("partNumber").replace("null", "");
            description = jsonObject.optString("description").replace("null", "");
            qtyRequired = jsonObject.optString("qtyRequired").replace("null", "");
            qtyCompleted = jsonObject.optString("qtyCompleted").replace("null", "");

            String orderType = jsonObject.optString("order_type").replace("null", "");
            if (orderType.equalsIgnoreCase("PRO")){
                aux1data = jsonObject.optString("aux1data").replace("null", "");
                aux2data = jsonObject.optString("aux2data").replace("null", "");
            }else{
                aux1data = jsonObject.optString("bom_dim1").replace("null", "");
                aux2data = jsonObject.optString("bom_dim2").replace("null", "");
            }
            aux3data = jsonObject.optString("aux3data").replace("null", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringBuilder strJobDetailsBuilder = new StringBuilder();
        strJobDetailsBuilder.append("Customer : <b>" + customer + "</b><br>");
        strJobDetailsBuilder.append("Part Number : <b>" + partNumber + "</b><br>");
        strJobDetailsBuilder.append("Description : <b>" + description + "</b><br>");
        strJobDetailsBuilder.append("Qty Required : <b>" + qtyRequired + "</b><br>");
        strJobDetailsBuilder.append("Qty Good Completed : <b>" + qtyCompleted + "</b><br>");
        strJobDetailsBuilder.append(appSettings.getAux1DataTitle() + " : <b>" + aux1data + "</b><br>");
        strJobDetailsBuilder.append(appSettings.getAux2DataTitle() + " : <b>" + aux2data + "</b><br>");
        strJobDetailsBuilder.append(appSettings.getAux3DataTitle() + " : <b>" + aux3data + "</b><br>");
        tvJobDetails.setText(Html.fromHtml(strJobDetailsBuilder.toString()));
        if (TextUtils.isEmpty(jobDetails)) {
            // btnJobGuides.setVisibility(View.GONE);
        } else {
            // btnJobGuides.setVisibility(View.VISIBLE);
        }
        updateJobReworkStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Some informations should update after user change settings.
        showSettings();

        // Send Online Status
        StatusReporter.getInstance(mContext).reportStatus("Online");

        // Check Server Setting Changes
        if (isUsingLocal != appSettings.isUsingLocalServer() ||
                (isUsingLocal && !localServerIP.equals(appSettings.getLocalServerIP()))) {
            // If Server Mode was changed or local server IP was changed, then should restart
            showAlert(R.string.msg_server_config_restart_app, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            return;
        }

        // If disabled status, then cancel to connect the device.
        if (!checkAppVaildStatus()) {
            return;
        }

        // If app works to make Simulation data, then doesn't connect the device
        if (makeSimData) {
            return;
        }

        // Assign machine To User
        assignMachineToUser();

        // Get Maintenance Task
        getMaintenanceTask();

        // Connect to Modbus TCP Device
        // Now We don't use this mode
        /*connectModbusTCPDevice();*/

        // Check new BLE Device changes
        String newDeviceAddr = appSettings.getBTDeviceAddr();
        if (!TextUtils.isEmpty(newDeviceAddr) && !newDeviceAddr.equalsIgnoreCase(bleDeviceAddress)) {
            disconnectBLEDevice();

            // Switch to New device
            bleDeviceAddress = newDeviceAddr;

            Log.e("report", "Try to Connect To Device!");
            connectBLEDevice();
            timePrevStatusMilis = getCurrentTime();
            timeLastReportMilis = timePrevStatusMilis;
            timeLastUncatStartTime = timePrevStatusMilis;

            mBLESignalCheckerHandler.sendEmptyMessageDelayed(MSG_SIGNALCHECKER_PER_SEC, 1000);
            mBLESignalCheckerHandler.sendEmptyMessageDelayed(MSG_TIMECOUNTER_PER_SEC, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("its", "onDestroy");

        // Set Not-Running Status
        mMyApp.setAppRunningStatus(false);

        LocalBroadcastManager.getInstance(mContext).
                unregisterReceiver(appUsageBroadcastReceiver);

        // Disconnect Modbus TCP DEVICE
        // Now We don't use this mode
        /*disconnectModbusTCPDevice();

        mDeviceConnectHandler.removeMessages(DEVICE_CONN_ON);
        mDeviceConnectHandler.removeMessages(DEVICE_CONN_OFF);
        mDeviceConnectHandler.removeMessages(DEVICE_CONN_RETRY);

        mStatusCheckerHandler.removeMessages(MSG_SIGNALCHECKER_PER_SEC);
        mStatusCheckerHandler.removeMessages(MSG_TIMECOUNTER_PER_SEC);*/

        // Make Report not to miss data
        reportStatus();

        // Save Current Shift Data
        saveCurrentShiftData();

        disconnectBLEDevice();
        mBLESignalCheckerHandler.removeMessages(MSG_SIGNALCHECKER_PER_SEC);
        mBLESignalCheckerHandler.removeMessages(MSG_TIMECOUNTER_PER_SEC);

        mBLEDeviceConnectionCheckHandler.removeMessages(MSG_DEVICE_CONNECTION_CHECK);

        appTimer.cancel();

        if (dbHelper != null) {
            try {
                dbHelper.close();
                dbHelper = null;
            } catch (Exception e) {
            }
        }

        // release POWER Manager
        wakeLock.release();

        // Release Log system
        Logger.clearLogAdapters();

        // Send Offline Status
        StatusReporter.getInstance(mContext).reportStatus("Offline");

        // Stop ChinChin Sound
        stopChinChin();

    }

    private void startBottomTimer() {
        appTimer = new Timer();
        appTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        long tEnd = System.currentTimeMillis();
                        tvDate.setText(getDateTime());
                    }
                });
            }
        }, 1000, 1000);
    }

    private void setUpUI() {
        tvMachineName = findViewById(R.id.txtMachineName);
        tvMachineCateStatus = findViewById(R.id.txtMachineCateStatus);

        // Init First Status
        tvMachineCateStatus.setText(appSettings.isJobSetupStatus() ? "Setup - UnCategorized" : "UnCategorized");
        tvMachineCateStatus.setTextColor(colorRed);

        ivCustomerAvatar = findViewById(R.id.ivCustomerAvatar);

        // PLC Connection Status
        txtPLCConnStatus = findViewById(R.id.txtPLCConnStatus);
        ivPLCConnStatus = findViewById(R.id.ivPLCConnStatus);

        // Server Connection Status
        txtServerConnStatus = findViewById(R.id.txtServerConnStatus);
        ivServerConnStatus = findViewById(R.id.ivServerConnStatus);

        ivAvatar = findViewById(R.id.ivAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resetShiftData(false);
            }
        });

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        imageOptionsWithCustomerAvatar = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.ic_logo_small)
                .showImageOnFail(R.drawable.ic_logo_small)
                .showImageForEmptyUri(R.drawable.ic_logo_small)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

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

        panelJobDetailsInfo = findViewById(R.id.panelJobDetailsInfo);

        tvJobId = findViewById(R.id.tvJobId);
        tvJobId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                resetShiftData(false);
            }
        });

        tvJobReworkSetupStatus = findViewById(R.id.tvJobReworkSetupStatus);

        tvJobDetails = findViewById(R.id.tvJobDetails);
        btnJobInfo = findViewById(R.id.btnJobInfo);
        btnJobFiles = findViewById(R.id.btnJobFiles);
        findViewById(R.id.btnJobId).setOnClickListener(this);
        findViewById(R.id.btnJobInfo).setOnClickListener(this);
        findViewById(R.id.btnJobFiles).setOnClickListener(this);

        tvDate = findViewById(R.id.tvDate);
        tvSyncTime = findViewById(R.id.tvSyncTime);
        tvUnCatTime = findViewById(R.id.tvUnCatTime);
        tvIdleTime = findViewById(R.id.tvIdleTime);

        tvGood = findViewById(R.id.tvGood);
        tvGood.setOnClickListener(this);
        findViewById(R.id.iv_good_down).setOnClickListener(this);
        findViewById(R.id.iv_good_up).setOnClickListener(this);

        tvBad = findViewById(R.id.tvBad);
        tvBad.setOnClickListener(this);
        findViewById(R.id.iv_bad_down).setOnClickListener(this);
        findViewById(R.id.iv_bad_up).setOnClickListener(this);

        ganttChart = findViewById(R.id.ganttChart);
        tvGanttTitle = findViewById(R.id.tvGantTitle);
        //findViewById(R.id.btnGanttChart).setOnClickListener(this);
        findViewById(R.id.btnSettings).setOnClickListener(this);

        // Scan User Button
        findViewById(R.id.btnScanUser).setOnClickListener(this);
        findViewById(R.id.btnLogout).setOnClickListener(this);

        // Idle Status Actions
        findViewById(R.id.panelIdleStatus).setOnClickListener(this);

        // Idle Status Panel
        panelIdleSetUp = findViewById(R.id.panelIdleSetUp);
        tvElapsedIdleTIme = findViewById(R.id.tvElapsedIdleTIme);
        backCsLockStatus = findViewById(R.id.backCsLockStatus);
        tvTitleStatusInterlocked = findViewById(R.id.tvTitleStatusInterlocked);

        blinkAnimation = new AlphaAnimation(1f, 0.3f);
        blinkAnimation.setDuration(1000); // 500
        blinkAnimation.setRepeatCount(Animation.INFINITE);
        blinkAnimation.setRepeatMode(Animation.REVERSE);

        btnCloseIdleStatus = findViewById(R.id.btnCloseIdleStatus);
        btnCloseIdleStatus.setOnClickListener(this);

        // Login Protect module
        panelLoginMessage = findViewById(R.id.panelLoginMessage);
        panelLoginMessage.setOnClickListener(this);
        findViewById(R.id.btnGotoLogin).setOnClickListener(this);

        View.OnClickListener downTimeButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lock Status
                synchronized (timeLock) {
                    // Reset Selected Status
                    resetDownTimeReasonButtonStatus();

                    // Set New Selected Button
                    v.setSelected(true);
                    opDownTimeStatus = (int) v.getTag();

                    // Change prev uncate to Downtime Reason Status
                    if (currentDeviceStatus != DEVICE_STATUS_UNCATEGORIZED) {
                        if (timeLastUncatStartTime > 0) {
                            // TODO
                            // change the gannt data status from uncate to downtime reason
                        }
                    }

                    // Change new Status
                    if (currentDeviceStatus != DEVICE_STATUS_INCYCLE) {
                        // In case of the currently InCycle, ignore
                        currentDeviceStatus = opDownTimeStatus;
                    }

                    // Global Time Logic
                    elapsedMiliseconds[currentDeviceStatus] += elapsedStopTime;
                    elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] -= elapsedStopTime;
                    if (elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] < 0) {
                        // Important Module, when the app pass the midnight In UnCate State and reset the elapsedSeconds,
                        // following module is essential
                        elapsedMiliseconds[currentDeviceStatus] += elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED];
                        elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] = 0;
                    }

                    // Shift Time Logic
                    long elapsedShiftCurrStateMils = shiftData.getElapsedTimeInMils(currentDeviceStatus);
                    long elapsedShiftUnCatMils = shiftData.getElapsedTimeInMils(DEVICE_STATUS_UNCATEGORIZED);
                    elapsedShiftCurrStateMils += elapsedStopTime;
                    elapsedShiftUnCatMils -= elapsedStopTime;
                    if (elapsedShiftUnCatMils < 0) {
                        elapsedShiftCurrStateMils += elapsedShiftUnCatMils;
                        elapsedShiftUnCatMils = 0;
                    }
                    shiftData.setElapsedTimeInMils(currentDeviceStatus, elapsedShiftCurrStateMils);
                    shiftData.setElapsedTimeInMils(DEVICE_STATUS_UNCATEGORIZED, elapsedShiftUnCatMils);

                    // When in Cycle Start Interlock and user selects downtime button, close the downtime button screen
                    if (statusCsLock == 1) {
                        hideIdleSetupPanel();

                        stopChinChin();
                    }

                    // Reset Stop Time
                    logE("Clicked Reason");
                    resetStopTime();
                }
            }
        };

        btnDownTimeStatus[0] = findViewById(R.id.btnDownTime1);
        btnDownTimeStatus[1] = findViewById(R.id.btnDownTime2);
        btnDownTimeStatus[2] = findViewById(R.id.btnDownTime3);
        btnDownTimeStatus[3] = findViewById(R.id.btnDownTime4);
        btnDownTimeStatus[4] = findViewById(R.id.btnDownTime5);
        btnDownTimeStatus[5] = findViewById(R.id.btnDownTime6);
        btnDownTimeStatus[6] = findViewById(R.id.btnDownTime7);
        btnDownTimeStatus[7] = findViewById(R.id.btnDownTime8);

        btnDownTimeStatus[0].setTag(DEVICE_STATUS_IDLE1);
        btnDownTimeStatus[1].setTag(DEVICE_STATUS_IDLE2);
        btnDownTimeStatus[2].setTag(DEVICE_STATUS_IDLE3);
        btnDownTimeStatus[3].setTag(DEVICE_STATUS_IDLE4);
        btnDownTimeStatus[4].setTag(DEVICE_STATUS_IDLE5);
        btnDownTimeStatus[5].setTag(DEVICE_STATUS_IDLE6);
        btnDownTimeStatus[6].setTag(DEVICE_STATUS_IDLE7);
        btnDownTimeStatus[7].setTag(DEVICE_STATUS_IDLE8);

        for (int i = 0; i < 8; i++) {
            btnDownTimeStatus[i].setOnClickListener(downTimeButtonClickListener);
        }

        // Disable Feature Panel
        panelDisabled = findViewById(R.id.panelDisabled);

        // Maintenance Task Panel
        panelMaintenance = findViewById(R.id.panelMaintenance);
        tvTaskMachine = findViewById(R.id.tvTaskMachine);
        tvTaskName = findViewById(R.id.tvTaskName);
        tvTaskCategory = findViewById(R.id.tvTaskCategory);
        ivTaskLogo = findViewById(R.id.ivTaskLogo);
        tvTaskInstruction = findViewById(R.id.tvTaskInstruction);
        tvTaskFrequency = findViewById(R.id.tvTaskFrequency);
        tvTaskCycleStartInterlock = findViewById(R.id.tvTaskCycleStartInterlock);
        tvTaskNotes = findViewById(R.id.tvTaskNotes);
        findViewById(R.id.btnCompleteMaintenanceTask).setOnClickListener(this);

        // Panel Gauge
        vpGauge = (ViewPager) findViewById(R.id.vpGauge);
        vpGauge.setOffscreenPageLimit(1);

        pageNavigator = (PageNavigator) findViewById(R.id.navigatorGauges);
        pageNavigator.setSize(GaugePagerAdapter.PAGE_SIZE);

        vpGauge.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pageNavigator.setPosition(position);
                pageNavigator.invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Create Fragment
        gaugeFragment = GaugeFragment.newInstance("gauge");
        utilizationFragment = UtilizationFragment.newInstance("utilization");

        // Apply Fragment
        GaugePagerAdapter pagerAdapter = new GaugePagerAdapter(getSupportFragmentManager());
        vpGauge.setAdapter(pagerAdapter);

    }

    private void setPLCConnStatus(boolean connected) {
        if (connected) {
            txtPLCConnStatus.setText(R.string.title_connected_to_plc);
            ivPLCConnStatus.setImageResource(R.drawable.gradient_circle_on);
        } else {
            txtPLCConnStatus.setText(R.string.title_disconnected_plc);
            ivPLCConnStatus.setImageResource(R.drawable.gradient_circle_off);
        }

        tankTemperatureData.setConnPLCStatus(connected);
    }

    private void setServerConnStatus(boolean connected) {
        if (connected) {
            txtServerConnStatus.setText(R.string.title_connected_to_server);
            ivServerConnStatus.setImageResource(R.drawable.gradient_circle_on);
        } else {
            txtServerConnStatus.setText(R.string.title_disconnected_server);
            ivServerConnStatus.setImageResource(R.drawable.gradient_circle_off);
        }

        tankTemperatureData.setConnServerStatus(connected);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.tvGood) {
            gotoGoodBadPartsInput();
        } else if (viewId == R.id.tvBad) {
            gotoGoodBadPartsInput();
        } else if (viewId == R.id.iv_good_down) {
            int good_val = Integer.parseInt(tvGood.getText().toString());
            if (good_val > 0) {
                good_val--;
                tvGood.setText(String.valueOf(good_val));
                appSettings.setShiftGoodParts(good_val);
                shiftData.setGoodParts(good_val);

                // Daily Live Data
                if (appSettings.getGoodParts() > 0) {
                    appSettings.setGoodParts(appSettings.getGoodParts() - 1);
                }
            }

            broadcastPartCountStatusToFragments();
        } else if (viewId == R.id.iv_good_up) {
            int good_val = Integer.parseInt(tvGood.getText().toString());
            good_val++;
            tvGood.setText(String.valueOf(good_val));
            appSettings.setShiftGoodParts(good_val);
            shiftData.setGoodParts(good_val);

            // Daily Live Data
            appSettings.setGoodParts(appSettings.getGoodParts() + 1);

            broadcastPartCountStatusToFragments();
        } else if (viewId == R.id.iv_bad_down) {
            int bad_val = Integer.parseInt(tvBad.getText().toString());
            if (bad_val > 0) {
                bad_val--;
                tvBad.setText(String.valueOf(bad_val));
                appSettings.setShiftBadParts(bad_val);
                shiftData.setBadParts(bad_val);

                if (appSettings.getBadParts() > 0) {
                    appSettings.setBadParts(appSettings.getBadParts() - 1);
                }

                // Increase Good Part
                /*
                int good_val = Integer.parseInt(tvGood.getText().toString());
                good_val++;
                tvGood.setText(String.valueOf(good_val));
                appSettings.setShiftGoodParts(good_val);
                shiftData.setGoodParts(good_val);

                appSettings.setGoodParts(appSettings.getGoodParts() + 1);
                */
            }

            broadcastPartCountStatusToFragments();
        } else if (viewId == R.id.iv_bad_up) {
            // Increase Bad Part
            int bad_val = Integer.parseInt(tvBad.getText().toString());
            bad_val++;
            tvBad.setText(String.valueOf(bad_val));
            appSettings.setShiftBadParts(bad_val);
            shiftData.setBadParts(bad_val);

            // Daily Live Data
            appSettings.setBadParts(appSettings.getBadParts() + 1);

            // Decrease Good Part
            /*
            int good_val = Integer.parseInt(tvGood.getText().toString());
            if (good_val > 0) {
                good_val--;
                tvGood.setText(String.valueOf(good_val));
                appSettings.setShiftGoodParts(good_val);
                shiftData.setGoodParts(good_val);

                // Daily Live Data
                if (appSettings.getGoodParts() > 0) {
                    appSettings.setGoodParts(appSettings.getGoodParts() - 1);
                }
            }
            */

            broadcastPartCountStatusToFragments();
        } else if (viewId == R.id.btnGanttChart) {
            Intent intent = new Intent(this, GanttChartActivity.class);
            // create the transition animation - the images in the layouts
            // of both activities are defined with android:transitionName="robot"
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    MainActivity.this,

                    // Now we provide a list of Pair items which contain the view we can transitioning
                    // from, and the name of the view it is transitioning to, in the launched activity
                    new Pair<>(findViewById(R.id.btnGanttChart),
                            GanttChartActivity.VIEW_NAME_HEADER_IMAGE));
            // start the new activity
            startActivity(intent, activityOptions.toBundle());
        } else if (viewId == R.id.btnSettings) {
            gotoSettingScreen(true);
        } else if (viewId == R.id.panelIdleStatus) {
            showIdleSetupPanel();
        } else if (viewId == R.id.btnCloseIdleStatus) {
            hideIdleSetupPanel();
        } else if (viewId == R.id.btnScanUser) {
            gotoLogin();
        } else if (viewId == R.id.btnLogout) {
            // Logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
            alertDialogBuilder.setTitle(R.string.dialog_logout_title);
            alertDialogBuilder.setMessage(R.string.dialog_logout_message)
                    .setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    logout();
                }
            }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else if (viewId == R.id.btnJobId) {
            gotoJobIDInput();
        } else if (viewId == R.id.btnJobFiles) {
            // Go to Job Guides
            gotoJobGuides();
        } else if (viewId == R.id.btnJobInfo) {
            // Go to Job Guides
            showJobDetails();
        } else if (viewId == R.id.btnGotoLogin) {
            gotoLogin();
        } else if (viewId == R.id.btnCompleteMaintenanceTask) {
            completeMaintenanceTask();
        }
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
                    shiftData.setGoodParts(newGoodPart);

                    // Show Bad Value
                    appSettings.setShiftBadParts(newBadPart);
                    tvBad.setText(String.valueOf(newBadPart));
                    shiftData.setBadParts(newBadPart);

                    // Daily Live Data
                    appSettings.setGoodParts(newGoodPart - goodPart > 0 ? newGoodPart - goodPart : 0);
                    appSettings.setBadParts(newBadPart - badPart > 0 ? newBadPart - badPart : 0);

                    broadcastPartCountStatusToFragments();
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

    private void gotoSettingScreen(boolean bAnimation) {
        // Check user level here
        if (appSettings.getUserLevel() < 4) {
            showToastMessage(R.string.msg_ask_admin_for_settings);
            return;
        }

        Intent intent = new Intent(this, SettingActivity.class);
        if (bAnimation) {
            // create the transition animation - the images in the layouts
            // of both activities are defined with android:transitionName="robot"

            /*ActivityOptions activityOptions = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                activityOptions = ActivityOptions
                        .makeSceneTransitionAnimation(this, findViewById(R.id.btnSettings), "setting");
            }*/
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    MainActivity.this,

                    // Now we provide a list of Pair items which contain the view we can transitioning
                    // from, and the name of the view it is transitioning to, in the launched activity
                    new Pair<>(findViewById(R.id.btnSettings),
                            SettingActivity.VIEW_NAME_HEADER_IMAGE));
            // start the new activity
            startActivity(intent, activityOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private void getMaintenanceTask() {
        maintenanceTaskManager = MaintenanceTaskManager.getInstance(mContext);
        mMyApp.getMaintenanceTask();
    }

    private void completeMaintenanceTask() {
        Maintenance maintenanceInfo = (Maintenance) panelMaintenance.getTag();
        if (maintenanceInfo == null) {
            panelMaintenance.setVisibility(View.GONE);
            return;
        }

        String taskNote = tvTaskNotes.getText().toString();

        final long newCycleStartTime = System.currentTimeMillis() / 1000;

        showProgressDialog();
        RequestParams requestParams = new RequestParams();
        requestParams.put("customerId", appSettings.getCustomerID());
        requestParams.put("machineId", appSettings.getMachineName());
        requestParams.put("taskId", maintenanceInfo.getId());
        requestParams.put("startTime", newCycleStartTime);      // Start New Cycle
        requestParams.put("completedTime", DateUtil.toStringFormat_20(new Date()));
        requestParams.put("note", taskNote);

        Log.e("completeMaintenanceTask", requestParams.toString());

        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "completeMaintenanceTask", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                hideProgressDialog();
                // If the response is JSONObject instead of expected JSONArray
                Log.e("completeMaintenanceTask", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {

                        //maintenanceInfo.setTaskCompleted(true);

                        // Reset Time and start it again
                        maintenanceInfo.resetInCycleTime();
                        maintenanceInfo.setStart(newCycleStartTime);

                        maintenanceTaskManager.updateLocalData();

                        panelMaintenance.setVisibility(View.GONE);

                        Log.e("completeMaintenanceTask", "Completed Task!");
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = getString(R.string.error_server_busy);
                        }

                        showAlert(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showAlert(errorMsg);
                Log.e("completeMaintenanceTask", errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showAlert(errorMsg);
                Log.e("completeMaintenanceTask", errorMsg);
            }
        });
    }

    private void gotoJobIDInput() {
        // JobID Input Channel
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_jobid_select, null);
        AlertDialog jobIdInputDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.inputCode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jobIdInputDlg.dismiss();

                // JOBID Input Dialog
                showJobIDCodeInputDialog();
            }
        });
        dialogView.findViewById(R.id.scanCode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jobIdInputDlg.dismiss();

                if (checkPermissions(mContext, PERMISSION_REQUEST_QRSCAN_STRING, false, PERMISSION_REQUEST_CODE_QRSCAN)) {
                    startActivityForResult(new Intent(mContext, FullScannerActivity.class), REQUEST_SCAN_JOBID);
                }
            }
        });

        dialogView.findViewById(R.id.cleanData).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jobIdInputDlg.dismiss();
                jobIDLogout();
            }
        });

        final SwitchCompat switchSetupStatus = dialogView.findViewById(R.id.switchSetupStatus);
        switchSetupStatus.setChecked(appSettings.isJobSetupStatus());
        final View updateSetup = dialogView.findViewById(R.id.updateSetup);
        updateSetup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jobIdInputDlg.dismiss();

                // *Should set first before change job info
                appSettings.setJobReworkStatus(false);
                appSettings.setJobSetupStatus(switchSetupStatus.isChecked());

                // Update Setup Status
                shiftData.setStatusRework(0);
                shiftData.setStatusSetup(switchSetupStatus.isChecked() ? 1 : 0);

                shiftData.calcRegime(appSettings);
                dbHelper.updateShiftData(shiftData);

                showJobInformation();
            }
        });

        // If Rework status, then disable it to prevent the distribution between Rework and Setup status
        if (appSettings.isJobReworkStatus()) {
            switchSetupStatus.setEnabled(false);
            updateSetup.setEnabled(false);
        }

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jobIdInputDlg.dismiss();

            }
        });

        jobIdInputDlg.setCanceledOnTouchOutside(false);
        jobIdInputDlg.setCancelable(false);
        jobIdInputDlg.show();
        jobIdInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void showJobIDCodeInputDialog() {
        // Check factory info first
        String accountID = appSettings.getCustomerID();
        if (TextUtils.isEmpty(accountID)) {
            showToastMessage(R.string.msg_setup_factory);
            return;
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_id_input, null);
        final EditText edtJobId = dialogView.findViewById(R.id.edtIDInput);

        AlertDialog jobIdInputDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(edtJobId);

                jobIdInputDlg.dismiss();
            }
        });

        dialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jobIdInputDlg.dismiss();

                hideKeyboard(edtJobId);

                String jobID = edtJobId.getText().toString().trim();
                if (!TextUtils.isEmpty(jobID)) {
                    getJobInfo(jobID, true);
                }
            }
        });

        jobIdInputDlg.setCanceledOnTouchOutside(false);
        jobIdInputDlg.setCancelable(false);
        jobIdInputDlg.show();
        jobIdInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        edtJobId.requestFocus();
    }

    // https://slymms.com/backend/files/1242x2688bb%20(2).png
    // SettingActivity Also use this module, when you change this module, please check it too.
    private void getJobInfo(String jobId, boolean isNewJobID) {

        String accountID = appSettings.getCustomerID();
        if (TextUtils.isEmpty(accountID) && isNewJobID) {
            showToastMessage(R.string.msg_setup_factory);
            return;
        }

        // Call Web API
        Log.e("jobID", jobId);

        if (!NetUtil.isInternetAvailable(mContext)) {
            showToastMessage("Internet is unavailable!");
            return;
        }

        if (isNewJobID) {
            showProgressDialog();
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("customerId", accountID);
        requestParams.put("jobId", jobId);
//        requestParams.put("jobId", "PRO0188843WK");

        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "getJobDataByPrOrderNo", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (isNewJobID) {
                    hideProgressDialog();
                }
                // If the response is JSONObject instead of expected JSONArray
                Log.e("jobID", response.toString());
                try {
                    if (response.has("status") && response.getBoolean("status")) {
                        Gson gson = new Gson();
                        ArrayList<MJob> jobs = gson.fromJson(response.getString("data"), new TypeToken<ArrayList<MJob>>() {
                        }.getType());
                        if (jobs.size() > 1){
                            View dialogView = getLayoutInflater().inflate(R.layout.dialog_jobid_selector, null);
                            final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(MainActivity.this)
                                    .setView(dialogView)
                                    .setCancelable(true)
                                    .create();

                            dlg.setCanceledOnTouchOutside(false);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                            RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerJob);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            ClickListener listener = new ClickListener() {
                                @Override
                                public void onClick(int index) {
                                    dlg.dismiss();
                                    selectJob(response, index, isNewJobID);
                                }
                            };
                            JobListAdapter adapter = new JobListAdapter(MainActivity.this, jobs, listener);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> {
                                dlg.dismiss();
                            });
                            dlg.setCanceledOnTouchOutside(false);
                            dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dlg.show();
                        }else if (jobs.size() == 1){
                            selectJob(response, 0, isNewJobID);
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

                if (isNewJobID) {
                    hideProgressDialog();
                }

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showToastMessage(errorMsg);
                Log.e("JOB", errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if (isNewJobID) {
                    hideProgressDialog();
                }

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                showToastMessage(errorMsg);
                Log.e("JOB", errorMsg);
            }
        });
    }

    private void selectJob(JSONObject response, int index, boolean isNewJobID){
        JSONObject jsonData = null;
        try {
            jsonData = response.getJSONArray("data").getJSONObject(index);
            String jobId = jsonData.getString("jobID");
            String jobDetails = jsonData.toString();
            // Save User Information
            appSettings.setJobId(jobId);
            appSettings.setJobDetails(jobDetails);

            showJobInformation();

            // Refresh the setting values
            try {
                JSONObject jsonObject = new JSONObject(jobDetails);
                // Parts Per Cycle
                int partsPerCycle = jsonObject.optInt("partsPerCycle");
                if (partsPerCycle > 0) {
                    appSettings.setPartsPerCycle(partsPerCycle);
                    mSettingPartsPerCycle = partsPerCycle;
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

            // As Rework status for new job status
            if (isNewJobID) {
                askReworkStatus();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void gotoJobGuides() {
        String jobDetails = appSettings.getJobDetails();
        String jobGuides = "";

        try {
            JSONObject jsonObject = new JSONObject(jobDetails);
            jobGuides = jsonObject.optString("files");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Check File
        if (TextUtils.isEmpty(jobGuides) || "null".equalsIgnoreCase(jobGuides)) {
            showToastMessage(R.string.error_no_job_guides);
            return;
        }

        // start the new activity
        Intent intent = new Intent(this, ViewAttachesActivity.class);
        intent.putExtra("files", jobGuides);
        intent.putExtra("title", "Attaches for job data");
        startActivity(intent);
    }

    private void showJobDetails() {
        tvJobId.setText(appSettings.getJobId());
        String jobDetails = appSettings.getJobDetails();

        Log.e("JobDetails", jobDetails);

        String customer = "";
        String partNumber = "";
        String description = "";
        String qtyRequired = "";
        String qtyCompleted = "";

        String aux1data = "";
        String aux2data = "";
        String aux3data = "";

        try {
            JSONObject jsonObject = new JSONObject(jobDetails);
            customer = jsonObject.optString("customer").replace("null", "");
            partNumber = jsonObject.optString("partNumber").replace("null", "");
            description = jsonObject.optString("description").replace("null", "");
            qtyRequired = jsonObject.optString("qtyRequired").replace("null", "");
            qtyCompleted = jsonObject.optString("qtyCompleted").replace("null", "");

            String orderType = jsonObject.optString("order_type").replace("null", "");
            if (orderType.equalsIgnoreCase("PRO")){
                aux1data = jsonObject.optString("aux1data").replace("null", "");
                aux2data = jsonObject.optString("aux2data").replace("null", "");
            }else{
                aux1data = jsonObject.optString("bom_dim1").replace("null", "");
                aux2data = jsonObject.optString("bom_dim2").replace("null", "");
            }
            aux3data = jsonObject.optString("aux3data").replace("null", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringBuilder strJobDetailsBuilder = new StringBuilder();
        strJobDetailsBuilder.append("Customer : <b>" + customer + "</b><br>");
        strJobDetailsBuilder.append("Part Number : <b>" + partNumber + "</b><br>");
        strJobDetailsBuilder.append("Description : <b>" + description + "</b><br>");
        strJobDetailsBuilder.append("Qty Required : <b>" + qtyRequired + "</b><br>");
        strJobDetailsBuilder.append("Qty Good Completed : <b>" + qtyCompleted + "</b><br>");

        strJobDetailsBuilder.append(appSettings.getAux1DataTitle() + " : <b>" + aux1data + "</b><br>");
        strJobDetailsBuilder.append(appSettings.getAux2DataTitle() + " : <b>" + aux2data + "</b><br>");
        strJobDetailsBuilder.append(appSettings.getAux3DataTitle() + " : <b>" + aux3data + "</b><br>");

        tvJobDetails.setText(Html.fromHtml(strJobDetailsBuilder.toString()));

        AlertDialog idInputDlg = new AlertDialog.Builder(mContext)
                .setTitle("Job Information")
                .setCancelable(true)
                .setMessage(Html.fromHtml(strJobDetailsBuilder.toString()))
                .create();

        idInputDlg.setCanceledOnTouchOutside(true);
        idInputDlg.setCancelable(true);
        idInputDlg.show();
        //idInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void updateJobReworkStatus() {
        if (appSettings.isJobReworkStatus()) {
            panelJobDetailsInfo.setBackgroundResource(R.drawable.gradient_back_ligh_red);
            tvJobReworkSetupStatus.setVisibility(View.VISIBLE);
            tvJobReworkSetupStatus.setText("(Rework)");
            tvJobReworkSetupStatus.setTextColor(0xffff160c);
        } else if (appSettings.isJobSetupStatus()) {
            panelJobDetailsInfo.setBackgroundResource(R.drawable.gradient_back_ligh_blue);
            tvJobReworkSetupStatus.setVisibility(View.VISIBLE);
            tvJobReworkSetupStatus.setText("(Setup)");
            tvJobReworkSetupStatus.setTextColor(0xff5080ff);
        } else {
            panelJobDetailsInfo.setBackgroundResource(R.drawable.gradient_back);
            tvJobReworkSetupStatus.setVisibility(View.GONE);
        }
    }

    private void gotoLogin() {
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
                showLoginIDCodeInputDialog();
            }
        });
        dialogView.findViewById(R.id.scanCode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                idInputDlg.dismiss();

                if (checkPermissions(mContext, PERMISSION_REQUEST_QRSCAN_STRING, false, PERMISSION_REQUEST_CODE_QRSCAN)) {
                    startActivityForResult(new Intent(mContext, FullScannerActivity.class), REQUEST_SCAN_USER);
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

    private void showLoginIDCodeInputDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_login, null);

//        final EditText edtFactoryIdInput = dialogView.findViewById(R.id.edtFactoryIDInput);
        final EditText edtUserIdInput = dialogView.findViewById(R.id.edtUserIDInput);

        AlertDialog loginIdInputDlg = new AlertDialog.Builder(mContext)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                hideKeyboard(edtFactoryIdInput);
                hideKeyboard(edtUserIdInput);

                loginIdInputDlg.dismiss();
            }
        });

        dialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginIdInputDlg.dismiss();

//                hideKeyboard(edtFactoryIdInput);
                hideKeyboard(edtUserIdInput);

                String userID = edtUserIdInput.getText().toString().trim();
                if (!TextUtils.isEmpty(userID)) {
                    getUserInfo(userID);
                }
            }
        });

        loginIdInputDlg.setCanceledOnTouchOutside(false);
        loginIdInputDlg.setCancelable(false);
        loginIdInputDlg.show();
        loginIdInputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        edtUserIdInput.requestFocus();
    }

    private void getUserInfo(String userId) {

        // Call Web API
        Log.e(TAG, "Login");

        // To Do List
        // API name loginWithCustomerId.php
        // Params : customerid, deviceid
        // Response : {status : true, data : {username : "User1", avatar : "http://..."}}

        // Check network status
        if (!NetUtil.isInternetAvailable(mContext)) {
            showToastMessage("Internet is unavailable!");
            return;
        }

        showProgressDialog();

        RequestParams requestParams = new RequestParams();
        String factoryId = appSettings.getCustomerID();
        String newUserId = userId;
        if (userId.equalsIgnoreCase("3039681707")){
            newUserId = "6566";
        }
        requestParams.put("userId", newUserId);
        if (!TextUtils.isEmpty(factoryId)){
            requestParams.put("customerId", factoryId);
        }
        requestParams.put("deviceId", mMyApp.getAndroidId());
        GoogleCertProvider.install(mContext);
        String finalUserId = newUserId;
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
                        appSettings.setUserId(finalUserId);
                        appSettings.setCustomerID(factoryId);
                        appSettings.setUserName(name);
                        appSettings.setUserAvatar(logo/*"https://i1.sndcdn.com/avatars-000002504134-byor70-t300x300.jpg"*/);
                        appSettings.setUserLevel(userLevel);
                        appSettings.setLoggedIn();

                        updateUserInfo();

                        broadcastLoginLogoutStatusToFragments();
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

    private void assignMachineToUser() {
        // Check Data
        if (TextUtils.isEmpty(appSettings.getCustomerID()) || TextUtils.isEmpty(appSettings.getMachineName()) || TextUtils.isEmpty(appSettings.getUserName())) {
            return;
        }

        // Call Web API
        Log.e(TAG, "Machine");

        // showProgressDialog();

        RequestParams requestParams = new RequestParams();
        requestParams.put("userId", appSettings.getUserId());
        requestParams.put("userName", appSettings.getUserName());
        requestParams.put("accountID", appSettings.getCustomerID());
        requestParams.put("machineName", appSettings.getMachineName());
        requestParams.put("versionInfo", String.format("%s%s", getVersionName(), MyApplication.TEST_VERSION ? "b" : ""));

        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "assignMachineToUser", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                // hideProgressDialog();

                // If the response is JSONObject instead of expected JSONArray
                Log.e("Machine", response.toString());

                try {
                    if (response.has("status") && response.getBoolean("status")) {
                    } else {
                        String message = "";
                        if (response.has("message") && !response.isNull("message")) {
                            message = response.getString("message");
                        }

                        if (TextUtils.isEmpty(message)) {
                            message = getString(R.string.error_server_busy);
                        }

                        //showToastMessage(message);

                        // Server is Online
                        setServerConnStatus(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToastMessage(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                // Server is Offline
                setServerConnStatus(false);

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                Log.e("Machine", errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                // Server is Offline
                setServerConnStatus(false);

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                }

                //showAlert(errorMsg);
                Log.e("Machine", errorMsg);
            }
        });
    }

    private void updateGanttChart() {
        // Apply Gantt Data
        ganttChart.setGanttData(ganttDataList);
        if (ganttDataList.size() > 0) {
            tvGanttTitle.setVisibility(View.GONE);
        } else {
            tvGanttTitle.setVisibility(View.VISIBLE);
        }
    }

    private void getGanttDataHistory() {

        // Call Web API
        Log.e(TAG, "GanttData");

        String accountID = appSettings.getCustomerID();
        if (TextUtils.isEmpty(accountID)) {
            return;
        }

        if (!NetUtil.isInternetAvailable(mContext)) {
            showToastMessage("Internet is unavailable!");
            return;
        }

        // To Do List
        // API name loginWithCustomerId.php
        // Params : customerid, deviceid
        // Response : {status : true, data : {username : "User1", avatar : "http://..."}}
        RequestParams requestParams = new RequestParams();
        requestParams.put("customerId", appSettings.getCustomerID());
        requestParams.put("machineId", appSettings.getMachineName());
        requestParams.put("date", DateUtil.toStringFormat_22(new Date()));

        GoogleCertProvider.install(mContext);
        //HttpsTrustManager.allowAllSSL();
        ITSRestClient.post(mContext, "getGanttDataMobile", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (weakReference.get() == null || !isActivityRunning())
                    return;

                // If the response is JSONObject instead of expected JSONArray
                // Log.e("GanttData", response.toString());
                try {
                    if (response.has("status") && response.getBoolean("status")) {
                        JSONArray ganttData = response.getJSONArray("data");

                        synchronized (ganttDataList) {
                            ganttDataList.clear();
                            GanttPlotModel lastSeg = null;
                            for (int i = 0; i < ganttData.length(); i++) {
                                JSONObject ganttObj = ganttData.getJSONObject(i);
                                String color = ganttObj.getString("color");
                                long start = ganttObj.getLong("start");
                                long end = ganttObj.getLong("end");

                                GanttPlotModel newSeg = new GanttPlotModel(start, end - start, color);
                                // Check same status of sequence segment
                                if (newSeg.isNextSameSegment(lastSeg)) {
                                    // Only update existing segment
                                    // Log.e("gantt", "Updated prev segment!!!");
                                    lastSeg.setLength(end - lastSeg.getStart());
                                } else {
                                    // Log.e("gantt", "new segment!!!");
                                    ganttDataList.add(newSeg);
                                    lastSeg = newSeg;
                                }
                            }
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

                // Load Local Gantt
                List<GanttDataModel> todayGanttData = dbHelper.getTodayGanttData();
                synchronized (ganttDataList) {

                    GanttPlotModel lastSeg = null;
                    for (int i = 0; i < todayGanttData.size(); i++) {
                        GanttDataModel ganttData = todayGanttData.get(i);

                        GanttPlotModel newSeg = new GanttPlotModel(ganttData.getStart() / 1000,
                                (ganttData.getEnd() - ganttData.getStart()) / 1000,
                                ganttData.getColor());
                        // Check same status of sequence segment
                        if (newSeg.isNextSameSegment(lastSeg)) {
                            // Only update existing segment
                            // Log.e("gantt", "Updated prev segment!!!");
                            lastSeg.setLength(ganttData.getEnd() - lastSeg.getStart());
                        } else {
                            // Log.e("gantt", "new segment!!!");
                            ganttDataList.add(newSeg);
                            lastSeg = newSeg;
                        }
                    }
                }

                // Refresh UI
                updateGanttChart();

                // Server is Online
                setServerConnStatus(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                if (weakReference.get() == null || !isActivityRunning())
                    return;

                // Server is Online
                setServerConnStatus(false);

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                } else {
                    errorMsg = getString(R.string.error_network);
                }

                showAlert(errorMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                Log.e("Gantt", errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if (weakReference.get() == null || !isActivityRunning())
                    return;

                // Server is Online
                setServerConnStatus(false);

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                } else {
                    errorMsg = getString(R.string.error_network);
                }

                showAlert(errorMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                Log.e("Gantt", errorMsg);
            }
        });
    }

    private void showJobLogoutDlg(){
        // Logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.dialog_logout_title);
        alertDialogBuilder.setMessage(R.string.dialog_jobid_logout_message)
                .setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        jobIDLogout();
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

        updateUserInfo();

        broadcastLoginLogoutStatusToFragments();

        if (currentDeviceStatus != DEVICE_STATUS_INCYCLE){
            showJobLogoutDlg();
        }
    }

    private void jobIDLogout() {
        // *Should set first before change job info
        appSettings.setJobReworkStatus(false);
        appSettings.setJobSetupStatus(false);

        appSettings.setJobId("");
        appSettings.setJobDetails("");
        appSettings.setTargetCycleTimeSeconds(0);

        showJobInformation();
    }

    private void updateUserInfo() {
        mSettingIsLoggedIn = appSettings.isLoggedIn();

        tvUserName.setText(appSettings.getUserName());
        imageLoader.displayImage(appSettings.getUserAvatar(), ivAvatar, imageOptionsWithUserAvatar);

        assignMachineToUser();

        // Hide Login Lock Screen if it was opened
        if (mSettingIsLoggedIn && panelLoginMessage.getVisibility() == View.VISIBLE) {
            panelLoginMessage.setVisibility(View.GONE);
        }
    }

    private void broadcastLoginLogoutStatusToFragments() {
        Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
        intent.putExtra("STATUS", AlarmSettings.STATUS_MAIN_LOGIN_LOGOUT);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void broadcastPartCountStatusToFragments() {
        Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
        intent.putExtra("STATUS", AlarmSettings.STATUS_MAIN_PARTS_COUNT_CHANGED);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void updatePartsCount() {
        // Update Goods
        int shiftGoodParts = appSettings.getShiftGoodParts();
        tvGood.setText(String.valueOf(shiftGoodParts));
        shiftData.setGoodParts(shiftGoodParts);

        // Update bads
        int shiftBadParts = appSettings.getShiftBadParts();
        tvBad.setText(String.valueOf(shiftBadParts));
        shiftData.setBadParts(shiftBadParts);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e(TAG, "" + requestCode + "-" + resultCode);
            if (requestCode == REQUEST_SCAN_USER) {
                getUserInfo(data.getStringExtra("code"));
            } else if (requestCode == REQUEST_SCAN_JOBID) {
                getJobInfo(data.getStringExtra("code"), true);
            }
        }
    }

    private void startAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // setInExactAndRepeating – This doesn’t trigger the alarm at the exact time.
            // setExact – Setting an alarm manager using this ensures that the OS triggers the alarm at almost the exact time.
            // setExactAndAllowWhileIdle – This method came up with Android M. These types of alarms are allowed to get executed even in low power modes.

            // alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, AlarmSettings.INTERVAL, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, AlarmSettings.INTERVAL, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, AlarmSettings.INTERVAL, pendingIntent);
        }
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }

    private static final int ALERT_TYPE_OUTCYCLE = 1;
    private static final int ALERT_TYPE_STOPTIMELIMIT = 2;

    boolean sentStopTimeAlert = false;

    private boolean sendAlert(int alertType) {

        if (!appSettings.isCycleStopAlert()) {
            logE("Mail, not alert set or not loggedin");
            return false;
        }

        ArrayList<String> toMails = new ArrayList<>();
        if (isValidEmail(appSettings.getAlertEmail1())) {
            toMails.add(appSettings.getAlertEmail1());
        }
        if (isValidEmail(appSettings.getAlertEmail2())) {
            toMails.add(appSettings.getAlertEmail2());
        }
        if (isValidEmail(appSettings.getAlertEmail3())) {
            toMails.add(appSettings.getAlertEmail3());
        }

        if (toMails.isEmpty()) {
            logE("Mail, Not recepters");
            return false;
        }

        // Check App Valid Status, We don't send mail.
        if (appSettings.isValidStatus() == false) {
            return true;
        }

        String subject = "";
        String contents = "";
        if (alertType == ALERT_TYPE_OUTCYCLE) {
            subject = "Cycle Stop Time";
            //Machine Name’ cycle ended at: ‘cycle stop time”
            String machineName = appSettings.getMachineName();
            String cycleStopTime = DateUtil.toStringFormat_12(new Date());
            contents = String.format("%s' cycle ended at %s.", machineName, cycleStopTime);
            return true;
        } else if (alertType == ALERT_TYPE_STOPTIMELIMIT) {
            subject = "Exceed Stop Time";

            //Makino has been stopped for x minutes”
            String machineName = appSettings.getMachineName();
            float stopTimeMins = mSettingStopTimeLimit / 60000f;
            contents = String.format("%s has been stopped for %.1f minute(s) in %s.", machineName, stopTimeMins, DateUtil.toStringFormat_20(new Date()));
        }

        //https://myaccount.google.com/lesssecureapps
        new MailSender("reports@slymms.com",
                "246896321S!",
                toMails,
                subject,
                contents, new MailSender.MailSendCallback() {
            @Override
            public void onMailFailed(String error) {
                logE("Mail, Failed - " + error);
            }

            @Override
            public void onMailSent() {
                logE("Mail, Sent!");
            }
        }).execute();

        return true;
    }

    MediaPlayer chinchinPlayer;

    public void stopChinChin() {
        try {
            if (chinchinPlayer != null && chinchinPlayer.isPlaying()) {
                chinchinPlayer.stop();
                chinchinPlayer.release();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        chinchinPlayer = null;
    }

    public void playChinChin() {
        if (mSettingIsEnabledCSLockSound) {
            // Play Sound
            try {
                stopChinChin();

                chinchinPlayer = new MediaPlayer();
                AssetFileDescriptor descriptor = getAssets().openFd("chinchin.mp3");
                chinchinPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                chinchinPlayer.prepare();
                chinchinPlayer.setVolume(1f, 1f);
                chinchinPlayer.setLooping(true);
                chinchinPlayer.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class GaugePagerAdapter extends FragmentStatePagerAdapter {

        public static final int PAGE_SIZE = 2;

        public GaugePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return gaugeFragment;
            } else if (position == 1) {
                return utilizationFragment;
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return PAGE_SIZE;
        }
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
        } else if (requestCode == PERMISSION_REQUEST_CODE_FILELOGGER) {
            if (bAllGranted) {
                initLogSystem();
            } else {
                showAlert(R.string.msg_permission_file_log);
            }
        }else if (requestCode == PERMISSION_REQUEST_CODE_BLUETOOTH_CONNECT) {
            if (bAllGranted) {
                connect();
            } else {
                showAlert(R.string.error_bluetooth_not_enabled);
            }
        }
    }
    // ---------------------------------------------------------------------------------------------

    // File Logger System --------------------------------------------------------------------------
    private static final boolean USE_FILE_LOG = true;
    private boolean isUsingFileLog = false;

    private void initLogSystem() {
        if (!USE_FILE_LOG)
            return;

        // Check Permissions
        if (checkPermissions(mContext, PERMISSION_REQUEST_FILELOGGER_STRING, true, PERMISSION_REQUEST_CODE_FILELOGGER)) {
            // Trigger App download Task Here

            Logger.clearLogAdapters();

            CsvFormatStrategy csvFormatStrategy = CsvFormatStrategy.newBuilder().build();
            Logger.addLogAdapter(new AndroidLogAdapter(csvFormatStrategy));

            isUsingFileLog = true;

            // First log machine name
            logE(appSettings.getMachineName());
        }
    }

    private void logE(String contents) {
        logError(null, contents);

        Log.e("MMS", contents);
    }

    private void logError(String tag, String contents) {
        if (!isUsingFileLog)
            return;

        if (TextUtils.isEmpty(tag)) {
            Logger.t(tag).e(contents);
        } else {
            Logger.e(contents);
        }
    }

    private void logSample() {
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("message");

        Logger.clearLogAdapters();

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
//        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("My custom tag")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        Logger.addLogAdapter(new DiskLogAdapter());

        Logger.w("no thread info and only 1 method");

        Logger.clearLogAdapters();
        formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        CsvFormatStrategy csvFormatStrategy = CsvFormatStrategy.newBuilder().build();
        Logger.addLogAdapter(new AndroidLogAdapter(csvFormatStrategy));

        Logger.i("no thread info and method info");

        Logger.t("tag").e("Custom tag for only one use");

        Logger.json("{ \"key\": 3, \"value\": something}");

        Logger.d(Arrays.asList("foo", "bar"));

        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        map.put("key1", "value2");

        Logger.d(map);

        Logger.clearLogAdapters();
        formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .tag("MyTag")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        Logger.w("my log message with my tag");
    }
    //----------------------------------------------------------------------------------------------

    //--- Time Report Screen Actions
    private void showTimeReportPanel() {

        int processMonitorType = appSettings.getProcessMonitorType();
        boolean createNewMonitorPanel = false;

        if (processMonitorType == 0) {
            showToastMessage(R.string.msg_choose_process_monitor_type);
            return;
        }

        if(processMonitorFragment == null) {
            createNewMonitorPanel = true;
        } else {
            if (processMonitorFragment instanceof TimeReportFragment && processMonitorType != 1) {
                createNewMonitorPanel = true;
            } else if (processMonitorFragment instanceof CleaningStationFragment && processMonitorType != 2) {
                createNewMonitorPanel = true;
            } else if (processMonitorFragment instanceof BlastStationFragment && processMonitorType != 3) {
                createNewMonitorPanel = true;
            } else if (processMonitorFragment instanceof PaintStationFragment && processMonitorType != 4) {
                createNewMonitorPanel = true;
            } else if (processMonitorFragment instanceof AssemblyStation137Fragment && processMonitorType != 5) {
                createNewMonitorPanel = true;
            } else if (processMonitorFragment instanceof AssemblyStation136Fragment && processMonitorType != 6) {
                createNewMonitorPanel = true;
            } else if (processMonitorFragment instanceof AssemblyStation3Fragment && processMonitorType != 7) {
                createNewMonitorPanel = true;
            } else if (processMonitorFragment instanceof QualityFragment && processMonitorType != 8) {
                createNewMonitorPanel = true;
            }
        }

        if (createNewMonitorPanel) {
            String panelTag = "";
            if (processMonitorType == 1) {
                panelTag = "TimeReportPanel";
                processMonitorFragment = TimeReportFragment.newInstance("TimeReport");
            } else if (processMonitorType == 2) {
                panelTag = "CleaingStationPanel";
                processMonitorFragment = CleaningStationFragment.newInstance("CleaingStation");
            } else if (processMonitorType == 3) {
                panelTag = "BlastStationPanel";
                processMonitorFragment = BlastStationFragment.newInstance("BlastStation");
            } else if (processMonitorType == 4) {
                panelTag = "PaintStationPanel";
                processMonitorFragment = PaintStationFragment.newInstance("PaintStation");
            } else if (processMonitorType == 5) {
                panelTag = "AssemblyStationPanel137";
                processMonitorFragment = AssemblyStation137Fragment.newInstance("AssemblyStation137");
            } else if (processMonitorType == 6) {
                panelTag = "AssemblyStationPanel136";
                processMonitorFragment = AssemblyStation136Fragment.newInstance("AssemblyStation136");
            } else if (processMonitorType == 7) {
                panelTag = "AssemblyStationPanel3";
                processMonitorFragment = AssemblyStation3Fragment.newInstance("AssemblyStation3");
            } else if (processMonitorType == 8) {
                panelTag = "QualityStationPanel";
                processMonitorFragment = QualityFragment.newInstance("QualityStation");
            } else {
                panelTag = "ProcessMonitorPanel";
                processMonitorFragment = BaseFragment.newInstance("ProcessMonitor");
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.containerTimeReport, processMonitorFragment, panelTag).commit();
        }

        containerTimeReport.setVisibility(View.VISIBLE);
    }

    private void hideTimeReportPanel() {
        containerTimeReport.setVisibility(View.GONE);
    }
    //----------------------------------------------------------------------------------------------

    //--- Shift Data Process -----------------------------------------------------------------------
    private void getShiftsData() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("customer_id", appSettings.getCustomerID());
        requestParams.put("machineId", appSettings.getMachineName());
        requestParams.put("date", DateUtil.toStringFormat_22(new Date()));

        GoogleCertProvider.install(mContext);
        //HttpsTrustManager.allowAllSSL();
        ITSRestClient.post(mContext, "get_shift_detail", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (weakReference.get() == null || !isActivityRunning())
                    return;

                // If the response is JSONObject instead of expected JSONArray
                // Log.e("GanttData", response.toString());
                try {
                    if (response.has("status") && response.getBoolean("status")) {
                        JSONObject jsonShiftData = response.getJSONObject("shift_info");

                        String shift1Start = jsonShiftData.getString("shift1_start");
                        String shift1End = jsonShiftData.getString("shift1_end");

                        String shift2Start = jsonShiftData.getString("shift2_start");
                        String shift2End = jsonShiftData.getString("shift2_end");

                        String shift3Start = jsonShiftData.getString("shift3_start");
                        String shift4End = jsonShiftData.getString("shift3_end");

                        
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

                if (weakReference.get() == null || !isActivityRunning())
                    return;

                // Server is Online
                setServerConnStatus(false);

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                } else {
                    errorMsg = getString(R.string.error_network);
                }

                showAlert(errorMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {}
                });
                Log.e("Shifts", errorMsg);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                if (weakReference.get() == null || !isActivityRunning())
                    return;

                // Server is Online
                setServerConnStatus(false);

                String errorMsg = throwable.getMessage();
                if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                    errorMsg = getString(R.string.error_connection_timeout);
                } else {
                    errorMsg = getString(R.string.error_network);
                }

                showAlert(errorMsg, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {}
                });
                Log.e("Shifts", errorMsg);
            }
        });
    }

    // Reset Shift data whenever Shift Triggers(Log In/Out, Job In/Out, Shift Time, Midnight)
    private void resetShiftData(boolean isForMidnightOrNewShift) {

        String machine = appSettings.getMachineName();
        String currJobID = appSettings.getJobId();
        String operator = appSettings.getUserName();
        String userId = appSettings.getUserId();

        // Same Shift Data
        if (!isForMidnightOrNewShift) {
            // *In case of midnight, should process shift data reset forcebly.
            // *In case of normal time, should check dup data, when start app, and when set machine name, job, operator name,
            // this function is called several times at the same time
            if (machine.equals(shiftData.getMachine()) && currJobID.equals(shiftData.getJobID()) && operator.equals(shiftData.getOperator())) {
                Log.e("shiftData", "Same Job Info");
                return;
            }
        }

        // Complete current Shift and create new Shift -----

        // Check valid Shift Data
        if(shiftData.getStartTime() > 0 && shiftData.getStopTime() - shiftData.getStartTime() > 5000 && !TextUtils.isEmpty(shiftData.getMachine())) {
            Log.e("shiftData", "Save old shift data");

            shiftData.calcRegime(appSettings);
            shiftData.setCompleted(true);

            dbHelper.updateShiftData(shiftData);

            // Process Counter Values
            // *To use live data for daily good/bad parts, we disable followings.
            // *Instead of this, we calc daily good/bad parts whenever shift good/bad changes
            //// appSettings.setPartsGood(appSettings.getPartsGood() + shiftData.getGoodParts());
            //// appSettings.setPartsBad(appSettings.getPartsBad() + shiftData.getBadParts());
        } else {
            // Invalid Shift Data and remove it.
            dbHelper.deleteShiftData(shiftData);
        }

        // Reset Shift Counter Values
        appSettings.setShiftGoodParts(0);
        appSettings.setShiftBadParts(0);
        tvGood.setText("0");
        tvBad.setText("0");

        long currTimeMils = getCurrentTime();

        // Create new Shift Data
        shiftData.resetData();

        // Set the current JOB Data and AUX Data
        shiftData.setJobID(currJobID);
        shiftData.setJobSequenceNo("");

        String jobDetails = appSettings.getJobDetails();
        String valAuxData1 = "";
        String valAuxData2 = "";
        String valAuxData3 = "";
        try {
            JSONObject jsonObject = new JSONObject(jobDetails);
            String orderType = jsonObject.optString("order_type").replace("null", "");
            if (orderType.equalsIgnoreCase("PRO")){
                valAuxData1 = jsonObject.optString("aux1data").replace("null", "");
                valAuxData2 = jsonObject.optString("aux2data").replace("null", "");
            }else{
                valAuxData1 = jsonObject.optString("bom_dim1").replace("null", "");
                valAuxData2 = jsonObject.optString("bom_dim2").replace("null", "");
            }
            valAuxData3 = jsonObject.optString("aux3data").replace("null", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            shiftData.setAuxData1(Float.parseFloat(valAuxData1));
        } catch (Exception e) {
        }

        try {
            shiftData.setAuxData2(Float.parseFloat(valAuxData2));
        } catch (Exception e) {
        }

        try {
            shiftData.setAuxData3(Float.parseFloat(valAuxData3));
        } catch (Exception e) {
        }

        // Set Target Cycle Time Seconds
        shiftData.setTargetCycleTimeSeconds(appSettings.getTargetCycleTimeSeconds()); // This is second value, no need to convert

        // -- Rework/Setup Status logic
        shiftData.setStatusRework(appSettings.isJobReworkStatus() ? 1 : 0);
        shiftData.setStatusSetup(appSettings.isJobSetupStatus() ? 1 : 0);

        shiftData.setStartTime(currTimeMils);
        shiftData.setStopTime(currTimeMils);

        // Current Shift Setting
        ShiftTime currShiftInfo = ShiftTimeManager.getInstance().getShiftTime(currTimeMils);
        if (currShiftInfo != null) {
            // Set the Shift Setting info
            shiftData.setShiftStartSetting((long) currShiftInfo.getStart() * 60000);
            shiftData.setShiftEndSetting((long) currShiftInfo.getEnd() * 60000);
            shiftData.setShiftSetting(currShiftInfo.toString());

            // Set the P.P.T
            ArrayList<ShiftTime> shiftTimes = ShiftTimeManager.getInstance().getShiftTimes();
            int idxOfShift = shiftTimes.indexOf(currShiftInfo);
            if (idxOfShift == 0) {
                shiftData.setPlannedProductionTime(appSettings.getShift1PPT());
            } else if (idxOfShift == 1) {
                shiftData.setPlannedProductionTime(appSettings.getShift2PPT());
            } else if (idxOfShift == 2) {
                shiftData.setPlannedProductionTime(appSettings.getShift3PPT());
            } else {
                shiftData.setPlannedProductionTime(appSettings.getPlannedProductionTime());
            }
        }

        shiftData.setMachine(machine);

        shiftData.setOperator(operator);
        shiftData.setUserID(userId);

        shiftData.calcRegime(appSettings);

        dbHelper.insertShiftData(shiftData);
        Log.e("shiftData", "New Shift : ID=" + shiftData.getId());

        // Get Original Parts Info
        RequestParams requestParams = new RequestParams();
        requestParams.put("customer_id", appSettings.getCustomerID());
        requestParams.put("operator", operator);
        requestParams.put("machine", machine);
        requestParams.put("date", DateUtil.toStringFormat_13(new Date(currTimeMils)));
        requestParams.put("jobID", currJobID);

        GoogleCertProvider.install(mContext);
        ITSRestClient.post(mContext, "getCurrentPartsCount", requestParams, partsCountListener);

        // Remove old shift data
        dbHelper.removeOldShiftData();
    }

    JsonHttpResponseHandler partsCountListener = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

            if (weakReference.get() == null || !isActivityRunning())
                return;

            // If the response is JSONObject instead of expected JSONArray
            Log.e("getCurrentPartsCount", response.toString());

            try {
                if (response.has("status") && response.getBoolean("status")) {

                    JSONObject jsonStation = response.getJSONObject("data");

                    // These values is only related with the Shift Data
                    int prevGoods = jsonStation.optInt("goodParts");
                    int prevBads = jsonStation.optInt("badParts");

                    shiftData.setPrevGoodParts(prevGoods);
                    shiftData.setPrevBadParts(prevBads);

                    int currGoods = shiftData.getGoodParts() + prevGoods;
                    int currBads = shiftData.getBadParts() + prevBads;

                    shiftData.setGoodParts(currGoods);
                    shiftData.setBadParts(currBads);

                    appSettings.setShiftGoodParts(currGoods);
                    appSettings.setShiftBadParts(currBads);

                    tvGood.setText(String.valueOf(currGoods));
                    tvBad.setText(String.valueOf(currBads));

                    broadcastPartCountStatusToFragments();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

            String errorMsg = throwable.getMessage();
            if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                errorMsg = getString(R.string.error_connection_timeout);
            }

            //showAlert(errorMsg);
            Log.e("NetErr", errorMsg);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            String errorMsg = throwable.getMessage();
            if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                errorMsg = getString(R.string.error_connection_timeout);
            }

            //showAlert(errorMsg);
            Log.e("NetErr", errorMsg);
        }
    };

    private void saveCurrentShiftData() {
        // Check valid Shift Data
        if(shiftData.getStartTime() > 0 && shiftData.getStopTime() - shiftData.getStartTime() > 3000 && !TextUtils.isEmpty(shiftData.getMachine())) {
            Log.e("shiftData", "Save old shift data");

            shiftData.calcRegime(appSettings);
            shiftData.setCompleted(true);

            dbHelper.updateShiftData(shiftData);
        }
    }

    private void askReworkStatus() {
        appSettings.setJobSetupStatus(false);

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_rework)
                .setMessage(R.string.dialog_msg_rework)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        appSettings.setJobReworkStatus(true);
                        shiftData.setStatusRework(1);
                        dbHelper.updateShiftData(shiftData);

                        updateJobReworkStatus();
                    }})
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appSettings.setJobReworkStatus(false);
                        shiftData.setStatusRework(0);
                        dbHelper.updateShiftData(shiftData);

                        updateJobReworkStatus();
                    }
                }).show();
    }

    //----------------------------------------------------------------------------------------------

    //--- Back Button Process
    boolean isFinish = false;

    class FinishTimer extends CountDownTimer {
        public FinishTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            isFinish = true;
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            isFinish = false;
        }
    }

    @Override
    public void onBackPressed() {

        // Close Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        // If Panel Setup opened, then close it.
        if (panelIdleSetUp.getVisibility() == View.VISIBLE) {
            hideIdleSetupPanel();
            return;
        }

        if (containerTimeReport.getVisibility() == View.VISIBLE) {
            hideTimeReportPanel();
            return;
        }

        if (!isFinish) {
            showToastMessage(R.string.finish_message);
            FinishTimer timer = new FinishTimer(2000, 1);
            timer.start();
        } else {
            finish();
        }
    }
}
