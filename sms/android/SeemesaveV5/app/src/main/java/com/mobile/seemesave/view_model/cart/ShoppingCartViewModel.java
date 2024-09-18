package com.mobile.seemesave.view_model.cart;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.cart.CartApi;
import com.mobile.seemesave.model.common.ShoppingModel;
import com.mobile.seemesave.model.res.DeliverCartOrderRes;
import com.mobile.seemesave.model.res.ShoppingCartOrderRes;
import com.mobile.seemesave.model.res.ShoppingHistoryRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class ShoppingCartViewModel  extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<ArrayList<ShoppingModel>> cartList;
    private MutableLiveData<ArrayList<ShoppingModel>> paidCartList;
    private MutableLiveData<ArrayList<ShoppingModel>> unPaidCartList;
    private MutableLiveData<ArrayList<ShoppingModel>> pendingCartList;
    private MutableLiveData<ArrayList<ShoppingModel>> historyList;
    public ShoppingCartViewModel() {
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
    public MutableLiveData<ArrayList<ShoppingModel>> getCartList() {
        return cartList;
    }
    public void setCartList(ArrayList<ShoppingModel> cartList) {
        this.cartList.setValue(cartList);
    }
    public MutableLiveData<ArrayList<ShoppingModel>> getUnPaidCartList() {
        return unPaidCartList;
    }
    public void setUnPaidCartList(ArrayList<ShoppingModel> cartList) {
        this.unPaidCartList.setValue(cartList);
    }
    public MutableLiveData<ArrayList<ShoppingModel>> getPaidCartList() {
        return paidCartList;
    }
    public void setPaidCartList(ArrayList<ShoppingModel> cartList) {
        this.paidCartList.setValue(cartList);
    }

    public MutableLiveData<ArrayList<ShoppingModel>> getPendingCartList() {
        return pendingCartList;
    }

    public void setPendingCartList(ArrayList<ShoppingModel> pendingCartList) {
        this.pendingCartList.setValue(pendingCartList);
    }

    public MutableLiveData<ArrayList<ShoppingModel>> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(ArrayList<ShoppingModel> historyList) {
        this.historyList.setValue(historyList);
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "ShoppingCart", "");
            if (!TextUtils.isEmpty(data)) {
                ShoppingCartOrderRes localRes = GsonUtils.getInstance().fromJson(data, ShoppingCartOrderRes.class);
                setData(localRes);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData() {
        CartApi.getShoppingCartAll();
    }
    public void loadLocalHistoryData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "ShoppingCartHistory", "");
            if (!TextUtils.isEmpty(data)) {
                ShoppingHistoryRes localRes = GsonUtils.getInstance().fromJson(data, ShoppingHistoryRes.class);
                setHistoryData(localRes);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadHistory() {
        CartApi.getShoppingHistory(0, 100);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(ShoppingCartOrderRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setData(res);
            String data = new Gson().toJson(res, new TypeToken<ShoppingCartOrderRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "ShoppingCart",
                    data,
                    "",
                    ""
            );
        }
    }
    private void setData(ShoppingCartOrderRes res) {
        ArrayList<ShoppingModel> tmpPaidList = new ArrayList<>();
        ArrayList<ShoppingModel> tmpUnPaidList = new ArrayList<>();
        ArrayList<ShoppingModel> tmpPendingList = new ArrayList<>();
        tmpPaidList.clear();
        tmpUnPaidList.clear();
        tmpPendingList.clear();
        for (int i = 0; i < res.getData().size(); i++) {
            ShoppingModel oneModel = res.getData().get(i);
            oneModel.setName(oneModel.getStore().getName());
            oneModel.setAddress(oneModel.getStore().getAddress());
            oneModel.setImage_url(oneModel.getStore().getLogo());
            oneModel.setRating(oneModel.getStore().getRating());
            oneModel.setLat(oneModel.getStore().getCoordinates().get(0));
            oneModel.setLon(oneModel.getStore().getCoordinates().get(1));
            oneModel.setCount(oneModel.getProductCount());
            if (oneModel.isIs_pending()) {
                tmpPendingList.add(oneModel);
            } else {
                if (oneModel.isIs_paid()) {
                    if (oneModel.isIs_ready()) {
                        oneModel.setStatus(2);
                    } else if (oneModel.isIs_finished()) {
                        oneModel.setStatus(3);
                    } else {
                        oneModel.setStatus(1);
                    }
                    if (TextUtils.isEmpty(oneModel.getEstimate_date()) || oneModel.getEstimate_date() == "null") {
                        oneModel.setTime(oneModel.getEstimate_time());
                    } else {
                        @SuppressLint("DefaultLocale")
                        String times = String.format("%s %02d:00~%02d:00", oneModel.getEstimate_date(), oneModel.getEstimate_hour(), oneModel.getEstimate_hour()+1);
                        oneModel.setTime(times);
                    }
                    tmpPaidList.add(oneModel);
                } else {
                    tmpUnPaidList.add(oneModel);
                }
            }

        }
        setPaidCartList(tmpPaidList);
        setUnPaidCartList(tmpUnPaidList);
        setPendingCartList(tmpPendingList);
        setCartList(res.getData());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessHistory(ShoppingHistoryRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setHistoryData(res);
            String data = new Gson().toJson(res, new TypeToken<ShoppingHistoryRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "ShoppingCartHistory",
                    data,
                    "",
                    ""
            );
        }
    }
    private void setHistoryData(ShoppingHistoryRes res) {
        ArrayList<ShoppingModel> tmpPaidList = new ArrayList<>();
        tmpPaidList.clear();
        for (int i = 0; i < res.getData().size(); i++) {
            ShoppingModel oneModel = res.getData().get(i);
            oneModel.setName(oneModel.getStore().getName());
            oneModel.setAddress(oneModel.getStore().getAddress());
            oneModel.setImage_url(oneModel.getStore().getLogo());
            oneModel.setRating(oneModel.getStore().getRating());
            oneModel.setLat(oneModel.getStore().getCoordinates().get(0));
            oneModel.setLon(oneModel.getStore().getCoordinates().get(1));
            oneModel.setCount(oneModel.getProductCount());
            if (oneModel.isIs_ready()) {
                oneModel.setStatus(2);
            } else if (oneModel.isIs_finished()) {
                oneModel.setStatus(3);
            } else {
                oneModel.setStatus(1);
            }
            if (TextUtils.isEmpty(oneModel.getEstimate_date()) || oneModel.getEstimate_date() == "null") {
                oneModel.setTime(oneModel.getEstimate_time());
            } else {
                @SuppressLint("DefaultLocale")
                String times = String.format("%s %02d:00~%02d:00", oneModel.getEstimate_date(), oneModel.getEstimate_hour(), oneModel.getEstimate_hour()+1);
                oneModel.setTime(times);
            }
            tmpPaidList.add(oneModel);
        }
        setHistoryList(tmpPaidList);
    }
}