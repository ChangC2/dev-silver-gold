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
import com.mobile.seemesave.model.common.FeedModel;

import java.util.ArrayList;

public class FeaturedBrandAdapter extends RecyclerView.Adapter<FeaturedBrandAdapter.ViewHolder>{
    private Context mContext;
    private FeaturedBrandRecyclerListener mListener;
    private ArrayList<FeedModel> mList = new ArrayList<>();

    public void setData(ArrayList<FeedModel> storeList) {
        this.mList.clear();
        this.mList.addAll(storeList);
        notifyDataSetChanged();
    }

    public interface FeaturedBrandRecyclerListener{
        void onItemClicked(int pos, FeedModel model);
    }

    public FeaturedBrandAdapter(Context context, ArrayList<FeedModel> list, FeaturedBrandRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_featured_brand, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getAvatar())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgItem);
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
        }
    }
}
