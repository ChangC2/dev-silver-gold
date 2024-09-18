package com.mobile.seemesave.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.ProductCategoryModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductCategoryDetailAdapter extends RecyclerView.Adapter<ProductCategoryDetailAdapter.ViewHolder>{
    private Context mContext;
    private ProductCategoryDetailAdapter.ProductCategoryDetailAdapterRecyclerListener mListener;
    private ArrayList<ProductCategoryModel> mList = new ArrayList<>();
    private ProductOneAdapter productOneAdapter;
    private boolean isDeliver;
    public void setData(ArrayList<ProductCategoryModel> brandList) {
        this.mList.clear();
        this.mList.addAll(brandList);
        notifyDataSetChanged();
    }

    public interface ProductCategoryDetailAdapterRecyclerListener{
        void onItemClicked(int pos, ProductCategoryModel model);
    }

    public ProductCategoryDetailAdapter(Context context, ArrayList<ProductCategoryModel> list, boolean is_deliver, ProductCategoryDetailAdapter.ProductCategoryDetailAdapterRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
        this.isDeliver = is_deliver;
    }

    @NonNull
    @Override
    public ProductCategoryDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product_category_detail, parent, false);
        return new ProductCategoryDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryDetailAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.indicator)
        IndefinitePagerIndicator indicator;
        @BindView(R.id.btnAll)
        LinearLayout btnAll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            ProductCategoryModel model = mList.get(position);
            tvName.setText(model.getTitle());
            if (model.isCheck()) {
                tvName.setTypeface(null, Typeface.BOLD);
            } else {
                tvName.setTypeface(null, Typeface.NORMAL);
            }
            if (model.getProductList() != null) {
                productOneAdapter = new ProductOneAdapter(mContext, model.getProductList(), false, isDeliver ? 1 : 0, new ProductOneAdapter.ProductOneRecyclerListener() {
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
                recyclerView.setAdapter(productOneAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                indicator.attachToRecyclerView(recyclerView);
            }
            btnAll.setOnClickListener(v -> {
                mListener.onItemClicked(position, model);
            });

        }
        
    }
}
