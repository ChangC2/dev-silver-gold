package com.mobile.seemesave.view.detail;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.adapter.Watch_Videos_Adapter;
import com.mobile.seemesave.base.BaseActivity;
import com.mobile.seemesave.listener.ClickListener;
import com.mobile.seemesave.model.common.FeedModel;
import com.mobile.seemesave.model.common.ProductModel;
import com.mobile.seemesave.sqlite.DatabaseQueryClass;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.bookmark.BookMarkAddBottomSheet;
import com.mobile.seemesave.view.cart.DeliverCartActivity;
import com.mobile.seemesave.view.cart.ShoppingCartActivity;
import com.mobile.seemesave.view.profile.FriendActivity;
import com.mobile.seemesave.view.profile.FriendDetailActivity;
import com.mobile.seemesave.view.profile.FriendSelectActivity;
import com.mobile.seemesave.view_model.detail.VideoPlayViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayerActivity extends BaseActivity implements Player.EventListener {

    private VideoPlayerActivity activity;
    private VideoPlayViewModel mViewModel;

    SimpleExoPlayer privious_player;
    Handler handler;
    Runnable runnable;
    Boolean animation_running = false;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    Watch_Videos_Adapter adapter;


    private int post_id = 0;
    private String media_url = "";
    private int offset = 0;
    private int limit = 100;
    private boolean isLoading = false;
    private boolean isLast = false;

    private enum VolumeState {ON, OFF};
    private VolumeState volumeState = VolumeState.OFF;

    int position = 0;
    int currentPage = -1;

    ArrayList<FeedModel> mediaList = new ArrayList<>();

    private SimpleExoPlayer player;
    String feedData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VideoPlayViewModel.class);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        activity = this;
        media_url = getIntent().getStringExtra("url");
        post_id = getIntent().getIntExtra("post_id", 0);
        Gson gson = new Gson();
        feedData = getIntent().getStringExtra("feed_data");
        if (feedData != null) {
            mediaList = gson.fromJson(getIntent().getStringExtra("feed_data"), new TypeToken<ArrayList<FeedModel>>() {
            }.getType());
        }
        initView();
    }
    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getIsBusy().observe(this, isBusy -> {
            if (isBusy) {
                showLoadingDialog();
            } else {
                hideLoadingDialog();
            }
        });
        mViewModel.getPostList().observe(this, list -> {
            if (offset == 0 && list.size() == 0) {
                return;
            } else {
                if (offset == 0) {
                    mediaList.clear();
                }
                if (list.size() < limit) {
                    isLast = true;
                }
                mediaList.addAll(list);
                adapter.setData(mediaList);
                isLoading = false;
            }
        });
    }
    private void initView() {
        G.black_status_bar(activity);

        handler = new Handler();

        Set_Adapter();
        if (post_id != 0) {
            mViewModel.setPost_id(post_id);
            if (feedData == null) {
                try {
                    String local_data = DatabaseQueryClass.getInstance().getData(G.getUserID(), "VideoDetail", String.valueOf(post_id));
                    if (TextUtils.isEmpty(local_data)) {
                        mViewModel.setIsBusy(true);
                    } else {
                        mViewModel.loadLocalData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mViewModel.loadData();
            }
        }
    }
    public void Set_Adapter() {
        recyclerView = findViewById(R.id.recylerview);
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        adapter = new Watch_Videos_Adapter(activity, mediaList, new Watch_Videos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, final FeedModel item, View view) {

                switch (view.getId()) {

                    case R.id.user_pic:
                        Intent intent;
                        if (item.getFeed_type().equalsIgnoreCase("UserPost")) {
                            intent = new Intent(activity, FriendDetailActivity.class);
                            intent.putExtra("friend_id", feedData == null ? item.getRelated_id() : item.getUser().getId());
                            intent.putExtra("image", item.getAvatar());
                            intent.putExtra("name", item.getName());
                            startActivity(intent);
                        } else if (item.getFeed_type().equalsIgnoreCase("VendorPost")) {
                            intent = new Intent(activity, StoreDetailActivity.class);
                            intent.putExtra("id", feedData == null ? item.getRelated_id() : item.getUser().getId());
                            startActivity(intent);
                        } else if (item.getFeed_type().equalsIgnoreCase("SupplierPost")) {
                            intent = new Intent(activity, BrandDetailActivity.class);
                            intent.putExtra("id", feedData == null ? item.getRelated_id() : item.getUser().getId());
                            startActivity(intent);
                        } else {
                            intent = new Intent(activity, PromotionDetailActivity.class);
                            intent.putExtra("id", item.getRelated_id());
                            intent.putExtra("media", item.getMedia());
                            intent.putExtra("feed_type", item.getFeed_type());
                            intent.putExtra("brand", item.getTitle());
                        }
//                        if (item.getFeed_type().equalsIgnoreCase("Promotion")) {
//                            intent = new Intent(activity, PromotionDetailActivity.class);
//                            intent.putExtra("id", Integer.valueOf(item.getRelated_id()));
//                            startActivity(intent);
//                            return;
//                        }
//                        if (item.getFeed_type().equalsIgnoreCase("Store")) {
//                            intent = new Intent(activity, StoreDetailActivity.class);
//                            intent.putExtra("id", Integer.valueOf(item.getRelated_id()));
//                            startActivity(intent);
//                            return;
//                        }
//                        if (item.getFeed_type().equalsIgnoreCase("Post")) {
//                            intent = new Intent(activity, FriendDetailActivity.class);
//                            intent.putExtra("friend_id", feedData == null ? item.getRelated_id() : item.getUser().getId());
//                            intent.putExtra("image", item.getAvatar());
//                            intent.putExtra("name", item.getName());
//                            startActivity(intent);
//                            return;
//                        }
//                        if (item.getFeed_type().equalsIgnoreCase("Brand")) {
//                            intent = new Intent(activity, BrandDetailActivity.class);
//                            intent.putExtra("id", Integer.valueOf(item.getRelated_id()));
//                            startActivity(intent);
//                            return;
//                        }
//
//                        if (item.getFeed_type().equalsIgnoreCase("CollectCart")) {
//                            intent = new Intent(activity, ShoppingCartActivity.class);
//                            startActivity(intent);
//                            return;
//                        }
//                        if (item.getFeed_type().equalsIgnoreCase("DeliverCart")) {
//                            intent = new Intent(activity, DeliverCartActivity.class);
//                            startActivity(intent);
//                            return;
//                        }
//                        if (item.getFeed_type().equalsIgnoreCase("Product")) {
//                            intent = new Intent(activity, ProductDetailActivity.class);
//                            intent.putExtra("barcode", item.getRelated_id());
//                            startActivity(intent);
//                            return;
//                        }
//                        if (item.getFeed_type().equalsIgnoreCase("Friend")) {
//                            intent = new Intent(activity, FriendActivity.class);
//                            startActivity(intent);
//                            return;
//                        }
//                        intent = new Intent(activity, PromotionDetailActivity.class);
//                        intent.putExtra("id", item.getRelated_id());
//                        intent.putExtra("media", item.getMedia());
//                        intent.putExtra("feed_type", item.getFeed_type());
//                        intent.putExtra("brand", item.getTitle());
//                        startActivity(intent);
                        break;
                    case R.id.comment_layout:
                        Intent i = new Intent(activity, FeedCommentActivity.class);
                        i.putExtra("promo_id", String.valueOf(item.getId()));
                        startActivity(i);
                        break;
                    case R.id.shared_layout:
                        apiCallForPPL("CPH", String.valueOf(item.getId()));
                        if (item.getFeed_type().equalsIgnoreCase("UserPost")) {
                            Intent intents = new Intent(activity, FriendSelectActivity.class);
                            intents.putExtra("type", 1);
                            intents.putExtra("id", String.valueOf(item.getId()));
                            startActivity(intents);
                        } else {
                            ClickListener listener = new ClickListener() {
                                @Override
                                public void onClick(boolean flag) {
                                    if (flag) {
                                        Intent intent = new Intent(activity, FriendSelectActivity.class);
                                        intent.putExtra("type", 1);
                                        intent.putExtra("id", String.valueOf(item.getId()));
                                        startActivity(intent);
                                    } else {
                                        String text = "Share From Seemesave\n\n" + "https://seemesave.com/feed-detail/" + item.getId();
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.setType("text/plain");
                                        intent.putExtra(Intent.EXTRA_TEXT, text);
                                        startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
                                    }
                                }
                            };
                            G.showShareDlg(activity, listener);
                        }
                        break;
                    case R.id.ic_bookmark:
                        if (item.isMarked()) {
                            apiCallForDeleteBookmark(item.getId(), pos);
                        } else {
                            BookMarkAddBottomSheet detailFragment = BookMarkAddBottomSheet.newInstance((cID, pID) -> {
                                apiCallForSetBookmark(pID, cID, pos);
                            });
                            detailFragment.post_id = item.getId();
                            detailFragment.show(getSupportFragmentManager(), "AddBookMarkBottomSheet");
                        }
                        break;

                }

            }
        });

//        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);


        // this is the scroll listener of recycler view which will tell the current item number
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no = scrollOffset / height;

                if (page_no != currentPage) {
                    currentPage = page_no;

                    Privious_Player();
                    Set_Player(currentPage);
                }
            }
        });
        recyclerView.scrollToPosition(position);

    }
    void apiCallForDeleteBookmark(int post_id, int position) {
        if (G.isNetworkAvailable(activity)) {
            Ion.with(activity)
                    .load("DELETE", G.RemoveFeedBookmarkUrl)
                    .addHeader("Authorization", "Bearer " + G.pref.getString("token", ""))
                    .setBodyParameter("feed_id", String.valueOf(post_id))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            FeedModel item;
                            item = mediaList.get(position);
                            item.setMarked(false);
                            mediaList.set(position, item);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }

                        }
                    });
        } else {
            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
        }
    }
    void apiCallForSetBookmark(int post_id, String category_id, int position) {
        if (G.isNetworkAvailable(activity) && !category_id.equalsIgnoreCase("-1")) {
            JsonObject json = new JsonObject();
            json.addProperty("category_id", category_id);
            json.addProperty("feed_id", post_id);
            String token = G.pref.getString("token", "");
            showLoadingDialog();
            Ion.with(this)
                    .load("POST", G.SetFeedBookmarkUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .setJsonObjectBody(json)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            hideLoadingDialog();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getBoolean("status")) {
                                    FeedModel item;
                                    item = mediaList.get(position);
                                    item.setMarked(true);
                                    mediaList.set(position, item);
                                    if (adapter != null) {
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(activity, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(activity, R.string.connection_fail, Toast.LENGTH_LONG).show();
        }
    }
    public void Privious_Player() {
        if (privious_player != null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }

    SimpleExoPlayer cache_player;

    public void Call_cache() {

        if (currentPage + 1 < mediaList.size()) {
            FeedModel item = mediaList.get(currentPage + 1);

            if (item.getMedia_type().equalsIgnoreCase("Video")) {
                if (cache_player != null)
                    cache_player.release();

                cache_player = new SimpleExoPlayer.Builder(activity).build();
                Uri mp4VideoUri = Uri.parse(mediaList.get(currentPage + 1).getMediaList().get(0));
                cache_player.setMediaItem(MediaItem.fromUri(mp4VideoUri));
                cache_player.prepare();
            }

        }

    }

    public void Set_Player(final int currentPage) {

        Log.e("Playing:", currentPage + "Ok");
        final FeedModel item = mediaList.get(currentPage);

        Call_cache();

        player = new SimpleExoPlayer.Builder(activity).build();
        player.setRepeatMode(Player.REPEAT_MODE_OFF);

        View layout = layoutManager.findViewByPosition(currentPage);
        StyledPlayerView playerView = layout.findViewById(R.id.playerview);

        final RelativeLayout mainlayout = layout.findViewById(R.id.mainlayout);
        ProgressBar progressBar = layout.findViewById(R.id.progressBar);
        ImageView volumnControlImg = layout.findViewById(R.id.volume_control);
        if (item.getMedia_type().equalsIgnoreCase("Video")) {

            playerView.setVisibility(VISIBLE);

            playerView.setPlayer(player);
            playerView.setControllerAutoShow(false);
            playerView.setUseController(true);

            Uri mp4VideoUri = Uri.parse(item.getMediaList().get(0));
            player.setMediaItem(MediaItem.fromUri(mp4VideoUri));
            player.prepare();
            player.play();

            volumnControlImg.bringToFront();
            //volumeState = VolumeState.OFF;
            if (G.getAudioStatus().equalsIgnoreCase("true") && volumeState == VolumeState.ON) {
                player.setVolume(1f);
                volumnControlImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_unmute));
            } else {
                player.setVolume(0f);
                volumnControlImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mute));
            }
            if (!G.isNetworkAvailable(this)) {
                progressBar.setVisibility(GONE);
                playerView.setVisibility(GONE);
            }

            volumnControlImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (G.isNetworkAvailable(activity)) {
                        if (volumeState == VolumeState.OFF) {
                            player.setVolume(1f);
                            volumnControlImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_unmute));
                            volumeState = VolumeState.ON;
                        } else if (volumeState == VolumeState.ON) {
                            player.setVolume(0f);
                            volumnControlImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mute));
                            volumeState = VolumeState.OFF;
                        }
                    }
                }
            });

            player.addListener(new Player.EventListener() {

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

                @Override
                public void onLoadingChanged(boolean isLoading) {}

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {

                        case Player.STATE_BUFFERING:
                            if (progressBar != null) {
                                progressBar.setVisibility(VISIBLE);
                            }

                            break;
                        case Player.STATE_ENDED:
                            player.seekTo(0);
                            break;
                        case Player.STATE_IDLE:

                            break;
                        case Player.STATE_READY:
                            if (progressBar != null) {
                                progressBar.setVisibility(GONE);
                            }
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {}

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}


                @Override
                public void onPositionDiscontinuity(int reason) {}

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {}
            });

            privious_player = player;

            playerView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        super.onFling(e1, e2, velocityX, velocityY);
                        float deltaX = e1.getX() - e2.getX();
                        float deltaXAbs = Math.abs(deltaX);
                        // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                        if ((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                            if (deltaX > 0) {
                                //OpenProfile(item, true);
                            }
                        }


                        return true;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        super.onSingleTapUp(e);
                        if (!player.getPlayWhenReady()) {
                            privious_player.setPlayWhenReady(true);
                        } else {
                            privious_player.setPlayWhenReady(false);
                        }


                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
//                    Show_video_option(item);

                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {


                        if (!player.getPlayWhenReady()) {
                            privious_player.setPlayWhenReady(true);
                        }

                        if (!animation_running) {
                            if (handler != null && runnable != null) {
                                handler.removeCallbacks(runnable);

                            }
                            runnable = new Runnable() {
                                public void run() {
                                    Show_heart_on_DoubleTap(mediaList.get(currentPage), mainlayout, e);
                                    Like_Video(currentPage, mediaList.get(currentPage));
                                }
                            };
                            handler.postDelayed(runnable, 500);


                        }
                        return super.onDoubleTap(e);

                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }
    }
    public void Show_heart_on_DoubleTap(FeedModel item, final RelativeLayout mainlayout, MotionEvent e) {
        int x = (int) e.getX() - 100;
        int y = (int) e.getY() - 100;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView iv = new ImageView(getApplicationContext());
        lp.setMargins(x, y, 0, 0);
        iv.setLayoutParams(lp);
        if (item.isLike())
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_like));
        else
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_like_fill));

        mainlayout.addView(iv);
        Animation fadeoutani = AnimationUtils.loadAnimation(activity, R.anim.fade_out);

        fadeoutani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // this will call when animation start
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainlayout.removeView(iv);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // this will call when animation start again
            }
        });
        iv.startAnimation(fadeoutani);

    }
    public void Like_Video(final int position, final FeedModel home_get_set) {
        boolean action = home_get_set.isLike();
        if (action) {
            action = false;
            home_get_set.setLikeCount(home_get_set.getLikeCount() - 1);
        } else {
            action = true;
            home_get_set.setLikeCount(home_get_set.getLikeCount() + 1);
        }

        mediaList.get(position).setLike(action);
        adapter.setData(mediaList);

        if (G.isNetworkAvailable(VideoPlayerActivity.this)) {
            apiCallForPPL("CPL", String.valueOf(home_get_set.getId()));
            String token = G.pref.getString("token", "");
            Ion.with(VideoPlayerActivity.this)
                    .load(G.SetFeedLikeUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .setBodyParameter("newsfeed_id", String.valueOf(home_get_set.getId()))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {

                        }
                    });
        }

    }
    public void apiCallForPPL(String type, String ids) {
        String token = G.pref.getString("token", "");
        String url = String.format(java.util.Locale.US, G.FeedPPLUrl, type, ids);
        Ion.with(VideoPlayerActivity.this)
                .load(url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                    }
                });
    }

    private void goBack() {
        if (player != null && player.isPlaying()) {
            player.pause();
            player.release();
        }
        finish();
    }
    @OnClick({R.id.imgBack})
    public void onClickButtons(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                goBack();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}
