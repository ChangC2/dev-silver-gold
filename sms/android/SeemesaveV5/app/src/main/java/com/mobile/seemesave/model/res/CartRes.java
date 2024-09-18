package com.mobile.seemesave.model.res;

public class CartRes {
    private boolean status;
    private String message = "Something went wrong";
    private String type = "collect";
    public CartRes(){}

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
