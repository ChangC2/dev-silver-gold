package com.mobile.seemesave.view_model.menu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.api.store.StoreApi;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.res.StoreRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class LocationViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<ArrayList<StoreModel>> mStoreList;


    public LocationViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        mStoreList = new MutableLiveData<>();
        mStoreList.setValue(new ArrayList<>());

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

    public MutableLiveData<ArrayList<StoreModel>> getStoreList() {
        return mStoreList;
    }

    public void setStoreList(ArrayList<StoreModel> mStoreList) {
        this.mStoreList.setValue(mStoreList);
    }
    public void loadData(double lat, double lng) {
        setIsBusy(true);
        StoreApi.getStoreListFromLocation(lat, lng);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(StoreRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<StoreModel> list = res.getData();
            setStoreList(list);
        }
    }
}
