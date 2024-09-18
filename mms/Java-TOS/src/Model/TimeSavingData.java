package Model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TimeSavingData {
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleIntegerProperty tool;
    private final SimpleIntegerProperty section;
    private final SimpleIntegerProperty channel;
    private final SimpleFloatProperty learnedMonitorTime;
    private final SimpleFloatProperty elapsedMonitorTime;
    private final SimpleFloatProperty timeSavings;
    private final SimpleFloatProperty timeSavingsPer;

    public TimeSavingData() {
        this.date = new SimpleStringProperty("");
        this.time = new SimpleStringProperty("");

        this.tool = new SimpleIntegerProperty(0);
        this.section = new SimpleIntegerProperty(0);
        this.channel = new SimpleIntegerProperty(0);

        this.learnedMonitorTime = new SimpleFloatProperty(0);
        this.elapsedMonitorTime = new SimpleFloatProperty(0);
        this.timeSavings = new SimpleFloatProperty(0);
        this.timeSavingsPer = new SimpleFloatProperty(0);
    }

    private TimeSavingData(String date, String time, int tool, int section, int channel, float learnedMonitorTime, float elapsedMonitorTime, float timeSavings, float timeSavingsPer) {
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);

        this.tool = new SimpleIntegerProperty(tool);
        this.section = new SimpleIntegerProperty(section);
        this.channel = new SimpleIntegerProperty(channel);

        this.learnedMonitorTime = new SimpleFloatProperty(learnedMonitorTime);
        this.elapsedMonitorTime = new SimpleFloatProperty(elapsedMonitorTime);
        this.timeSavings = new SimpleFloatProperty(timeSavings);
        this.timeSavingsPer = new SimpleFloatProperty(timeSavingsPer);
    }

    public String getDate() { return date.get(); }
    public void setDate(String val) { date.set(val); }

    public String getTime() { return time.get(); }
    public void setTime(String val) { time.set(val); }

    public int getTool() { return tool.get(); }
    public void setTool(int val) { tool.set(val); }

    public int getSection() { return section.get(); }
    public void setSection(int val) { section.set(val); }

    public int getChannel() { return channel.get(); }
    public void setChannel(int val) { channel.set(val); }

    public float getElapsedMonitorTime() { return elapsedMonitorTime.get(); }
    public void setElapsedMonitorTime(float val) { elapsedMonitorTime.set(val); }

    public float getLearnedMonitorTime() { return learnedMonitorTime.get(); }
    public void setLearnedMonitorTime(float val) { learnedMonitorTime.set(val); }

    public float getTimeSavings() { return timeSavings.get(); }
    public void setTimeSavings(float val) { timeSavings.set(val); }

    public float getTimeSavingsPer() { return timeSavingsPer.get(); }
    public void setTimeSavingsPer(float val) { timeSavingsPer.set(val); }

}
