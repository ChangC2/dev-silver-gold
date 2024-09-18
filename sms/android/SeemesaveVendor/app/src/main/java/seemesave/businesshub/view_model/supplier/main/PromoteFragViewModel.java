package seemesave.businesshub.view_model.supplier.main;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

import seemesave.businesshub.api.ads.AdsApi;
import seemesave.businesshub.api.promote.Promote;
import seemesave.businesshub.api.promote.PromoteApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.AdsModel;
import seemesave.businesshub.model.common.PayPromoteModel;
import seemesave.businesshub.model.res.AdsBestDealListRes;
import seemesave.businesshub.model.res.PaytoPromoteListRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.GsonUtils;

public class PromoteFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<ArrayList<PayPromoteModel>> dataList;
    public PromoteFragViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);

        dataList = new MutableLiveData<>();
        dataList.setValue(new ArrayList<>());
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

    public MutableLiveData<Integer> getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset.setValue(offset);
    }

    public MutableLiveData<ArrayList<PayPromoteModel>> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<PayPromoteModel> dataList) {
        this.dataList.setValue(dataList);
    }

    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "PaytoPromote", "");
            if (!TextUtils.isEmpty(data)) {
                PaytoPromoteListRes localRes = GsonUtils.getInstance().fromJson(data, PaytoPromoteListRes.class);
                setDataList(localRes.getDataList());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData(String keyword) {
        PromoteApi.getPaytoPromoteList(keyword, offset.getValue(), 20);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessData(PaytoPromoteListRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setDataList(res.getDataList());
            if (offset.getValue() == 0) {
                String data = new Gson().toJson(res, new TypeToken<PaytoPromoteListRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        App.getUserID(),
                        "PaytoPromote",
                        data,
                        "",
                        ""
                );
            }
        }
    }
}
