package seemesave.businesshub.view.supplier.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.adapter.AdsTabAdapter;
import seemesave.businesshub.adapter.MainPagerAdapter;
import seemesave.businesshub.base.BaseFragment;
import seemesave.businesshub.model.common.AdsTabModel;
import seemesave.businesshub.utils.CustomViewPager;
import seemesave.businesshub.view.supplier.ads.AdsAdvertsFragment;
import seemesave.businesshub.view.supplier.ads.AdsFeaturedBrandFragment;
import seemesave.businesshub.view.vendor.ads.AdsPostFragment;
import seemesave.businesshub.view.vendor.ads.AdsStoryFragment;

public class AdsFragment extends BaseFragment {
    private View mFragView;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private AdsTabAdapter recyclerAdapter;
    private MainPagerAdapter viewPagerAdapter;
    private Fragment featuredBrandFragment, advertFragment, postFragment, storyFragment;

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
        list.add(new AdsTabModel(7, "Featured Brand"));
        list.add(new AdsTabModel(4, "Adverts"));
        list.add(new AdsTabModel(6, "Posts"));
        list.add(new AdsTabModel(7, "Stories"));
        recyclerAdapter = new AdsTabAdapter(getActivity(), list, new AdsTabAdapter.AdsTabRecyclerListener() {
            @Override
            public void onItemClicked(int pos, AdsTabModel model) {
                recyclerView.scrollToPosition(pos);
                viewPager.setCurrentItem(pos);
            }

        });
//        if (this.subPage.equalsIgnoreCase("promotion")) {
//            recyclerAdapter.setSelected_index(4);
//            recyclerView.scrollToPosition(4);
//        } else if (this.subPage.equalsIgnoreCase("post")) {
//            recyclerAdapter.setSelected_index(5);
//            recyclerView.scrollToPosition(5);
//        } else {
//            recyclerAdapter.setSelected_index(0);
//        }

        recyclerView.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setFragmentView() {
        featuredBrandFragment = AdsFeaturedBrandFragment.newInstance();
        advertFragment = AdsAdvertsFragment.newInstance();
        postFragment = AdsPostFragment.newInstance();
        storyFragment = AdsStoryFragment.newInstance();
        viewPagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFrag(featuredBrandFragment);
        viewPagerAdapter.addFrag(advertFragment);
        viewPagerAdapter.addFrag(postFragment);
        viewPagerAdapter.addFrag(storyFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.disableScroll(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ((AdsFeaturedBrandFragment) featuredBrandFragment).loadData();
                        break;
                    case 1:
                        ((AdsAdvertsFragment) advertFragment).loadData();
                        break;
                    case 2:
                        ((AdsPostFragment) postFragment).loadData();
                        break;
                    case 3:
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