package seemesave.businesshub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.CollectOrderModel;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.view.vendor.detail.DealDetailActivity;
import seemesave.businesshub.view.vendor.detail.SingleProductDetailActivity;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<ProductOneModel> mList = new ArrayList<>();
    private boolean is_change_stock = false;
    private ProductOrderRecyclerListener mListener;

    public ProductOrderAdapter(Context context, ArrayList<ProductOneModel> list, boolean is_change_stock, ProductOrderRecyclerListener listener){
        this.mContext = context;
        this.mList.clear();
        this.mList.addAll(list);
        this.is_change_stock = is_change_stock;
        this.mListener = listener;
    }
    public interface ProductOrderRecyclerListener{
        void onPlus(int pos, ProductOneModel model);
        void onMinus(int pos, ProductOneModel model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product_order, parent, false);
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

    public void setData(ArrayList<ProductOneModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgProduct)
        ImageView imgProduct;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtDescription)
        TextView txtDescription;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.txtCount)
        TextView txtCount;
        @BindView(R.id.txtUnit)
        TextView txtUnit;
        @BindView(R.id.imgProductType)
        ImageView imgProductType;
        @BindView(R.id.imgPlus)
        ImageView imgPlus;
        @BindView(R.id.imgMinus)
        ImageView imgMinus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            ProductOneModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_me)
                    .into(imgProduct);
            txtTitle.setText(model.getTitle());
            txtDescription.setText(model.getDescription());
            txtPrice.setText(String.format(Locale.US,"R %.2f", Float.valueOf(model.getPrice())));
            txtUnit.setText(model.getPack_size() + " " + model.getUnit());
            txtCount.setText(String.valueOf(model.getStock()));
            if (model.getProduct_type().equalsIgnoreCase("SingleProduct")) {
                imgProductType.setImageDrawable(mContext.getDrawable(R.drawable.ic_single));
            } else if (model.getProduct_type().equalsIgnoreCase("ComboDeal")) {
                imgProductType.setImageDrawable(mContext.getDrawable(R.drawable.ic_combo));
            } else if (model.getProduct_type().equalsIgnoreCase("Buy1Get1FreeDeal")) {
                imgProductType.setImageDrawable(mContext.getDrawable(R.drawable.ic_buyget));
            }
            if (is_change_stock) {
                imgMinus.setVisibility(View.VISIBLE);
                imgPlus.setVisibility(View.VISIBLE);
            } else {
                imgMinus.setVisibility(View.GONE);
                imgPlus.setVisibility(View.GONE);
            }
            imgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPlus(position, model);
                }
            });
            imgMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMinus(position, model);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewDetail(model, position);
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
    }
}
