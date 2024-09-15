package com.cam8.mmsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cam8.mmsapp.adapters.JobFilesAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class ViewAttachesActivity extends BaseActivity implements JobFilesAdapter.RecyclerViewClickListener, DownloadFile.Listener {

    private String[] guideFileArray;

    TextView tvFileName;
    TextView tvPageInfo;

    RecyclerView recyclerView;
    JobFilesAdapter filesAdapter;

    FrameLayout root;
    RemotePDFViewPager remotePDFViewPager;
    PDFPagerAdapter adapter;

    ImageView ivPicture;

    ImageLoader imageLoader;
    DisplayImageOptions imageOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_attaches);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.title_view_attaches);

        Intent intent = getIntent();
        String guides = intent.getStringExtra("files");
        if (!TextUtils.isEmpty(guides)) {
            //guideFileArray = guides.split("[,:]+");
            guideFileArray = guides.split(":");
        }

        if (guideFileArray == null || guideFileArray.length == 0) {
            finish();
            return;
        }

        tvFileName = findViewById(R.id.tvFileName);
        tvPageInfo = findViewById(R.id.tvPageInfo);

        // Get Content root panel
        root = findViewById(R.id.root);

        ivPicture = findViewById(R.id.ivPicture);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        imageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnLoading(R.drawable.ic_placeholder)
                .showImageOnFail(R.drawable.ic_placeholder)
                .showImageForEmptyUri(R.drawable.ic_placeholder)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        // PDF Files list
        recyclerView = findViewById(R.id.rcvFiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        filesAdapter = new JobFilesAdapter(mContext, guideFileArray, this);
        recyclerView.setAdapter(filesAdapter);

        showFileInfo(0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view, int position) {
        showFileInfo(position);
    }

    private void showFileInfo(int position) {

        //https://slymms.com/backend/files/I-65BG6KWJTP57.pdf
        //https://github.com/voghDev/PdfViewPager/blob/master/sample/src/main/java/es/voghdev/pdfviewpager/RemotePDFActivity.java

        String fileName = guideFileArray[position].trim();
        if (TextUtils.isEmpty(fileName)) {
            showToastMessage("The file is Invalid!");
            return;
        }

        // Check valid URL
        String url = fileName;
        if (!url.toLowerCase().startsWith("http")) {
            url = "https://slymms.com/backend/files/" + url;
        }

        if (fileName.toLowerCase().endsWith("pdf")) {

            remotePDFViewPager = new RemotePDFViewPager(mContext, url, this);
            remotePDFViewPager.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    return false;
                }
            });
            remotePDFViewPager.setId(R.id.pdfViewPager);

            showProgressDialog();
        } else {

            if (remotePDFViewPager != null) {
                remotePDFViewPager.setVisibility(View.GONE);
            }

            // Show Image
            imageLoader.displayImage(url, ivPicture, imageOptions);
            tvPageInfo.setText("1/1");
        }
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        hideProgressDialog();

        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        remotePDFViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvPageInfo.setText(String.format("%d/%d", position + 1, adapter.getCount()));
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        updateLayout();
    }

    @Override
    public void onFailure(Exception e) {
        hideProgressDialog();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
    }

    public void updateLayout() {
        root.removeAllViewsInLayout();

        root.addView(ivPicture,
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        root.addView(remotePDFViewPager,
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }
}
