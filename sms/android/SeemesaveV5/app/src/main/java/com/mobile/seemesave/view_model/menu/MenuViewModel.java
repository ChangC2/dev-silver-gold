package com.mobile.seemesave.view_model.menu;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.res.AlertCountRes;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.auth.LoginActivity;
import com.mobile.seemesave.view.cart.DeliverCartActivity;
import com.mobile.seemesave.view.cart.ShoppingCartActivity;
import com.mobile.seemesave.view.menu.ContactUsActivity;
import com.mobile.seemesave.view.profile.EditProfileActivity;
import com.mobile.seemesave.view.menu.MenuActivity;
import com.mobile.seemesave.view.menu.NotificationActivity;
import com.mobile.seemesave.view.cart.OrderHistoryActivity;
import com.mobile.seemesave.view.menu.SettingActivity;
import com.mobile.seemesave.view.profile.FriendActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MenuViewModel extends ViewModel {
    private MutableLiveData<Boolean> isBusy;

    public MenuViewModel(){
        isBusy = new MutableLiveData<>();
        isBusy.setValue(false);
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

    public void goNotification(MenuActivity activity) {
        Intent intent = new Intent(activity, NotificationActivity.class);
        activity.startActivity(intent);
    }
    public void goEdit(MenuActivity activity) {
        Intent intent = new Intent(activity, EditProfileActivity.class);
        activity.startActivity(intent);
    }
    public void goLogout(MenuActivity activity) {
        View dialogView = activity.getLayoutInflater().inflate(R.layout.dlg_exit, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        final TextView ok = dialogView.findViewById(R.id.ok);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        cancel.setVisibility(View.VISIBLE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G.logout();
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                dlg.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiSuccessResult(AlertCountRes res) {
        setIsBusy(false);
    }


    public void goSetting(MenuActivity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void goOrderHistory(MenuActivity activity) {
        Intent intent = new Intent(activity, OrderHistoryActivity.class);
        activity.startActivity(intent);
    }

    public void goContactUs(MenuActivity activity) {
        Intent intent = new Intent(activity, ContactUsActivity.class);
        activity.startActivity(intent);
    }

    public void goDeliverCart(MenuActivity activity) {
        Intent intent = new Intent(activity, DeliverCartActivity.class);
        activity.startActivity(intent);
    }

    public void goShoppingCart(MenuActivity activity) {
        Intent intent = new Intent(activity, ShoppingCartActivity.class);
        activity.startActivity(intent);
    }

    public void goFriend(MenuActivity activity) {
        Intent intent = new Intent(activity, FriendActivity.class);
        activity.startActivity(intent);
    }

    public void goSupport(MenuActivity activity) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://support.seemesave.com/"));
        activity.startActivity(i);
    }
}
