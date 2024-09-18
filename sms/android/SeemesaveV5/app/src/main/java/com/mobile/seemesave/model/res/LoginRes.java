package com.mobile.seemesave.model.res;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.mobile.seemesave.model.common.UserModel;

public class LoginRes {
    @SerializedName("user")
    @Expose
    private UserModel user;
    @SerializedName("token")
    @Expose
    private String token;

    private boolean status;
    private String message;
    public LoginRes(){
        this.status = false;
        this.message = "";
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
