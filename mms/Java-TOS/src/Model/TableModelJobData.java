package Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableModelJobData {
    JobData jobData;

    private final SimpleIntegerProperty propTool;
    private final SimpleIntegerProperty propSection;
    private final SimpleIntegerProperty propChannel;

    private final SimpleStringProperty propComment;
    private final SimpleFloatProperty propTarget;

    private final SimpleFloatProperty propHighLimit;
    private final SimpleFloatProperty propWearLimit;
    private final SimpleFloatProperty propIdle;
    private final SimpleFloatProperty propLowLimitTime;

    public final SimpleBooleanProperty propAdaptiveEnable;
    private final SimpleBooleanProperty propMacroInterruptEnable;

    private final SimpleFloatProperty propLeadinFeedrate;
    private final SimpleFloatProperty propLeadinTrigger;
    private final SimpleBooleanProperty propLeadinEnable;

    private final SimpleFloatProperty propStartDelay;
    private final SimpleFloatProperty propHighLimitDelay;
    private final SimpleFloatProperty propWearLimitDelay;

    private final SimpleFloatProperty propAdaptiveMin;
    private final SimpleFloatProperty propAdaptiveMax;
    private final SimpleFloatProperty propAdaptiveWearLimit;
    private final SimpleFloatProperty propAdaptiveHighLimit;

    private final SimpleFloatProperty propFilter;

    private final SimpleFloatProperty propSensorScaleSend;

    private final SimpleFloatProperty propMonitorTime;

    public final SimpleBooleanProperty propWearLogicFeedrate;

    public TableModelJobData(JobData jobData) {
        this.jobData = jobData;

        this.propTool = new SimpleIntegerProperty(jobData.tool);
        this.propSection = new SimpleIntegerProperty(jobData.section);
        this.propChannel = new SimpleIntegerProperty(jobData.channel);

        this.propComment = new SimpleStringProperty(jobData.comment);
        this.propTarget = new SimpleFloatProperty(jobData.target);

        this.propHighLimit = new SimpleFloatProperty(jobData.highLimit);
        this.propWearLimit = new SimpleFloatProperty(jobData.wearLimit);

        this.propIdle = new SimpleFloatProperty(jobData.idle);

        this.propLowLimitTime = new SimpleFloatProperty(jobData.lowLimitTime);

        this.propAdaptiveEnable = new SimpleBooleanProperty(jobData.adaptiveEnable);
        this.propMacroInterruptEnable = new SimpleBooleanProperty(jobData.macroInterruptEnable);

        this.propLeadinFeedrate = new SimpleFloatProperty(jobData.leadInFeedrate);
        this.propLeadinTrigger = new SimpleFloatProperty(jobData.leadInTrigger);
        this.propLeadinEnable = new SimpleBooleanProperty(jobData.leadInEnable);

        this.propStartDelay = new SimpleFloatProperty(jobData.startDelay);
        this.propHighLimitDelay = new SimpleFloatProperty(jobData.highLimitDelay);
        this.propWearLimitDelay = new SimpleFloatProperty(jobData.wearLimitDelay);

        this.propAdaptiveMin = new SimpleFloatProperty(jobData.adaptiveMin);
        this.propAdaptiveMax = new SimpleFloatProperty(jobData.adaptiveMax);

        this.propAdaptiveWearLimit = new SimpleFloatProperty(jobData.adaptiveWearLimit);
        this.propAdaptiveHighLimit = new SimpleFloatProperty(jobData.adaptiveHighLimit);

        this.propFilter = new SimpleFloatProperty(jobData.filter);
        this.propSensorScaleSend = new SimpleFloatProperty(jobData.sensorScaleSend);

        this.propMonitorTime = new SimpleFloatProperty(jobData.monitorTime);

        this.propWearLogicFeedrate = new SimpleBooleanProperty(jobData.wearLogicFeedrate);
    }

    // Tool
    public int getPropTool() {
        return propTool.get();
    }
    public void setPropTool(int value) {
        propTool.set(value);
        jobData.tool = value;
    }

    // Section
    public int getPropSection() {
        return propSection.get();
    }
    public void setPropSection(int value) {
        propSection.set(value);
        jobData.section = value;
    }

    // Channel
    public int getPropChannel() {
        return propChannel.get();
    }
    public void setPropChannel(int value) {
        propChannel.set(value);
        jobData.channel = value;
    }

    // Comment
    public String getPropComment() {
        return propComment.get();
    }
    public void setPropComment(String value) {
        propComment.set(value);
        jobData.comment = value;
    }

    // Target
    public float getPropTarget() {
        return propTarget.get();
    }
    public void setPropTarget(float value) {
        propTarget.set(value);
        jobData.target = value;
    }

    // High Limit
    public float getPropHighLimit() {
        return propHighLimit.get();
    }
    public void setPropHighLimit(float value) {
        propHighLimit.set(value);
        jobData.highLimit = value;
    }

    // WearLimit
    public float getPropWearLimit() {
        return propWearLimit.get();
    }
    public void setPropWearLimit(float value) {
        propWearLimit.set(value);
        jobData.wearLimit = value;
    }

    // Idle
    public float getPropIdle() {
        return propIdle.get();
    }
    public void setPropIdle(float value) {
        propIdle.set(value);
        jobData.idle = value;
    }

    // LowLimitTime
    public float getPropLowLimitTime() {
        return propLowLimitTime.get();
    }
    public void setPropLowLimitTime(float value) {
        propLowLimitTime.set(value);
        jobData.lowLimitTime = value;
    }

    // AdaptiveEnable
    public boolean isPropAdaptiveEnable() {
        return propAdaptiveEnable.get();
    }
    public void setPropAdaptiveEnable(boolean value) {
        propAdaptiveEnable.set(value);
        jobData.adaptiveEnable = value;
    }

    // MacroInterruptEnable
    public boolean isPropMacroInterruptEnable() {
        return propMacroInterruptEnable.get();
    }
    public void setPropMacroInterruptEnable(boolean value) {
        propMacroInterruptEnable.set(value);
        jobData.macroInterruptEnable = value;
    }

    // LeadInFeedrate
    public float getPropLeadinFeedrate() {
        return propLeadinFeedrate.get();
    }
    public void setPropLeadinFeedrate(float value) {
        propLeadinFeedrate.set(value);
        jobData.leadInFeedrate = value;
    }

    // LeadInTrigger
    public float getPropLeadinTrigger() {
        return propLeadinTrigger.get();
    }
    public void setPropLeadinTrigger(float value) {
        propLeadinTrigger.set(value);
        jobData.leadInTrigger = value;
    }

    // LeadInEnable
    public boolean isPropLeadinEnable() {
        return propLeadinEnable.get();
    }
    public void setPropLeadinEnable(boolean value) {
        propLeadinEnable.set(value);
        jobData.leadInEnable = value;
    }

    // StartDelay
    public float getPropStartDelay() {
        return propStartDelay.get();
    }
    public void setPropStartDelay(float value) {
        propStartDelay.set(value);
        jobData.startDelay = value;
    }

    // HighLimitDelay
    public float getPropHighLimitDelay() {
        return propHighLimitDelay.get();
    }
    public void setPropHighLimitDelay(float value) {
        propHighLimitDelay.set(value);
        jobData.highLimitDelay = value;
    }

    // WearLimitDelay
    public float getPropWearLimitDelay() {
        return propWearLimitDelay.get();
    }
    public void setPropWearLimitDelay(float value) {
        propWearLimitDelay.set(value);
        jobData.wearLimitDelay = value;
    }

    // AdaptiveMin
    public float getPropAdaptiveMin() {
        return propAdaptiveMin.get();
    }
    public void setPropAdaptiveMin(float value) {
        propAdaptiveMin.set(value);
        jobData.adaptiveMin = value;
    }

    // AdaptiveMax
    public float getPropAdaptiveMax() {
        return propAdaptiveMax.get();
    }
    public void setPropAdaptiveMax(float value) {
        propAdaptiveMax.set(value);
        jobData.adaptiveMax = value;
    }

    // AdaptiveWearLimit
    public float getPropAdaptiveWearLimit() {
        return propAdaptiveWearLimit.get();
    }
    public void setPropAdaptiveWearLimit(float value) {
        propAdaptiveWearLimit.set(value);
        jobData.adaptiveWearLimit = value;
    }

    // AdaptiveHighLimit
    public float getPropAdaptiveHighLimit() {
        return propAdaptiveHighLimit.get();
    }
    public void setPropAdaptiveHighLimit(float value) {
        propAdaptiveHighLimit.set(value);
        jobData.adaptiveHighLimit = value;
    }


    // Filter
    public float getPropFilter() {
        return propFilter.get();
    }
    public void setPropFilter(float value) {
        propFilter.set(value);
        jobData.filter = value;
    }

    // SensorScale
    public float getPropSensorScaleSend() {
        return propSensorScaleSend.get();
    }
    public void setPropSensorScaleSend(float value) {
        propSensorScaleSend.set(value);
        jobData.sensorScaleSend = value;
    }

    // MonitorTime
    public float getPropMonitorTime() {
        return propMonitorTime.get();
    }
    public void setPropMonitorTime(float value) {
        propMonitorTime.set(value);
        jobData.monitorTime = value;
    }

    // Wear Logic - Feedrate
    public boolean getPropWearLogicFeedrate() { return propWearLogicFeedrate.get();}
    public void setPropWearLogicFeedrate(boolean value) {
        propWearLogicFeedrate.set(value);
        jobData.wearLogicFeedrate = value;
    }

}
