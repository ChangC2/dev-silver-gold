package com.mobile.seemesave.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.utils.G;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class Watch_Videos_Adapter extends RecyclerView.Adapter<Watch_Videos_Adapter.CustomViewHolder> {

    public Context context;
    private Watch_Videos_Adapter.OnItemClickListener listener;
    private ArrayList<FeedModel> dataList = new ArrayList<>();
    private Watch_Videos_Adapter.LikedClicked liked_listener;

    public void setData(ArrayList<FeedModel> mediaList) {
        this.dataList.clear();
        this.dataList.addAll(mediaList);
        notifyDataSetChanged();
    }

    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, FeedModel item, View view);
    }

    public interface LikedClicked {
        void like_clicked(View view, FeedModel item, int position);
    }

    public Watch_Videos_Adapter(Context context, ArrayList<FeedModel> dataList, Watch_Videos_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList.clear();
        this.dataList.addAll(dataList);
        this.listener = listener;

    }

    @Override
    public Watch_Videos_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_video_view_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        Watch_Videos_Adapter.CustomViewHolder viewHolder = new Watch_Videos_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @Override
    public void onBindViewHolder(final Watch_Videos_Adapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int pos) {
        final FeedModel item = dataList.get(pos);
        try {

            holder.bind(pos, item, listener);


            holder.username.setText(item.getName());
            holder.duet_txt.setText("" + item.getTitle());
            holder.desc_txt.setText("" + item.getDescription());
            if (item.getAvatar() != null && !item.getAvatar().equalsIgnoreCase("")) {
                Glide.with(context)
                        .load(item.getAvatar())
                        .fitCenter()
                        .placeholder(R.drawable.ic_avatar)
                        .into(holder.user_pic);
            }
            if (item.isLike()) {
                holder.like_image.setLikeDrawable(context.getResources().getDrawable(R.drawable.ic_heart_gradient));
                holder.like_image.setLiked(true);
            } else {
                holder.like_image.setLikeDrawable(context.getResources().getDrawable(R.drawable.ic_unliked));
                holder.like_image.setLiked(false);
            }
            if (item.isMarked()) {
                holder.ic_marked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bookmark_y));
            } else {
                holder.ic_marked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bookmark_w));
            }
            holder.like_txt.setText(item.getLikeCount() + "");
            holder.comment_txt.setText(item.getReviewCount() + "");


            Glide.with(context)
                    .load(item.getMedia_gif())
                    .fitCenter()
                    .into(holder.imageGif);

            if (item.getMedia_type().equalsIgnoreCase("Video")) {
                holder.playerView.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.volumnControlImg.setVisibility(View.VISIBLE);
            } else {
                holder.playerView.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.volumnControlImg.setVisibility(View.GONE);
//                Glide.with(context)
//                        .load(item.getMediaList().get(0))
//                        .placeholder(R.drawable.ic_splash_logo)
//                        .fitCenter()
//                        .into(holder.imageView);
                holder.imageView.setSliderAdapter(new ImageSliderAdapter(context, item.getMediaList()));
            }
        } catch (Exception e) {
            Log.e("excenption:", e.toString() + ":" + pos);
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        StyledPlayerView playerview;
        TextView username, duet_txt;
        ImageView user_pic, sound_image, varified_btn;
        RelativeLayout duet_layout_username, itemView;
        LinearLayout comment_layout, shared_layout;
        public LikeButton like_image;
        ImageView comment_image, ic_marked;
        SliderView imageView;
        public TextView like_txt, comment_txt, duet_username;
        TextView desc_txt;
        ProgressBar progressBar;

        StyledPlayerView playerView;
        ImageView imageGif;
        ImageView volumnControlImg;
        public CustomViewHolder(View view) {
            super(view);

            itemView = view.findViewById(R.id.mainlayout);
            imageView = view.findViewById(R.id.media_image);
            playerview = view.findViewById(R.id.playerview);
            username = view.findViewById(R.id.username);
            duet_txt = view.findViewById(R.id.duet_txt);
            user_pic = view.findViewById(R.id.user_pic);
            varified_btn = view.findViewById(R.id.varified_btn);
            like_txt = view.findViewById(R.id.like_txt);
            duet_layout_username = view.findViewById(R.id.duet_layout_username);
            like_image = view.findViewById(R.id.likebtn);
            ic_marked = view.findViewById(R.id.ic_bookmark);
            progressBar = view.findViewById(R.id.progressBar);

            comment_layout = view.findViewById(R.id.comment_layout);
            comment_image = view.findViewById(R.id.comment_image);
            comment_txt = view.findViewById(R.id.comment_txt);

            desc_txt = view.findViewById(R.id.desc_txt);

            shared_layout = view.findViewById(R.id.shared_layout);


            playerView = view.findViewById(R.id.playerview);
            imageGif = view.findViewById(R.id.img_gif);
            volumnControlImg = view.findViewById(R.id.volume_control);
        }

        public void bind(final int postion, final FeedModel item, final Watch_Videos_Adapter.OnItemClickListener listener) {
            user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion, item, v);
                }
            });


            comment_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (G.isNetworkAvailable(context)) {
                        listener.onItemClick(postion, item, v);
                    }
                }
            });

            shared_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (G.isNetworkAvailable(context)) {
                        listener.onItemClick(postion, item, v);
                    }
                }
            });

            ic_marked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion, item, v);
                }
            });

            like_image.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    dataList.get(postion).setLike(true);
                    int count = dataList.get(postion).getLikeCount() + 1;
                    dataList.get(postion).setLikeCount(count);
                    like_txt.setText(String.valueOf(count));
                    like_image.setLikeDrawable(context.getResources().getDrawable(R.drawable.ic_heart_gradient));
                    like_image.setLiked(true);
                    return;
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if (dataList.get(postion).getLikeCount() == 0) return;
                    dataList.get(postion).setLike(false);
                    int count = dataList.get(postion).getLikeCount() - 1;
                    dataList.get(postion).setLikeCount(count);
                    like_txt.setText(String.valueOf(count));
                    like_image.setLikeDrawable(context.getResources().getDrawable(R.drawable.ic_unliked));
                    like_image.setLiked(false);
                    return;
                }
            });


        }


    }


}