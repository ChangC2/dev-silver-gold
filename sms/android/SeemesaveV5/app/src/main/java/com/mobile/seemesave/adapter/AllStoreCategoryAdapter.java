package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.StoreCategoryModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllStoreCategoryAdapter extends RecyclerView.Adapter<AllStoreCategoryAdapter.ViewHolder>{
    private Context mContext;
    private AllStoreCategoryAdapter.StoreCategoryAdapterRecyclerListener mListener;
    private ArrayList<StoreCategoryModel> mList = new ArrayList<>();

    public void setData(ArrayList<StoreCategoryModel> brandList) {
        this.mList.clear();
        this.mList.addAll(brandList);
        notifyDataSetChanged();
    }

    public interface StoreCategoryAdapterRecyclerListener{
        void onItemClicked(int pos, StoreCategoryModel model);
    }

    public AllStoreCategoryAdapter(Context context, ArrayList<StoreCategoryModel> list, AllStoreCategoryAdapter.StoreCategoryAdapterRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public AllStoreCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_all_store_category, parent, false);
        return new AllStoreCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllStoreCategoryAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvName)
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void setData(int position) {
            StoreCategoryModel model = mList.get(position);
            tvName.setText(model.getName());
            itemView.setOnClickListener(v -> {
                mListener.onItemClicked(position, model);
            });

        }
        
    }
}
