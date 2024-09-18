package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("order_no")
    @Expose
    private String order_no;
    @SerializedName("order_time")
    @Expose
    private String order_time;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("collection_time")
    @Expose
    private String collection_time;
    public OrderModel(){}
    public OrderModel(int id, String order_no, String order_time, String order, String collection_time){
        this.id = id;
        this.order_no = order_no;
        this.order_time = order_time;
        this.order = order;
        this.collection_time = collection_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCollection_time() {
        return collection_time;
    }

    public void setCollection_time(String collection_time) {
        this.collection_time = collection_time;
    }
}
