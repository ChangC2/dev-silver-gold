package com.mobile.seemesave.view.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.PromotionOneAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.SimpleGestureFilter;
import com.mobile.seemesave.model.common.BrandModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.res.CartRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.detail.BrandDetailViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrandDetailActivity extends BaseActivity {
    private BrandDetailViewModel mViewModel;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.li_collect_empty)
    LinearLayout li_collect_empty;
    @BindView(R.id.li_deliver_empty)
    LinearLayout li_deliver_empty;
    @BindView(R.id.li_collect)
    LinearLayout li_collect;
    @BindView(R.id.li_deliver)
    LinearLayout li_deliver;
    @BindView(R.id.imgProduct)
    RoundedImageView imgProduct;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.recyclerCollectView)
    RecyclerView recyclerCollectView;
    @BindView(R.id.recyclerDeliverView)
    RecyclerView recyclerDeliverView;
    @BindView(R.id.btnCollect)
    LinearLayout btnCollect;
    @BindView(R.id.btnDeliver)
    LinearLayout btnDeliver;
    @BindView(R.id.txt_collect)
    TextView txtCollect;
    @BindView(R.id.txt_deliver)
    TextView txtDeliver;

    private PromotionOneAdapter promotionCollectAdapter;
    private PromotionOneAdapter promotionDeliverAdapter;

    private BrandDetailActivity activity;

    private BrandModel brandInfo;
    private ArrayList<PromotionOneModel> collectList = new ArrayList<>();
    private ArrayList<PromotionOneModel> deliverList = new ArrayList<>();
    private ArrayList<PromotionModel> promotionList = new ArrayList<>();
    private boolean is_click_collect = true;
    private int brand_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BrandDetailViewModel.class);
        setContentView(R.layout.activity_brand_detail);
        ButterKnife.bind(this);
        activity = this;
        brand_id = getIntent().getIntExtra("id", 0);

        initView();
    }

    private void initView() {
        collectList.clear();
        deliverList.clear();
        promotionList.clear();
        setCollectRecycler();
        setDeliverRecycler();
        mViewModel.setBrandId(brand_id);
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "BrandDetail", String.valueOf(brand_id));
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadData(brand_id);
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getBrandInfo().observe(this, info -> {
            brandInfo = info;
            Glide.with(activity)
                    .load(brandInfo.getMedia())
                    .centerCrop()
                    .into(imgProduct);
            tvName.setText(brandInfo.getName());
        });
        mViewModel.getCollectList().observe(this, list -> {
            collectList.clear();
            if (list.size() == 0) {
                li_collect_empty.setVisibility(View.VISIBLE);
                return;
            } else {
                li_collect_empty.setVisibility(View.GONE);
                collectList.addAll(list);
                promotionCollectAdapter.setData(collectList);
            }
        });
        mViewModel.getDeliverList().observe(this, list -> {
            deliverList.clear();
            if (list.size() == 0) {
                li_deliver_empty.setVisibility(View.VISIBLE);
                return;
            } else {
                li_deliver_empty.setVisibility(View.GONE);
                deliverList.addAll(list);
                promotionDeliverAdapter.setData(deliverList);
            }
        });
        mViewModel.getPromotionList().observe(this, list -> {
            promotionList.clear();
            if (list.size() != 0) {
                promotionList.addAll(list);
                promotionCollectAdapter.setPromotionData(promotionList);
                promotionDeliverAdapter.setPromotionData(promotionList);
            }
        });
    }

    private void setCollectRecycler() {
        promotionCollectAdapter = new PromotionOneAdapter(activity, promotionList, collectList, true, new PromotionOneAdapter.PromotionOneRecyclerListener() {
            @Override
            public void onItemClicked(int pos, PromotionOneModel model) {

            }

            @Override
            public void onPlusCount(int pPos, int cPos, ProductOneModel model) {

            }

            @Override
            public void onMinusCount(int pPos, int cPos, ProductOneModel model) {

            }
        });
        recyclerCollectView.setAdapter(promotionCollectAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerCollectView.setLayoutManager(linearLayoutManager);
    }
    private void setDeliverRecycler() {
        promotionDeliverAdapter = new PromotionOneAdapter(activity, promotionList, deliverList, true, new PromotionOneAdapter.PromotionOneRecyclerListener() {
            @Override
            public void onItemClicked(int pos, PromotionOneModel model) {

            }
            @Override
            public void onPlusCount(int pPos, int cPos, ProductOneModel model) {
                deliverList.get(pPos).getProductList().get(cPos).setCount(model.getCount() + 1);
                promotionDeliverAdapter.setData(deliverList);
            }

            @Override
            public void onMinusCount(int pPos, int cPos, ProductOneModel model) {
                if (model.getCount() == 1) return;
                deliverList.get(pPos).getProductList().get(cPos).setCount(model.getCount() -1 );
                promotionDeliverAdapter.setData(deliverList);
            }
        });
        recyclerDeliverView.setAdapter(promotionDeliverAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerDeliverView.setLayoutManager(linearLayoutManager);
    }
    private void setCollectBtn(int tab) {
        switch (tab) {
            case 0:
                btnCollect.setBackground(getResources().getDrawable(R.drawable.bk_blue_rect_20));
                txtCollect.setTextColor(getResources().getColor(R.color.white_color));
                btnDeliver.setBackground(getResources().getDrawable(R.drawable.bk_white_rect_20));
                txtDeliver.setTextColor(getResources().getColor(R.color.grey_dark));
                is_click_collect = true;
                li_collect.setVisibility(View.VISIBLE);
                li_deliver.setVisibility(View.GONE);
                break;
            case 1:
                btnDeliver.setBackground(getResources().getDrawable(R.drawable.bk_blue_rect_20));
                txtDeliver.setTextColor(getResources().getColor(R.color.white_color));
                btnCollect.setBackground(getResources().getDrawable(R.drawable.bk_white_rect_20));
                txtCollect.setTextColor(getResources().getColor(R.color.grey_dark));
                is_click_collect = false;
                li_collect.setVisibility(View.GONE);
                li_deliver.setVisibility(View.VISIBLE);
                break;
        }
    }
    @OnClick({R.id.btBack, R.id.btnCollect, R.id.btnDeliver})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnCollect:
                setCollectBtn(0);
                break;
            case R.id.btnDeliver:
                setCollectBtn(1);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccessCart(CartRes res) {
        if (res.isStatus()) {
            Toast.makeText(activity, res.getType().equalsIgnoreCase("collect") ? R.string.add_success_shopping_cart : R.string.add_success_deliver_cart, Toast.LENGTH_LONG).show();
        } else {
            if (!res.getMessage().equalsIgnoreCase("")) {
                Toast.makeText(activity, res.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
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
