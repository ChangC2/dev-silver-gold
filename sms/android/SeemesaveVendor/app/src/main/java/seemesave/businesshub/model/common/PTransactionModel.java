package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PTransactionModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("payment_reason")
    @Expose
    private String payment_reason;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private float price;

    @SerializedName("Currency")
    @Expose
    private CurrencyModel currency;

    @SerializedName("creator_name")
    @Expose
    private String creator_name;

    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("invoice_url")
    @Expose
    private String invoice_url;

    @SerializedName("order_number")
    @Expose
    private String order_number;


    public PTransactionModel(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayment_reason() {
        return payment_reason;
    }

    public void setPayment_reason(String payment_reason) {
        this.payment_reason = payment_reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getInvoice_url() {
        return invoice_url;
    }

    public void setInvoice_url(String invoice_url) {
        this.invoice_url = invoice_url;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
}
