package seemesave.businesshub.view.vendor.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import seemesave.businesshub.R;
import seemesave.businesshub.adapter.AdsTabAdapter;
import seemesave.businesshub.adapter.MainPagerAdapter;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.model.common.AdsTabModel;
import seemesave.businesshub.utils.CustomViewPager;
import seemesave.businesshub.view.vendor.ads.AdsBestDealFragment;
import seemesave.businesshub.view.vendor.ads.AdsExclusiveDealFragment;
import seemesave.businesshub.view.vendor.ads.AdsFeaturedStoreFragment;
import seemesave.businesshub.view.vendor.ads.AdsMissDealFragment;
import seemesave.businesshub.view.vendor.ads.AdsPostFragment;
import seemesave.businesshub.view.vendor.ads.AdsPromotionFragment;
import seemesave.businesshub.view.vendor.ads.AdsStoryFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdsFragment extends BaseFragment {
    private View mFragView;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AdsTabAdapter recyclerAdapter;
    private MainPagerAdapter viewPagerAdapter;
    private Fragment canMissDealFragment, promotionFragment, paypromoteFragment, bestdealFragment, exclusiveFragment, storeFragment, featureStoreFragment, postFragment, storyFragment;

    public AdsFragment() {
    }
    public static AdsFragment newInstance() {
        AdsFragment fragment = new AdsFragment();
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
        mFragView = inflater.inflate(R.layout.fragment_ads, container, false);
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
        ArrayList<AdsTabModel> list = new ArrayList<>();
        list.add(new AdsTabModel(1, "Best Deal"));
        list.add(new AdsTabModel(2, "Can't Miss Deal"));
        list.add(new AdsTabModel(3, "Exclusive Deal"));
        list.add(new AdsTabModel(4, "Feature Store"));
        list.add(new AdsTabModel(5, "Promotion"));
        list.add(new AdsTabModel(6, "Posts"));
        list.add(new AdsTabModel(7, "Stories"));
        recyclerAdapter = new AdsTabAdapter(getActivity(), list, new AdsTabAdapter.AdsTabRecyclerListener() {
            @Override
            public void onItemClicked(int pos, AdsTabModel model) {
                recyclerView.scrollToPosition(pos);
                viewPager.setCurrentItem(pos);
            }

        });
        if (this.subPage.equalsIgnoreCase("promotion")) {
            recyclerAdapter.setSelected_index(4);
            recyclerView.scrollToPosition(4);
        } else if (this.subPage.equalsIgnoreCase("post")) {
            recyclerAdapter.setSelected_index(5);
            recyclerView.scrollToPosition(5);
        } else {
            recyclerAdapter.setSelected_index(0);
        }

        recyclerView.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setFragmentView() {
        bestdealFragment = AdsBestDealFragment.newInstance();
        canMissDealFragment = AdsMissDealFragment.newInstance();
        exclusiveFragment = AdsExclusiveDealFragment.newInstance();
        featureStoreFragment = AdsFeaturedStoreFragment.newInstance();
        promotionFragment = AdsPromotionFragment.newInstance();
        postFragment = AdsPostFragment.newInstance();
        storyFragment = AdsStoryFragment.newInstance();
        viewPagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFrag(bestdealFragment);
        viewPagerAdapter.addFrag(canMissDealFragment);
        viewPagerAdapter.addFrag(exclusiveFragment);
        viewPagerAdapter.addFrag(featureStoreFragment);
        viewPagerAdapter.addFrag(promotionFragment);
        viewPagerAdapter.addFrag(postFragment);
        viewPagerAdapter.addFrag(storyFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(6);
        viewPager.disableScroll(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ((AdsBestDealFragment) bestdealFragment).loadData();
                        break;
                    case 1:
                        ((AdsMissDealFragment) canMissDealFragment).loadData();
                        break;
                    case 2:
                        ((AdsExclusiveDealFragment) exclusiveFragment).loadData();
                        break;
                    case 3:
                        ((AdsFeaturedStoreFragment) featureStoreFragment).loadData();
                        break;
                    case 4:
                        ((AdsPromotionFragment) promotionFragment).loadData();
                        break;
                    case 5:
                        ((AdsPostFragment) postFragment).loadData();
                        break;
                    case 6:
                        ((AdsStoryFragment) storyFragment).loadData();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}