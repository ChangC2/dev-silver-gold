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
import com.mobile.seemesave.model.common.TrendingBrandModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdBrandAdapter extends RecyclerView.Adapter<AdBrandAdapter.ViewHolder>{
    private Context mContext;
    private AdBrandAdapter.AdBrandAdapterRecyclerListener mListener;
    private ArrayList<TrendingBrandModel> mList = new ArrayList<>();

    public void setData(ArrayList<TrendingBrandModel> brandList) {
        this.mList.clear();
        this.mList.addAll(brandList);
        notifyDataSetChanged();
    }

    public interface AdBrandAdapterRecyclerListener{
        void onItemClicked(int pos, TrendingBrandModel model);
    }

    public AdBrandAdapter(Context context, ArrayList<TrendingBrandModel> list, AdBrandAdapter.AdBrandAdapterRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public AdBrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_ad_brand, parent, false);
        return new AdBrandAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdBrandAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgItem)
        RoundedImageView imgItem;
        @BindView(R.id.tvName)
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void setData(int position) {
            TrendingBrandModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getMedia())
                    .centerCrop()
                    .placeholder(R.drawable.ic_me)
                    .into(imgItem);
            tvName.setText(model.getName());
            itemView.setOnClickListener(v -> {
                mListener.onItemClicked(position, model);
            });

        }
        
    }
}
