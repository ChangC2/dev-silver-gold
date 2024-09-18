/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.util.ArrayList;
import java.util.List;

public class DBBet {

    public static List<Bet> readBets() {
        List<Bet> read_all = new ArrayList<>();
        if (DBConnection.connect == null) {
            return read_all;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bet");

            while (DBConnection.resultSet.next()) {
                try {
                    Bet bet = new Bet();

                    bet.setId(DBConnection.resultSet.getInt("id"));
                    bet.setBId(DBConnection.resultSet.getInt("bid"));
                    bet.setrBetId(DBConnection.resultSet.getString("bet_id"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setMonth(DBConnection.resultSet.getInt("month"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setWeek(DBConnection.resultSet.getInt("week"));
                    bet.setDate(DBConnection.resultSet.getString("date"));
                    bet.setTime(DBConnection.resultSet.getString("time"));
                    bet.setUser(DBConnection.resultSet.getString("user"));
                    bet.setSport(DBConnection.resultSet.getString("sport"));
                    bet.setCountry(DBConnection.resultSet.getString("country"));
                    bet.setLeague(DBConnection.resultSet.getString("league"));
                    bet.setHome(DBConnection.resultSet.getString("home"));
                    bet.setAway(DBConnection.resultSet.getString("away"));
                    bet.setStatus(DBConnection.resultSet.getString("status"));
                    bet.setScore(DBConnection.resultSet.getString("score"));
                    bet.setType(DBConnection.resultSet.getString("type"));
                    bet.setUnits(DBConnection.resultSet.getFloat("units"));
                    bet.setAmount(DBConnection.resultSet.getFloat("amount"));
                    bet.setOdd(DBConnection.resultSet.getFloat("odd"));
                    bet.setBetBalance(DBConnection.resultSet.getFloat("balance"));
                    bet.setWin(DBConnection.resultSet.getInt("win"));

                    read_all.add(bet);
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
    public static List<Bet> readBets(int bid) {
        List<Bet> read_all = new ArrayList<>();
        if (DBConnection.connect == null) {
            return read_all;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bet where bid=" + "'" + bid + "'");

            while (DBConnection.resultSet.next()) {
                try {
                    Bet bet = new Bet();

                    bet.setId(DBConnection.resultSet.getInt("id"));
                    bet.setBId(DBConnection.resultSet.getInt("bid"));
                    bet.setrBetId(DBConnection.resultSet.getString("bet_id"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setMonth(DBConnection.resultSet.getInt("month"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setWeek(DBConnection.resultSet.getInt("week"));
                    bet.setDate(DBConnection.resultSet.getString("date"));
                    bet.setTime(DBConnection.resultSet.getString("time"));
                    bet.setUser(DBConnection.resultSet.getString("user"));
                    bet.setSport(DBConnection.resultSet.getString("sport"));
                    bet.setCountry(DBConnection.resultSet.getString("country"));
                    bet.setLeague(DBConnection.resultSet.getString("league"));
                    bet.setHome(DBConnection.resultSet.getString("home"));
                    bet.setAway(DBConnection.resultSet.getString("away"));
                    bet.setStatus(DBConnection.resultSet.getString("status"));
                    bet.setScore(DBConnection.resultSet.getString("score"));
                    bet.setType(DBConnection.resultSet.getString("type"));
                    bet.setUnits(DBConnection.resultSet.getFloat("units"));
                    bet.setAmount(DBConnection.resultSet.getFloat("amount"));
                    bet.setOdd(DBConnection.resultSet.getFloat("odd"));
                    bet.setBetBalance(DBConnection.resultSet.getFloat("balance"));
                    bet.setWin(DBConnection.resultSet.getInt("win"));

                    read_all.add(bet);
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
    public static List<Bet> readBets(int bid, int year) {
        List<Bet> read_all = new ArrayList<>();
        if (DBConnection.connect == null) {
            return read_all;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bet where bid=" + "'" + bid + "'" + " and year=" + "'" + year + "'");

            while (DBConnection.resultSet.next()) {
                try {
                    Bet bet = new Bet();

                    bet.setId(DBConnection.resultSet.getInt("id"));
                    bet.setBId(DBConnection.resultSet.getInt("bid"));
                    bet.setrBetId(DBConnection.resultSet.getString("bet_id"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setMonth(DBConnection.resultSet.getInt("month"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setWeek(DBConnection.resultSet.getInt("week"));
                    bet.setDate(DBConnection.resultSet.getString("date"));
                    bet.setTime(DBConnection.resultSet.getString("time"));
                    bet.setUser(DBConnection.resultSet.getString("user"));
                    bet.setSport(DBConnection.resultSet.getString("sport"));
                    bet.setCountry(DBConnection.resultSet.getString("country"));
                    bet.setLeague(DBConnection.resultSet.getString("league"));
                    bet.setHome(DBConnection.resultSet.getString("home"));
                    bet.setAway(DBConnection.resultSet.getString("away"));
                    bet.setStatus(DBConnection.resultSet.getString("status"));
                    bet.setScore(DBConnection.resultSet.getString("score"));
                    bet.setType(DBConnection.resultSet.getString("type"));
                    bet.setUnits(DBConnection.resultSet.getFloat("units"));
                    bet.setAmount(DBConnection.resultSet.getFloat("amount"));
                    bet.setOdd(DBConnection.resultSet.getFloat("odd"));
                    bet.setBetBalance(DBConnection.resultSet.getFloat("balance"));
                    bet.setWin(DBConnection.resultSet.getInt("win"));

                    read_all.add(bet);
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
    
    public static List<Bet> readBets(int bid, int year, int month) {
        List<Bet> read_all = new ArrayList<>();
        if (DBConnection.connect == null) {
            return read_all;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bet where bid=" + "'" + bid + "'" + " and year=" + "'" + year + "'" + 
                    " and month=" + "'" + month + "'");

            while (DBConnection.resultSet.next()) {
                try {
                    Bet bet = new Bet();

                    bet.setId(DBConnection.resultSet.getInt("id"));
                    bet.setBId(DBConnection.resultSet.getInt("bid"));
                    bet.setrBetId(DBConnection.resultSet.getString("bet_id"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setMonth(DBConnection.resultSet.getInt("month"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setWeek(DBConnection.resultSet.getInt("week"));
                    bet.setDate(DBConnection.resultSet.getString("date"));
                    bet.setTime(DBConnection.resultSet.getString("time"));
                    bet.setUser(DBConnection.resultSet.getString("user"));
                    bet.setSport(DBConnection.resultSet.getString("sport"));
                    bet.setCountry(DBConnection.resultSet.getString("country"));
                    bet.setLeague(DBConnection.resultSet.getString("league"));
                    bet.setHome(DBConnection.resultSet.getString("home"));
                    bet.setAway(DBConnection.resultSet.getString("away"));
                    bet.setStatus(DBConnection.resultSet.getString("status"));
                    bet.setScore(DBConnection.resultSet.getString("score"));
                    bet.setType(DBConnection.resultSet.getString("type"));
                    bet.setUnits(DBConnection.resultSet.getFloat("units"));
                    bet.setAmount(DBConnection.resultSet.getFloat("amount"));
                    bet.setOdd(DBConnection.resultSet.getFloat("odd"));
                    bet.setBetBalance(DBConnection.resultSet.getFloat("balance"));
                    bet.setWin(DBConnection.resultSet.getInt("win"));

                    read_all.add(bet);
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
    public static List<Bet> readLossBets(int bid) {
        List<Bet> read_all = new ArrayList<>();
        if (DBConnection.connect == null) {
            return read_all;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bet where bid=" + "'" + bid + "'" + " and win=" + "'" + 2 + "'");

            while (DBConnection.resultSet.next()) {
                try {
                    Bet bet = new Bet();

                    bet.setId(DBConnection.resultSet.getInt("id"));
                    bet.setBId(DBConnection.resultSet.getInt("bid"));
                    bet.setrBetId(DBConnection.resultSet.getString("bet_id"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setMonth(DBConnection.resultSet.getInt("month"));
                    bet.setYear(DBConnection.resultSet.getInt("year"));
                    bet.setWeek(DBConnection.resultSet.getInt("week"));
                    bet.setDate(DBConnection.resultSet.getString("date"));
                    bet.setTime(DBConnection.resultSet.getString("time"));
                    bet.setUser(DBConnection.resultSet.getString("user"));
                    bet.setSport(DBConnection.resultSet.getString("sport"));
                    bet.setCountry(DBConnection.resultSet.getString("country"));
                    bet.setLeague(DBConnection.resultSet.getString("league"));
                    bet.setHome(DBConnection.resultSet.getString("home"));
                    bet.setAway(DBConnection.resultSet.getString("away"));
                    bet.setStatus(DBConnection.resultSet.getString("status"));
                    bet.setScore(DBConnection.resultSet.getString("score"));
                    bet.setType(DBConnection.resultSet.getString("type"));
                    bet.setUnits(DBConnection.resultSet.getFloat("units"));
                    bet.setAmount(DBConnection.resultSet.getFloat("amount"));
                    bet.setOdd(DBConnection.resultSet.getFloat("odd"));
                    bet.setBetBalance(DBConnection.resultSet.getFloat("balance"));
                    bet.setWin(DBConnection.resultSet.getInt("win"));

                    read_all.add(bet);
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
    public static float getTotalPending(int bid){
        float total = 0;
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select sum(amount) as sum_amount from bet where bid=" + "'" + bid + "' and win=" + "'" + 0 + "'");
            total = DBConnection.resultSet.getFloat("sum_amount");
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
        }
        return total;
    }
    public static boolean insertBet(List<Object> params) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            if (params.size() != 21) {
                return false;
            }
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("insert into bet values ($next_id,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            DBConnection.preparedStatement.setInt(2, (int) params.get(0));
            DBConnection.preparedStatement.setString(3, (String) params.get(1));
            DBConnection.preparedStatement.setInt(4, (int) params.get(2));
            DBConnection.preparedStatement.setInt(5, (int) params.get(3));
            DBConnection.preparedStatement.setInt(6, (int) params.get(4));
            DBConnection.preparedStatement.setString(7, (String) params.get(5));
            DBConnection.preparedStatement.setString(8, (String) params.get(6));
            DBConnection.preparedStatement.setString(9, (String) params.get(7));
            DBConnection.preparedStatement.setString(10, (String) params.get(8));
            DBConnection.preparedStatement.setString(11, (String) params.get(9));
            DBConnection.preparedStatement.setString(12, (String) params.get(10));
            DBConnection.preparedStatement.setString(13, (String) params.get(11));
            DBConnection.preparedStatement.setString(14, (String) params.get(12));
            DBConnection.preparedStatement.setString(15, (String) params.get(13));
            DBConnection.preparedStatement.setString(16, (String) params.get(14));
            DBConnection.preparedStatement.setString(17, (String) params.get(15));
            DBConnection.preparedStatement.setFloat(18, (float) params.get(16));
            DBConnection.preparedStatement.setFloat(19, (float) params.get(17));
            DBConnection.preparedStatement.setFloat(20, (float) params.get(18));
            DBConnection.preparedStatement.setFloat(21, (float) params.get(19));
            DBConnection.preparedStatement.setInt(22, (int) params.get(20));

            DBConnection.preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
        return true;
    }

    public static boolean updateBet(int id, String username, String type, float unit, float amount, float odd) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            String query = "update bet set user=?, type=?, units=?, amount=?, odd=? where id=?";
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement(query);
            DBConnection.preparedStatement.setString(1, username);
            DBConnection.preparedStatement.setString(2, type);
            DBConnection.preparedStatement.setFloat(3, unit);
            DBConnection.preparedStatement.setFloat(4, amount);
            DBConnection.preparedStatement.setFloat(5, odd);
            DBConnection.preparedStatement.setInt(6, id);
            DBConnection.preparedStatement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
        return true;
    }
    
     public static boolean updateBet(int id, int win) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            String query = "update bet set win=? where id=?";
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement(query);
            DBConnection.preparedStatement.setInt(1, win);
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
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("delete from bet where id=" + "'" + id + "'");
            DBConnection.preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
    }

    public static boolean removeByBankroll(int bid) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("delete from bet where bid=" + "'" + bid + "'");
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
            DBConnection.preparedStatement = DBConnection.connect.prepareStatement("delete from bet");
            DBConnection.preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            DBConnection.reconnect();
            return false;
        }
    }
    
    public static boolean checkSameEvent(String bet_id) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bet where bet_id=" + "'" + bet_id + "'");
            if (DBConnection.resultSet.isBeforeFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //DBConnection.reconnect();
            return false;
        }
    }
    public static boolean checkExist(String name) {
        if (DBConnection.connect == null) {
            return false;
        }
        try {
            DBConnection.resultSet = DBConnection.statement.executeQuery("select * from bet where name=" + "'" + name + "'");
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
}
