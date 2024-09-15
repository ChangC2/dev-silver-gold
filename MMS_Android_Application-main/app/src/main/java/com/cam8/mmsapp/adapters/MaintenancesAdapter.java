package com.cam8.mmsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cam8.mmsapp.R;
import com.cam8.mmsapp.model.Maintenance;

import java.util.ArrayList;


public class MaintenancesAdapter extends RecyclerView.Adapter<MaintenancesAdapter.ItemViewHolder> implements View.OnClickListener {

    private Context context;
    private long selectedItem = 0;

    MaintenanceItemListener listener;
    private ArrayList<Maintenance> dataList;
    private String TAG = MaintenancesAdapter.class.getSimpleName();

    public MaintenancesAdapter(Context context, ArrayList<Maintenance> dataList, MaintenanceItemListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
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
        public TextView tvName;
        public View viewMarker;

        public ItemViewHolder(View itemView) {
            super(itemView);
            panelBack = itemView.findViewById(R.id.panelBack);
            tvName = itemView.findViewById(R.id.tvName);
            viewMarker = itemView.findViewById(R.id.viewMarker);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_maintenances,parent,false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.tvName.setText(dataList.get(position).getTaskName());

        if (selectedItem == position) {
            holder.viewMarker.setVisibility(View.VISIBLE);
        } else {
            holder.viewMarker.setVisibility(View.GONE);
        }

        holder.panelBack.setTag(position);
        holder.panelBack.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface MaintenanceItemListener {
        void onClick(View view, int position);
    }
}