package com.mobile.seemesave.view_model.detail;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.store.StoreApi;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.res.BrandPromotionRes;
import com.mobile.seemesave.model.res.StorePromotionRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class StoreDetailViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> storeId;
    private MutableLiveData<StoreModel> storeInfo;
    private MutableLiveData<ArrayList<PromotionOneModel>> collectList;
    private MutableLiveData<ArrayList<PromotionOneModel>> deliverList;
    private MutableLiveData<ArrayList<PromotionModel>> promotionList;

    public StoreDetailViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        storeId = new MutableLiveData<>();
        storeId.setValue(-1);
        storeInfo = new MutableLiveData<>();
        storeInfo.setValue(new StoreModel());
        collectList = new MutableLiveData<>();
        collectList.setValue(new ArrayList<>());
        deliverList = new MutableLiveData<>();
        deliverList.setValue(new ArrayList<>());
        promotionList = new MutableLiveData<>();
        promotionList.setValue(new ArrayList<>());

        EventBus.getDefault().register(this);
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "StoreDetail", String.valueOf(storeId.getValue()));
            if (!TextUtils.isEmpty(data)) {
                StorePromotionRes localRes = GsonUtils.getInstance().fromJson(data, StorePromotionRes.class);
                setCollectList(localRes.getCollectList());
                setDeliverList(localRes.getDeliverList());
                setPromotionList(localRes.getPromotionList());
                setStoreInfo(localRes.getStoreInfo());
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

    public MutableLiveData<Integer> getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId.setValue(storeId);
    }

    public MutableLiveData<StoreModel> getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreModel brandInfo) {
        this.storeInfo.setValue(brandInfo);;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessResult(StorePromotionRes res) {
        setIsBusy(false);
        if (res.isStatus()) {

            ArrayList<PromotionOneModel> cList = res.getCollectList();
            setCollectList(cList);
            ArrayList<PromotionOneModel> dList = res.getDeliverList();
            setDeliverList(dList);
            ArrayList<PromotionModel> pList = res.getPromotionList();
            setPromotionList(pList);
            setStoreInfo(res.getStoreInfo());
            String data = new Gson().toJson(res, new TypeToken<StorePromotionRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "StoreDetail",
                    data,
                    String.valueOf(storeId.getValue()),
                    ""
            );
        }
    }

    public void loadData(int id) {
        StoreApi.getStorePromotion(id);
    }
}
