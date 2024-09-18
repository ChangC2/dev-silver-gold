package com.mobile.seemesave.view_model.detail;

import static com.mobile.seemesave.utils.ParseUtil.parseSingleProduct;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.newsfeed.NewsFeedApi;
import com.mobile.seemesave.api.promotion.PromotionApi;
import com.mobile.seemesave.model.common.BrandModel;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.PostModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.res.AdRes;
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

public class AdDetailViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<String> feedType;
    private MutableLiveData<FeedModel> feedInfo;
    private MutableLiveData<ArrayList<TrendingBrandModel>> brandList;

    public AdDetailViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        feedType = new MutableLiveData<>();
        feedType.setValue("");
        feedInfo = new MutableLiveData<>();
        feedInfo.setValue(new FeedModel());
        brandList = new MutableLiveData<>();
        brandList.setValue(new ArrayList<>());

        EventBus.getDefault().register(this);
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "HomeAdvert", this.feedType.getValue());
            if (!TextUtils.isEmpty(data)) {
                AdRes localRes = GsonUtils.getInstance().fromJson(data, AdRes.class);
                setFeedInfo(localRes.getFeedInfo());
                setBrandList(localRes.getBrandList());
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



    public MutableLiveData<String> getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType.setValue(feedType);
    }

    public MutableLiveData<FeedModel> getFeedInfo() {
        return feedInfo;
    }

    public void setFeedInfo(FeedModel feedInfo) {
        this.feedInfo.setValue(feedInfo);
    }

    public MutableLiveData<ArrayList<TrendingBrandModel>> getBrandList() {
        return brandList;
    }

    public void setBrandList(ArrayList<TrendingBrandModel> brandList) {
        this.brandList.setValue(brandList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessResult(AdRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setBrandList(res.getBrandList());
            setFeedInfo(res.getFeedInfo());
            String data = new Gson().toJson(res, new TypeToken<AdRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "HomeAdvert",
                    data,
                    this.feedType.getValue(),
                    ""
            );
        }
    }

    public void loadData(int id) {
        NewsFeedApi.getAdDetail(id);
    }

}
