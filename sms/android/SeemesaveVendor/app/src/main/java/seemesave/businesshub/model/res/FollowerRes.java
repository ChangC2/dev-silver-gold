package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.FollowerModel;
import seemesave.businesshub.model.common.StoreModel;

import java.util.ArrayList;

public class FollowerRes {
    @SerializedName("data")
    @Expose
    private ArrayList<FollowerModel> data;

    @SerializedName("totalCount")
    @Expose
    private int totalCount;

    private boolean status;
    private String message;

    public FollowerRes(){
        data = new ArrayList<>();
        this.status = false;
        this.message = "Something went wrong!";
    }

    public ArrayList<FollowerModel> getData() {
        return data;
    }

    public void setData(ArrayList<FollowerModel> data) {
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
