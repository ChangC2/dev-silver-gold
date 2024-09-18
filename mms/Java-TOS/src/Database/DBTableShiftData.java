
package Database;

import org.apache.http.util.TextUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBTableShiftData {

    public static List<ShiftDataModel> getUpdatedShiftDataList() {
        List<ShiftDataModel> sugarList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + ShiftDataModel.TABLE_SHIFT_DATA_NAME + " WHERE " + ShiftDataModel.COLUMN_UPDATED + " >= 1";

        try {
            Statement statement = DBConnection.connect.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                try {
                    ShiftDataModel tempObj = new ShiftDataModel();

                    sugarList.add(tempObj);

                    tempObj.setId(resultSet.getLong(ShiftDataModel.COLUMN_ID));
                    tempObj.setShiftId(resultSet.getLong(ShiftDataModel.COLUMN_SHIFTID));

                    tempObj.setShiftStartTime(resultSet.getInt(ShiftDataModel.COLUMN_SHIFT_STARTTIME));
                    tempObj.setShiftStopTime(resultSet.getInt(ShiftDataModel.COLUMN_SHIFT_STOPTIME));

                    tempObj.setJobID(resultSet.getString(ShiftDataModel.COLUMN_JOBID));
                    tempObj.setJobSequenceNo(resultSet.getString(ShiftDataModel.COLUMN_JOBSEQNO));

                    tempObj.setMachine(resultSet.getString(ShiftDataModel.COLUMN_MACHINE));
                    tempObj.setOperator(resultSet.getString(ShiftDataModel.COLUMN_OPERATOR));
                    tempObj.setUserID(resultSet.getString(ShiftDataModel.COLUMN_USERID));

                    tempObj.setStartTime(resultSet.getLong(ShiftDataModel.COLUMN_STARTTIME));
                    tempObj.setStopTime(resultSet.getLong(ShiftDataModel.COLUMN_STOPTIME));

                    tempObj.setUtilization(resultSet.getFloat(ShiftDataModel.COLUMN_UTILIZATION));
                    tempObj.setOffLineT(resultSet.getLong(ShiftDataModel.COLUMN_OFFLINE));

                    tempObj.setOee(resultSet.getFloat(ShiftDataModel.COLUMN_OEE));
                    tempObj.setAvailablity(resultSet.getFloat(ShiftDataModel.COLUMN_AVAILABILITY));
                    tempObj.setPerformance(resultSet.getFloat(ShiftDataModel.COLUMN_PERFORMANCE));
                    tempObj.setQuality(resultSet.getFloat(ShiftDataModel.COLUMN_QUALITY));

                    tempObj.setGoodParts(resultSet.getInt(ShiftDataModel.COLUMN_GOODS));
                    tempObj.setBadParts(resultSet.getInt(ShiftDataModel.COLUMN_BADS));

                    tempObj.setElapsedTimeInMils(0, resultSet.getLong(ShiftDataModel.COLUMN_UNCAT));
                    tempObj.setElapsedTimeInMils(1, resultSet.getLong(ShiftDataModel.COLUMN_INCYCLE));
                    tempObj.setElapsedTimeInMils(2, resultSet.getLong(ShiftDataModel.COLUMN_R1T));
                    tempObj.setElapsedTimeInMils(3, resultSet.getLong(ShiftDataModel.COLUMN_R2T));
                    tempObj.setElapsedTimeInMils(4, resultSet.getLong(ShiftDataModel.COLUMN_R3T));
                    tempObj.setElapsedTimeInMils(5, resultSet.getLong(ShiftDataModel.COLUMN_R4T));
                    tempObj.setElapsedTimeInMils(6, resultSet.getLong(ShiftDataModel.COLUMN_R5T));
                    tempObj.setElapsedTimeInMils(7, resultSet.getLong(ShiftDataModel.COLUMN_R6T));
                    tempObj.setElapsedTimeInMils(8, resultSet.getLong(ShiftDataModel.COLUMN_R7T));
                    tempObj.setElapsedTimeInMils(9, resultSet.getLong(ShiftDataModel.COLUMN_R8T));

                    tempObj.setAuxData1(resultSet.getFloat(ShiftDataModel.COLUMN_AUXDATA1));
                    tempObj.setAuxData2(resultSet.getFloat(ShiftDataModel.COLUMN_AUXDATA2));
                    tempObj.setAuxData3(resultSet.getFloat(ShiftDataModel.COLUMN_AUXDATA3));

                    tempObj.setCompleted(resultSet.getInt(ShiftDataModel.COLUMN_COMPLETED) > 0);
                    tempObj.setUpdated(resultSet.getInt(ShiftDataModel.COLUMN_UPDATED) > 0);

                    tempObj.setShiftSetting(resultSet.getString(ShiftDataModel.COLUMN_EXT1));
                    tempObj.setTargetCycleTimeSeconds(resultSet.getLong(ShiftDataModel.COLUMN_EXT2));
                    tempObj.setPlannedProductionTime(resultSet.getLong(ShiftDataModel.COLUMN_EXT3));

                    // Rework Status
                    String strRework = resultSet.getString(ShiftDataModel.COLUMN_REWORK);
                    int rework = 0;
                    try{
                        rework = Integer.parseInt(strRework);
                    }catch (Exception e) {e.printStackTrace();}
                    tempObj.setStatusRework(rework);

                    String strSetup = resultSet.getString(ShiftDataModel.COLUMN_SETUP);
                    int setup = 0;
                    try{
                        setup = Integer.parseInt(strSetup);
                    }catch (Exception e) {e.printStackTrace();}
                    tempObj.setStatusSetup(setup);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return sugarList;
        }

        // return notes list
        return sugarList;
    }

    public static long insertShiftData(ShiftDataModel shiftData) {

        if (TextUtils.isEmpty(shiftData.getMachine())) {
            return 0;
        }

        if (DBConnection.connect == null) {
            return -1;
        }
        try {
            PreparedStatement preparedStatement = DBConnection.connect.prepareStatement("insert into tbl_shiftData " +
                    "(`shift_id`, `shift_start_time`, `shift_stop_time`, `job_id`, `job_sequenceno`, `machine`, `operator`, `user_id`, `start_time`, `stop_time`, `utilization`, `offline`, " +
                    "`oee`, `availability`, `performance`, `quality`, `goods`, `bads`, `incycle`, `uncat`, `r1t`, `r2t`, `r3t`, `r4t`, `r5t`, `r6t`, `r7t`, `r8t`, `aux_data1`, `aux_data2`, `aux_data3`, " +
                    "`completed`, `updated`, `rework`, `setup`, `ext1`, `ext2`, `ext3`) " +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            preparedStatement.setLong(1, shiftData.getShiftId());
            preparedStatement.setLong(2, shiftData.getShiftStartTime());
            preparedStatement.setLong(3, shiftData.getShiftStopTime());

            preparedStatement.setString(4, shiftData.getJobID());
            preparedStatement.setString(5, shiftData.getJobSequenceNo());

            preparedStatement.setString(6, shiftData.getMachine());
            preparedStatement.setString(7, shiftData.getOperator());
            preparedStatement.setString(8, shiftData.getUserID());

            preparedStatement.setLong(9, shiftData.getStartTime());
            preparedStatement.setLong(10, shiftData.getStopTime());
            preparedStatement.setFloat(11, shiftData.getUtilization());
            preparedStatement.setLong(12, shiftData.getOffLineT());

            preparedStatement.setFloat(13, shiftData.getOee());
            preparedStatement.setFloat(14, shiftData.getAvailablity());
            preparedStatement.setFloat(15, shiftData.getPerformance());
            preparedStatement.setFloat(16, shiftData.getQuality());

            preparedStatement.setInt(17, shiftData.getGoodParts());
            preparedStatement.setInt(18, shiftData.getBadParts());

            preparedStatement.setLong(19, shiftData.getElapsedTimeInMils(0));
            preparedStatement.setLong(20, shiftData.getElapsedTimeInMils(1));
            preparedStatement.setLong(21, shiftData.getElapsedTimeInMils(2));
            preparedStatement.setLong(22, shiftData.getElapsedTimeInMils(3));
            preparedStatement.setLong(23, shiftData.getElapsedTimeInMils(4));
            preparedStatement.setLong(24, shiftData.getElapsedTimeInMils(5));
            preparedStatement.setLong(25, shiftData.getElapsedTimeInMils(6));
            preparedStatement.setLong(26, shiftData.getElapsedTimeInMils(7));
            preparedStatement.setLong(27, shiftData.getElapsedTimeInMils(8));
            preparedStatement.setLong(28, shiftData.getElapsedTimeInMils(9));

            preparedStatement.setFloat(29, shiftData.getAuxData1());
            preparedStatement.setFloat(30, shiftData.getAuxData2());
            preparedStatement.setFloat(31, shiftData.getAuxData3());

            preparedStatement.setLong(32, 0);
            preparedStatement.setLong(33, 1);

            preparedStatement.setInt(34, shiftData.getStatusRework());
            preparedStatement.setInt(35, shiftData.getStatusSetup());

            preparedStatement.setString(36, shiftData.getShiftSetting());
            preparedStatement.setLong(37, shiftData.getTargetCycleTimeSeconds());
            preparedStatement.setLong(38, shiftData.getPlannedProductionTime());

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

    public static boolean updateShiftData(ShiftDataModel shiftData) {

        if (DBConnection.connect == null) {
            return false;
        }

        // In case of machine field is empty, this is invalid shift data and ignore all shift data
        if (shiftData.getId() == 0)
            return false;

        try {
            //"(`shift_start_time`, `shift_stop_time`, `job_id`, `job_sequenceno`, `machine`, `operator`, `user_id`, `start_time`, `stop_time`, `utilization`, `offline`, " +
            //"`oee`, `availability`, `performance`, `quality`, `goods`, `bads`, `incycle`, `uncat`, `r1t`, `r2t`, `r3t`, `r4t`, `r5t`, `r6t`, `r7t`, `r8t`, `aux_data1`, `aux_data2`, `aux_data3`, " +
            //"`completed`, `updated`, `rework`, `setup`, `ext1`, `ext2`, `ext3`) " +
            String query = "update tbl_shiftData set shift_start_time=?, shift_stop_time=?, job_id=?, job_sequenceno=?, machine=?, operator=?, user_id=?, start_time=?, stop_time=?, utilization=?, offline=?, " +
                    "oee=?, availability=?, performance=?, quality=?, goods=?, bads=?, incycle=?, uncat=?, r1t=?, r2t=?, r3t=?, r4t=?, r5t=?, r6t=?, r7t=?, r8t=?, aux_data1=?, aux_data2=?, aux_data3=?, " +
                    "completed=?, updated=?, rework=?, setup=?, ext1=?, ext2=?, ext3=? where id=?";
            PreparedStatement preparedStatement = DBConnection.connect.prepareStatement(query);
            preparedStatement.setLong(1, shiftData.getShiftStartTime());
            preparedStatement.setLong(2, shiftData.getShiftStopTime());

            preparedStatement.setString(3, shiftData.getJobID());
            preparedStatement.setString(4, shiftData.getJobSequenceNo());

            preparedStatement.setString(5, shiftData.getMachine());
            preparedStatement.setString(6, shiftData.getOperator());
            preparedStatement.setString(7, shiftData.getUserID());

            preparedStatement.setLong(8, shiftData.getStartTime());
            preparedStatement.setLong(9, shiftData.getStopTime());
            preparedStatement.setFloat(10, shiftData.getUtilization());
            preparedStatement.setLong(11, shiftData.getOffLineT());

            preparedStatement.setFloat(12, shiftData.getOee());
            preparedStatement.setFloat(13, shiftData.getAvailablity());
            preparedStatement.setFloat(14, shiftData.getPerformance());
            preparedStatement.setFloat(15, shiftData.getQuality());

            preparedStatement.setInt(16, shiftData.getGoodParts());
            preparedStatement.setInt(17, shiftData.getBadParts());

            preparedStatement.setLong(18, shiftData.getElapsedTimeInMils(0));
            preparedStatement.setLong(19, shiftData.getElapsedTimeInMils(1));
            preparedStatement.setLong(20, shiftData.getElapsedTimeInMils(2));
            preparedStatement.setLong(21, shiftData.getElapsedTimeInMils(3));
            preparedStatement.setLong(22, shiftData.getElapsedTimeInMils(4));
            preparedStatement.setLong(23, shiftData.getElapsedTimeInMils(5));
            preparedStatement.setLong(24, shiftData.getElapsedTimeInMils(6));
            preparedStatement.setLong(25, shiftData.getElapsedTimeInMils(7));
            preparedStatement.setLong(26, shiftData.getElapsedTimeInMils(8));
            preparedStatement.setLong(27, shiftData.getElapsedTimeInMils(9));

            preparedStatement.setFloat(28, shiftData.getAuxData1());
            preparedStatement.setFloat(29, shiftData.getAuxData2());
            preparedStatement.setFloat(30, shiftData.getAuxData3());

            preparedStatement.setLong(31, shiftData.isCompleted() ? 1 : 0);
            preparedStatement.setLong(32, 1);

            preparedStatement.setInt(33, shiftData.getStatusRework());
            preparedStatement.setInt(34, shiftData.getStatusSetup());

            preparedStatement.setString(35, shiftData.getShiftSetting());
            preparedStatement.setLong(36, shiftData.getTargetCycleTimeSeconds());
            preparedStatement.setLong(37, shiftData.getPlannedProductionTime());

            preparedStatement.setLong(38, shiftData.getId());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
        return true;
    }

    // Refresh Shift ID after insert Server, this is tracking number for sync and update
    public static boolean updateShiftStatusAsProcessed(ShiftDataModel shiftData) {

        if (shiftData.getId() == 0)
            return false;

        if (DBConnection.connect == null) {
            return false;
        }
        try {
            String query = "update tbl_shiftData set shift_id=?, updated=? where id=?";
            PreparedStatement preparedStatement = DBConnection.connect.prepareStatement(query);
            preparedStatement.setLong(1, shiftData.getShiftId());
            preparedStatement.setInt(2, 0);
            preparedStatement.setLong(3, shiftData.getId());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
        return true;
    }

    public static boolean deleteShiftData(ShiftDataModel shiftData) {

        if (DBConnection.connect == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = DBConnection.connect.prepareStatement("delete from tbl_shiftData where id=" + shiftData.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
    }

    public static boolean removeOldShiftData() {
        long timeValidData = System.currentTimeMillis() - 86400000 * 3; // Remove data since 3 days ago
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = DBConnection.connect.prepareStatement("delete from tbl_shiftData where stop_time<=" + timeValidData + " and updated = 0");
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
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("delete from tbl_shiftData");
            DBConnection.preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
    }
}
