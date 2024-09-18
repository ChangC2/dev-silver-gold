package seemesave.businesshub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.PromoteReceiverModel;
import seemesave.businesshub.utils.DialogUtils;
import seemesave.businesshub.utils.G;

public class PromoteStoreAdapter extends RecyclerView.Adapter<PromoteStoreAdapter.ViewHolder>{
    private Context mContext;
    private PromoteStoreRecyclerListener mListener;
    private ArrayList<PromoteReceiverModel> mList = new ArrayList<>();
    private boolean status_flag;

    public void setData(ArrayList<PromoteReceiverModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface PromoteStoreRecyclerListener{
        void onItemClicked(int pos, PromoteReceiverModel model);
    }

    public PromoteStoreAdapter(Context context, ArrayList<PromoteReceiverModel> list, boolean status_flag, PromoteStoreRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
        this.status_flag = status_flag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_promote_store, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ckStatus)
        CheckBox ckStatus;
        @BindView(R.id.imgAvatar)
        RoundedImageView imgAvatar;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtAddress)
        TextView txtAddress;
        @BindView(R.id.txtCurrency)
        TextView txtCurrency;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @SuppressLint("SetTextI18n")
        public void setData(int position) {
            PromoteReceiverModel model = mList.get(position);
            if (model.getStore() != null) {
                if (model.getStore().getLogo() != null) {
                    Glide.with(mContext)
                            .load(model.getStore().getLogo())
                            .placeholder(mContext.getDrawable(R.drawable.ic_placeholder))
                            .into(imgAvatar);
                }
                txtName.setText(model.getStore().getName());
                txtAddress.setText(model.getStore().getAddress());
                txtCurrency.setText(model.getStore().getCurrency().getIso());
                if (status_flag) {
                    ckStatus.setVisibility(View.VISIBLE);
                    if (model.getId() == -1) {
                        ckStatus.setEnabled(true);

                    } else {
                        ckStatus.setChecked(true);
                        ckStatus.setEnabled(false);
                    }
                } else {
                    ckStatus.setVisibility(View.GONE);
                }
            }
            ckStatus.setChecked(model.isCheck());
            ckStatus.setOnClickListener(v -> {
                mList.get(position).setCheck(!mList.get(position).isCheck());
                notifyItemChanged(position);
            });
        }
    }
}
