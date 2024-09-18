package com.mobile.seemesave.view_model.auth;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.mobile.seemesave.view.auth.ResetPasswordActivity;
import com.mobile.seemesave.model.res.LoginRes;

public class ResetPasswordViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private String fcmToken = "";

    public ResetPasswordViewModel(){
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

    public void changeClicked(ResetPasswordActivity context, String password) {
//        Intent intent = new Intent(context, OTPActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("isMobile", is_mobile);
//        intent.putExtra("email", email);
//        intent.putExtra("countryCode", country_code);
//        intent.putExtra("phoneNumber", phone_number);
//        context.startActivity(intent);
//        context.finish();
//        if (checkValidate(context, email, password)){
//            LoginReq req = new LoginReq();
//            req.setUserName(email);
//            req.setPassword(password);
//            req.setDeviceId(SystemUtils.getDeviceID(context));
//            req.setDeviceName(SystemUtils.getDeviceName());
//            req.setDeviceToken(fcmToken);
//            req.setUserType("user");
//            req.setDeviceType("android");
//            req.setLatitude("");
//            req.setLongitude("");
//
//            setIsBusy(true);
//            ApiService.doLoginEmail(req);
//        }
    }
    public void gotoSignup(Context context) {
//        Intent intent = new Intent(context, SignupActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        context.startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessLogin(LoginRes res) {
        setIsBusy(false);
    }
}
