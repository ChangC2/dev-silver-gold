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
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.PromotionModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.BrandDetailActivity;
import com.mobile.seemesave.view.detail.PromotionDetailActivity;
import com.mobile.seemesave.view.detail.VideoPlayerActivity;

import java.util.ArrayList;

public class StoreDetailAdapter extends RecyclerView.Adapter<StoreDetailAdapter.ViewHolder>{
    private Context mContext;
    private StoreDetailAdapter.StoreDetailRecyclerListener mListener;
    private ArrayList<PromotionModel> mList = new ArrayList<>();

    public void setData(ArrayList<PromotionModel> brandList) {
        this.mList.clear();
        this.mList.addAll(brandList);
        notifyDataSetChanged();
    }

    public interface StoreDetailRecyclerListener{
        void onItemClicked(int pos, PromotionModel model);
    }

    public StoreDetailAdapter(Context context, ArrayList<PromotionModel> list, StoreDetailAdapter.StoreDetailRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public StoreDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_store_detail, parent, false);
        return new StoreDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreDetailAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProduct;
        private TextView txtTitle;
        private TextView txtPeriod;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPeriod = itemView.findViewById(R.id.txtPeriod);
        }

        public void setData(int position) {
            PromotionModel model = mList.get(position);
            if (model.getSubMedia().size() > 0) {
                if (model.getSubMedia().get(0).getMedia_Type().equalsIgnoreCase("Image")) {
                    Glide.with(mContext)
                            .load(model.getSubMedia().get(0).getMedia())
                            .fitCenter()
                            .placeholder(R.drawable.ic_me)
                            .into(imgProduct);
                } else {
                    Glide.with(mContext)
                            .load(model.getSubMedia().get(0).getVideoThumb())
                            .fitCenter()
                            .placeholder(R.drawable.ic_me)
                            .into(imgProduct);
                }
            }
            txtTitle.setText(model.getTitle());
            txtPeriod.setText(model.getStart_date() + "~" + model.getEnd_date());
            itemView.setOnClickListener(v -> {
                if (model.getSubMedia().size() > 0) {
                    String media = "";
                    if (model.getSubMedia() != null && model.getSubMedia().size() > 0) {
                        media = model.getSubMedia().get(0).getMedia();
                        if (!model.getSubMedia().get(0).getMedia_Type().equalsIgnoreCase("Image")) {
                            media = model.getSubMedia().get(0).getVideoThumb();
                        }
                    }
                    Intent intent = new Intent(mContext, PromotionDetailActivity.class);
                    intent.putExtra("id", model.getId());
                    intent.putExtra("media", media);
                    intent.putExtra("feed_type", model.getFeed_type());
                    intent.putExtra("brand", model.getTitle());
                    intent.putExtra("isDeliver", model.isIs_deliver());
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
