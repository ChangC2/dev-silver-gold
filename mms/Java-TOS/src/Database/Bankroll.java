/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

public class Bankroll {

    private int id;
    private String name;
    private float balance;
    private float ubalance;
    private float unit;
    private float mProfilt;
    private float wProfilt;
    private float dProfilt;
    private int type;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
    
    public float getBalance() {
        return balance;
    }
    
    public void setUBalance(float rbalance) {
        this.ubalance = rbalance;
    }

    public float getUBalance() {
        return ubalance;
    }

    public void setUnit(float unit) {
        this.unit = unit;
    }

    public float getUnit() {
        return unit;
    }

    public void setMProfilt(float mProfit) {
        this.mProfilt = mProfit;
    }

    public float getMProfilt() {
        return mProfilt;
    }

    public void setWProfilt(float wProfit) {
        this.wProfilt = wProfit;
    }

    public float getWProfilt() {
        return wProfilt;
    }

    public void setDProfilt(float dProfit) {
        this.dProfilt = dProfit;
    }

    public float getDProfilt() {
        return dProfilt;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return getName();
    }
}
