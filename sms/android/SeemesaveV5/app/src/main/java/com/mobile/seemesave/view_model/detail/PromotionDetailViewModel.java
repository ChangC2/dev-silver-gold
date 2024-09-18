package com.mobile.seemesave.view_model.detail;

import static com.mobile.seemesave.utils.ParseUtil.parseSingleProduct;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.promotion.PromotionApi;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.res.HomeRes;
import com.mobile.seemesave.model.res.ProductSingleRes;
import com.mobile.seemesave.model.res.PromotionRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class PromotionDetailViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<String> feedType;
    private MutableLiveData<PromotionOneModel> promotionInfo;
    private MutableLiveData<PromotionModel> data;
    private MutableLiveData<ArrayList<ProductOneModel>> singleProductList;

    private MutableLiveData<String> localID;

    public PromotionDetailViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        feedType = new MutableLiveData<>();
        feedType.setValue("");
        promotionInfo = new MutableLiveData<>();
        promotionInfo.setValue(new PromotionOneModel());
        data = new MutableLiveData<>();
        data.setValue(new PromotionModel());
        singleProductList = new MutableLiveData<>();
        singleProductList.setValue(new ArrayList<>());
        localID = new MutableLiveData<>();
        localID.setValue("");
        EventBus.getDefault().register(this);
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "PromotionDetail", this.feedType.getValue());
            if (!TextUtils.isEmpty(data)) {
                PromotionRes localRes = GsonUtils.getInstance().fromJson(data, PromotionRes.class);
                setPromotionInfo(localRes.getPromotionData());
                setData(localRes.getData());
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

    public MutableLiveData<PromotionOneModel> getPromotionInfo() {
        return promotionInfo;
    }

    public void setPromotionInfo(PromotionOneModel promotionInfo) {
        this.promotionInfo.setValue(promotionInfo);
    }
    public MutableLiveData<PromotionModel> getData() {
        return data;
    }

    public void setData(PromotionModel promotionInfo) {
        this.data.setValue(promotionInfo);
    }

    public MutableLiveData<String> getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType.setValue(feedType);
    }

    public MutableLiveData<Integer> getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset.setValue(offset);
    }


    public MutableLiveData<ArrayList<ProductOneModel>> getSingleProductList() {
        return singleProductList;
    }

    public void setSingleProductList(ArrayList<ProductOneModel> singleProductList) {
        this.singleProductList.setValue(singleProductList);
    }

    public MutableLiveData<String> getLocalID() {
        return localID;
    }

    public void setLocalID(String localID) {
        this.localID.setValue(localID);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessResult(PromotionRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setPromotionInfo(res.getPromotionData());
            setData(res.getData());
            String data = new Gson().toJson(res, new TypeToken<PromotionRes>() {
            }.getType());

            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "PromotionDetail",
                    data,
                    this.feedType.getValue(),
                    ""
            );
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessSingleProductResult(ProductSingleRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<ProductOneModel> tmpList = new ArrayList<>();
            tmpList.clear();
            if (promotionInfo.getValue() != null && promotionInfo.getValue().getStores() != null && promotionInfo.getValue().getStores().size() > 0) {
                tmpList.addAll(parseSingleProduct(res.getData(), promotionInfo.getValue().getFeed_type(), promotionInfo.getValue().getStores().get(0).getId()));
            }
            setSingleProductList(tmpList);
            String data = new Gson().toJson(res, new TypeToken<ProductSingleRes>() {
            }.getType());

            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "PromotionCategoryProduct",
                    data,
                    this.localID.getValue(),
                    ""
            );
        }
    }
    public void loadData(int id) {
        PromotionApi.getPromotionDetail(id);
    }
    public void loadProductData(int promotion_id, int category_id) {
        PromotionApi.getPromotionSingleProducts(promotion_id, category_id, offset.getValue(), 20);
    }


    public void loadLocalProductData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "PromotionCategoryProduct", this.localID.getValue());
            if (!TextUtils.isEmpty(data)) {
                ProductSingleRes localRes = GsonUtils.getInstance().fromJson(data, ProductSingleRes.class);

                ArrayList<ProductOneModel> tmpList = new ArrayList<>();
                tmpList.clear();
                if (promotionInfo.getValue() != null && promotionInfo.getValue().getStores() != null && promotionInfo.getValue().getStores().size() > 0) {
                    tmpList.addAll(parseSingleProduct(localRes.getData(), promotionInfo.getValue().getFeed_type(), promotionInfo.getValue().getStores().get(0).getId()));
                }
                setSingleProductList(tmpList);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
