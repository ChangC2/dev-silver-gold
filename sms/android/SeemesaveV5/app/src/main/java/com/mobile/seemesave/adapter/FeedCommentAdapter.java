package com.mobile.seemesave.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mobile.seemesave.R;
import com.mobile.seemesave.listener.CommentClickListener;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.model.common.MCommon;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.utils.TimeUtils;
import com.mobile.seemesave.view.detail.CommentReplyActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedCommentAdapter extends RecyclerView.Adapter<FeedCommentAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<MCommon> datas;
    private RecyclerClickListener listener;

    public FeedCommentAdapter(Context context, ArrayList<MCommon> pDatas, RecyclerClickListener pListener) {
        datas = pDatas;
        listener = pListener;
        mContext = context;
    }

    public void setDatas(ArrayList<MCommon> pDatas){
        datas = pDatas;
        notifyDataSetChanged();
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtDesc)
        TextView txtDesc;
        @BindView(R.id.txtTime)
        TextView txtTime;

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
            try {
                String dateStr = datas.get(position).getCreated();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = df.parse(dateStr);
                df.setTimeZone(TimeZone.getDefault());
                String formattedDate = df.format(date);

                String timeStr = TimeUtils.getTimeAgo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDate).getTime());
                txtTime.setText(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
                txtTime.setText("Just now");
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentClickListener likeClickListener = new CommentClickListener() {
                        @Override
                        public void onClick(String json) {
                            Gson gson  = new Gson();
                            MCommon reply = gson.fromJson(json,  MCommon.class);
                            datas.get(position).getSubItems().add(reply);
                        }
                    };
                    G.commentClickListener = likeClickListener;

                    Intent intent = new Intent(mContext, CommentReplyActivity.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(datas.get(position));
                    intent.putExtra("comment", json);
                    mContext.startActivity(intent);
                }
            });
        }
    }
    // ******************************class ViewHoler redefinition ***************************//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_feed_comment, parent, false));
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