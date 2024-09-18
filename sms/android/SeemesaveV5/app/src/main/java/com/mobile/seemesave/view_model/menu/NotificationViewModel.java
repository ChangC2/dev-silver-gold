package com.mobile.seemesave.view_model.menu;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.notification.NotificationApi;
import com.mobile.seemesave.model.common.NotificationModel;
import com.mobile.seemesave.model.res.FeedRes;
import com.mobile.seemesave.model.res.NotificationRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class NotificationViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<ArrayList<NotificationModel>> notificationList;

    public NotificationViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        notificationList = new MutableLiveData<>();
        notificationList.setValue(new ArrayList<>());
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
    public MutableLiveData<ArrayList<NotificationModel>> getNotificationList() {
        return notificationList;
    }
    public void setNotificationList(ArrayList<NotificationModel> notificationList) {
        this.notificationList.setValue(notificationList);
    }

    public MutableLiveData<Integer> getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset.setValue(offset);
    }

    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "Notification", "");
            if (!TextUtils.isEmpty(data)) {
                NotificationRes localRes = GsonUtils.getInstance().fromJson(data, NotificationRes.class);
                setNotificationList(localRes.getNotificationList());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData() {
        NotificationApi.getNotificationAll(offset.getValue(), 20);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(NotificationRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setNotificationList(res.getNotificationList());
            if (offset.getValue() == 0) {
                String data = new Gson().toJson(res, new TypeToken<NotificationRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        G.getUserID(),
                        "Notification",
                        data,
                        "",
                        ""
                );
            }
        }
    }


}
