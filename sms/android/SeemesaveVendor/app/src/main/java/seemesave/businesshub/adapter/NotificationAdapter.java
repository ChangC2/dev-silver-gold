package seemesave.businesshub.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.NotificationModel;
import seemesave.businesshub.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
    private Context mContext;
    private NotificationRecyclerListener mListener;
    private ArrayList<NotificationModel> mList = new ArrayList<>();

    public void setData(ArrayList<NotificationModel> notificationList) {
        this.mList.clear();
        this.mList.addAll(notificationList);
        notifyDataSetChanged();
    }

    public interface NotificationRecyclerListener{
        void onItemClicked(int pos, NotificationModel model);
        void onOption(int pos, NotificationModel model);
    }

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list, NotificationRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_notfication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel model = mList.get(holder.getLayoutPosition());

        Glide.with(mContext)
                .load(model.getSenderAvatar())
                .fitCenter()
                .placeholder(R.drawable.ic_avatar)
                .into(holder.imgPhoto);
        holder.txtName.setText(model.getSenderName());
        holder.txtDesc.setText(model.getNotificationMessage());
        String subDateStr = model.getCreated().substring(0, 19).replace("T", " ");
        Date createDate = TimeUtils.parseDataFromFormat12(subDateStr);
        holder.txtTime.setText(TimeUtils.toStringFormat_7(createDate));

        if (model.isIs_read()){
            holder.imgReadFlag.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_read));
//            holder.txtDesc.setTypeface(null, Typeface.BOLD);
//            holder.txtDesc.setTextColor(ContextCompat.getColor(mContext,R.color.md_grey_700));
            holder.lytNotification.setBackgroundColor(ContextCompat.getColor(mContext,R.color.blue_semi_color));
        }else{
            holder.imgReadFlag.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_unread));
//            holder.txtDesc.setTypeface(null, Typeface.NORMAL);
//            holder.txtDesc.setTextColor(ContextCompat.getColor(mContext,R.color.md_grey_600));
            holder.lytNotification.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white_color));
        }
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });

        holder.imgOption.setOnClickListener(v -> {
            mListener.onOption(holder.getLayoutPosition(), model);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;
        @BindView(R.id.imgReadFlag)
        ImageView imgReadFlag;
        @BindView(R.id.imgOption)
        ImageView imgOption;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtDesc)
        TextView txtDesc;
        @BindView(R.id.txtTime)
        TextView txtTime;
        @BindView(R.id.lytNotification)
        LinearLayout lytNotification;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
