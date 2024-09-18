package seemesave.businesshub.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import seemesave.businesshub.model.common.CurrencyRateModel;

public class RateListRes {
   @SerializedName("rateList")
   @Expose
   private CurrencyRateModel rateList;
   @SerializedName("currency")
   @Expose
   private String currency;
   @SerializedName("cpcValue")
   @Expose
   private float cpcValue;
   @SerializedName("cplValue")
   @Expose
   private float cplValue;
   @SerializedName("cphValue")
   @Expose
   private float cphValue;
   @SerializedName("cprValue")
   @Expose
   private float cprValue;
   @SerializedName("cpmValue")
   @Expose
   private float cpmValue;

   private boolean status;
   private String message;
   public RateListRes(){
      this.status = false;
      this.message = "Something went wrong!";
   }

   public CurrencyRateModel getRateList() {
      return rateList;
   }

   public void setRateList(CurrencyRateModel rateList) {
      this.rateList = rateList;
   }

   public String getCurrency() {
      return currency;
   }

   public void setCurrency(String currency) {
      this.currency = currency;
   }

   public float getCpcValue() {
      return cpcValue;
   }

   public void setCpcValue(float cpcValue) {
      this.cpcValue = cpcValue;
   }

   public float getCplValue() {
      return cplValue;
   }

   public void setCplValue(float cplValue) {
      this.cplValue = cplValue;
   }

   public float getCphValue() {
      return cphValue;
   }

   public void setCphValue(float cphValue) {
      this.cphValue = cphValue;
   }

   public float getCprValue() {
      return cprValue;
   }

   public void setCprValue(float cprValue) {
      this.cprValue = cprValue;
   }

   public float getCpmValue() {
      return cpmValue;
   }

   public void setCpmValue(float cpmValue) {
      this.cpmValue = cpmValue;
   }

   public boolean isStatus() {
      return status;
   }

   public void setStatus(boolean status) {
      this.status = status;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
