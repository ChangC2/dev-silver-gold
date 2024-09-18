package com.mobile.seemesave.model.req;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginReq {
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    public LoginReq(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
