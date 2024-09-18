package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.common.SupplierModel;

public class StoreRes {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private StoreModel data;
    @SerializedName("message")
    @Expose
    private String message;

    public StoreRes(){

    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public StoreModel getData() {
        return data;
    }

    public void setData(StoreModel data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
