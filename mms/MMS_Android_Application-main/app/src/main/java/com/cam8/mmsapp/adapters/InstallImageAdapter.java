package com.cam8.mmsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cam8.mmsapp.ImageActivity;
import com.cam8.mmsapp.R;
import com.cam8.mmsapp.model.ImageInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class InstallImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public interface OnImageListener {
        void onAddImage();

        void onRemoveImage(int position);
    }

    private List<ImageInfo> mDataset;
    Context context;
    OnImageListener imageListener;

    // Image Loader
    ImageLoader mImageLoader;
    DisplayImageOptions mImageOptions;

    private static final int TYPE_ADD = 0;
    private static final int TYPE_IMAGE = 1;

    public class AddViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView imageAdd;

        public AddViewHolder(View v) {
            super(v);
            imageAdd = (ImageView) v.findViewById(R.id.ic_add);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView imageView;
        ImageView imageDel;

        public ImageViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.ic_img);
            imageDel = (ImageView) v.findViewById(R.id.ic_del);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ADD;
        } else {
            return TYPE_IMAGE;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 1;
        } else {
            return mDataset.size() + 1;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public InstallImageAdapter(Context context, List<ImageInfo> myDataset, OnImageListener imageListener) {
        this.mDataset = myDataset;
        this.context = context;
        this.imageListener = imageListener;

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(this.context.getApplicationContext()));
        mImageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .showImageOnFail(R.drawable.ic_placeholder)
                .showImageOnLoading(R.drawable.ic_placeholder)
                .showImageForEmptyUri(R.drawable.ic_placeholder)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD) {
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_img_layout, parent, false);
            AddViewHolder vh = new AddViewHolder(v);
            return vh;
        } else {
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_img_layout, parent, false);
            ImageViewHolder vh = new ImageViewHolder(v);
            return vh;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (position == 0) {
            AddViewHolder addViewHolder = (AddViewHolder) holder;
            addViewHolder.imageAdd.setOnClickListener(view -> imageListener.onAddImage());
        } else {
            ImageInfo imageInfo = mDataset.get(position - 1);
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            mImageLoader.displayImage(imageInfo.getUrlPath(), imageViewHolder.imageView, mImageOptions);
            imageViewHolder.imageView.setTag(position - 1);
            imageViewHolder.imageView.setOnClickListener(this);

            imageViewHolder.imageDel.setTag(position - 1);
            imageViewHolder.imageDel.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        final int position = (int) view.getTag();

        if (id == R.id.ic_img) {
            // Show Image
            ImageInfo imgInfo = mDataset.get(position);
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("img_url", imgInfo.getUrlPath());
            context.startActivity(intent);
        } else if (id == R.id.ic_del) {
            // Remove image
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            alertDialogBuilder.setTitle("Please confirm");
            alertDialogBuilder.setMessage("Would you like to remove?")
                    .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                    imageListener.onRemoveImage(position);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}

