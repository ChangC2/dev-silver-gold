package com.mobile.seemesave.view_model.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.api.friend.FriendApi;
import com.mobile.seemesave.model.common.FriendModel;
import com.mobile.seemesave.model.res.FriendRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class FriendViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<Integer> offset;
    private MutableLiveData<ArrayList<FriendModel>> friendList;
    private MutableLiveData<ArrayList<FriendModel>> requestList;
    private MutableLiveData<ArrayList<FriendModel>> inviteList;
    public FriendViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        offset = new MutableLiveData<>();
        offset.setValue(0);
        friendList = new MutableLiveData<>();
        friendList.setValue(new ArrayList<>());
        requestList = new MutableLiveData<>();
        requestList.setValue(new ArrayList<>());
        inviteList = new MutableLiveData<>();
        inviteList.setValue(new ArrayList<>());

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

    public MutableLiveData<ArrayList<FriendModel>> getFriendList() {
        return friendList;
    }
    public void setFriendList(ArrayList<FriendModel> friendList) {
        this.friendList.setValue(friendList);
    }
    public MutableLiveData<Integer> getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset.setValue(offset);
    }
    public MutableLiveData<ArrayList<FriendModel>> getRequestList() {
        return requestList;
    }
    public void setRequestList(ArrayList<FriendModel> requestList) {
        this.requestList.setValue(requestList);
    }
    public MutableLiveData<ArrayList<FriendModel>> getInviteList() {
        return inviteList;
    }
    public void setInviteList(ArrayList<FriendModel> inviteList) {
        this.inviteList.setValue(inviteList);
    }
    public void loadData() {
        setIsBusy(true);
        FriendApi.getFriendAll(offset.getValue(), 100);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(FriendRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<FriendModel> requestList = new ArrayList<>();
            ArrayList<FriendModel> inviteList = new ArrayList<>();
            ArrayList<FriendModel> friendList = new ArrayList<>();
            requestList.clear();
            inviteList.clear();
            friendList.clear();
            for (int i = 0; i < res.getFriendList().size(); i++) {
                FriendModel friendModel = res.getFriendList().get(i);
                if (friendModel.isIs_pending()) {
                    if (friendModel.isMaster()) {
                        inviteList.add(friendModel);
                    } else {
                        requestList.add(friendModel);
                    }
                } else {
                    friendList.add(friendModel);
                }
            }
            setFriendList(friendList);
            setInviteList(inviteList);
            setRequestList(requestList);
        }
    }

}
