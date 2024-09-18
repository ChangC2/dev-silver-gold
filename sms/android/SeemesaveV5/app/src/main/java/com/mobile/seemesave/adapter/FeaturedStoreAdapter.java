package com.mobile.seemesave.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.view.detail.StoreDetailActivity;

public class FeaturedStoreAdapter extends RecyclerView.Adapter<FeaturedStoreAdapter.ViewHolder>{
    private Context mContext;
    private FeaturedStoreRecyclerListener mListener;
    private ArrayList<FeedModel> mList = new ArrayList<>();

    public void setData(ArrayList<FeedModel> storeList) {
        this.mList.clear();
        this.mList.addAll(storeList);
        notifyDataSetChanged();
    }

    public interface FeaturedStoreRecyclerListener{
        void onItemClicked(int pos, FeedModel model);
    }

    public FeaturedStoreAdapter(Context context, ArrayList<FeedModel> list, FeaturedStoreRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_featured_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedModel model = mList.get(holder.getLayoutPosition());
        if (model.getSubMedia().size() > 0) {
            Glide.with(mContext)
                    .load(model.getSubMedia().get(0).getMedia())
                    .centerCrop()
                    .placeholder(R.drawable.ic_me)
                    .into(holder.imgItem);
        }
        Glide.with(mContext)
                .load(model.getAvatar())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgLogo);
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
        holder.tvName.setText(model.getName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imgItem, imgLogo;
        private TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvName = itemView.findViewById(R.id.tvName);
            imgLogo = itemView.findViewById(R.id.imgLogo);
        }
    }
}
