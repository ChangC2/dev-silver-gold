package com.mobile.seemesave.view.menu;

import static com.mobile.seemesave.utils.G.ADDRESS_PICKER_REQUEST;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.mobile.seemesave.R;
import com.mobile.seemesave.api.user.UserApi;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.AlertCountModel;
import com.mobile.seemesave.model.res.AlertCountRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.menu.MenuViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.nikartm.support.ImageBadgeView;

public class MenuActivity extends BaseActivity {

    private MenuViewModel mViewModel;
    private MenuActivity activity;

    @BindView(R.id.imgUser)
    CircleImageView imgUser;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.notificationCountTxt)
    ImageBadgeView notificationCountTxt;

    @BindView(R.id.deliverCountTxt)
    ImageBadgeView deliverCountTxt;
    @BindView(R.id.shoppingCountTxt)
    ImageBadgeView shoppingCountTxt;
    @BindView(R.id.txtVersion)
    TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        activity = this;

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        Glide.with(activity)
                .load(G.user.getImage_url())
                .placeholder(R.drawable.ic_avatar)
                .fitCenter()
                .into(imgUser);
        txtName.setText(G.user.getFirst_name());
//        showBadgeFromLocal();
        txtVersion.setText("Version " + G.getVersion(activity));
        UserApi.getAlertCount();
    }
    private void showBadgeFromLocal() {
        int deliverCount = G.pref.getInt("deliverCount", 0);
        int cartCount = G.pref.getInt("cartCount", 0);
        int listCount = G.pref.getInt("listCount", 0);
        int notificationCount = G.pref.getInt("notificationCount", 0);
        deliverCountTxt.setBadgeValue(deliverCount);
        shoppingCountTxt.setBadgeValue(cartCount);
        notificationCountTxt.setBadgeValue(notificationCount);
    }
    private void onLocation() {
        Intent i = G.isAvailableGoogleApi(activity)
                ? new Intent(activity, LocationGoogleActivity.class)
                : new Intent(activity, LocationGoogleActivity.class);
        startActivityForResult(i, ADDRESS_PICKER_REQUEST);
    }

    @OnClick({R.id.li_edit, R.id.li_notification, R.id.li_logout, R.id.imgClose, R.id.li_setting, R.id.li_deliver_cart, R.id.li_shopping_cart, R.id.li_order_history, R.id.li_friend,  R.id.li_contact,  R.id.li_support, R.id.li_location})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.li_edit:
                mViewModel.goEdit(activity);
                break;
            case R.id.li_notification:
                mViewModel.goNotification(activity);
                break;
            case R.id.li_location:
                onLocation();
                break;
            case R.id.li_logout:
                mViewModel.goLogout(activity);
                break;
            case R.id.li_deliver_cart:
                mViewModel.goDeliverCart(activity);
                break;
            case R.id.li_shopping_cart:
                mViewModel.goShoppingCart(activity);
                break;
            case R.id.li_setting:
                mViewModel.goSetting(activity);
                break;
            case R.id.li_friend:
                mViewModel.goFriend(activity);
                break;
            case R.id.li_order_history:
                mViewModel.goOrderHistory(activity);
                break;
            case R.id.li_contact:
                mViewModel.goContactUs(activity);
            case R.id.li_support:
                mViewModel.goSupport(activity);
            case R.id.imgClose:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiSuccessResult(AlertCountRes res) {
        if (res.isStatus()) {
            AlertCountModel model = res.getData();
//            shoppingCountTxt.setBadgeValue(model.getShopping_cart_count());
//            deliverCountTxt.setBadgeValue(model.getDeliver_cart_count());
//            notificationCountTxt.setBadgeValue(model.getNotification_count());
            G.setBadgeCount(model.getShopping_list_count(), model.getShopping_cart_count(), model.getNotification_count(), model.getDeliver_cart_count());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra("fullAddress") != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra("lat", 0.0);
                    double currentLongitude = data.getDoubleExtra("lon", 0.0);
                    G.user.setLatitude(currentLatitude);
                    G.user.setLongitude(currentLongitude);
                    G.user.setAddress(data.getStringExtra("fullAddress"));
                    G.saveUserAddress(currentLatitude, currentLongitude, data.getStringExtra("fullAddress"));
                    JsonObject json = new JsonObject();
                    json.addProperty("latitude", G.user.getLatitude());
                    json.addProperty("longitude", G.user.getLongitude());
                    UserApi.updateLocation(json);
                    Log.e("location_update:", "2");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}