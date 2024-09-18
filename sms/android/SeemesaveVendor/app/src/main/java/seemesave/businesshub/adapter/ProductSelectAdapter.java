package seemesave.businesshub.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.MCommon;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.utils.G;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductSelectAdapter extends RecyclerView.Adapter<ProductSelectAdapter.ViewHolder> {
    private Context mContext;
    private ProductSelectRecyclerListener mListener;
    private ArrayList<ProductDetailModel> mList = new ArrayList<>();
    private String selType = "single";

    public void setData(ArrayList<ProductDetailModel> productList) {
        this.mList.clear();
        this.mList.addAll(productList);
        notifyDataSetChanged();
    }

    public interface ProductSelectRecyclerListener {
        void onItemClicked(int pos, ProductDetailModel model);

        void onPlus(int pos, ProductDetailModel model);

        void onMinus(int pos, ProductDetailModel model);

        void onSwitch(int pos, ProductDetailModel model);
    }

    public ProductSelectAdapter(Context context, ArrayList<ProductDetailModel> list, String type, ProductSelectAdapter.ProductSelectRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
        this.selType = type;
    }

    @NonNull
    @Override
    public ProductSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product_select, parent, false);
        return new ProductSelectAdapter.ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ProductSelectAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductDetailModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getThumbnail_image())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgProduct);
        holder.txtTitle.setText(model.getBrand());
        holder.txtDescription.setText(model.getDescription() + " " + model.getPackSize() + model.getUnit());
        holder.txtCount.setText(String.valueOf(model.getProduct_count()));
        if (model.isCheck()) {
            holder.swSelect.setChecked(true);
            if (!selType.equalsIgnoreCase("single")) {
                holder.lytAdd.setVisibility(View.VISIBLE);
            }
        } else {
            holder.swSelect.setChecked(false);
            if (!selType.equalsIgnoreCase("single")) {
                holder.lytAdd.setVisibility(View.INVISIBLE);
            }
        }
        if (selType.equalsIgnoreCase("single")) {
            holder.lytAdd.setVisibility(View.INVISIBLE);
        }
        holder.imgPlus.setOnClickListener(v -> {
            int count = model.getProduct_count() + 1;
            mList.get(holder.getLayoutPosition()).setProduct_count(count);
            notifyItemChanged(holder.getLayoutPosition());
        });
        holder.imgMinus.setOnClickListener(v -> {
            if (model.getProduct_count() == 1) return;
            int count = model.getProduct_count() - 1;
            mList.get(holder.getLayoutPosition()).setProduct_count(count);
            notifyItemChanged(holder.getLayoutPosition());
        });

        holder.swSelect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(holder.getLayoutPosition(), model);
            }
        });

        holder.swSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });
        holder.imgVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallForGetVariants(position, model.getBarcode());
            }
        });
        if (!App.getPortalType()) {
            holder.imgVariant.setVisibility(View.GONE);
        }
    }
    void apiCallForGetVariants(int pos, String barcode) {
        if (G.isNetworkAvailable(mContext)) {
            ArrayList<MCommon> variants = new ArrayList<>();
            G.showLoading(mContext);
            String url = String.format(java.util.Locale.US, G.GetVariantsUrl, barcode);
            Ion.with(mContext)
                    .load(url)
                    .addHeader("Authorization", App.getToken())
                    .addHeader("Content-Language", App.getPortalToken())
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            G.hideLoading();
                            if (e != null) {
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getBoolean("status")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject variantInfo = jsonArray.getJSONObject(i);
                                            MCommon variant = new MCommon();
                                            variant.setBarcode(variantInfo.getString("barcode"));
                                            variant.setImageUrl(variantInfo.getString("thumbnail_image"));
                                            variant.setName(variantInfo.getString("Brand"));
                                            variant.setDescription(variantInfo.getString("description"));
                                            variant.setPackSize(variantInfo.getString("PackSize"));
                                            variant.setUnit(variantInfo.getString("Unit"));
                                            boolean variant_flag = false;
                                            if (!TextUtils.isEmpty(mList.get(pos).getVariant_string())) {
                                                if (mList.get(pos).getVariant_string().contains(variantInfo.getString("barcode"))) {
                                                    variant_flag = true;
                                                }
                                            }
                                            variant.setCheck(variant_flag);
                                            variants.add(variant);
                                        }
                                        if (variants.size() > 0) {
                                            showVariants(variants, pos);
                                        } else {
                                            Toast.makeText(mContext, R.string.txt_msg_no_variants,Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (JSONException jsonException) {
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(mContext, R.string.msg_offline, Toast.LENGTH_LONG).show();
        }

    }

    private void showVariants(ArrayList<MCommon> variants, int position) {
        Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(R.layout.dlg_variant_select);
        dialog.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String variant_str = "";
                for (int i = 0; i < variants.size(); i ++) {
                    if (variants.get(i).isCheck()) {
                        if (TextUtils.isEmpty(variant_str)) {
                            variant_str = variants.get(i).getBarcode();
                        } else {
                            variant_str = variant_str + "," + variants.get(i).getBarcode();
                        }
                    }
                }
                mList.get(position).setVariant_string(variant_str);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.select_product);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerClickListener mListner = new RecyclerClickListener() {
            @Override
            public void onClick(View v, int vPosition) {

            }

            @Override
            public void onClick(View v, int position, int type) {

            }
        };
        VariantsAdapter variantsAdapter = new VariantsAdapter(mContext, variants, mListner);
        recyclerView.setAdapter(variantsAdapter);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtTitle;
        private TextView txtDescription;
        private TextView txtCount;
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
            txtCount = itemView.findViewById(R.id.txtCount);
            imgVariant = itemView.findViewById(R.id.imgVariant);
            imgPlus = itemView.findViewById(R.id.imgPlus);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            lytAdd = itemView.findViewById(R.id.lytAdd);
            swSelect = itemView.findViewById(R.id.swSelect);

        }
    }
}
