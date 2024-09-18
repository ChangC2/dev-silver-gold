/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Bet {

    private int id;
    private int bId;
    private String betId;
    private int index;
    private int year;
    private int month;
    private int week;
    private String date;
    private String time;
    private String user;
    private String sport;
    private String country;
    private String league;
    private String home;
    private String away;
    private String status;
    private String score;
    private String type;
    private float units;
    private float amount;
    private float odd;
    private float bet_balance;
    private int win;

    private List<Bet> bet_list;
    private Bankroll bankroll;

    public Bet() {
        bet_list = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setBId(int bId) {
        this.bId = bId;
    }

    public int getBId() {
        return bId;
    }
    public void setrBetId(String bet_id){
        this.betId = bet_id;
    }
    public String getBetId(){
        return betId;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
    public void setYear(int year){
        this.year = year;
    }
    public int getYear(){
        return year;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getWeek() {
        return week;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
    
    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getSport() {
        return sport;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getLeague() {
        return league;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getHome() {
        return home;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getAway() {
        return away;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUnits(float units) {
        this.units = units;
    }

    public float getUnits() {
        return units;
    }
    public void setAmount(float amount){
        this.amount = amount;
    }
    public float getAmount(){
        return amount;
    }
    public void setOdd(float odd) {
        this.odd = odd;
    }

    public float getOdd() {
        return odd;
    }
    public void setBetBalance(float bet_balance){
        this.bet_balance = bet_balance;
    }
    public float getBetBalance(){
        return bet_balance;
    }
    public void setWin(int win) {
        this.win = win;
    }
    
    public int getWin() {
        return win;
    }

    public void setBet_list(List<Bet> bet_list) {
        this.bet_list = bet_list;
    }

    public void setBankRoll(Bankroll bankroll) {
        this.bankroll = bankroll;
    }

    public float getUnitSize() {
        try {
            return getBetBalance() * bankroll.getUnit() / 100;
        } catch (Exception e) {
            return 0;
        }
    }
    public float getReturn() {
        try {
            switch (getWin()) {
                case 0:
                    return 0;
                case 1:
                    return getAmount() * getOdd();
                case 2:
                    return 0;
                case 3:
                    return getAmount();
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;

        }
    }

    public float getNet() {
        try {
            if(getWin() == 0){
                return 0;
            }else{
                return getReturn() - getAmount();
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public float getCumulative() {
        try {
            if (index == 0) {
                return getNet();
            } else {
                return bet_list.get(index - 1).getCumulative() + getNet();
            }
        } catch (Exception e) {
            return 0;
        }
    }
    
    public float getBalance() {
        try {
            if (index == 0) {
                return getBetBalance() + getNet();
            } else {
                return bet_list.get(index - 1).getBalance() + getNet();
            }
        } catch (Exception e) {
            return 0;
        }
    }
    
    //================================================
    public Label getDateLabel() {
        return getLabel("table_label", getDate());
    }

    public Label getTimeLabel() {
        return getLabel("table_label", getTime());
    }

    public Label getUserLabel() {
        return getLabel("table_label", getUser());
    }

    public Label getLeagueLabel() {
        return getLabel("table_label", getLeague());
    }

    public Label getSportLabel() {
        return getLabel("table_label", getSport());
    }

    public Label getHomeLabel() {
        return getLabel("table_label", getHome());
    }

    public Label getAwayLabel() {
        return getLabel("table_label", getAway());
    }

    public Label getStatusLabel() {
        Label label = getLabel("over_label", getStatus());
        //label.setWrapText(true);
        return label;
    }

    public Label getScoreLabel() {
        return getLabel("under_label", getScore());
    }

    public Label getTypeLabel() {
        return getLabel("table_label", getType());
    }

    public Label getUnitLabel() {
        return getLabel("table_label", String.format("%.2f", getUnits()));
    }

    public Label getUnitSizeLabel() {
        return getLabel("table_label", String.format("%.2f", getUnitSize()));
    }

    public Label getAmountLabel() {
        return getLabel("table_label", String.format("%.2f", getAmount()));
    }

    public Label getOddLabel() {
        return getLabel("table_label", String.format("%.2f", getOdd()));
    }

    public Label getReturnLabel() {
        return getLabel("table_label", String.format("%.2f", getReturn()));
    }

    public Label getNetLabel() {
        float net = getNet();
        if (net < 0) {
            return getLabel("under_label", String.format("%.2f", net));
        } else {
            return getLabel("over_label", String.format("%.2f", net));
        }
    }

    public Label getCumulativeLabel() {
        float cumulative = getCumulative();

        if (cumulative < 0) {
            return getLabel("under_label", String.format("%.2f", cumulative));
        } else {
            return getLabel("over_label", String.format("%.2f", cumulative));
        }
    }

    public Label getBalanceLabel() {
        float balance = getBalance();
        if (balance < 0) {
            return getLabel("under_label", String.format("%.2f", balance));
        } else {
            return getLabel("over_label", String.format("%.2f", balance));
        }
    }

    private Label getLabel(String id, String text) {
        Label label = new Label();
        label.setId(id);
        //label.setWrapText(true);
        if (text == null || text.trim().isEmpty()) {
            label.setText("--");
        } else {
            label.setText(text);
        }
        return label;
    }

    @Override
    public boolean equals(Object model) {
        return id == ((Bet) model).getId();
    }
}
