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
import com.mobile.seemesave.model.common.DiscoverModel;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder>{
    private Context mContext;
    private DiscoverRecyclerListener mListener;
    private ArrayList<DiscoverModel> mList = new ArrayList<>();

    public interface DiscoverRecyclerListener{
        void onItemClicked(int pos, DiscoverModel model);
        void onItemAdd(int pos, DiscoverModel model);
    }
    public void setData(ArrayList<DiscoverModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }
    public DiscoverAdapter(Context context, ArrayList<DiscoverModel> list, DiscoverRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_discover, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiscoverModel model = mList.get(holder.getLayoutPosition());
        holder.li_item.setVisibility(View.VISIBLE);
        holder.li_add.setVisibility(View.GONE);
        holder.tvName.setText(model.getName());
        Glide.with(mContext)
                .load(model.getLogo())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgItem);
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
//        if (holder.getLayoutPosition() == 0) {
//            holder.li_item.setVisibility(View.GONE);
//            holder.li_add.setVisibility(View.VISIBLE);
//            holder.li_add.setOnClickListener(v -> {
//                mListener.onItemAdd(holder.getLayoutPosition(), model);
//            });
//        } else {
//            holder.li_item.setVisibility(View.VISIBLE);
//            holder.li_add.setVisibility(View.GONE);
//            holder.tvName.setText(model.getLabel());
//            Glide.with(mContext)
//                    .load(model.getImageUrl())
//                    .centerCrop()
//                    .placeholder(R.drawable.ic_me)
//                    .into(holder.imgItem);
//            holder.itemView.setOnClickListener(v -> {
//                mListener.onItemClicked(holder.getLayoutPosition(), model);
//            });
//        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout li_add;
        private LinearLayout li_item;
        private ImageView imgItem;
        private TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            li_add = itemView.findViewById(R.id.li_add);
            li_item = itemView.findViewById(R.id.li_item);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
