package com.mobile.seemesave.view_model.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoryPlayerViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;

    public StoryPlayerViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public MutableLiveData<Boolean> getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy.setValue(isBusy);
    }
}
