package com.cam8.icsapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppSettings {
    private static final String APP_SHARED_PREFS = "kinetTx_prefs";
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

    // Bluetooth Settings
    private static final String MACHINE_NAME = "machine_name";
    private static final String BT_DEVICE_NAME = "bt_device_name";
    private static final String BT_DEVICE_ADDR = "bt_device_addr";

    private String USE_PRESSUE_PSI = "PSI_PRESSUE_UNIT";

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


    public void setDeviceIdSet(boolean isAdded) {
        prefsEditor.putBoolean(DEVICE_ID_SET, isAdded);
        prefsEditor.apply();
    }

    public boolean isDeviceIdSet() {
        return appSharedPrefs.getBoolean(DEVICE_ID_SET, false);
    }

    public void setDeviceId(String deviceId) {
        if (!isDeviceIdSet()) {
            setDeviceIdSet(true);
            prefsEditor.putString(DEVICE_ID, deviceId);
            prefsEditor.apply();
        }
    }

    public String getDeviceId() {
        return appSharedPrefs.getString(DEVICE_ID, "");
    }

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

    public void setUnitPSI(boolean value) {
        prefsEditor.putBoolean(USE_PRESSUE_PSI, value);
        prefsEditor.apply();
    }

    public boolean isUnitPSI() {
        return appSharedPrefs.getBoolean(USE_PRESSUE_PSI, true);
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
}