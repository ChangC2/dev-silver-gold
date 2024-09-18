package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertCountModel {
    @SerializedName("shopping_list_count")
    @Expose
    private int shopping_list_count;

    @SerializedName("shopping_cart_count")
    @Expose
    private int shopping_cart_count;

    @SerializedName("deliver_cart_count")
    @Expose
    private int deliver_cart_count;

    @SerializedName("notification_count")
    @Expose
    private int notification_count;

    public AlertCountModel() {}

    public int getShopping_list_count() {
        return shopping_list_count;
    }

    public void setShopping_list_count(int shopping_list_count) {
        this.shopping_list_count = shopping_list_count;
    }

    public int getShopping_cart_count() {
        return shopping_cart_count;
    }

    public void setShopping_cart_count(int shopping_cart_count) {
        this.shopping_cart_count = shopping_cart_count;
    }

    public int getDeliver_cart_count() {
        return deliver_cart_count;
    }

    public void setDeliver_cart_count(int deliver_cart_count) {
        this.deliver_cart_count = deliver_cart_count;
    }

    public int getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(int notification_count) {
        this.notification_count = notification_count;
    }
}
