package com.mobile.seemesave.view.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.OrderHistoryAdapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.model.common.OrderHistoryModel;
import com.mobile.seemesave.view_model.menu.OrderHistoryViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderHistoryActivity extends BaseActivity {

    private OrderHistoryViewModel mViewModel;
    private OrderHistoryActivity activity;

    @BindView(R.id.imgBack)
    ImageView imgBack;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);
        setContentView(R.layout.activity_order_history1);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initView() {
        setRecycler();
    }
    private void setRecycler() {
        ArrayList<OrderHistoryModel> list = new ArrayList<>();
        list.clear();
        OrderHistoryModel model = new OrderHistoryModel();
        model.setId("112342");
        model.setTime("2022 07-26 09:21");
        OrderHistoryModel model1 = new OrderHistoryModel();
        model1.setId("223121");
        model1.setTime("2022 07-28 08:21");
        list.add(model);
        list.add(model1);
        orderHistoryAdapter = new OrderHistoryAdapter(activity, list, new OrderHistoryAdapter.OrderHistoryRecyclerListener() {
            @Override
            public void onItemClicked(int pos, OrderHistoryModel model) {

            }
        });
        recyclerView.setAdapter(orderHistoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @OnClick({R.id.imgBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
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