package com.mobile.seemesave.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.ProductDetailModel;
import com.mobile.seemesave.model.common.SingleProductCartModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.SingleProductDetailActivity;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderSingleAdapter extends RecyclerView.Adapter<OrderSingleAdapter.ViewHolder>{
    private Context mContext;
    private OrderSingleAdapter.OrderSingleRecyclerListener mListener;
    private ArrayList<SingleProductCartModel> mList = new ArrayList<>();

    public void setData(ArrayList<SingleProductCartModel> List) {
        this.mList.clear();
        this.mList.addAll(List);
        notifyDataSetChanged();
    }

    public interface OrderSingleRecyclerListener{
        void onItemClicked(int pos, SingleProductCartModel model);
        void onStar(int pos, SingleProductCartModel model);
    }

    public OrderSingleAdapter(Context context, ArrayList<SingleProductCartModel> list, OrderSingleAdapter.OrderSingleRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public OrderSingleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_order_single, parent, false);
        return new OrderSingleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSingleAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgProduct)
        ImageView imgProduct;

        @BindView(R.id.imgLike)
        ImageView imgLike;

        @BindView(R.id.txtName)
        TextView txtName;

        @BindView(R.id.txtDesc)
        TextView txtDesc;

        @BindView(R.id.txtPrice)
        TextView txtPrice;

        @BindView(R.id.txtCount)
        TextView txtCount;

        @BindView(R.id.txtPackSize)
        TextView txtPackSize;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            SingleProductCartModel model = mList.get(position);
            ProductDetailModel productDetailModel = model.getRealDetails().getRealProduct();
            Glide.with(mContext)
                    .load(productDetailModel.getThumbnail_image())
                    .fitCenter()
                    .into(imgProduct);
            txtName.setText(productDetailModel.getBrand());
            txtDesc.setText(productDetailModel.getDescription());
            txtCount.setText(String.format(Locale.US, "✕ %d", model.getCount()));
            try {
                txtPrice.setText(String.format(java.util.Locale.US,"%s %.2f", model.getProductDetails().getCurrency().getSimple(), Float.valueOf(model.getProductDetails().getSelling_price())));
            } catch (Exception cu) {
                txtPrice.setText(String.format(java.util.Locale.US,"R %.2f", Float.valueOf(model.getProductDetails().getSelling_price())));
            }
            txtPackSize.setText(String.format(java.util.Locale.US,"%s%s",productDetailModel.getPackSize(), productDetailModel.getUnit()));
            setLikeBtn(position);
            imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).getRealDetails().getRealProduct().isLike()){
                        imgLike.setImageDrawable(mContext.getDrawable(R.drawable.ic_like));
                    }else{
                        imgLike.setImageDrawable(mContext.getDrawable(R.drawable.ic_like_sel));
                    }
                    mList.get(position).getRealDetails().getRealProduct().setLike(!mList.get(position).getRealDetails().getRealProduct().isLike());
                    notifyDataSetChanged();
                    if (G.isNetworkAvailable(mContext)) {
                        String token = G.pref.getString("token" , "");
                        Ion.with(mContext)
                                .load(G.SetLikeProductsUrl)
                                .addHeader("Authorization", "Bearer " + token)
                                .setBodyParameter("barcode", mList.get(position).getRealDetails().getRealProduct().getBarcode())
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                    }
                                });
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SingleProductDetailActivity.class);
                    intent.putExtra("barcode", mList.get(position).getRealDetails().getRealProduct().getBarcode());
                    mContext.startActivity(intent);
                }
            });
        }

        private void setLikeBtn(int position){
            SingleProductCartModel model = mList.get(position);
            ProductDetailModel productDetailModel = model.getRealDetails().getRealProduct();
            if (!productDetailModel.isLike()){
                imgLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_like));
            }else{
                imgLike.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_like_sel));
            }
        }
    }
}
