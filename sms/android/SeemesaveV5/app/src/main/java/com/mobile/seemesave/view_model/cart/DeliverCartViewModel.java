package com.mobile.seemesave.view_model.cart;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.cart.CartApi;
import com.mobile.seemesave.model.common.DeliverModel;
import com.mobile.seemesave.model.res.DeliverCartOrderRes;
import com.mobile.seemesave.model.res.DeliverHistoryRes;
import com.mobile.seemesave.model.res.NotificationRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class DeliverCartViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<ArrayList<DeliverModel>> cartList;
    private MutableLiveData<ArrayList<DeliverModel>> paidCartList;
    private MutableLiveData<ArrayList<DeliverModel>> unPaidCartList;
    private MutableLiveData<ArrayList<DeliverModel>> pendingCartList;
    private MutableLiveData<ArrayList<DeliverModel>> historyList;
    public DeliverCartViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        cartList = new MutableLiveData<>();
        cartList.setValue(new ArrayList<>());
        paidCartList = new MutableLiveData<>();
        paidCartList.setValue(new ArrayList<>());
        unPaidCartList = new MutableLiveData<>();
        unPaidCartList.setValue(new ArrayList<>());
        pendingCartList = new MutableLiveData<>();
        pendingCartList.setValue(new ArrayList<>());
        historyList = new MutableLiveData<>();
        historyList.setValue(new ArrayList<>());
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

    public MutableLiveData<ArrayList<DeliverModel>> getCartList() {
        return cartList;
    }
    public void setCartList(ArrayList<DeliverModel> cartList) {
        this.cartList.setValue(cartList);
    }
    public MutableLiveData<ArrayList<DeliverModel>> getUnPaidCartList() {
        return unPaidCartList;
    }
    public void setUnPaidCartList(ArrayList<DeliverModel> cartList) {
        this.unPaidCartList.setValue(cartList);
    }
    public MutableLiveData<ArrayList<DeliverModel>> getPaidCartList() {
        return paidCartList;
    }
    public void setPaidCartList(ArrayList<DeliverModel> cartList) {
        this.paidCartList.setValue(cartList);
    }
    public MutableLiveData<ArrayList<DeliverModel>> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(ArrayList<DeliverModel> historyList) {
        this.historyList.setValue(historyList);
    }

    public MutableLiveData<ArrayList<DeliverModel>> getPendingCartList() {
        return pendingCartList;
    }

    public void setPendingCartList(ArrayList<DeliverModel> pendingCartList) {
        this.pendingCartList.setValue(pendingCartList);
    }

    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "DeliverCart", "");
            if (!TextUtils.isEmpty(data)) {
                DeliverCartOrderRes localRes = GsonUtils.getInstance().fromJson(data, DeliverCartOrderRes.class);
                setData(localRes);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData() {
        CartApi.getDeliverCartAll();
    }
    public void loadLocalHistoryData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "DeliverCartHistory", "");
            if (!TextUtils.isEmpty(data)) {
                DeliverHistoryRes localRes = GsonUtils.getInstance().fromJson(data, DeliverHistoryRes.class);
                setHistoryData(localRes);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadHistory() {
        CartApi.getDeliverHistory(0, 100);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(DeliverCartOrderRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setData(res);
            String data = new Gson().toJson(res, new TypeToken<DeliverCartOrderRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "DeliverCart",
                    data,
                    "",
                    ""
            );
        }
    }
    private void setData(DeliverCartOrderRes res) {
        ArrayList<DeliverModel> tmpPaidList = new ArrayList<>();
        ArrayList<DeliverModel> tmpUnPaidList = new ArrayList<>();
        ArrayList<DeliverModel> tmpPendingList = new ArrayList<>();
        tmpPaidList.clear();
        tmpUnPaidList.clear();
        tmpPendingList.clear();
        for (int i = 0; i < res.getData().size(); i++) {
            DeliverModel oneModel = res.getData().get(i);
            oneModel.setName(oneModel.getStore().getName());
            oneModel.setAddress(oneModel.getStore().getAddress());
            oneModel.setImage_url(oneModel.getStore().getLogo());
            oneModel.setRating(oneModel.getStore().getRating());
            oneModel.setLat(oneModel.getStore().getCoordinates().get(0));
            oneModel.setLon(oneModel.getStore().getCoordinates().get(1));
            oneModel.setCount(oneModel.getProductCount());
            if (oneModel.getCart_status() < 2) {
                tmpUnPaidList.add(oneModel);
            } else {
                if (oneModel.getCart_status() == 10) {
                    tmpPendingList.add(oneModel);
                } else {
                    oneModel.setPrice(oneModel.getTotal_price());
                    oneModel.setStatus(oneModel.getCart_status());
                    if (TextUtils.isEmpty(oneModel.getDelivery_from_time())){
                        oneModel.setTime("~");
                    }else{
                        String fromTime = oneModel.getDelivery_from_time();
                        String toTime = oneModel.getDelivery_to_time();
                        String strTime = String.format("%s ~ %s", fromTime, toTime);
                        oneModel.setTime(strTime);
                    }
                    tmpPaidList.add(oneModel);
                }
            }
        }
        setUnPaidCartList(tmpUnPaidList);
        setPaidCartList(tmpPaidList);
        setPendingCartList(tmpPendingList);
        setCartList(res.getData());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessHistory(DeliverHistoryRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setHistoryData(res);
            String data = new Gson().toJson(res, new TypeToken<DeliverHistoryRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "DeliverCartHistory",
                    data,
                    "",
                    ""
            );
        }
    }
    private void setHistoryData(DeliverHistoryRes res) {
        ArrayList<DeliverModel> tmpPaidList = new ArrayList<>();
        tmpPaidList.clear();
        for (int i = 0; i < res.getData().size(); i++) {
            DeliverModel oneModel = res.getData().get(i);
            oneModel.setName(oneModel.getStore().getName());
            oneModel.setAddress(oneModel.getStore().getAddress());
            oneModel.setImage_url(oneModel.getStore().getLogo());
            oneModel.setRating(oneModel.getStore().getRating());
            oneModel.setLat(oneModel.getStore().getCoordinates().get(0));
            oneModel.setLon(oneModel.getStore().getCoordinates().get(1));
            oneModel.setCount(oneModel.getProductCount());
            oneModel.setPrice(oneModel.getTotal_price());
            oneModel.setStatus(oneModel.getCart_status());
            if (TextUtils.isEmpty(oneModel.getDelivery_from_time())){
                oneModel.setTime("~");
            }else{
                String fromTime = oneModel.getDelivery_from_time();
                String toTime = oneModel.getDelivery_to_time();
                String strTime = String.format("%s ~ %s", fromTime, toTime);
                oneModel.setTime(strTime);
            }
            tmpPaidList.add(oneModel);
        }
        setHistoryList(tmpPaidList);
    }
    public void deleteCart(int id) {
        setIsBusy(true);
        CartApi.deleteDeliverCart(id);
    }
}