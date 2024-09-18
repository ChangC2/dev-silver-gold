package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import seemesave.businesshub.model.common.BrandModel;
import seemesave.businesshub.model.common.NotificationModel;

public class BrandListRes {
    @SerializedName("data")
    @Expose
    private ArrayList<BrandModel> data;
    @SerializedName("totalCount")
    @Expose
    private int totalCount;
    private boolean status;
    private String message;

    public BrandListRes() {}

    public ArrayList<BrandModel> getData() {
        return data;
    }

    public void setData(ArrayList<BrandModel> data) {
        this.data = data;
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
}
