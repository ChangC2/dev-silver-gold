package com.mobile.seemesave.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
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
import com.mobile.seemesave.model.common.DeliverModel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartDeliverPaidAdapter extends RecyclerView.Adapter<CartDeliverPaidAdapter.ViewHolder>{
    private Context mContext;
    private CartDeliverPaidAdapter.CartDeliverPaidRecyclerListener mListener;
    private ArrayList<DeliverModel> mList = new ArrayList<>();

    public void setData(ArrayList<DeliverModel> sellingList) {
        this.mList.clear();
        this.mList.addAll(sellingList);
        notifyDataSetChanged();
    }

    public interface CartDeliverPaidRecyclerListener{
        void onItemClicked(int pos, DeliverModel model);
        void onRate(int pos, DeliverModel model);
        void onLink(int pos, DeliverModel model);
        
    }

    public CartDeliverPaidAdapter(Context context, ArrayList<DeliverModel> list, CartDeliverPaidAdapter.CartDeliverPaidRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public CartDeliverPaidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_cart_paid, parent, false);
        return new CartDeliverPaidAdapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull CartDeliverPaidAdapter.ViewHolder holder, int position) {
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
            DeliverModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getImage_url())
                    .fitCenter()
                    .into(imgPhoto);
            txtName.setText(model.getName());
            txtAddress.setText(model.getAddress());
            txtTime.setText(model.getTime());
            txtCode.setText(String.valueOf(model.getId()));
            txtQuantity.setText(String.format(Locale.US, "%d items", model.getCount()));
            lytFee.setVisibility(View.VISIBLE);
            timeLabel.setText(mContext.getString(R.string.txt_deliver_time));
            statusLabel.setText(mContext.getString(R.string.total_price_label));
            txtFee.setText(String.format(java.util.Locale.US,"%s %.2f", model.getCurrency().getSimple(), Float.valueOf(model.getDelivery_price())));
            if (TextUtils.isEmpty(model.getDeliveryLink())) {
                imgLink.setVisibility(View.GONE);
            } else {
                imgLink.setVisibility(View.VISIBLE);
            }
            lytStatus.setVisibility(View.VISIBLE);
            imgLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onLink(position, model);
                }
            });
            float price = model.getDelivery_price() + model.getPrice();
            txtStatus.setText(String.format(java.util.Locale.US,"%s %.2f", model.getCurrency().getSimple(), Float.valueOf(price)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                txtStatus.setTextColor(mContext.getColor(R.color.md_grey_800));
            }
            imgReady.setVisibility(View.VISIBLE);
            switch(model.getStatus()) {
                case 2:
                    imgReady.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_deliver_paid));
                    break;
                case 11:
                    imgReady.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_deliver_read));
                    break;
                case 12:
                    imgReady.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_deliver_preparing));
                    break;
                case 13:
                    imgReady.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_collection_ready));
                    break;
                case 14:
                    imgReady.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_deliver_processing));
                    break;
                case 50:
                    imgReady.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_deliver_failed));
                    break;
                case 30:
                    imgReady.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_deliver_delived));
                    break;
                case 31:
                    imgReady.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_deliver_finished));
                    break;
                default:
                    imgReady.setVisibility(View.GONE);
                    break;
            }

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
