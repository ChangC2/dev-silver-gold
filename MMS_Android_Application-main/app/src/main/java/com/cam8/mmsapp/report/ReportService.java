package com.cam8.mmsapp.report;

import static com.cam8.mmsapp.report.DBConnInfo.CONNECTION_URL;
import static com.cam8.mmsapp.report.DBConnInfo.DB_PASS;
import static com.cam8.mmsapp.report.DBConnInfo.DB_USER;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cam8.mmsapp.AppSettings;
import com.cam8.mmsapp.MainActivity;
import com.cam8.mmsapp.MyApplication;
import com.cam8.mmsapp.R;
import com.cam8.mmsapp.alarm.AlarmSettings;
import com.cam8.mmsapp.model.DatabaseHelper;
import com.cam8.mmsapp.model.GanttDataModel;
import com.cam8.mmsapp.model.RegimeData;
import com.cam8.mmsapp.model.RegimeUtils;
import com.cam8.mmsapp.model.ShiftDataModel;
import com.cam8.mmsapp.model.TemperatureDataModel;
import com.cam8.mmsapp.network.GoogleCertProvider;
import com.cam8.mmsapp.network.ITSRestClient;
import com.cam8.mmsapp.utils.DateUtil;
import com.cam8.mmsapp.utils.NetUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import cz.msebera.android.httpclient.Header;

public class ReportService extends Service {

    public static final String CHANNEL_ID = "com.cam8.mmsapp.reportservice";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Stop Update Progress
        if (progressUpdateHandler != null) {
            progressUpdateHandler.removeCallbacks(progressUpdateRunnable);
            progressUpdateHandler = null;
        }

        if (reportThread != null) {
            reportThread.interrupt();
            reportThread = null;
        }

        if (tempDataThread != null) {
            tempDataThread.interrupt();
            tempDataThread = null;
        }

        if (shiftDataThread != null) {
            shiftDataThread.interrupt();
            shiftDataThread = null;
        }

        if (dbHelper != null) {
            dbHelper.close();
        }

        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        myApp = (MyApplication) getApplication();
        context = ReportService.this;
        dbHelper = new DatabaseHelper(context);
        appSettings = new AppSettings(context);

        /* Handler Mode, but it have many loads to run
        progressUpdateHandler = new Handler();
        progressUpdateHandler.postDelayed(progressUpdateRunnable, 1000);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        */

        regimeData = new RegimeData();

        // Run Gantt & Regime data thread
        reportThread = new Thread(progressUpdateRunnable);
        reportThread.start();

        // Run Temp data thread
        tempDataThread = new Thread(tempDataUploadRunnable);
        tempDataThread.start();

        // Run Shift data thread
        shiftDataThread = new Thread(shiftDataUploadRunnable);
        shiftDataThread.start();

        return START_STICKY;
    }

    private static final int NOTIF_ID = 1234;
    private void showNotification() {

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("MMS")
                .setContentText("MMS is running report data")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIF_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private final String LOG_TAG = "NotificationService";

    MyApplication myApp;
    Context context;
    AppSettings appSettings;
    DatabaseHelper dbHelper;
    Handler progressUpdateHandler;

    Thread reportThread;
    Thread tempDataThread;
    Thread shiftDataThread;

    RegimeData regimeData;

    long lastReportTime = 0;
    int cntReportSuccess = 0;

    private static final long STATUS_UPDATE_INTERVAL_FAST = 1000 * 5;           // 5 seconds
    private static final long STATUS_UPDATE_INTERVAL_MEDIUM = 1000 * 60;        // 1 min
    private static final long STATUS_UPDATE_INTERVAL_SLOW = 1000 * 60 * 5;      // 5 mins
    private static final long STATUS_UPDATE_INTERVAL_SHUTDOWN = 1000 * 60 * 10; // 10 mins

    private static final long GAUGE_UPDATE_INTERVAL = 1000 * 60;                // 60 seconds
    private static final long SHIFTDATA_UPDATE_INTERVAL = 1000 * 60;            // 60 seconds

    private long GANTT_UPLOAD_INTERVAL = STATUS_UPDATE_INTERVAL_FAST;

    Runnable progressUpdateRunnable = new Runnable() {
        @Override
        public void run() {

            /*if (progressUpdateHandler != null) {
                reportGanttData();
                progressUpdateHandler.postDelayed(this, 10000);
            }*/

            while (reportThread != null) {
                //reportGanttData();
                reportGanttDataApi();

                try {
                    Thread.sleep(GANTT_UPLOAD_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Runnable tempDataUploadRunnable = new Runnable() {
        @Override
        public void run() {

            while (tempDataThread != null) {

                uploadTemperatureData();

                try {
                    Thread.sleep(STATUS_UPDATE_INTERVAL_FAST);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Runnable shiftDataUploadRunnable = new Runnable() {
        @Override
        public void run() {

            while (shiftDataThread != null) {
                uploadShiftData();
                try {
                    Thread.sleep(SHIFTDATA_UPDATE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //private int sequence = 0;

    private void reportGanttData() {

        //Log.e("Alarm", "" + sequence++);

        String accountID = /*"test";*/appSettings.getCustomerID();
        String machineID = appSettings.getMachineName();

        if (TextUtils.isEmpty(accountID) || TextUtils.isEmpty(machineID))
            return;

        // Check App Valid Status, We don't send report.
        if (appSettings.isValidStatus() == false) {
            return;
        }

        // Get Data
        List<GanttDataModel> ganttDataModels = dbHelper.getAllGanttData();

        // If gantt is empty, then check Report time to reduce the db connection
        long currentTIme = System.currentTimeMillis();
        if (ganttDataModels.isEmpty() && (currentTIme - lastReportTime) < GAUGE_UPDATE_INTERVAL) {
            //Log.e("report", "No Gantt");
            return;
        }
        lastReportTime = currentTIme;

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            DriverManager.setLoginTimeout(10);

            String dbConnURL = CONNECTION_URL;
            String dbUser = DB_USER;
            String dbPass = DB_PASS;
            if (appSettings.isUsingLocalServer()) {
                dbConnURL = String.format("jdbc:mysql://%s:3309/slymms", appSettings.getLocalServerIP());
                dbUser = "root";
                dbPass = "root";
            }

            con = DriverManager.getConnection(dbConnURL, dbUser, dbPass);

            if (con == null) {
                Log.e("report", "DB conn Null!");

                // Send Server disconnect status
                sendServerConnectionStatus(false);
                return;
            }

            sendServerConnectionStatus(true);
            Log.e("report", "DB conn OK!!!");

            if (ganttDataModels.size() > 0) {

                for (int i = 0; i < ganttDataModels.size(); i++) {
                    GanttDataModel ganttData = ganttDataModels.get(i);

                    //Log.e("report", String.format("%s, %s, %s, %s, %s, %d, %d", ganttData.getColor(), ganttData.getMachineId(), ganttData.getOperator(), ganttData.getStatus(),
                    //        ganttData.getTimeStamp(), ganttData.getStart(), ganttData.getEnd()));
                    if (ganttData.getStart() == 0 || ganttData.getEnd() == 0) {
                        // Invalid Records
                        dbHelper.deleteGantData(ganttData);
                        continue;
                    }

                    String statementString = "";
                    PreparedStatement preparedStatement = null;
                    int records = 0;
                    boolean updateRecords = false;

                    if ("UPDATE_STATUS".equalsIgnoreCase(ganttData.getCreatedAt())) {
                        // Need to change status
                        statementString = String.format("update %s_ganttdata set status=?, color=?, comment=? where start>=? and end<=? and machine_id=?", accountID);
                        preparedStatement = con.prepareStatement(statementString);

                        preparedStatement.setString(1, ganttData.getStatus());
                        preparedStatement.setString(2, ganttData.getColor());
                        preparedStatement.setString(3, "update");
                        preparedStatement.setLong(4, ganttData.getStart() / 1000);
                        preparedStatement.setLong(5, ganttData.getEnd() / 1000);
                        preparedStatement.setString(6, ganttData.getMachineId());

                        updateRecords = true;
                    } else {
                        // Add new Record
                        statementString = String.format("insert into %s_ganttdata (created_at, machine_id, Operator, status, color, start, end, time_stamp, time_stamp_ms, job_id, interface)"
                                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", accountID);
                        preparedStatement = con.prepareStatement(statementString);

                        preparedStatement.setString(1, ganttData.getCreatedAt());
                        preparedStatement.setString(2, ganttData.getMachineId());
                        preparedStatement.setString(3, ganttData.getOperator());
                        preparedStatement.setString(4, ganttData.getStatus());
                        preparedStatement.setString(5, ganttData.getColor());
                        preparedStatement.setLong(6, ganttData.getStart() / 1000);
                        preparedStatement.setLong(7, ganttData.getEnd() / 1000);
                        preparedStatement.setString(8, ganttData.getTimeStamp());
                        preparedStatement.setInt(9, ganttData.getTimeStampMs());
                        preparedStatement.setString(10, ganttData.getJobId());
                        preparedStatement.setString(11, String.format("MMS Interface Module(%d%%)", ganttData.getBattLev()));
                    }

                    try {
                        records = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        Log.e("report", e.getMessage());

                        // Incase of duplicate recordes, we think it is ok for now.
                        if ("23000".equals(e.getSQLState())) {
                            records = 1;
                        }
                    }

                    if (records > 0) {
                        dbHelper.deleteGantData(ganttData);
                        Log.e("report", "Upload Record OK!!!");
                    } else if (Math.abs(System.currentTimeMillis() - ganttData.getStart()) > 86400000) { // 1 Day Later data, then throw it
                        Log.e("report", "Old Data Throwing!!!");
                        dbHelper.deleteGantData(ganttData);
                    } else {
                        Log.e("report", "Upload Record Error");
                    }
                }
            } else {
                Log.e("report", "No Gantt");
            }

            ///*
            //------- Calculate Regimes ---------
            // Check Reset Status
            String currentTimeString = DateUtil.getDayTimeStringInUniformat();
            String lastRecordTime = appSettings.getLastTime();

            if (myApp.isAppRunningStatus() == false &&
                    !currentTimeString.equals(lastRecordTime)) {
                // Report previous date
                appSettings.setTimeUnCat(0);
                appSettings.setTimeInCycle(0);
                appSettings.setTimeIdle1(0);
                appSettings.setTimeIdle2(0);
                appSettings.setTimeIdle3(0);
                appSettings.setTimeIdle4(0);
                appSettings.setTimeIdle5(0);
                appSettings.setTimeIdle6(0);
                appSettings.setTimeIdle7(0);
                appSettings.setTimeIdle8(0);

                appSettings.setBadParts(0);
                appSettings.setGoodParts(0);

                appSettings.setLastTime(currentTimeString);

            }

            // Calculate Regime Data
            RegimeUtils.calcRegimes(appSettings, regimeData, true);

            String statementString = String.format("update %s_hst set Utilization=?, inCycle=?, uncat=?, offline=?, r1t=?, r2t=?, " +
                    "r3t=?, r4t=?, r5t=?, r6t=?, r7t=?, r8t=?, oee=?, availability=?, quality=?, performance=?, goodParts=?, badParts=?, square_inch=? where `date`=? and `machine_id`=?", accountID);
            PreparedStatement preparedStatement = con.prepareStatement(statementString);

            preparedStatement.setInt(1, (int) regimeData.utilization);
            preparedStatement.setInt(2, (int) regimeData.inCycleTime);
            preparedStatement.setInt(3, (int) (regimeData.unCatTime));
            preparedStatement.setInt(4, (int) (regimeData.offlineT));
            preparedStatement.setInt(5, (int) (regimeData.idle1Time));
            preparedStatement.setInt(6, (int) (regimeData.idle2Time));
            preparedStatement.setInt(7, (int) (regimeData.idle3Time));
            preparedStatement.setInt(8, (int) (regimeData.idle4Time));
            preparedStatement.setInt(9, (int) (regimeData.idle5Time));
            preparedStatement.setInt(10, (int) (regimeData.idle6Time));
            preparedStatement.setInt(11, (int) (regimeData.idle7Time));
            preparedStatement.setInt(12, (int) (regimeData.idle8Time));
            preparedStatement.setInt(13, (int) regimeData.oee);
            preparedStatement.setInt(14, (int) regimeData.Availablity);
            preparedStatement.setInt(15, (int) regimeData.quality);
            preparedStatement.setInt(16, (int) regimeData.performance);
            preparedStatement.setInt(17, appSettings.getGoodParts());
            preparedStatement.setInt(18, appSettings.getBadParts());
            preparedStatement.setFloat(19, regimeData.squareInch);
            preparedStatement.setString(20, currentTimeString);
            preparedStatement.setString(21, machineID);

            int records = 0;
            try {
                records = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Log.e("report", e.getMessage());
            }

            if (records == 0) {
                statementString = String.format("insert into `%s_hst` (`Utilization`, `inCycle`, `uncat`, `offline`, `r1t`, `r2t`, " +
                        "`r3t`, `r4t`, `r5t`, `r6t`, `r7t`, `r8t`, `oee`, `availability`, `quality`, `performance`, `date`, `machine_id`, `goodParts`, `badParts`, `square_inch`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", accountID);
                preparedStatement = con.prepareStatement(statementString);

                preparedStatement.setInt(1, (int) regimeData.utilization);
                preparedStatement.setInt(2, (int) regimeData.inCycleTime);
                preparedStatement.setInt(3, (int) (regimeData.unCatTime));
                preparedStatement.setInt(4, (int) (regimeData.offlineT));
                preparedStatement.setInt(5, (int) (regimeData.idle1Time));
                preparedStatement.setInt(6, (int) (regimeData.idle2Time));
                preparedStatement.setInt(7, (int) (regimeData.idle3Time));
                preparedStatement.setInt(8, (int) (regimeData.idle4Time));
                preparedStatement.setInt(9, (int) (regimeData.idle5Time));
                preparedStatement.setInt(10, (int) (regimeData.idle6Time));
                preparedStatement.setInt(11, (int) (regimeData.idle7Time));
                preparedStatement.setInt(12, (int) (regimeData.idle8Time));
                preparedStatement.setInt(13, (int) regimeData.oee);
                preparedStatement.setInt(14, (int) regimeData.Availablity);
                preparedStatement.setInt(15, (int) regimeData.quality);
                preparedStatement.setInt(16, (int) regimeData.performance);
                preparedStatement.setString(17, currentTimeString);
                preparedStatement.setString(18, machineID);
                preparedStatement.setInt(19, appSettings.getGoodParts());
                preparedStatement.setInt(20, appSettings.getBadParts());
                preparedStatement.setFloat(21, regimeData.squareInch);

                try {
                    records = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    Log.e("report", e.getMessage());
                }

                if (records > 0) {
                    Log.e("report", "Added new record");
                }
            } else {
                Log.e("report", "Updated regime");
            }

            String qtyCompleted = appSettings.getQtyCompleted();
            String jobId = appSettings.getJobId();
            if (!TextUtils.isEmpty(qtyCompleted) && !TextUtils.isEmpty(jobId)) {
                String[] partsValue = qtyCompleted.split(",");
                if (partsValue.length ==2) {
                    int qtyGoodCompleted = Integer.parseInt(partsValue[0]);
                    int qtyBadCompleted = Integer.parseInt(partsValue[1]);

                    statementString = String.format("update %s_jobdata set qtyGoodCompleted=?, qtyBadCompleted=? where jobID=?", accountID);
                    preparedStatement = con.prepareStatement(statementString);

                    preparedStatement.setInt(1, qtyGoodCompleted);
                    preparedStatement.setInt(2, qtyBadCompleted);
                    preparedStatement.setString(3, jobId);

                    try {
                        records = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        Log.e("report", e.getMessage());
                    }

                    if (records > 0) {
                        // Update Success
                        Log.e("report", "Added QTY Completed");
                    }

                    appSettings.setQtyCompleted("");
                }
            }
            //*/

            // Close the connection
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("report", e.getMessage());

            sendServerConnectionStatus(false);
        }
    }

    private void reportGanttDataApi() {

        //Log.e("Alarm", "" + sequence++);

        String accountID = appSettings.getCustomerID();
        String machineID = appSettings.getMachineName();

        if (TextUtils.isEmpty(accountID) || TextUtils.isEmpty(machineID))
            return;

        // Check App Valid Status, We don't send report.
        if (appSettings.isValidStatus() == false) {
            return;
        }

        // Get Data
        List<GanttDataModel> ganttDataModels = dbHelper.getAllGanttData();

        // If gantt is empty, then check Report time to reduce the db connection
        long currentTIme = System.currentTimeMillis();
        if (ganttDataModels.isEmpty() && (currentTIme - lastReportTime) < GAUGE_UPDATE_INTERVAL) {
            //Log.e("report", "No Gantt");
            return;
        }

        // Check network status
        if (!NetUtil.isInternetAvailable(context)) {
            Log.e("MMSNet", "Offline!");
            sendServerConnectionStatus(false);
            return;
        }

        try {
            if (ganttDataModels.size() > 0) {

                cntReportSuccess = 0;

                final CountDownLatch countDownLatch = new CountDownLatch(ganttDataModels.size());

                for (int i = 0; i < ganttDataModels.size(); i++) {
                    GanttDataModel ganttData = ganttDataModels.get(i);

                    //Log.e("report", String.format("%s, %s, %s, %s, %s, %d, %d", ganttData.getColor(), ganttData.getMachineId(), ganttData.getOperator(), ganttData.getStatus(),
                    //        ganttData.getTimeStamp(), ganttData.getStart(), ganttData.getEnd()));
                    if (ganttData.getStart() == 0 || ganttData.getEnd() == 0) {
                        // Invalid Records
                        dbHelper.deleteGantData(ganttData);
                        countDownLatch.countDown();
                        continue;
                    }

                    // 1 Day Later data, then throw it
                    if (Math.abs(System.currentTimeMillis() - ganttData.getStart()) > 86400000) {
                        Log.e("report", "Old Data Throwing!!!");
                        dbHelper.deleteGantData(ganttData);
                        countDownLatch.countDown();
                        continue;
                    }

                    RequestParams requestParams = new RequestParams();

                    if ("UPDATE_STATUS".equalsIgnoreCase(ganttData.getCreatedAt())) {
                        // Need to change status
                        //statementString = String.format("update %s_ganttdata set status=?, color=?, comment=? where start>=? and end<=? and machine_id=?", accountID);

                        requestParams.put("action", 1); // Means that this is update action, not insert new record.
                        requestParams.put("customer_id", accountID);
                        requestParams.put("machine_id", ganttData.getMachineId());

                        requestParams.put("status", ganttData.getStatus());
                        requestParams.put("color", ganttData.getColor());
                        requestParams.put("comment", "update");

                        requestParams.put("start", ganttData.getStart() / 1000);
                        requestParams.put("end", ganttData.getEnd() / 1000);
                    } else {
                        // Add new Record
                        //statementString = String.format("insert into %s_ganttdata (created_at, machine_id, Operator, status, color, start, end, time_stamp, time_stamp_ms, job_id, interface)"
                        //        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", accountID);

                        requestParams.put("action", 0); // Means that this is for insertting new record.
                        requestParams.put("customer_id", accountID);
                        requestParams.put("machine_id", ganttData.getMachineId());

                        requestParams.put("created_at", ganttData.getCreatedAt());
                        requestParams.put("Operator", ganttData.getOperator());
                        requestParams.put("status", ganttData.getStatus());
                        requestParams.put("color", ganttData.getColor());

                        requestParams.put("start", ganttData.getStart() / 1000);
                        requestParams.put("end", ganttData.getEnd() / 1000);

                        requestParams.put("time_stamp", ganttData.getTimeStamp());
                        requestParams.put("time_stamp_ms", ganttData.getTimeStampMs());
                        requestParams.put("job_id", ganttData.getJobId());
                        requestParams.put("interface", String.format("MMS Interface Module(%d%%)", ganttData.getBattLev()));
                    }

                    GoogleCertProvider.install(context);
                    ITSRestClient.postSync(context, "postGanttData", requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            Log.e("postGantt", response.toString());

                            cntReportSuccess++;
                            dbHelper.deleteGantData(ganttData);
                            Log.e("report", "Upload Record OK!!!");

                            /*try {
                                if (response.has("status") && response.getBoolean("status")) {
                                } else {
                                    String message = "";
                                    if (response.has("message") && !response.isNull("message")) {
                                        message = response.getString("message");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/

                            countDownLatch.countDown();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);

                            String errorMsg = throwable.getMessage();
                            if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                                errorMsg = getString(R.string.error_connection_timeout);
                            }

                            //showAlert(errorMsg);
                            Log.e("pNetErr1", errorMsg);

                            countDownLatch.countDown();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);

                            String errorMsg = throwable.getMessage();
                            if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                                errorMsg = getString(R.string.error_connection_timeout);
                            }

                            //showAlert(errorMsg);
                            Log.e("pNetErr1", errorMsg);

                            countDownLatch.countDown();
                        }
                    });
                }

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (cntReportSuccess > 0) {
                    sendServerConnectionStatus(true);

                    GANTT_UPLOAD_INTERVAL = STATUS_UPDATE_INTERVAL_FAST;
                } else {
                    sendServerConnectionStatus(false);

                    if (GANTT_UPLOAD_INTERVAL == STATUS_UPDATE_INTERVAL_FAST) {
                        GANTT_UPLOAD_INTERVAL = STATUS_UPDATE_INTERVAL_MEDIUM;
                    } else if (GANTT_UPLOAD_INTERVAL == STATUS_UPDATE_INTERVAL_MEDIUM) {
                        GANTT_UPLOAD_INTERVAL = STATUS_UPDATE_INTERVAL_SLOW;
                    } else if (GANTT_UPLOAD_INTERVAL == STATUS_UPDATE_INTERVAL_SLOW) {
                        GANTT_UPLOAD_INTERVAL = STATUS_UPDATE_INTERVAL_SHUTDOWN;
                    }
                }
            } else {
                Log.e("report", "No Gantt");
            }

            ///*
            //------- Calculate Regimes ---------
            // Check Reset Status
            String currentTimeString = DateUtil.getDayTimeStringInUniformat();
            String lastRecordTime = appSettings.getLastTime();

            if (myApp.isAppRunningStatus() == false &&
                    !currentTimeString.equals(lastRecordTime)) {
                // Report previous date
                appSettings.setTimeUnCat(0);
                appSettings.setTimeInCycle(0);
                appSettings.setTimeIdle1(0);
                appSettings.setTimeIdle2(0);
                appSettings.setTimeIdle3(0);
                appSettings.setTimeIdle4(0);
                appSettings.setTimeIdle5(0);
                appSettings.setTimeIdle6(0);
                appSettings.setTimeIdle7(0);
                appSettings.setTimeIdle8(0);

                appSettings.setBadParts(0);
                appSettings.setGoodParts(0);

                appSettings.setLastTime(currentTimeString);

            }

            // Check HST report time
            if ((currentTIme - lastReportTime) > GAUGE_UPDATE_INTERVAL) {
                // Calculate Regime Data and upload to HST ---------------------------------------------
                RegimeUtils.calcRegimes(appSettings, regimeData, true);

                //String statementString = String.format("update %s_hst set Utilization=?, inCycle=?, uncat=?, offline=?, r1t=?, r2t=?, " +
                //        "r3t=?, r4t=?, r5t=?, r6t=?, r7t=?, r8t=?, oee=?, availability=?, quality=?, performance=?, goodParts=?, badParts=?, square_inch=? where `date`=? and `machine_id`=?", accountID);

                RequestParams requestParams = new RequestParams();
                requestParams.put("customer_id", accountID);
                requestParams.put("date", currentTimeString);
                requestParams.put("machine_id", machineID);

                requestParams.put("Utilization", (int) regimeData.utilization);
                requestParams.put("inCycle", regimeData.inCycleTime);
                requestParams.put("uncat", regimeData.unCatTime);
                requestParams.put("offline", regimeData.offlineT);
                requestParams.put("r1t", regimeData.idle1Time);
                requestParams.put("r2t", regimeData.idle2Time);
                requestParams.put("r3t", regimeData.idle3Time);
                requestParams.put("r4t", regimeData.idle4Time);
                requestParams.put("r5t", regimeData.idle5Time);
                requestParams.put("r6t", regimeData.idle6Time);
                requestParams.put("r7t", regimeData.idle7Time);
                requestParams.put("r8t", regimeData.idle8Time);
                requestParams.put("oee", (int) regimeData.oee);
                requestParams.put("availability", (int) regimeData.Availablity);
                requestParams.put("quality", (int) regimeData.quality);
                requestParams.put("performance", (int) regimeData.performance);
                requestParams.put("goodParts", appSettings.getGoodParts());
                requestParams.put("badParts", appSettings.getBadParts());
                requestParams.put("square_inch", regimeData.squareInch);

                GoogleCertProvider.install(context);
                ITSRestClient.postSync(context, "postHSTData", requestParams, hstDataListener);

                // Upload JobData Completed Count ------------------------------------------------------
                String qtyCompleted = appSettings.getQtyCompleted();
                String jobId = appSettings.getJobId();
                if (!TextUtils.isEmpty(qtyCompleted) && !TextUtils.isEmpty(jobId)) {
                    String[] partsValue = qtyCompleted.split(",");
                    if (partsValue.length == 2) {
                        int qtyGoodCompleted = Integer.parseInt(partsValue[0]);
                        int qtyBadCompleted = Integer.parseInt(partsValue[1]);

                        //statementString = String.format("update %s_jobdata set qtyGoodCompleted=?, qtyBadCompleted=? where jobID=?", accountID);
                        requestParams = new RequestParams();
                        requestParams.put("customer_id", accountID);
                        requestParams.put("qtyGoodCompleted", qtyGoodCompleted);
                        requestParams.put("qtyBadCompleted", qtyBadCompleted);
                        requestParams.put("jobID", jobId);

                        GoogleCertProvider.install(context);
                        ITSRestClient.postSync(context, "postJobDataCompletedParts", requestParams, jobCompletedCountsDataListener);
                    }
                }

                // Save the last report time
                lastReportTime = currentTIme;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("report", e.getMessage());

            sendServerConnectionStatus(false);
        }
    }

    // send server disconnect status
    private void sendServerConnectionStatus(boolean status) {
        // New version is available
        Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
        intent.putExtra("STATUS", AlarmSettings.STATUS_SERVER_CONN_STATUS);
        intent.putExtra("SERVER_CONNECTED", status);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    JsonHttpResponseHandler hstDataListener = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

            // If the response is JSONObject instead of expected JSONArray
            Log.e("postHSTData", response.toString());

            try {
                if (response.has("status") && response.getBoolean("status")) {
                    Log.e("report", "Added QTY Completed");
                } else {
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
            Log.e("NetErrPostHST", errorMsg);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            String errorMsg = throwable.getMessage();
            if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                errorMsg = getString(R.string.error_connection_timeout);
            }

            //showAlert(errorMsg);
            Log.e("NetErrPostHST", errorMsg);
        }
    };

    JsonHttpResponseHandler jobCompletedCountsDataListener = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

            // If the response is JSONObject instead of expected JSONArray
            Log.e("postJobCompletedCount", response.toString());

            appSettings.setQtyCompleted("");

            try {
                if (response.has("status") && response.getBoolean("status")) {
                    Log.e("report", "Added QTY Completed");
                } else {
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
            Log.e("NetErrPostJobC", errorMsg);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            String errorMsg = throwable.getMessage();
            if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                errorMsg = getString(R.string.error_connection_timeout);
            }

            //showAlert(errorMsg);
            Log.e("NetErrPostJobC", errorMsg);
        }
    };

    private void uploadTemperatureData() {

        // Get Data
        List<TemperatureDataModel> tempDataModels = dbHelper.getAllTempData();
        if (tempDataModels.size() > 5) {
            tempDataModels = tempDataModels.subList(0, 5);
        }

        if (tempDataModels.size() > 0) {

            if (!NetUtil.isInternetAvailable(context)) {
                Log.e("MMSNet", "Offline!");
                return;
            }

            final CountDownLatch countDownLatch = new CountDownLatch(tempDataModels.size());
            for (int i = 0; i < tempDataModels.size(); i++) {

                TemperatureDataModel tempData = tempDataModels.get(i);

                RequestParams requestParams = new RequestParams();
                requestParams.put("customer_id", appSettings.getCustomerID());

                requestParams.put("created_at", tempData.getDate());
                requestParams.put("timestamp", tempData.getTimeStamp());
                requestParams.put("part_id", tempData.getPartId());
                requestParams.put("date", tempData.getDate());
                requestParams.put("time", tempData.getTime());

                requestParams.put("ttime1", tempData.getTtime1());
                requestParams.put("ttemp1", tempData.getTtemp1());
                requestParams.put("ttime2", tempData.getTtime2());
                requestParams.put("ttemp2", tempData.getTtemp2());
                requestParams.put("ttime3", tempData.getTtime3());
                requestParams.put("ttemp3", tempData.getTtemp3());
                requestParams.put("ttime4", tempData.getTtime4());
                requestParams.put("ttemp4", tempData.getTtemp4());
                requestParams.put("ttime5", tempData.getTtime5());
                requestParams.put("ttemp5", tempData.getTtemp5());
                requestParams.put("ttime6", tempData.getTtime6());
                requestParams.put("ttemp6", tempData.getTtemp6());
                requestParams.put("ttime7", tempData.getTtime7());
                requestParams.put("ttemp7", tempData.getTtemp7());
                requestParams.put("ttime8", tempData.getTtime8());
                requestParams.put("ttemp8", tempData.getTtemp8());

                // Extra field
                requestParams.put("toven", tempData.gettOven());
                requestParams.put("operator", appSettings.getUserName());

                GoogleCertProvider.install(context);
                ITSRestClient.postSync(context, "postTankTime", requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        Log.e("tankTime", response.toString());

                        try {
                            if (response.has("status") && response.getBoolean("status")) {
                                dbHelper.deleteTempData(tempData);
                            } else {
                                String message = "";
                                if (response.has("message") && !response.isNull("message")) {
                                    message = response.getString("message");
                                }

                                if (TextUtils.isEmpty(message)) {
                                    message = "No Temperature Data!";
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        countDownLatch.countDown();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        String errorMsg = throwable.getMessage();
                        if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                            errorMsg = getString(R.string.error_connection_timeout);
                        }

                        //showAlert(errorMsg);
                        Log.e("NetErr1", errorMsg);

                        countDownLatch.countDown();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                        String errorMsg = throwable.getMessage();
                        if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                            errorMsg = getString(R.string.error_connection_timeout);
                        }

                        //showAlert(errorMsg);
                        Log.e("NetErr1", errorMsg);

                        countDownLatch.countDown();
                    }
                });
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadShiftData() {
        // Get Data
        List<ShiftDataModel> shiftDataList = dbHelper.getUpdatedShiftDataList();
        if (shiftDataList.size() > 5) {
            shiftDataList = shiftDataList.subList(0, 5);
        }

        if (shiftDataList.size() > 0) {

            // Check network status
            if (!NetUtil.isInternetAvailable(context)) {
                Log.e("MMSNet", "Offline!");
                return;
            }

            final CountDownLatch countDownLatch = new CountDownLatch(shiftDataList.size());
            for (int i = 0; i < shiftDataList.size(); i++) {

                final ShiftDataModel shiftData = shiftDataList.get(i);
                if (shiftData.getPartIds() != null) {
                    Log.e("shiftData", "partid:" + shiftData.getPartIds());
                } else {
                    Log.e("shiftData", "partid: NULL");
                }

                RequestParams requestParams = new RequestParams();
                requestParams.put("customer_id", appSettings.getCustomerID());

                requestParams.put("shift_id", shiftData.getShiftId());

                requestParams.put("jobId", shiftData.getJobID());
                requestParams.put("sequenceNo", shiftData.getJobSequenceNo());

                requestParams.put("machine", shiftData.getMachine());
                requestParams.put("operator", shiftData.getOperator());
                requestParams.put("userid", shiftData.getUserID());

                requestParams.put("date", DateUtil.toStringFormat_12(new Date(shiftData.getStartTime())));

                requestParams.put("startTime", shiftData.getStartTime());
                requestParams.put("stopTime", shiftData.getStopTime());

                requestParams.put("oee", shiftData.getOee());
                requestParams.put("availability", shiftData.getAvailablity());
                requestParams.put("quality", shiftData.getQuality());
                requestParams.put("performance", shiftData.getPerformance());

                requestParams.put("goodParts", shiftData.getGoodParts());
                requestParams.put("badParts", shiftData.getBadParts());

                requestParams.put("uncat", shiftData.getElapsedTimeInMils(0));
                requestParams.put("inCycle", shiftData.getElapsedTimeInMils(1));
                requestParams.put("offline", shiftData.getOffLineT());
                requestParams.put("r1t", shiftData.getElapsedTimeInMils(2));
                requestParams.put("r2t", shiftData.getElapsedTimeInMils(3));
                requestParams.put("r3t", shiftData.getElapsedTimeInMils(4));
                requestParams.put("r4t", shiftData.getElapsedTimeInMils(5));
                requestParams.put("r5t", shiftData.getElapsedTimeInMils(6));
                requestParams.put("r6t", shiftData.getElapsedTimeInMils(7));
                requestParams.put("r7t", shiftData.getElapsedTimeInMils(8));
                requestParams.put("r8t", shiftData.getElapsedTimeInMils(9));

                requestParams.put("aux1data", shiftData.getAuxData1());
                requestParams.put("aux2data", shiftData.getAuxData2());
                requestParams.put("aux3data", shiftData.getAuxData3());

                requestParams.put("rework", shiftData.getStatusRework());
                requestParams.put("setup", shiftData.getStatusSetup());

                requestParams.put("partId", shiftData.getPartIds());

                requestParams.put("shiftTime", shiftData.getShiftSetting());
                requestParams.put("targetCycleTime", shiftData.getTargetCycleTimeSeconds() * 1000); // Convert to Miliseconds.
                requestParams.put("plannedProductionTime", shiftData.getPlannedProductionTime());   // Already used Miliseconds unit.

                GoogleCertProvider.install(context);
                ITSRestClient.postSync(context, "postShiftData", requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        Log.e("shiftData", response.toString());

                        try {
                            if (response.has("status") && response.getBoolean("status")) {

                                if (shiftData.isCompleted()) {
                                    // This data was completed, remove record in the db.
                                    Log.e("shiftData", "Completed shift, remove record!");
                                    dbHelper.deleteShiftData(shiftData);
                                } else {
                                    // This is new Shift Data in server and got new shiftID and update in local db.
                                    if (shiftData.getShiftId() == 0) {
                                        Log.e("shiftData", "New shift data, refresh shift ID.");

                                        // User Information is exist
                                        int shiftId = response.optInt("shift_id");
                                        if (shiftId > 0) {
                                            shiftData.setShiftId(shiftId);
                                        }
                                    }

                                    // Update Shift Data Status
                                    dbHelper.updateShiftStatusAsProcessed(shiftData);
                                }
                            } else {
                                String message = "";
                                if (response.has("message") && !response.isNull("message")) {
                                    message = response.getString("message");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        countDownLatch.countDown();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        String errorMsg = throwable.getMessage();
                        if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                            errorMsg = getString(R.string.error_connection_timeout);
                        }

                        //showAlert(errorMsg);
                        Log.e("NetErr2", errorMsg);

                        countDownLatch.countDown();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                        String errorMsg = throwable.getMessage();
                        if (!TextUtils.isEmpty(errorMsg) && errorMsg.contains("time")) {
                            errorMsg = getString(R.string.error_connection_timeout);
                        }

                        //showAlert(errorMsg);
                        Log.e("NetErr2", errorMsg);

                        countDownLatch.countDown();
                    }
                });
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Get the Daily Goal Target Value
            if (appSettings.getChartOption() == 2) {
                RequestParams requestParams = new RequestParams();
                requestParams.put("factory_id", appSettings.getCustomerID());
                requestParams.put("machine_id", appSettings.getMachineName());
                requestParams.put("operator", appSettings.getUserName());
                requestParams.put("jobID", appSettings.getJobId());
                requestParams.put("date", DateUtil.toStringFormat_13(new Date()));

                GoogleCertProvider.install(context);
                ITSRestClient.post(context, "getMachineCalculation", requestParams, dgtDataListener);
            }
        }
    }

    JsonHttpResponseHandler dgtDataListener = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

            // If the response is JSONObject instead of expected JSONArray
            Log.e("MachineCalculation", response.toString());

            try {
                if (response.has("status") && response.getBoolean("status")) {

                    // User Information is exist
                    float dgtValue = (float)response.optDouble("data");
                    appSettings.setDGTValue(dgtValue);

                    // New version is available
                    Intent intent = new Intent(AlarmSettings.ACTION_VALID_STATUS_UPDATED);
                    intent.putExtra("STATUS", AlarmSettings.STATUS_NEW_DAILY_GOAL_TARGET);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } else {
                    /*String message = "";
                    if (response.has("message") && !response.isNull("message")) {
                        message = response.getString("message");
                    }

                    if (TextUtils.isEmpty(message)) {
                        message = getString(R.string.error_server_busy);
                    }

                    showToastMessage(message);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

}
