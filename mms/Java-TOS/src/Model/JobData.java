package Model;

import Utils.PreferenceManager;

public class JobData {

    public int jobID = 0;
    public int tool = 0;
    public int section = 0;
    public int channel = 0;
    public String comment = "";
    public float target = 0;
    public float highLimit = 0;
    public float wearLimit = 0;
    public float idle = 0;
    public float lowLimitTime = 0;
    public boolean adaptiveEnable= false;
    public boolean macroInterruptEnable = true;
    public float leadInFeedrate = 0;
    public float leadInTrigger = 0;
    public boolean leadInEnable = true;
    public float startDelay = 0;
    public float highLimitDelay = 0;
    public float wearLimitDelay = 0;
    public float adaptiveMin = 0;
    public float adaptiveMax = 0;
    public float adaptiveWearLimit = 0;
    public float adaptiveHighLimit = 0;
    public float filter = 0;
    public float sensorScaleSend = 0;
    public float monitorTime = 0;


    /*Make change to high/wear limit logic when adaptive=1
    Current logic= when adaptive=1 then High/Wear limit alarms are generated when feedrate<high/wear limit.
    Want to give option for each TSC to choose this logic or traditional logic: High/Wear limit alarms generated if HP>limits.
    Basically want to able to select which logic to use from the TSC data file, for each tool.
    So need to add another field in tool data file, just a checkbox.
    When save tool file, it should default to "traditional" logic.*/
    public boolean wearLogicFeedrate = false;

    public JobData() {
        jobID = 1;
        tool = 0;
        section = 0;
        channel = 0;
        comment = "";
        target = 0;
        highLimit = 0;
        wearLimit = 0;
        idle = 0;
        lowLimitTime = 0;
        adaptiveEnable= PreferenceManager.isAdaptiveEnabled();
        macroInterruptEnable = PreferenceManager.isMacroInterruptEnabled();
        leadInFeedrate = 0;
        leadInTrigger = PreferenceManager.getDefaultLeadInTrigger();
        leadInEnable = PreferenceManager.isLeadInFREnabled();
        startDelay = 0;
        highLimitDelay = PreferenceManager.getDefaultHighLimitDelay();
        wearLimitDelay = PreferenceManager.getDefaultWearLimitDelay();
        adaptiveMin = PreferenceManager.getAdaptiveMin();
        adaptiveMax = PreferenceManager.getAdaptiveMax();
        adaptiveWearLimit = PreferenceManager.getAdaptiveWearLimit();
        adaptiveHighLimit = PreferenceManager.getAdaptiveHighLimit();
        filter = PreferenceManager.getDefaultFilter();
        sensorScaleSend = 0;
        monitorTime = 0;
    }

}
