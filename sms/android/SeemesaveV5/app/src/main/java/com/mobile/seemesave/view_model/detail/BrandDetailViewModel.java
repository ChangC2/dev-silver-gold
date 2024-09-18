package com.mobile.seemesave.view_model.detail;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.brand.BrandApi;
import com.mobile.seemesave.model.common.BrandModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.res.BrandPromotionRes;
import com.mobile.seemesave.model.res.PromotionRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class BrandDetailViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> brandId;
    private MutableLiveData<BrandModel> brandInfo;
    private MutableLiveData<ArrayList<PromotionOneModel>> collectList;
    private MutableLiveData<ArrayList<PromotionOneModel>> deliverList;
    private MutableLiveData<ArrayList<PromotionModel>> promotionList;
    private MutableLiveData<ArrayList<PromotionModel>> promotionCollectList;
    private MutableLiveData<ArrayList<PromotionModel>> promotionDeliverList;

    public BrandDetailViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        brandId = new MutableLiveData<>();
        brandId.setValue(-1);
        brandInfo = new MutableLiveData<>();
        brandInfo.setValue(new BrandModel());
        collectList = new MutableLiveData<>();
        collectList.setValue(new ArrayList<>());
        deliverList = new MutableLiveData<>();
        deliverList.setValue(new ArrayList<>());
        promotionList = new MutableLiveData<>();
        promotionList.setValue(new ArrayList<>());
        promotionCollectList = new MutableLiveData<>();
        promotionCollectList.setValue(new ArrayList<>());
        promotionDeliverList = new MutableLiveData<>();
        promotionDeliverList.setValue(new ArrayList<>());
        EventBus.getDefault().register(this);
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "BrandDetail", String.valueOf(brandId.getValue()));
            if (!TextUtils.isEmpty(data)) {
                BrandPromotionRes localRes = GsonUtils.getInstance().fromJson(data, BrandPromotionRes.class);
                setCollectList(localRes.getCollectList());
                setDeliverList(localRes.getDeliverList());
                setPromotionList(localRes.getPromotionList());
                setBrandInfo(localRes.getBrandInfo());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public MutableLiveData<Integer> getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId.setValue(brandId);
    }

    public MutableLiveData<BrandModel> getBrandInfo() {
        return brandInfo;
    }

    public void setBrandInfo(BrandModel brandInfo) {
        this.brandInfo.setValue(brandInfo);;
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

    public MutableLiveData<ArrayList<PromotionModel>> getPromotionCollectList() {
        return promotionCollectList;
    }

    public void setPromotionCollectList(ArrayList<PromotionModel> promotionCollectList) {
        this.promotionCollectList.setValue(promotionCollectList);
    }

    public MutableLiveData<ArrayList<PromotionModel>> getPromotionDeliverList() {
        return promotionDeliverList;
    }

    public void setPromotionDeliverList(ArrayList<PromotionModel> promotionDeliverList) {
        this.promotionDeliverList.setValue(promotionDeliverList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessResult(BrandPromotionRes res) {
        setIsBusy(false);
        if (res.isStatus()) {

            ArrayList<PromotionOneModel> cList = res.getCollectList();
            setCollectList(cList);
            ArrayList<PromotionOneModel> dList = res.getDeliverList();
            setDeliverList(dList);
            ArrayList<PromotionModel> pList = res.getPromotionList();
            setPromotionList(pList);
            setBrandInfo(res.getBrandInfo());

            String data = new Gson().toJson(res, new TypeToken<BrandPromotionRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "BrandDetail",
                    data,
                    String.valueOf(brandId.getValue()),
                    ""
            );
        }
    }

    public void loadData(int id) {
        BrandApi.getBrandPromotion(id);
    }
}
