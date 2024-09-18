package Database;

import org.apache.http.util.TextUtils;

import java.io.Serializable;

/**
 * Created by Xian on 19-03-2020.
 */

public class GanttDataModel implements Serializable {
    public static final String TABLE_GANTT_DATA_NAME = "tbl_ganttData";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATEDAT = "created_at";
    public static final String COLUMN_MACHINEID = "machine_id";
    public static final String COLUMN_OPERATOR = "operator";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_END = "end";
    public static final String COLUMN_TIMESTAMP = "time_stamp";
    public static final String COLUMN_TIMESTAMPMS = "time_stamp_ms";
    public static final String COLUMN_JOBID = "job_id";
    public static final String COLUMN_BATT = "batt";
    public static final String COLUMN_OTHER = "other";

    //// insert into `tbl_ganttData`
    // (`created_at`, `machine_id`, `operator`, `status`, `color`, `start`, `end`, `time_stamp`, `time_stamp_ms`, `job_id`, `batt`, `other`)
    // values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

    private int id;
    private String createdAt;
    private String machineId;
    private String operator;
    private String status;
    private String color;
    private long start;
    private long end;
    private String timeStamp;
    private int timeStampMs;
    private String jobId;
    private int battLev;
    private String other;

    // Create table SQL query
    public static final String CREATE_GANTT_DATA_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_GANTT_DATA_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CREATEDAT + " TEXT,"
                    + COLUMN_MACHINEID + " TEXT,"
                    + COLUMN_OPERATOR + " TEXT,"
                    + COLUMN_STATUS + " TEXT,"
                    + COLUMN_COLOR + " TEXT,"
                    + COLUMN_START + " INTEGER,"
                    + COLUMN_END + " INTEGER,"
                    + COLUMN_TIMESTAMP + " TEXT,"
                    + COLUMN_TIMESTAMPMS + " INTEGER,"
                    + COLUMN_JOBID + " TEXT,"
                    + COLUMN_BATT + " INTEGER,"
                    + COLUMN_OTHER + " TEXT"
                    + ")";

    public GanttDataModel() {
    }

    public GanttDataModel(int id, String createdAt, String machineId, String operator, String status, String color, long start, long end, String timeStamp, int timeStampMs, String jobId, int battLev, String other) {
        this.id = id;
        this.createdAt = createdAt;
        this.machineId = machineId;
        this.operator = operator;
        this.status = status;
        this.color = color;
        this.start = start;
        this.end = end;
        this.timeStamp = timeStamp;
        this.timeStampMs = timeStampMs;
        this.jobId = jobId;
        this.battLev = battLev;
        this.other = other;
    }

    public GanttDataModel(String createdAt, String machineId, String operator, String status, String color, long start, long end, String timeStamp, int timeStampMs, String jobId, int battLev, String other) {
        this.createdAt = createdAt;
        this.machineId = machineId;
        this.operator = operator;
        this.status = status;
        this.color = color;
        this.start = start;
        this.end = end;
        this.timeStamp = timeStamp;
        this.timeStampMs = timeStampMs;
        this.jobId = jobId;
        this.battLev = battLev;
        this.other = other;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        if (TextUtils.isEmpty(createdAt)) {
            return "0";
        } else {
            return createdAt;
        }
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMachineId() {
        return machineId;
    }
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public long getStart() {
        return start;
    }
    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }
    public void setEnd(long end) {
        this.end = end;
    }

    public String getTimeStamp() { return timeStamp; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public int getTimeStampMs() { return timeStampMs; }
    public void setTimeStampMs(int timeStampMs) { this.timeStampMs = timeStampMs; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public int getBattLev() { return battLev; }
    public void setBattLev(int battLev) { this.battLev = battLev; }

    public String getOther() { return other; }
    public void setOther(String other) { this.other = other; }
}
