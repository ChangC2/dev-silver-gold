package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import seemesave.businesshub.model.common.BaseInfoModel;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.common.TimeZoneModel;

public class PromoteBaseInfoRes {
    @SerializedName("storeList")
    @Expose
    private ArrayList<StoreModel> storeList;

    @SerializedName("rate")
    @Expose
    private float rate;

    private boolean status;
    private String message;
    public PromoteBaseInfoRes(){
        this.status = false;
        this.message = "Something went wrong!";
    }

    public ArrayList<StoreModel> getStoreList() {
        return storeList;
    }

    public void setStoreList(ArrayList<StoreModel> storeList) {
        this.storeList = storeList;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

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
}
