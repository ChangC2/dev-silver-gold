package Model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SystemAlertData {
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;

    private final SimpleIntegerProperty tool;
    private final SimpleIntegerProperty section;
    private final SimpleIntegerProperty channel;

    private final SimpleStringProperty alarmType;
    private final SimpleFloatProperty elapsedMonitorTime;


    public SystemAlertData() {
        this.date = new SimpleStringProperty("");
        this.time = new SimpleStringProperty("");

        this.tool = new SimpleIntegerProperty(0);
        this.section = new SimpleIntegerProperty(0);
        this.channel = new SimpleIntegerProperty(0);

        this.alarmType = new SimpleStringProperty("");

        this.elapsedMonitorTime = new SimpleFloatProperty(0);
    }

    private SystemAlertData(String date, String time, int tool, int section, int channel, String alarmType, float elapsedMonitorTime) {
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);

        this.tool = new SimpleIntegerProperty(tool);
        this.section = new SimpleIntegerProperty(section);
        this.channel = new SimpleIntegerProperty(channel);

        this.alarmType = new SimpleStringProperty(alarmType);
        this.elapsedMonitorTime = new SimpleFloatProperty(elapsedMonitorTime);
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

    public String getAlarmType() { return alarmType.get(); }
    public void setAlarmType(String val) { alarmType.set(val);}

    public float getElapsedMonitorTime() { return elapsedMonitorTime.get(); }
    public void setElapsedMonitorTime(float val) { elapsedMonitorTime.set(val); }

}
