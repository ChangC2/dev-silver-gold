package com.mobile.seemesave.view_model.main;

import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.mobile.seemesave.view.cart.CartActivity;
import com.mobile.seemesave.view.main.MainActivity;
import com.mobile.seemesave.view.menu.MenuActivity;
import com.mobile.seemesave.view.menu.NotificationActivity;
import com.mobile.seemesave.view.search.SearchActivity;

public class MainViewModel extends ViewModel {
    public MainViewModel(){

    }
    public void goMenu(MainActivity activity) {
        Intent intent = new Intent(activity, MenuActivity.class);
        activity.startActivity(intent);
    }

    public void goCart(MainActivity activity) {
        Intent intent = new Intent(activity, CartActivity.class);
        activity.startActivity(intent);
    }

    public void goNotification(MainActivity activity) {
        Intent intent = new Intent(activity, NotificationActivity.class);
        activity.startActivity(intent);
    }

    public void goSearch(MainActivity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }
}
