
package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBTableGanttData {

    public static long insertGanttData(String createdAt, String machineId, String operator, String status, String color,
                                long start, long end, String timeStamp, int timeStampMs, String jobId, int battLevel, String other) {

        //Log.e("report", String.format("%s, %s, %s, %s, %s, %d, %d", createdAt, machineId, operator, status, timeStamp, start, end));
        if (start == 0 || end == 0) {
            // Invalid Records
            return -1;
        }

        if (DBConnection.connect == null) {
            return -1;
        }
        try {

            PreparedStatement preparedStatement = DBConnection.connect.prepareStatement("insert into tbl_ganttData " +
                    "(created_at, machine_id, operator, status, color, start, end, time_stamp, time_stamp_ms, job_id, batt, other) " +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setString(1, createdAt);
            preparedStatement.setString(2, machineId);
            preparedStatement.setString(3, operator);
            preparedStatement.setString(4, status);
            preparedStatement.setString(5, color);
            preparedStatement.setLong(6, start);
            preparedStatement.setLong(7, end);
            preparedStatement.setString(8, timeStamp);
            preparedStatement.setLong(9, timeStampMs);
            preparedStatement.setString(10, jobId);
            preparedStatement.setInt(11, battLevel);
            preparedStatement.setString(12, other);

            preparedStatement.executeUpdate();
            //get generated key
            ResultSet rs = preparedStatement.getGeneratedKeys();
            long id = -1;
            if (rs.next()) {
                id = rs.getLong(1);
            }
            return (int)id;
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return -1;
        }
    }

    public static List<GanttDataModel> getAllGanttData() {

        List<GanttDataModel> sugarList = new ArrayList<>();
        if (DBConnection.connect == null) {
            return sugarList;
        }

        // Select All Query
        String selectQuery = "SELECT * FROM " + GanttDataModel.TABLE_GANTT_DATA_NAME;
        try {
            Statement statement = DBConnection.connect.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                try {
                    GanttDataModel ganttObj = new GanttDataModel();
                    ganttObj.setId(resultSet.getInt(GanttDataModel.COLUMN_ID));
                    ganttObj.setCreatedAt(resultSet.getString(GanttDataModel.COLUMN_CREATEDAT));
                    ganttObj.setMachineId(resultSet.getString(GanttDataModel.COLUMN_MACHINEID));
                    ganttObj.setOperator(resultSet.getString(GanttDataModel.COLUMN_OPERATOR));
                    ganttObj.setStatus(resultSet.getString(GanttDataModel.COLUMN_STATUS));
                    ganttObj.setColor(resultSet.getString(GanttDataModel.COLUMN_COLOR));
                    ganttObj.setStart(resultSet.getLong(GanttDataModel.COLUMN_START));
                    ganttObj.setEnd(resultSet.getLong(GanttDataModel.COLUMN_END));
                    ganttObj.setTimeStamp(resultSet.getString(GanttDataModel.COLUMN_TIMESTAMP));
                    ganttObj.setTimeStampMs(resultSet.getInt(GanttDataModel.COLUMN_TIMESTAMPMS));
                    ganttObj.setJobId(resultSet.getString(GanttDataModel.COLUMN_JOBID));
                    ganttObj.setBattLev(resultSet.getInt(GanttDataModel.COLUMN_BATT));
                    ganttObj.setOther(resultSet.getString(GanttDataModel.COLUMN_OTHER));

                    sugarList.add(ganttObj);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return sugarList;
        }
        return sugarList;
    }

    public static boolean deleteGantData(GanttDataModel ganttData) {

        if (DBConnection.connect == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = DBConnection.connect.prepareStatement("delete from tbl_ganttData where id=" + "'" + ganttData.getId() + "'");
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
    }

    public static boolean removeAll() {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = DBConnection.connect.prepareStatement("delete from tbl_ganttData");
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
    }
}
