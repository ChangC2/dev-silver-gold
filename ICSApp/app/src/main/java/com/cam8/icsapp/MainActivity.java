package com.cam8.icsapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cam8.icsapp.bluetooth.SerialListener;
import com.cam8.icsapp.bluetooth.SerialService;
import com.cam8.icsapp.bluetooth.SerialSocket;
import com.cam8.icsapp.views.PressureGauge;
import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ServiceConnection, SerialListener {

    WeakReference<MainActivity> weakReference;

    // Top bar Information
    TextView tvDataTime;
    TextView tvSerialInfo;

    Timer appTimer;

    // Device Model
    TextView tvModel;

    // Device Status
    TextView txtMachineStatus;
    ImageView ivStatus;

    // Pressure Gauge Control
    ImageView ivGaugeBack;
    ImageView ivGaugeUnit;
    PressureGauge gaugePressure;

    // Device Settings
    TextView tvPressure;
    TextView tvPressureUnit;
    TextView tvCurrentPressure;
    TextView tvCurrentPressureUnit;
    TextView tvPressureSetPoint;
    TextView tvPressureSetPointUnit;

    SeekBar seekPressure;
    TextView tvStatus;
    TextView tvElapsedTime;
    TextView tvFilterLife;
    TextView tvFilterLastChanged;

    Random randomGen = new Random();

    AlphaAnimation alphaAnimation;

    private boolean isUnitPSI = true;
    private int currentPressueInPSI = 0;
    private static final float PSI_TO_BAR = 0.0689476f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setContentInsetsAbsolute(0,toolbar.getContentInsetStartWithNavigation());

        // Define the WeakReference
        weakReference = new WeakReference<>(this);

        // Set up DrawLayout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Define navigation Callback Handler
        navigationView.setNavigationItemSelectedListener(this);

        // Load Status Blink Animation
        alphaAnimation = new AlphaAnimation(1f, 0.3f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        // Top bar Information
        tvDataTime = findViewById(R.id.tvDataTime);
        tvSerialInfo = findViewById(R.id.tvSerialInfo);

        // Device Model
        tvModel = findViewById(R.id.tvModel);

        // Device Status
        txtMachineStatus = findViewById(R.id.txtMachineStatus);
        ivStatus = findViewById(R.id.ivStatus);

        // Gauge
        ivGaugeBack = findViewById(R.id.ivGaugeBack);
        ivGaugeUnit = findViewById(R.id.ivGaugeUnit);
        gaugePressure = findViewById(R.id.gaugePressure);

        // Device Settings
        tvPressure = findViewById(R.id.tvPressure);
        tvPressureUnit = findViewById(R.id.tvPressureUnit);
        tvCurrentPressure = findViewById(R.id.tvCurrentPressure);
        tvCurrentPressureUnit = findViewById(R.id.tvCurrentPressureUnit);
        tvPressureSetPoint = findViewById(R.id.tvPressureSetPoint);
        tvPressureSetPointUnit = findViewById(R.id.tvPressureSetPointUnit);

        seekPressure = findViewById(R.id.seekPressure);

        // We use 0 ~ 1000 psi
        seekPressure.setMax(1000);
        seekPressure.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {

                if(isUnitPSI) {
                    tvPressureSetPoint.setText(String.valueOf(value));
                } else {
                    tvPressureSetPoint.setText(String.format("%.2f", value * PSI_TO_BAR));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        tvStatus = findViewById(R.id.tvStatus);
        tvElapsedTime = findViewById(R.id.tvElapsedTime);
        tvFilterLife = findViewById(R.id.tvFilterLife);
        tvFilterLastChanged = findViewById(R.id.tvFilterLastChanged);

        // Trigger Timer
        appTimer = new Timer();
        appTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        long tEnd = System.currentTimeMillis();
                        tvDataTime.setText(getDateTime());
                    }
                });
            }
        }, 1000, 1000000);

        // Check Bluetooth Presence
        bleDeviceAddress = appSettings.getBTDeviceAddr();
        if (TextUtils.isEmpty(bleDeviceAddress)) {
            showToastMessage("Please select PLC to pair");
            startActivity(new Intent(mContext, DeviceScanActivity.class));
        } else {
            connectBLEDevice();

            timePrevStatusMilis = getCurrentTime();

            mBLESignalCheckerHandler.sendEmptyMessageDelayed(MSG_SIGNALCHECKER_PER_SEC, 1000);
            mBLESignalCheckerHandler.sendEmptyMessageDelayed(MSG_TIMECOUNTER_PER_SEC, 1000);
        }

        mBLEDeviceConnectionCheckHandler.sendEmptyMessageDelayed(MSG_DEVICE_CONNECTION_CHECK, DEVICE_RECONNECT_INTERVAL);

        // Check App Version
        checkAppVersion();
    }

    private void setCurrentPressure(int newPressurePSI) {
        gaugePressure.setPressure(newPressurePSI, 300, 0);

        String pressureBar = "";
        if (isUnitPSI) {
            pressureBar = String.format("%d", newPressurePSI);
        } else {
            pressureBar = String.format("%.2f", newPressurePSI * PSI_TO_BAR);
        }

        tvPressure.setText(pressureBar);
        tvCurrentPressure.setText(pressureBar);

        currentPressueInPSI = newPressurePSI;
    }

    @Override
    protected void onResume() {
        super.onResume();

        showSettings();

        // Check BLE Device changes
        String newDeviceAddr = appSettings.getBTDeviceAddr();

        // Still user didn't select Bluetooth device or user selected same device, no need reconnect
        if (TextUtils.isEmpty(newDeviceAddr) || newDeviceAddr.equalsIgnoreCase(bleDeviceAddress)) {
            return;
        }
        disconnectBLEDevice();

        // Switch to New device
        bleDeviceAddress = newDeviceAddr;

        Log.e("report", "Try to Connect To Device!");
        connectBLEDevice();
        timePrevStatusMilis = getCurrentTime();

        mBLESignalCheckerHandler.sendEmptyMessageDelayed(MSG_SIGNALCHECKER_PER_SEC, 1000);
        mBLESignalCheckerHandler.sendEmptyMessageDelayed(MSG_TIMECOUNTER_PER_SEC, 1000);
    }

    private void showSettings() {
        // Change Gauge Unit
        isUnitPSI = appSettings.isUnitPSI();

        if (isUnitPSI) {
            // 0 ~ 1000 psi
            ivGaugeUnit.setImageResource(R.drawable.ic_gaugeback_unit_psi);

            tvPressure.setText(String.valueOf(currentPressueInPSI));
            tvPressureUnit.setText("psi");

            tvCurrentPressure.setText(String.valueOf(currentPressueInPSI));
            tvCurrentPressureUnit.setText("psi");

            tvPressureSetPoint.setText(String.valueOf(seekPressure.getProgress()));
            tvPressureSetPointUnit.setText("psi");
        } else {
            // 0 ~ 68.94 bar
            ivGaugeUnit.setImageResource(R.drawable.ic_gaugeback_unit_bar);

            String pressureValue = String.format("%.2f", currentPressueInPSI * PSI_TO_BAR);
            tvPressure.setText(pressureValue);
            tvPressureUnit.setText("bar");

            tvCurrentPressure.setText(pressureValue);
            tvCurrentPressureUnit.setText("bar");

            float setPointBar = seekPressure.getProgress() * PSI_TO_BAR;
            tvPressureSetPoint.setText(String.format("%.2f", setPointBar));
            tvPressureSetPointUnit.setText("bar");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (appTimer != null) {
            appTimer.cancel();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {

        } else if(id == R.id.nav_videos) {

        } else if(id == R.id.nav_manuals) {

        } else if(id == R.id.nav_order) {

        } else if(id == R.id.nav_setting) {
            startActivity(new Intent(mContext, SettingActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // -----------------------------------------------------------------------------------------------
    private static long DEVICE_RECONNECT_INTERVAL = 15000;
    private static final int MSG_DEVICE_CONNECTION_CHECK = 1;

    private enum PLCConnected {False, Pending, True}

    String bleDeviceAddress = "";
    private String newline = "\r\n";

    private SerialSocket socket;
    private SerialService service;
    private boolean initialStart = true;
    private PLCConnected connected = PLCConnected.False;

    // -----------------------------------------------------------------------------------------------
    private static final int DEVICE_STATUS_IDLE = 0;
    private static final int DEVICE_STATUS_RUNNING = 1;
    private static final int DEVICE_STATUS_WARNNING = 2;
    private static final int DEVICE_STATUS_ALARM = 3;
    private static final int DEVICE_STATUS_RUNNINGWITHWARNNING = 4;

    private int currentDeviceStatus = DEVICE_STATUS_IDLE;
    private int prevDeviceStatus = currentDeviceStatus;

    int colorGreen = 0xff46c392;
    int colorOrange = 0xffffa300;
    int colorRed = 0xffff0000;

    Handler mBLEDeviceConnectionCheckHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity myActivity = weakReference.get();
            if (myActivity == null || myActivity.isFinishing()) {
                Log.e("report", "Died!");
                return;
            }

            if (msg.what == MSG_DEVICE_CONNECTION_CHECK) {
                if (connected == PLCConnected.False) {
                    // Try to connect with BLE
                    connect();
                }

                removeMessages(MSG_DEVICE_CONNECTION_CHECK);
                sendEmptyMessageDelayed(MSG_DEVICE_CONNECTION_CHECK, DEVICE_RECONNECT_INTERVAL);
            }
        }
    };

    private static final int MSG_SIGNALCHECKER_PER_SEC = 0;
    private static final int MSG_TIMECOUNTER_PER_SEC = 1;

    // Current time in milis
    private long timeCurrentMilis = 0;

    // Previous check time in milis
    private long timePrevStatusMilis = 0;

    private long elapsedSeconds = 0;

    private long getCurrentTime() {
        //return new Date().getTime();
        return System.currentTimeMillis();
    }

    private String getElapsedTimeMinutesSecondsString(long miliseconds) {
        long elapsedTime = miliseconds;
        String format = String.format("%%0%dd", 2);

        elapsedTime = elapsedTime / 1000;

        int hour = (int) (elapsedTime / 3600);
        int min = (int) ((elapsedTime - hour * 3600) / 60);
        int sec = (int) (elapsedTime - hour * 3600 - min * 60);
        String seconds = String.format(format, sec);
        String hours = String.format(format, hour);
        String minutes = String.format(format, min);
        return hours + ":" + minutes + ":" + seconds;
    }

    private String getCommand() {
        return String.format("%d,%d,%d,%d", seekPressure.getProgress(), 0, 0, 0);
    }

    Handler mBLESignalCheckerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity myActivity = weakReference.get();
            if (myActivity == null || myActivity.isFinishing()) {
                Log.e("report", "Died!");
                return;
            }

            if (msg.what == MSG_SIGNALCHECKER_PER_SEC) {
                if (connected == PLCConnected.True) {
                    send(getCommand());
                }

                // We call this to avoid message duplication when change the device.
                removeMessages(MSG_SIGNALCHECKER_PER_SEC);
                sendEmptyMessageDelayed(MSG_SIGNALCHECKER_PER_SEC, 3000);
            } else if (msg.what == MSG_TIMECOUNTER_PER_SEC) {

                timeCurrentMilis = getCurrentTime();

                // Check Valid Status for Timer
                long timeGap = (timeCurrentMilis > timePrevStatusMilis ? timeCurrentMilis - timePrevStatusMilis : 0);

                timePrevStatusMilis = timeCurrentMilis;

                // Repeat counting
                removeMessages(MSG_TIMECOUNTER_PER_SEC);
                sendEmptyMessageDelayed(MSG_TIMECOUNTER_PER_SEC, 1000);

                // Counting the Status Timer
                elapsedSeconds += timeGap;

                // Refresh InCycle Time
                tvElapsedTime.setText(getElapsedTimeMinutesSecondsString(elapsedSeconds));

                if (currentDeviceStatus != prevDeviceStatus) {
                    // To do list
                    // Record the status changes

                    // Change current device status value and Switch Animation
                    if (currentDeviceStatus == DEVICE_STATUS_RUNNING) {
                        tvStatus.setText("RUNNING");
                        tvStatus.setTextColor(colorGreen);
                    } else if (currentDeviceStatus == DEVICE_STATUS_IDLE) {

                        tvStatus.setText("IDLE");
                        tvStatus.setTextColor(colorRed);
                    } else if (currentDeviceStatus == DEVICE_STATUS_ALARM) {
                        tvStatus.setText("ALRAM");
                        tvStatus.setTextColor(colorOrange);
                    } else if (currentDeviceStatus == DEVICE_STATUS_WARNNING) {
                        tvStatus.setText("ALRAM");
                        tvStatus.setTextColor(colorOrange);
                    } else if (currentDeviceStatus == DEVICE_STATUS_RUNNINGWITHWARNNING) {
                        tvStatus.setText("RUNNING With WARN");
                        tvStatus.setTextColor(colorRed);
                    }

                    prevDeviceStatus = currentDeviceStatus;
                }
            }
        }
    };

    private void connectBLEDevice() {
        Intent intent = new Intent(mContext, SerialService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void disconnectBLEDevice() {
        if (connected != PLCConnected.False)
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
                downloadNewVersion();
            } else {
                showAlert("Need permissions to download new version");
            }
        }
    }

    //----------------------------------- BLE Device Settings -----------------------------------------
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        service = ((SerialService.SerialBinder) iBinder).getService();
        service.attach(this);

        if (initialStart) {
            initialStart = false;
            runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
    }

    @Override
    public void onSerialConnect() {
        status("connected");
        connected = PLCConnected.True;

        currentDeviceStatus = DEVICE_STATUS_IDLE;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToastMessage("Connected!");
            }
        });


        txtMachineStatus.setText("PLC Connected");
        ivStatus.setImageResource(R.drawable.gradient_circle_on);
    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("connection failed: " + e.getMessage());
        disconnect();

        // PLC Disconnected status
        txtMachineStatus.setText("PLC Disconnected");
        ivStatus.setImageResource(R.drawable.gradient_circle_off);
    }

    @Override
    public void onSerialRead(byte[] data) {
        receive(data);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("connection lost: " + e.getMessage());

        disconnect();

        // PLC Disconnected status
        txtMachineStatus.setText("PLC Disconnected");
        ivStatus.setImageResource(R.drawable.gradient_circle_off);

        // Reset Downtime reason status
        currentDeviceStatus = DEVICE_STATUS_IDLE;
    }

    private void connect() {

        if (TextUtils.isEmpty(bleDeviceAddress)) {
            return;
        }

        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(bleDeviceAddress);
            String deviceName = device.getName() != null ? device.getName() : device.getAddress();
            status("connecting...");
            connected = PLCConnected.Pending;

            socket = new SerialSocket();
            service.connect(this, "Connected to " + deviceName);
            socket.connect(mContext, service, device);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private void disconnect() {
        if (connected != PLCConnected.False) {
            connected = PLCConnected.False;
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
        if (connected != PLCConnected.True) {
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

        String[] plcValues = receivedString.split(",");

        if (plcValues != null && plcValues.length > 2) {
            String stateString = plcValues[0].replaceAll("\\D+","");
            String valueString = plcValues[1].replaceAll("\\D+","");

            if ("0".equals(stateString)) {
                currentDeviceStatus = DEVICE_STATUS_IDLE;
            } else if ("1".equals(stateString)) {
                currentDeviceStatus = DEVICE_STATUS_RUNNING;
            } else if ("2".equals(stateString)) {
                currentDeviceStatus = DEVICE_STATUS_WARNNING;
            } else if ("3".equals(stateString)) {
                currentDeviceStatus = DEVICE_STATUS_ALARM;
            } else if ("12".equals(stateString)) {
                currentDeviceStatus = DEVICE_STATUS_RUNNINGWITHWARNNING;
            }

            int valuePressure = 0;
            try{
                valuePressure = Integer.parseInt(valueString);
            } catch (Exception e){
                e.printStackTrace();
            }

            // Update Current Pressure
            setCurrentPressure(valuePressure);
        }

        //receiveText.append(new String(data));
    }

    private void status(String str) {
        SpannableStringBuilder spn = new SpannableStringBuilder(str + '\n');
        spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorStatusText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //receiveText.append(spn);
    }
}
