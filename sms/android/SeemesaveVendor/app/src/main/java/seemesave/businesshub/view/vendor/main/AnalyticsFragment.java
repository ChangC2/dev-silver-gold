package seemesave.businesshub.view.vendor.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import seemesave.businesshub.R;
import seemesave.businesshub.adapter.MainPagerAdapter;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.utils.CustomViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnalyticsFragment  extends BaseFragment {
    private View mFragView;

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    @BindView(R.id.txtHeatmap)
    TextView txtHeatmap;
    @BindView(R.id.txtStore)
    TextView txtStore;
    @BindView(R.id.txtProduct)
    TextView txtProduct;
    @BindView(R.id.txtPost)
    TextView txtPost;
    @BindView(R.id.txtAnalytics)
    TextView txtAnalytics;
    private MainPagerAdapter viewPagerAdapter;
    private Fragment heatmapFragment, chartStoreFragment, chartProductFragment, chartPostFragment, analyticsReportFragment;

    public AnalyticsFragment() {
    }

    public static AnalyticsFragment newInstance() {
        AnalyticsFragment fragment = new AnalyticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragView = inflater.inflate(R.layout.fragment_analytics, container, false);
        ButterKnife.bind(this, mFragView);
        initView();
        return mFragView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView() {
        setFragmentView();
    }
    private void setFragmentView() {
        heatmapFragment = HeatmapFragment.newInstance();
        chartStoreFragment = ChartStoreFragment.newInstance();
        chartProductFragment = ChartProductFragment.newInstance();
        chartPostFragment = ChartPostFragment.newInstance();
        analyticsReportFragment = AnalyticsReportFragment.newInstance();
        viewPagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFrag(heatmapFragment);
        viewPagerAdapter.addFrag(chartStoreFragment);
        viewPagerAdapter.addFrag(chartProductFragment);
        viewPagerAdapter.addFrag(chartPostFragment);
        viewPagerAdapter.addFrag(analyticsReportFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
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
    private void setTitleColor(int pos) {
        txtHeatmap.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtStore.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtProduct.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtPost.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtAnalytics.setTextColor(ContextCompat.getColor(getContext(), R.color.md_grey_600));
        txtHeatmap.setTypeface(null, Typeface.NORMAL);
        txtStore.setTypeface(null, Typeface.NORMAL);
        txtProduct.setTypeface(null, Typeface.NORMAL);
        txtPost.setTypeface(null, Typeface.NORMAL);
        txtAnalytics.setTypeface(null, Typeface.NORMAL);
        switch (pos) {
            case 0:
                txtHeatmap.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtHeatmap.setTypeface(null, Typeface.BOLD);
                break;
            case 1:
                txtStore.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtStore.setTypeface(null, Typeface.BOLD);
                break;
            case 2:
                txtProduct.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtProduct.setTypeface(null, Typeface.BOLD);
                break;
            case 3:
                txtPost.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtPost.setTypeface(null, Typeface.BOLD);
                break;
            case 4:
                txtAnalytics.setTextColor(ContextCompat.getColor(getContext(), R.color.main_blue_color));
                txtAnalytics.setTypeface(null, Typeface.BOLD);
                break;

        }
    }
    @OnClick({R.id.txtHeatmap, R.id.txtStore, R.id.txtProduct, R.id.txtPost, R.id.txtAnalytics})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.txtHeatmap:
                viewPager.setCurrentItem(0);
                setTitleColor(0);
                break;
            case R.id.txtStore:
                viewPager.setCurrentItem(1);
                setTitleColor(1);
                break;
            case R.id.txtProduct:
                viewPager.setCurrentItem(2);
                setTitleColor(2);
                break;
            case R.id.txtPost:
                viewPager.setCurrentItem(3);
                setTitleColor(3);
                break;
            case R.id.txtAnalytics:
                viewPager.setCurrentItem(4);
                setTitleColor(4);
                break;

        }
    }
}