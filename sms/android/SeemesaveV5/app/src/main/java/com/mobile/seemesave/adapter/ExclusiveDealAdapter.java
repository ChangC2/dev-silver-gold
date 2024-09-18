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
import com.mobile.seemesave.model.common.PromotionModel;

import java.util.ArrayList;

public class ExclusiveDealAdapter extends RecyclerView.Adapter<ExclusiveDealAdapter.ViewHolder>{
    private Context mContext;
    private ExclusiveDealRecyclerListener mListener;
    private ArrayList<PromotionModel> mList = new ArrayList<>();

    public void setData(ArrayList<PromotionModel> exclusiveDealList) {
        this.mList.clear();
        this.mList.addAll(exclusiveDealList);
        notifyDataSetChanged();
    }

    public interface ExclusiveDealRecyclerListener{
        void onItemClicked(int pos, PromotionModel model);
    }

    public ExclusiveDealAdapter(Context context, ArrayList<PromotionModel> list, ExclusiveDealRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_special_deal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PromotionModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getSubMedia().get(0).getMedia())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgItem);
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
        holder.tvName.setText(model.getTitle());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgItem;
        private TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
