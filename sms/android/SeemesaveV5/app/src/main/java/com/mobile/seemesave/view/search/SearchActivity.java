package com.mobile.seemesave.view.search;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.FavouriteTabAdapter;
import com.mobile.seemesave.adapter.MainPagerAdapter;
import com.mobile.seemesave.adapter.TagSelectAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.model.common.FavouriteTabModel;
import com.mobile.seemesave.utils.CustomViewPager;
import com.mobile.seemesave.utils.G;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {

    private SearchActivity activity;

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    RecyclerView recyclerSearchView;

    private FavouriteTabAdapter recyclerAdapter;
    private MainPagerAdapter viewPagerAdapter;
    private Fragment productFragment, brandFragment, storeFragment, postFragment;

    public String searchKey = "", query = "";
    ArrayList<String> lists = new ArrayList<>();
    TagSelectAdapter searchAdapter;
    private Dialog dialog;

    private boolean dlg_show = false;
    private int cur_pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        activity = this;
        searchKey = getIntent().getStringExtra("tag");
        initView();
    }

    private void initView() {
        setFragmentView();
        ArrayList<FavouriteTabModel> list = new ArrayList<>();
        list.add(new FavouriteTabModel(1, "Products"));
        list.add(new FavouriteTabModel(2, "Brands"));
        list.add(new FavouriteTabModel(3, "Stores"));
        list.add(new FavouriteTabModel(4, "Posts"));
        recyclerAdapter = new FavouriteTabAdapter(activity, list, new FavouriteTabAdapter.FavouriteTabRecyclerListener() {
            @Override
            public void onItemClicked(int pos, FavouriteTabModel model) {
                recyclerView.scrollToPosition(pos);
                viewPager.setCurrentItem(pos);
            }

        });
        recyclerAdapter.setSelected_index(0);
        recyclerView.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        apiCallForGetTagList();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    private void setFragmentView() {
        productFragment = SearchProductFragment.newInstance();
        brandFragment = SearchBrandFragment.newInstance();
        storeFragment = SearchStoreFragment.newInstance();
        postFragment = SearchPostFragment.newInstance();
        viewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
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
                cur_pos = position;
                goSearchPage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("page:3", state + "");
            }
        });
    }
    private void goSearchPage() {
        switch (cur_pos) {
            case 0:
                ((SearchProductFragment) productFragment).loadData(searchKey);
                break;
            case 1:
                ((SearchBrandFragment) brandFragment).loadData(searchKey);
                break;
            case 2:
                ((SearchStoreFragment) storeFragment).loadData(searchKey);
                break;
            case 3:
                ((SearchPostFragment) postFragment).loadData(searchKey);
                break;
        }
    }
    public void setSearchKey() {
        searchKey = "";
    }
    void apiCallForGetTagList() {
        showLoadingDialog();
        String token = G.pref.getString("token" , "");
        String encodedQuery = "";
        try {
            encodedQuery = URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(java.util.Locale.US,G.SearchTagStringUrl, encodedQuery);

        Ion.with(this)
                .load(url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        hideLoadingDialog();
                        lists.clear();
                        if (e == null){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")){
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                                    for (int i=0;i<jsonArray.length();i++){
                                        lists.add(jsonArray.getJSONObject(i).getString("tag"));
                                    }
                                }
                            } catch (JSONException jsonException) {
                            }
                        }
                        if (dlg_show) {
                            setSearchRecycler();
                        } else {
                            if (!getIntent().hasExtra("tag")) {
                                showSearchDlg();
                            }
                        }

                    }
                });
    }
    private void setSearchRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerSearchView.setLayoutManager(linearLayoutManager);
        recyclerSearchView.setItemAnimator(new DefaultItemAnimator());
        RecyclerClickListener listener = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int position) {
                searchKey = lists.get(position);
                dialog.dismiss();
                goSearchPage();
            }

            @Override
            public void onClick(View v, int position, int type) {
            }
        };
        searchAdapter = new TagSelectAdapter(this, lists, listener);
        recyclerSearchView.setAdapter(searchAdapter);
    }
    private void showSearchDlg() {
        dlg_show = true;
        dialog = new Dialog(activity, R.style.DialogTheme);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(R.layout.dlg_search);
        EditText editSearch = dialog.findViewById(R.id.editSearch);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    query = editSearch.getText().toString();
                    apiCallForGetTagList();
                }
                return false;
            }
        });
        recyclerSearchView = dialog.findViewById(R.id.recycler);
        setSearchRecycler();
        dialog.findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_show = false;
                dialog.dismiss();
            }
        });
    }
    @OnClick({R.id.btBack, R.id.imgSearch})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.btBack:
                finish();
                break;
            case R.id.imgSearch:
                showSearchDlg();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
