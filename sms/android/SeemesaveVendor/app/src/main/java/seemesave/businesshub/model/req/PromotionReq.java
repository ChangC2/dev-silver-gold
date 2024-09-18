package seemesave.businesshub.model.req;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PromotionReq {
    @SerializedName("promotion_type")
    @Expose
    private RequestBody promotion_type;
    @SerializedName("title")
    @Expose
    private RequestBody title;

    @SerializedName("sub_media")
    @Expose
    private MultipartBody.Part sub_media;

    @SerializedName("description")
    @Expose
    private RequestBody description;

    @SerializedName("start_date")
    @Expose
    private RequestBody start_date;


    @SerializedName("end_date")
    @Expose
    private RequestBody end_date;

    @SerializedName("store_id")
    @Expose
    private RequestBody store_id;

    @SerializedName("total_money")
    @Expose
    private RequestBody total_money;

    @SerializedName("currency")
    @Expose
    private RequestBody currency;

    @SerializedName("is_private")
    @Expose
    private RequestBody is_private;

    @SerializedName("is_deliver")
    @Expose
    private RequestBody is_deliver;

    @SerializedName("need_data")
    @Expose
    private RequestBody need_data;

    @SerializedName("tag_list")
    @Expose
    private RequestBody tag_list;

    @SerializedName("place_id_list")
    @Expose
    private RequestBody place_id_list;

    @SerializedName("singleString")
    @Expose
    private RequestBody singleString;

    @SerializedName("comboString")
    @Expose
    private RequestBody comboString;

    @SerializedName("buy1get1String")
    @Expose
    private RequestBody buy1get1String;

    @SerializedName("pay_singleString")
    @Expose
    private RequestBody pay_singleString;

    @SerializedName("pay_comboString")
    @Expose
    private RequestBody pay_comboString;

    @SerializedName("pay_buy1get1String")
    @Expose
    private RequestBody pay_buy1get1String;

    public PromotionReq(){}

    public RequestBody getPromotion_type() {
        return promotion_type;
    }

    public void setPromotion_type(RequestBody promotion_type) {
        this.promotion_type = promotion_type;
    }

    public RequestBody getTitle() {
        return title;
    }

    public void setTitle(RequestBody title) {
        this.title = title;
    }

    public MultipartBody.Part getSub_media() {
        return sub_media;
    }

    public void setSub_media(MultipartBody.Part sub_media) {
        this.sub_media = sub_media;
    }

    public RequestBody getDescription() {
        return description;
    }

    public void setDescription(RequestBody description) {
        this.description = description;
    }

    public RequestBody getStart_date() {
        return start_date;
    }

    public void setStart_date(RequestBody start_date) {
        this.start_date = start_date;
    }

    public RequestBody getEnd_date() {
        return end_date;
    }

    public void setEnd_date(RequestBody end_date) {
        this.end_date = end_date;
    }

    public RequestBody getStore_id() {
        return store_id;
    }

    public void setStore_id(RequestBody store_id) {
        this.store_id = store_id;
    }

    public RequestBody getTotal_money() {
        return total_money;
    }

    public void setTotal_money(RequestBody total_money) {
        this.total_money = total_money;
    }

    public RequestBody getCurrency() {
        return currency;
    }

    public void setCurrency(RequestBody currency) {
        this.currency = currency;
    }

    public RequestBody getIs_private() {
        return is_private;
    }

    public void setIs_private(RequestBody is_private) {
        this.is_private = is_private;
    }

    public RequestBody getIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(RequestBody is_deliver) {
        this.is_deliver = is_deliver;
    }

    public RequestBody getNeed_data() {
        return need_data;
    }

    public void setNeed_data(RequestBody need_data) {
        this.need_data = need_data;
    }

    public RequestBody getTag_list() {
        return tag_list;
    }

    public void setTag_list(RequestBody tag_list) {
        this.tag_list = tag_list;
    }

    public RequestBody getPlace_id_list() {
        return place_id_list;
    }

    public void setPlace_id_list(RequestBody place_id_list) {
        this.place_id_list = place_id_list;
    }

    public RequestBody getSingleString() {
        return singleString;
    }

    public void setSingleString(RequestBody singleString) {
        this.singleString = singleString;
    }

    public RequestBody getComboString() {
        return comboString;
    }

    public void setComboString(RequestBody comboString) {
        this.comboString = comboString;
    }

    public RequestBody getBuy1get1String() {
        return buy1get1String;
    }

    public void setBuy1get1String(RequestBody buy1get1String) {
        this.buy1get1String = buy1get1String;
    }

    public RequestBody getPay_singleString() {
        return pay_singleString;
    }

    public void setPay_singleString(RequestBody pay_singleString) {
        this.pay_singleString = pay_singleString;
    }

    public RequestBody getPay_comboString() {
        return pay_comboString;
    }

    public void setPay_comboString(RequestBody pay_comboString) {
        this.pay_comboString = pay_comboString;
    }

    public RequestBody getPay_buy1get1String() {
        return pay_buy1get1String;
    }

    public void setPay_buy1get1String(RequestBody pay_buy1get1String) {
        this.pay_buy1get1String = pay_buy1get1String;
    }
}
