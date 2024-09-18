package seemesave.businesshub.view_model.vendor.menu;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.listener.ClickListener;
import seemesave.businesshub.model.res.AlertCountRes;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view.auth.LoginActivity;
import seemesave.businesshub.view.vendor.menu.PostGroupActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MenuViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;

    public MenuViewModel(){
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

    public void goNotification(PostGroupActivity activity) {
    }
    public void goEdit(PostGroupActivity activity) {
    }
    public void goLogout(PostGroupActivity activity) {
        ClickListener listener = new ClickListener() {
            @Override
            public void onClick(boolean flag) {
                App.logout();
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        };
        G.showDlg(activity, activity.getString(R.string.logout_confirm), listener, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiSuccessResult(AlertCountRes res) {
        setIsBusy(false);
    }


}
