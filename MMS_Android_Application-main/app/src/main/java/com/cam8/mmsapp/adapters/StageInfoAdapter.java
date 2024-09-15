package com.cam8.mmsapp.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cam8.mmsapp.R;
import com.cam8.mmsapp.model.FaxonStageBase;

public class StageInfoAdapter extends RecyclerView.Adapter<StageInfoAdapter.ItemViewHolder> implements View.OnClickListener {

    private Context context;
    private long selectedItem = 0;

    StageInfoItemListener listener;
    private FaxonStageBase stageInfo;
    private String TAG = StageInfoAdapter.class.getSimpleName();

    public StageInfoAdapter(Context context, FaxonStageBase stageInfo, StageInfoItemListener listener) {
        this.context = context;
        this.stageInfo = stageInfo;
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

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout panelBack;
        public TextView tvName;
        public TextView tvRange;
        public EditText tvResult;

        public MutableWatcher textWatcher;

        public ItemViewHolder(View itemView) {
            super(itemView);
            panelBack = itemView.findViewById(R.id.panelBack);
            tvName = itemView.findViewById(R.id.tvName);
            tvRange = itemView.findViewById(R.id.tvRange);
            tvResult = itemView.findViewById(R.id.tvResult);

            textWatcher = new MutableWatcher();
            tvResult.addTextChangedListener(textWatcher);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stageinput,parent,false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.tvName.setText(stageInfo.getItemsList().get(position).getName());
        holder.tvRange.setText(stageInfo.getItemsList().get(position).getRange());


        holder.textWatcher.setActive(false);
        holder.textWatcher.setPosition(position);
        holder.tvResult.setText(stageInfo.getItemsList().get(position).getResult());
        holder.textWatcher.setActive(true);
    }

    @Override
    public int getItemCount() {
        return stageInfo.getItemsList().size();
    }

    public interface StageInfoItemListener {
        void onClick(View view, int position);
    }

    class MutableWatcher implements TextWatcher {

        private int mPosition;
        private boolean mActive = true;

        void setPosition(int position) {
            mPosition = position;
        }

        void setActive(boolean active) {
            mActive = active;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if (mActive) {
                stageInfo.getItemsList().get(mPosition).setResult(s.toString().trim());
            }
        }
    }
}