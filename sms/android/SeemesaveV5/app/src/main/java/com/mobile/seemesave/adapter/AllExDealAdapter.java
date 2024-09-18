package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.PromotionModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllExDealAdapter extends RecyclerView.Adapter<AllExDealAdapter.ViewHolder>{
    private Context mContext;
    private AllExDealAdapter.AllExDealAdapterRecyclerListener mListener;
    private ArrayList<PromotionModel> mList = new ArrayList<>();

    public void setData(ArrayList<PromotionModel> brandList) {
        this.mList.clear();
        this.mList.addAll(brandList);
        notifyDataSetChanged();
    }

    public interface AllExDealAdapterRecyclerListener{
        void onItemClicked(int pos, PromotionModel model);
    }

    public AllExDealAdapter(Context context, ArrayList<PromotionModel> list, AllExDealAdapter.AllExDealAdapterRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public AllExDealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_all_canmiss, parent, false);
        return new AllExDealAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllExDealAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgItem)
        RoundedImageView imgItem;
        @BindView(R.id.tvName)
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void setData(int position) {
            PromotionModel model = mList.get(position);
            if (model.getSubMedia().size() > 0) {
                Glide.with(mContext)
                        .load(model.getSubMedia().get(0).getMedia())
                        .placeholder(R.drawable.ic_me)
                        .into(imgItem);
                itemView.setOnClickListener(v -> {
                    mListener.onItemClicked(position, model);
                });
            }
            tvName.setText(model.getTitle());
        }
        
    }
}
