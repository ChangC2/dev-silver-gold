package com.mobile.seemesave.view_model.seeall;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.newsfeed.NewsFeedApi;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.res.StoryStoreRes;
import com.mobile.seemesave.model.res.StoryStoreRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class AllStoryStoreViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> id;

    private MutableLiveData<ArrayList<StoreModel>> stores;
    public AllStoryStoreViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        id = new MutableLiveData<>();
        id.setValue(-1);
        stores = new MutableLiveData<>();
        stores.setValue(new ArrayList<>());
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

    public MutableLiveData<ArrayList<StoreModel>> getStores() {
        return stores;
    }

    public void setStores(ArrayList<StoreModel> stores) {
        this.stores.setValue(stores);
    }

    public MutableLiveData<Integer> getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id.setValue(id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiSuccessResult(StoryStoreRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setStores(res.getStores());
            String data = new Gson().toJson(res, new TypeToken<StoryStoreRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "AllStoryStore",
                    data,
                    String.valueOf(id.getValue()),
                    ""
            );
        }
    }

    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "AllStoryStore", String.valueOf(id.getValue()));
            if (!TextUtils.isEmpty(data)) {
                StoryStoreRes localRes = GsonUtils.getInstance().fromJson(data, StoryStoreRes.class);
                setStores(localRes.getStores());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData() {
        NewsFeedApi.getAllStoryStore(id.getValue());
    }

}
