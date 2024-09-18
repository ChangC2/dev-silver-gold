package seemesave.businesshub.view.supplier.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.nikartm.support.ImageBadgeView;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.MainPagerAdapter;
import seemesave.businesshub.api.common.CommonApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.model.res.BaseInfoRes;
import seemesave.businesshub.model.res.PortalProfileInfoRes;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.utils.DialogUtils;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.GsonUtils;
import seemesave.businesshub.view.common.MenuOptionFragment;
import seemesave.businesshub.view.common.NotificationActivity;
import seemesave.businesshub.view_model.vendor.main.MainViewModel;

public class MainSupplierActivity extends BaseActivity {
    private MainViewModel mViewModel;
    @BindView(R.id.main_fragment_container)
    FrameLayout frameLayout;

    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.imgBottomHome)
    ImageView imgBottomHome;
    @BindView(R.id.imgBottomOrder)
    ImageView imgBottomOrder;
    @BindView(R.id.imgBottomAnalytics)
    ImageView imgBottomAnalytics;
    @BindView(R.id.imgBottomAds)
    ImageView imgBottomAds;
    @BindView(R.id.imgBottomMenu)
    ImageView imgBottomMenu;
    @BindView(R.id.tvBottomHome)
    ImageView tvBottomHome;
    @BindView(R.id.tvBottomOrder)
    ImageView tvBottomOrder;
    @BindView(R.id.tvBottomAnalytics)
    ImageView tvBottomAnalytics;
    @BindView(R.id.tvBottomAds)
    ImageView tvBottomAds;
    @BindView(R.id.tvBottomMenu)
    ImageView tvBottomMenu;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.imgNotification)
    ImageBadgeView imgNotification;
    @BindView(R.id.imgNotiBadge)
    ImageView imgNotiBadge;
    @BindView(R.id.txtPayment1)
    TextView txtPayment1;
    @BindView(R.id.txtPayment2)
    TextView txtPayment2;

    private MainPagerAdapter viewPagerAdapter;
    public BaseFragment fragment;
    public Fragment homeFragment, promoteFragment, analyticsFragment, adsFragment, menuFragment;
    private MenuOptionFragment menuOptionFragment;
    private MainSupplierActivity activity;
    ArrayList<ImageView> tabIcons = new ArrayList<>();
    private int pageTab = -1;
    private String subPage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activity = this;
        if (getIntent().hasExtra("page")) {
            pageTab = getIntent().getIntExtra("page", -1);
            subPage = getIntent().getStringExtra("sub_page");
        }

        initView();
        CommonApi.getBaseInfo();
        CommonApi.getPortalProfile();
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
        imgBottomOrder.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_pay_promote));
        apiGetNotiCount();
        Glide.with(activity)
                .load(App.getUserImage())
                .placeholder(R.drawable.ic_vendor_logo)
                .fitCenter()
                .into(imgUser);
        try {
            String data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "PortalProfileInfo", "supplier");
            if (!TextUtils.isEmpty(data)) {
                PortalProfileInfoRes localRes = GsonUtils.getInstance().fromJson(data, PortalProfileInfoRes.class);
                txtPayment1.setText(localRes.getData().getBalance().get(0).getStr());
                if (localRes.getData().getBalance().size() > 1) {
                    txtPayment2.setText(localRes.getData().getBalance().get(1).getStr());
                } else {
                    txtPayment2.setVisibility(View.GONE);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        tabIcons.clear();
        tabIcons.add(imgBottomHome);
        tabIcons.add(imgBottomOrder);
        tabIcons.add(imgBottomAnalytics);
        tabIcons.add(imgBottomAds);
        tabIcons.add(imgBottomMenu);
        G.tabIndex = -1;
        if (pageTab == -1) {
            setTab(0);
        } else {
            setTab(pageTab);
        }


        homeFragment = HomeFragment.newInstance();
        promoteFragment = PaytoPromoteFragment.newInstance();
        analyticsFragment = AnalyticsFragment.newInstance();
        adsFragment = AdsFragment.newInstance();
        menuFragment = MenuFragment.newInstance();

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
                    newFragment = PaytoPromoteFragment.newInstance();
                    break;
                case 2:
                    newFragment = AnalyticsFragment.newInstance();
                    break;
                case 3:
                    newFragment = AdsFragment.newInstance();
                    break;
                case 4:
                    newFragment = MenuFragment.newInstance();
                    break;
                default:
                    newFragment = HomeFragment.newInstance();
                    break;
            }
            newFragment.subPage = subPage;
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
        imgBottomOrder.setColorFilter(getColor(R.color.grey_light));
        imgBottomAnalytics.setColorFilter(getColor(R.color.grey_light));
        imgBottomAds.setColorFilter(getColor(R.color.grey_light));
        imgBottomMenu.setColorFilter(getColor(R.color.grey_light));
        tvBottomHome.setVisibility(View.INVISIBLE);
        tvBottomOrder.setVisibility(View.INVISIBLE);
        tvBottomAnalytics.setVisibility(View.INVISIBLE);
        tvBottomAds.setVisibility(View.INVISIBLE);
        tvBottomMenu.setVisibility(View.INVISIBLE);
    }

    public void setBottomBarStyle(int position) {
        clearBottomBarStyle();
        switch (position) {
            case 0:
                imgBottomHome.setColorFilter(getColor(R.color.colorAccent));
                tvBottomHome.setVisibility(View.VISIBLE);
                txtTitle.setText(R.string.overview);
                break;
            case 1:
                imgBottomOrder.setColorFilter(getColor(R.color.colorAccent));
                tvBottomOrder.setVisibility(View.VISIBLE);
                txtTitle.setText(R.string.txt_pay_to_promote);
                break;
            case 2:
                imgBottomAnalytics.setColorFilter(getColor(R.color.colorAccent));
                tvBottomAnalytics.setVisibility(View.VISIBLE);
                txtTitle.setText(R.string.analytics);
                break;
            case 3:
                imgBottomAds.setColorFilter(getColor(R.color.colorAccent));
                tvBottomAds.setVisibility(View.VISIBLE);
                txtTitle.setText(R.string.ads_manager);
                break;
            case 4:
                imgBottomMenu.setColorFilter(getColor(R.color.colorAccent));
                tvBottomMenu.setVisibility(View.VISIBLE);
                txtTitle.setText(R.string.menu);
                break;

        }
    }

    private void onMenu() {
        menuOptionFragment = new MenuOptionFragment(activity, new MenuOptionFragment.BottomFragListener() {

            @Override
            public void onDismiss() {

            }
        });
        menuOptionFragment.setCancelable(true);
        menuOptionFragment.show(getSupportFragmentManager(), menuOptionFragment.getTag());
    }

    private void onNotification() {
        Intent intent = new Intent(activity, NotificationActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.lvBottomHome, R.id.lvBottomOrder, R.id.lvBottomAnalytics, R.id.lvBottomAds, R.id.lvBottomMenu, R.id.imgOption, R.id.imgNotification})
    public void onClickBottom(View view) {
        switch (view.getId()) {
            case R.id.lvBottomHome:
                setTab(0);
                break;
            case R.id.lvBottomOrder:
                setTab(1);
                break;
            case R.id.lvBottomAnalytics:
                setTab(2);
                break;
            case R.id.lvBottomAds:
                setTab(3);
                break;
            case R.id.lvBottomMenu:
                setTab(4);
                break;
            case R.id.imgOption:
                onMenu();
                break;
            case R.id.imgNotification:
                onNotification();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiBaseInfo(BaseInfoRes res) {
        if (res.isStatus()) {
            String data = new Gson().toJson(res, new TypeToken<BaseInfoRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    App.getUserID(),
                    "BaseInfo",
                    data,
                    "",
                    ""
            );
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onApiPortalProfileInfo(PortalProfileInfoRes res) {
        if (res.isStatus()) {
            if (res.getData() != null && res.getData().getBalance().size() > 0) {
                txtPayment1.setText(res.getData().getBalance().get(0).getStr());
                if (res.getData().getBalance().size() > 1) {
                    txtPayment2.setText(res.getData().getBalance().get(1).getStr());
                } else {
                    txtPayment2.setVisibility(View.GONE);
                }
            }
            String data = new Gson().toJson(res, new TypeToken<PortalProfileInfoRes>() {
            }.getType());
            DatabaseQueryClass.getInstance().insertData(
                    App.getUserID(),
                    "PortalProfileInfo",
                    data,
                    "supplier",
                    ""
            );
        }
    }

    private void apiGetNotiCount() {
        if (G.isNetworkAvailable(activity)) {
            Ion.with(activity)
                    .load(G.GetUnReadNotificationCount)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    int unread_count = jsonObject.getJSONObject("data").getInt("unread_count");
                                    int collect_count = jsonObject.getJSONObject("data").getInt("collect_count");
                                    int deliver_count = jsonObject.getJSONObject("data").getInt("deliver_count");
                                    imgNotification.setBadgeValue(unread_count);
                                    App.initNotificationCount(unread_count, collect_count, deliver_count);
                                    if (collect_count != 0 || deliver_count != 0) {
                                        if (pageTab != 1) {
                                            showAlarmModal(collect_count, deliver_count);
                                        }
                                    }
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void showAlarmModal(int collect_count, int deliver_count) {
        View dialogView = getLayoutInflater().inflate(R.layout.dlg_order_alert, null);

        final android.app.AlertDialog dlg = new android.app.AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dlg.setCanceledOnTouchOutside(true);
        TextView txtCollectCnt = dialogView.findViewById(R.id.txtCollectCnt);
        TextView txtDeliverCnt = dialogView.findViewById(R.id.txtDeliverCnt);
        TextView txtContinue = dialogView.findViewById(R.id.txtContinue);

        if (collect_count == 0) {
            txtCollectCnt.setVisibility(View.GONE);
        } else {
            txtCollectCnt.setVisibility(View.VISIBLE);
            txtCollectCnt.setText(String.format("%1$s Click & Collect order(s)", String.valueOf(collect_count)));
        }
        if (deliver_count == 0) {
            txtDeliverCnt.setVisibility(View.GONE);
        } else {
            txtDeliverCnt.setVisibility(View.VISIBLE);
            txtDeliverCnt.setText(String.format("%1$s Click & Deliver order(s)", String.valueOf(deliver_count)));
        }
        TextView btnYes = dialogView.findViewById(R.id.btnYes);


        txtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, MainSupplierActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("page", 1);
                startActivity(i);
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dlg.show();
    }
}
