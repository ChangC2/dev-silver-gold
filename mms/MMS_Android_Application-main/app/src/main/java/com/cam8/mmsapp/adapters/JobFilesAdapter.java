package com.cam8.mmsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cam8.mmsapp.R;


public class JobFilesAdapter extends RecyclerView.Adapter<JobFilesAdapter.ItemViewHolder> implements View.OnClickListener {

    private Context context;
    private long selectedItem = 0;

    RecyclerViewClickListener listener;
    private String[] fileNames = new String[] {};
    private String TAG = JobFilesAdapter.class.getSimpleName();


    public JobFilesAdapter(Context context, String[] files, RecyclerViewClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.fileNames = files;
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        if (listener != null) {
            listener.onClick(view, position);
        }

        // If changed selected item, then notify
        if (selectedItem != position) {
            selectedItem = position;
            notifyDataSetChanged();
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout panelBack;
        public TextView tvFileName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            panelBack = itemView.findViewById(R.id.panelBack);
            tvFileName = itemView.findViewById(R.id.tvFileName);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_files,parent,false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.tvFileName.setText(fileNames[position]);

        if (selectedItem == position) {
            holder.panelBack.setBackgroundColor(ContextCompat.getColor(context, R.color.white_trans50));
        } else {
            holder.panelBack.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent_color));
        }

        holder.panelBack.setTag(position);
        holder.panelBack.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return fileNames.length;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}