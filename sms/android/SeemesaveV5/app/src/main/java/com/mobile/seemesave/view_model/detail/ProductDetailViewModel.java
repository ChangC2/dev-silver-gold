package com.mobile.seemesave.view_model.detail;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mobile.seemesave.api.product.ProductApi;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.res.ProductPromotionRes;
import com.mobile.seemesave.model.res.ProductRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class ProductDetailViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<String> barcode;
    private MutableLiveData<ProductDetailModel> productInfo;
    private MutableLiveData<ArrayList<PromotionOneModel>> collectList;
    private MutableLiveData<ArrayList<PromotionOneModel>> deliverList;
    private MutableLiveData<ArrayList<PromotionModel>> promotionList;

    public ProductDetailViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        barcode = new MutableLiveData<>();
        barcode.setValue("");
        productInfo = new MutableLiveData<>();
        productInfo.setValue(new ProductDetailModel());
        collectList = new MutableLiveData<>();
        collectList.setValue(new ArrayList<>());
        deliverList = new MutableLiveData<>();
        deliverList.setValue(new ArrayList<>());
        promotionList = new MutableLiveData<>();
        promotionList.setValue(new ArrayList<>());

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

    public MutableLiveData<ProductDetailModel> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductDetailModel brandInfo) {
        this.productInfo.setValue(brandInfo);;
    }

    public MutableLiveData<ArrayList<PromotionOneModel>> getCollectList() {
        return collectList;
    }

    public void setCollectList(ArrayList<PromotionOneModel> productList) {
        this.collectList.setValue(productList);
    }
    public MutableLiveData<ArrayList<PromotionOneModel>> getDeliverList() {
        return deliverList;
    }

    public void setDeliverList(ArrayList<PromotionOneModel> productList) {
        this.deliverList.setValue(productList);
    }
    public MutableLiveData<ArrayList<PromotionModel>> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(ArrayList<PromotionModel> promotionList) {
        this.promotionList.setValue(promotionList);
    }
    public MutableLiveData<String> getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.setValue(barcode);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessResult(ProductPromotionRes res) {
        setIsBusy(false);
        if (res.isStatus()) {

            ArrayList<PromotionOneModel> cList = res.getCollectList();
            setCollectList(cList);
            ArrayList<PromotionOneModel> dList = res.getDeliverList();
            setDeliverList(dList);
            ArrayList<PromotionModel> pList = res.getPromotionList();
            setPromotionList(pList);
            setProductInfo(res.getProductInfo());
            String data = new Gson().toJson(res, new TypeToken<ProductPromotionRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "ProductDetail",
                    data,
                    barcode.getValue(),
                    ""
            );

        }
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "ProductDetail", barcode.getValue());
            if (!TextUtils.isEmpty(data)) {
                ProductPromotionRes localRes = GsonUtils.getInstance().fromJson(data, ProductPromotionRes.class);
                ArrayList<PromotionOneModel> cList = localRes.getCollectList();
                setCollectList(cList);
                ArrayList<PromotionOneModel> dList = localRes.getDeliverList();
                setDeliverList(dList);
                ArrayList<PromotionModel> pList = localRes.getPromotionList();
                setPromotionList(pList);
                setProductInfo(localRes.getProductInfo());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData() {
        ProductApi.getProductPromotion(barcode.getValue());
    }
}
