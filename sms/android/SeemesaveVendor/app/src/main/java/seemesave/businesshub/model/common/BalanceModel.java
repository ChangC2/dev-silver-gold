package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BalanceModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("amount")
    @Expose
    private float amount;

    @SerializedName("Currency")
    @Expose
    private CurrencyModel currency;

    @SerializedName("str")
    @Expose
    private String str;

    public BalanceModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
