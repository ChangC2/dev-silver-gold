package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PromoteReceiverModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("pay_to_promote")
    @Expose
    private int pay_to_promote;

    @SerializedName("Store")
    @Expose
    private StoreModel Store;

    @SerializedName("is_accepted")
    @Expose
    private boolean is_accepted;

    @SerializedName("is_rejected")
    @Expose
    private boolean is_rejected;

    @SerializedName("reject_reason")
    @Expose
    private String reject_reason;
    private boolean isCheck;

    public PromoteReceiverModel() {
        this.isCheck = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPay_to_promote() {
        return pay_to_promote;
    }

    public void setPay_to_promote(int pay_to_promote) {
        this.pay_to_promote = pay_to_promote;
    }

    public StoreModel getStore() {
        return Store;
    }

    public void setStore(StoreModel store) {
        Store = store;
    }

    public boolean isIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(boolean is_accepted) {
        this.is_accepted = is_accepted;
    }

    public boolean isIs_rejected() {
        return is_rejected;
    }

    public void setIs_rejected(boolean is_rejected) {
        this.is_rejected = is_rejected;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
