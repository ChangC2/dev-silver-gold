package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import seemesave.businesshub.model.common.BankDetailModel;
import seemesave.businesshub.model.common.BrandModel;
import seemesave.businesshub.model.common.CategoryModel;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.SupplierModel;
import seemesave.businesshub.model.common.TimeZoneModel;
import seemesave.businesshub.model.common.UnitModel;

public class BankDetailRes {
    @SerializedName("data")
    @Expose
    private BankDetailModel data;

    private boolean status;
    private String message;
    public BankDetailRes(){
        this.status = false;
        this.message = "Something went wrong!";
    }

    public BankDetailModel getData() {
        return data;
    }

    public void setData(BankDetailModel data) {
        this.data = data;
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
