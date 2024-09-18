package seemesave.businesshub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;

import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.FollowerModel;
import seemesave.businesshub.utils.DialogUtils;
import seemesave.businesshub.utils.G;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {
    private Context mContext;
    private FollowerRecyclerListener mListener;
    private ArrayList<FollowerModel> mList = new ArrayList<>();

    public void setData(ArrayList<FollowerModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface FollowerRecyclerListener {
        void onItemClicked(int pos, FollowerModel model);
    }

    public FollowerAdapter(Context context, ArrayList<FollowerModel> list, FollowerRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_follower, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgAvatar)
        RoundedImageView imgAvatar;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtType)
        TextView txtType;
        @BindView(R.id.txtEmail)
        TextView txtEmail;
        @BindView(R.id.imgOption)
        ImageView imgOption;
        @BindView(R.id.btnResend)
        LinearLayout btnResend;
        @BindView(R.id.btnStatusActive)
        LinearLayout btnStatusActive;
        @BindView(R.id.btnStatusPending)
        LinearLayout btnStatusPending;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        public void setData(int position) {
            FollowerModel model = mList.get(position);
            if (model.getUser() != null) {
                if (model.getUser().getImage_url() != null) {
                    Glide.with(mContext)
                            .load(model.getUser().getImage_url())
                            .placeholder(mContext.getDrawable(R.drawable.ic_avatar))
                            .into(imgAvatar);
                }
                txtName.setText(model.getUser().getFirst_name() + " " + model.getUser().getLast_name());
                txtType.setText(model.getUser().isIs_trader() ? "Trader" : "Consumer");
            } else {
                txtName.setText(model.getFirst_name());
                txtType.setText("Consumer");
            }
            txtEmail.setText(model.getEmail());
            if (!model.isIs_pending() && !model.isIs_blocked()) {
                btnStatusActive.setVisibility(View.VISIBLE);
                btnStatusPending.setVisibility(View.GONE);
            } else {
                btnStatusActive.setVisibility(View.GONE);
                btnStatusPending.setVisibility(View.VISIBLE);
            }
            btnResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showConfirmDialogWithListener(mContext, mContext.getString(R.string.txt_resend_confirmation), mContext.getString(R.string.txt_resend_invitation), mContext.getString(R.string.txt_yes), mContext.getString(R.string.txt_no),
                            (dialog, which) -> onResend(String.valueOf(model.getId())),
                            (dialog, which) -> dialog.dismiss());
                }
            });
            imgOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showConfirmDialogWithListener(mContext, mContext.getString(R.string.txt_delete_confirmation), mContext.getString(R.string.txt_delete_user), mContext.getString(R.string.txt_yes), mContext.getString(R.string.txt_no),
                            (dialog, which) -> onDelete(String.valueOf(model.getId()), position),
                            (dialog, which) -> dialog.dismiss());
                }
            });
        }

        private void onResend(String id) {
            G.showLoading(mContext);
            Ion.with(mContext)
                    .load("POST", App.getPortalType() ? G.ResendStoreInvitation : G.ResendBrandInvitation)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .setBodyParameter("follow_id", id)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            G.hideLoading();
                            if (e == null) {
                                Toast.makeText(mContext, mContext.getString(R.string.msg_resend_invitation), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

        private void onDelete(String id, int pos) {
            G.showLoading(mContext);
            Ion.with(mContext)
                    .load("DELETE", App.getPortalType() ? G.DeleteStoreInvitation : G.DeleteBrandInvitation)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .setBodyParameter("follow_id", id)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            G.hideLoading();
                            if (e == null) {
                                mList.remove(pos);
                                notifyDataSetChanged();
                            }
                        }
                    });
        }
    }
}
