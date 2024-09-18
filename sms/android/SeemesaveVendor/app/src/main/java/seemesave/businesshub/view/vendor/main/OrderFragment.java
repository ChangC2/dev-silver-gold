package seemesave.businesshub.view.vendor.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.MainPagerAdapter;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.utils.CustomViewPager;
import seemesave.businesshub.view_model.vendor.main.StoreFragViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderFragment extends BaseFragment {
    private StoreFragViewModel mViewModel;
    private View mFragView;

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    @BindView(R.id.buttonGroup_draggable)
    SegmentedButtonGroup buttonGroup_draggable;

    private MainPagerAdapter viewPagerAdapter;
    private Fragment collectFragment, deliverFragment;

    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
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
        mViewModel = new ViewModelProvider(this).get(StoreFragViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_order, container, false);
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
        buttonGroup_draggable.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                viewPager.setCurrentItem(position);
            }
        });
    }
    private void setFragmentView() {
        collectFragment = OrderCollectFragment.newInstance();
        deliverFragment = OrderDeliverFragment.newInstance();
        viewPagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFrag(collectFragment);
        viewPagerAdapter.addFrag(deliverFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
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
    @OnClick({R.id.btnCollect, R.id.btnDeliver})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btnCollect:
                viewPager.setCurrentItem(0);
                break;
            case R.id.btnDeliver:
                viewPager.setCurrentItem(1);
                break;
        }
    }
}