package com.cam8.mmsapp.model;

import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShiftTimeManager {

    private static ShiftTimeManager _instance;

    public static ShiftTimeManager getInstance() {
        if (_instance == null)
            _instance = new ShiftTimeManager();
        return _instance;
    }

    private ArrayList<ShiftTime> shiftTimes = new ArrayList<>();

    // Refresh Shift Time List
    public void setShiftTime(ArrayList<ShiftTime> newShifts) {
        synchronized (shiftTimes) {
            this.shiftTimes.clear();

            // Check Data
            if (newShifts == null || newShifts.isEmpty())
                return;

            // Sort Shift Data
            Collections.sort(newShifts, new Comparator<ShiftTime>() {
                public int compare(ShiftTime o1, ShiftTime o2) {
                    if (o1.getStart() > o2.getStart()) {
                        return 1;
                    } else if (o1.getStart() < o2.getStart()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

            // In the sorted array, if start time of an interval
            // is less than end of previous interval, then there
            // is an overlap
            int n = newShifts.size();
            for(int i = 1; i < n; i++) {
                // Check bad data
                if (newShifts.get(i - 1).end > newShifts.get(i).start) {
                    //  |1111111                   |
                    //  |     222222               |
                    //  |              333333      |
                    return;
                }
            }

            // Check Exception of crossing the midnight
            if (n > 1) {
                // Check bad data
                for(int i = 0; i < n - 1; i++) {
                    // If the item crossing the Midnight is not the last item, this is bad data
                    if (newShifts.get(i).end < newShifts.get(i).start) {
                        //  |1111111      1111111111111|
                        //  |                  22222   |  Or
                        //  |2222                 22222|
                        return;
                    }
                }

                // Check The Last Item Time
                ShiftTime firstItem = newShifts.get(0);
                ShiftTime lastItem = newShifts.get(n - 1);
                if (lastItem.end < lastItem.start) {
                    if (lastItem.end > firstItem.start) {
                        //  |      1111111             |
                        //  |222222222              222|
                        return;
                    }
                }
            }

            Log.e("ShiftData", "Got New Shifts");
            this.shiftTimes.addAll(newShifts);
        }
    }

    public ArrayList<ShiftTime> getShiftTimes() { return shiftTimes; }

    // Get Current Shift Info
    public ShiftTime getCurrentShiftTime() {
        return getShiftTime(System.currentTimeMillis());
    }

    // Get Shift Info from time stamp
    public ShiftTime getShiftTime(long timeInMiliseconds) {
        Date date = new Date(timeInMiliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);

        synchronized (shiftTimes) {
            ShiftTime shiftInfo = null;
            for (ShiftTime item : shiftTimes) {
                if (item.isTimeInTheShift(dateFormatted)) {
                    shiftInfo = item;
                    break;
                }
            }

            return shiftInfo;
        }
    }
}
