package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.OrderHistoryModel;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>{
    private Context mContext;
    private OrderHistoryAdapter.OrderHistoryRecyclerListener mListener;
    private ArrayList<OrderHistoryModel> mList = new ArrayList<>();

    public interface OrderHistoryRecyclerListener{
        void onItemClicked(int pos, OrderHistoryModel model);
    }

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistoryModel> list, OrderHistoryAdapter.OrderHistoryRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_order_history, parent, false);
        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        OrderHistoryModel model = mList.get(holder.getLayoutPosition());
        holder.txtId.setText(model.getId());
        holder.txtTime.setText(model.getTime());
        holder.imgGo.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtId;
        private TextView txtTime;
        private ImageView imgGo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtId);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgGo = itemView.findViewById(R.id.imgGo);

        }
    }
}
