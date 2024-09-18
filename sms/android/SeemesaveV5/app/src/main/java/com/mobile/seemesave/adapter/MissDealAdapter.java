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

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.utils.G;

public class MissDealAdapter extends RecyclerView.Adapter<MissDealAdapter.ViewHolder>{
    private Context mContext;
    private MissDealRecyclerListener mListener;
    private ArrayList<FeedModel> mList = new ArrayList<>();

    public void setData(ArrayList<FeedModel> missDealList) {
        this.mList.clear();
        this.mList.addAll(missDealList);
        notifyDataSetChanged();
    }

    public interface MissDealRecyclerListener{
        void onItemClicked(int pos, FeedModel model);
    }

    public MissDealAdapter(Context context, ArrayList<FeedModel> list, MissDealRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_miss_deal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedModel model = mList.get(holder.getLayoutPosition());
        if (model.getSubMedia().size() > 0) {
            Glide.with(mContext)
                    .load(model.getSubMedia().get(0).getMedia())
                    .centerCrop()
                    .placeholder(R.drawable.ic_me)
                    .into(holder.imgItem);
        }
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
        private ImageView imgOption;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            tvName = itemView.findViewById(R.id.tvName);
            imgOption = itemView.findViewById(R.id.imgOption);
        }
    }
}
