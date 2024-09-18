package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenModel {
   @SerializedName("id")
   @Expose
   private String id;
   @SerializedName("name")
   @Expose
   private String name;
   @SerializedName("username")
   @Expose
   private String userName;
   @SerializedName("deviceType")
   @Expose
   private String deviceType;
   @SerializedName("tokenType")
   @Expose
   private String tokenType;
   @SerializedName("accessToken")
   @Expose
   private String accessToken;
   @SerializedName("encryptToken")
   @Expose
   private String encryptToken;

   public TokenModel(){}

   public String getId() {
      return id;
   }
   public void setId(String id) {
      this.id = id;
   }
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public String getUserName() {
      return userName;
   }
   public void setUserName(String userName) {
      this.userName = userName;
   }
   public String getDeviceType() {
      return deviceType;
   }
   public void setDeviceType(String deviceType) {
      this.deviceType = deviceType;
   }
   public String getTokenType() {
      return tokenType;
   }
   public void setTokenType(String tokenType) {
      this.tokenType = tokenType;
   }
   public String getAccessToken() {
      return accessToken;
   }
   public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
   }
   public String getEncryptToken() {
      return encryptToken;
   }
   public void setEncryptToken(String encryptToken) {
      this.encryptToken = encryptToken;
   }
}
