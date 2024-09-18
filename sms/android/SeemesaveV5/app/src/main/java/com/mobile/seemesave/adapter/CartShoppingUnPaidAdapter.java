package com.mobile.seemesave.adapter;

import android.annotation.SuppressLint;
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
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.ShoppingModel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartShoppingUnPaidAdapter extends RecyclerView.Adapter<CartShoppingUnPaidAdapter.ViewHolder>{
    private Context mContext;
    private CartShoppingUnPaidAdapter.CartShoppingUnPaidRecyclerListener mListener;
    private ArrayList<ShoppingModel> mList = new ArrayList<>();

    public void setData(ArrayList<ShoppingModel> sellingList) {
        this.mList.clear();
        this.mList.addAll(sellingList);
        notifyDataSetChanged();
    }

    public interface CartShoppingUnPaidRecyclerListener{
        void onItemClicked(int pos, ShoppingModel model);
        void onRemove(int pos, ShoppingModel model);

    }

    public CartShoppingUnPaidAdapter(Context context, ArrayList<ShoppingModel> list, CartShoppingUnPaidAdapter.CartShoppingUnPaidRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public CartShoppingUnPaidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_cart_unpaid, parent, false);
        return new CartShoppingUnPaidAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull CartShoppingUnPaidAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;
        @BindView(R.id.imgDel)
        ImageView imgDel;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtAddress)
        TextView txtAddress;
        @BindView(R.id.txtQty)
        TextView txtQty;

        @BindView(R.id.btnCheck)
        LinearLayout btnCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
        @SuppressLint("SetTextI18n")
        public void setData(int position) {
            ShoppingModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getImage_url())
                    .fitCenter()
                    .into(imgPhoto);
            txtName.setText(model.getName());
            txtAddress.setText(model.getAddress());
            txtQty.setText(String.format(Locale.US, "%d Products", model.getCount()));

            btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });

            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onRemove(position, model);
                }
            });
        }
    }
}
