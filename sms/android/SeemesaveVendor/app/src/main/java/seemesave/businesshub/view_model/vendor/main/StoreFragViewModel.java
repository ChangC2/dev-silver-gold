package seemesave.businesshub.view_model.vendor.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.res.StoreListRes;

public class StoreFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<String> keyword;
    private MutableLiveData<ArrayList<StoreModel>> mStoreList;


    public StoreFragViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        keyword = new MutableLiveData<>();
        keyword.setValue("");
        mStoreList = new MutableLiveData<>();
        mStoreList.setValue(new ArrayList<>());

        EventBus.getDefault().register(this);

//        ApiService.getStoreList(offset.getValue(), 20, keyword.getValue());
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

    public MutableLiveData<ArrayList<StoreModel>> getStoreList() {
        return mStoreList;
    }

    public void setStoreList(ArrayList<StoreModel> mStoreList) {
        this.mStoreList.setValue(mStoreList);
    }

    public MutableLiveData<Integer> getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset.setValue(offset);
    }

    public MutableLiveData<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword.setValue(keyword);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteStoreList(StoreListRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<StoreModel> list = res.getData();
            setStoreList(list);
        }
    }
}
