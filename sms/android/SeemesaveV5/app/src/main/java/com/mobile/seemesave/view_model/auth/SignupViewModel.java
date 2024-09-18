package com.mobile.seemesave.view_model.auth;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.mobile.seemesave.R;
import com.mobile.seemesave.api.user.UserApi;
import com.mobile.seemesave.model.req.SignupReq;
import com.mobile.seemesave.model.res.SignupRes;
import com.mobile.seemesave.view.auth.SignupActivity;
import com.mobile.seemesave.view.auth.LoginActivity;

public class SignupViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private String fcmToken = "";

    public SignupViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            fcmToken = token;
        });

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

    public void gotoLogin(SignupActivity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();
    }

    public void onSignup(SignupReq req) {
        setIsBusy(true);
        UserApi.doSignup(req);
    }
    public void onSendOtp(SignupReq req) {
        setIsBusy(true);
        UserApi.sendOtp(req);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessSignup(SignupRes res) {
        setIsBusy(false);
    }


}
