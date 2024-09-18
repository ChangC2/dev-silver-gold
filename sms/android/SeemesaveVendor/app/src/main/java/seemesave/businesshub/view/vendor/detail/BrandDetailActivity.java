package seemesave.businesshub.view.vendor.detail;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import seemesave.businesshub.R;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.view_model.vendor.detail.BrandDetailViewModel;

import butterknife.ButterKnife;

public class BrandDetailActivity extends BaseActivity {
    private BrandDetailViewModel mViewModel;

    private BrandDetailActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BrandDetailViewModel.class);
        setContentView(R.layout.activity_brand_detail);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    private void initView() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
