package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import seemesave.businesshub.model.common.CollectOrderModel;
import seemesave.businesshub.model.common.DeliverOrderModel;

public class DeliverOrderListRes {
    @SerializedName("data")
    @Expose
    private ArrayList<DeliverOrderModel> dataList;
    @SerializedName("totalCount")
    @Expose
    private int totalCount;
    private boolean status;
    private String message;

    public DeliverOrderListRes() {}

    public ArrayList<DeliverOrderModel> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<DeliverOrderModel> dataList) {
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
}
