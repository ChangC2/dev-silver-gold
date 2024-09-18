package com.mobile.seemesave.view.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.FavouriteTabAdapter;
import com.mobile.seemesave.adapter.MainPagerAdapter;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.model.common.FavouriteTabModel;
import com.mobile.seemesave.utils.CustomViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteFragment extends BaseFragment {
    private View mFragView;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private FavouriteTabAdapter recyclerAdapter;
    private MainPagerAdapter viewPagerAdapter;
    private Fragment productFragment, brandFragment, storeFragment, postFragment;

    public FavouriteFragment() {
    }

    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();
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
        mFragView = inflater.inflate(R.layout.fragment_favourite, container, false);
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
        ArrayList<FavouriteTabModel> list = new ArrayList<>();
        list.add(new FavouriteTabModel(1, "Products"));
        list.add(new FavouriteTabModel(2, "Brands"));
        list.add(new FavouriteTabModel(3, "Stores"));
        list.add(new FavouriteTabModel(4, "Posts"));
        recyclerAdapter = new FavouriteTabAdapter(getActivity(), list, new FavouriteTabAdapter.FavouriteTabRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FavouriteTabModel model) {
                recyclerView.scrollToPosition(pos);
                viewPager.setCurrentItem(pos);
            }

        });
        recyclerAdapter.setSelected_index(0);
        recyclerView.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void setFragmentView() {
        productFragment = FavProductFragment.newInstance();
        brandFragment = FavBrandFragment.newInstance();
        storeFragment = FavStoreFragment.newInstance();
        postFragment = FavPostFragment.newInstance();
        viewPagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFrag(productFragment);
        viewPagerAdapter.addFrag(brandFragment);
        viewPagerAdapter.addFrag(storeFragment);
        viewPagerAdapter.addFrag(postFragment);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.disableScroll(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("page:1", position + "");
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("page:2", position + "");
                switch (position) {
                    case 0:
                        ((FavProductFragment) productFragment).loadData();
                        break;
                    case 1:
                        ((FavBrandFragment) brandFragment).loadData();
                        break;
                    case 2:
                        ((FavStoreFragment) storeFragment).loadData();
                        break;
                    case 3:
                        ((FavPostFragment) postFragment).loadData();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}