package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.TrendingBrandModel;

public class TrendingBrandAdapter extends RecyclerView.Adapter<TrendingBrandAdapter.ViewHolder>{
    private Context mContext;
    private TrendingBrandRecyclerListener mListener;
    private ArrayList<TrendingBrandModel> mList = new ArrayList<>();

    public void setData(ArrayList<TrendingBrandModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface TrendingBrandRecyclerListener{
        void onItemClicked(int pos, TrendingBrandModel model);
    }

    public TrendingBrandAdapter(Context context, ArrayList<TrendingBrandModel> list, TrendingBrandRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_tranding_brand, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrendingBrandModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getMedia())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgItem);
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
        }
    }
}
