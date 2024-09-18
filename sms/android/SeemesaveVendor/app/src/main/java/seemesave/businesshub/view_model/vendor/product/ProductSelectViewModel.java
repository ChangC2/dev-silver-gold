package seemesave.businesshub.view_model.vendor.product;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import seemesave.businesshub.model.res.StoreListRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProductSelectViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    public ProductSelectViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
//        storeList = new MutableLiveData<>();
//        storeList.setValue(new ArrayList<>());
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

    public void getStoreList() {
    }
    public void getRateInfo(String currency) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessStoreList(StoreListRes res) {
        setIsBusy(false);
    }


}
