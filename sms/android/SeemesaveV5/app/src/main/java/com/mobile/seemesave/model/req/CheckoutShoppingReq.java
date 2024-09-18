package com.mobile.seemesave.model.req;

public class CheckoutShoppingReq {
    private int id = 0;
    private String bag_string = "";
    private String description = "";
    private String estimate_hour = "";
    private String estimate_date = "";
    private String currency = "";

    public CheckoutShoppingReq() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBag_string() {
        return bag_string;
    }

    public void setBag_string(String bag_string) {
        this.bag_string = bag_string;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstimate_hour() {
        return estimate_hour;
    }

    public void setEstimate_hour(String estimate_hour) {
        this.estimate_hour = estimate_hour;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEstimate_date() {
        return estimate_date;
    }

    public void setEstimate_date(String estimate_date) {
        this.estimate_date = estimate_date;
    }
}
