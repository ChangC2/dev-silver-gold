package com.mobile.seemesave.view_model.cart;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.view.cart.CartActivity;
import com.mobile.seemesave.view.cart.OrderHistoryActivity;

public class CartViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    public CartViewModel() {
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

    public void goHistory(CartActivity activity) {
        Intent intent = new Intent(activity, OrderHistoryActivity.class);
        activity.startActivity(intent);
    }
}
