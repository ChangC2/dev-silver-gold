package seemesave.businesshub.view_model.vendor.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import seemesave.businesshub.model.res.LoginRes;

public class FavBrandFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    public FavBrandFragViewModel(){
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
    public void onCompleteLogin(LoginRes res) {
        setIsBusy(false);
    }
}
