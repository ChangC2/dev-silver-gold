package com.cam8.mmsapp.model;

public class TankTemperatureData {
    float[] tankTemperature = new float[8];
    String machineStatus = "Uncategorized";
    boolean connPLCStatus = false;
    boolean connServerStatus = false;

    long timeInCycle = 0;
    long timeIdle = 0;
    long timeUnCate = 0;

    public TankTemperatureData() {}

    public void reset() {
        for (int i = 0; i < 8; i++) {
            tankTemperature[i] = 0;
        }
    }

    // Temperature Data
    public float getTempInfo(int tankIdx) {
        if (tankIdx >= 8)
            return 0;

        return tankTemperature[tankIdx];
    }

    public void setTempInfo(int tankIdx, float temp) {
        if (tankIdx >= 8)
            return;

        tankTemperature[tankIdx] = temp;
    }

    // Machine Status
    public String getMachineStatus() { return machineStatus; }
    public void setMachineStatus(String machineStatus) { this.machineStatus = machineStatus; }

    // PLC Connect Status
    public boolean isConnPLCStatus() { return connPLCStatus; }
    public void setConnPLCStatus(boolean connPLCStatus) { this.connPLCStatus = connPLCStatus; }

    // Server Connect Status
    public boolean isConnServerStatus() { return connServerStatus; }
    public void setConnServerStatus(boolean connServerStatus) { this.connServerStatus = connServerStatus; }

    // Incycle
    public long getTimeInCycle() { return timeInCycle; }
    public void setTimeInCycle(long timeInCycle) { this.timeInCycle = timeInCycle; }

    // Idle
    public long getTimeIdle() { return timeIdle; }
    public void setTimeIdle(long timeIdle) { this.timeIdle = timeIdle; }

    // UnCate
    public long getTimeUnCate() { return timeUnCate; }
    public void setTimeUnCate(long timeUnCate) { this.timeUnCate = timeUnCate; }
}
