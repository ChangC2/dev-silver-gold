package com.mobile.seemesave.view_model.seeall;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.newsfeed.NewsFeedApi;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.res.FeedRes;
import com.mobile.seemesave.model.res.StoryBrandRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class AllStoryBrandViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> id;

    private MutableLiveData<ArrayList<TrendingBrandModel>> brandList;
    public AllStoryBrandViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        id = new MutableLiveData<>();
        id.setValue(-1);
        brandList = new MutableLiveData<>();
        brandList.setValue(new ArrayList<>());
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

    public MutableLiveData<ArrayList<TrendingBrandModel>> getBrandList() {
        return brandList;
    }

    public void setBrandList(ArrayList<TrendingBrandModel> brandList) {
        this.brandList.setValue(brandList);
    }

    public MutableLiveData<Integer> getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id.setValue(id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiSuccessResult(StoryBrandRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setBrandList(res.getBrands());
            String data = new Gson().toJson(res, new TypeToken<StoryBrandRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "AllStoryBrand",
                    data,
                    String.valueOf(id.getValue()),
                    ""
            );
        }
    }

    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "AllStoryBrand", String.valueOf(id.getValue()));
            if (!TextUtils.isEmpty(data)) {
                StoryBrandRes localRes = GsonUtils.getInstance().fromJson(data, StoryBrandRes.class);
                setBrandList(localRes.getBrands());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData() {
        NewsFeedApi.getAllStoryBrand(id.getValue());
    }

}
