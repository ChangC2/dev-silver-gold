package com.cam8.mmsapp.model;

import android.text.TextUtils;

import com.cam8.mmsapp.AppSettings;
import com.cam8.mmsapp.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ShiftDataModel {

    public static final String TABLE_SHIFT_DATA_NAME = "tbl_shiftData";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SHIFTID = "shift_id";

    public static final String COLUMN_SHIFT_STARTTIME = "shift_start_time";
    public static final String COLUMN_SHIFT_STOPTIME = "shift_stop_time";

    public static final String COLUMN_JOBID = "job_id";
    public static final String COLUMN_JOBSEQNO = "job_sequenceno";      // Added in db version 6
    public static final String COLUMN_MACHINE = "machine";
    public static final String COLUMN_OPERATOR = "operator";
    public static final String COLUMN_USERID = "user_id";               // Added in db version 6

    public static final String COLUMN_STARTTIME = "start_time";
    public static final String COLUMN_STOPTIME = "stop_time";

    public static final String COLUMN_UTILIZATION = "utilization";
    public static final String COLUMN_OFFLINE = "offline";

    public static final String COLUMN_OEE = "oee";
    public static final String COLUMN_AVAILABILITY = "availability";
    public static final String COLUMN_PERFORMANCE = "performance";
    public static final String COLUMN_QUALITY = "quality";

    public static final String COLUMN_GOODS = "goods";
    public static final String COLUMN_BADS = "bads";

    public static final String COLUMN_INCYCLE = "incycle";
    public static final String COLUMN_UNCAT = "uncat";
    public static final String COLUMN_R1T = "r1t";
    public static final String COLUMN_R2T = "r2t";
    public static final String COLUMN_R3T = "r3t";
    public static final String COLUMN_R4T = "r4t";
    public static final String COLUMN_R5T = "r5t";
    public static final String COLUMN_R6T = "r6t";
    public static final String COLUMN_R7T = "r7t";
    public static final String COLUMN_R8T = "r8t";

    public static final String COLUMN_AUXDATA1 = "aux_data1";
    public static final String COLUMN_AUXDATA2 = "aux_data2";
    public static final String COLUMN_AUXDATA3 = "aux_data3";

    public static final String COLUMN_COMPLETED = "completed";
    public static final String COLUMN_UPDATED = "updated";

    public static final String COLUMN_REWORK = "rework";
    public static final String COLUMN_SETUP = "setup";

    public static final String COLUMN_PARTIDS = "partids";

    public static final String COLUMN_EXT1 = "ext1";    // Shift Setting time, if current shift is in 08:00-16:00, then 08:00-16:00 is saved
    public static final String COLUMN_EXT2 = "ext2";    // TargetCycleTime
    public static final String COLUMN_EXT3 = "ext3";    // Planned Production Time of this shift

    // Create table SQL query
    public static final String CREATE_SHIFT_DATA_TABLE =
            "CREATE TABLE " + TABLE_SHIFT_DATA_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SHIFTID + " INTEGER,"

                    + COLUMN_SHIFT_STARTTIME + " INTEGER,"
                    + COLUMN_SHIFT_STOPTIME + " INTEGER,"

                    + COLUMN_JOBID + " TEXT,"
                    + COLUMN_JOBSEQNO + " TEXT,"            // Added newly in db version 6
                    + COLUMN_MACHINE + " TEXT,"
                    + COLUMN_OPERATOR + " TEXT,"
                    + COLUMN_USERID + " TEXT,"              // Added newly in db version 6

                    + COLUMN_STARTTIME + " INTEGER,"
                    + COLUMN_STOPTIME + " INTEGER,"

                    + COLUMN_UTILIZATION + " REAL,"
                    + COLUMN_OFFLINE + " REAL,"

                    + COLUMN_OEE + " REAL,"
                    + COLUMN_AVAILABILITY + " REAL,"
                    + COLUMN_PERFORMANCE + " REAL,"
                    + COLUMN_QUALITY + " REAL,"

                    + COLUMN_GOODS + " INTEGER,"
                    + COLUMN_BADS + " INTEGER,"

                    + COLUMN_INCYCLE + " INTEGER,"
                    + COLUMN_UNCAT + " INTEGER,"
                    + COLUMN_R1T + " INTEGER,"
                    + COLUMN_R2T + " INTEGER,"
                    + COLUMN_R3T + " INTEGER,"
                    + COLUMN_R4T + " INTEGER,"
                    + COLUMN_R5T + " INTEGER,"
                    + COLUMN_R6T + " INTEGER,"
                    + COLUMN_R7T + " INTEGER,"
                    + COLUMN_R8T + " INTEGER,"

                    + COLUMN_AUXDATA1 + " REAL,"
                    + COLUMN_AUXDATA2 + " REAL,"
                    + COLUMN_AUXDATA3 + " REAL,"

                    + COLUMN_COMPLETED + " INTEGER,"
                    + COLUMN_UPDATED + " INTEGER,"

                    + COLUMN_REWORK + " TEXT,"
                    + COLUMN_SETUP + " TEXT,"
                    + COLUMN_PARTIDS + " TEXT,"         // Part IDs, added in db version 7

                    + COLUMN_EXT1 + " TEXT,"            // Shift Settings
                    + COLUMN_EXT2 + " TEXT,"            // Target Cycle Time
                    + COLUMN_EXT3 + " TEXT"             // Planned Production Time
                    + ")";

    private long id = 0;                // Local DB ID
    private long shiftId = 0;           // Server ID

    private int shiftStartTime = 0;     // Server Setting Start Time 08:00  = 8 * 60                        Not Used
    private int shiftStopTime = 0;      // Server Setting Stop Time 16:30 = 16 * 60 + 30                    Not Used

    private String jobID = "";
    private String jobSequenceNo = "";
    private String machine = "";
    private String operator = "Unattended";
    private String userID = "0";

    private long startTime = 0;         // Real Shift Start Timestamp in Milis
    private long stopTime = 0;          // Real Shift Stop Timestamp in Milis

    private long shiftStartSetting = 0; // Shift Start Time Setting in Milis    Daily Offset Time value     Not Used
    private long shiftEndSetting = 0;   // Shift Stop Time Setting in Milis                                 Not Used
    private String shiftSetting = "";   // Shift Time Setting String

    private float utilization = 0;
    private long offLineT = 0;

    private float oee = 0;
    private float availablity = 0;
    private float performance = 0;
    private float quality = 0;

    private int goodParts = 0;
    private int badParts = 0;

    private int prevGoodParts = 0;      // Previous Good Parts
    private int prevBadParts = 0;       // Previous Bad Parts

    /*
    [0] uncat
    [1] Incycle
    [2] r1t
    [3] r2t
    [4] r3t
    [5] r4t
    [6] r5t
    [7] r6t
    [8] r7t
    [9] r8t
    * */
    private long elapsedMiliseconds[] = new long[10];

    private int statusRework = 0;
    private int statusSetup = 0;

    private float auxData1 = 0;
    private float auxData2 = 0;
    private float auxData3 = 0;

    // Time Settings
    private long targetCycleTimeSeconds = 0;
    private long plannedProductionTime = 0;

    private boolean isCompleted = false;    // Completed Status
    private boolean isUpdated = false;      // New Data Status

    private String partIds = "";

    public ShiftDataModel() {
        resetData();
    }

    public void resetData() {
        id = 0;
        shiftId = 0;

        shiftStartTime = 0;     // Server Setting Start Time 08:00:00
        shiftStopTime = 0;      // Server Setting Stop Time 16:00:00

        jobID = "";
        jobSequenceNo = "";

        machine = "";

        operator = "Unattended";
        userID = "0";

        startTime = 0;     // Shift Start Time
        stopTime = 0;      // Shift Stop Time

        // Shift Settings info
        shiftStartSetting = 0;
        shiftEndSetting = 0;
        shiftSetting = "";

        // OEE Utilizations Value
        utilization = 0;
        offLineT = 0;

        oee = 0;
        availablity = 0;
        performance = 0;
        quality = 0;

        goodParts = 0;
        badParts = 0;

        // Base value for calculating the offset
        prevGoodParts = 0;
        prevBadParts = 0;

        // Time Miliseconds
        for (int i = 0; i < 10; i++) {
            elapsedMiliseconds[i] = 0;
        }

        statusRework = 0;
        statusSetup = 0;

        auxData1 = 0;
        auxData2 = 0;
        auxData3 = 0;

        targetCycleTimeSeconds = 0;
        plannedProductionTime = 0;

        isCompleted = false;
        isUpdated = false;

        partIds = "";
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getShiftId() { return shiftId; }
    public void setShiftId(long shiftId) { this.shiftId = shiftId; }

    public int getShiftStartTime() { return shiftStartTime; }
    public void setShiftStartTime(int shiftStartTime) { this.shiftStartTime = shiftStartTime; }

    public int getShiftStopTime() { return shiftStopTime; }
    public void setShiftStopTime(int shiftStopTime) { this.shiftStopTime = shiftStopTime; }

    public String getJobID() { return jobID; }
    public void setJobID(String jobID) { this.jobID = jobID; }

    public String getJobSequenceNo() { return jobSequenceNo; }
    public void setJobSequenceNo(String jobSequenceNo) { this.jobSequenceNo = jobSequenceNo; }

    public String getMachine() { return machine; }
    public void setMachine(String machine) { this.machine = machine; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }

    public long getStopTime() { return stopTime; }
    public void setStopTime(long stopTime) { this.stopTime = stopTime; }

    // Shift Time Settings --------------------------------------------------------------------------------
    public long getShiftStartSetting() { return shiftStartSetting; }
    public void setShiftStartSetting(long shiftStartSetting) { this.shiftStartSetting = shiftStartSetting; }

    public long getShiftEndSetting() { return shiftEndSetting; }
    public void setShiftEndSetting(long shiftEndSetting) { this.shiftEndSetting = shiftEndSetting; }

    public String getShiftSetting() { return shiftSetting; }
    public void setShiftSetting(String shiftSetting) { this.shiftSetting = shiftSetting; }
    // ----------------------------------------------------------------------------------------------------

    public float getUtilization() { return utilization; }
    public void setUtilization(float utilization) { this.utilization = utilization; }

    public long getOffLineT() { return offLineT; }
    public void setOffLineT(long offLineT) { this.offLineT = offLineT; }

    public float getOee() { return oee; }
    public void setOee(float oee) { this.oee = oee; }

    public float getAvailablity() { return availablity; }
    public void setAvailablity(float availablity) { this.availablity = availablity; }

    public float getPerformance() { return performance; }
    public void setPerformance(float performance) { this.performance = performance; }

    public float getQuality() { return quality; }
    public void setQuality(float quality) { this.quality = quality; }

    public int getGoodParts() { return goodParts; }
    public void setGoodParts(int goodParts) { this.goodParts = goodParts; }

    public int getBadParts() { return badParts; }
    public void setBadParts(int badParts) { this.badParts = badParts; }

    public int getPrevGoodParts() { return prevGoodParts; }
    public void setPrevGoodParts(int prevGoodParts) { this.prevGoodParts = prevGoodParts; }

    public int getPrevBadParts() { return prevBadParts; }
    public void setPrevBadParts(int prevBadParts) { this.prevBadParts = prevBadParts; }

    public long getElapsedTimeInMils(int idx) {
        if (idx >= elapsedMiliseconds.length)
            return 0;
        return elapsedMiliseconds[idx];
    }
    public void setElapsedTimeInMils(int idx, long val) {
        if (idx >= elapsedMiliseconds.length)
            return;
        elapsedMiliseconds[idx] = val;
    }

    public float getAuxData1() { return auxData1; }
    public void setAuxData1(float auxData1) { this.auxData1 = auxData1; }

    public float getAuxData2() { return auxData2; }
    public void setAuxData2(float auxData2) { this.auxData2 = auxData2; }

    public float getAuxData3() { return auxData3; }
    public void setAuxData3(float auxData3) { this.auxData3 = auxData3; }

    public long getTargetCycleTimeSeconds() { return targetCycleTimeSeconds; }
    public void setTargetCycleTimeSeconds(long targetCycleTimeSeconds) { this.targetCycleTimeSeconds = targetCycleTimeSeconds; }

    public long getPlannedProductionTime() { return plannedProductionTime; }
    public void setPlannedProductionTime(long plannedProductionTime) { this.plannedProductionTime = plannedProductionTime; }

    public void setStatusRework(int statusRework) { this.statusRework = statusRework; }
    public int getStatusRework() { return statusRework; }

    public void setStatusSetup(int statusSetup) { this.statusSetup = statusSetup; }
    public int getStatusSetup() { return statusSetup; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public boolean isUpdated() { return isUpdated; }
    public void setUpdated(boolean updated) { isUpdated = updated; }

    public String getPartIds() { return partIds; }
    public void setPartIds(String partIds) { this.partIds = partIds; }

    public void addPartId(String newPartId) {
        // Check Part ID exists or not
        String[] partIdArray = partIds.split(",");
        boolean found = false;
        for (String partId : partIdArray) {
            if (partId.trim().equals(newPartId)) {
                found = true;
                break;
            }
        }

        if (!found) {
            if (TextUtils.isEmpty(partIds)) {
                partIds += newPartId;
            } else {
                partIds += "," + newPartId;
            }
        }
    }

    public void calcRegime(AppSettings appSettings) {
        if (appSettings == null)
            return;

        long totalTimeInSec = 0;
        for (int i = 0; i < 10; i++) {
            totalTimeInSec += elapsedMiliseconds[i];
        }

        long secFromMidnight = Math.max(stopTime - startTime, 1);

        // TotalTime = $inCycleT + $uncatT + $rt[0] + $rt[1] + $rt[2] + $rt[3] + $rt[4] + $rt[5] +$rt[6] + $rt[7] + $rt[8]
        // posstime = DateDiff("s","00:00:00", Time)
        // $utilization = ($inCycleT / possTime)*100
        // $offlineT = posstime - Totaltime

        utilization = (elapsedMiliseconds[1] * 100.0f / secFromMidnight);
        offLineT = Math.max(0, secFromMidnight - totalTimeInSec);

        int totalParts = goodParts + badParts;

        // ——Availability = ‘In Cycle Time’ / ‘Planned Production Time’
        // —— Performance = (‘Planned Cycle Time’ * ‘Good Parts’)/ ‘In Cycle Time’
        // —— Quality = ‘Good Parts’ / (‘Good Parts’ + ‘Bad Parts’)

        // Availability 1
        //long plannedProductionTimeInSec = appSettings.getPlannedProductionTime();
        //availablity = elapsedMiliseconds[1] * 100.0f / plannedProductionTimeInSec;

        // Availability 2
        long passedTime = stopTime - DateUtil.getMidnightTimeStampInMilis() - shiftStartSetting;
        availablity = elapsedMiliseconds[1] * 100.0f / passedTime;

        quality = 0;
        if (totalParts > 0) {
            quality = goodParts * 100.0f / totalParts;
        }

        performance = 0;
        oee = 0;

        if (!TextUtils.isEmpty(jobID)) {

            // Calc Real Performance in case of jobID is present
            if (elapsedMiliseconds[1] > 0) {
                performance = targetCycleTimeSeconds * totalParts * 100.0f / elapsedMiliseconds[1];
            }

            if (performance > 200) {
                performance = 200;
            }

            /* Now we use updated Percentage Values */
            if (performance == 0 && quality == 0) {
                oee = availablity;
            } else if (performance == 0) {
                oee = availablity * quality / 100;
            } else if (quality == 0) {
                oee = availablity * performance / 100;
            } else {
                oee = availablity * performance * quality / 10000;
            }
        }
    }
}
