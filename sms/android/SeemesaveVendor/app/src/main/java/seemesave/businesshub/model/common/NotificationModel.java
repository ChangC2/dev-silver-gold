package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("senderName")
    @Expose
    private String senderName;
    @SerializedName("senderAvatar")
    @Expose
    private String senderAvatar;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("notificationMessage")
    @Expose
    private String notificationMessage;
    @SerializedName("notification_type")
    @Expose
    private String notification_type;
    @SerializedName("detailID")
    @Expose
    private String detailID;
    @SerializedName("detailPageType")
    @Expose
    private String detailPageType;
    @SerializedName("related_id")
    @Expose
    private String related_id;
    @SerializedName("is_read")
    @Expose
    private boolean is_read;
    @SerializedName("canAccept")
    @Expose
    private boolean canAccept;

    public NotificationModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getDetailID() {
        return detailID;
    }

    public void setDetailID(String detailID) {
        this.detailID = detailID;
    }

    public String getDetailPageType() {
        return detailPageType;
    }

    public void setDetailPageType(String detailPageType) {
        this.detailPageType = detailPageType;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public boolean isCanAccept() {
        return canAccept;
    }

    public void setCanAccept(boolean canAccept) {
        this.canAccept = canAccept;
    }
}
