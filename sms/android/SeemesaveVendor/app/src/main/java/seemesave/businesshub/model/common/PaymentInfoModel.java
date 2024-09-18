package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentInfoModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("total_amount")
    @Expose
    private float total_amount;
    @SerializedName("remain_amount")
    @Expose
    private float remain_amount;
    @SerializedName("today_amount")
    @Expose
    private float today_amount;
    @SerializedName("is_paid")
    @Expose
    private boolean is_paid;
    @SerializedName("Currency")
    @Expose
    private CurrencyModel Currency;
    public PaymentInfoModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public float getRemain_amount() {
        return remain_amount;
    }

    public void setRemain_amount(float remain_amount) {
        this.remain_amount = remain_amount;
    }

    public float getToday_amount() {
        return today_amount;
    }

    public void setToday_amount(float today_amount) {
        this.today_amount = today_amount;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public CurrencyModel getCurrency() {
        return Currency;
    }

    public void setCurrency(CurrencyModel currency) {
        Currency = currency;
    }
}
