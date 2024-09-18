package seemesave.businesshub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import seemesave.businesshub.R;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.PostModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdsPostAdapter extends RecyclerView.Adapter<AdsPostAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<PostModel> mList = new ArrayList<>();
    private RecyclerClickListener listener;
    private String feed_type = "";

    public AdsPostAdapter(Context context, ArrayList<PostModel> list, String type, RecyclerClickListener pListener) {
        this.mList.clear();
        this.mList.addAll(list);
        this.listener = pListener;
        this.mContext = context;
        this.feed_type = type;
    }

    public void setData(ArrayList<PostModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ckStatus)
        CheckBox ckStatus;

        @BindView(R.id.swSelect)
        SwitchCompat swSelect;

        @BindView(R.id.txtBudget)
        TextView txtBudget;

        @BindView(R.id.txtSpent)
        TextView txtSpent;

        @BindView(R.id.imgCnt)
        ImageView imgCnt;

        @BindView(R.id.txtCntLabel)
        TextView txtCntLabel;

        @BindView(R.id.txtCnt)
        TextView txtCnt;

        @BindView(R.id.txtTitle)
        TextView txtTitle;

        @BindView(R.id.txtPeriod)
        TextView txtPeriod;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void setData(int position) {
            PostModel model = mList.get(position);
            ckStatus.setChecked(model.isCheck());
            swSelect.setChecked(model.isActive());
            if (model.getPaymentInfo() != null) {
                txtBudget.setText(String.format(java.util.Locale.US,"%s %.2f", model.getPaymentInfo().getCurrency().getSimple(), model.getPaymentInfo().getTotal_amount()));
                txtSpent.setText(String.format(java.util.Locale.US,"%s %.2f", model.getPaymentInfo().getCurrency().getSimple(), model.getPaymentInfo().getTotal_amount() - model.getPaymentInfo().getRemain_amount()));
            }
            if (feed_type.equalsIgnoreCase("Story")) {
                imgCnt.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_ads_eye));
                txtCntLabel.setText(mContext.getString(R.string.views));
                txtCnt.setText(String.valueOf(model.getViewCount()));
                txtPeriod.setText(model.getCreated());
            } else {
                imgCnt.getLayoutParams().height = 60;
                imgCnt.getLayoutParams().width = 60;
                imgCnt.requestLayout();
                imgCnt.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_bank_ac_name_blue));
                txtCntLabel.setText(mContext.getString(R.string.txt_created_by));
                txtCnt.setText(String.format("%1$s %2$s", model.getCreator().getFirst_name(), model.getCreator().getLast_name()));
                txtPeriod.setText(String.format(java.util.Locale.US, "%s ~ %s", model.getStart_date(), model.getEnd_date()));
            }

            txtTitle.setText(model.getTitle());

            ckStatus.setOnClickListener(v -> {
                mList.get(position).setCheck(!mList.get(position).isCheck());
                notifyItemChanged(position);
                listener.onClick(v, position);
            });
            swSelect.setOnClickListener(v -> {
                mList.get(position).setActive(!mList.get(position).isActive());
                notifyItemChanged(position);
                listener.onClick(v, position, 1);
            });
            swSelect.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return event.getActionMasked() == MotionEvent.ACTION_MOVE;
                }
            });
        }
    }
    // ******************************class ViewHoler redefinition ***************************//
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_ads_promotion, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}