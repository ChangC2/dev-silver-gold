package com.cam8.mmsapp.adapters;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cam8.mmsapp.R;
import com.cam8.mmsapp.model.MJob;
import com.cam8.mmsapp.utils.ClickListener;

import java.util.ArrayList;


public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ItemViewHolder> implements View.OnClickListener {

    private Context context;
    private long selectedItem = 0;

    ClickListener listener;
    private ArrayList<MJob> jobs = new ArrayList<>();


    public JobListAdapter(Context context, ArrayList<MJob> jobs, ClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.jobs = jobs;
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        if (listener != null) {
            listener.onClick(position);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public TextView tvJobDetails;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvJobDetails = itemView.findViewById(R.id.tvJobDetails);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_job_list,parent,false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        StringBuilder strJobDetailsBuilder = new StringBuilder();
        strJobDetailsBuilder.append("Job ID : <b>" + jobs.get(position).getJobID() + "</b><br>");
        strJobDetailsBuilder.append("Seq No : <b>" + jobs.get(position).getSeq_no() + "</b><br>");
        strJobDetailsBuilder.append("Processing Center : <b>" + jobs.get(position).getShort_desc() + "</b><br>");
        if (!TextUtils.isEmpty(jobs.get(position).getBom_item()) && !TextUtils.isEmpty(jobs.get(position).getBom_item_no())){
            strJobDetailsBuilder.append("BOM Item : <b>" + jobs.get(position).getBom_item() + ", " + jobs.get(position).getBom_item_no() + "</b><br>");
        }
        holder.tvJobDetails.setText(Html.fromHtml(strJobDetailsBuilder.toString()));
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}