package com.mobile.seemesave.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.view.profile.FriendSelectActivity;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.ArrayList;
import java.util.Locale;

import com.mobile.seemesave.R;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.utils.G;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapterOld extends RecyclerView.Adapter<PostAdapterOld.ViewHolder> {
    private Context mContext;
    private PostRecyclerListener mListener;
    ArrayList<FeedModel> mList;


    public interface PostRecyclerListener {
        void onItemClicked(int pos, FeedModel model);

        void onOption(int pos, FeedModel model);

        void onLike(int pos, FeedModel model);
    }

    public PostAdapterOld(Context context, ArrayList<FeedModel> list, PostRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        mList = list;
    }


    public void setData(ArrayList<FeedModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("view_type:", viewType + "");
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_post_image, parent, false);
            ImageViewHolder viewholder = new ImageViewHolder(view);
            return viewholder;
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_post_video, parent, false);
            VideoViewHolder viewholder = new VideoViewHolder(view);
            return viewholder;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(int position) {
        }
    }

    @Override
    public int getItemViewType(int position) {
        FeedModel feed = mList.get(position);
        String media_type = mList.get(position).getMedia_type();
        if (TextUtils.isEmpty(media_type) || media_type.equalsIgnoreCase("None")) {
            if (feed.getSubMedia().size() > 0) {
                media_type = feed.getSubMedia().get(0).getMedia_Type();
            } else {
                media_type = "None";
            }
        }
        if (media_type.equalsIgnoreCase("Video")) {
            return 1;
        } else {
            return 0;
        }
    }

    public class FeedViewHolder extends ViewHolder {


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


        public FeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            FeedModel model = mList.get(position);
            txtName.setText(model.getTitle());

            ic_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onOption(position, model);
                }
            });
            if (model.getFeed_type().equalsIgnoreCase("UserPost")) {
                Glide.with(mContext)
                        .load(model.getAvatar())
                        .centerCrop()
                        .placeholder(R.drawable.ic_me)
                        .into(imgUserAvatar);
                txtDuration.setText(String.format(java.util.Locale.US, "%s", model.getStart_date()));
            } else {
                Glide.with(mContext)
                        .load(model.getAvatar())
                        .fitCenter()
                        .placeholder(R.drawable.ic_me)
                        .into(imgUserAvatar);
                txtDuration.setText(String.format(java.util.Locale.US, "%s ~ %s", model.getStart_date(), model.getEnd_date()));
            }
            txtTitle.setText(model.getTitle());
            if (TextUtils.isEmpty(model.getDescription()))
                txtDesc.setVisibility(View.GONE);
            else
                txtDesc.setText(model.getDescription());
            txtSharesAndComments.setText(String.format(Locale.US, mContext.getString(R.string.shares_comments), model.getReviewCount()));

            setLikeUI(position);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });

            if (model.getTags().size() > 0) {
                txtTags.setVisibility(View.VISIBLE);
                String tagString = "";
                for (int i = 0; i < model.getTags().size(); i++) {
                    tagString = String.format("%s%s   ", tagString, model.getTags().get(i));
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
                    //apiCallForPPL("CPL", datas.get(position).getId());
                    model.setLike(!model.isLike());
                    model.setLikeCount(model.isLike() ? model.getLikeCount() + 1 : model.getLikeCount() - 1);
                    setLikeUI(position);
                    if (G.isNetworkAvailable(mContext)) {
                        //call api like
                    } else {

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
                    apiCallForPPL("CPH", String.valueOf(model.getId()));
                    if (model.getFeed_type().equalsIgnoreCase("UserPost")) {
                        Intent intent = new Intent(mContext, FriendSelectActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("id", String.valueOf(model.getId()));
                        mContext.startActivity(intent);
                    }else{
                        ClickListener listener = new ClickListener() {
                            @Override
                            public void onClick(boolean flag) {
                                if (flag){
                                    Intent intent = new Intent(mContext, FriendSelectActivity.class);
                                    intent.putExtra("type", 1);
                                    intent.putExtra("id", String.valueOf(model.getId()));
                                    mContext.startActivity(intent);
                                }else{
                                    String text = "A Business social media - SEEMESAVE\n\n" + "https://seemesave.com/feed-detail/" + model.getId();
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_TEXT, text);
                                    mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.app_name)));
                                }
                            }
                        };
                        G.showShareDlg(mContext, listener);
                    }
                }
            });
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
//            G.clickListener = listener;
//            Intent intent = new Intent(mContext, FeedCommentActivity.class);
//            intent.putExtra("promo_id", datas.get(position).getId());
//            mContext.startActivity(intent);
        }

        public void setLikeUI(int position) {
            FeedModel model = mList.get(position);
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

            txtLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getLikedFriendsCount() > 0) {
                        // call api
                    }

                }
            });
        }

    }

    public class ImageViewHolder extends FeedViewHolder {
        @BindView(R.id.lytImg)
        LinearLayout lytImg;
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

        public ImageViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(int position) {
            super.setData(position);
            FeedModel feedModel = mList.get(position);
            if (feedModel.getMedia_type().equalsIgnoreCase("Image")) {
                lytMedia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FeedModel model = mList.get(position);
                        mListener.onItemClicked(position, model);
                    }
                });
                switch (mList.get(position).getMediaList().size()) {
                    case 1:
                        lytSmallImg.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(0))
                                .fitCenter()
                                .into(imgFeed4_1);
                        break;
                    case 2:
                        lytSmallImg.setVisibility(View.VISIBLE);
                        imgFeed4_3.setVisibility(View.GONE);
                        lytLastImg.setVisibility(View.GONE);
                        ((RoundedImageView) imgFeed4_1).setCornerRadius(30, 0, 30, 0);
                        ((RoundedImageView) imgFeed4_2).setCornerRadius(0, 30, 0, 30);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(0))
                                .centerCrop()
                                .into(imgFeed4_1);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(1))
                                .centerCrop()
                                .into(imgFeed4_2);
                        break;
                    case 3:
                        lytSmallImg.setVisibility(View.VISIBLE);
                        lytLastImg.setVisibility(View.GONE);

                        ((RoundedImageView) imgFeed4_1).setCornerRadius(30, 0, 30, 0);
                        ((RoundedImageView) imgFeed4_2).setCornerRadius(0, 30, 0, 0);
                        ((RoundedImageView) imgFeed4_3).setCornerRadius(0, 0, 0, 30);

                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(0))
                                .centerCrop()
                                .into(imgFeed4_1);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(1))
                                .centerCrop()
                                .into(imgFeed4_2);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(2))
                                .centerCrop()
                                .into(imgFeed4_3);
                        break;
                    case 4:
                        lytSmallImg.setVisibility(View.VISIBLE);
                        txtImageCount.setVisibility(View.GONE);

                        ((RoundedImageView) imgFeed4_1).setCornerRadius(30, 0, 30, 0);
                        ((RoundedImageView) imgFeed4_2).setCornerRadius(0, 30, 0, 0);
                        ((RoundedImageView) imgFeed4_3).setCornerRadius(0, 0, 0, 0);
                        ((RoundedImageView) imgFeed4_4).setCornerRadius(0, 0, 0, 30);

                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(0))
                                .centerCrop()
                                .into(imgFeed4_1);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(1))
                                .centerCrop()
                                .into(imgFeed4_2);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(2))
                                .centerCrop()
                                .into(imgFeed4_3);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(3))
                                .centerCrop()
                                .into(imgFeed4_4);
                        break;
                    default:
                        lytSmallImg.setVisibility(View.VISIBLE);
                        txtImageCount.setVisibility(View.VISIBLE);

                        ((RoundedImageView) imgFeed4_1).setCornerRadius(30, 0, 30, 0);
                        ((RoundedImageView) imgFeed4_2).setCornerRadius(0, 30, 0, 0);
                        ((RoundedImageView) imgFeed4_3).setCornerRadius(0, 0, 0, 0);
                        ((RoundedImageView) imgFeed4_4).setCornerRadius(0, 0, 0, 30);

                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(0))
                                .centerCrop()
                                .into(imgFeed4_1);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(1))
                                .centerCrop()
                                .into(imgFeed4_2);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(2))
                                .centerCrop()
                                .into(imgFeed4_3);
                        Glide.with(mContext)
                                .load(mList.get(position).getMediaList().get(3))
                                .centerCrop()
                                .into(imgFeed4_4);
                        imgFeed4_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent intent = new Intent(mContext, PhotoDetailsActivity.class);
//                                intent.putStringArrayListExtra(G.PHOTO_DETAILS_IMAGES, mList.get(position).getMedia());
//                                intent.putExtra(G.PHOTO_DETAILS_SELPOS, 0);
//                                mContext.startActivity(intent);
                            }
                        });
                        txtImageCount.setText(String.format(Locale.US, "+%d", mList.get(position).getMediaList().size() - 4));
                        break;
                }

            }

        }
    }

    public class VideoViewHolder extends FeedViewHolder {
        @BindView(R.id.videoThumb)
        RoundedImageView videoThumb;
        @BindView(R.id.lytMedia)
        RelativeLayout lytMedia;
        public VideoViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(int position) {
            super.setData(position);
            itemView.setTag(this);

            FeedModel feedModel = mList.get(position);
            String media_type = feedModel.getMedia_type();
            String media_gif = "";
            if (TextUtils.isEmpty(media_type) || media_type.equalsIgnoreCase("None")) {
                if (feedModel.getSubMedia().size() > 0) {
                    media_type = feedModel.getSubMedia().get(0).getMedia_Type();
                    media_gif = feedModel.getSubMedia().get(0).getVideoGIF();
                    for (int i = 0; i < feedModel.getSubMedia().size(); i++) {
                        feedModel.getMediaList().add(feedModel.getSubMedia().get(i).getMedia());
                    }
                } else {
                    feedModel.getMediaList().add("");
                    media_type = "None";
                }
            }
            feedModel.setMedia_gif(media_gif);
            feedModel.setMedia_type(media_type);
            if (media_type.equalsIgnoreCase("Video")) {
                Glide.with(mContext)
                        .load(mList.get(position).getMediaList().get(0))
                        .centerCrop()
                        .into(videoThumb);
            }
        }
    }
}
