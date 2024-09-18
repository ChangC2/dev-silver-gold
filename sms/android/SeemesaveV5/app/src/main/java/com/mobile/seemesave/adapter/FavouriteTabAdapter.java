package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.FavouriteTabModel;

public class FavouriteTabAdapter extends RecyclerView.Adapter<FavouriteTabAdapter.ViewHolder>{
    private Context mContext;
    private FavouriteTabRecyclerListener mListener;
    private ArrayList<FavouriteTabModel> mList = new ArrayList<>();
    private int selected_index = 0;
    public interface FavouriteTabRecyclerListener{
        void onItemClicked(int pos, FavouriteTabModel model);
    }

    public void setSelected_index(int index){
        selected_index = index;
        notifyDataSetChanged();
    }

    public FavouriteTabAdapter(Context context, ArrayList<FavouriteTabModel> list, FavouriteTabRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_favourite_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavouriteTabModel model = mList.get(holder.getLayoutPosition());
        holder.tvName.setText(model.getLabel());
        if (holder.getLayoutPosition() == selected_index) {
            holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.main_blue_color));
        }else {
            holder.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_500));
        }
        holder.itemView.setOnClickListener(v -> {
            selected_index = holder.getLayoutPosition();
            notifyDataSetChanged();
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}
