package com.mobile.seemesave.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.mobile.seemesave.R;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.bookmark.BookMarkAddBottomSheet;
import com.mobile.seemesave.view.detail.FeedCommentActivity;
import com.mobile.seemesave.view.profile.FriendSelectActivity;
import com.mobile.seemesave.view.search.SearchActivity;
import com.tylersuehr.socialtextview.SocialTextView;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private Activity mContext;
    private FeedRecyclerListener mListener;
    private ArrayList<FeedModel> mList = new ArrayList<>();

    public interface FeedRecyclerListener {
        void onItemClicked(int pos, FeedModel model);

        void onOption(int pos, FeedModel model);

        void onBookmark(int pos, FeedModel model);
    }

    public void setData(ArrayList<FeedModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public FeedAdapter(Activity context, ArrayList<FeedModel> list, FeedAdapter.FeedRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_post, parent, false);
        return new FeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.txtSite)
        TextView txtSite;
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

        @BindView(R.id.ic_bookmark)
        ImageView ic_bookmark;
        @BindView(R.id.ic_option)
        ImageView ic_option;
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
            FeedModel model = mList.get(position);

            if (model.getPost_info() != null && !TextUtils.isEmpty(model.getPost_info().getEmail())) {
                txtSite.setVisibility(View.VISIBLE);
                txtSite.setText(model.getPost_info().getSite());
            } else {
                txtSite.setVisibility(View.GONE);
            }
            txtSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = model.getPost_info().getSite();
                    if (!url.startsWith("https://") && !url.startsWith("http://")) {
                        url = "https://" + url;
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);
                }
            });
            txtName.setText(model.getName());

            ic_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onOption(position, model);
                }
            });
            ic_bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onBookmark(position, model);
                }
            });
            if (model.getFeed_type() == null || model.getFeed_type().equalsIgnoreCase("UserPost")) {
                Glide.with(mContext)
                        .load(model.getAvatar())
                        .centerCrop()
                        .placeholder(R.drawable.ic_me)
                        .into(imgUserAvatar);
                txtDuration.setText(String.format(Locale.US, "%s", model.getStart_date()));
            } else {
                Glide.with(mContext)
                        .load(model.getAvatar())
                        .fitCenter()
                        .placeholder(R.drawable.ic_me)
                        .into(imgUserAvatar);
                txtDuration.setText(String.format(Locale.US, "%s ~ %s", model.getStart_date(), model.getEnd_date()));
            }
            txtTitle.setText(model.getTitle());
            if (TextUtils.isEmpty(model.getDescription()))
                txtDesc.setVisibility(View.GONE);
            else
                txtDesc.setText(model.getDescription());
            txtSharesAndComments.setText(String.format(Locale.US, mContext.getString(R.string.shares_comments), model.getReviewCount()));

            if (model.isMarked()) {
                ic_bookmark.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_bookmark_y));
            } else {
                ic_bookmark.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_bookmark));
            }

            setLikeUI(position);

            imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
            if (model.getTags().size() > 0) {
                txtTags.setVisibility(View.VISIBLE);
                String tagString = "";
                for (int i = 0; i < model.getTags().size(); i++) {
                    tagString = String.format("%s%s   ", tagString, "#" + model.getTags().get(i));
                }
                txtTags.setLinkText(tagString);
                txtTags.setHintTextColor(ContextCompat.getColor(mContext, R.color.main_blue_color));
                txtTags.setOnLinkClickListener(new SocialTextView.OnLinkClickListener() {
                    @Override
                    public void onLinkClicked(int linkType, String matchedText) {
                        Intent intent = new Intent(mContext, SearchActivity.class);
                        intent.putExtra("tag", matchedText.replace("#", ""));
                        mContext.startActivity(intent);
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
                        String token = G.pref.getString("token", "");
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
                    apiCallForPPL("CPH", String.valueOf(model.getId()));
                    if (model.getFeed_type().equalsIgnoreCase("UserPost")) {
                        Intent intent = new Intent(mContext, FriendSelectActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("id", String.valueOf(model.getId()));
                        mContext.startActivity(intent);
                    } else {
                        ClickListener listener = new ClickListener() {
                            @Override
                            public void onClick(boolean flag) {
                                if (flag) {
                                    Intent intent = new Intent(mContext, FriendSelectActivity.class);
                                    intent.putExtra("type", 1);
                                    intent.putExtra("id", String.valueOf(model.getId()));
                                    mContext.startActivity(intent);
                                } else {
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

            lytImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedModel model = mList.get(position);
                    mListener.onItemClicked(position, model);
                }
            });
            lytMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedModel model = mList.get(position);
                    mListener.onItemClicked(position, model);
                }
            });
            if (model.getMedia_type().equalsIgnoreCase("Image")) {
                lytMedia.setVisibility(View.GONE);
                lytImage.setVisibility(View.VISIBLE);

                switch (model.getMediaList().size()) {
                    case 1:
                        lytSmallImg.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(0))
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
                                .load(model.getMediaList().get(0))
                                .centerCrop()
                                .into(imgFeed4_1);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(1))
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
                                .load(model.getMediaList().get(0))
                                .centerCrop()
                                .into(imgFeed4_1);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(1))
                                .centerCrop()
                                .into(imgFeed4_2);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(2))
                                .centerCrop()
                                .into(imgFeed4_3);
                        break;
                    case 4:
                        lytSmallImg.setVisibility(View.VISIBLE);
                        txtImageCount.setVisibility(View.GONE);

//                        ((RoundedImageView) imgFeed4_1).setCornerRadius(Corner.BOTTOM_LEFT, 30);
//                        ((RoundedImageView) imgFeed4_1).setCornerRadius(Corner.TOP_LEFT, 30);
                        ((RoundedImageView) imgFeed4_2).setCornerRadius(30, 0, 30, 0);
                        ((RoundedImageView) imgFeed4_2).setCornerRadius(0, 30, 0, 0);
                        ((RoundedImageView) imgFeed4_3).setCornerRadius(0, 0, 0, 0);
                        ((RoundedImageView) imgFeed4_4).setCornerRadius(0, 0, 0, 30);

                        Glide.with(mContext)
                                .load(model.getMediaList().get(0))
                                .centerCrop()
                                .into(imgFeed4_1);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(1))
                                .centerCrop()
                                .into(imgFeed4_2);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(2))
                                .centerCrop()
                                .into(imgFeed4_3);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(3))
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
                                .load(model.getMediaList().get(0))
                                .centerCrop()
                                .into(imgFeed4_1);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(1))
                                .centerCrop()
                                .into(imgFeed4_2);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(2))
                                .centerCrop()
                                .into(imgFeed4_3);
                        Glide.with(mContext)
                                .load(model.getMediaList().get(3))
                                .centerCrop()
                                .into(imgFeed4_4);
                        imgFeed4_4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent intent = new Intent(mContext, PhotoDetailsActivity.class);
//                                intent.putStringArrayListExtra(G.PHOTO_DETAILS_IMAGES, model.getMedia());
//                                intent.putExtra(G.PHOTO_DETAILS_SELPOS, 0);
//                                mContext.startActivity(intent);
                            }
                        });
                        txtImageCount.setText(String.format(Locale.US, "+%d", model.getMediaList().size() - 4));
                        break;
                }

            } else if (model.getMedia_type().equalsIgnoreCase("Video")) {
                lytMedia.setVisibility(View.VISIBLE);
                lytImage.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(model.getMediaList().get(0))
                        .centerCrop()
                        .into(videoThumb);
            } else {
                lytMedia.setVisibility(View.GONE);
                lytImage.setVisibility(View.GONE);
            }
        }

        public void apiCallForPPL(String type, String ids) {
            String token = G.pref.getString("token", "");
            String url = String.format(java.util.Locale.US, G.FeedPPLUrl, type, ids);
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

        public void showComments(int position) {
            ClickListener listener = new ClickListener() {
                @Override
                public void onClick(boolean flag) {
                    mList.get(position).setReviewCount(mList.get(position).getReviewCount() + 1);
                    txtSharesAndComments.setText(String.format(Locale.US, mContext.getString(R.string.shares_comments), mList.get(position).getReviewCount()));
                }
            };
            G.clickListener = listener;
            Intent intent = new Intent(mContext, FeedCommentActivity.class);
            intent.putExtra("promo_id", String.valueOf(mList.get(position).getId()));
            mContext.startActivity(intent);
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
//        public void apiCallForGetLikedFriends(int position) {
//            ArrayList<MCommon> friends = new ArrayList<>();
//            G.showLoading(mContext);
//            String token = G.pref.getString("token" , "");
//            String url = String.format(java.util.Locale.US,G.GetLikedFriendUrl,  mList.get(position).getId());
//            Ion.with(mContext)
//                    .load(url)
//                    .addHeader("Authorization", "Bearer " + token)
//                    .asString()
//                    .setCallback(new FutureCallback<String>() {
//                        @Override
//                        public void onCompleted(Exception e, String result) {
//                            G.hideLoading();
//                            if (e != null){
//                            }else{
//                                try {
//                                    JSONObject jsonObject = new JSONObject(result);
//                                    if (jsonObject.getBoolean("status")){
//                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                                        for (int i=0;i<jsonArray.length();i++){
//                                            JSONObject friendInfo = jsonArray.getJSONObject(i);
//                                            MCommon friend = new MCommon();
//                                            friend.setFreindInfoId(friendInfo.getString("id"));
//                                            friend.setId(friendInfo.getJSONObject("Friend").getString("id"));
//                                            friend.setName(friendInfo.getJSONObject("Friend").optString("first_name") + " " + friendInfo.getJSONObject("Friend").optString("last_name"));
//                                            friend.setImageUrl(friendInfo.getJSONObject("Friend").optString("image_url"));
//                                            friends.add(friend);
//                                        }
//                                        if (friends.size() > 0){
//                                            showFriends(friends);
//                                        }
//                                    }
//                                } catch (JSONException jsonException) {
//                                }
//                            }
//                        }
//                    });
//        }
//        public void showFriends(ArrayList<MCommon> friends){
//            Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
//            dialog.setContentView(R.layout.dlg_liked_friends);
//            dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
//            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerFriends);
//            recyclerView.setLayoutManager(linearLayoutManager);
//            RFeedLikedFriendsAdapter friendListAdapter = new RFeedLikedFriendsAdapter(mContext, friends, listener);
//            recyclerView.setAdapter(friendListAdapter);
//        }
    }
}
