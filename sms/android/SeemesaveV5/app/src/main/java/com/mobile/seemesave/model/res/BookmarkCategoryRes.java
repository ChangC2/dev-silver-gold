package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.BookmarkModel;
import com.mobile.seemesave.model.common.SingleProductModel;

import java.util.ArrayList;

public class BookmarkCategoryRes {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<BookmarkModel> data;
    @SerializedName("message")
    @Expose
    private String message;



    public BookmarkCategoryRes(){
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<BookmarkModel> getData() {
        return data;
    }

    public void setData(ArrayList<BookmarkModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
