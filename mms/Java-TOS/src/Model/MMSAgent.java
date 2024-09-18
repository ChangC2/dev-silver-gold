package Model;

import Controller.Api;
import Database.*;
import Mail.MailSender;
import Utils.ChartColorUtils;
import Utils.DateTimeUtils;
import Utils.PreferenceManager;
import Utils.Utils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.PieChart;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import view.MainView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MMSAgent {
    private static MMSAgent instance;

    public static MMSAgent getInstance() {
        if (instance == null) {
            instance = new MMSAgent();
        }
        return instance;
    }

    Boolean isRunning = false;
    Thread reportThread;
    BooleanProperty inCycleSignalProperty;

    // Shift Data
    public ShiftDataModel shiftData = new ShiftDataModel();
    ShiftTime currShiftTimeInfo;
    private long timeLastShiftUpdateMilis = 0;

    public static final int DEVICE_STATUS_UNCATEGORIZED = 0;
    public static final int DEVICE_STATUS_INCYCLE = 1;
    public static final int DEVICE_STATUS_IDLE1 = 2;
    public static final int DEVICE_STATUS_IDLE2 = 3;
    public static final int DEVICE_STATUS_IDLE3 = 4;
    public static final int DEVICE_STATUS_IDLE4 = 5;
    public static final int DEVICE_STATUS_IDLE5 = 6;
    public static final int DEVICE_STATUS_IDLE6 = 7;
    public static final int DEVICE_STATUS_IDLE7 = 8;
    public static final int DEVICE_STATUS_IDLE8 = 9;

    public int opDownTimeStatus = DEVICE_STATUS_UNCATEGORIZED;
    public int currentDeviceStatus = DEVICE_STATUS_UNCATEGORIZED;
    public int prevDeviceStatus = currentDeviceStatus;

    public long timeCurrentMilis = 0;
    public long timePrevStatusMilis = 0;

    // Used for Uncategorized Status And Downtime Reason Status
    // In case of Uncate Status, when user click downtime reason,
    // then all prev uncate status is moved to new downtime reason status
    public long timeLastUncatStartTime = 0;

    // Report times
    private static final long INTERVAL_1_SECOND = 800;             // 800 miliseconds
    private static final long INTERVAL_3_MINUTES = 1000 * 60 * 3;   // 3 mins
    private long INTERVAL_REPORT = INTERVAL_1_SECOND;
    private long timeLastReportMilis = 0;

    public long[] elapsedMiliseconds = new long[10];
    public String[] titleDownTimeReason = new String[8];
    String[] colorCharts = ChartColorUtils.chartColorStrings;

    // CSLock Status, Cycle Start Lock. This is output 1 on the PLC. Cycle Start Lock
    public int statusCsLock = 0;
    public long elapsedStopTime = 0;

    // “Elapsed idle time” is the time since the machine stopped In Cycle
    public long elapsedIdleTime = 0;

    // Regime UI Update
    public RegimeData regimeData = new RegimeData();
    public long timeLastRegimeUpdateTime = 0;
    public static final long INTERVAL_REGIME_UPDATE = 1000 * 15;   // 15 secs

    public static final int INTERVAL_SIGNAL_CHECK = 1000;
    public Object timeLock = new Object();

    long elapsedStopTimeForPartsCounting = 0;
    boolean bOneTimeCounted = false;

    public void start() {

        // Set Online Status
        StatusReporter.getInstance().reportStatus("Online");

        if (isRunning == false) {

            inCycleSignalProperty = new SimpleBooleanProperty(MainView.getInstance().getInCycleStatus());
            inCycleSignalProperty.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (timeLock) {
                                processNewDeviceStatus(newValue);
                            }
                        }
                    });
                }
            });

            retrieveTimerStatus();

            timePrevStatusMilis = DateTimeUtils.getCurrentTime();
            timeLastReportMilis = timePrevStatusMilis;
            timeLastUncatStartTime = timePrevStatusMilis;
            timeLastShiftUpdateMilis = timePrevStatusMilis;

            // Update Downtime reasons
            loadDowntimeTitles();

            // Reset Shift Data
            resetShiftData(false);

            // Run Gantt & Regime data thread
            reportThread = new Thread(progressUpdateRunnable);
            reportThread.start();
        }

        isRunning = true;
    }

    public void stop() {

        if (reportThread != null) {
            reportThread.interrupt();
            reportThread = null;
        }

        isRunning = false;

        reportStatus();
        saveCurrentShiftData();

        // Set Offline Status
        StatusReporter.getInstance().reportStatus("Offline");

    }

    public void loadDowntimeTitles() {
        titleDownTimeReason[0] = PreferenceManager.getDownTimeReason1();
        titleDownTimeReason[1] = PreferenceManager.getDownTimeReason2();
        titleDownTimeReason[2] = PreferenceManager.getDownTimeReason3();
        titleDownTimeReason[3] = PreferenceManager.getDownTimeReason4();
        titleDownTimeReason[4] = PreferenceManager.getDownTimeReason5();
        titleDownTimeReason[5] = PreferenceManager.getDownTimeReason6();
        titleDownTimeReason[6] = PreferenceManager.getDownTimeReason7();
        titleDownTimeReason[7] = PreferenceManager.getDownTimeReason8();
    }

    Runnable progressUpdateRunnable = new Runnable() {
        @Override
        public void run() {

            while (isRunning) {

                inCycleSignalProperty.set(MainView.getInstance().getInCycleStatus());

                //- setFanucConnStatus();
                //- setServerConnStatus();

                // Lock Status
                synchronized (timeLock) {
                    timeCurrentMilis = DateTimeUtils.getCurrentTime();

                    // Check Valid Status for Timer
                    long timeGap = (timeCurrentMilis > timePrevStatusMilis ? timeCurrentMilis - timePrevStatusMilis : 0);
                    if (timeLastReportMilis > timeCurrentMilis) {
                        timeLastReportMilis = timeCurrentMilis;
                    }

                    if (timeGap > 3000) {
                        LogManager.getInstance().addNewLog("*** Exception Time Gap ***" + timeGap);
                    }

                    timePrevStatusMilis = timeCurrentMilis;

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

                    if (isNewShiftData) {
                        resetShiftData(true);

                        timeLastShiftUpdateMilis = timeCurrentMilis;

                        currShiftTimeInfo = newShiftInfo;
                    } else {
                        shiftData.setStopTime(timeCurrentMilis);
                        shiftData.setElapsedTimeInMils(currentDeviceStatus, shiftData.getElapsedTimeInMils(currentDeviceStatus) + timeGap);
                        if (timeCurrentMilis - timeLastShiftUpdateMilis > 20000) { // Every 20 seconds
                            shiftData.calcRegime();
                            DBTableShiftData.updateShiftData(shiftData);

                            timeLastShiftUpdateMilis = timeCurrentMilis;
                        }
                    }

                    // Refresh InCycle Time
                    long cycleSeconds = elapsedMiliseconds[DEVICE_STATUS_INCYCLE];
                    //- tvSyncTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(cycleSeconds));

                    // Refresh UnCate Time
                    long unCateSeconds = elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED];
                    //- tvUnCatTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(unCateSeconds));

                    // Calc total Idle time and show it.
                    int idleSeconds = 0;
                    for (int i = 0; i < 8; i++) {
                        idleSeconds += elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i];
                    }
                    //- tvIdleTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(idleSeconds));

                    // Down Time Reason Status
                    // Originally only used following for the current downtime reason, but it has occur when the reset, not changed the value
                    if (currentDeviceStatus >= DEVICE_STATUS_IDLE1) {
                        // Refresh Idle Time(Downtime Reason Time)
                        // Refresh only currently-selected DownTime Reason Status
                        int selectedDowntimeIndex = currentDeviceStatus - DEVICE_STATUS_IDLE1;
                        if (TextUtils.isEmpty(titleDownTimeReason[selectedDowntimeIndex])) {
                            //- btnDownTimeStatus[selectedDowntimeIndex].setDisable(true);

                            elapsedMiliseconds[currentDeviceStatus] = 0;
                            //- String idleStatusText = String.format("Disabled - Reason Not Defined\n%s", DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(elapsedMiliseconds[currentDeviceStatus]));
                            //- btnDownTimeStatus[selectedDowntimeIndex].setText(idleStatusText);
                        } else {
                            //- btnDownTimeStatus[selectedDowntimeIndex].setDisable(false);

                            //- String idleStatusText = String.format("%s\n%s", titleDownTimeReason[selectedDowntimeIndex], DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(elapsedMiliseconds[currentDeviceStatus]));
                            //- btnDownTimeStatus[selectedDowntimeIndex].setText(idleStatusText);
                        }
                    }

                    // Originally here we called
                    // saveTimeStatus();

                    if (currentDeviceStatus != prevDeviceStatus) {

                        // Record the status changes
                        reportStatus();

                        // Change current device status value and Switch Animation
                        //- anminBlinkSync.jumpTo(Duration.ZERO);
                        //- anminBlinkSync.stop();

                        //- anminBlinkIdle.jumpTo(Duration.ZERO);
                        //- anminBlinkIdle.stop();

                        //- anminBlinkUnCat.jumpTo(Duration.ZERO);
                        //- anminBlinkUnCat.stop();

                        if (currentDeviceStatus == DEVICE_STATUS_INCYCLE) {
                            //- anminBlinkSync.play();

                            //- tvMachineCateStatus.setText(PreferenceManager.isJobSetupStatus() ? "Setup - In Cycle" : "In Cycle");
                            //- tvMachineCateStatus.setTextFill(colorGreen);

                            // Reset Last Uncate start time
                            timeLastUncatStartTime = 0;
                        } else if (currentDeviceStatus == DEVICE_STATUS_UNCATEGORIZED) {
                            //- anminBlinkUnCat.play();

                            //- tvMachineCateStatus.setText(PreferenceManager.isJobSetupStatus() ? "Setup - UnCategorized" : "UnCategorized");
                            //- tvMachineCateStatus.setTextFill(Color.RED);

                            // Save Last Uncate start time
                            timeLastUncatStartTime = timeCurrentMilis;
                        } else {
                            //- anminBlinkIdle.play();

                            //- String idleStatus = titleDownTimeReason[currentDeviceStatus - DEVICE_STATUS_IDLE1];

                            //- tvMachineCateStatus.setText(String.format("%sIdle[%s]", PreferenceManager.isJobSetupStatus() ? "Setup - " : "", idleStatus));
                            //- tvMachineCateStatus.setTextFill(colorOrange);

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

                    //- tvElapsedIdleTIme.setText("Elapsed Idle Time : " + DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(elapsedIdleTime));

                    // Check Stop Time and Parts Counting Time
                    if (currentDeviceStatus == DEVICE_STATUS_UNCATEGORIZED) {
                        // Check Stop Time and CSLock Status
                        elapsedStopTime += timeGap;

                        if (elapsedStopTime > PreferenceManager.getStopTimeLimit()) {

                            if (statusCsLock == 0) {
                                LogManager.getInstance().addNewLog("=> csLock Status : " + elapsedIdleTime + "_" + PreferenceManager.getStopTimeLimit());
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
                        if (PreferenceManager.isAutomaticPartsCounter() && !bOneTimeCounted) {
                            elapsedStopTimeForPartsCounting += timeGap;
                            if (elapsedStopTimeForPartsCounting >= PreferenceManager.getMinElapsedStopTime()) {
                                // Increase good value
                                int good_val = PreferenceManager.getShiftGoodParts();//Integer.parseInt(tvGood.getText().toString());
                                good_val += PreferenceManager.getPartsPerCycle();
                                // Show & Save Good
                                //- tvGood.setText(String.valueOf(good_val));
                                PreferenceManager.setShiftGoodParts(good_val);
                                shiftData.setGoodParts(good_val);

                                // Daily Live Data
                                PreferenceManager.setGoodParts(PreferenceManager.getGoodParts() + PreferenceManager.getPartsPerCycle());

                                elapsedStopTimeForPartsCounting = 0;
                                bOneTimeCounted = true;
                            }
                        }
                    }

                    // Show CSLock Status
                    //- if (statusCsLock == 1) {
                    //-     if (panelIdleSetUp.isVisible() == false) {
                    //-         showIdleSetupPanel();
                    //-         backCsLockStatus.setVisible(true);
                    //-         tvTitleStatusInterlocked.setVisible(true);

                    //-         playChinChin();
                    //-     }
                    //- }

                    // Update Regimes
                    //-if (timeCurrentMilis - timeLastRegimeUpdateTime >= INTERVAL_REGIME_UPDATE) {
                    //-    updateRegime();
                    //-
                    //-    timeLastRegimeUpdateTime = timeCurrentMilis;
                    //-}

                    // Finally Save current Timer Status in the storage
                    saveTimeStatus();
                }

                try {
                    Thread.sleep(INTERVAL_SIGNAL_CHECK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void retrieveTimerStatus() {

        // Manual Reset due to the over time and for checking new version
        String currentTimeString = DateTimeUtils.getDayTimeStringInUniformat();

        if (currentTimeString.equals(PreferenceManager.getLastTime())) {
            elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] = PreferenceManager.getTimeUnCat();
            elapsedMiliseconds[DEVICE_STATUS_INCYCLE] = PreferenceManager.getTimeIncycle();

            elapsedMiliseconds[DEVICE_STATUS_IDLE1] = PreferenceManager.getTimeIdle1();
            elapsedMiliseconds[DEVICE_STATUS_IDLE2] = PreferenceManager.getTimeIdle2();
            elapsedMiliseconds[DEVICE_STATUS_IDLE3] = PreferenceManager.getTimeIdle3();
            elapsedMiliseconds[DEVICE_STATUS_IDLE4] = PreferenceManager.getTimeIdle4();
            elapsedMiliseconds[DEVICE_STATUS_IDLE5] = PreferenceManager.getTimeIdle5();
            elapsedMiliseconds[DEVICE_STATUS_IDLE6] = PreferenceManager.getTimeIdle6();
            elapsedMiliseconds[DEVICE_STATUS_IDLE7] = PreferenceManager.getTimeIdle7();
            elapsedMiliseconds[DEVICE_STATUS_IDLE8] = PreferenceManager.getTimeIdle8();

            // Parts Good & Bad
            //- tvGood.setText(String.valueOf(0/*appSettings.getPartsGood()*/));    // We use shift counter, not daily counter
            //- tvBad.setText(String.valueOf(0/*appSettings.getPartsBad()*/));      // We use shift counter, not daily counter

            // Exception in case of negative value, not the normal logic
            if (elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] < 0) {
                resetTimers();
            }
        } else {
            // Report previous date
            resetTimers();
        }
    }

    private void saveTimeStatus() {

        String currentTimeString = DateTimeUtils.getDayTimeStringInUniformat();
        if (!currentTimeString.equals(PreferenceManager.getLastTime())) {
            // Report previous date
            //Log.e("MMS", "***" + lastRecordTime+ " " + currentTimeString);
            reportStatus();

            resetTimers();

            resetShiftData(true);
        } else {
            // Check Exception
            if (elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] < 0) {
                LogManager.getInstance().addNewLog("Unexpected Negative Value, Reset status");
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
                    PreferenceManager.setTimeUnCat(elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED]);
                    PreferenceManager.setTimeInCycle(elapsedMiliseconds[DEVICE_STATUS_INCYCLE]);
                    PreferenceManager.setTimeIdle1(elapsedMiliseconds[DEVICE_STATUS_IDLE1]);
                    PreferenceManager.setTimeIdle2(elapsedMiliseconds[DEVICE_STATUS_IDLE2]);
                    PreferenceManager.setTimeIdle3(elapsedMiliseconds[DEVICE_STATUS_IDLE3]);
                    PreferenceManager.setTimeIdle4(elapsedMiliseconds[DEVICE_STATUS_IDLE4]);
                    PreferenceManager.setTimeIdle5(elapsedMiliseconds[DEVICE_STATUS_IDLE5]);
                    PreferenceManager.setTimeIdle6(elapsedMiliseconds[DEVICE_STATUS_IDLE6]);
                    PreferenceManager.setTimeIdle7(elapsedMiliseconds[DEVICE_STATUS_IDLE7]);
                    PreferenceManager.setTimeIdle8(elapsedMiliseconds[DEVICE_STATUS_IDLE8]);

                    //Log.e("MMS", lastRecordTime+ " " + currentTimeString);
                    PreferenceManager.setLastTime(currentTimeString);
                }
            }
        }
    }

    private void resetTimers() {

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
        String qtyCompleted = String.format("%d,%d", PreferenceManager.getGoodParts(), PreferenceManager.getBadParts());
        PreferenceManager.setQtyCompleted(qtyCompleted);

        // Set Good and Bad Parts
        PreferenceManager.setGoodParts(0);
        PreferenceManager.setBadParts(0);

        //tvGood.setText("0");
        //tvBad.setText("0");

        // Update Timer to avoid duplication
        String currentTimeString = DateTimeUtils.getDayTimeStringInUniformat();
        PreferenceManager.setLastTime(currentTimeString);

        // Garbage Collect
        System.gc();
    }

    private void refreshDowntimeReasons() {
        //- Update in UI
        /*for (int i = 0; i < 8; i++) {
            //btnDownTimeStatus[i].setText(titleDownTimeReason[i]);

            if (TextUtils.isEmpty(titleDownTimeReason[i])) {
                btnDownTimeStatus[i].setDisable(true);

                elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i] = 0;
                String idleStatusText = String.format("Disabled - Reason Not Defined\n%s",
                        DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i]));
                btnDownTimeStatus[i].setText(idleStatusText);
            } else {
                btnDownTimeStatus[i].setDisable(false);

                String idleStatusText = String.format("%s\n%s",
                        titleDownTimeReason[i],
                        DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i]));
                btnDownTimeStatus[i].setText(idleStatusText);
            }
        }*/
    }

    private void updateRegime() {
        RegimeUtils.calcRegimes(regimeData, false);

        //- Update in UI
        /*// Update Gauges
        gaugeAvailablity.setValue(regimeData.Availablity);
        gaugePerformance.setValue(regimeData.performance);
        gaugeQuality.setValue(regimeData.quality);
        gaugeOEE.setValue(regimeData.oee);

        // Update PieChart
        long totalTime = 0;
        totalTime += regimeData.inCycleTime;
        totalTime += regimeData.unCatTime;
        totalTime += regimeData.idle1Time;
        totalTime += regimeData.idle2Time;
        totalTime += regimeData.idle3Time;
        totalTime += regimeData.idle4Time;
        totalTime += regimeData.idle5Time;
        totalTime += regimeData.idle6Time;
        totalTime += regimeData.idle7Time;
        totalTime += regimeData.idle8Time;
        totalTime += regimeData.offlineT;

        if (totalTime == 0)
            totalTime = 100;

        // Calculate total time
        ArrayList<PieChart.Data> entries = new ArrayList<>();
        entries.add(new PieChart.Data("Cycle", (float) regimeData.inCycleTime * 100f / totalTime));
        entries.add(new PieChart.Data("Uncategorized", (float) regimeData.unCatTime * 100f / totalTime));

        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason1())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason1(), (float) regimeData.idle1Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason2())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason2(), (float) regimeData.idle2Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason3())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason3(), (float) regimeData.idle3Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason4())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason4(), (float) regimeData.idle4Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason5())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason5(), (float) regimeData.idle5Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason6())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason6(), (float) regimeData.idle6Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason7())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason7(), (float) regimeData.idle7Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason8())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason8(), (float) regimeData.idle8Time * 100f / totalTime));
        }

        entries.add(new PieChart.Data("Offline", (float) regimeData.offlineT * 100f / totalTime));

        pieChartData.setAll(entries);*/
    }

    private void saveCurrentShiftData() {
        // Check valid Shift Data
        if (shiftData.getStartTime() > 0 && shiftData.getStopTime() - shiftData.getStartTime() > 3000 && !TextUtils.isEmpty(shiftData.getMachine())) {
            shiftData.calcRegime();
            shiftData.setCompleted(true);

            DBTableShiftData.updateShiftData(shiftData);
        } else {
            DBTableShiftData.deleteShiftData(shiftData);
        }
    }

    // Reset Shift data whenever Shift Triggers(Log In/Out, Job In/Out, Shift Time, Midnight)
    public void resetShiftData(boolean isForMidnight) {

        String machine = PreferenceManager.getMachineID();
        String currJobID = PreferenceManager.getJobID();
        String operator = PreferenceManager.getUserName();
        String userId = PreferenceManager.getUserID();

        // Same Shift Data
        if (!isForMidnight) {
            // *In case of midnight, should process shift data reset forcebly.
            // *In case of normal time, should check dup data, when start app, and when set machine name, job, operator name,
            // this function is called several times at the same time
            if (machine.equals(shiftData.getMachine()) && currJobID.equals(shiftData.getJobID()) && operator.equals(shiftData.getOperator())) {
                System.out.println("shiftData: Same Job Info");
                return;
            }
        }

        // Complete current Shift and create new Shift -----

        // Check valid Shift Data
        if (shiftData.getStartTime() > 0 && shiftData.getStopTime() - shiftData.getStartTime() > 5000 && !TextUtils.isEmpty(shiftData.getMachine())) {
            System.out.println("shiftData: Save old shift data");

            shiftData.calcRegime();
            shiftData.setCompleted(true);

            DBTableShiftData.updateShiftData(shiftData);

            // Process Counter Values
            // *To use live data for daily good/bad parts, we disable followings.
            // *Instead of this, we calc daily good/bad parts whenever shift good/bad changes
            //// appSettings.setPartsGood(appSettings.getPartsGood() + shiftData.getGoodParts());
            //// appSettings.setPartsBad(appSettings.getPartsBad() + shiftData.getBadParts());
        } else {
            DBTableShiftData.deleteShiftData(shiftData);
        }

        // Reset Shift Counter Values
        PreferenceManager.setShiftGoodParts(0);
        PreferenceManager.setShiftBadParts(0);

        //- tvGood.setText("0");
        //- tvBad.setText("0");

        long currTimeMils = DateTimeUtils.getCurrentTime();

        // Create new Shift Data
        shiftData.resetData();

        // Set the current JOB Data and AUX Data
        shiftData.setJobID(currJobID);
        shiftData.setJobSequenceNo("");

        String jobDetails = PreferenceManager.getJobDetails();
        String valAuxData1 = "";
        String valAuxData2 = "";
        String valAuxData3 = "";
        try {
            JSONObject jsonObject = new JSONObject(jobDetails);
            valAuxData1 = jsonObject.optString("aux1data").replace("null", "");
            valAuxData2 = jsonObject.optString("aux2data").replace("null", "");
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
        shiftData.setTargetCycleTimeSeconds(PreferenceManager.getTargetCycleTimeSeconds()); // This is second value, no need to convert

        // -- Rework/Setup Status logic
        shiftData.setStatusRework(PreferenceManager.isJobReworkStatus() ? 1 : 0);
        shiftData.setStatusSetup(PreferenceManager.isJobSetupStatus() ? 1 : 0);

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
                //shiftData.setPlannedProductionTime(appSettings.getShift1PPT());
            } else if (idxOfShift == 1) {
                //shiftData.setPlannedProductionTime(appSettings.getShift2PPT());
            } else if (idxOfShift == 2) {
                //shiftData.setPlannedProductionTime(appSettings.getShift3PPT());
            } else {
                shiftData.setPlannedProductionTime(PreferenceManager.getPlannedProductionTime());
            }
        }

        shiftData.setMachine(machine);

        shiftData.setOperator(operator);
        shiftData.setUserID(userId);

        shiftData.calcRegime();

        long newShiftID = DBTableShiftData.insertShiftData(shiftData);
        if (newShiftID > 0) {
            shiftData.setId(newShiftID);
        }

        System.out.println("shiftData: New Shift : ID=" + shiftData.getId());

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_getCurrentPartsCount);
                // add request parameters or form parameters
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("customer_id", PreferenceManager.getFactoryID()));
                urlParameters.add(new BasicNameValuePair("operator", operator));
                urlParameters.add(new BasicNameValuePair("machine", machine));
                urlParameters.add(new BasicNameValuePair("date", DateTimeUtils.toStringFormat_13(new Date(currTimeMils))));
                urlParameters.add(new BasicNameValuePair("jobID", currJobID));

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse result = httpClient.execute(post);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    JSONObject response = new JSONObject(json);
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

                            PreferenceManager.setShiftGoodParts(currGoods);
                            PreferenceManager.setShiftBadParts(currBads);

                            //- tvGood.setText(String.valueOf(currGoods));
                            //- tvBad.setText(String.valueOf(currBads));
                        } else {
                            String message = "";
                            if (response.has("message") && !response.isNull("message")) {
                                message = response.getString("message");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Remove old shift data
        DBTableShiftData.removeOldShiftData();
    }

    private void reportStatus() {

        String accountID = PreferenceManager.getFactoryID();
        String machineID = PreferenceManager.getMachineID();
        if (TextUtils.isEmpty(accountID) || TextUtils.isEmpty(machineID)) {
            return;
        }

        // upload prevDevice Status
        String createdAt = DateTimeUtils.toStringFormat_20(new Date(timeLastReportMilis));
        String operator = PreferenceManager.getUserName();

        String status = "";
        String colorPie = "";
        if (prevDeviceStatus == DEVICE_STATUS_UNCATEGORIZED) {
            colorPie = colorCharts[DEVICE_STATUS_UNCATEGORIZED];   //  "#ff0000"

            if (PreferenceManager.isJobSetupStatus()) {
                status = "Setup-Uncategorized";
            } else {
                status = "Idle-Uncategorized";
            }
        } else if (prevDeviceStatus == DEVICE_STATUS_INCYCLE) {
            colorPie = colorCharts[DEVICE_STATUS_INCYCLE];   // "#46c392"

            if (PreferenceManager.isJobSetupStatus()) {
                status = "Setup-In Cycle";
            } else {
                status = "In Cycle";
            }
        } else {
            // In case of Idle, we apply same gantt color
            colorPie = colorCharts[prevDeviceStatus];              // "#ffa300";

            if (PreferenceManager.isJobSetupStatus()) {
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

        int battLevel = 100;
        String jobId = PreferenceManager.getJobID();

        long start = timeLastReportMilis;
        long end = timeCurrentMilis;

        String timeStamp = DateTimeUtils.toStringFormat_12(new Date(timeCurrentMilis));
        int timeStampMs = (int) (timeCurrentMilis % 1000);

        // GanttDataModel newGanttData = new GanttDataModel(createdAt, machineID, operator, status, color, start, end, timeStamp, timeStampMs);
        DBTableGanttData.insertGanttData(createdAt, machineID, operator, status, colorPie, start, end, timeStamp, timeStampMs, jobId, battLevel, "");

        // ------------------------ Add New record for the db updates -----------------
        if (needToChangeStatusFromUncat && timeLastUncatStartTime > 0) {
            DBTableGanttData.insertGanttData("UPDATE_STATUS", machineID, operator, status, colorPie, timeLastUncatStartTime, end, timeStamp, timeStampMs, jobId, battLevel, "");
        }
        // ----------------------------------------------------------------------------

        timeLastReportMilis = timeCurrentMilis;
    }

    private static final int ALERT_TYPE_OUTCYCLE = 1;
    private static final int ALERT_TYPE_STOPTIMELIMIT = 2;

    public boolean sentStopTimeAlert = false;

    private boolean sendAlert(int alertType) {

        if (!PreferenceManager.isCycleStopAlert()) {
            LogManager.getInstance().addNewLog("Mail, not alert set or not loggedin");
            return false;
        }

        ArrayList<String> toMails = new ArrayList<>();
        if (Utils.isValidEmail(PreferenceManager.getAlertEmail1())) {
            toMails.add(PreferenceManager.getAlertEmail1());
        }
        if (Utils.isValidEmail(PreferenceManager.getAlertEmail2())) {
            toMails.add(PreferenceManager.getAlertEmail2());
        }
        if (Utils.isValidEmail(PreferenceManager.getAlertEmail3())) {
            toMails.add(PreferenceManager.getAlertEmail3());
        }

        if (toMails.isEmpty()) {
            LogManager.getInstance().addNewLog("Mail, Not recepters");
            return false;
        }

        String subject = "";
        String contents = "";
        if (alertType == ALERT_TYPE_OUTCYCLE) {
            subject = "Cycle Stop Time";

            //Machine Name’ cycle ended at: ‘cycle stop time”
            String machineName = PreferenceManager.getMachineID();
            String cycleStopTime = DateTimeUtils.toStringFormat_12(new Date());
            contents = String.format("%s' cycle ended at %s.", machineName, cycleStopTime);
        } else if (alertType == ALERT_TYPE_STOPTIMELIMIT) {
            subject = "Exceed Stop Time";

            //Makino has been stopped for x minutes”
            String machineName = PreferenceManager.getMachineID();
            float stopTimeMins = PreferenceManager.getStopTimeLimit() / 60000f;
            contents = String.format("%s has been stopped for %.1f minute(s) in %s.", machineName, stopTimeMins, DateTimeUtils.toStringFormat_20(new Date()));
        }

        //https://myaccount.google.com/lesssecureapps
        new MailSender("reports@slymms.com",
                "246896321S!",
                toMails,
                subject,
                contents, new MailSender.MailSendCallback() {
            @Override
            public void onMailFailed(String error) {
                LogManager.getInstance().addNewLog("Mail, Failed - " + error);
            }

            @Override
            public void onMailSent() {
                LogManager.getInstance().addNewLog("Mail, Sent!");
            }
        }).start();

        return true;
    }

    private void processNewDeviceStatus(boolean isNewStatusInCycle) {
        if (isNewStatusInCycle) {
            if (currentDeviceStatus != DEVICE_STATUS_INCYCLE) {

                resetStopTime();
                resetElapsedIdleTime();

                currentDeviceStatus = DEVICE_STATUS_INCYCLE;

                // Hide Stop Time Reason Screen
                //hideIdleSetupPanel();
            }
        } else {
            if (currentDeviceStatus == DEVICE_STATUS_INCYCLE) {

                opDownTimeStatus = DEVICE_STATUS_UNCATEGORIZED;
                sentStopTimeAlert = false; // ! Important !

                //resetDownTimeReasonStatus();
            }

            currentDeviceStatus = opDownTimeStatus;
        }
    }

    public void resetStopTime() {
        LogManager.getInstance().addNewLog("<= csLock Status");

        // Set StopTime = 0
        elapsedStopTime = 0;
        statusCsLock = 0;

        elapsedStopTimeForPartsCounting = 0;
        bOneTimeCounted = false;

        //- backCsLockStatus.setVisible(false);

        //- tvTitleStatusInterlocked.setVisible(false);

        //- btnCloseIdleStatus.setVisible(true);

        // Stop Audio
        //- stopChinChin();
    }

    private void resetElapsedIdleTime() {
        elapsedIdleTime = 0;
    }
}
