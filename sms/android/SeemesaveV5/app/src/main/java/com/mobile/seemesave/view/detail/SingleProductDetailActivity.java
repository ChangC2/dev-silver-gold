package com.mobile.seemesave.view.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view_model.detail.SingleProductDetailViewModel;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingleProductDetailActivity extends BaseActivity {
    private SingleProductDetailViewModel mViewModel;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.imgProduct)
    RoundedImageView imgProduct;
    @BindView(R.id.txtBarcode)
    TextView txtBarcode;
    @BindView(R.id.txtBrand)
    TextView txtBrand;
    @BindView(R.id.txtDesc)
    TextView txtDesc;
    @BindView(R.id.txtSize)
    TextView txtSize;
    @BindView(R.id.btnFollow)
    LinearLayout btnFollow;
    @BindView(R.id.txt_follow)
    TextView txtFollow;
    @BindView(R.id.imgStar)
    ImageView imgStar;


    private SingleProductDetailActivity activity;
    private ProductDetailModel productInfo;
    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SingleProductDetailViewModel.class);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);
        activity = this;
        barcode = getIntent().getStringExtra("barcode");
        if (!TextUtils.isEmpty(barcode)) {
            mViewModel.setBarcode(barcode);
            try {
                String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "SingleProductDetail", barcode);
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
    }


    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getProductInfo().observe(this, info -> {
            if (info != null && info.getBrand() != null) {
                productInfo = info;
                showResult();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void showResult() {
        Glide.with(activity)
                .load(productInfo.getMedia())
                .fitCenter()
                .into(imgProduct);
        txtBarcode.setText(productInfo.getBarcode());
        txtTitle.setText(productInfo.getBrand());
        txtBrand.setText(productInfo.getBrand());
        txtDesc.setText(productInfo.getDescription());
        txtSize.setText(productInfo.getPackSize() + " " + productInfo.getUnit());
        if (TextUtils.isEmpty(productInfo.getPackSize().trim()))
            txtSize.setVisibility(View.GONE);
        else {
            txtSize.setVisibility(View.VISIBLE);
            txtSize.setText(String.format(java.util.Locale.US, "%s %s", productInfo.getPackSize(), productInfo.getUnit()));
        }
        setFollowBtn();
        setLikeBtn();
    }

    void setFollowBtn() {
        if (productInfo.isBrandFollow()) {
            txtFollow.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
            btnFollow.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_blue_rect_20));
            txtFollow.setText(R.string.txt_following);
        } else {
            txtFollow.setTextColor(ContextCompat.getColor(activity, R.color.grey_dark));
            btnFollow.setBackground(ContextCompat.getDrawable(activity, R.drawable.bk_white_rect_20));
            txtFollow.setText(R.string.txt_follow);
        }
    }

    private void setLikeBtn() {
        if (productInfo.isLike()) {
            imgStar.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_star));
        } else {
            imgStar.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_unstar));
        }
    }

    private void onStar() {
        if (productInfo == null) return;
        productInfo.setLike(!productInfo.isLike());
        setLikeBtn();
        if (G.isNetworkAvailable(activity)) {
            String token = G.pref.getString("token", "");
            Ion.with(activity)
                    .load(G.SetLikeProductsUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .setBodyParameter("barcode", productInfo.getBarcode())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                        }
                    });
        }
    }

    private void onFollow() {
        if (productInfo == null) return;
        productInfo.setBrandFollow(!productInfo.isBrandFollow());
        setFollowBtn();
        if (G.isNetworkAvailable(activity)) {
            Ion.with(activity)
                    .load(G.FollowBrandUrl)
                    .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                    .setBodyParameter("brand_id", String.valueOf(productInfo.getBrandId()))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                        }
                    });
        }
    }

    @OnClick({R.id.btBack, R.id.btnViewStore, R.id.imgStar, R.id.btnFollow})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.btnViewStore:
                if (productInfo != null && productInfo.getBarcode() != null) {
                    mViewModel.goPromotionDetail(activity, productInfo.getBarcode());
                }
                break;
            case R.id.imgStar:
                onStar();
                break;
            case R.id.btnFollow:
                onFollow();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
