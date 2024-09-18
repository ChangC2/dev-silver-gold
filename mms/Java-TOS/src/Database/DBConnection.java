/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {

    public static Connection connect = null;
    public static Statement statement = null;
    public static PreparedStatement preparedStatement = null;
    public static ResultSet resultSet = null;

    public static boolean connection() {
        try {
            if(connect != null){
                return true;
            }
            connect = DriverManager.getConnection(DBParams.LOCAL_DATABASE_PATH);
            statement = connect.createStatement();

            //statement.execute(DBParams.BANKROLL_TABLE);
            //statement.execute(DBParams.BET_TABLE);
            //statement.execute(DBParams.BETTYPE_TABLE);
            //statement.execute(DBParams.USER_TABLE);

            statement.execute(GanttDataModel.CREATE_GANTT_DATA_TABLE);
            statement.execute(ShiftDataModel.CREATE_SHIFT_DATA_TABLE);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reconnect() {
        close();
        connection();
    }
}
