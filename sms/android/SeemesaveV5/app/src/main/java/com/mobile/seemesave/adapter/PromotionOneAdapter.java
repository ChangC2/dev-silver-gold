package com.mobile.seemesave.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.model.common.PromotionOneModel;
import com.mobile.seemesave.view.detail.StoreDetailActivity;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.util.ArrayList;

public class PromotionOneAdapter extends RecyclerView.Adapter<PromotionOneAdapter.ViewHolder>{
    private Context mContext;
    private PromotionOneAdapter.PromotionOneRecyclerListener mListener;
    private ArrayList<PromotionOneModel> mList = new ArrayList<>();
    private ArrayList<PromotionModel> promotionList = new ArrayList<>();
    private boolean view_store = false;

    private ProductOneAdapter productOneAdapter;

    public void setData(ArrayList<PromotionOneModel> promotionProductList) {
        this.mList.clear();
        this.mList.addAll(promotionProductList);
        notifyDataSetChanged();
    }
    public void setPromotionData(ArrayList<PromotionModel> promotionList) {
        this.promotionList.clear();
        this.promotionList.addAll(promotionList);
        notifyDataSetChanged();
    }
    public interface PromotionOneRecyclerListener{
        void onItemClicked(int pos, PromotionOneModel model);
        void onPlusCount(int pPos, int cPos, ProductOneModel model);
        void onMinusCount(int pPos, int cPos, ProductOneModel model);
    }

    public PromotionOneAdapter(Context context, ArrayList<PromotionModel> promotionList, ArrayList<PromotionOneModel> list, boolean view_store,  PromotionOneAdapter.PromotionOneRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
        this.promotionList.clear();
        this.promotionList.addAll(promotionList);
        this.view_store = view_store;
    }

    @NonNull
    @Override
    public PromotionOneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_promotion_one, parent, false);
        return new PromotionOneAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionOneAdapter.ViewHolder holder, int position) {
        PromotionOneModel model = mList.get(holder.getLayoutPosition());
        holder.txtName.setText(model.getTitle());

        holder.btn_view.setOnClickListener(v -> {
            if (model.getStores().size() == 0) return;
            int store_id = model.getStores().get(0).getId();
            Intent intent = new Intent(mContext, StoreDetailActivity.class);
            intent.putExtra("id", store_id);
            mContext.startActivity(intent);
        });
        if (view_store) {
            holder.btn_view.setVisibility(View.VISIBLE);
        } else {
            holder.btn_view.setVisibility(View.GONE);
        }
        productOneAdapter = new ProductOneAdapter(mContext, model.getProductList(), false, -1, new ProductOneAdapter.ProductOneRecyclerListener() {
            @Override
            public void onRemoveCart(int pos, ProductOneModel model) {

            }

            @Override
            public void onPlus(int pos, ProductOneModel model) {

            }

            @Override
            public void onMinus(int pos, ProductOneModel model) {

            }
        });
        holder.recyclerView.setAdapter(productOneAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.pagerIndicator.attachToRecyclerView(holder.recyclerView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtName;
        private RecyclerView recyclerView;
        private LinearLayout btn_view;
        private IndefinitePagerIndicator pagerIndicator;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            btn_view = itemView.findViewById(R.id.btn_view_store);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            pagerIndicator = itemView.findViewById(R.id.pagerIndicator);
        }
    }
}
