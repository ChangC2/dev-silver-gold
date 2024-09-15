/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;


public class Soccer {
    private int id;
    private String league;
    private String home;
    private String away;
    private String event;
    private String view;
    private String odd;
    private String day;
    
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setLeague(String league){
        this.league = league;
    }
    public String getLeague(){
        return league;
    }
    public void setHome(String home){
        this.home = home;
    }
    public String getHome(){
        return home;
    }
    public void setAway(String away){
        this.away = away;
    }
    public String getAway(){
        return away;
    }
    public void setEvent(String event){
        this.event = event;
    }
    public String getEvent(){
        return event;
    }
    public void setView(String view){
        this.view = view;
    }
    public String getView(){
        return view;
    }
    public void setOdd(String odd){
        this.odd = odd;
    }
    public String getOdd(){
        return odd;
    }
    public void setDay(String day){
        this.day = day;
    }
    public String getDay(){
        return day;
    }
}
