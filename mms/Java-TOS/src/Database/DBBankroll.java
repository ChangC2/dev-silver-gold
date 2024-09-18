/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DBBankroll {

    public static List<Bankroll> readBankroll() {
        List<Bankroll> read_all = new ArrayList<>();
        if (DBConnection.connect == null) {
            return read_all;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bankroll");

            while (DBConnection.resultSet.next()) {
                try {
                    Bankroll bankroll = new Bankroll();

                    bankroll.setId(DBConnection.resultSet.getInt("id"));
                    bankroll.setName(DBConnection.resultSet.getString("name"));
                    bankroll.setBalance(DBConnection.resultSet.getFloat("balance"));
                    bankroll.setUBalance(DBConnection.resultSet.getFloat("u_balance"));
                    bankroll.setUnit(DBConnection.resultSet.getFloat("unit"));
                    bankroll.setMProfilt(DBConnection.resultSet.getFloat("m_profilt"));
                    bankroll.setWProfilt(DBConnection.resultSet.getFloat("w_profilt"));
                    bankroll.setDProfilt(DBConnection.resultSet.getFloat("d_profilt"));
                    bankroll.setType(DBConnection.resultSet.getInt("type"));

                    read_all.add(bankroll);
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

    public static List<Bankroll> readBankroll(int id) {
        List<Bankroll> read_all = new ArrayList<>();
        if (DBConnection.connect == null) {
            return read_all;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bankroll where id=" + "'" + id + "'");

            while (DBConnection.resultSet.next()) {
                try {
                    Bankroll bankroll = new Bankroll();

                    bankroll.setId(DBConnection.resultSet.getInt("id"));
                    bankroll.setName(DBConnection.resultSet.getString("name"));
                    bankroll.setBalance(DBConnection.resultSet.getFloat("balance"));
                    bankroll.setUBalance(DBConnection.resultSet.getFloat("u_balance"));
                    bankroll.setUnit(DBConnection.resultSet.getFloat("unit"));
                    bankroll.setMProfilt(DBConnection.resultSet.getFloat("m_profilt"));
                    bankroll.setWProfilt(DBConnection.resultSet.getFloat("w_profilt"));
                    bankroll.setDProfilt(DBConnection.resultSet.getFloat("d_profilt"));
                    bankroll.setType(DBConnection.resultSet.getInt("type"));

                    read_all.add(bankroll);
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
    
    public static int insertBankroll(List<Object> params) {
        if (DBConnection.connect == null) {
            return -1;
        }
        try {
            if (params.size() != 8) {
                return -1;
            }
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("insert into bankroll values ($next_id,?,?,?,?,?,?,?,?)");

            DBConnection.preparedStatement.setString(2, (String) params.get(0));
            DBConnection.preparedStatement.setFloat(3, (float) params.get(1));
            DBConnection.preparedStatement.setFloat(4, (float) params.get(2));
            DBConnection.preparedStatement.setFloat(5, (float) params.get(3));
            DBConnection.preparedStatement.setFloat(6, (float) params.get(4));
            DBConnection.preparedStatement.setFloat(7, (float) params.get(5));
            DBConnection.preparedStatement.setFloat(8, (float) params.get(6));
            DBConnection.preparedStatement.setInt(9, (int) params.get(7));

            DBConnection.preparedStatement.executeUpdate();
            //get generated key
            ResultSet rs = DBConnection.preparedStatement.getGeneratedKeys();
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
    public static boolean updateUBalance(int id, float u_balance) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            String query = "update bankroll set u_balance=? where id=?";
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement(query);
            DBConnection.preparedStatement.setFloat(1, u_balance);
            DBConnection.preparedStatement.setInt(2, id);
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
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("delete from bankroll where id=" + "'" + id + "'");
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
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("delete from bankroll");
            DBConnection.preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
    }

    public static boolean checkExist(String name) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bankroll where name=" + "'" + name + "'");
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
                    + "    FROM bankroll t2"
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
