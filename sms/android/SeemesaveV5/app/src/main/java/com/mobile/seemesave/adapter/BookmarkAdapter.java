package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.model.common.BookmarkModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<BookmarkModel> datas;
    private RecyclerClickListener listener;
    private String item_type = "";

    public BookmarkAdapter(Context context, ArrayList<BookmarkModel> pDatas, RecyclerClickListener pListener, String type) {
        datas = pDatas;
        listener = pListener;
        mContext = context;
        item_type = type;
    }

    public void setDatas(ArrayList<BookmarkModel> pDatas){
        datas = pDatas;
        notifyDataSetChanged();
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.li_all)
        ConstraintLayout li_all;

        @BindView(R.id.li_main)
        RelativeLayout li_main;

        @BindView(R.id.li_add)
        RelativeLayout li_add;

        @BindView(R.id.rm_image)
        RoundedImageView rm_image;
        @BindView(R.id.rm_image1)
        RoundedImageView rm_image1;
        @BindView(R.id.rm_image2)
        RoundedImageView rm_image2;
        @BindView(R.id.rm_image3)
        RoundedImageView rm_image3;
        @BindView(R.id.rm_image4)
        RoundedImageView rm_image4;
        @BindView(R.id.rm_add)
        RoundedImageView rm_add;

        @BindView(R.id.rm_close)
        ImageView rm_close;

        @BindView(R.id.rm_name)
        TextView rm_name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            if (datas.get(position).getItem_type().equalsIgnoreCase("all")) {
                li_main.setVisibility(View.GONE);
                li_all.setVisibility(View.VISIBLE);
                li_add.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(datas.get(position).getCover_image())
                        .placeholder(mContext.getDrawable(R.drawable.round_grey))
                        .fitCenter()
                        .into(rm_image1);
                Glide.with(mContext)
                        .load(datas.get(position).getCover_image2())
                        .placeholder(mContext.getDrawable(R.drawable.round_grey))
                        .fitCenter()
                        .into(rm_image2);
                Glide.with(mContext)
                        .load(datas.get(position).getCover_image3())
                        .placeholder(mContext.getDrawable(R.drawable.round_grey))
                        .fitCenter()
                        .into(rm_image3);
                Glide.with(mContext)
                        .load(datas.get(position).getCover_image4())
                        .placeholder(mContext.getDrawable(R.drawable.round_grey))
                        .fitCenter()
                        .into(rm_image4);
            } else if (datas.get(position).getItem_type().equalsIgnoreCase("main")) {
                li_main.setVisibility(View.VISIBLE);
                li_all.setVisibility(View.GONE);
                li_add.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(datas.get(position).getCover_image())
                        .placeholder(mContext.getDrawable(R.drawable.round_grey))
                        .fitCenter()
                        .into(rm_image);
            } else {
                li_main.setVisibility(View.GONE);
                li_all.setVisibility(View.GONE);
                li_add.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load("")
                        .placeholder(mContext.getDrawable(R.drawable.round_grey))
                        .fitCenter()
                        .into(rm_add);
            }

            rm_name.setText(datas.get(position).getName());
            li_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, position);
                }
            });
            rm_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, position);
                }
            });
            li_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, position);
                }
            });
            if (item_type.equalsIgnoreCase("bottom")) {
                rm_close.setVisibility(View.GONE);
            }
            rm_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, position);
                }
            });


        }

    }
    // ******************************class ViewHoler redefinition ***************************//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_bookmark, parent, false));
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