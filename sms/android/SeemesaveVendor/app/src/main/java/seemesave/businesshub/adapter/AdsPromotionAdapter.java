package seemesave.businesshub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import seemesave.businesshub.R;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.AdsModel;
import seemesave.businesshub.model.common.ProductModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdsPromotionAdapter extends RecyclerView.Adapter<AdsPromotionAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<AdsModel> mList = new ArrayList<>();
    private RecyclerClickListener listener;
    private String feed_type = "";

    public AdsPromotionAdapter(Context context, ArrayList<AdsModel> list, String type, RecyclerClickListener pListener) {
        this.mList.clear();
        this.mList.addAll(list);
        listener = pListener;
        mContext = context;
        this.feed_type = type;
    }

    public void setData(ArrayList<AdsModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder {
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
            AdsModel model = mList.get(position);
            ckStatus.setChecked(model.isCheck());
            swSelect.setChecked(model.isActive());
            if (model.getPaymentInfo() != null) {
                txtBudget.setText(String.format(java.util.Locale.US, "%s %.2f", model.getPaymentInfo().getCurrency().getSimple(), model.getPaymentInfo().getTotal_amount()));
                txtSpent.setText(String.format(java.util.Locale.US, "%s %.2f", model.getPaymentInfo().getCurrency().getSimple(), model.getPaymentInfo().getTotal_amount() - model.getPaymentInfo().getRemain_amount()));
            }
            if (feed_type.equalsIgnoreCase("FeaturedBrand") || feed_type.equalsIgnoreCase("HomeAdvert")) {
                imgCnt.getLayoutParams().height = 60;
                imgCnt.getLayoutParams().width = 60;
                imgCnt.requestLayout();
                imgCnt.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_bank_ac_name_blue));
                txtCntLabel.setText(mContext.getString(R.string.txt_created_by));
                txtCnt.setText(String.format("%1$s %2$s", model.getCreator().getFirst_name(), model.getCreator().getLast_name()));
            } else {
                imgCnt.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_product));
                txtCntLabel.setText(mContext.getString(R.string.txt_products));
                txtCnt.setText(String.valueOf(model.getProductCount()));
            }
            txtPeriod.setText(String.format(java.util.Locale.US, "%s ~ %s", model.getStart_date(), model.getEnd_date()));
            if (feed_type.equalsIgnoreCase("FeaturedBrand")) {
                txtTitle.setText(model.getBrand().getName());
            } else {
                txtTitle.setText(model.getTitle());
            }

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
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_ads_promotion, parent, false));
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