package com.mobile.seemesave.view_model.main;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobile.seemesave.api.newsfeed.NewsFeedApi;
import com.mobile.seemesave.api.post.PostApi;
import com.mobile.seemesave.api.user.UserApi;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.PostModel;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.common.UserModel;
import com.mobile.seemesave.model.res.FeedRes;
import com.mobile.seemesave.model.res.PostRes;
import com.mobile.seemesave.model.res.UserRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;
import com.mobile.seemesave.view.menu.MenuActivity;
import com.mobile.seemesave.view.profile.EditProfileActivity;
import com.mobile.seemesave.view.profile.FriendActivity;
import com.mobile.seemesave.view.main.ProductLikeActivity;
import com.mobile.seemesave.view.main.StoreFollowActivity;

import java.util.ArrayList;

public class AccountFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<UserModel> userInfo;
    private MutableLiveData<ArrayList<StoreModel>> topStoreList;
    private MutableLiveData<ArrayList<TrendingBrandModel>> topBrandList;
    private MutableLiveData<ArrayList<ProductDetailModel>> topProductList;
    private MutableLiveData<Integer> post_offset;
    private MutableLiveData<ArrayList<FeedModel>> postList;
    private MutableLiveData<Integer> feed_offset;
    private MutableLiveData<ArrayList<FeedModel>> feedList;
    private MutableLiveData<Boolean> emptyDashboard;
    public AccountFragViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        userInfo = new MutableLiveData<>();
        userInfo.setValue(new UserModel());
        topStoreList = new MutableLiveData<>();
        topStoreList.setValue(new ArrayList<>());
        topBrandList = new MutableLiveData<>();
        topBrandList.setValue(new ArrayList<>());
        topProductList = new MutableLiveData<>();
        topProductList.setValue(new ArrayList<>());
        post_offset = new MutableLiveData<>();
        post_offset.setValue(0);
        postList = new MutableLiveData<>();
        postList.setValue(new ArrayList<>());
        feed_offset = new MutableLiveData<>();
        feed_offset.setValue(0);
        feedList = new MutableLiveData<>();
        feedList.setValue(new ArrayList<>());
        emptyDashboard = new MutableLiveData<>();
        emptyDashboard.setValue(false);

        EventBus.getDefault().register(this);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus.getDefault().unregister(this);
    }
    public void goMenu(Activity activity) {
        Intent intent = new Intent(activity, MenuActivity.class);
        activity.startActivity(intent);
    }
    public void goEditProfile(Activity activity) {
        Intent intent = new Intent(activity, EditProfileActivity.class);
        activity.startActivity(intent);
    }

    public MutableLiveData<Boolean> getIsBusy() {
        return isBusy;
    }
    public void setIsBusy(boolean isBusy) {
        this.isBusy.setValue(isBusy);
    }
    public MutableLiveData<UserModel> getUserInfo() {
        return userInfo;
    }
    public void setUserInfo(UserModel userInfo) {
        this.userInfo.setValue(userInfo);
    }
    public MutableLiveData<ArrayList<StoreModel>> getTopStoreList() {
        return topStoreList;
    }
    public void setTopStoreList(ArrayList<StoreModel> storeList) {
        this.topStoreList.setValue(storeList);
    }
    public MutableLiveData<ArrayList<TrendingBrandModel>> getTopBrandList() {
        return topBrandList;
    }
    public void setTopBrandList(ArrayList<TrendingBrandModel> topBrandList) {
        this.topBrandList.setValue(topBrandList);
    }
    public MutableLiveData<ArrayList<ProductDetailModel>> getTopProductList() {
        return topProductList;
    }
    public void setTopProductList(ArrayList<ProductDetailModel> productList) {
        this.topProductList.setValue(productList);
    }
    public MutableLiveData<Integer> getPost_offset() {
        return post_offset;
    }

    public void setPost_offset(int offset) {
        this.post_offset.setValue(offset);
    }

    public MutableLiveData<ArrayList<FeedModel>> getPostList() {
        return postList;
    }

    public void setPostList(ArrayList<FeedModel> postList) {
        this.postList.setValue(postList);
    }

    public MutableLiveData<Integer> getFeed_offset() {
        return feed_offset;
    }

    public void setFeed_offset(int offset) {
        this.feed_offset.setValue(offset);
    }

    public MutableLiveData<ArrayList<FeedModel>> getFeedList() {
        return feedList;
    }

    public void setFeedList(ArrayList<FeedModel> postList) {
        this.feedList.setValue(postList);
    }

    public MutableLiveData<Boolean> getEmptyDashboard() {
        return emptyDashboard;
    }

    public void setEmptyDashboard(Boolean emptyDashboard) {
        this.emptyDashboard.setValue(emptyDashboard);
    }

    public void loadLocalDashboardData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "AccountDashboard", "");
            if (!TextUtils.isEmpty(data)) {
                UserRes localRes = GsonUtils.getInstance().fromJson(data, UserRes.class);
                ArrayList<TrendingBrandModel> brandList = localRes.getTopBrandList();
                setTopBrandList(brandList);
                ArrayList<StoreModel> sList = localRes.getTopStoreList();
                setTopStoreList(sList);
                ArrayList<ProductDetailModel> pList = localRes.getTopProductList();
                setTopProductList(pList);
                UserModel userModel = localRes.getUserInfo();
                if (brandList.size() == 0 && sList.size() == 0 || pList.size() == 0) {
                    setEmptyDashboard(true);
                } else {
                    setEmptyDashboard(false);
                }
                G.user = userModel;
                G.location = new Location(LocationManager.GPS_PROVIDER);
                G.location.setLatitude(G.user.getLatitude());
                G.location.setLongitude(G.user.getLongitude());
                G.initUserInfo(userModel, false, "", "", false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void loadLocalPostData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "AccountPost", "");
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
    public void onSuccessDashboard(UserRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<TrendingBrandModel> brandList = res.getTopBrandList();
            setTopBrandList(brandList);
            ArrayList<StoreModel> sList = res.getTopStoreList();
            setTopStoreList(sList);
            ArrayList<ProductDetailModel> pList = res.getTopProductList();
            setTopProductList(pList);
            UserModel userModel = res.getUserInfo();
            if (brandList.size() == 0 && sList.size() == 0 || pList.size() == 0) {
                setEmptyDashboard(true);
            } else {
                setEmptyDashboard(false);
            }
            G.user = userModel;
            G.location = new Location(LocationManager.GPS_PROVIDER);
            G.location.setLatitude(G.user.getLatitude());
            G.location.setLongitude(G.user.getLongitude());
            G.initUserInfo(userModel, false, "", "", false);
            String data = new Gson().toJson(res, new TypeToken<UserRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "AccountDashboard",
                    data,
                    "",
                    ""
            );
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessPost(PostRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<FeedModel> list = res.getPostList();
            setPostList(list);
            String data = new Gson().toJson(res, new TypeToken<PostRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    G.getUserID(),
                    "AccountPost",
                    data,
                    "",
                    ""
            );
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessFeed(FeedRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<FeedModel> list = res.getPostList();
            setFeedList(list);
        }
    }
    public void loadDashboardData() {
        UserApi.getUserInfo();
    }
    public void loadBookmarkData() {
        setIsBusy(true);
        NewsFeedApi.getFollowingFeedList(feed_offset.getValue(), 10);
    }

    public void loadPostData() {
        setIsBusy(true);
        PostApi.getMyPostList(post_offset.getValue(), 10);
    }


    public void goFriend(Activity activity) {
        activity.startActivity(new Intent(activity, FriendActivity.class));
    }
    public void goProductLike(Activity activity) {
        activity.startActivity(new Intent(activity, ProductLikeActivity.class));
    }

    public void goStoreFollow(Activity activity) {
        activity.startActivity(new Intent(activity, StoreFollowActivity.class));
    }
}
