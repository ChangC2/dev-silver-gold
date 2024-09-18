package com.mobile.seemesave.view.cart;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.CartShoppingPaidAdapter;
import com.mobile.seemesave.base.BaseFragment;
import com.mobile.seemesave.model.common.ShoppingModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.checkout.OrderShoppingDetailActivity;
import com.mobile.seemesave.view_model.cart.ShoppingCartViewModel;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingHistoryFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    OrderHistoryActivity activity;


    ArrayList<ShoppingModel> dataList = new ArrayList<>();
    CartShoppingPaidAdapter adapter;

    private ShoppingCartViewModel mViewModel;
    private View mFragView;
    private boolean firstLoading = false;
    public ShoppingHistoryFragment() {
    }

    public static ShoppingHistoryFragment newInstance() {
        ShoppingHistoryFragment fragment = new ShoppingHistoryFragment();
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
        mViewModel = new ViewModelProvider(this).get(ShoppingCartViewModel.class);
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
    }
    public void loadData() {
        if (!firstLoading) {
            try {
                String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "ShoppingCartHistory", "");
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
        adapter = new CartShoppingPaidAdapter(getActivity(), dataList, new CartShoppingPaidAdapter.CartShoppingPaidRecyclerListener() {


            @Override
            public void onItemClicked(int pos, ShoppingModel model) {
                Intent intent = new Intent(getActivity(), OrderShoppingDetailActivity.class);
                intent.putExtra("cart_id", model.getId());
                startActivity(intent);
            }

            @Override
            public void onRate(int pos, ShoppingModel model) {
                showRatingDlg(model.getId());
            }

        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    public void showRatingDlg(int id) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(R.layout.dlg_rating);
        EditText editMsg = dialog.findViewById(R.id.editMsg);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingbar);
        dialog.findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                JsonObject json = new JsonObject();
                json.addProperty("id", id);
                json.addProperty("comment", editMsg.getText().toString().trim());
                json.addProperty("rating", (int) ratingBar.getRating());
                String token = G.pref.getString("token", "");
                Ion.with(activity)
                        .load(G.OrderRating)
                        .addHeader("Authorization", "Bearer " + token)
                        .addHeader("Content-Type", "application/json")
                        .setJsonObjectBody(json)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                Toast.makeText(activity, "Review submitted", Toast.LENGTH_LONG).show();
                            }
                        });
                G.hideSoftKeyboard(activity);
            }
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
