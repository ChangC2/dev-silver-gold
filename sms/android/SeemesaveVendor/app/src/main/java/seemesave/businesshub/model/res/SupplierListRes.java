package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.FollowerModel;
import seemesave.businesshub.model.common.SupplierModel;

import java.util.ArrayList;

public class SupplierListRes {
    @SerializedName("data")
    @Expose
    private ArrayList<SupplierModel> data;

    @SerializedName("totalCount")
    @Expose
    private int totalCount;

    private boolean status;
    private String message;

    public SupplierListRes(){
        data = new ArrayList<>();
        this.status = false;
        this.message = "Something went wrong!";
    }

    public ArrayList<SupplierModel> getData() {
        return data;
    }

    public void setData(ArrayList<SupplierModel> data) {
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
