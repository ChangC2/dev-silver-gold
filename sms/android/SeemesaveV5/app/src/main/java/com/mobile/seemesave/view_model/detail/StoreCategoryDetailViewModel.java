package com.mobile.seemesave.view_model.detail;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mobile.seemesave.api.favourite.FavouriteApi;
import com.mobile.seemesave.api.store.StoreApi;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.res.StoreRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class StoreCategoryDetailViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> category_id;
    private MutableLiveData<ArrayList<StoreModel>> storeList;
    public StoreCategoryDetailViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        category_id = new MutableLiveData<>();
        category_id.setValue(0);
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
    public MutableLiveData<Integer> getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int offset) {
        this.category_id.setValue(offset);
    }
    public MutableLiveData<ArrayList<StoreModel>> getStoreList() {
        return storeList;
    }
    public void setStoreList(ArrayList<StoreModel> storeList) {
        this.storeList.setValue(storeList);
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "StoreCategoryDetail", String.valueOf(category_id.getValue()));
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
            String data = new Gson().toJson(res, new TypeToken<StoreRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "StoreCategoryDetail",
                    data,
                    String.valueOf(category_id.getValue()),
                    ""
            );
        }
    }
    public void loadData() {
        StoreApi.getStoreListFromCategory(category_id.getValue());
    }
}
