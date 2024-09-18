/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.util.ArrayList;
import java.util.List;

public class DBSoccer {

    public static List<Soccer> readSoccer() {
        List<Soccer> read_all = new ArrayList<>();
        if (DBConnection.connect == null) {
            return read_all;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from soccer");

            while (DBConnection.resultSet.next()) {
                try {
                    Soccer soccer = new Soccer();

                    soccer.setId(DBConnection.resultSet.getInt("id"));
                    soccer.setLeague(DBConnection.resultSet.getString("league"));
                    soccer.setHome(DBConnection.resultSet.getString("home"));
                    soccer.setAway(DBConnection.resultSet.getString("away"));
                    soccer.setEvent(DBConnection.resultSet.getString("event"));
                    soccer.setView(DBConnection.resultSet.getString("view"));
                    soccer.setOdd(DBConnection.resultSet.getString("odd"));
                    soccer.setDay(DBConnection.resultSet.getString("day"));

                    read_all.add(soccer);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return read_all;
        }
        return read_all;
    }

    public static List<Soccer> readSoccer(String day) {
        List<Soccer> read_all = new ArrayList<>();
        if (DBConnection.connect == null) {
            return read_all;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from soccer where day=" + "'" + day + "'");

            while (DBConnection.resultSet.next()) {
                try {
                    Soccer soccer = new Soccer();

                    soccer.setId(DBConnection.resultSet.getInt("id"));
                    soccer.setLeague(DBConnection.resultSet.getString("league"));
                    soccer.setHome(DBConnection.resultSet.getString("home"));
                    soccer.setAway(DBConnection.resultSet.getString("away"));
                    soccer.setEvent(DBConnection.resultSet.getString("event"));
                    soccer.setView(DBConnection.resultSet.getString("view"));
                    soccer.setOdd(DBConnection.resultSet.getString("odd"));
                    soccer.setDay(DBConnection.resultSet.getString("day"));

                    read_all.add(soccer);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return read_all;
        }
        return read_all;
    }

    public static boolean insertSoccer(List<Object> params) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            if (params.size() != 7) {
                return false;
            }
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("insert into soccer values ($next_id,?,?,?,?,?,?,?)");
            for (int i = 1; i <= params.size(); i++) {
                DBConnection.preparedStatement.setString(i + 1, (String) params.get(i - 1));
            }
            DBConnection.preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
        return true;
    }

    public static boolean remove(int id) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("delete from soccer where id=" + "'" + id + "'");
            DBConnection.preparedStatement.executeUpdate();
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
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("delete from soccer");
            DBConnection.preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
    }

    public static boolean checkExist(String day) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from soccer where day=" + "'" + day + "'");
            if (DBConnection.resultSet.isBeforeFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
    }

    public static int getNoExistId() {
        if (DBConnection.connect == null) {
            return -1;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("SELECT t1.id + 1 "
                    + "FROM soccer t1 "
                    + "WHERE NOT EXISTS ("
                    + "    SELECT * "
                    + "    FROM soccer t2"
                    + "    WHERE t2.id = t1.id + 1"
                    + ") "
                    + "LIMIT 1");
            DBConnection.resultSet.next();
            return DBConnection.resultSet.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return -1;
        }
    }
}
