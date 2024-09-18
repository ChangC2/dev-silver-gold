package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.FeedModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class SpecialDealAdapter extends RecyclerView.Adapter<SpecialDealAdapter.ViewHolder>{
    private Context mContext;
    private SpecialDealRecyclerListener mListener;
    private ArrayList<FeedModel> mList = new ArrayList<>();

    public void setData(ArrayList<FeedModel> specialDealList) {
        this.mList.clear();
        this.mList.addAll(specialDealList);
        notifyDataSetChanged();
    }

    public interface SpecialDealRecyclerListener{
        void onItemClicked(int pos, FeedModel model);
    }

    public SpecialDealAdapter(Context context, ArrayList<FeedModel> list, SpecialDealRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_promotion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedModel model = mList.get(holder.getLayoutPosition());

        if (model.getSubMedia().size() > 0) {
            Glide.with(mContext)
                    .load(model.getSubMedia().get(0).getMedia())
                    .fitCenter()
                    .placeholder(R.drawable.ic_me)
                    .into(holder.imgItem);
        }
        Glide.with(mContext)
                .load(model.getAvatar())
                .fitCenter()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgLogo);
        holder.txtName.setText(model.getName());
        holder.txtTime.setText(String.format("%s ~ %s", model.getStart_date(), model.getEnd_date()));
        holder.btnViewDetails.setOnClickListener(v->{
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgItem;
        private TextView txtName;
        private TextView txtTime;
        private Button btnViewDetails;
        private CircleImageView imgLogo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }
}
