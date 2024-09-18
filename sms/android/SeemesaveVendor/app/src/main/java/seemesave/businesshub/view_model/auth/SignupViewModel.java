package seemesave.businesshub.view_model.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import seemesave.businesshub.api.user.UserApi;
import seemesave.businesshub.model.common.UserLoginModel;
import seemesave.businesshub.model.req.LoginReq;
import seemesave.businesshub.model.req.SignupReq;
import seemesave.businesshub.model.res.LoginRes;

public class SignupViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<UserLoginModel> userData;


    public SignupViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        userData = new MutableLiveData<>();
        userData.setValue(null);
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

    public MutableLiveData<UserLoginModel> getUserData() {
        return userData;
    }

    public void setUserData(UserLoginModel userData) {
        this.userData.setValue(userData);
    }

    public void onLogin(LoginReq req) {
        setIsBusy(true);
        UserApi.doLogin(req);
    }
    public void onSignup(SignupReq req) {
        setIsBusy(true);
        UserApi.createPortal(req);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginComplete(LoginRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            UserLoginModel model = new UserLoginModel();
            model.setOwns(res.getOwns());
            model.setToken(res.getToken());
            model.setUser(res.getUser());
            setUserData(model);
        }
    }
}
