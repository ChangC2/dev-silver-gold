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
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.res.BrandRes;
import com.mobile.seemesave.model.res.StoreRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import java.util.ArrayList;

public class FavStoreFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<ArrayList<StoreModel>> storeList;
    public FavStoreFragViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        storeList = new MutableLiveData<>();
        storeList.setValue(new ArrayList<>());

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
    public MutableLiveData<ArrayList<StoreModel>> getStoreList() {
        return storeList;
    }
    public void setStoreList(ArrayList<StoreModel> storeList) {
        this.storeList.setValue(storeList);
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "FavStore", "");
            if (!TextUtils.isEmpty(data)) {
                StoreRes localRes = GsonUtils.getInstance().fromJson(data, StoreRes.class);
                ArrayList<StoreModel> list = localRes.getData();
                setStoreList(list);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessResult(StoreRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<StoreModel> list = res.getData();
            setStoreList(list);
            if (offset.getValue() == 0) {
                String data = new Gson().toJson(res, new TypeToken<StoreRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        G.getUserID(),
                        "FavStore",
                        data,
                        "",
                        ""
                );
            }
        }
    }
    public void loadData() {
        FavouriteApi.getFavouriteStore(offset.getValue(), 20);
    }
    public void loadFriendData(int user_id) {
        setIsBusy(true);
        FavouriteApi.getFollowingStore(user_id, offset.getValue(), 20);
    }
    public void loadDataSearch(String key) {
        setIsBusy(true);
        FavouriteApi.getFavouriteStoreSearch(key, 0, 100);
    }
}
