package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliverStatusModel {
    @SerializedName("UNPAID")
    @Expose
    private int UNPAID;
    @SerializedName("PAYING")
    @Expose
    private int PAYING;
    @SerializedName("PAID")
    @Expose
    private int PAID;
    @SerializedName("READ")
    @Expose
    private int READ;
    @SerializedName("PICKING")
    @Expose
    private int PICKING;
    @SerializedName("COLLECTION_READY")
    @Expose
    private int COLLECTION_READY;
    @SerializedName("COLLECTED")
    @Expose
    private int COLLECTED;
    @SerializedName("FAILED")
    @Expose
    private int FAILED;
    @SerializedName("DELIVERED")
    @Expose
    private int DELIVERED;
    @SerializedName("FINISHED")
    @Expose
    private int FINISHED;

    public DeliverStatusModel () {

    }

    public int getUNPAID() {
        return UNPAID;
    }

    public void setUNPAID(int UNPAID) {
        this.UNPAID = UNPAID;
    }

    public int getPAYING() {
        return PAYING;
    }

    public void setPAYING(int PAYING) {
        this.PAYING = PAYING;
    }

    public int getPAID() {
        return PAID;
    }

    public void setPAID(int PAID) {
        this.PAID = PAID;
    }

    public int getREAD() {
        return READ;
    }

    public void setREAD(int READ) {
        this.READ = READ;
    }

    public int getPICKING() {
        return PICKING;
    }

    public void setPICKING(int PICKING) {
        this.PICKING = PICKING;
    }

    public int getCOLLECTION_READY() {
        return COLLECTION_READY;
    }

    public void setCOLLECTION_READY(int COLLECTION_READY) {
        this.COLLECTION_READY = COLLECTION_READY;
    }

    public int getCOLLECTED() {
        return COLLECTED;
    }

    public void setCOLLECTED(int COLLECTED) {
        this.COLLECTED = COLLECTED;
    }

    public int getFAILED() {
        return FAILED;
    }

    public void setFAILED(int FAILED) {
        this.FAILED = FAILED;
    }

    public int getDELIVERED() {
        return DELIVERED;
    }

    public void setDELIVERED(int DELIVERED) {
        this.DELIVERED = DELIVERED;
    }

    public int getFINISHED() {
        return FINISHED;
    }

    public void setFINISHED(int FINISHED) {
        this.FINISHED = FINISHED;
    }
}
