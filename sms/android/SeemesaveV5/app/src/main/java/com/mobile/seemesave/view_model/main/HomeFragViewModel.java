package com.mobile.seemesave.view_model.main;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

import com.mobile.seemesave.api.newsfeed.NewsFeedApi;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.DiscoverModel;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.StoreCategoryModel;
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.model.res.HomeRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.GsonUtils;

public class HomeFragViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<Boolean> isDeliver;
    private MutableLiveData<ArrayList<DiscoverModel>> discoverList;
    private MutableLiveData<ArrayList<FeedModel>> postRecentList;
    private MutableLiveData<ArrayList<FeedModel>> postPopularList;
    private MutableLiveData<ArrayList<FeedModel>> allFeedList;
    private MutableLiveData<ArrayList<TrendingBrandModel>> brandList;
    private MutableLiveData<ArrayList<StoreCategoryModel>> storeCategoryList;
    private MutableLiveData<ArrayList<FeedModel>> storeList;
    private MutableLiveData<ArrayList<ProductOneModel>> sellingList;
    private MutableLiveData<ArrayList<PromotionModel>> bestSellingList;
    private MutableLiveData<ArrayList<FeedModel>> specialDealList;
    private MutableLiveData<ArrayList<PromotionModel>> exclusiveDealList;
    private MutableLiveData<ArrayList<FeedModel>> missDealList;
    private MutableLiveData<ArrayList<FeedModel>> featuredBrandList;
    private MutableLiveData<ArrayList<FeedModel>> adList;

    public HomeFragViewModel() {
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);


        isDeliver = new MutableLiveData<>();
        isDeliver.setValue(true);
        discoverList = new MutableLiveData<>();
        discoverList.setValue(new ArrayList<>());
        postRecentList = new MutableLiveData<>();
        postRecentList.setValue(new ArrayList<>());
        postPopularList = new MutableLiveData<>();
        postPopularList.setValue(new ArrayList<>());
        allFeedList = new MutableLiveData<>();
        allFeedList.setValue(new ArrayList<>());
        brandList = new MutableLiveData<>();
        brandList.setValue(new ArrayList<>());
        storeCategoryList = new MutableLiveData<>();
        storeCategoryList.setValue(new ArrayList<>());
        storeList = new MutableLiveData<>();
        storeList.setValue(new ArrayList<>());
        sellingList = new MutableLiveData<>();
        sellingList.setValue(new ArrayList<>());
        bestSellingList = new MutableLiveData<>();
        bestSellingList.setValue(new ArrayList<>());
        specialDealList = new MutableLiveData<>();
        specialDealList.setValue(new ArrayList<>());
        exclusiveDealList = new MutableLiveData<>();
        exclusiveDealList.setValue(new ArrayList<>());
        missDealList = new MutableLiveData<>();
        missDealList.setValue(new ArrayList<>());
        featuredBrandList = new MutableLiveData<>();
        featuredBrandList.setValue(new ArrayList<>());
        adList = new MutableLiveData<>();
        adList.setValue(new ArrayList<>());
        loadLocalData();

        EventBus.getDefault().register(this);
    }
    private void loadLocalData() {
        try {
            String data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "HomeFragment", "");
            if (!TextUtils.isEmpty(data)) {
                HomeRes localRes = GsonUtils.getInstance().fromJson(data, HomeRes.class);
                setDiscoverList(localRes.getStoryList());
                G.stories.clear();
                G.stories.addAll(localRes.getStoryList());
                setBrandList(localRes.getBrandList());
                setBestSellingList(localRes.getBestSellingList());
                setStoreCategoryList(localRes.getStoreCategoryList());
                setStoreList(localRes.getStoreList());
                setMissDealList(localRes.getMissDealList());
                setExclusiveDealList(localRes.getExclusiveDealList());
                setSpecialDealList(localRes.getSpecialDealList());
                setPostRecentList(localRes.getFeedList());
                setPostPopularList(localRes.getPoupularList());
                ArrayList<FeedModel> tmpAllList = new ArrayList<>();
                tmpAllList.clear();
                tmpAllList.addAll(localRes.getFeedList());
                tmpAllList.addAll(localRes.getPoupularList());
                setAllFeedList(tmpAllList);
                setFeaturedBrandList(localRes.getFeaturedBrandList());
                setAdList(localRes.getHomeAdvertList());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void setIsDeliver(boolean b) {
        this.isDeliver.setValue(b);
    }

    public MutableLiveData<ArrayList<FeedModel>> getPostRecentList() {
        return postRecentList;
    }

    public void setPostRecentList(ArrayList<FeedModel> postList) {
        this.postRecentList.setValue(postList);
    }

    public MutableLiveData<ArrayList<FeedModel>> getPostPopularList() {
        return postPopularList;
    }

    public void setPostPopularList(ArrayList<FeedModel> postList) {
        this.postPopularList.setValue(postList);
    }

    public MutableLiveData<ArrayList<TrendingBrandModel>> getBrandList() {
        return brandList;
    }

    public void setBrandList(ArrayList<TrendingBrandModel> brandList) {
        this.brandList.setValue(brandList);
    }

    public MutableLiveData<ArrayList<DiscoverModel>> getDiscoverList() {
        return discoverList;
    }

    public void setDiscoverList(ArrayList<DiscoverModel> list) {
        this.discoverList.setValue(list);
    }

    public MutableLiveData<ArrayList<StoreCategoryModel>> getStoreCategoryList() {
        return storeCategoryList;
    }

    public void setStoreCategoryList(ArrayList<StoreCategoryModel> storeCategoryList) {
        this.storeCategoryList.setValue(storeCategoryList);
    }

    public MutableLiveData<ArrayList<FeedModel>> getStoreList() {
        return storeList;
    }

    public void setStoreList(ArrayList<FeedModel> list) {
        this.storeList.setValue(list);
    }
    public MutableLiveData<ArrayList<ProductOneModel>> getSellingList() {
        return sellingList;
    }
    public void setSellingList(ArrayList<ProductOneModel> list) {
        this.sellingList.setValue(list);
    }
    public MutableLiveData<ArrayList<PromotionModel>> getBestSellingList() {
        return bestSellingList;
    }
    public void setBestSellingList(ArrayList<PromotionModel> list) {
        this.bestSellingList.setValue(list);
    }

    public MutableLiveData<ArrayList<FeedModel>> getAllFeedList() {
        return allFeedList;
    }

    public void setAllFeedList(ArrayList<FeedModel> allFeedList) {
        this.allFeedList.setValue(allFeedList);
    }

    public MutableLiveData<ArrayList<FeedModel>> getSpecialDealList() {
        return specialDealList;
    }

    public void setSpecialDealList(ArrayList<FeedModel> specialDealList) {
        this.specialDealList.setValue(specialDealList);
    }

    public MutableLiveData<ArrayList<PromotionModel>> getExclusiveDealList() {
        return exclusiveDealList;
    }

    public void setExclusiveDealList(ArrayList<PromotionModel> exclusiveDealList) {
        this.exclusiveDealList.setValue(exclusiveDealList);
    }

    public MutableLiveData<ArrayList<FeedModel>> getMissDealList() {
        return missDealList;
    }

    public void setMissDealList(ArrayList<FeedModel> missDealList) {
        this.missDealList.setValue(missDealList);
    }

    public MutableLiveData<ArrayList<FeedModel>> getFeaturedBrandList() {
        return featuredBrandList;
    }

    public void setFeaturedBrandList(ArrayList<FeedModel> featuredBrandList) {
        this.featuredBrandList.setValue(featuredBrandList);
    }

    public MutableLiveData<ArrayList<FeedModel>> getAdList() {
        return adList;
    }

    public void setAdList(ArrayList<FeedModel> adList) {
        this.adList.setValue(adList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessPost(HomeRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<FeedModel> tmpAllList = new ArrayList<>();
            tmpAllList.clear();
            if (offset.getValue() == 0) {
                ArrayList<DiscoverModel> discoverList = res.getStoryList();
                setDiscoverList(discoverList);
                G.stories.clear();
                G.stories.addAll(discoverList);
                ArrayList<FeedModel> pList = res.getPoupularList();
                setPostPopularList(pList);
                ArrayList<TrendingBrandModel> brandList = res.getBrandList();
                setBrandList(brandList);
                setStoreCategoryList(res.getStoreCategoryList());
                ArrayList<FeedModel> sList = res.getStoreList();
                setStoreList(sList);
                ArrayList<ProductOneModel> bList = res.getSellingList();
                setSellingList(bList);
                ArrayList<PromotionModel> bestList = res.getBestSellingList();
                setBestSellingList(bestList);
                tmpAllList.addAll(pList);
                setMissDealList(res.getMissDealList());
                setExclusiveDealList(res.getExclusiveDealList());
                setSpecialDealList(res.getSpecialDealList());
                setFeaturedBrandList(res.getFeaturedBrandList());
                setAdList(res.getHomeAdvertList());
            }
            ArrayList<FeedModel> list = res.getFeedList();
            setPostRecentList(list);
            tmpAllList.addAll(list);
            setAllFeedList(tmpAllList);
        }
    }

    public void loadNewsFeed() {
        NewsFeedApi.getNewsFeed(offset.getValue(), 10, isDeliver.getValue());
    }


}
