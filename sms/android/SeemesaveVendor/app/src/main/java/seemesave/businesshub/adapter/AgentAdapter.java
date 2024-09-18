package seemesave.businesshub.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.makeramen.roundedimageview.RoundedImageView;
import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.AgentModel;
import seemesave.businesshub.utils.DialogUtils;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.widget.ActionSheet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.ViewHolder>{
    private Context mContext;
    private AgentRecyclerListener mListener;
    private ArrayList<AgentModel> mList = new ArrayList<>();

    public void setData(ArrayList<AgentModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface AgentRecyclerListener{
        void onItemClicked(int pos, AgentModel model);
    }

    public AgentAdapter(Context context, ArrayList<AgentModel> list, AgentRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_agent, parent, false);
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
        @BindView(R.id.imgAvatar)
        RoundedImageView imgAvatar;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtTime)
        TextView txtTime;
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
            AgentModel model = mList.get(position);
            if (model.getUser() != null) {
                if (model.getUser().getImage_url() != null) {
                    Glide.with(mContext)
                            .load(model.getUser().getImage_url())
                            .placeholder(mContext.getDrawable(R.drawable.ic_avatar))
                            .into(imgAvatar);
                }
                txtName.setText(model.getUser().getFirst_name() + " " + model.getUser().getLast_name());
                txtEmail.setText(model.getUser().getEmail());
            }
            txtType.setText(model.getRole());
            txtTime.setText(model.getCreated());
            if (model.isPending()) {
                btnStatusActive.setVisibility(View.GONE);
                btnStatusPending.setVisibility(View.VISIBLE);
            } else {
                btnStatusActive.setVisibility(View.VISIBLE);
                btnStatusPending.setVisibility(View.GONE);
            }
            imgOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
            btnResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showConfirmDialogWithListener(mContext, mContext.getString(R.string.txt_resend_confirmation), mContext.getString(R.string.txt_resend_invitation), mContext.getString(R.string.txt_yes), mContext.getString(R.string.txt_no),
                            (dialog, which) -> onResend(String.valueOf(model.getId())),
                            (dialog, which) -> dialog.dismiss());
                }
            });
//            imgOption.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DialogUtils.showConfirmDialogWithListener(mContext, mContext.getString(R.string.txt_delete_confirmation), mContext.getString(R.string.txt_delete_user), mContext.getString(R.string.txt_yes), mContext.getString(R.string.txt_no),
//                            (dialog, which) -> onDelete(String.valueOf(model.getId()), position),
//                            (dialog, which) -> dialog.dismiss());
//                }
//            });
        }

        private void onResend(String id) {
            G.showLoading(mContext);
            Ion.with(mContext)
                    .load(G.ResendAgentInvitation)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .setBodyParameter("id", id)
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
//        private void onDelete(String id, int pos) {
//            G.showLoading(mContext);
//            Ion.with(mContext)
//                    .load(G.DeleteAgentInvitation)
//                    .addHeader("Authorization", App.getToken())
//                    .addHeader("Content-Language", App.getPortalToken())
//                    .setBodyParameter("follow_id", id)
//                    .asString()
//                    .setCallback(new FutureCallback<String>() {
//                        @Override
//                        public void onCompleted(Exception e, String result) {
//                            G.hideLoading();
//                            if (e == null) {
//                                mList.remove(pos);
//                                notifyDataSetChanged();
//                            }
//                        }
//                    });
//        }
    }
}
