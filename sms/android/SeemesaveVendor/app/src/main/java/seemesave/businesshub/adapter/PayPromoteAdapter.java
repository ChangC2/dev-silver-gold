package seemesave.businesshub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.PayPromoteModel;
import seemesave.businesshub.view.supplier.promote.PromoteCommentActivity;
import seemesave.businesshub.view.supplier.promote.PromoteDetailActivity;
import seemesave.businesshub.view.supplier.promote.PromoteStoreActivity;

public class PayPromoteAdapter extends RecyclerView.Adapter<PayPromoteAdapter.ViewHolder> {
    private Context mContext;
    private PayProductRecyclerListener mListener;
    private ArrayList<PayPromoteModel> mList = new ArrayList<>();

    public void setData(ArrayList<PayPromoteModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface PayProductRecyclerListener {
        void onItemClicked(int pos, PayPromoteModel model);
    }

    public PayPromoteAdapter(Context context, ArrayList<PayPromoteModel> list, PayProductRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_pay_promote, parent, false);
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
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtType)
        TextView txtType;
        @BindView(R.id.txtPeriod)
        TextView txtPeriod;
        @BindView(R.id.txtProductCnt)
        TextView txtProductCnt;
        @BindView(R.id.txtStoreCnt)
        TextView txtStoreCnt;
        @BindView(R.id.txtCommentCnt)
        TextView txtCommentCnt;
        @BindView(R.id.imgAvatar)
        RoundedImageView imgAvatar;
        @BindView(R.id.imgOption)
        ImageView imgOption;
        @BindView(R.id.lytComment)
        LinearLayout lytComment;
        @BindView(R.id.lytStore)
        LinearLayout lytStore;
        @BindView(R.id.lytProduct)
        LinearLayout lytProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            PayPromoteModel model = mList.get(position);
            txtProductCnt.setText(String.valueOf(model.getProductCount()));
            txtStoreCnt.setText(String.valueOf(model.getStoreCount()));
            txtCommentCnt.setText(String.valueOf(model.getCommentCount()));
            txtTitle.setText(model.getTitle());
            String type = "";
            switch (model.getPromotion_type()) {
                case 1:
                    type = "Promotion";
                    break;
                case 2:
                    type = "Can't Miss Deal";
                    break;
                case 3:
                    type = "Best Deal";
                    break;
                case 4:
                    type = "Exclusive Deal";
                    break;
            }
            txtType.setText(type);
            txtPeriod.setText(String.format(java.util.Locale.US, "%s ~ %s", model.getStart_date(), model.getEnd_date()));
            Glide.with(mContext)
                    .load(model.getMedia())
                    .placeholder(mContext.getDrawable(R.drawable.ic_placeholder))
                    .into(imgAvatar);
            imgOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
            lytProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, PromoteDetailActivity.class);
                    i.putExtra("id", model.getId());
                    mContext.startActivity(i);
                }
            });
            lytStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, PromoteStoreActivity.class);
                    i.putExtra("id", model.getId());
                    mContext.startActivity(i);
                }
            });
            lytComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, PromoteCommentActivity.class);
                    i.putExtra("id", String.valueOf(model.getId()));
                    mContext.startActivity(i);
                }
            });
        }
    }
}
