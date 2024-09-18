package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.StoreCategoryModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreCategoryAdapter extends RecyclerView.Adapter<StoreCategoryAdapter.ViewHolder>{
    private Context mContext;
    private StoreCategoryAdapter.StoreCategoryAdapterRecyclerListener mListener;
    private ArrayList<StoreCategoryModel> mList = new ArrayList<>();

    public void setData(ArrayList<StoreCategoryModel> brandList) {
        this.mList.clear();
        this.mList.addAll(brandList);
        notifyDataSetChanged();
    }

    public interface StoreCategoryAdapterRecyclerListener{
        void onItemClicked(int pos, StoreCategoryModel model);
    }

    public StoreCategoryAdapter(Context context, ArrayList<StoreCategoryModel> list, StoreCategoryAdapter.StoreCategoryAdapterRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public StoreCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_store_category, parent, false);
        return new StoreCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreCategoryAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvName)
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void setData(int position) {
            StoreCategoryModel model = mList.get(position);
            tvName.setText(model.getName());
            itemView.setOnClickListener(v -> {
                mListener.onItemClicked(position, model);
            });

        }
        
    }
}
