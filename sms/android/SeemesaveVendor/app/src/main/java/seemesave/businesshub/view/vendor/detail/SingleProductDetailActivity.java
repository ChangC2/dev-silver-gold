package seemesave.businesshub.view.vendor.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.sqlite.DatabaseQueryClass;
import seemesave.businesshub.view_model.vendor.detail.SingleProductDetailViewModel;

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
        setContentView(R.layout.activity_single_product);
        ButterKnife.bind(this);
        activity = this;
        barcode = getIntent().getStringExtra("barcode");
        if (!TextUtils.isEmpty(barcode)) {
            mViewModel.setBarcode(barcode);
            try {
                String local_data = DatabaseQueryClass.getInstance().getData(App.getUserID(), "SingleProductDetail", barcode);
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
                .load(productInfo.getThumbnail_image())
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
    }





    @OnClick({R.id.btBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
