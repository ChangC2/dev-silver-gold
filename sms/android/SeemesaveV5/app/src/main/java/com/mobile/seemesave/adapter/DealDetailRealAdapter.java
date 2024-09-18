package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.RealDetailsModel;

import java.util.ArrayList;

public class DealDetailRealAdapter extends RecyclerView.Adapter<DealDetailRealAdapter.ViewHolder>{
    private Context mContext;
    private DealDetailRealAdapter.DealDetailRealRecyclerListener mListener;
    private ArrayList<RealDetailsModel> mList = new ArrayList<>();

    public interface DealDetailRealRecyclerListener{
        void onItemClicked(int pos, RealDetailsModel model);
    }

    public DealDetailRealAdapter(Context context, ArrayList<RealDetailsModel> list, DealDetailRealAdapter.DealDetailRealRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public DealDetailRealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_deal_detail, parent, false);
        return new DealDetailRealAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealDetailRealAdapter.ViewHolder holder, int position) {
        RealDetailsModel model = mList.get(holder.getLayoutPosition());

        Glide.with(mContext)
                .load(model.getRealProduct().getMedia())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgProduct);
        holder.txtDesc.setText(model.getRealProduct().getDescription());
        holder.txtSize.setText(String.format(java.util.Locale.US,"%s%s", model.getRealProduct().getPackSize(), model.getRealProduct().getUnit()));
        holder.txtCount.setText(String.format(java.util.Locale.US,"✕  %d",  model.getOrgProduct().getQuantity()));


        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProduct;
        private TextView txtSize;
        private TextView txtDesc;
        private TextView txtCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtCount = itemView.findViewById(R.id.txtCount);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtSize = itemView.findViewById(R.id.txtSize);

        }
    }
}
