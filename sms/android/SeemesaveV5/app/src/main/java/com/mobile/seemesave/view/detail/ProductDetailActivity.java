package com.mobile.seemesave.view.detail;

import android.annotation.SuppressLint;
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
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.model.res.CartRes;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.detail.ProductDetailViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailActivity extends BaseActivity {
    private ProductDetailViewModel mViewModel;
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

    private ProductDetailActivity activity;

    private ProductDetailModel productInfo;
    private ArrayList<PromotionOneModel> collectList = new ArrayList<>();
    private ArrayList<PromotionOneModel> deliverList = new ArrayList<>();
    private ArrayList<PromotionModel> promotionList = new ArrayList<>();
    private boolean is_click_collect = true;
    private String barcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        activity = this;
        barcode = getIntent().getStringExtra("barcode");
        initView();
    }

    private void initView() {
        collectList.clear();
        deliverList.clear();
        promotionList.clear();
        setCollectRecycler();
        setDeliverRecycler();
        mViewModel.setBarcode(barcode);
        try {
            String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "ProductDetail", barcode);
            if (TextUtils.isEmpty(local_data)) {
                mViewModel.setIsBusy(true);
            } else {
                mViewModel.loadLocalData();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.loadData();
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
        mViewModel.getProductInfo().observe(this, info -> {
            productInfo = info;
            Glide.with(activity)
                    .load(productInfo.getMedia())
                    .centerCrop()
                    .into(imgProduct);
            tvName.setText(productInfo.getBrand());
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

            }

            @Override
            public void onMinusCount(int pPos, int cPos, ProductOneModel model) {

            }
        });
        recyclerDeliverView.setAdapter(promotionDeliverAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerDeliverView.setLayoutManager(linearLayoutManager);
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setCollectBtn(int tab) {
        switch (tab) {
            case 0:
                btnCollect.setBackground(getDrawable(R.drawable.bk_blue_rect_20));
                txtCollect.setTextColor(getColor(R.color.white_color));
                btnDeliver.setBackground(getDrawable(R.drawable.bk_white_rect_20));
                txtDeliver.setTextColor(getColor(R.color.grey_dark));
                is_click_collect = true;
                li_collect.setVisibility(View.VISIBLE);
                li_deliver.setVisibility(View.GONE);
                break;
            case 1:
                btnDeliver.setBackground(getDrawable(R.drawable.bk_blue_rect_20));
                txtDeliver.setTextColor(getColor(R.color.white_color));
                btnCollect.setBackground(getDrawable(R.drawable.bk_white_rect_20));
                txtCollect.setTextColor(getColor(R.color.grey_dark));
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
    public void onSuccess(CartRes res) {
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
