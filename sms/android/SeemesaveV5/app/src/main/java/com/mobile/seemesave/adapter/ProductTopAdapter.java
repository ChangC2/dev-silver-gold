package com.mobile.seemesave.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.ProductDetailActivity;
import com.mobile.seemesave.view.detail.SingleProductDetailActivity;

import java.util.ArrayList;

public class ProductTopAdapter extends RecyclerView.Adapter<ProductTopAdapter.ViewHolder>{
    private Context mContext;
    private ProductTopAdapter.ProductTopRecyclerListener mListener;
    private ArrayList<ProductDetailModel> mList = new ArrayList<>();

    public void setData(ArrayList<ProductDetailModel> productList) {
        this.mList.clear();
        this.mList.addAll(productList);
        notifyDataSetChanged();
    }

    public interface ProductTopRecyclerListener{
        void onItemClicked(int pos, ProductDetailModel model);
        void onViewPromotion(int pos, ProductDetailModel model);
        void onStar(int pos, ProductDetailModel model);
    }

    public ProductTopAdapter(Context context, ArrayList<ProductDetailModel> list, ProductTopAdapter.ProductTopRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ProductTopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product_top, parent, false);
        return new ProductTopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTopAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProduct;
        private TextView txtTitle;
        private TextView txtSize;
        private TextView txtDescription;
        private ImageView imgStar;
        private ImageView imgView;
        private LinearLayout btn_view_promotion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtSize = itemView.findViewById(R.id.txtSize);
            imgStar = itemView.findViewById(R.id.imgStar);
            imgView = itemView.findViewById(R.id.imgView);
            btn_view_promotion = itemView.findViewById(R.id.btn_view_promotion);

        }

        public void setData(int position) {
            ProductDetailModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getMedia())
                    .centerCrop()
                    .placeholder(R.drawable.ic_me)
                    .into(imgProduct);
            txtTitle.setText(model.getBrand());
            txtDescription.setText(model.getDescription());
            txtSize.setText(model.getPackSize() + model.getUnit());
            if (model.isLike()){
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star));
            }else{
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_unstar));
            }
            imgView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, SingleProductDetailActivity.class);
                intent.putExtra("barcode", model.getBarcode());
                mContext.startActivity(intent);
            });
            btn_view_promotion.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("barcode", model.getBarcode());
                mContext.startActivity(intent);
            });
            imgStar.setOnClickListener(v -> {
                model.setLike(!model.isLike());
                setLikeBtn(position);
                if (G.isNetworkAvailable(mContext)) {
                    String token = G.pref.getString("token" , "");
                    Ion.with(mContext)
                            .load(G.SetLikeProductsUrl)
                            .addHeader("Authorization", "Bearer " + token)
                            .setBodyParameter("barcode", model.getBarcode())
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                }
                            });
                }
                mListener.onStar(getLayoutPosition(), model);
            });
        }
        private void setLikeBtn(int position){
            ProductDetailModel model = mList.get(position);
            if (model.isLike()){
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star));
            }else{
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_unstar));
            }
        }
    }
}
