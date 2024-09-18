package com.mobile.seemesave.view_model.main;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mobile.seemesave.api.favourite.FavouriteApi;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.res.BrandRes;
import com.mobile.seemesave.model.res.ProductListRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import java.util.ArrayList;

public class FavBrandFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<ArrayList<TrendingBrandModel>> brandList;
    public FavBrandFragViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
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
    public MutableLiveData<Integer> getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset.setValue(offset);
    }
    public MutableLiveData<ArrayList<TrendingBrandModel>> getBrandList() {
        return brandList;
    }
    public void setBrandList(ArrayList<TrendingBrandModel> topBrandList) {
        this.brandList.setValue(topBrandList);
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "FavBrand", "");
            if (!TextUtils.isEmpty(data)) {
                BrandRes localRes = GsonUtils.getInstance().fromJson(data, BrandRes.class);
                ArrayList<TrendingBrandModel> list = localRes.getData();
                setBrandList(list);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessResult(BrandRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<TrendingBrandModel> list = res.getData();
            setBrandList(list);
            if (offset.getValue() == 0) {
                String data = new Gson().toJson(res, new TypeToken<BrandRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        G.getUserID(),
                        "FavBrand",
                        data,
                        "",
                        ""
                );
            }
        }
    }
    public void loadData() {
        FavouriteApi.getFavouriteBrand(offset.getValue(), 20);
    }
    public void loadFriendData(int user_id) {
        setIsBusy(true);
        FavouriteApi.getFollowingFriend(user_id, offset.getValue(), 20);
    }
    public void loadDataSearch(String key) {
        setIsBusy(true);
        FavouriteApi.getFavouriteBrandSearch(key, 0, 100);
    }
}
