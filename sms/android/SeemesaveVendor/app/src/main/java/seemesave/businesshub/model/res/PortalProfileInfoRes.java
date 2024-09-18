package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.BalanceModel;
import seemesave.businesshub.model.common.BrandModel;
import seemesave.businesshub.model.common.CategoryModel;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.FollowerModel;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.common.SupplierModel;
import seemesave.businesshub.model.common.TimeZoneModel;
import seemesave.businesshub.model.common.UnitModel;
import seemesave.businesshub.model.common.UserPortalModel;

import java.util.ArrayList;

public class PortalProfileInfoRes {
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("data")
    @Expose
    private UserPortalModel data;

    @SerializedName("message")
    @Expose
    private String message;


    public PortalProfileInfoRes(){
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UserPortalModel getData() {
        return data;
    }

    public void setData(UserPortalModel data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
