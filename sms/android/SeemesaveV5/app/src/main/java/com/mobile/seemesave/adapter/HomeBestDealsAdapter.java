package com.mobile.seemesave.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.ProductCategoryModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeBestDealsAdapter extends RecyclerView.Adapter<HomeBestDealsAdapter.ViewHolder>{
    private Context mContext;
    private HomeBestDealsAdapter.ProductCategoryDetailAdapterRecyclerListener mListener;
    private ArrayList<ProductOneModel> mList = new ArrayList<>();
    private ProductOneAdapter productOneAdapter;
    private boolean isDeliver;
    public void setData(ArrayList<ProductOneModel> mList, boolean isDeliver) {
        this.isDeliver = isDeliver;
        this.mList.clear();
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    public interface ProductCategoryDetailAdapterRecyclerListener{
        void onItemClicked(int pos, ProductOneModel model);
    }

    public HomeBestDealsAdapter(Context context, ArrayList<ProductOneModel> list, boolean is_deliver, HomeBestDealsAdapter.ProductCategoryDetailAdapterRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
        this.isDeliver = is_deliver;
    }

    @NonNull
    @Override
    public HomeBestDealsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_home_best_deals, parent, false);
        return new HomeBestDealsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBestDealsAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.indicator)
        IndefinitePagerIndicator indicator;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            productOneAdapter = new ProductOneAdapter(mContext, mList, false, isDeliver ? 1 : 0, new ProductOneAdapter.ProductOneRecyclerListener() {
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
        
    }
}
