package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import seemesave.businesshub.model.common.BaseInfoModel;
import seemesave.businesshub.model.common.BrandModel;
import seemesave.businesshub.model.common.CategoryModel;
import seemesave.businesshub.model.common.CurrencyModel;
import seemesave.businesshub.model.common.StoreCategoryModel;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.common.SupplierModel;
import seemesave.businesshub.model.common.SupplierOneModel;
import seemesave.businesshub.model.common.TimeZoneModel;
import seemesave.businesshub.model.common.UnitModel;

import java.util.ArrayList;

public class BaseInfoRes {
    @SerializedName("currencyList")
    @Expose
    private ArrayList<CurrencyModel> currencyList;

    @SerializedName("storeCategoryList")
    @Expose
    private ArrayList<StoreCategoryModel> storeCategoryList;
    @SerializedName("timezoneList")
    @Expose
    private ArrayList<TimeZoneModel> timezoneList;

    @SerializedName("roleList")
    @Expose
    private ArrayList<String> roleList;

    @SerializedName("supplierList")
    @Expose
    private ArrayList<BaseInfoModel> supplierList;

    @SerializedName("brandList")
    @Expose
    private ArrayList<BaseInfoModel> brandList;

    @SerializedName("categoryList")
    @Expose
    private ArrayList<BaseInfoModel> categoryList;

    @SerializedName("unitList")
    @Expose
    private ArrayList<BaseInfoModel> unitList;

    @SerializedName("packList")
    @Expose
    private ArrayList<BaseInfoModel> packList;

    private boolean status;
    private String message;
    public BaseInfoRes(){
        this.status = false;
        this.message = "Something went wrong!";
    }

    public ArrayList<CurrencyModel> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(ArrayList<CurrencyModel> currencyList) {
        this.currencyList = currencyList;
    }

    public ArrayList<TimeZoneModel> getTimezoneList() {
        return timezoneList;
    }

    public void setTimezoneList(ArrayList<TimeZoneModel> timezoneList) {
        this.timezoneList = timezoneList;
    }

    public ArrayList<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(ArrayList<String> roleList) {
        this.roleList = roleList;
    }

    public ArrayList<BaseInfoModel> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(ArrayList<BaseInfoModel> supplierList) {
        this.supplierList = supplierList;
    }

    public ArrayList<BaseInfoModel> getBrandList() {
        return brandList;
    }

    public void setBrandList(ArrayList<BaseInfoModel> brandList) {
        this.brandList = brandList;
    }

    public ArrayList<BaseInfoModel> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<BaseInfoModel> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<BaseInfoModel> getUnitList() {
        return unitList;
    }

    public void setUnitList(ArrayList<BaseInfoModel> unitList) {
        this.unitList = unitList;
    }

    public ArrayList<BaseInfoModel> getPackList() {
        return packList;
    }

    public void setPackList(ArrayList<BaseInfoModel> packList) {
        this.packList = packList;
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

    public ArrayList<StoreCategoryModel> getStoreCategoryList() {
        return storeCategoryList;
    }

    public void setStoreCategoryList(ArrayList<StoreCategoryModel> storeCategoryList) {
        this.storeCategoryList = storeCategoryList;
    }
}
