package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.FriendModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{
    private Context mContext;
    private FriendAdapter.FriendRecyclerListener mListener;
    private ArrayList<FriendModel> mList = new ArrayList<>();

    public void setData(ArrayList<FriendModel> friendList) {
        this.mList.clear();
        this.mList.addAll(friendList);
        notifyDataSetChanged();
    }

    public interface FriendRecyclerListener{
        void onItemClicked(int pos, FriendModel model);
        void onRemove(int pos, FriendModel model);
        void onAccept(int pos, FriendModel model);
        void onDecline(int pos, FriendModel model);
    }

    public FriendAdapter(Context context, ArrayList<FriendModel> list, FriendAdapter.FriendRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_friend, parent, false);
        return new FriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        FriendModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getFriend().getImage_url())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgPhoto);
        holder.txtName.setText(model.getFriend().getFirst_name() + " " + model.getFriend().getLast_name());

        if (model.isIs_pending()){
            if (model.isMaster()) {
                holder.lytAll.setVisibility(View.VISIBLE);
                holder.lytRequest.setVisibility(View.GONE);
            } else {
                holder.lytAll.setVisibility(View.GONE);
                holder.lytRequest.setVisibility(View.VISIBLE);
            }
            holder.txtStatus.setVisibility(View.VISIBLE);
        }else{
            holder.txtStatus.setVisibility(View.GONE);
            holder.lytAll.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
        holder.btnRemove.setOnClickListener(v -> {
            mListener.onRemove(holder.getLayoutPosition(), model);
        });
        holder.btnAccept.setOnClickListener(v -> {
            mListener.onAccept(holder.getLayoutPosition(), model);
        });
        holder.btnDecline.setOnClickListener(v -> {
            mListener.onDecline(holder.getLayoutPosition(), model);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView imgPhoto;
        private TextView txtName;
        private TextView txtStatus;
        private LinearLayout lytAll, lytRequest, btnAccept, btnRemove, btnDecline;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtName = itemView.findViewById(R.id.txtName);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            lytAll = itemView.findViewById(R.id.lytAll);
            lytRequest = itemView.findViewById(R.id.lytRequest);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);

        }
    }
}
