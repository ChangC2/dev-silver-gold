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
import com.mobile.seemesave.model.common.TrendingBrandModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.BrandDetailActivity;

import java.util.ArrayList;

public class BrandTopAdapter extends RecyclerView.Adapter<BrandTopAdapter.ViewHolder>{
    private Context mContext;
    private BrandTopAdapter.BrandTopRecyclerListener mListener;
    private ArrayList<TrendingBrandModel> mList = new ArrayList<>();

    public void setData(ArrayList<TrendingBrandModel> brandList) {
        this.mList.clear();
        this.mList.addAll(brandList);
        notifyDataSetChanged();
    }

    public interface BrandTopRecyclerListener{
        void onItemClicked(int pos, TrendingBrandModel model);
        void onStar(int pos, TrendingBrandModel model);
    }

    public BrandTopAdapter(Context context, ArrayList<TrendingBrandModel> list, BrandTopAdapter.BrandTopRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public BrandTopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_brand_top, parent, false);
        return new BrandTopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandTopAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProduct;
        private TextView txtTitle;
        private ImageView imgStar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            imgStar = itemView.findViewById(R.id.imgStar);

        }

        public void setData(int position) {
            TrendingBrandModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getMedia())
                    .centerCrop()
                    .placeholder(R.drawable.ic_me)
                    .into(imgProduct);
            txtTitle.setText(model.getName());
            if (model.isFollowing()){
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_like_sel));
            }else{
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_like));
            }
            itemView.setOnClickListener(v -> {
                int brand_id = model.getId();
                Intent intent = new Intent(mContext, BrandDetailActivity.class);
                intent.putExtra("id", brand_id);
                mContext.startActivity(intent);
            });
            imgStar.setOnClickListener(v -> {
                onStar(position);
            });
        }
        private void onStar(int pos) {
            TrendingBrandModel model = mList.get(pos);
            if (model.isFollowing()){
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_like));
            }else{
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_like_sel));
            }
            mList.get(pos).setFollowing(!model.isFollowing());
            if (G.isNetworkAvailable(mContext)) {
                String token = G.pref.getString("token", "");
                Ion.with(mContext)
                        .load(G.FollowBrandUrl)
                        .addHeader("Authorization", "Bearer " + token)
                        .setBodyParameter("brand_id", String.valueOf(model.getId()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                            }
                        });
            }
        }
    }
}
