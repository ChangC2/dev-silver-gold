package com.mobile.seemesave.view.main;

import static com.mobile.seemesave.utils.G.ADDRESS_PICKER_REQUEST;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.api.user.UserApi;
import com.mobile.seemesave.application.App;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.AlertCountModel;
import com.mobile.seemesave.model.res.AlertCountRes;
import com.mobile.seemesave.service.GpsTracker;
import com.mobile.seemesave.utils.DialogUtils;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.checkout.CheckOutDeliverActivity;
import com.mobile.seemesave.view_model.main.MainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.nikartm.support.ImageBadgeView;

public class MainActivity extends BaseActivity {
    private MainViewModel mViewModel;

    @BindView(R.id.main_fragment_container)
    FrameLayout frameLayout;

    @BindView(R.id.li_app_bar)
    ConstraintLayout li_app_bar;
    @BindView(R.id.imgBottomHome)
    ImageView imgBottomHome;
    @BindView(R.id.imgBottomPromotion)
    ImageView imgBottomPromotion;
    @BindView(R.id.imgBottomPost)
    ImageView imgBottomPost;
    @BindView(R.id.imgBottomAccount)
    ImageView imgBottomAccount;

    @BindView(R.id.tvBottomHome)
    ImageView tvBottomHome;
    @BindView(R.id.tvBottomPromotion)
    ImageView tvBottomPromotion;
    @BindView(R.id.tvBottomPost)
    ImageView tvBottomPost;
    @BindView(R.id.tvBottomAccount)
    ImageView tvBottomAccount;

    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.imgNotification)
    ImageBadgeView imgNotification;
    @BindView(R.id.imgCart)
    ImageBadgeView imgCart;
    @BindView(R.id.imgMenu)
    ImageView imgMenu;
    @BindView(R.id.imgCartBadge)
    ImageView imgCartBadge;
    @BindView(R.id.imgNotiBadge)
    ImageView imgNotiBadge;
    public BaseFragment fragment;
    public Fragment homeFragment, storeFragment, favouriteFragment, accountFragment;

    private MainActivity activity;
    private android.app.AlertDialog dlg;
    static final int PERM_REQUEST_CODES = 100;
    static final int BACKGROUND_REQUEST_CODES = 101;
    String[] perms = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            /*Manifest.permission.POST_NOTIFICATIONS*/
    };

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    ArrayList<ImageView> tabIcons = new ArrayList<>();

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "show_notification":
                    if (!G.pref.getString("relId", "").equalsIgnoreCase("")) {
                        showCartDlg();
                    }
                    break;
                case "disable_notification":
                    if (dlg != null) {
                        dlg.dismiss();
                    }
                    break;
            }
        }
    };

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("show_notification");
        filter.addAction("disable_notification");
        LocalBroadcastManager.getInstance(activity).registerReceiver(updateReceiver,
                filter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activity = this;
        initView();
        registerBroadcast();
        checkPerm();
        askNotificationPermission();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        UserApi.getAlertCount();
    }
    private void showCartDlg() {

        try {
            View dialogView = activity.getLayoutInflater().inflate(R.layout.dlg_cart_alert, null);

            dlg = new android.app.AlertDialog.Builder(activity)
                    .setView(dialogView)
                    .setCancelable(true)
                    .create();

            dlg.setCanceledOnTouchOutside(true);
            TextView content = dialogView.findViewById(R.id.confirm_txt);
            TextView btnAccept = dialogView.findViewById(R.id.btnYes);
            content.setText(G.pref.getString("relBody", ""));
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intents = new Intent(activity, CheckOutDeliverActivity.class);
                    intents.putExtra("cart_id", G.pref.getString("relId", "-1"));
                    startActivity(intents);
                    dlg.dismiss();
                }
            });
            dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dlg.show();
        } catch (Exception e) {
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            for (int i = 1; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            DialogUtils.showConfirmDialogWithListener(this, getString(R.string.txt_warning), getString(R.string.txt_do_you_want_to_finish_), getString(R.string.txt_exit), getString(R.string.txt_cancel),
                    (dialog, which) -> finishAffinity(),
                    (dialog, which) -> dialog.dismiss());
        }
    }

    private void initView() {
        updateFCMToken();
        showBadgeCount();
        G.getUserInfoFromLocal();
        tabIcons.add(imgBottomHome);
        tabIcons.add(imgBottomPromotion);
        tabIcons.add(imgBottomPost);
        tabIcons.add(imgBottomAccount);
        G.tabIndex = -1;
        setTab(0);

        Glide.with(activity)
                .load(G.user.getImage_url())
                .placeholder(R.drawable.ic_avatar)
                .fitCenter()
                .into(imgUser);

        homeFragment = HomeFragment.newInstance();
        storeFragment = StoreFragment.newInstance();
        favouriteFragment = FavouriteFragment.newInstance();
        accountFragment = AccountFragment.newInstance();

        apiCallForGetversion();
    }

    private void showBadgeCount () {
        int deliverCount = G.pref.getInt("deliverCount", 0);
        int cartCount = G.pref.getInt("cartCount", 0);
        int notificationCount = G.pref.getInt("notificationCount", 0);

        if (deliverCount + cartCount > 0) {
            imgCartBadge.setVisibility(View.VISIBLE);
        } else {
            imgCartBadge.setVisibility(View.GONE);
        }
        if (notificationCount > 0) {
            imgNotiBadge.setVisibility(View.VISIBLE);
        } else {
            imgNotiBadge.setVisibility(View.GONE);
        }
    }
    public void setTab(int index) {
        setBottomBarStyle(index);
        G.tabIndex = index;
        if (checkExist(G.tabName[index])) {
            BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(G.tabName[index]);
            setFragment(currentFragment);
        } else {
            BaseFragment newFragment;
            switch (index) {
                case 1:
                    newFragment = StoreFragment.newInstance();
                    break;
                case 2:
                    newFragment = FavouriteFragment.newInstance();
                    break;
                case 3:
                    newFragment = AccountFragment.newInstance();
                    break;
                default:
                    newFragment = HomeFragment.newInstance();
                    break;
            }
            newFragment.parentName = "";
            newFragment.fragmentName = G.tabName[index];
            setFragment(newFragment);
        }


    }
    private boolean checkExist(String name) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(name);
        if (fragment == null)
            return false;
        else
            return true;
    }

    private void setFragment(BaseFragment newFragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            if (fragment.getClass() != null && fragment.getClass().getSimpleName().equalsIgnoreCase("HomeFragment")) {
                fragment.onPause();
            }
        }

        fragment = newFragment;
        if (TextUtils.isEmpty(fragment.fragmentName)) {
            fragment.fragmentName = fragment.getTag();
        }
        if (checkExist(fragment.fragmentName)) {
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
            if (fragment.getClass() != null && fragment.getClass().getSimpleName().equalsIgnoreCase("HomeFragment")) {
                fragment.onResume();
            }
        } else {
            try {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, fragment, fragment.fragmentName).commit();
            } catch (Exception e) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, fragment, fragment.getTag()).commit();
            }
        }
    }

    private void clearBottomBarStyle() {
        imgBottomHome.setColorFilter(getColor(R.color.grey_light));
        imgBottomPromotion.setColorFilter(getColor(R.color.grey_light));
        imgBottomPost.setColorFilter(getColor(R.color.grey_light));
        imgBottomAccount.setColorFilter(getColor(R.color.grey_light));
        tvBottomHome.setVisibility(View.INVISIBLE);
        tvBottomPromotion.setVisibility(View.INVISIBLE);
        tvBottomPost.setVisibility(View.INVISIBLE);
        tvBottomAccount.setVisibility(View.INVISIBLE);
    }

    public void setBottomBarStyle(int position) {
        clearBottomBarStyle();
        switch (position) {
            case 0:
                imgBottomHome.setColorFilter(getColor(R.color.colorAccent));
                tvBottomHome.setVisibility(View.VISIBLE);
                li_app_bar.setVisibility(View.VISIBLE);
                setStatusBar(true);
                break;
            case 1:
                imgBottomPromotion.setColorFilter(getColor(R.color.colorAccent));
                tvBottomPromotion.setVisibility(View.VISIBLE);
                li_app_bar.setVisibility(View.VISIBLE);
                setStatusBar(true);
                break;
            case 2:
                imgBottomPost.setColorFilter(getColor(R.color.colorAccent));
                tvBottomPost.setVisibility(View.VISIBLE);
                li_app_bar.setVisibility(View.VISIBLE);
                setStatusBar(true);
                break;
            case 3:
                imgBottomAccount.setColorFilter(getColor(R.color.colorAccent));
                tvBottomAccount.setVisibility(View.VISIBLE);
                li_app_bar.setVisibility(View.GONE);
                setStatusBar(false);
                break;

        }
    }
    public void setStatusBar(boolean flag) {
        if (flag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.white_color));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.main_blue_color));
            }
        }
    }
    @OnClick({R.id.lvBottomHome, R.id.lvBottomPromotion, R.id.lvBottomPost, R.id.lvBottomAccount, R.id.imgMenu, R.id.imgCart, R.id.imgNotification, R.id.imgSearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lvBottomHome:
                setTab(0);
                break;
            case R.id.lvBottomPromotion:
                setTab(1);
                break;
            case R.id.lvBottomPost:
                setTab(2);
                break;
            case R.id.lvBottomAccount:
                setTab(3);
                break;
            case R.id.imgMenu:
                mViewModel.goMenu(activity);
                break;
            case R.id.imgCart:
                mViewModel.goCart(activity);
                break;
            case R.id.imgNotification:
                mViewModel.goNotification(activity);
                break;
            case R.id.imgSearch:
                mViewModel.goSearch(activity);
                break;
        }
    }


    public void checkPerm() {
        List<String> permlist = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String perm : perms) {
                if (ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, perm);
                    permlist.add(perm);
                }
            }
            if (permlist.size() > 0) {
                G.showDlg(activity, getString(R.string.alert_location), new ClickListener() {
                    @Override
                    public void onClick(boolean flag) {
                        ActivityCompat.requestPermissions(activity, permlist.toArray(new String[permlist.size()]), PERM_REQUEST_CODES);
                    }
                });
            } else {
                GpsTracker gpsTracker = new GpsTracker(activity);
                if (gpsTracker.canGetLocation() && gpsTracker.getLocation() != null) {
                    G.location = gpsTracker.getLocation();
                }
                checkBackgroundPermission();
            }
        }
    }
    private void checkBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("");
                alertDialog.setMessage(getString(R.string.alert_background_location));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_REQUEST_CODES);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("");
                alertDialog.setMessage(getString(R.string.alert_background_location));
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = new Uri.Builder()
                                .scheme("package")
                                .opaquePart(activity.getPackageName())
                                .build();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
                        activity.startActivityIfNeeded(intent, BACKGROUND_REQUEST_CODES);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        }
    }
    private void updateFCMToken() {
        try {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            String token = task.getResult();
                            Log.d("FCM Toke=>", token);
                            apiCallForSaveToken(token);
                        }
                    });
        } catch (Exception e) {
        }
    }

    void apiCallForSaveToken(String fcmToken) {
        JsonObject json = new JsonObject();
        json.addProperty("device_type", "Android");
        json.addProperty("device_id", fcmToken);
        String token = G.pref.getString("token", "");
        Ion.with(this)
                .load("PUT", G.REGISTER_TOKEN)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                    }
                });
    }
    void apiCallForGetversion() {
        ClickListener netErrorListner = new ClickListener() {
            @Override
            public void onClick(boolean flag) {
                apiCallForGetversion();
            }
        };
        Ion.with(this)
                .load(G.GetVersionUrl)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    String onlineVersion = jsonObject.getJSONObject("data").getString("android");
                                    String installedVersionString = G.getVersion(activity);
                                    if (installedVersionString.compareToIgnoreCase(onlineVersion) < 0) {
                                        ClickListener listener = new ClickListener() {
                                            @Override
                                            public void onClick(boolean flag) {
                                                G.openUrlBrowser(activity, "https://play.google.com/store/apps/details?id=com.mobile.seemesave");
                                            }
                                        };
                                        G.showDlg(activity, getString(R.string.version_update), listener, true);
                                    }
                                }
                            } catch (JSONException jsonException) {
                            }
                        }
                    }
                });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiSuccessResult(AlertCountRes res) {
        if (res.isStatus()) {
            AlertCountModel model = res.getData();
            G.setBadgeCount(model.getShopping_list_count(), model.getShopping_cart_count(), model.getNotification_count(), model.getDeliver_cart_count());
            showBadgeCount();
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
                    json.addProperty("address", data.getStringExtra("fullAddress"));
                    UserApi.updateLocation(json);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("update_location"));
                    Log.e("location_update:", "2");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
