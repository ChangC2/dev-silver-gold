package com.cam8.mmsapp;

import android.content.Context;

import com.cam8.mmsapp.model.Maintenance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MaintenanceTaskManager {
    public static MaintenanceTaskManager _instance;

    public static MaintenanceTaskManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new MaintenanceTaskManager(context);
        }
        return _instance;
    }

    Context mContext;
    AppSettings mAppSettings;
    String mCustomerId = "";
    String mMachineId = "";

    ArrayList<Maintenance> listMaintenances = new ArrayList<>();
    HashMap<String, Maintenance> mapMaintenances = new HashMap<>();

    Object mLock = new Object();

    public MaintenanceTaskManager(Context context) {
        mContext = context;
        mAppSettings = new AppSettings(mContext);

        // Load all save info
        String maintenanceInfo = mAppSettings.restoreMaintenanceTask();
        try {
            JSONObject jsonMaintenance = new JSONObject(maintenanceInfo);
            mCustomerId = jsonMaintenance.getString("customerId");
            mMachineId = jsonMaintenance.getString("machineId");

            JSONArray jsonArray = jsonMaintenance.getJSONArray("tasks");
            for (int i = 0; i < jsonArray.length(); i++) {
                Maintenance newItem = new Maintenance(jsonArray.getJSONObject(i));
                listMaintenances.add(newItem);
                mapMaintenances.put(newItem.getId(), newItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Update current task Incycle Time
    public void addInCycleTime(long duration) {

        synchronized (mLock) {
            for (int i = 0; i < listMaintenances.size(); i++) {
                ////// if (!listMaintenances.get(i).isTaskCompleted() && listMaintenances.get(i).getStart() > 0) {
                if (listMaintenances.get(i).getStart() > 0) {
                    listMaintenances.get(i).addInCycleTime(duration);
                }
            }
            saveStatus();
        }
    }

    // Get new Instance for the maintenance
    public Maintenance getNewMaintenanceItem() {
        synchronized (mLock) {
            for (int i = 0; i < listMaintenances.size(); i++) {
                Maintenance maintenance = listMaintenances.get(i);

                ///// if (maintenance.isTaskCompleted())
                //////    continue;

                if (maintenance.timeIsForMaintenance()) {
                    return maintenance;
                }
            }

            return null;
        }
    }

    // Get new Instance for the maintenance
    public Maintenance getMaintenanceInfoWithID(String id) {
        synchronized (mLock) {
            Maintenance oldItem = mapMaintenances.get(id);
            return oldItem;
        }
    }

    // Need to syncronize it using separate function
    public void updateLocalData() {
        synchronized (mLock) {
            saveStatus();
        }
    }

    // save current status internally
    private void saveStatus() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < listMaintenances.size(); i++) {
                Maintenance maintenance = listMaintenances.get(i);
                jsonArray.put(maintenance.toJsonData());
            }

            jsonObject.put("customerId", mAppSettings.getCustomerID());
            jsonObject.put("machineId", mAppSettings.getMachineName());
            jsonObject.put("tasks", jsonArray);

            mAppSettings.saveMaintenanceTask(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refreshDataList(ArrayList<Maintenance> newItems) {

        synchronized (mLock) {

            // If machine information was changed, then reset all task info
            if (!mCustomerId.equals(mAppSettings.getCustomerID()) || !mMachineId.equals(mAppSettings.getMachineName())) {
                listMaintenances.clear();
                mapMaintenances.clear();
            }

            // Update info
            for (int i = 0; i < newItems.size(); i++) {
                Maintenance newMaintenanceInfo = newItems.get(i);

                Maintenance oldItem = mapMaintenances.get(newMaintenanceInfo.getId());
                if (oldItem == null) {
                    // This is new item for the machine, we need to add in the list
                    listMaintenances.add(newMaintenanceInfo);
                    mapMaintenances.put(newMaintenanceInfo.getId(), newMaintenanceInfo);
                } else {
                    // This is original item
                    oldItem.updateInfo(newMaintenanceInfo);
                }
            }

            ArrayList<Maintenance> copyListmaintenances = new ArrayList<>(listMaintenances);

            // Filter remove items
            for (Maintenance oldInfo : copyListmaintenances) {
                if (!newItems.contains(oldInfo)) {
                    // This item doesn't contain in new list, need to remove in the local
                    listMaintenances.remove(oldInfo);
                    mapMaintenances.remove(oldInfo.getId());
                }
            }

            saveStatus();
        }
    }
}
