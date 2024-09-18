package com.mobile.seemesave.view_model.seeall;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.bookmark.Bookmark;
import com.mobile.seemesave.api.bookmark.BookmarkApi;
import com.mobile.seemesave.api.newsfeed.NewsFeedApi;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.res.FeedRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

public class BookAllPostViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<String> category_id;
    private MutableLiveData<ArrayList<FeedModel>> dealList;
    public BookAllPostViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        category_id = new MutableLiveData<>();
        category_id.setValue("");
        dealList = new MutableLiveData<>();
        dealList.setValue(new ArrayList<>());
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

    public MutableLiveData<ArrayList<FeedModel>> getDealList() {
        return dealList;
    }

    public void setDealList(ArrayList<FeedModel> dealList) {
        this.dealList.setValue(dealList);
    }

    public MutableLiveData<String> getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id.setValue(category_id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiSuccessResult(FeedRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            setDealList(res.getPostList());
            if (offset.getValue() == 0) {
                String data = new Gson().toJson(res, new TypeToken<FeedRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        G.getUserID(),
                        "BookmarkPost",
                        data,
                        String.valueOf(category_id.getValue()),
                        ""
                );
            }
        }
    }

    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "BookmarkPost", String.valueOf(category_id.getValue()));
            if (!TextUtils.isEmpty(data)) {
                FeedRes localRes = GsonUtils.getInstance().fromJson(data, FeedRes.class);
                setDealList(localRes.getPostList());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData() {
        BookmarkApi.getAllBookmarkPosts(category_id.getValue(), offset.getValue(), 20);
    }

}
