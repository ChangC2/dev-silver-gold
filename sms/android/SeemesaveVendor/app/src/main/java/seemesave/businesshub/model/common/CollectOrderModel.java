package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CollectOrderModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("security_code")
    @Expose
    private String security_code;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("is_paid")
    @Expose
    private boolean is_paid;

    @SerializedName("order_time")
    @Expose
    private String order_time;

    @SerializedName("EstimateTime")
    @Expose
    private String EstimateTime;

    @SerializedName("estimate_date")
    @Expose
    private String estimate_date;

    @SerializedName("estimate_hour")
    @Expose
    private int estimate_hour;

    @SerializedName("is_being_picked")
    @Expose
    private boolean is_being_picked;

    @SerializedName("is_pending")
    @Expose
    private boolean is_pending;

    @SerializedName("is_ready")
    @Expose
    private boolean is_ready;

    @SerializedName("is_finished")
    @Expose
    private boolean is_finished;

    @SerializedName("finish_time")
    @Expose
    private String finish_time;

    @SerializedName("Store")
    @Expose
    private StoreModel Store;

    @SerializedName("User")
    @Expose
    private UserModel User;


    public CollectOrderModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecurity_code() {
        return security_code;
    }

    public void setSecurity_code(String security_code) {
        this.security_code = security_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getEstimateTime() {
        return EstimateTime;
    }

    public void setEstimateTime(String estimateTime) {
        EstimateTime = estimateTime;
    }

    public String getEstimate_date() {
        return estimate_date;
    }

    public void setEstimate_date(String estimate_date) {
        this.estimate_date = estimate_date;
    }

    public int getEstimate_hour() {
        return estimate_hour;
    }

    public void setEstimate_hour(int estimate_hour) {
        this.estimate_hour = estimate_hour;
    }

    public boolean isIs_being_picked() {
        return is_being_picked;
    }

    public void setIs_being_picked(boolean is_being_picked) {
        this.is_being_picked = is_being_picked;
    }

    public boolean isIs_ready() {
        return is_ready;
    }

    public void setIs_ready(boolean is_ready) {
        this.is_ready = is_ready;
    }

    public boolean isIs_finished() {
        return is_finished;
    }

    public void setIs_finished(boolean is_finished) {
        this.is_finished = is_finished;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public StoreModel getStore() {
        return Store;
    }

    public void setStore(StoreModel store) {
        Store = store;
    }

    public UserModel getUser() {
        return User;
    }

    public void setUser(UserModel user) {
        User = user;
    }

    public boolean isIs_pending() {
        return is_pending;
    }

    public void setIs_pending(boolean is_pending) {
        this.is_pending = is_pending;
    }
}
