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

import java.util.ArrayList;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.StoreModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.StoreDetailActivity;

public class StoreFavAdapter extends RecyclerView.Adapter<StoreFavAdapter.ViewHolder>{
    private Context mContext;
    private StoreFavRecyclerListener mListener;
    private ArrayList<StoreModel> mList = new ArrayList<>();

    public interface StoreFavRecyclerListener{
        void onItemClicked(int pos, StoreModel model);
        void onStar(int pos, StoreModel model);
    }

    public StoreFavAdapter(Context context, ArrayList<StoreModel> list, StoreFavAdapter.StoreFavRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public StoreFavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_store_fav, parent, false);
        return new StoreFavAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreFavAdapter.ViewHolder holder, int position) {
        StoreModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getLogo())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgProduct);
        holder.txtTitle.setText(model.getName());
        holder.itemView.setOnClickListener(v -> {
            int store_id = model.getId();
            Intent intent = new Intent(mContext, StoreDetailActivity.class);
            intent.putExtra("id", store_id);
            mContext.startActivity(intent);
        });
        if (model.isFollowing()) {
            holder.imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star));
        } else {
            holder.imgStar.setBackground(mContext.getDrawable(R.drawable.ic_unstar));
        }
        holder.imgStar.setOnClickListener(v -> {
            String user_id = G.pref.getString("user_id" , "");
            String status = String.valueOf(!model.isFollowing());
            mList.get(position).setFollowing(!model.isFollowing());
            if (!model.isFollowing()){
                holder.imgStar.setBackground(mContext.getDrawable(R.drawable.ic_unstar));
            }else{
                holder.imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star));
            }
            if (G.isNetworkAvailable(mContext)) {
                Ion.with(mContext)
                        .load(G.StoreFollowUrl)
                        .addHeader("Authorization", "Bearer " + G.pref.getString("token" , ""))
                        .setBodyParameter("id", String.valueOf(model.getId()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {

                            }
                        });
            }
            mListener.onStar(holder.getLayoutPosition(), model);
        });

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

    }
}
