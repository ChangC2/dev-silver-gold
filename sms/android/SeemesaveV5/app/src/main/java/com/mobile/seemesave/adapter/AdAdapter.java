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

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder>{
    private Context mContext;
    private AdRecyclerListener mListener;
    private ArrayList<FeedModel> mList = new ArrayList<>();

    public void setData(ArrayList<FeedModel> storeList) {
        this.mList.clear();
        this.mList.addAll(storeList);
        notifyDataSetChanged();
    }

    public interface AdRecyclerListener{
        void onItemClicked(int pos, FeedModel model);
    }

    public AdAdapter(Context context, ArrayList<FeedModel> list, AdRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_ad, parent, false);
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
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imgItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
        }
    }
}
