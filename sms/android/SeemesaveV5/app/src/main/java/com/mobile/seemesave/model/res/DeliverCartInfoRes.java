package com.mobile.seemesave.model.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobile.seemesave.model.common.BuyGetProductCartModel;
import com.mobile.seemesave.model.common.ComboDealProductCartModel;
import com.mobile.seemesave.model.common.DeliverModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.SingleProductCartModel;

import java.util.ArrayList;

public class DeliverCartInfoRes {
    @SerializedName("CartInfo")
    @Expose
    private DeliverModel CartInfo;
    @SerializedName("DeliveryFee")
    @Expose
    private float DeliveryFee;
    @SerializedName("SingleProducts")
    @Expose
    private ArrayList<SingleProductCartModel> SingleProducts;
    @SerializedName("ComboDeals")
    @Expose
    private ArrayList<ComboDealProductCartModel> ComboDeals;
    @SerializedName("Buy1Get1FreeDeals")
    @Expose
    private ArrayList<BuyGetProductCartModel> Buy1Get1FreeDeals;
    private ArrayList<ProductOneModel> productList = new ArrayList<>();


    private boolean status;
    private String message;
    public DeliverCartInfoRes() {
        this.status = false;
        this.message = "";
        this.productList.clear();
    }

    public DeliverModel getCartInfo() {
        return CartInfo;
    }

    public void setCartInfo(DeliverModel cartInfo) {
        CartInfo = cartInfo;
    }

    public float getDeliveryFee() {
        return DeliveryFee;
    }

    public void setDeliveryFee(float deliveryFee) {
        DeliveryFee = deliveryFee;
    }

    public ArrayList<SingleProductCartModel> getSingleProducts() {
        return SingleProducts;
    }

    public void setSingleProducts(ArrayList<SingleProductCartModel> singleProducts) {
        SingleProducts = singleProducts;
    }

    public ArrayList<ComboDealProductCartModel> getComboDeals() {
        return ComboDeals;
    }

    public void setComboDeals(ArrayList<ComboDealProductCartModel> comboDeals) {
        ComboDeals = comboDeals;
    }

    public ArrayList<BuyGetProductCartModel> getBuy1Get1FreeDeals() {
        return Buy1Get1FreeDeals;
    }

    public void setBuy1Get1FreeDeals(ArrayList<BuyGetProductCartModel> buy1Get1FreeDeals) {
        Buy1Get1FreeDeals = buy1Get1FreeDeals;
    }

    public ArrayList<ProductOneModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductOneModel> productList) {
        this.productList = productList;
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
