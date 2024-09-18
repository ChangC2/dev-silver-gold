package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowerModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("is_blocked")
    @Expose
    private boolean is_blocked;
    @SerializedName("is_pending")
    @Expose
    private boolean is_pending;
    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("User")
    @Expose
    private UserModel User;
    @SerializedName("Store")
    @Expose
    private StoreModel Store;
    @SerializedName("Brand")
    @Expose
    private BrandModel Brand;
    public FollowerModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_blocked() {
        return is_blocked;
    }

    public void setIs_blocked(boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    public boolean isIs_pending() {
        return is_pending;
    }

    public void setIs_pending(boolean is_pending) {
        this.is_pending = is_pending;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserModel getUser() {
        return User;
    }

    public void setUser(UserModel user) {
        User = user;
    }

    public StoreModel getStore() {
        return Store;
    }

    public void setStore(StoreModel store) {
        Store = store;
    }

    public BrandModel getBrand() {
        return Brand;
    }

    public void setBrand(BrandModel brand) {
        Brand = brand;
    }
}
