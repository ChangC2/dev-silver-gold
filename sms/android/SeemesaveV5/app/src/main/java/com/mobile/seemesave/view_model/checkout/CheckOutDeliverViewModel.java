package com.mobile.seemesave.view_model.checkout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.api.cart.CartApi;
import com.mobile.seemesave.model.common.BuyGetProductCartModel;
import com.mobile.seemesave.model.common.ComboDealProductCartModel;
import com.mobile.seemesave.model.common.CurrencyRateModel;
import com.mobile.seemesave.model.common.DeliverModel;
import com.mobile.seemesave.model.common.DeliverShipModel;
import com.mobile.seemesave.model.common.DeliverStatusModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.SingleProductCartModel;
import com.mobile.seemesave.model.req.CheckoutDeliverReq;
import com.mobile.seemesave.model.res.CheckoutRes;
import com.mobile.seemesave.model.res.DeliverCartBaseRes;
import com.mobile.seemesave.model.res.DeliverCartInfoRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class CheckOutDeliverViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<ArrayList<CurrencyRateModel>> currencyData;
    private MutableLiveData<DeliverShipModel> shipData;
    private MutableLiveData<DeliverStatusModel> statusData;
    private MutableLiveData<DeliverModel> CartInfo;
    private MutableLiveData<Float> DeliveryFee;
    private MutableLiveData<ArrayList<SingleProductCartModel>> SingleProducts;
    private MutableLiveData<ArrayList<ComboDealProductCartModel>> ComboDeals;
    private MutableLiveData<ArrayList<BuyGetProductCartModel>> Buy1Get1FreeDeals;
    private MutableLiveData<ArrayList<ProductOneModel>> productList;

    public CheckOutDeliverViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        currencyData = new MutableLiveData<>();
        currencyData.setValue(new ArrayList<>());
        shipData = new MutableLiveData<>();
        shipData.setValue(new DeliverShipModel());
        statusData = new MutableLiveData<>();
        statusData.setValue(new DeliverStatusModel());
        CartInfo = new MutableLiveData<>();
        CartInfo.setValue(new DeliverModel());
        DeliveryFee = new MutableLiveData<>();
        DeliveryFee.setValue(0.0F);
        SingleProducts = new MutableLiveData<>();
        SingleProducts.setValue(new ArrayList<>());
        ComboDeals = new MutableLiveData<>();
        ComboDeals.setValue(new ArrayList<>());
        Buy1Get1FreeDeals = new MutableLiveData<>();
        Buy1Get1FreeDeals.setValue(new ArrayList<>());
        productList = new MutableLiveData<>();
        productList.setValue(new ArrayList<>());
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus.getDefault().unregister(this);
    }
    public MutableLiveData<Boolean> getIsBusy() {
        return isBusy;
    }
    public void setIsBusy(boolean isBusy) {
        this.isBusy.setValue(isBusy);
    }

    public MutableLiveData<ArrayList<CurrencyRateModel>> getCurrencyData() {
        return currencyData;
    }
    public void setCurrencyData(ArrayList<CurrencyRateModel> deliverModels) {
        this.currencyData.setValue(deliverModels);
    }
    public MutableLiveData<DeliverShipModel> getShipData() {
        return shipData;
    }
    public void setShipData(DeliverShipModel shipData) {
        this.shipData.setValue(shipData);
    }
    public MutableLiveData<DeliverStatusModel> getStatusData() {
        return statusData;
    }
    public void setStatusData(DeliverStatusModel statusData) {
        this.statusData.setValue(statusData);
    }

    public MutableLiveData<DeliverModel> getCartInfo() {
        return CartInfo;
    }

    public void setCartInfo(DeliverModel cartInfo) {
        this.CartInfo.setValue(cartInfo);
    }

    public MutableLiveData<Float> getDeliveryFee() {
        return DeliveryFee;
    }

    public void setDeliveryFee(float deliveryFee) {
        this.DeliveryFee.setValue(deliveryFee);
    }

    public MutableLiveData<ArrayList<SingleProductCartModel>> getSingleProducts() {
        return SingleProducts;
    }

    public void setSingleProducts(ArrayList<SingleProductCartModel> singleProducts) {
        this.SingleProducts.setValue(singleProducts);
    }

    public MutableLiveData<ArrayList<ComboDealProductCartModel>> getComboDeals() {
        return ComboDeals;
    }

    public void setComboDeals(ArrayList<ComboDealProductCartModel> comboDeals) {
        this.ComboDeals.setValue(comboDeals);
    }

    public MutableLiveData<ArrayList<BuyGetProductCartModel>> getBuy1Get1FreeDeals() {
        return Buy1Get1FreeDeals;
    }

    public void setBuy1Get1FreeDeals(ArrayList<BuyGetProductCartModel> buy1Get1FreeDeals) {
        this.Buy1Get1FreeDeals.setValue(buy1Get1FreeDeals);
    }

    public MutableLiveData<ArrayList<ProductOneModel>> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductOneModel> productList) {
        this.productList.setValue(productList);
    }

    public void loadBaseInfo(int cart_id) {
        setIsBusy(true);
        CartApi.getDeliverBaseInfo(cart_id);
    }
    public void loadCartInfo(int cart_id) {
        setIsBusy(true);
        CartApi.getDeliverCartById(cart_id);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessBaseResult(DeliverCartBaseRes res) {
        if (res.isStatus()) {
            setCurrencyData(res.getCurrencyData());
            setShipData(res.getShipData());
            setStatusData(res.getStatusConstant());
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCartInfoResult(DeliverCartInfoRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setCartInfo(res.getCartInfo());
            setDeliveryFee(res.getDeliveryFee());
            setSingleProducts(res.getSingleProducts());
            setComboDeals(res.getComboDeals());
            setBuy1Get1FreeDeals(res.getBuy1Get1FreeDeals());
            setProductList(res.getProductList());
        }
    }

    public void checkOut(CheckoutDeliverReq req) {
        setIsBusy(true);
        CartApi.checkoutDeliver(req);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCheckoutResult(CheckoutRes res) {
        setIsBusy(false);
    }
}
