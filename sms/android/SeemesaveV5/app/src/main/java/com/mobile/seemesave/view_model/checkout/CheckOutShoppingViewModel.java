package com.mobile.seemesave.view_model.checkout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.api.cart.CartApi;
import com.mobile.seemesave.model.common.BuyGetProductCartModel;
import com.mobile.seemesave.model.common.CurrencyRateModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.ShoppingModel;
import com.mobile.seemesave.model.common.ComboDealProductCartModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.ShoppingTimeModel;
import com.mobile.seemesave.model.common.SingleProductCartModel;
import com.mobile.seemesave.model.req.CheckoutShoppingReq;
import com.mobile.seemesave.model.res.CheckoutRes;
import com.mobile.seemesave.model.res.ExclusiveDealRes;
import com.mobile.seemesave.model.res.ShoppingCartBaseRes;
import com.mobile.seemesave.model.res.ShoppingCartInfoRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class CheckOutShoppingViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<ShoppingModel> CartInfo;
    private MutableLiveData<ArrayList<SingleProductCartModel>> SingleProducts;
    private MutableLiveData<ArrayList<ComboDealProductCartModel>> ComboDeals;
    private MutableLiveData<ArrayList<BuyGetProductCartModel>> Buy1Get1FreeDeals;
    private MutableLiveData<ArrayList<ProductOneModel>> productList;
    private MutableLiveData<ArrayList<ProductOneModel>> exclusiveDeals;
    private MutableLiveData<ArrayList<PromotionModel>> exclusiveList;
    private MutableLiveData<ArrayList<CurrencyRateModel>> currencyData;
    private MutableLiveData<ArrayList<ShoppingTimeModel>> dateList;

    public CheckOutShoppingViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        CartInfo = new MutableLiveData<>();
        CartInfo.setValue(new ShoppingModel());
        SingleProducts = new MutableLiveData<>();
        SingleProducts.setValue(new ArrayList<>());
        ComboDeals = new MutableLiveData<>();
        ComboDeals.setValue(new ArrayList<>());
        Buy1Get1FreeDeals = new MutableLiveData<>();
        Buy1Get1FreeDeals.setValue(new ArrayList<>());
        productList = new MutableLiveData<>();
        productList.setValue(new ArrayList<>());
        exclusiveDeals = new MutableLiveData<>();
        exclusiveDeals.setValue(new ArrayList<>());
        exclusiveList = new MutableLiveData<>();
        exclusiveList.setValue(new ArrayList<>());
        currencyData = new MutableLiveData<>();
        currencyData.setValue(new ArrayList<>());
        dateList = new MutableLiveData<>();
        dateList.setValue(new ArrayList<>());
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



    public MutableLiveData<ShoppingModel> getCartInfo() {
        return CartInfo;
    }

    public void setCartInfo(ShoppingModel cartInfo) {
        this.CartInfo.setValue(cartInfo);
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

    public MutableLiveData<ArrayList<ProductOneModel>> getExclusiveDeals() {
        return exclusiveDeals;
    }

    public void setExclusiveDeals(ArrayList<ProductOneModel> exclusiveDeals) {
        this.exclusiveDeals.setValue(exclusiveDeals);
    }

    public MutableLiveData<ArrayList<CurrencyRateModel>> getCurrencyData() {
        return currencyData;
    }

    public void setCurrencyData(ArrayList<CurrencyRateModel> currencyData) {
        this.currencyData .setValue(currencyData);
    }

    public MutableLiveData<ArrayList<ShoppingTimeModel>> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<ShoppingTimeModel> dateList) {
        this.dateList.setValue(dateList);
    }

    public MutableLiveData<ArrayList<PromotionModel>> getExclusiveList() {
        return exclusiveList;
    }

    public void setExclusiveList(ArrayList<PromotionModel> exclusiveList) {
        this.exclusiveList.setValue(exclusiveList);
    }

    public void loadBaseInfo(int cart_id) {
        CartApi.getAvailableTime(cart_id);
    }
    public void loadCartInfo(int cart_id) {
        CartApi.getShoppingCartById(cart_id);
    }
    public void loadExclusiveDeal(int cart_id) {
        setIsBusy(true);
        CartApi.getExcusiveDealsFromShoppingCart(cart_id);
    }
    public void checkOut(CheckoutShoppingReq req) {
        setIsBusy(true);
        CartApi.checkoutShopping(req);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCartBaseInfoResult(ShoppingCartBaseRes res) {
        if (res.isStatus()) {
            setCurrencyData(res.getCurrencyData());
            setDateList(res.getData());
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCartInfoResult(ShoppingCartInfoRes res) {
        if (res.isStatus()) {
            setCartInfo(res.getCartInfo());
            setSingleProducts(res.getSingleProducts());
            setComboDeals(res.getComboDeals());
            setBuy1Get1FreeDeals(res.getBuy1Get1FreeDeals());
            setProductList(res.getProductList());
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessExclusiveResult(ExclusiveDealRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setExclusiveDeals(res.getProductList());
            setExclusiveList(res.getData());
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCheckoutResult(CheckoutRes res) {
        setIsBusy(false);
    }

}
