package seemesave.businesshub.view_model.auth;

import static seemesave.businesshub.utils.SystemUtils.isValidEmail;
import static seemesave.businesshub.utils.SystemUtils.isValidMobile;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import seemesave.businesshub.R;
import seemesave.businesshub.api.user.UserApi;
import seemesave.businesshub.model.req.LoginReq;
import seemesave.businesshub.view.auth.ForgotPasswordActivity;
import seemesave.businesshub.view.auth.SignupActivity;
import seemesave.businesshub.model.res.LoginRes;
import seemesave.businesshub.view.auth.LoginActivity;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;

    public LoginViewModel() {
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

    public void onLogin(LoginActivity context, boolean isMobile, String email, String countryCode, String phoneNumber, String password) {
        if (checkValidate(context, isMobile, phoneNumber, email, password)) {
            setIsBusy(true);
            LoginReq req = new LoginReq();
            req.setEmail(email);
            req.setPassword(password);
            req.setCountryCode(countryCode);
            req.setPhoneNumber(phoneNumber);
            req.setRegister_with(isMobile ? "phone" : "email");
            UserApi.doLogin(req);
        }
    }

    public void gotoSignup(LoginActivity context) {
        Intent intent = new Intent(context, SignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();
    }

    public void gotoForgotPassword(LoginActivity context) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private boolean checkValidate(LoginActivity context, boolean isMobile, String phoneNumber, String email, String password) {
        if (isMobile) {
            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
                Toast.makeText(context, R.string.msg_valid_number, Toast.LENGTH_LONG).show();
                return false;
            } else {
                if (!isValidMobile(phoneNumber)) {
                    Toast.makeText(context, R.string.msg_valid_phone, Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        } else {
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(context, R.string.msg_empty_email, Toast.LENGTH_LONG).show();
                return false;
            } else {
                if (!isValidEmail(email)) {
                    Toast.makeText(context, R.string.msg_valid_email, Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteLogin(LoginRes res) {
        setIsBusy(false);
    }
}
