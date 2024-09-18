package seemesave.businesshub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.listener.CommentClickListener;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.MCommon;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.utils.TimeUtils;
import seemesave.businesshub.view.vendor.detail.StoreCommentReplyActivity;

public class StoreCommentAdapter extends RecyclerView.Adapter<StoreCommentAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<MCommon> datas;
    private RecyclerClickListener listener;

    public StoreCommentAdapter(Context context, ArrayList<MCommon> pDatas, RecyclerClickListener pListener) {
        datas = pDatas;
        listener = pListener;
        mContext = context;
    }

    public void setDatas(ArrayList<MCommon> pDatas){
        datas = pDatas;
        notifyDataSetChanged();
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtDesc)
        TextView txtDesc;
        @BindView(R.id.txtTime)
        TextView txtTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            Glide.with(mContext)
                    .load(datas.get(position).getImageUrl())
                    .fitCenter()
                    .placeholder(R.drawable.ic_avatar)
                    .into(imgPhoto);
            txtName.setText(datas.get(position).getName());
            txtDesc.setText(datas.get(position).getDescription());
            try {
                String dateStr = datas.get(position).getCreated();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = df.parse(dateStr);
                df.setTimeZone(TimeZone.getDefault());
                String formattedDate = df.format(date);

                String timeStr = TimeUtils.getTimeAgo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDate).getTime());
                txtTime.setText(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
                txtTime.setText("Just now");
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentClickListener likeClickListener = new CommentClickListener() {
                        @Override
                        public void onClick(String json) {
                            Gson gson  = new Gson();
                            MCommon reply = gson.fromJson(json,  MCommon.class);
                            datas.get(position).getSubItems().add(reply);
                        }
                    };
                    G.commentClickListener = likeClickListener;
                    listener.onClick(view, position);
                }
            });
        }
    }
    // ******************************class ViewHoler redefinition ***************************//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_store_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}