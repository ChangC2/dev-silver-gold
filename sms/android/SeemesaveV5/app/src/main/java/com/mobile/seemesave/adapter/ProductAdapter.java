package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.model.common.ProductModel;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private Context mContext;
    private ProductRecyclerListener mListener;
    private ArrayList<ProductModel> mList = new ArrayList<>();

    public interface ProductRecyclerListener{
        void onItemClicked(int pos, ProductModel model);
        void onAddCart(int pos, ProductModel model);
        void onInCart(int pos, ProductModel model);
        void onPlus(int pos, ProductModel model);
        void onMinus(int pos, ProductModel model);
        void onStar(int pos, ProductModel model);
    }

    public ProductAdapter(Context context, ArrayList<ProductModel> list, ProductAdapter.ProductRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductModel model = mList.get(holder.getLayoutPosition());
        ProductDetailModel productDetailModel = model.getProductDetail();
        Glide.with(mContext)
                .load(productDetailModel.getMedia())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgProduct);
        holder.txtTitle.setText(productDetailModel.getBrand());
        holder.txtDescription.setText(productDetailModel.getDescription() + " " + productDetailModel.getPackSize() + productDetailModel.getUnit());
        holder.txtPrice.setText("R 11");
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
        holder.btn_add_cart.setOnClickListener(v -> {
            mListener.onAddCart(holder.getLayoutPosition(), model);
        });
        holder.btn_in_cart.setOnClickListener(v -> {
            mListener.onInCart(holder.getLayoutPosition(), model);
        });
        holder.imgPlus.setOnClickListener(v -> {
            mListener.onPlus(holder.getLayoutPosition(), model);
        });
        holder.imgMinus.setOnClickListener(v -> {
            mListener.onMinus(holder.getLayoutPosition(), model);
        });
        holder.imgView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
        holder.imgStar.setOnClickListener(v -> {
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
        private TextView txtDescription;
        private TextView txtPrice;
        private TextView txtCount;
        private ImageView imgView;
        private ImageView imgStar;
        private ImageView imgPlus;
        private ImageView imgMinus;
        private LinearLayout btn_add_cart, btn_in_cart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtCount = itemView.findViewById(R.id.txtCount);
            imgView = itemView.findViewById(R.id.imgView);
            imgStar = itemView.findViewById(R.id.imgStar);
            imgPlus = itemView.findViewById(R.id.imgPlus);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            btn_add_cart = itemView.findViewById(R.id.btn_add_cart);
            btn_in_cart = itemView.findViewById(R.id.btn_in_cart);

        }
    }
}
