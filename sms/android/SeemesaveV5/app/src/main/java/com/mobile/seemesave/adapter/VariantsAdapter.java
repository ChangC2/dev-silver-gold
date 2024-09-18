package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.seemesave.R;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.model.common.MCommon;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VariantsAdapter extends RecyclerView.Adapter<VariantsAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<MCommon> datas;
    private RecyclerClickListener listener;

    public VariantsAdapter(Context context, ArrayList<MCommon> pDatas, RecyclerClickListener pListener) {
        datas = pDatas;
        listener = pListener;
        mContext = context;
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtDesc)
        TextView txtDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            Glide.with(mContext)
                    .load(datas.get(position).getImageUrl())
                    .fitCenter()
                    .placeholder(R.drawable.ic_avatar)
                    .into(imgPhoto);
            txtName.setText(datas.get(position).getName());
            txtDesc.setText(datas.get(position).getDescription());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, position);
                }
            });
        }
    }

    // ******************************class ViewHoler redefinition ***************************//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_variant_select, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}