package com.mobile.seemesave.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.view.detail.FeedCommentActivity;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.ArrayList;
import java.util.Locale;

import com.mobile.seemesave.R;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.PostModel;
import com.mobile.seemesave.utils.G;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private Context mContext;
    private PostRecyclerListener mListener;
    private ArrayList<PostModel> mList = new ArrayList<>();

    public interface PostRecyclerListener{
        void onItemClicked(int pos, PostModel model);
        void onOption(int pos, PostModel model);
        void onBookmark(int pos, PostModel model);
    }

    public void setData(ArrayList<PostModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }
    public PostAdapter(Context context, ArrayList<PostModel> list, PostAdapter.PostRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_post, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgUserAvatar)
        ImageView imgUserAvatar;

        @BindView(R.id.txtName)
        TextView txtName;

        @BindView(R.id.txtDuration)
        TextView txtDuration;

        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtDesc)
        TextView txtDesc;

        @BindView(R.id.lytImage)
        RelativeLayout lytImage;
        @BindView(R.id.lytMedia)
        RelativeLayout lytMedia;

        @BindView(R.id.txtLikes)
        TextView txtLikes;
        @BindView(R.id.txtSharesAndComments)
        TextView txtSharesAndComments;

        @BindView(R.id.imgLike)
        ImageView imgLike;
        @BindView(R.id.txtLike)
        TextView txtLike;
        @BindView(R.id.imgComment)
        ImageView imgComment;

        @BindView(R.id.lytLikes)
        LinearLayout lytLike;
        @BindView(R.id.lytComments)
        LinearLayout lytComments;
        @BindView(R.id.lytShare)
        LinearLayout lytShare;

        @BindView(R.id.txtTags)
        SocialTextView txtTags;

        @BindView(R.id.ic_option)
        ImageView ic_option;
        @BindView(R.id.ic_bookmark)
        ImageView ic_bookmark;
        @BindView(R.id.imgPlay)
        ImageView imgPlay;

        @BindView(R.id.imgFeed4_1)
        ImageView imgFeed4_1;
        @BindView(R.id.lytSmallImg)
        LinearLayout lytSmallImg;
        @BindView(R.id.imgFeed4_2)
        ImageView imgFeed4_2;
        @BindView(R.id.imgFeed4_3)
        ImageView imgFeed4_3;
        @BindView(R.id.lytLastImg)
        RelativeLayout lytLastImg;
        @BindView(R.id.imgFeed4_4)
        ImageView imgFeed4_4;
        @BindView(R.id.txtImageCount)
        TextView txtImageCount;
        @BindView(R.id.videoThumb)
        RoundedImageView videoThumb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            if (mList.size() == 0)
                return;
            PostModel model = mList.get(position);

            txtName.setText(model.getTitle());
            ic_bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onBookmark(position, model);
                }
            });
            ic_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onOption(position, model);
                }
            });
            try {
                Glide.with(mContext)
                        .load(model.getUser().getImage_url())
                        .centerCrop()
                        .placeholder(R.drawable.ic_me)
                        .into(imgUserAvatar);
            } catch (Exception e) {
                e.printStackTrace();
            }

            txtDuration.setText(String.format(java.util.Locale.US, "%s", model.getStart_date()));

            txtTitle.setText(model.getTitle());
            if (TextUtils.isEmpty(model.getDescription()))
                txtDesc.setVisibility(View.GONE);
            else
                txtDesc.setText(model.getDescription());
            txtSharesAndComments.setText(String.format(Locale.US, mContext.getString(R.string.shares_comments), model.getReviewCount()));

            setLikeUI(position);

            if (model.isMarked()) {
                ic_bookmark.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_bookmark_y));
            } else {
                ic_bookmark.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_bookmark));
            }
            imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
            if (model.getTagList().size() > 0) {
                txtTags.setVisibility(View.VISIBLE);
                String tagString = "";
                for (int i = 0; i < model.getTagList().size(); i++) {
                    tagString = String.format("%s%s   ", tagString, model.getTagList().get(i));
                }
                txtTags.setLinkText(tagString);
                txtTags.setHintTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                txtTags.setOnLinkClickListener(new SocialTextView.OnLinkClickListener() {
                    @Override
                    public void onLinkClicked(int linkType, String matchedText) {
//                        Intent intent = new Intent(mContext, TagsSearchActivity.class);
//                        intent.putExtra("tag", matchedText.replace("#", ""));
//                        mContext.startActivity(intent);
                    }
                });
            } else {
                txtTags.setVisibility(View.GONE);
            }

            lytLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiCallForPPL("CPL", String.valueOf(model.getId()));
                    model.setLike(!model.isLike());
                    model.setLikeCount(model.isLike() ? model.getLikeCount() + 1 : model.getLikeCount() - 1);
                    setLikeUI(position);
                    if (G.isNetworkAvailable(mContext)) {
                        String token = G.pref.getString("token" , "");
                        Ion.with(mContext)
                                .load(G.SetFeedLikeUrl)
                                .addHeader("Authorization", "Bearer " + token)
                                .setBodyParameter("newsfeed_id", String.valueOf(model.getId()))
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {

                                    }
                                });
                    }

                }
            });
            txtSharesAndComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showComments(position);
                }
            });
            lytComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showComments(position);
                }
            });

            lytShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            lytImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostModel model = mList.get(position);
                    mListener.onItemClicked(position, model);
                }
            });
            lytMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostModel model = mList.get(position);
                    mListener.onItemClicked(position, model);
                }
            });
            if (model.getMedia_type().equalsIgnoreCase("Image")) {
                lytMedia.setVisibility(View.GONE);
                lytImage.setVisibility(View.VISIBLE);
                lytSmallImg.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(model.getMedia())
                        .fitCenter()
                        .into(imgFeed4_1);

            } else if (model.getMedia_type().equalsIgnoreCase("Video")){
                lytMedia.setVisibility(View.VISIBLE);
                lytImage.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(model.getMedia_gif())
                        .centerCrop()
                        .into(videoThumb);
            } else {
                lytMedia.setVisibility(View.GONE);
                lytImage.setVisibility(View.GONE);
            }
        }
        public void apiCallForPPL(String type, String ids){
            String token = G.pref.getString("token" , "");
            String url = String.format(java.util.Locale.US,G.FeedPPLUrl, type, ids);
            Ion.with(mContext)
                    .load(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                        }
                    });
        }
        public void showComments(int position){
            ClickListener listener = new ClickListener() {
                @Override
                public void onClick(boolean flag) {
                    mList.get(position).setReviewCount(mList.get(position).getReviewCount()+1);
                    txtSharesAndComments.setText(String.format(Locale.US, mContext.getString(R.string.shares_comments), mList.get(position).getReviewCount()));
                }
            };
            G.clickListener = listener;
            Intent intent = new Intent(mContext, FeedCommentActivity.class);
            intent.putExtra("promo_id", String.valueOf(mList.get(position).getId()));
            mContext.startActivity(intent);
        }

        public void setLikeUI(int position) {
            PostModel model = mList.get(position);
            if (model.getLikeCount() == 0) {
                txtLikes.setText("");
            } else if (model.getLikeCount() == 1 && model.getLikedFriendsCount() == 0) {
                txtLikes.setText(String.format(Locale.US, "%d Like", model.getLikeCount()));
            } else if (model.getLikeCount() > 1 && model.getLikedFriendsCount() == 0) {
                txtLikes.setText(String.format(Locale.US, "%d Likes", model.getLikeCount()));
            } else if (model.getLikeCount() == 1 && model.getLikedFriendsCount() == 1) {
                txtLikes.setText(String.format(Locale.US, "%d(1 friend) Like", model.getLikeCount()));
            } else if (model.getLikeCount() > 1 && model.getLikedFriendsCount() == 1) {
                txtLikes.setText(String.format(Locale.US, "%d(1 friend) Likes", model.getLikeCount()));
            } else {
                txtLikes.setText(String.format(Locale.US, "%d(%d friends) Likes", model.getLikeCount(), model.getLikedFriendsCount()));
            }


            if (model.isLike()) {
                imgLike.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent));
                txtLike.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            } else {
                imgLike.setColorFilter(ContextCompat.getColor(mContext, R.color.md_grey_600));
                txtLike.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_600));
            }

//            txtLikes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (model.getLikedFriendsCount() > 0) {
//                        apiCallForGetLikedFriends(position);
//                    }
//
//                }
//            });
        }
    }
}
