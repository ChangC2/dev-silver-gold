package com.mobile.seemesave.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.ShoppingModel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartShoppingPaidAdapter extends RecyclerView.Adapter<CartShoppingPaidAdapter.ViewHolder>{
    private Context mContext;
    private CartShoppingPaidAdapter.CartShoppingPaidRecyclerListener mListener;
    private ArrayList<ShoppingModel> mList = new ArrayList<>();

    public void setData(ArrayList<ShoppingModel> sellingList) {
        this.mList.clear();
        this.mList.addAll(sellingList);
        notifyDataSetChanged();
    }

    public interface CartShoppingPaidRecyclerListener{
        void onItemClicked(int pos, ShoppingModel model);
        void onRate(int pos, ShoppingModel model);
        
    }

    public CartShoppingPaidAdapter(Context context, ArrayList<ShoppingModel> list, CartShoppingPaidAdapter.CartShoppingPaidRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public CartShoppingPaidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_cart_paid, parent, false);
        return new CartShoppingPaidAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull CartShoppingPaidAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;
        @BindView(R.id.imgReady)
        ImageView imgReady;
        @BindView(R.id.imgDel)
        ImageView imgDel;
        @BindView(R.id.imgLink)
        ImageView imgLink;
        @BindView(R.id.txtFee)
        TextView txtFee;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtAddress)
        TextView txtAddress;
        @BindView(R.id.txtCode)
        TextView txtCode;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.txtTime)
        TextView txtTime;
        @BindView(R.id.status_label)
        TextView statusLabel;
        @BindView(R.id.time_label)
        TextView timeLabel;

        @BindView(R.id.btnViewProduct)
        LinearLayout btnViewProduct;

        @BindView(R.id.btnRate)
        LinearLayout btnRate;
        @BindView(R.id.lytFee)
        LinearLayout lytFee;
        @BindView(R.id.lytStatus)
        LinearLayout lytStatus;

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
            txtCode.setText(model.getSecurity_code());
            txtQuantity.setText(String.format(Locale.US, "%d items", model.getCount()));
            imgLink.setVisibility(View.GONE);
            lytFee.setVisibility(View.GONE);
            lytStatus.setVisibility(View.VISIBLE);
            try {
                timeLabel.setText(mContext.getString(R.string.pickup_time));
                statusLabel.setText(mContext.getString(R.string.txt_status));
                int status = 0;
                String[] statusTitles = {"", "Preparing", "Ready for collection", "Finished"};

                if (model.isIs_ready()) {
                    status = 2;
                    imgReady.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_collection_ready));
                } else if (model.isIs_finished()) {
                    status = 3;
                    imgReady.setVisibility(View.GONE);
                } else {
                    status = 1;
                    imgReady.setVisibility(View.GONE);
                }
                txtStatus.setText(statusTitles[status]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            txtTime.setText(model.getTime());

            btnViewProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
            btnRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRate(position, model);
                }
            });

        }
    }
}
