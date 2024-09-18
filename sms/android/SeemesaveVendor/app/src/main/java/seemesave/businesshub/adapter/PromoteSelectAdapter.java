package seemesave.businesshub.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.view.vendor.detail.DealDetailActivity;
import seemesave.businesshub.view.vendor.detail.SingleProductDetailActivity;

import java.util.ArrayList;

public class PromoteSelectAdapter extends RecyclerView.Adapter<PromoteSelectAdapter.ViewHolder> {
    private Context mContext;
    private PromoteProductRecyclerListener mListener;
    private ArrayList<ProductOneModel> mList = new ArrayList<>();
    private String selType = "single";

    public void setData(ArrayList<ProductOneModel> productList) {
        this.mList.clear();
        this.mList.addAll(productList);
        notifyDataSetChanged();
    }

    public interface PromoteProductRecyclerListener {
        void onItemClicked(int pos, ProductOneModel model);

        void onPlus(int pos, ProductOneModel model);

        void onMinus(int pos, ProductOneModel model);

        void onSwitch(int pos, ProductOneModel model);
    }

    public PromoteSelectAdapter(Context context, ArrayList<ProductOneModel> list, String type, PromoteSelectAdapter.PromoteProductRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
        this.selType = type;
    }

    @NonNull
    @Override
    public PromoteSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_promote_select, parent, false);
        return new PromoteSelectAdapter.ViewHolder(view);
    }

    @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull PromoteSelectAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductOneModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgProduct);
        holder.txtTitle.setText(model.getTitle());
        holder.txtDescription.setText(model.getDescription() + " " + model.getPack_size() + model.getUnit());
        holder.txtPrice.setText(model.getCurrency().getSimple() + model.getDailyPayment());
        if (model.isCheck()) {
            holder.swSelect.setChecked(true);
        } else {
            holder.swSelect.setChecked(false);
        }
        if (model.getProduct_type().equalsIgnoreCase("SingleProduct")) {
            holder.imgVariant.setImageDrawable(mContext.getDrawable(R.drawable.ic_single));
        } else if (model.getProduct_type().equalsIgnoreCase("ComboDeal")) {
            holder.imgVariant.setImageDrawable(mContext.getDrawable(R.drawable.ic_combo));
        } else if (model.getProduct_type().equalsIgnoreCase("Buy1Get1FreeDeal")) {
            holder.imgVariant.setImageDrawable(mContext.getDrawable(R.drawable.ic_buyget));
        }
        holder.swSelect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mList.size(); i++) {
                    if (i == holder.getLayoutPosition()) {
                        mList.get(i).setCheck(true);
                    } else {
                        mList.get(i).setCheck(false);
                    }
                }
                notifyDataSetChanged();
                mListener.onSwitch(holder.getLayoutPosition(), model);
            }
        });

        holder.swSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewDetail(model, holder.getLayoutPosition());
            }
        });
    }
    private void onViewDetail(ProductOneModel model, int pos) {
        if (model.getProduct_type().equalsIgnoreCase("SingleProduct")) {
            Intent intent = new Intent(mContext, SingleProductDetailActivity.class);
            intent.putExtra("barcode", model.getBarcode());
            mContext.startActivity(intent);
        } else if (model.getProduct_type().equalsIgnoreCase("ComboDeal")) {
            Intent intent = new Intent(mContext, DealDetailActivity.class);
            Gson gson = new Gson();
            String buysJson = gson.toJson(model.getComboDeals());
            intent.putExtra("combodeal", buysJson);
            intent.putExtra("type", "combo");
            intent.putExtra("pos", pos);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, DealDetailActivity.class);
            Gson gson = new Gson();
            String buysJson = gson.toJson(model.getBuyList());
            intent.putExtra("buys", buysJson);
            String getsJson = gson.toJson(model.getGetList());
            intent.putExtra("gets", getsJson);
            intent.putExtra("type", "buyget");
            intent.putExtra("pos", pos);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtTitle;
        private TextView txtDescription;
        private TextView txtPrice;
        private ImageView imgVariant;
        private ImageView imgPlus;
        private ImageView imgMinus;
        private LinearLayout lytAdd;
        private SwitchCompat swSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgVariant = itemView.findViewById(R.id.imgVariant);
            imgPlus = itemView.findViewById(R.id.imgPlus);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            lytAdd = itemView.findViewById(R.id.lytAdd);
            swSelect = itemView.findViewById(R.id.swSelect);

        }
    }
}
