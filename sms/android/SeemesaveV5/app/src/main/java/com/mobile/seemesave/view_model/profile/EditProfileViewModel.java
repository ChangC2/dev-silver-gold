package com.mobile.seemesave.view_model.profile;

import android.content.Intent;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.api.user.UserApi;
import com.mobile.seemesave.model.common.CurrencyModel;
import com.mobile.seemesave.model.common.TimeZoneModel;
import com.mobile.seemesave.model.common.UserModel;
import com.mobile.seemesave.model.res.UserBaseInfoRes;
import com.mobile.seemesave.view.profile.EditProfileActivity;
import com.mobile.seemesave.view.profile.FriendActivity;
import com.mobile.seemesave.view.profile.ProfileStep1Activity;
import com.mobile.seemesave.view.profile.ProfileStep2Activity;
import com.mobile.seemesave.view.profile.StoreSelectActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

public class EditProfileViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;
    private MutableLiveData<UserModel> userInfo;
    private MutableLiveData<ArrayList<CurrencyModel>> currencyList;
    private MutableLiveData<ArrayList<TimeZoneModel>> timezoneList;
    public EditProfileViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
        userInfo = new MutableLiveData<>();
        userInfo.setValue(new UserModel());
        currencyList = new MutableLiveData<>();
        currencyList.setValue(new ArrayList<>());
        timezoneList = new MutableLiveData<>();
        timezoneList.setValue(new ArrayList<>());
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
    public MutableLiveData<UserModel> getUserInfo() {
        return userInfo;
    }
    public void setUserInfo(UserModel userInfo) {
        this.userInfo.setValue(userInfo);
    }
    public MutableLiveData<ArrayList<CurrencyModel>> getCurrencyList() {
        return currencyList;
    }
    public void setCurrencyList(ArrayList<CurrencyModel> currencyList) {
        this.currencyList.setValue(currencyList);
    }
    public MutableLiveData<ArrayList<TimeZoneModel>> getTimezoneList() {
        return timezoneList;
    }
    public void setTimezoneList(ArrayList<TimeZoneModel> timezoneList) {
        this.timezoneList.setValue(timezoneList);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessBaseInfo(UserBaseInfoRes res) {
        setIsBusy(false);
        if (res.isStatus()) {
            ArrayList<CurrencyModel> cList = res.getCurrencyList();
            setCurrencyList(cList);
            ArrayList<TimeZoneModel> tList = res.getTimezoneList();
            setTimezoneList(tList);
        }
    }
    public void loadBaseInfo() {
        setIsBusy(true);
        UserApi.getUserBaseInfo();
    }

    public void updateProfile(EditProfileActivity activity,
                              String first_name,
                              String last_name,
                              String new_password,
                              String old_password,
                              int currency,
                              String time_zone,
                              String time_offset,
                              String countryCode,
                              String phoneNumber,
                              String email,
                              String mediaPath1,
                              String mediaPath2) {

        if (!TextUtils.isEmpty(mediaPath1)) {
            File file0 = new File(mediaPath1);
//            builder.setMultipartFile("new_image_url", file);        }
        }
        if (!TextUtils.isEmpty(mediaPath2)){
            File file1 = new File(mediaPath2);
//            builder.setMultipartFile("new_image_url_bg", file);
        }


    }
    public void updateProfileStep1(ProfileStep1Activity activity,
                                   String countryCode,
                                   String age,
                                   String gender,
                                   String bio,
                                   String phoneNumber,
                                   String email) {


    }

    public void goStep1(EditProfileActivity activity) {
        activity.startActivity(new Intent(activity, ProfileStep1Activity.class));
    }
    public void goStep2(EditProfileActivity activity) {
        activity.startActivity(new Intent(activity, ProfileStep2Activity.class));
    }
    public void goStep3(EditProfileActivity activity) {
        activity.startActivity(new Intent(activity, StoreSelectActivity.class));
    }
    public void goStep4(EditProfileActivity activity) {
        activity.startActivity(new Intent(activity, FriendActivity.class));
    }
}
