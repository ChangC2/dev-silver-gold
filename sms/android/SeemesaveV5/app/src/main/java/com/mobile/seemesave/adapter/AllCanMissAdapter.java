package com.mobile.seemesave.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.BrandDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCanMissAdapter extends RecyclerView.Adapter<AllCanMissAdapter.ViewHolder>{
    private Context mContext;
    private AllCanMissAdapter.AllCanMissAdapterRecyclerListener mListener;
    private ArrayList<FeedModel> mList = new ArrayList<>();

    public void setData(ArrayList<FeedModel> brandList) {
        this.mList.clear();
        this.mList.addAll(brandList);
        notifyDataSetChanged();
    }

    public interface AllCanMissAdapterRecyclerListener{
        void onItemClicked(int pos, FeedModel model);
    }

    public AllCanMissAdapter(Context context, ArrayList<FeedModel> list, AllCanMissAdapter.AllCanMissAdapterRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public AllCanMissAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_all_canmiss, parent, false);
        return new AllCanMissAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCanMissAdapter.ViewHolder holder, int position) {
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
            FeedModel model = mList.get(position);
            if (model.getFeed_type().equalsIgnoreCase("FeaturedBrand")) {
                Glide.with(mContext)
                        .load(model.getAvatar())
                        .placeholder(R.drawable.ic_me)
                        .into(imgItem);
                itemView.setOnClickListener(v -> {
                    mListener.onItemClicked(position, model);
                });
                tvName.setText(model.getName());
            } else {
                if (model.getSubMedia().size() > 0) {
                    Glide.with(mContext)
                            .load(model.getSubMedia().get(0).getMedia())
                            .placeholder(R.drawable.ic_me)
                            .into(imgItem);
                    itemView.setOnClickListener(v -> {
                        mListener.onItemClicked(position, model);
                    });
                }
                tvName.setText(model.getTitle());
            }


        }
        
    }
}
