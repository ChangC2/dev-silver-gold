package Database;

import Utils.DateTimeUtils;
import Utils.PreferenceManager;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class RegimeUtils {
    public static void calcRegimes(RegimeData regimeData, boolean isForHst) {

        if (regimeData == null)
            return;

        regimeData.inCycleTime = PreferenceManager.getTimeIncycle();
        regimeData.unCatTime = PreferenceManager.getTimeUnCat();
        regimeData.idle1Time = PreferenceManager.getTimeIdle1();
        regimeData.idle2Time = PreferenceManager.getTimeIdle2();
        regimeData.idle3Time = PreferenceManager.getTimeIdle3();
        regimeData.idle4Time = PreferenceManager.getTimeIdle4();
        regimeData.idle5Time = PreferenceManager.getTimeIdle5();
        regimeData.idle6Time = PreferenceManager.getTimeIdle6();
        regimeData.idle7Time = PreferenceManager.getTimeIdle7();
        regimeData.idle8Time = PreferenceManager.getTimeIdle8();

        String jobDetails = PreferenceManager.getJobDetails();

        String aux1data = "";
        String aux2data = "";
        String aux3data = "";
        try {
            JSONObject jsonObject = new JSONObject(jobDetails);
            aux1data = jsonObject.optString("aux1data").replace("null", "");
            aux2data = jsonObject.optString("aux2data").replace("null", "");
            aux3data = jsonObject.optString("aux3data").replace("null", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        float aux1Value = 0;
        float aux2Value = 0;
        float aux3Value = 0;
        try {
            aux1Value = Float.parseFloat(aux1data);
        } catch (Exception e) {
        }

        try {
            aux2Value = Float.parseFloat(aux2data);
        } catch (Exception e) {
        }

        try {
            aux3Value = Float.parseFloat(aux3data);
        } catch (Exception e) {
        }

        long totalTimeInSec = 0;
        totalTimeInSec += regimeData.inCycleTime;
        totalTimeInSec += regimeData.unCatTime;
        totalTimeInSec += regimeData.idle1Time;
        totalTimeInSec += regimeData.idle2Time;
        totalTimeInSec += regimeData.idle3Time;
        totalTimeInSec += regimeData.idle4Time;
        totalTimeInSec += regimeData.idle5Time;
        totalTimeInSec += regimeData.idle6Time;
        totalTimeInSec += regimeData.idle7Time;
        totalTimeInSec += regimeData.idle8Time;

        totalTimeInSec /= 1000;

        regimeData.inCycleTime /= 1000;
        regimeData.unCatTime /= 1000;
        regimeData.idle1Time /= 1000;
        regimeData.idle2Time /= 1000;
        regimeData.idle3Time /= 1000;
        regimeData.idle4Time /= 1000;
        regimeData.idle5Time /= 1000;
        regimeData.idle6Time /= 1000;
        regimeData.idle7Time /= 1000;
        regimeData.idle8Time /= 1000;

        long secFromMidnight = DateTimeUtils.getTimeInSecondsFromMidnight();

        // TotalTime = $inCycleT + $uncatT + $rt[0] + $rt[1] + $rt[2] + $rt[3] + $rt[4] + $rt[5] +$rt[6] + $rt[7] + $rt[8]
        // posstime = DateDiff("s","00:00:00", Time)
        // $utilization = ($inCycleT / possTime)*100
        // $offlineT = posstime - Totaltime

        regimeData.utilization = (regimeData.inCycleTime * 100.0f / secFromMidnight);
        regimeData.offlineT = Math.max(0, secFromMidnight - totalTimeInSec);

        int goodParts = PreferenceManager.getGoodParts();
        int badParts = PreferenceManager.getBadParts();

        if (!isForHst) {
            goodParts += PreferenceManager.getShiftGoodParts();
            badParts += PreferenceManager.getShiftBadParts();
        }

        int totalParts = goodParts + badParts;

        // ——Availability = ‘In Cycle Time’ / ‘Planned Production Time’
        // —— Performance = (‘Planned Cycle Time’ * ‘Good Parts’)/ ‘In Cycle Time’
        // —— Quality = ‘Good Parts’ / (‘Good Parts’ + ‘Bad Parts’)
        long plannedProductionTimeInSec = PreferenceManager.getPlannedProductionTime() / 1000;
        regimeData.Availablity = regimeData.inCycleTime * 100.0f / plannedProductionTimeInSec;

        regimeData.quality = 0;
        if (totalParts > 0) {
            regimeData.quality = goodParts * 100.0f / totalParts;
        }
        regimeData.performance = 0;
        regimeData.oee = 0;
        long targetCycleTimeInSec = PreferenceManager.getTargetCycleTimeSeconds(); // This is second value, no need to convert
        float partsPerCycle = PreferenceManager.getPartsPerCycle();


        if (!TextUtils.isEmpty(PreferenceManager.getJobID())) {

            // Calc Real Performance in case of jobID is present
            if (regimeData.inCycleTime > 0 || targetCycleTimeInSec > 0) {
                if (regimeData.inCycleTime == 0) {
                    regimeData.performance = 0;
                } else {
                    // Original Calc formular
                    // regimeData.performance = (targetCycleTimeInSec / partsPerCycle) * totalParts * 100.0f / regimeData.inCycleTime;
                    regimeData.performance = targetCycleTimeInSec * totalParts * 100.0f / regimeData.inCycleTime;
                }

                if (regimeData.performance > 200) {
                    regimeData.performance = 200;
                }
            }

            /* Now we use updated Percentage Values */
            if (regimeData.performance == 0 && regimeData.quality == 0) {
                regimeData.oee = regimeData.Availablity;
            } else if (regimeData.performance == 0) {
                regimeData.oee = regimeData.Availablity * regimeData.quality / 100;
            } else if (regimeData.quality == 0) {
                regimeData.oee = regimeData.Availablity * regimeData.performance / 100;
            } else {
                regimeData.oee = regimeData.Availablity * regimeData.performance * regimeData.quality / 10000;
            }
        }

        regimeData.squareInch = ((aux1Value + aux2Value) * aux3Value * (goodParts + badParts));
    }
}
