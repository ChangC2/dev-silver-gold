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
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.res.FeedRes;
import com.mobile.seemesave.model.res.StoreRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import java.util.ArrayList;

public class FavPostFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<ArrayList<FeedModel>> postList;
    public FavPostFragViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        postList = new MutableLiveData<>();
        postList.setValue(new ArrayList<>());

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

    public void setOffset(int offset) {
        this.offset.setValue(offset);
    }

    public MutableLiveData<ArrayList<FeedModel>> getPostList() {
        return postList;
    }

    public void setPostList(ArrayList<FeedModel> postList) {
        this.postList.setValue(postList);
    }

    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "FavPost", "");
            if (!TextUtils.isEmpty(data)) {
                FeedRes localRes = GsonUtils.getInstance().fromJson(data, FeedRes.class);
                ArrayList<FeedModel> list = localRes.getPostList();
                setPostList(list);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessLogin(FeedRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<FeedModel> list = res.getPostList();
            setPostList(list);
            if (offset.getValue() == 0) {
                String data = new Gson().toJson(res, new TypeToken<FeedRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        G.getUserID(),
                        "FavPost",
                        data,
                        "",
                        ""
                );
            }
        }
    }
    public void loadData() {
        FavouriteApi.getFavouritePost(offset.getValue(), 20);
    }
    public void loadFriendData(int user_id) {
        setIsBusy(true);
        FavouriteApi.getFollowingPost(user_id, offset.getValue(), 20);
    }
    public void loadDataSearch(String key) {
        setIsBusy(true);
        FavouriteApi.getFavouritePostSearch(key, 0, 100);
    }
}
