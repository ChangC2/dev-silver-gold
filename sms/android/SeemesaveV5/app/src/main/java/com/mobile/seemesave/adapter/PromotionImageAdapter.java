package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.seemesave.R;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.model.common.ProductModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PromotionImageAdapter extends RecyclerView.Adapter<PromotionImageAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<ProductModel> datas;
    private RecyclerClickListener listener;

    public PromotionImageAdapter(Context context, ArrayList<ProductModel> pDatas, RecyclerClickListener pListener) {
        datas = pDatas;
        listener = pListener;
        mContext = context;
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;

        @BindView(R.id.imgPlus)
        ImageView imgPlus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            Glide.with(mContext)
                    .load(datas.get(position).getProductDetail().getMedia())
                    .fitCenter()
                    .into(imgPhoto);
            if (position == 0)
                imgPlus.setVisibility(View.INVISIBLE);
            else
                imgPlus.setVisibility(View.VISIBLE);
            imgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    // ******************************class ViewHoler redefinition ***************************//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_promotion_image, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}