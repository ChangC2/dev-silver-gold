package seemesave.businesshub.view.vendor.menu;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.MainPagerAdapter;
import seemesave.businesshub.base.BaseActivity;
import seemesave.businesshub.utils.CustomViewPager;
public class PaymentActivity extends BaseActivity {

    private PaymentActivity activity;

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.txtTransaction)
    TextView txtTransaction;
    @BindView(R.id.txtFund)
    TextView txtFund;
    @BindView(R.id.txtWithdrawalFund)
    TextView txtWithdrawalFund;

    private MainPagerAdapter viewPagerAdapter;
    private Fragment transactionFragment, fundFragment, withdrawFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }


    private void initView() {
        setFragmentView();
    }
    private void setFragmentView() {
        transactionFragment = TransactionFragment.newInstance();
        fundFragment = FundFragment.newInstance();
        withdrawFragment = WithdrawFragment.newInstance();
        viewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(transactionFragment);
        viewPagerAdapter.addFrag(fundFragment);
        viewPagerAdapter.addFrag(withdrawFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.disableScroll(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void setSelectionTitle(int pos) {
        txtTransaction.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_600));
        txtFund.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_600));
        txtWithdrawalFund.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_600));
        txtTransaction.setTypeface(null, Typeface.NORMAL);
        txtFund.setTypeface(null, Typeface.NORMAL);
        txtWithdrawalFund.setTypeface(null, Typeface.NORMAL);
        switch (pos) {
            case 0:
                txtTransaction.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                txtTransaction.setTypeface(null, Typeface.BOLD);
                break;
            case 1:
                txtFund.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                txtFund.setTypeface(null, Typeface.BOLD);
                break;
            case 2:
                txtWithdrawalFund.setTextColor(ContextCompat.getColor(activity, R.color.main_blue_color));
                txtWithdrawalFund.setTypeface(null, Typeface.BOLD);
                break;

        }
    }
    @OnClick({R.id.btBack, R.id.txtTransaction, R.id.txtFund, R.id.txtWithdrawalFund})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.txtTransaction:
                viewPager.setCurrentItem(0);
                setSelectionTitle(0);
                break;
            case R.id.txtFund:
                viewPager.setCurrentItem(1);
                setSelectionTitle(1);
                break;
            case R.id.txtWithdrawalFund:
                viewPager.setCurrentItem(2);
                setSelectionTitle(2);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}