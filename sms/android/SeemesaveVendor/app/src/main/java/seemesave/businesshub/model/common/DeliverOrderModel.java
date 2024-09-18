package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliverOrderModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("collection_name")
    @Expose
    private String collection_name;

    @SerializedName("collection_from_time")
    @Expose
    private String collection_from_time;

    @SerializedName("collection_to_time")
    @Expose
    private String collection_to_time;

    @SerializedName("collection_contact_number")
    @Expose
    private String collection_contact_number;

    @SerializedName("collection_email")
    @Expose
    private String collection_email;

    @SerializedName("collection_street1")
    @Expose
    private String collection_street1;

    @SerializedName("collection_street2")
    @Expose
    private String collection_street2;

    @SerializedName("collection_suburb")
    @Expose
    private String collection_suburb;

    @SerializedName("collection_city")
    @Expose
    private String collection_city;

    @SerializedName("collection_state")
    @Expose
    private String collection_state;

    @SerializedName("collection_country")
    @Expose
    private String collection_country;

    @SerializedName("delivery_name")
    @Expose
    private String delivery_name;

    @SerializedName("delivery_from_time")
    @Expose
    private String delivery_from_time;

    @SerializedName("delivery_to_time")
    @Expose
    private String delivery_to_time;

    @SerializedName("delivery_contact_number")
    @Expose
    private String delivery_contact_number;

    @SerializedName("delivery_email")
    @Expose
    private String delivery_email;

    @SerializedName("delivery_postal_code")
    @Expose
    private String delivery_postal_code;

    @SerializedName("delivery_note")
    @Expose
    private String delivery_note;

    @SerializedName("delivery_street1")
    @Expose
    private String delivery_street1;

    @SerializedName("delivery_street2")
    @Expose
    private String delivery_street2;

    @SerializedName("delivery_suburb")
    @Expose
    private String delivery_suburb;

    @SerializedName("delivery_city")
    @Expose
    private String delivery_city;

    @SerializedName("delivery_state")
    @Expose
    private String delivery_state;

    @SerializedName("delivery_country")
    @Expose
    private String delivery_country;

    @SerializedName("order_time")
    @Expose
    private String order_time;

    @SerializedName("finished_time")
    @Expose
    private String finished_time;

    @SerializedName("cart_status")
    @Expose
    private int cart_status;

    @SerializedName("job_status")
    @Expose
    private String job_status;

    @SerializedName("total_price")
    @Expose
    private float total_price;

    @SerializedName("Store")
    @Expose
    private StoreModel Store;

    @SerializedName("User")
    @Expose
    private UserModel User;

    public DeliverOrderModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollection_name() {
        return collection_name;
    }

    public void setCollection_name(String collection_name) {
        this.collection_name = collection_name;
    }

    public String getCollection_from_time() {
        return collection_from_time;
    }

    public void setCollection_from_time(String collection_from_time) {
        this.collection_from_time = collection_from_time;
    }

    public String getCollection_to_time() {
        return collection_to_time;
    }

    public void setCollection_to_time(String collection_to_time) {
        this.collection_to_time = collection_to_time;
    }

    public String getCollection_contact_number() {
        return collection_contact_number;
    }

    public void setCollection_contact_number(String collection_contact_number) {
        this.collection_contact_number = collection_contact_number;
    }

    public String getCollection_email() {
        return collection_email;
    }

    public void setCollection_email(String collection_email) {
        this.collection_email = collection_email;
    }

    public String getCollection_street1() {
        return collection_street1;
    }

    public void setCollection_street1(String collection_street1) {
        this.collection_street1 = collection_street1;
    }

    public String getCollection_street2() {
        return collection_street2;
    }

    public void setCollection_street2(String collection_street2) {
        this.collection_street2 = collection_street2;
    }

    public String getCollection_suburb() {
        return collection_suburb;
    }

    public void setCollection_suburb(String collection_suburb) {
        this.collection_suburb = collection_suburb;
    }

    public String getCollection_city() {
        return collection_city;
    }

    public void setCollection_city(String collection_city) {
        this.collection_city = collection_city;
    }

    public String getCollection_state() {
        return collection_state;
    }

    public void setCollection_state(String collection_state) {
        this.collection_state = collection_state;
    }

    public String getCollection_country() {
        return collection_country;
    }

    public void setCollection_country(String collection_country) {
        this.collection_country = collection_country;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_from_time() {
        return delivery_from_time;
    }

    public void setDelivery_from_time(String delivery_from_time) {
        this.delivery_from_time = delivery_from_time;
    }

    public String getDelivery_to_time() {
        return delivery_to_time;
    }

    public void setDelivery_to_time(String delivery_to_time) {
        this.delivery_to_time = delivery_to_time;
    }

    public String getDelivery_contact_number() {
        return delivery_contact_number;
    }

    public void setDelivery_contact_number(String delivery_contact_number) {
        this.delivery_contact_number = delivery_contact_number;
    }

    public String getDelivery_email() {
        return delivery_email;
    }

    public void setDelivery_email(String delivery_email) {
        this.delivery_email = delivery_email;
    }

    public String getDelivery_postal_code() {
        return delivery_postal_code;
    }

    public void setDelivery_postal_code(String delivery_postal_code) {
        this.delivery_postal_code = delivery_postal_code;
    }

    public String getDelivery_note() {
        return delivery_note;
    }

    public void setDelivery_note(String delivery_note) {
        this.delivery_note = delivery_note;
    }

    public String getDelivery_street1() {
        return delivery_street1;
    }

    public void setDelivery_street1(String delivery_street1) {
        this.delivery_street1 = delivery_street1;
    }

    public String getDelivery_street2() {
        return delivery_street2;
    }

    public void setDelivery_street2(String delivery_street2) {
        this.delivery_street2 = delivery_street2;
    }

    public String getDelivery_suburb() {
        return delivery_suburb;
    }

    public void setDelivery_suburb(String delivery_suburb) {
        this.delivery_suburb = delivery_suburb;
    }

    public String getDelivery_city() {
        return delivery_city;
    }

    public void setDelivery_city(String delivery_city) {
        this.delivery_city = delivery_city;
    }

    public String getDelivery_state() {
        return delivery_state;
    }

    public void setDelivery_state(String delivery_state) {
        this.delivery_state = delivery_state;
    }

    public String getDelivery_country() {
        return delivery_country;
    }

    public void setDelivery_country(String delivery_country) {
        this.delivery_country = delivery_country;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getFinished_time() {
        return finished_time;
    }

    public void setFinished_time(String finished_time) {
        this.finished_time = finished_time;
    }

    public int getCart_status() {
        return cart_status;
    }

    public void setCart_status(int cart_status) {
        this.cart_status = cart_status;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
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
}
