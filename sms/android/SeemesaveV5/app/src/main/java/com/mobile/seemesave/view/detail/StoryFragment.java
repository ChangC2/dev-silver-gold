package com.mobile.seemesave.view.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.DiscoverModel;
import com.mobile.seemesave.view.seeall.AllStoryBrandActivity;
import com.mobile.seemesave.view.seeall.AllStoryStoreActivity;
import com.mobile.seemesave.widget.ResizableImageView;
import com.mobile.seemesave.widget.ResizableWImageView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class StoryFragment extends Fragment {
    public DiscoverModel story;
    StyledPlayerView videoStoryItem;
    ImageView imageStoryItem;
    LinearLayout lytProgress;
    TextView txtStoreName;
//    ImageView imgPause;
    ImageView imgStore;
    LinearLayout lytMainSlide;
    Button btnPrev;
    Button btnNext;

    public StoryPlayerActivity activity;
    ArrayList<ProgressBar> pBar = new ArrayList<>();

    SimpleExoPlayer player;
    int currentIndex = 0;

    private final Handler mHandler = new Handler();
    private Timer mTimer = null;

    private boolean isPlay = true;
    private GestureDetector mPrevDetector, mNextDetector;
    // newInstance constructor for creating fragment with arguments
    public static StoryFragment newInstance() {
        StoryFragment fragmentFirst = new StoryFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        videoStoryItem = view.findViewById(R.id.videoStoryItem);
        imageStoryItem = view.findViewById(R.id.imgStoryItem);
        txtStoreName = view.findViewById(R.id.txtStoreName);
        imgStore = view.findViewById(R.id.imgStore);
//        imgPause = view.findViewById(R.id.imgPause);
        lytMainSlide = view.findViewById(R.id.lytMainSlide);
        btnPrev = view.findViewById(R.id.btnPrev);
        btnNext = view.findViewById(R.id.btnNext);

        txtStoreName.setText(story.getName());
        Glide.with(activity)
                .load(story.getLogo())
                .fitCenter()
                .into(imgStore);

        mPrevDetector = new GestureDetector(getActivity(), new PrevMyGestureListener());
        mNextDetector = new GestureDetector(getActivity(), new NextMyGestureListener());
        btnPrev.setOnTouchListener(prevTouchListener);
        btnNext.setOnTouchListener(nextTouchListener);

        imgStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (story.getStory_type().equalsIgnoreCase("VendorStory")) {
                    intent = new Intent(activity, AllStoryStoreActivity.class);
                    intent.putExtra("id", story.getStories().get(0).getId());
                } else {
                    intent = new Intent(activity, AllStoryBrandActivity.class);
                    intent.putExtra("id", story.getStories().get(0).getId());
                }
                startActivity(intent);
            }
        });

        loadProgressBars(view);

        player = new SimpleExoPlayer.Builder(activity).build();
        player.setRepeatMode(Player.REPEAT_MODE_OFF);
        videoStoryItem.setPlayer(player);
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        mTimer = new Timer();
        mTimer.schedule(new StoryPlayTask(), 3, 50);
//        imgPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isPlay) {
//                    mTimer.cancel();
//                    imgPause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_play_story));
//                    if (currentIndex < story.getStories().size() && story.getStories().get(currentIndex).getMedia_Type().equalsIgnoreCase("video")) {
//                        player.pause();
//                    }
//                } else {
//                    mTimer = new Timer();
//                    mTimer.schedule(new StoryPlayTask(), 3, 50);
//                    imgPause.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_pause_story));
//                    if (currentIndex < story.getStories().size() && story.getStories().get(currentIndex).getMedia_Type().equalsIgnoreCase("video")) {
//                        player.play();
//                    }
//                }
//                isPlay = !isPlay;
//
//            }
//        });
        return view;
    }

    private void pauseStory() {
        if (isPlay) {
            mTimer.cancel();
            if (currentIndex < story.getStories().size() && story.getStories().get(currentIndex).getMedia_Type().equalsIgnoreCase("video")) {
                player.pause();
            }
        } else {
            mTimer = new Timer();
            mTimer.schedule(new StoryPlayTask(), 3, 50);
            if (currentIndex < story.getStories().size() && story.getStories().get(currentIndex).getMedia_Type().equalsIgnoreCase("video")) {
                player.play();
            }
        }
        isPlay = !isPlay;
    }
    View.OnTouchListener prevTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // pass the events to the gesture detector
            // a return value of true means the detector is handling it
            // a return value of false means the detector didn't
            // recognize the event
            return mPrevDetector.onTouchEvent(event);

        }
    };
    View.OnTouchListener nextTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // pass the events to the gesture detector
            // a return value of true means the detector is handling it
            // a return value of false means the detector didn't
            // recognize the event
            return mNextDetector.onTouchEvent(event);

        }
    };
    @Override
    public void onResume() {
        super.onResume();

        setCurrentStory();
    }

    @SuppressLint("ClickableViewAccessibility")
    void loadProgressBars(View view){
        lytProgress = view.findViewById(R.id.lytProgress);
        for (int i= 0; i<story.getStories().size();i++){
            ProgressBar progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 5);
            params.weight = 1.0f;
            params.setMarginStart(10);
            progressBar.setMax(100);
            progressBar.setLayoutParams(params);
            progressBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            lytProgress.addView(progressBar);
            pBar.add(progressBar);
        }

//        lytProgress.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    int x = (int) event.getX();
//                    for (int i=0;i<pBar.size();i++){
//                        if (x>lytProgress.getWidth()*i/pBar.size() && x < lytProgress.getWidth()*(i+1)/pBar.size()){
//                            currentIndex = i;
//                            setCurrentStory();
//                        }
//                    }
//                }
//                return true;
//            }
//        });
    }

    void setCurrentStory(){
        if (activity.isDestroyed())
            return;
        for (int i=0;i<currentIndex;i++){
            pBar.get(i).setProgress(100);
        }

        for (int i=currentIndex;i<pBar.size();i++){
            pBar.get(i).setProgress(0);
        }

        if (currentIndex < story.getStories().size() && story.getStories().get(currentIndex).getMedia_Type().equalsIgnoreCase("video")){
            videoStoryItem.setVisibility(View.VISIBLE);
            imageStoryItem.setVisibility(View.GONE);

            Uri mp4VideoUri = Uri.parse(story.getStories().get(currentIndex).getMedia());
            player.setMediaItem(MediaItem.fromUri(mp4VideoUri));
            player.prepare();
            player.play();
        }else if (currentIndex < story.getStories().size() && story.getStories().get(currentIndex).getMedia_Type().equalsIgnoreCase("image")){
            videoStoryItem.setVisibility(View.GONE);
            imageStoryItem.setVisibility(View.VISIBLE);
            Glide.with(activity)
                    .load(story.getStories().get(currentIndex).getMedia())
                    .into(imageStoryItem);
        }
    }


    @Override
    public void onDestroy() {
        releaseMusic();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        try{
            player.pause();
        }catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            player.play();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseMusic() {
        try{
            player.stop();
            player.release();
            mHandler.removeCallbacks(runnable);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (currentIndex < story.getStories().size() && story.getStories().get(currentIndex).getMedia_Type().equalsIgnoreCase("video")){
                int timeMs = (int) player.getDuration();
                if (player.getCurrentPosition() < timeMs){
                    pBar.get(currentIndex).setProgress((int) (100 * player.getCurrentPosition()/timeMs));
                }else{
                    if (currentIndex < story.getStories().size()-1){
                        currentIndex++;
                        setCurrentStory();
                    }else{

                    }
                }
            }else if (currentIndex < story.getStories().size() && story.getStories().get(currentIndex).getMedia_Type().equalsIgnoreCase("image")){
                if (pBar.get(currentIndex).getProgress() < 100)
                    pBar.get(currentIndex).setProgress(pBar.get(currentIndex).getProgress() + 1);
                else{
                    if (currentIndex < story.getStories().size()-1){
                        currentIndex++;
                        setCurrentStory();
                    }else{

                    }
                }
            }
        }
    };

    private class StoryPlayTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(runnable);
        }
    }

    class PrevMyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("STORYTAG","onDown: ");
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (currentIndex > 0){
                currentIndex--;
                setCurrentStory();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            pauseStory();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("STORYTAG", "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (distanceY < -20) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.slide_out_down);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("STORYTAG", "onFling: ");
            return true;
        }
    }
    class NextMyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("STORYTAG","onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (currentIndex < story.getStories().size()-1){
                currentIndex++;
                setCurrentStory();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            pauseStory();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("STORYTAG", "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (distanceY < -20) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.slide_out_down);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("STORYTAG", "onFling: ");
            return true;
        }
    }
}
