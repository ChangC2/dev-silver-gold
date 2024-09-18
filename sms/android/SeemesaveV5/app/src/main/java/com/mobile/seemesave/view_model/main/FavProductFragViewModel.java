package com.mobile.seemesave.view_model.main;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mobile.seemesave.api.favourite.FavouriteApi;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.model.res.ProductListRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import java.util.ArrayList;

public class FavProductFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<ArrayList<ProductDetailModel>> productList;

    public FavProductFragViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        productList = new MutableLiveData<>();
        productList.setValue(new ArrayList<>());

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
    public MutableLiveData<ArrayList<ProductDetailModel>> getProductList() {
        return productList;
    }
    public MutableLiveData<Integer> getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset.setValue(offset);
    }
    public void setProductList(ArrayList<ProductDetailModel> productList) {
        this.productList.setValue(productList);
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "FavProduct", "");
            if (!TextUtils.isEmpty(data)) {
                ProductListRes localRes = GsonUtils.getInstance().fromJson(data, ProductListRes.class);
                ArrayList<ProductDetailModel> pList = localRes.getData();
                setProductList(pList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessResult(ProductListRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<ProductDetailModel> pList = res.getData();
            setProductList(pList);
            if (offset.getValue() == 0) {
                String data = new Gson().toJson(res, new TypeToken<ProductListRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        G.getUserID(),
                        "FavProduct",
                        data,
                        "",
                        ""
                );
            }
        }
    }
    public void loadData() {
        FavouriteApi.getFavouriteProduct(offset.getValue(), 20);
    }
    public void loadFriendData(int user_id) {
        setIsBusy(true);
        FavouriteApi.getFollowingProduct(user_id, offset.getValue(), 20);
    }
    public void loadDataSearch(String key) {
        setIsBusy(true);
        FavouriteApi.getFavouriteProductSearch(key, 0, 100);
    }
}
