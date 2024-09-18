package com.mobile.seemesave.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyModel {
   @SerializedName("id")
   @Expose
   private int id;
   @SerializedName("simple")
   @Expose
   private String simple;
   @SerializedName("iso")
   @Expose
   private String iso;
   @SerializedName("full")
   @Expose
   private String full;
   @Override
   public String toString() {
      String mark = simple + "(" + iso + ")";
      return mark;
   }
   public CurrencyModel(){}

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getSimple() {
      return simple;
   }

   public void setSimple(String simple) {
      this.simple = simple;
   }

   public String getIso() {
      return iso;
   }

   public void setIso(String iso) {
      this.iso = iso;
   }

   public String getFull() {
      return full;
   }

   public void setFull(String full) {
      this.full = full;
   }
}
