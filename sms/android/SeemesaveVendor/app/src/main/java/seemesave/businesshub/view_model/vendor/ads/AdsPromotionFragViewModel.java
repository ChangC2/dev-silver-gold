package seemesave.businesshub.view_model.vendor.ads;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import seemesave.businesshub.api.ads.AdsApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.AdsModel;
import seemesave.businesshub.model.res.AdsListRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class AdsPromotionFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<String> app_name;
    private MutableLiveData<String> model_name;
    private MutableLiveData<ArrayList<AdsModel>> dataList;
    public AdsPromotionFragViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        app_name = new MutableLiveData<>();
        app_name.setValue("");
        model_name = new MutableLiveData<>();
        model_name.setValue("");
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

    public MutableLiveData<ArrayList<AdsModel>> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<AdsModel> dataList) {
        this.dataList.setValue(dataList);
    }

    public MutableLiveData<String> getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name.setValue(app_name);
    }

    public MutableLiveData<String> getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name.setValue(model_name);
    }

    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "AdsPromotion", "");
            if (!TextUtils.isEmpty(data)) {
                AdsListRes localRes = GsonUtils.getInstance().fromJson(data, AdsListRes.class);
                setDataList(localRes.getDataList());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData(String promotion_type, String keyword, String from_date, String end_date) {
        AdsApi.getAdsList(promotion_type, keyword, from_date, end_date, offset.getValue(), 20);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessData(AdsListRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            if (res.getModel_name().equalsIgnoreCase("VendorPromotion")) {
                setDataList(res.getDataList());
                setApp_name(res.getApp_name());
                setModel_name(res.getModel_name());
                if (offset.getValue() == 0) {
                    String data = new Gson().toJson(res, new TypeToken<AdsListRes>() {
                    }.getType());
                    DatabaseQueryClass.getInstance().insertData(
                            App.getUserID(),
                            "AdsPromotion",
                            data,
                            "",
                            ""
                    );
                }
            }
        }
    }
}