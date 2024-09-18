package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.ProductCategoryModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>{
    private Context mContext;
    private ProductCategoryAdapter.ProductCategoryAdapterRecyclerListener mListener;
    private ArrayList<ProductCategoryModel> mList = new ArrayList<>();

    public void setData(ArrayList<ProductCategoryModel> brandList) {
        this.mList.clear();
        this.mList.addAll(brandList);
        notifyDataSetChanged();
    }

    public interface ProductCategoryAdapterRecyclerListener{
        void onItemClicked(int pos, ProductCategoryModel model);
    }

    public ProductCategoryAdapter(Context context, ArrayList<ProductCategoryModel> list, ProductCategoryAdapter.ProductCategoryAdapterRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ProductCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product_category, parent, false);
        return new ProductCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.lytBottom)
        View lytBottom;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void setData(int position) {
            ProductCategoryModel model = mList.get(position);
            tvName.setText(model.getTitle());
            if (model.isCheck()) {
                lytBottom.setVisibility(View.VISIBLE);
                tvName.setTextColor(ContextCompat.getColor(mContext, R.color.main_blue_color));
            } else {
                tvName.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_800));
                lytBottom.setVisibility(View.INVISIBLE);
            }
            itemView.setOnClickListener(v -> {
                mListener.onItemClicked(position, model);
            });

        }
        
    }
}
