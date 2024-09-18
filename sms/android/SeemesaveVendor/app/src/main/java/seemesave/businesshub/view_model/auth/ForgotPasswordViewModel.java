package seemesave.businesshub.view_model.auth;

import android.content.Intent;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import seemesave.businesshub.R;
import seemesave.businesshub.view.auth.ForgotPasswordActivity;
import seemesave.businesshub.view.auth.OTPActivity;
import seemesave.businesshub.view.auth.SignupActivity;
import seemesave.businesshub.model.res.LoginRes;

public class ForgotPasswordViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private String fcmToken = "";

    public ForgotPasswordViewModel(){
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

    public void sendClicked(ForgotPasswordActivity context, boolean is_mobile, String email, String country_code, String phone_number) {
        Intent intent = new Intent(context, OTPActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isMobile", is_mobile);
        intent.putExtra("email", email);
        intent.putExtra("countryCode", country_code);
        intent.putExtra("phoneNumber", phone_number);
        context.startActivity(intent);
        context.finish();
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
    public void gotoSignup(ForgotPasswordActivity context) {
        Intent intent = new Intent(context, SignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    private boolean checkValidate(ForgotPasswordActivity context, String email, String password) {
        if (TextUtils.isEmpty(email)){
            context.showMessages(context.getString(R.string.txt_please_enter_email));
            return false;
        }
        if (TextUtils.isEmpty(password)){
            context.showMessages(context.getString(R.string.txt_please_enter_password));
            return false;
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteLogin(LoginRes res) {
        setIsBusy(false);
    }
}
