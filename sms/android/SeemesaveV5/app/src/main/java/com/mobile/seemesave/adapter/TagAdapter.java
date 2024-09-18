package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.listener.RecyclerClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<String> datas;
    private RecyclerClickListener listener;

    public TagAdapter(Context context, ArrayList<String> pDatas, RecyclerClickListener pListener) {
        datas = pDatas;
        listener = pListener;
        mContext = context;
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt)
        TextView txt;

        @BindView(R.id.imgRemove)
        ImageView imgRemove;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            txt.setText("#" + datas.get(position));
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
    }

    // ******************************class ViewHoler redefinition ***************************//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_tag, parent, false));
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