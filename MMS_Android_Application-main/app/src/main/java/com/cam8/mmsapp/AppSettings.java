package com.cam8.mmsapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONException;
import org.json.JSONObject;

public class AppSettings {
    private static final String APP_SHARED_PREFS = "cam8_mms_prefs";
    private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;
    private static final String LOGGED_IN = "logged_in";
    private static final String DEVICE_ID = "device_id";
    private static final String DEVICE_ID_SET = "device_id_set";
    private static final String DEVICE_TOKEN = "device_token";

    // Modbus TCP Settings
    private static final String DEVICE_IP = "device_ip";
    private static final String DEVICE_PORT = "device_port";
    private static final String DEVICE_MEMORY = "device_memory";

    // Machine Status Offline/Online
    private static final String MACHINE_STATUS = "machine_status";

    // Bluetooth Settings
    private static final String MACHINE_NAME = "machine_name";
    private static final String BT_DEVICE_NAME = "bt_device_name";
    private static final String BT_DEVICE_ADDR = "bt_device_addr";

    private static final String JOB_ID = "job_id";
    private static final String JOB_DETAILS = "job_details";
    private static final String JOB_REWORK = "job_rework";
    private static final String JOB_SETUP = "job_setup";

    private static final String VALID_STATUS = "valid_status";



    // Downtime Settings
    private String DOWNTIME_REASON1 = "downtime_reason1";
    private String DOWNTIME_REASON2 = "downtime_reason2";
    private String DOWNTIME_REASON3 = "downtime_reason3";
    private String DOWNTIME_REASON4 = "downtime_reason4";
    private String DOWNTIME_REASON5 = "downtime_reason5";
    private String DOWNTIME_REASON6 = "downtime_reason6";
    private String DOWNTIME_REASON7 = "downtime_reason7";
    private String DOWNTIME_REASON8 = "downtime_reason8";

    // Aux Data Titles
    private String AUX1_DATA_TITLE = "aux1data_title";
    private String AUX2_DATA_TITLE = "aux2data_title";
    private String AUX3_DATA_TITLE = "aux3data_title";

    // CSLock
    private String USE_CSLOCK = "USE_CSLOCK";
    private String REVERSE_CSLOCK = "REVERSE_CSLOCK";
    private String GUEST_LOCK = "GUEST_LOCK";
    private String CSLOCK_SOUND = "CSLOCK_SOUND";

    // Stop Time Limit Settings
    private String STOP_TIME_LIMIT = "stop_time_limit";

    // Production Time Settings
    private String PLANNED_PRODUCTION_TIME = "planned_production_time";

    // Shifts Planned Production Time Settings
    private String SHIFT1_PPT = "shift1_ppt";
    private String SHIFT2_PPT = "shift2_ppt";
    private String SHIFT3_PPT = "shift3_ppt";


    private String TARGET_CYCLE_TIME = "target_cycle_time";

    // Timer Status
    private static final String TIME_INCYCLE = "time_incycle";

    private static final String TIME_IDLE1 = "time_idle1";
    private static final String TIME_IDLE2 = "time_idle2";
    private static final String TIME_IDLE3 = "time_idle3";
    private static final String TIME_IDLE4 = "time_idle4";
    private static final String TIME_IDLE5 = "time_idle5";
    private static final String TIME_IDLE6 = "time_idle6";
    private static final String TIME_IDLE7 = "time_idle7";
    private static final String TIME_IDLE8 = "time_idle8";

    private static final String TIME_UNCAT = "time_uncate";

    private static final String PARTS_GOOD = "parts_good";
    private static final String PARTS_BAD = "parts_bad";

    private static final String SHIFT_PARTS_GOOD = "shift_parts_good";
    private static final String SHIFT_PARTS_BAD = "shift_parts_bad";

    private static final String QTY_COMPLETED = "qty_completed";

    private static final String LAST_TIME = "last_recorded_time";

    private String USER_ID = "userid";
    private String USER_NAME = "USER_NAME";
    private String USER_AVATAR = "USER_AVATAR";
    private String USER_LEVEL = "USER_LEVEL";

    private static final String CUSTOMERID = "customer";
    private String CUSTOMER_NAME = "CUSTOMER_NAME";
    private String CUSTOMER_AVATAR = "CUSTOMER_AVATAR";

    private String CYCLE_STOP_ALERT = "CYCLE_STOP_ALERT";
    private String ALERT_EMAIL1 = "ALERT_EMAIL1";
    private String ALERT_EMAIL2 = "ALERT_EMAIL2";
    private String ALERT_EMAIL3 = "ALERT_EMAIL3";
    private String ALERT_EMAIL1_TIME = "ALERT_EMAIL1_TIME";
    private String ALERT_EMAIL2_TIME = "ALERT_EMAIL2_TIME";
    private String ALERT_EMAIL3_TIME = "ALERT_EMAIL3_TIME";

    private String AUTOMATIC_PARTS_COUNTER = "AUTOMATIC_PARTS_COUNTER";
    private String MIN_ELAPSED_TIME = "MIN_ELAPSED_TIME";
    private String PARTS_PER_CYCLE = "PARTS_PER_CYCLE";

    private String BARCHART_FORMAT = "BARCHART_FORMAT";

    private String MAINTENANCE_TASK = "MAINTENANCE_TASK";

    private String VERSION_NOTIFIED = "VERSION_NOTIFIED";

    private String USE_LOCAL_SERVER = "USE_LOCAL_SERVER";
    private String LOCAL_SERVERIP = "LOCAL_SERVERIP";

    private String USE_JOB_AUTO_LOGOUT = "USE_JOB_AUTO_LOGOUT";

    private String INCYCLE_FROM_PLC = "INCYCLE_FROM_PLC";
    private String PROCESS_MONITOR_TYPE = "PROCESS_MONITOR_TYPE";
    private String TEMPERATURE_DATA_SOURCE = "TEMPERATURE_DATA_SOURCE";

    // Daily Goal Target
    private String DGT_TITLE = "DGT_TITLE";
    private String DGT_FOMULAR = "DGT_FOMULAR";
    private String DGT_OPTION = "DGT_OPTION";
    private String DGT_UNIT = "DGT_UNIT";
    private String DGT_DISP_MODE = "DGT_DISP_MODE";
    private String DGT_MAX_VALUE = "DGT_MAX_VALUE";
    private String DGT_VALUE = "DGT_VALUE";

    // Quality Station
    private String REJECT_REASON_TITLES = "REJECT_REASON_TITLES";

    // Stage Info
    private String STAGE1_INFO = "STAGE1_INFO";
    private String STAGE2_INFO = "STAGE2_INFO";
    private String STAGE3_INFO = "STAGE3_INFO";
    private String STAGE4_INFO = "STAGE4_INFO";
    private String STAGE5_INFO = "STAGE5_INFO";
    private String STAGE6_INFO = "STAGE6_INFO";
    private String STAGE7_INFO = "STAGE7_INFO";
    private String STAGE8_INFO = "STAGE8_INFO";
    private String STAGE9_INFO = "STAGE9_INFO";
    private String PANEL_DATA_LOG_INFO = "PANEL_DATA_LOG_INFO";

    public AppSettings(Context context) {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public String getString(String key) {
        return appSharedPrefs.getString(key, "");
    }

    public int getInt(String key) {
        return appSharedPrefs.getInt(key, 0);
    }

    public long getLong(String key) {
        try {
            return appSharedPrefs.getLong(key, 0);
        } catch (ClassCastException e) {
            return appSharedPrefs.getInt(key, 0) * 1000;
        }
    }

    public float getFloat(String key) {
        try {
            return appSharedPrefs.getFloat(key, 0.0f);
        } catch (ClassCastException e) {
            return 0.0f;
        }
    }

    public boolean getBoolean(String key) {
        return appSharedPrefs.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public void putInt(String key, int value) {
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public void putLong(String key, long value) {
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
    }

    public void putFloat(String key, float value) {
        prefsEditor.putFloat(key, value);
        prefsEditor.commit();
    }

    public void putBoolean(String key, boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public void remove(String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    public void clear() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public String getUserId() {
        return appSharedPrefs.getString(USER_ID, "0");
    }

    public void setUserId(String userId) {
        prefsEditor.putString(USER_ID, userId);
        prefsEditor.commit();
    }

    public String getUserName() {
        return appSharedPrefs.getString(USER_NAME, "Unattended");
    }
    public void setUserName(String value) {
        prefsEditor.putString(USER_NAME, value);
        prefsEditor.commit();
    }

    public String getUserAvatar() {
        String avatar = appSharedPrefs.getString(USER_AVATAR, "");
        avatar = avatar.replaceAll("\n", "");
        return avatar;
    }

    public void setUserAvatar(String value) {
        prefsEditor.putString(USER_AVATAR, value);
        prefsEditor.commit();
    }

    public int getUserLevel() {
        int userLevel = appSharedPrefs.getInt(USER_LEVEL, 0);
        return userLevel;
    }

    public void setUserLevel(int value) {
        prefsEditor.putInt(USER_LEVEL, value);
        prefsEditor.commit();
    }

    public void setDeviceIdSet(boolean isAdded) {
        prefsEditor.putBoolean(DEVICE_ID_SET, isAdded);
        prefsEditor.apply();
    }

    public boolean isDeviceIdSet() {
        return appSharedPrefs.getBoolean(DEVICE_ID_SET, false);
    }

    /*public void setDeviceId(String deviceId) {
        if (!isDeviceIdSet()) {
            setDeviceIdSet(true);
            prefsEditor.putString(DEVICE_ID, deviceId);
            prefsEditor.apply();
        }
    }

    public String getDeviceId() {
        return appSharedPrefs.getString(DEVICE_ID, "");
    }*/

    public void setLoggedIn() {
        prefsEditor.putBoolean(LOGGED_IN, true);
        prefsEditor.apply();
    }

    public boolean isLoggedIn() {
        return appSharedPrefs.getBoolean(LOGGED_IN, false);
    }

    public boolean logOut() {
        prefsEditor.putBoolean(LOGGED_IN, false);
        return prefsEditor.commit();
    }

    public void setDeviceToken(String deviceToken) {
        prefsEditor.putString(DEVICE_TOKEN, deviceToken);
        prefsEditor.apply();
    }

    public String getDeviceToken() {
        return appSharedPrefs.getString(DEVICE_TOKEN, "");
    }


    // These are all Modbus TCP Settings
    public void setDeviceIP(String deviceToken) {
        prefsEditor.putString(DEVICE_IP, deviceToken);
        prefsEditor.apply();
    }
    public String getDeviceIP() {
        return appSharedPrefs.getString(DEVICE_IP, "192.168.1.51");
    }

    public void setDevicePort(int devicePort) {
        prefsEditor.putInt(DEVICE_PORT, devicePort);
        prefsEditor.apply();
    }
    public int getDevicePort() {
        return appSharedPrefs.getInt(DEVICE_PORT, 502);
    }

    public void setDeviceMemory(int deviceMemory) {
        prefsEditor.putInt(DEVICE_MEMORY, deviceMemory);
        prefsEditor.apply();
    }
    public int getDeviceMemory() {
        return appSharedPrefs.getInt(DEVICE_MEMORY, 1);
    }

    // Machine Status
    public String getMachineStatus() {
        String entryItemInfo = appSharedPrefs.getString(MACHINE_STATUS, "Online");
        return entryItemInfo;
    }
    public void setMachineStatus(String devStatus) {
        if (devStatus == null) {
            devStatus = "";
        }
        prefsEditor.putString(MACHINE_STATUS, devStatus);
        prefsEditor.commit();
    }

    // Bluetooth Device Information
    public String getMachineName() {
        String entryItemInfo = appSharedPrefs.getString(MACHINE_NAME, "Machine Name");
        return entryItemInfo;
    }
    public void setMachineName(String devName) {
        if (devName == null) {
            devName = "";
        }
        prefsEditor.putString(MACHINE_NAME, devName);
        prefsEditor.commit();
    }

    public String getBTDeviceName() {
        String entryItemInfo = appSharedPrefs.getString(BT_DEVICE_NAME, "");
        return entryItemInfo;
    }
    public void setBTDeviceName(String devName) {
        if (devName == null) {
            devName = "";
        }
        prefsEditor.putString(BT_DEVICE_NAME, devName);
        prefsEditor.commit();
    }

    public String getBTDeviceAddr() {
        String entryItemInfo = appSharedPrefs.getString(BT_DEVICE_ADDR, "");
        return entryItemInfo;
    }

    public void setBTDeviceAddr(String devAddr) {
        prefsEditor.putString(BT_DEVICE_ADDR, devAddr);
        prefsEditor.commit();
    }

    public String getJobId() {
        String jobID = appSharedPrefs.getString(JOB_ID, "");
        return jobID;
    }
    public void setJobId(String value) {
        prefsEditor.putString(JOB_ID, value);
        prefsEditor.commit();
    }

    public String getJobDetails() {
        String value = appSharedPrefs.getString(JOB_DETAILS, "");
        return value;
    }
    public void setJobDetails(String value) {
        prefsEditor.putString(JOB_DETAILS, value);
        prefsEditor.commit();
    }

    public boolean isJobReworkStatus() {
        return appSharedPrefs.getBoolean(JOB_REWORK, false);
    }
    public void setJobReworkStatus(boolean value) {
        prefsEditor.putBoolean(JOB_REWORK, value);
        prefsEditor.commit();
    }

    public boolean isJobSetupStatus() {
        return appSharedPrefs.getBoolean(JOB_SETUP, false);
    }
    public void setJobSetupStatus(boolean value) {
        prefsEditor.putBoolean(JOB_SETUP, value);
        prefsEditor.commit();
    }


    // Account ID, Customer ID sm_ks
    public String getCustomerID() {
        String accountID = appSharedPrefs.getString(CUSTOMERID, "");
        if (accountID.endsWith("_")) {
            accountID = accountID.substring(0, accountID.length() - 1);
        }
        return accountID;
    }
    public void setCustomerID(String value) {
        prefsEditor.putString(CUSTOMERID, value);
        prefsEditor.commit();
    }

    public String getCustomerName() {
        String jobID = appSharedPrefs.getString(CUSTOMER_NAME, "Guest");
        return jobID;
    }
    public void setCustomerName(String value) {
        prefsEditor.putString(CUSTOMER_NAME, value);
        prefsEditor.commit();
    }

    public String getCustomerAvatar() {
        String jobID = appSharedPrefs.getString(CUSTOMER_AVATAR, "");
        return jobID;
    }
    public void setCustomerAvatar(String value) {
        prefsEditor.putString(CUSTOMER_AVATAR, value);
        prefsEditor.commit();
    }

    public boolean isValidStatus() {
        return appSharedPrefs.getBoolean(VALID_STATUS, true);
    }

    public void setValidStatus(boolean value) {
        prefsEditor.putBoolean(VALID_STATUS, value);
        prefsEditor.commit();
    }

    // Downtime Status
    public String getDownTimeReason1() {
        String entryItemInfo = appSharedPrefs.getString(DOWNTIME_REASON1, "Clear Chips");
        return entryItemInfo;
    }
    public void setDowntimeReason1(String reason) {
        prefsEditor.putString(DOWNTIME_REASON1, reason);
        prefsEditor.commit();
    }

    public String getDownTimeReason2() {
        String entryItemInfo = appSharedPrefs.getString(DOWNTIME_REASON2, "Wait Materials");
        return entryItemInfo;
    }
    public void setDowntimeReason2(String reason) {
        prefsEditor.putString(DOWNTIME_REASON2, reason);
        prefsEditor.commit();
    }

    public String getDownTimeReason3() {
        String entryItemInfo = appSharedPrefs.getString(DOWNTIME_REASON3, "Wait Tooling");
        return entryItemInfo;
    }
    public void setDowntimeReason3(String reason) {
        prefsEditor.putString(DOWNTIME_REASON3, reason);
        prefsEditor.commit();
    }

    public String getDownTimeReason4() {
        String entryItemInfo = appSharedPrefs.getString(DOWNTIME_REASON4, "Break");
        return entryItemInfo;
    }
    public void setDowntimeReason4(String reason) {
        prefsEditor.putString(DOWNTIME_REASON4, reason);
        prefsEditor.commit();
    }

    public String getDownTimeReason5() {
        String entryItemInfo = appSharedPrefs.getString(DOWNTIME_REASON5, "No Operator");
        return entryItemInfo;
    }
    public void setDowntimeReason5(String reason) {
        prefsEditor.putString(DOWNTIME_REASON5, reason);
        prefsEditor.commit();
    }

    public String getDownTimeReason6() {
        String entryItemInfo = appSharedPrefs.getString(DOWNTIME_REASON6, "P.M.");
        return entryItemInfo;
    }
    public void setDowntimeReason6(String reason) {
        prefsEditor.putString(DOWNTIME_REASON6, reason);
        prefsEditor.commit();
    }

    public String getDownTimeReason7() {
        String entryItemInfo = appSharedPrefs.getString(DOWNTIME_REASON7, "Unplanned Repair");
        return entryItemInfo;
    }
    public void setDowntimeReason7(String reason) {
        prefsEditor.putString(DOWNTIME_REASON7, reason);
        prefsEditor.commit();
    }

    public String getDownTimeReason8() {
        String entryItemInfo = appSharedPrefs.getString(DOWNTIME_REASON8, "Other");
        return entryItemInfo;
    }
    public void setDowntimeReason8(String reason) {
        prefsEditor.putString(DOWNTIME_REASON8, reason);
        prefsEditor.commit();
    }

    public String getAux1DataTitle() {
        String entryItemInfo = appSharedPrefs.getString(AUX1_DATA_TITLE, "Aux1Data");
        return entryItemInfo;
    }
    public void setAux1DataTitle(String value) {
        prefsEditor.putString(AUX1_DATA_TITLE, value);
        prefsEditor.commit();
    }

    public String getAux2DataTitle() {
        String entryItemInfo = appSharedPrefs.getString(AUX2_DATA_TITLE, "Aux2Data");
        return entryItemInfo;
    }
    public void setAux2DataTitle(String value) {
        prefsEditor.putString(AUX2_DATA_TITLE, value);
        prefsEditor.commit();
    }

    public String getAux3DataTitle() {
        String entryItemInfo = appSharedPrefs.getString(AUX3_DATA_TITLE, "Aux3Data");
        return entryItemInfo;
    }
    public void setAux3DataTitle(String value) {
        prefsEditor.putString(AUX3_DATA_TITLE, value);
        prefsEditor.commit();
    }

    public boolean isUseCSLock() {
        boolean isUseCSLock = appSharedPrefs.getBoolean(USE_CSLOCK, true);
        return isUseCSLock;
    }
    public void setUseCSLock(boolean value) {
        prefsEditor.putBoolean(USE_CSLOCK, value);
        prefsEditor.commit();
    }

    public boolean isReverseCSLock() {
        boolean isUseCSLock = appSharedPrefs.getBoolean(REVERSE_CSLOCK, false);
        return isUseCSLock;
    }
    public void setReverseCSLock(boolean value) {
        prefsEditor.putBoolean(REVERSE_CSLOCK, value);
        prefsEditor.commit();
    }

    // when the user=guest, make cycle start interlock active.
    // We need to make an option in setting page to enable/disable this feature.
    // When cycle start interlock is on because of user not logged in, need to display message and prompt user to log in
    // we need to add an additional feature. I’m CSLock Settings, we need to add another feature: Lock if operator is not logged in
    // If user is not logged in yet(Guest), then CSLock is on regardless of stop time?
    // Yes correct. This is so that the machine can not run unless a qualified operator is logged in using credentials
    public boolean isGuestLock() {
        boolean isUseCSLock = appSharedPrefs.getBoolean(GUEST_LOCK, false);
        return isUseCSLock;
    }
    public void setGuestLock(boolean value) {
        prefsEditor.putBoolean(GUEST_LOCK, value);
        prefsEditor.commit();
    }

    // CSLock Status Sound status
    public boolean isCSLockSoundEnabled() {
        boolean isUseCSLock = appSharedPrefs.getBoolean(CSLOCK_SOUND, true);
        return isUseCSLock;
    }
    public void setCSLockSoundEnabled(boolean value) {
        prefsEditor.putBoolean(CSLOCK_SOUND, value);
        prefsEditor.commit();
    }

    public long getStopTimeLimit() {
        long stopTime = getLong(STOP_TIME_LIMIT);
        if (stopTime == 0) {
            stopTime = 90 * 1000; // Default Time : 90s
            setStopTimeLimit(stopTime);
        }
        return stopTime;
    }
    public void setStopTimeLimit(long time) { putLong(STOP_TIME_LIMIT, time); }

    // Planned Production Time
    public long getPlannedProductionTime() {
        long plannedTime = getLong(PLANNED_PRODUCTION_TIME);
        if (plannedTime == 0) {
            plannedTime = 8 * 3600 * 1000; // Default Time : 8 hour
            setPlannedProductionTime(plannedTime);
        }
        return plannedTime;
    }
    public void setPlannedProductionTime(long time) { putLong(PLANNED_PRODUCTION_TIME, time); }

    // Shift Planned Production Time Settings
    public long getShift1PPT() { return getLong(SHIFT1_PPT); }
    public void setShift1PPT(long time) { putLong(SHIFT1_PPT, time); }

    public long getShift2PPT() { return getLong(SHIFT2_PPT); }
    public void setShift2PPT(long time) { putLong(SHIFT2_PPT, time); }

    public long getShift3PPT() { return getLong(SHIFT3_PPT); }
    public void setShift3PPT(long time) { putLong(SHIFT3_PPT, time); }


    // Target Cycle Time In Seconds
    public long getTargetCycleTimeSeconds() {
        long targetCycleTime = getLong(TARGET_CYCLE_TIME);
        if (targetCycleTime == 0) {
            String jobDetails = getJobDetails();
            try {
                JSONObject jsonObject = new JSONObject(jobDetails);
                String strValue = jsonObject.optString("targetCycleTime");
                targetCycleTime = Long.parseLong(strValue);

                setTargetCycleTimeSeconds(targetCycleTime);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return targetCycleTime;
    }
    public void setTargetCycleTimeSeconds(long time) { putLong(TARGET_CYCLE_TIME, time); }

    public String getLastTime() {
        String lastTime = appSharedPrefs.getString(LAST_TIME, "");
        return lastTime;
    }
    public void setLastTime(String time) {
        prefsEditor.putString(LAST_TIME, time);
        prefsEditor.commit();
    }

    public int getGoodParts() { return getInt(PARTS_GOOD); }
    public void setGoodParts(int value) { putInt(PARTS_GOOD, value);}

    public int getBadParts() { return getInt(PARTS_BAD); }
    public void setBadParts(int value) { putInt(PARTS_BAD, value);}

    public int getShiftGoodParts() { return getInt(SHIFT_PARTS_GOOD); }
    public void setShiftGoodParts(int value) { putInt(SHIFT_PARTS_GOOD, value);}

    public int getShiftBadParts() { return getInt(SHIFT_PARTS_BAD); }
    public void setShiftBadParts(int value) { putInt(SHIFT_PARTS_BAD, value);}

    public String getQtyCompleted() { return getString(QTY_COMPLETED); }
    public void setQtyCompleted(String value) { putString(QTY_COMPLETED, value);}

    public long getTimeIncycle() { return getLong(TIME_INCYCLE); }
    public void setTimeInCycle(long time) { putLong(TIME_INCYCLE, time); }

    public long getTimeUnCat() { return getLong(TIME_UNCAT); }
    public void setTimeUnCat(long time) { putLong(TIME_UNCAT, time); }

    public long getTimeIdle1() { return getLong(TIME_IDLE1); }
    public void setTimeIdle1(long time) { putLong(TIME_IDLE1, time); }

    public long getTimeIdle2() { return getLong(TIME_IDLE2); }
    public void setTimeIdle2(long time) { putLong(TIME_IDLE2, time); }

    public long getTimeIdle3() { return getLong(TIME_IDLE3); }
    public void setTimeIdle3(long time) { putLong(TIME_IDLE3, time); }

    public long getTimeIdle4() { return getLong(TIME_IDLE4); }
    public void setTimeIdle4(long time) { putLong(TIME_IDLE4, time); }

    public long getTimeIdle5() { return getLong(TIME_IDLE5); }
    public void setTimeIdle5(long time) { putLong(TIME_IDLE5, time); }

    public long getTimeIdle6() { return getLong(TIME_IDLE6); }
    public void setTimeIdle6(long time) { putLong(TIME_IDLE6, time); }

    public long getTimeIdle7() { return getLong(TIME_IDLE7); }
    public void setTimeIdle7(long time) { putLong(TIME_IDLE7, time); }

    public long getTimeIdle8() { return getLong(TIME_IDLE8); }
    public void setTimeIdle8(long time) { putLong(TIME_IDLE8, time); }

    public boolean isCycleStopAlert() {
        return appSharedPrefs.getBoolean(CYCLE_STOP_ALERT, false);
    }

    public void setCycleStopAlert(boolean value) {
        prefsEditor.putBoolean(CYCLE_STOP_ALERT, value);
        prefsEditor.apply();
    }

    public String getAlertEmail1() { return getString(ALERT_EMAIL1);}
    public void setAlertEmail1(String value) {putString(ALERT_EMAIL1, value);}
    public float getAlertEmail1Time() { return getFloat(ALERT_EMAIL1_TIME);}
    public void setAlertEmail1Time(float value) {putFloat(ALERT_EMAIL1_TIME, value);}


    public String getAlertEmail2() { return getString(ALERT_EMAIL2);}
    public void setAlertEmail2(String value) {putString(ALERT_EMAIL2, value);}
    public float getAlertEmail2Time() { return getFloat(ALERT_EMAIL2_TIME);}
    public void setAlertEmail2Time(float value) {putFloat(ALERT_EMAIL2_TIME, value);}

    public String getAlertEmail3() { return getString(ALERT_EMAIL3);}
    public void setAlertEmail3(String value) {putString(ALERT_EMAIL3, value);}
    public float getAlertEmail3Time() { return getFloat(ALERT_EMAIL3_TIME);}
    public void setAlertEmail3Time(float value) {putFloat(ALERT_EMAIL3_TIME, value);}

    public boolean isAutomaticPartsCounter() {
        return appSharedPrefs.getBoolean(AUTOMATIC_PARTS_COUNTER, false);
    }

    public void setAutomaticPartsCounter(boolean value) {
        prefsEditor.putBoolean(AUTOMATIC_PARTS_COUNTER, value);
        prefsEditor.apply();
    }

    public long getMinElapsedStopTime() {
        long stopTime = getLong(MIN_ELAPSED_TIME);
        if (stopTime == 0) {
            stopTime = 10 * 1000; // Default Time : 10s
            setMinElapsedStopTime(stopTime);
        }
        return stopTime;
    }
    public void setMinElapsedStopTime(long time) { putLong(MIN_ELAPSED_TIME, time); }

    public int getPartsPerCycle() {
        int partsPerCycle = getInt(PARTS_PER_CYCLE);
        if (partsPerCycle == 0) {
            partsPerCycle = 1;
            setPartsPerCycle(partsPerCycle);
        }
        return partsPerCycle;
    }
    public void setPartsPerCycle(int value) { putInt(PARTS_PER_CYCLE, value); }

    public void setChartOption(int value) {
        prefsEditor.putInt(BARCHART_FORMAT, value);
        prefsEditor.apply();
    }

    public int getChartOption() {
        return appSharedPrefs.getInt(BARCHART_FORMAT, 0);
    }

    private String LAST_UPDATE = "LastUpdate_Date";
    // Save Last Update Date
    public void setLastUpdateDate(String dumpData) {
        prefsEditor.putString(LAST_UPDATE, dumpData);
        prefsEditor.commit();
    }
    // Restore Update Date
    public String getLastUpdateDate() {
        return appSharedPrefs.getString(LAST_UPDATE, "");
    }

    // Maintenance Task
    // Save Maintenance
    public void saveMaintenanceTask(String info) {
        prefsEditor.putString(MAINTENANCE_TASK, info);
        prefsEditor.commit();
    }
    // Restore Maintenance
    public String restoreMaintenanceTask() {
        return appSharedPrefs.getString(MAINTENANCE_TASK, "");
    }

    // Save Version Notified
    public void setVersionNotified(String info) {
        prefsEditor.putString(VERSION_NOTIFIED, info);
        prefsEditor.commit();
    }
    // Restore Version Notified
    public String getVersionNotified() {
        return appSharedPrefs.getString(VERSION_NOTIFIED, "");
    }

    // Server
    public boolean isUsingLocalServer() {
        boolean isUseCSLock = appSharedPrefs.getBoolean(USE_LOCAL_SERVER, false);
        return isUseCSLock;
    }
    public void setUsingLocalServer(boolean value) {
        prefsEditor.putBoolean(USE_LOCAL_SERVER, value);
        prefsEditor.commit();
    }

    public String getLocalServerIP() {
        return appSharedPrefs.getString(LOCAL_SERVERIP, "192.168.1.36");
    }
    public void setLocalServerIP(String ip) {
        prefsEditor.putString(LOCAL_SERVERIP, ip);
        prefsEditor.commit();
    }

    // Job
    public boolean isUsingJobAutoLogout() {
        boolean isJobAutoLogout = appSharedPrefs.getBoolean(USE_JOB_AUTO_LOGOUT, false);
        return isJobAutoLogout;
    }
    public void setUsingJobAutoLogout(boolean value) {
        prefsEditor.putBoolean(USE_JOB_AUTO_LOGOUT, value);
        prefsEditor.commit();
    }

    public boolean isInCycleFromPLC() {
        boolean isInCycleFromPLC = appSharedPrefs.getBoolean(INCYCLE_FROM_PLC, true);
        return isInCycleFromPLC;
    }

    public void setInCycleFromPLC(boolean value) {
        prefsEditor.putBoolean(INCYCLE_FROM_PLC, value);
        prefsEditor.commit();
    }

    /*
    Process Monitor Type
    0 : NONE
    1 : Time Logger
    2 : Cleaning Station
    3 : Blast Station
    4 : Paint Station
    5 : Assembly Station 137
    6 : Assembly Station 136
    7 : Assembly Station 3
    */
    public int getProcessMonitorType() {
        int value = appSharedPrefs.getInt(PROCESS_MONITOR_TYPE, 0);
        return value;
    }

    public void setProcessMonitorType(int value) {
        prefsEditor.putInt(PROCESS_MONITOR_TYPE, value);
        prefsEditor.commit();
    }

    /*
    Temperature Data Source
    0 : Manual Entry
    1 : IIot Link = API Data from backend
    2 : PLC
    */
    public int getTemperatureDataSource() {
        int value = appSharedPrefs.getInt(TEMPERATURE_DATA_SOURCE, 0);
        return value;
    }

    public void setTemperatureDataSource(int value) {
        prefsEditor.putInt(TEMPERATURE_DATA_SOURCE, value);
        prefsEditor.commit();
    }

    /*Orientation Status*/
    private String APP_ORIENTATION = "APP_ORIENTATION";
    public int getAppOrientation() {
        return appSharedPrefs.getInt(APP_ORIENTATION, 0);
    }
    public void setAppOrientation(int value) {
        prefsEditor.putInt(APP_ORIENTATION, value);
        prefsEditor.commit();
    }

    // Daily Goal Target
    public String getDGTTitle() {
        return appSharedPrefs.getString(DGT_TITLE, "");}
    public void setDGTTitle(String value) { prefsEditor.putString(DGT_TITLE, value).commit(); }

    public int getDGTFormula() {
        return appSharedPrefs.getInt(DGT_FOMULAR, 0);}
    public void setDGTFomular(int value) { prefsEditor.putInt(DGT_FOMULAR, value).commit(); }

    public int getDGTOption() {
        return appSharedPrefs.getInt(DGT_OPTION, 0);}
    public void setDGTOption(int value) { prefsEditor.putInt(DGT_OPTION, value).commit(); }

    public String getDGTUnit() {
        return appSharedPrefs.getString(DGT_UNIT, "");}
    public void setDGTUnit(String value) { prefsEditor.putString(DGT_UNIT, value).commit(); }

    public int getDGTDispMode() {
        return appSharedPrefs.getInt(DGT_DISP_MODE, 0);}
    public void setDGTDispMode(int value) { prefsEditor.putInt(DGT_DISP_MODE, value).commit(); }

    public float getDGTMaxValue() {
        return appSharedPrefs.getFloat(DGT_MAX_VALUE, 0);}
    public void setDGTMaxValue(float value) { prefsEditor.putFloat(DGT_MAX_VALUE, value).commit(); }

    public float getDGTValue() {
        return appSharedPrefs.getFloat(DGT_VALUE, 0);}
    public void setDGTValue(float value) { prefsEditor.putFloat(DGT_VALUE, value).commit(); }

    // Reject Reason Titles
    public String getRejectReasonTitles() {
        return appSharedPrefs.getString(REJECT_REASON_TITLES, "");}
    public void setRejectReasonTitles(String value) { prefsEditor.putString(REJECT_REASON_TITLES, value).commit(); }


    // Stage1 Info
    public String getStage1Info() {
        return appSharedPrefs.getString(STAGE1_INFO, "");}
    public void setStage1Info(String value) { prefsEditor.putString(STAGE1_INFO, value).commit(); }

    // Stage2 Info
    public String getStage2Info() {
        return appSharedPrefs.getString(STAGE2_INFO, "");}
    public void setStage2Info(String value) { prefsEditor.putString(STAGE2_INFO, value).commit(); }

    // Stage3 Info
    public String getStage3Info() {
        return appSharedPrefs.getString(STAGE3_INFO, "");}
    public void setStage3Info(String value) { prefsEditor.putString(STAGE3_INFO, value).commit(); }

    // Stage4 Info
    public String getStage4Info() {
        return appSharedPrefs.getString(STAGE4_INFO, "");}
    public void setStage4Info(String value) { prefsEditor.putString(STAGE4_INFO, value).commit(); }

    // Stage5 Info
    public String getStage5Info() {
        return appSharedPrefs.getString(STAGE5_INFO, "");}
    public void setStage5Info(String value) { prefsEditor.putString(STAGE5_INFO, value).commit(); }

    // Stage6 Info
    public String getStage6Info() {
        return appSharedPrefs.getString(STAGE6_INFO, "");}
    public void setStage6Info(String value) { prefsEditor.putString(STAGE6_INFO, value).commit(); }

    // Stage7 Info
    public String getStage7Info() {
        return appSharedPrefs.getString(STAGE7_INFO, "");}
    public void setStage7Info(String value) { prefsEditor.putString(STAGE7_INFO, value).commit(); }

    // Stage8 Info
    public String getStage8Info() {
        return appSharedPrefs.getString(STAGE8_INFO, "");}
    public void setStage8Info(String value) { prefsEditor.putString(STAGE8_INFO, value).commit(); }

    // Stage9 Info
    public String getStage9Info() {
        return appSharedPrefs.getString(STAGE9_INFO, "");}
    public void setStage9Info(String value) { prefsEditor.putString(STAGE9_INFO, value).commit(); }

    // Panel Data Log
    public String getDataLogInfo() {
        return appSharedPrefs.getString(PANEL_DATA_LOG_INFO, "");}
    public void setDataLogInfo(String value) { prefsEditor.putString(PANEL_DATA_LOG_INFO, value).commit(); }

}