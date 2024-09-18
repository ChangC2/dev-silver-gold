package com.mobile.seemesave.view_model.checkout;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.model.res.AlertCountRes;
import com.mobile.seemesave.view.checkout.CheckoutDetailActivity;
import com.mobile.seemesave.view.checkout.CheckoutFinishActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CheckoutDetailViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;

    public CheckoutDetailViewModel(){
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
    public void goNext(CheckoutDetailActivity activity) {
        Intent intent = new Intent(activity, CheckoutFinishActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

}