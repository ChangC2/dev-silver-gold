package seemesave.businesshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.MCommon;

public class StoreCommentReplyAdapter  extends RecyclerView.Adapter<StoreCommentReplyAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<MCommon> datas;
    private RecyclerClickListener listener;

    public StoreCommentReplyAdapter(Context context, ArrayList<MCommon> pDatas, RecyclerClickListener pListener) {
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
    // ******************************class ViewHoler redefinition ***************************//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_store_comment_reply, parent, false));
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