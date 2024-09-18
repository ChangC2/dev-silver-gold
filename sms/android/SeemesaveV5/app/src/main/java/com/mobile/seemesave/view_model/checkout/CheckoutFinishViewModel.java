package com.mobile.seemesave.view_model.checkout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.model.res.AlertCountRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CheckoutFinishViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;

    public CheckoutFinishViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiSuccessResult(AlertCountRes res) {
        setIsBusy(false);
    }


}