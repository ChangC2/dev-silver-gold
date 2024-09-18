package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiErrorModel {
   @SerializedName("status")
   @Expose
   private String status;
   @SerializedName("message")
   @Expose
   private String message;

   public ApiErrorModel(){
      status = "failure";
      message = "Something went wrong";
   }

   public String getStatus() {
      return status;
   }
   public String getMessage() {
      return message;
   }
}
