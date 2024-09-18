package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliverShipModel {
    @SerializedName("delivery_name")
    @Expose
    private String delivery_name;
    @SerializedName("delivery_contact_number")
    @Expose
    private String delivery_contact_number;
    @SerializedName("delivery_email")
    @Expose
    private String delivery_email;
    @SerializedName("delivery_postal_code")
    @Expose
    private String delivery_postal_code;
    @SerializedName("delivery_note")
    @Expose
    private String delivery_note;
    @SerializedName("delivery_street1")
    @Expose
    private String delivery_street1;
    @SerializedName("delivery_street2")
    @Expose
    private String delivery_street2;
    @SerializedName("delivery_suburb")
    @Expose
    private String delivery_suburb;
    @SerializedName("delivery_city")
    @Expose
    private String delivery_city;
    @SerializedName("delivery_state")
    @Expose
    private String delivery_state;
    @SerializedName("delivery_country")
    @Expose
    private String delivery_country;
    @SerializedName("delivery_latitude")
    @Expose
    private double delivery_latitude;
    @SerializedName("delivery_longitude")
    @Expose
    private double delivery_longitude;
    public DeliverShipModel() {}

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_contact_number() {
        return delivery_contact_number;
    }

    public void setDelivery_contact_number(String delivery_contact_number) {
        this.delivery_contact_number = delivery_contact_number;
    }

    public String getDelivery_email() {
        return delivery_email;
    }

    public void setDelivery_email(String delivery_email) {
        this.delivery_email = delivery_email;
    }

    public String getDelivery_postal_code() {
        return delivery_postal_code;
    }

    public void setDelivery_postal_code(String delivery_postal_code) {
        this.delivery_postal_code = delivery_postal_code;
    }

    public String getDelivery_note() {
        return delivery_note;
    }

    public void setDelivery_note(String delivery_note) {
        this.delivery_note = delivery_note;
    }

    public String getDelivery_street1() {
        return delivery_street1;
    }

    public void setDelivery_street1(String delivery_street1) {
        this.delivery_street1 = delivery_street1;
    }

    public String getDelivery_street2() {
        return delivery_street2;
    }

    public void setDelivery_street2(String delivery_street2) {
        this.delivery_street2 = delivery_street2;
    }

    public String getDelivery_suburb() {
        return delivery_suburb;
    }

    public void setDelivery_suburb(String delivery_suburb) {
        this.delivery_suburb = delivery_suburb;
    }

    public String getDelivery_city() {
        return delivery_city;
    }

    public void setDelivery_city(String delivery_city) {
        this.delivery_city = delivery_city;
    }

    public String getDelivery_state() {
        return delivery_state;
    }

    public void setDelivery_state(String delivery_state) {
        this.delivery_state = delivery_state;
    }

    public String getDelivery_country() {
        return delivery_country;
    }

    public void setDelivery_country(String delivery_country) {
        this.delivery_country = delivery_country;
    }

    public double getDelivery_latitude() {
        return delivery_latitude;
    }

    public void setDelivery_latitude(double delivery_latitude) {
        this.delivery_latitude = delivery_latitude;
    }

    public double getDelivery_longitude() {
        return delivery_longitude;
    }

    public void setDelivery_longitude(double delivery_longitude) {
        this.delivery_longitude = delivery_longitude;
    }
}
