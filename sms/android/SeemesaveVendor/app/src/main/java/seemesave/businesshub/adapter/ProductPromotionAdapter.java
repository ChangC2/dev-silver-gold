package seemesave.businesshub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.NotificationModel;
import seemesave.businesshub.model.common.ProductOneModel;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view.vendor.detail.DealDetailActivity;
import seemesave.businesshub.view.vendor.detail.SingleProductDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductPromotionAdapter extends RecyclerView.Adapter<ProductPromotionAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<ProductOneModel> mList = new ArrayList<>();
    private ProductPromotionRecyclerListener mListener;
    private boolean editable;

    public ProductPromotionAdapter(Context context, ArrayList<ProductOneModel> list, boolean editable, ProductPromotionRecyclerListener listener){
        this.mContext = context;
        this.mList.clear();
        this.mList.addAll(list);
        this.editable = editable;
        this.mListener = listener;
    }
    public interface ProductPromotionRecyclerListener{
        void onItemClicked(int pos, ProductOneModel model);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product_promotion, parent, false);
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
        @BindView(R.id.imgClose)
        ImageView imgClose;
        @BindView(R.id.imgPlus)
        ImageView imgPlus;
        @BindView(R.id.imgMinus)
        ImageView imgMinus;
        @BindView(R.id.imgProductType)
        ImageView imgProductType;
        @BindView(R.id.lytInvendory)
        LinearLayout lytInvendory;
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
            txtPrice.setText(String.format(java.util.Locale.US,"R %.2f", Float.valueOf(model.getPrice())));
            txtUnit.setText(model.getPack_size() + " " + model.getUnit());
            txtCount.setText(String.valueOf(model.getStock()));
            if (model.getProduct_type().equalsIgnoreCase("SingleProduct")) {
                imgProductType.setImageDrawable(mContext.getDrawable(R.drawable.ic_single));
            } else if (model.getProduct_type().equalsIgnoreCase("ComboDeal")) {
                imgProductType.setImageDrawable(mContext.getDrawable(R.drawable.ic_combo));
            } else if (model.getProduct_type().equalsIgnoreCase("Buy1Get1FreeDeal")) {
                imgProductType.setImageDrawable(mContext.getDrawable(R.drawable.ic_buyget));
            }
            if (editable) {
                imgClose.setVisibility(View.GONE);
            } else {
                imgClose.setVisibility(View.VISIBLE);
            }
            imgMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getStock() == 1) return;
                    mList.get(position).setStock(model.getStock() - 1);
                    notifyItemChanged(position);
                    if (editable) {
                        onUpdateStock(model.getStock() - 1, model.getProduct_id(), model.getProduct_type());
                    }
                }
            });
            imgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.get(position).setStock(model.getStock() + 1);
                    notifyItemChanged(position);
                    if (editable) {
                        onUpdateStock(model.getStock() + 1, model.getProduct_id(), model.getProduct_type());
                    }
                }
            });
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewDetail(model, position);
                }
            });
            if (!App.getPortalType()) {
                lytInvendory.setVisibility(View.GONE);
            }
        }
        private void onUpdateStock(int stock, int pid, String type) {
            Ion.with(mContext)
                    .load(G.UpdateStock)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .setBodyParameter("product_type", type)
                    .setBodyParameter("product_id", String.valueOf(pid))
                    .setBodyParameter("stock", String.valueOf(stock))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (!jsonObject.getBoolean("status")){
                                    Toast.makeText(mContext, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException jsonException) {
                                Toast.makeText(mContext, R.string.msg_something_wrong, Toast.LENGTH_LONG).show();
                            }
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
