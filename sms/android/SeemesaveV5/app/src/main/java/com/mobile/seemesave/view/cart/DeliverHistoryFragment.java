package com.mobile.seemesave.view.cart;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.CartDeliverPaidAdapter;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.model.common.DeliverModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.checkout.OrderDeliverDetailActivity;
import com.mobile.seemesave.view_model.cart.DeliverCartViewModel;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliverHistoryFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    OrderHistoryActivity activity;


    ArrayList<DeliverModel> dataList = new ArrayList<>();
    CartDeliverPaidAdapter adapter;

    private DeliverCartViewModel mViewModel;
    private View mFragView;
    private boolean firstLoading = false;
    public DeliverHistoryFragment() {
    }

    public static DeliverHistoryFragment newInstance() {
        DeliverHistoryFragment fragment = new DeliverHistoryFragment();
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
        mViewModel = new ViewModelProvider(this).get(DeliverCartViewModel.class);
        mFragView = inflater.inflate(R.layout.fragment_order_history, container, false);
        ButterKnife.bind(this, mFragView);
        activity = (OrderHistoryActivity) getActivity();
        initView();
        return mFragView;
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
        mViewModel.getHistoryList().observe(this, list -> {
            dataList.clear();
            if (list.size() == 0) {
                txtEmpty.setVisibility(View.VISIBLE);
                return;
            } else {
                txtEmpty.setVisibility(View.GONE);
                dataList.addAll(list);
                adapter.setData(dataList);
            }
        });
        mViewModel.loadHistory();
    }
    public void loadData() {
        if (!firstLoading) {
            try {
                String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "DeliverCartHistory", "");
                if (TextUtils.isEmpty(local_data)) {
                    mViewModel.setIsBusy(true);
                } else {
                    mViewModel.loadLocalHistoryData();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mViewModel.loadHistory();
            firstLoading = true;
        }
    }
    private void initView() {
        dataList.clear();
        setRecycler();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
                if (G.isNetworkAvailable(getContext())) {
                    mViewModel.loadHistory();
                }

            }
        });
    }
    private void setRecycler() {
        adapter = new CartDeliverPaidAdapter(getActivity(), dataList, new CartDeliverPaidAdapter.CartDeliverPaidRecyclerListener() {

            @Override
            public void onItemClicked(int pos, DeliverModel model) {
                Intent intent = new Intent(getActivity(), OrderDeliverDetailActivity.class);
                intent.putExtra("cart_id", model.getId());
                startActivity(intent);
            }

            @Override
            public void onRate(int pos, DeliverModel model) {

            }

            @Override
            public void onLink(int pos, DeliverModel model) {

            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
