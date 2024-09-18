package com.mobile.seemesave.view_model.menu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.model.res.AlertCountRes;
import com.mobile.seemesave.view.menu.ContactUsNewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ContactUsViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;

    public ContactUsViewModel(){
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

    public void goSpeak(ContactUsNewActivity activity) {
    }

    public void goEmail(ContactUsNewActivity activity) {
    }

    public void goPhone(ContactUsNewActivity activity) {
    }
}
