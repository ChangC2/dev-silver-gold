package com.mobile.seemesave.view_model.detail;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.post.PostApi;
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

public class VideoPlayViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<Integer> post_id;
    private MutableLiveData<ArrayList<FeedModel>> postList;
    public VideoPlayViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        post_id = new MutableLiveData<>();
        post_id.setValue(-1);
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

    public MutableLiveData<Integer> getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id.setValue(post_id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessPost(FeedRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<FeedModel> list = res.getPostList();
            setPostList(list);
            if (offset.getValue() == 0) {
                String data = new Gson().toJson(res, new TypeToken<FeedRes>() {
                }.getType());
                DatabaseQueryClass.getInstance().insertData(
                        G.getUserID(),
                        "VideoDetail",
                        data,
                        String.valueOf(post_id.getValue()),
                        ""
                );
            }
        } else {

        }
    }
    public void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "VideoDetail", String.valueOf(post_id.getValue()));
            if (!TextUtils.isEmpty(data)) {
                FeedRes localRes = GsonUtils.getInstance().fromJson(data, FeedRes.class);
                ArrayList<FeedModel> list = localRes.getPostList();
                setPostList(list);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadData() {
        PostApi.getRelatedMediaList(post_id.getValue(), offset.getValue(), 100);
    }
}
