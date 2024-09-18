package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserPortalModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("portal_token")
    @Expose
    private String portal_token;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("contact_number")
    @Expose
    private String contact_number;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("website")
    @Expose
    private String website;

    @SerializedName("followers")
    @Expose
    private ArrayList<FollowerModel> followers;

    @SerializedName("time_zone")
    @Expose
    private String time_zone;

    @SerializedName("time_offset")
    @Expose
    private int time_offset;

    @SerializedName("Currency")
    @Expose
    private CurrencyModel currency;

    @SerializedName("Balance")
    @Expose
    private ArrayList<BalanceModel> balance;

    @SerializedName("Stores")
    @Expose
    private ArrayList<StoreModel> stores;

    private boolean isCheck;

    public UserPortalModel(){
        this.isCheck = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPortal_token() {
        return portal_token;
    }

    public void setPortal_token(String portal_token) {
        this.portal_token = portal_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public int getTime_offset() {
        return time_offset;
    }

    public void setTime_offset(int time_offset) {
        this.time_offset = time_offset;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ArrayList<FollowerModel> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<FollowerModel> followers) {
        this.followers = followers;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public ArrayList<BalanceModel> getBalance() {
        return balance;
    }

    public void setBalance(ArrayList<BalanceModel> balance) {
        this.balance = balance;
    }

    public ArrayList<StoreModel> getStores() {
        return stores;
    }

    public void setStores(ArrayList<StoreModel> stores) {
        this.stores = stores;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
