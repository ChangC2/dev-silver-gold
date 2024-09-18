package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.model.common.AdsModel;

import java.util.ArrayList;

public class AdsListRes {
    @SerializedName("data")
    @Expose
    private ArrayList<AdsModel> dataList;
    @SerializedName("totalCount")
    @Expose
    private int totalCount;
    @SerializedName("app_name")
    @Expose
    private String app_name;
    @SerializedName("model_name")
    @Expose
    private String model_name;
    private boolean status;
    private String message;

    public AdsListRes() {}

    public ArrayList<AdsModel> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<AdsModel> dataList) {
        this.dataList = dataList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }
}
