package seemesave.businesshub.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyRateModel {
   @SerializedName("newsfeed")
   @Expose
   private float newsfeed;
   @SerializedName("vendorpost")
   @Expose
   private float vendorpost;
   @SerializedName("supplierpost")
   @Expose
   private float supplierpost;
   @SerializedName("post")
   @Expose
   private float post;
   @SerializedName("homeadvert")
   @Expose
   private float homeadvert;
   @SerializedName("bestdeal")
   @Expose
   private float bestdeal;
   @SerializedName("exclusivedeal")
   @Expose
   private float exclusivedeal;
   @SerializedName("cantmissdeal")
   @Expose
   private float cantmissdeal;
   @SerializedName("vendorpromotion")
   @Expose
   private float vendorpromotion;
   @SerializedName("featuredstore")
   @Expose
   private float featuredstore;
   @SerializedName("featuredbrand")
   @Expose
   private float featuredbrand;
   @SerializedName("story")
   @Expose
   private float story;
   @SerializedName("payproductsingle")
   @Expose
   private float payproductsingle;
   @SerializedName("payproductcombo")
   @Expose
   private float payproductcombo;
   @SerializedName("payproductbuy1get1")
   @Expose
   private float payproductbuy1get1;
   @SerializedName("payproduct")
   @Expose
   private float payproduct;
   public CurrencyRateModel(){}

   public float getNewsfeed() {
      return newsfeed;
   }

   public void setNewsfeed(float newsfeed) {
      this.newsfeed = newsfeed;
   }

   public float getVendorpost() {
      return vendorpost;
   }

   public void setVendorpost(float vendorpost) {
      this.vendorpost = vendorpost;
   }

   public float getSupplierpost() {
      return supplierpost;
   }

   public void setSupplierpost(float supplierpost) {
      this.supplierpost = supplierpost;
   }

   public float getPost() {
      return post;
   }

   public void setPost(float post) {
      this.post = post;
   }

   public float getHomeadvert() {
      return homeadvert;
   }

   public void setHomeadvert(float homeadvert) {
      this.homeadvert = homeadvert;
   }

   public float getBestdeal() {
      return bestdeal;
   }

   public void setBestdeal(float bestdeal) {
      this.bestdeal = bestdeal;
   }

   public float getExclusivedeal() {
      return exclusivedeal;
   }

   public void setExclusivedeal(float exclusivedeal) {
      this.exclusivedeal = exclusivedeal;
   }

   public float getCantmissdeal() {
      return cantmissdeal;
   }

   public void setCantmissdeal(float cantmissdeal) {
      this.cantmissdeal = cantmissdeal;
   }

   public float getVendorpromotion() {
      return vendorpromotion;
   }

   public void setVendorpromotion(float vendorpromotion) {
      this.vendorpromotion = vendorpromotion;
   }

   public float getFeaturedstore() {
      return featuredstore;
   }

   public void setFeaturedstore(float featuredstore) {
      this.featuredstore = featuredstore;
   }

   public float getFeaturedbrand() {
      return featuredbrand;
   }

   public void setFeaturedbrand(float featuredbrand) {
      this.featuredbrand = featuredbrand;
   }

   public float getStory() {
      return story;
   }

   public void setStory(float story) {
      this.story = story;
   }

   public float getPayproductsingle() {
      return payproductsingle;
   }

   public void setPayproductsingle(float payproductsingle) {
      this.payproductsingle = payproductsingle;
   }

   public float getPayproductcombo() {
      return payproductcombo;
   }

   public void setPayproductcombo(float payproductcombo) {
      this.payproductcombo = payproductcombo;
   }

   public float getPayproductbuy1get1() {
      return payproductbuy1get1;
   }

   public void setPayproductbuy1get1(float payproductbuy1get1) {
      this.payproductbuy1get1 = payproductbuy1get1;
   }

   public float getPayproduct() {
      return payproduct;
   }

   public void setPayproduct(float payproduct) {
      this.payproduct = payproduct;
   }
}
