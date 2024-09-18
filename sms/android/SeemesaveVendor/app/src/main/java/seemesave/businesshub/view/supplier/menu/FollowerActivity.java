package seemesave.businesshub.view.supplier.menu;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.FollowerAdapter;
import seemesave.businesshub.api.brand.BrandApi;
import seemesave.businesshub.api.follower.FollowerApi;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.BrandModel;
import seemesave.businesshub.model.common.FollowerModel;
import seemesave.businesshub.model.req.InvitePostReq;
import seemesave.businesshub.model.res.BrandListRes;
import seemesave.businesshub.model.res.FollowerRes;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view.vendor.menu.InviteStoreFollowerFragment;

public class FollowerActivity extends BaseActivity {

    private FollowerActivity activity;
    private InviteStoreFollowerFragment inviteStoreFollowerFragment;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.lytEmpty)
    LinearLayout lytEmpty;
    private FollowerAdapter followerAdapter;
    private ArrayList<FollowerModel> followerList = new ArrayList<>();

    @BindView(R.id.txtType)
    TextView txtType;
    @BindView(R.id.imgExpand)
    ImageView imgExpand;
    @BindView(R.id.lytSearch)
    LinearLayout lytSearch;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.imgClear)
    ImageView imgClear;
    @BindView(R.id.spinnerStore)
    Spinner spinnerBrand;
    private ArrayAdapter<BrandModel> storeAdapter;
    private ArrayList<BrandModel> brandList = new ArrayList<>();
    private int selBrand = -1;
    private String keyword = "";
    private boolean isExpand = false;

    private int offset = 0;
    private int limit = 20;
    private boolean isLoading = false;
    private boolean isLast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }


    private void initView() {
        txtType.setText(R.string.txt_brand);
        brandList.clear();
        followerList.clear();
        nestedScrollView.setNestedScrollingEnabled(false);
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0) {
                    imgClear.setVisibility(View.GONE);
                } else {
                    imgClear.setVisibility(View.VISIBLE);
                }

            }
        });
        edtSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = edtSearch.getText().toString().toLowerCase();
                    followerList.clear();
                    showLoadingDialog();
                    FollowerApi.getSupplierFollowerList(selBrand, offset, limit, keyword);
                    return true;
                }
                return false;
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                initParam();
                showLoadingDialog();
                FollowerApi.getSupplierFollowerList(selBrand, offset, limit, keyword);
            }
        });
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                if (diff == 0 && !isLoading && !isLast) {
                    isLoading = true;
                    offset = offset + limit;
                    showLoadingDialog();
                    FollowerApi.getSupplierFollowerList(selBrand, offset, limit, keyword);
                }

            }
        });
        setRecycler();
        showLoadingDialog();
        BrandApi.getBrandList(0, 1000, "");
    }
    private void initParam() {
        offset = 0;
        isLoading = false;
        isLast = false;
        followerList.clear();
    }
    private void setRecycler() {
        followerAdapter = new FollowerAdapter(activity, followerList, new FollowerAdapter.FollowerRecyclerListener() {

            @Override
            public void onItemClicked(int pos, FollowerModel model) {

            }
        });
        recyclerView.setAdapter(followerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    public void showExpand(){
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imgExpand, "rotation", 180f, 0f);
        rotate.setDuration(500);
        rotate.start();
        lytSearch.setVisibility(View.VISIBLE);
    }

    public void hideExpand(){
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imgExpand, "rotation", 0f, 180f);
        rotate.setDuration(500);
        rotate.start();
        lytSearch.setVisibility(View.GONE);
        edtSearch.setText("");
        keyword = "";
        imgClear.setVisibility(View.GONE);
    }
    private void onInvite() {
        inviteStoreFollowerFragment = new InviteStoreFollowerFragment(this, selBrand, new InviteStoreFollowerFragment.BottomFragListener() {
            @Override
            public void onDismiss() {

            }

        });
        inviteStoreFollowerFragment.setCancelable(true);
        inviteStoreFollowerFragment.show(getSupportFragmentManager(), inviteStoreFollowerFragment.getTag());
    }
    private void setBrandAdapter() {
        spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selBrand = brandList.get(position).getId();
                initParam();
                showLoadingDialog();
                FollowerApi.getSupplierFollowerList(selBrand, offset, limit, keyword);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        storeAdapter = new ArrayAdapter<BrandModel>(activity, R.layout.custom_spinner, brandList);
        storeAdapter.setDropDownViewResource(R.layout.custom_spinner_combo);
        spinnerBrand.setAdapter(storeAdapter);
        spinnerBrand.setSelection(getBrandIndex());
    }
    private int getBrandIndex() {
        for (int i = 0; i < brandList.size(); i++) {
            if (brandList.get(i).getId() == selBrand) {
                return i;
            }
        }
        return -1;
    }
    @OnClick({R.id.btBack, R.id.btnInvite, R.id.imgExpand, R.id.imgClear})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgClear:
                edtSearch.setText("");
                keyword = "";
                followerList.clear();
                showLoadingDialog();
                FollowerApi.getSupplierFollowerList(selBrand, offset, limit, keyword);
                break;
            case R.id.btBack:
                finish();
                break;
            case R.id.btnInvite:
                onInvite();
                break;
            case R.id.imgExpand:
                if (isExpand) {
                    hideExpand();
                } else {
                    showExpand();
                }
                isExpand = !isExpand;
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessBrandList(BrandListRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            brandList.addAll(res.getData());
            setBrandAdapter();
            if (brandList.size() > 0) {
                showLoadingDialog();
                FollowerApi.getSupplierFollowerList(brandList.get(0).getId(), offset, limit, keyword);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessFollowerList(FollowerRes res) {
        hideLoadingDialog();
        if (res.isStatus()) {
            if (offset == 0 && res.getData().size() == 0) {
                followerList.clear();
                followerAdapter.setData(followerList);
                lytEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                lytEmpty.setVisibility(View.GONE);
                if (offset == 0) {
                    followerList.clear();
                }
                if (res.getData().size() < limit) {
                    isLast = true;
                }
                followerList.addAll(res.getData());
                followerAdapter.setData(followerList);
                isLoading = false;
            }
        } else {
            if (!TextUtils.isEmpty(res.getMessage())) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();    
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendInvitation(InvitePostReq req) {
        if (req != null && !TextUtils.isEmpty(req.getFirstName())) {
            showLoadingDialog();
            Builders.Any.B builder = Ion.with(activity)
                    .load("POST", G.SendBrandInvitation);
            builder.addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken());
            builder .setMultipartParameter("brand_id", String.valueOf(selBrand))
                    .setMultipartParameter("first_name", req.getFirstName());
            if (req.isMail()) {
                builder.setMultipartParameter("email", req.getEmail());
            } else {
                builder.setMultipartParameter("countryCode", req.getCountryCode())
                        .setMultipartParameter("phoneNumber", req.getPhoneNumber());
            }
            builder.asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            hideLoadingDialog();
                            if (e == null) {
                                Toast.makeText(activity, getText(R.string.txt_msg_send_invitation), Toast.LENGTH_LONG).show();
                                initParam();
                                FollowerApi.getSupplierFollowerList(selBrand, offset, limit, keyword);
                            }
                        }
                    });

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}