package com.mobile.seemesave.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobile.seemesave.R;
import com.mobile.seemesave.api.cart.CartApi;
import com.mobile.seemesave.listener.BroadCastReceiverListener;
import com.mobile.seemesave.listener.RecyclerClickListener;
import com.mobile.seemesave.model.common.MCommon;
import com.mobile.seemesave.model.common.ProductModel;
import com.mobile.seemesave.model.common.ProductOneModel;
import com.mobile.seemesave.model.req.CartReq;
import com.mobile.seemesave.utils.G;
import com.mobile.seemesave.view.detail.DealDetailActivity;
import com.mobile.seemesave.view.detail.SingleProductDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductOneAdapter extends RecyclerView.Adapter<ProductOneAdapter.ViewHolder> {
    private Context mContext;
    private ProductOneRecyclerListener mListener;
    private ArrayList<ProductOneModel> mList = new ArrayList<>();
    private boolean isStar = false;
    private int isDeliver = -1;

    public void setData(ArrayList<ProductOneModel> sellingList, int is_deal) {
        this.mList.clear();
        this.mList.addAll(sellingList);
        this.isDeliver = is_deal;
        notifyDataSetChanged();
    }

    public interface ProductOneRecyclerListener {
        void onRemoveCart(int pos, ProductOneModel model);

        void onPlus(int pos, ProductOneModel model);

        void onMinus(int pos, ProductOneModel model);
    }

    public ProductOneAdapter(Context context, ArrayList<ProductOneModel> list, boolean isStar, int is_deal, ProductOneRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
        this.isStar = isStar;
        this.isDeliver = is_deal;

        G.registerBroadCast(mContext, new String[]{"Variant"}, new BroadCastReceiverListener() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String type = intent.getStringExtra("type");
                    int pos = intent.getIntExtra("pos", -1);
                    Gson gson = new Gson();
                    ArrayList<ProductModel> tmpComboList = new ArrayList<>();
                    ArrayList<ProductModel> tmpBuyList = new ArrayList<>();
                    ArrayList<ProductModel> tmpGetList = new ArrayList<>();

                    if (type.equalsIgnoreCase("combo")) {
                        tmpComboList = gson.fromJson(intent.getStringExtra("combodeal"), new TypeToken<ArrayList<ProductModel>>() {
                        }.getType());
                        if (pos != -1 && mList.size() > pos && tmpComboList.size() > 0) {
                            mList.get(pos).setImageUrl(tmpComboList.get(0).getProductDetail().getMedia());
                            mList.get(pos).setComboDeals(tmpComboList);
                            notifyDataSetChanged();
                        }
                    } else {
                        tmpBuyList = gson.fromJson(intent.getStringExtra("buys"), new TypeToken<ArrayList<ProductModel>>() {
                        }.getType());
                        tmpGetList = gson.fromJson(intent.getStringExtra("gets"), new TypeToken<ArrayList<ProductModel>>() {
                        }.getType());
                        if (pos != -1 && mList.size() > pos) {
                            if (tmpBuyList.size() > 0) {
                                mList.get(pos).setBuyList(tmpBuyList);
                                mList.get(pos).setImageUrl(tmpBuyList.get(0).getProductDetail().getMedia());
                            }
                            if (tmpGetList.size() > 0) {
                                mList.get(pos).setGetList(tmpGetList);
                            }

                            notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product_one, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
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
        private TextView txtCount;
        private TextView txtUnit;
        private ImageView imgView;
        private ImageView imgStar;
        private ImageView imgPlus;
        private ImageView imgMinus;
        private ImageView imgProductType;
        private ImageView imgVariant;
        private LinearLayout btn_add_cart, btn_in_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtUnit = itemView.findViewById(R.id.txtUnit);
            txtCount = itemView.findViewById(R.id.txtCount);
            imgView = itemView.findViewById(R.id.imgView);
            imgStar = itemView.findViewById(R.id.imgStar);
            imgPlus = itemView.findViewById(R.id.imgPlus);
            imgMinus = itemView.findViewById(R.id.imgMinus);
            imgProductType = itemView.findViewById(R.id.imgProductType);
            imgVariant = itemView.findViewById(R.id.imgVariant);
            btn_add_cart = itemView.findViewById(R.id.btn_add_cart);
            btn_in_cart = itemView.findViewById(R.id.btn_in_cart);

        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void setData(int position) {

            ProductOneModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_me)
                    .into(imgProduct);
            txtTitle.setText(model.getTitle());
            txtDescription.setText(model.getDescription() + " " + model.getPack_ize() + model.getUnit());
            txtPrice.setText(model.getCurrency().getSimple() + model.getPrice());
            if (model.getStock() == 0) {
                txtUnit.setText("Out of stock");
            } else {
                txtUnit.setText(String.format(Locale.US, "Available stock: %d", model.getStock()));
            }

            txtCount.setText(String.valueOf(model.getCount()));
            if (model.isLike()) {
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star));
            } else {
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_unstar));
            }
            if (isStar) {
                imgStar.setVisibility(View.VISIBLE);
            } else {
                imgStar.setVisibility(View.GONE);
            }
            if (model.isCart()) {
                btn_add_cart.setVisibility(View.GONE);
                btn_in_cart.setVisibility(View.VISIBLE);
            } else {
                btn_add_cart.setVisibility(View.VISIBLE);
                btn_in_cart.setVisibility(View.GONE);
            }
            if (model.isHasVariant()) {
                imgVariant.setVisibility(View.VISIBLE);
            } else {
                imgVariant.setVisibility(View.INVISIBLE);
            }
            if (model.getProduct_type().equalsIgnoreCase("SingleProduct")) {
                imgProductType.setImageDrawable(mContext.getDrawable(R.drawable.ic_single));
            } else if (model.getProduct_type().equalsIgnoreCase("ComboDeal")) {
                imgProductType.setImageDrawable(mContext.getDrawable(R.drawable.ic_combo));
            } else if (model.getProduct_type().equalsIgnoreCase("Buy1Get1FreeDeal")) {
                imgProductType.setImageDrawable(mContext.getDrawable(R.drawable.ic_buyget));
            }
            imgVariant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getProduct_type().equalsIgnoreCase("SingleProduct")) {
                        apiCallForGetVariants(position, model.getOneProductId());
                    } else {
                        onViewDetail(model, position);
                    }

                }
            });
            btn_add_cart.setOnClickListener(v -> {
                if (G.isNetworkAvailable(mContext)) {
                    CartReq req = new CartReq();
                    req.setCount(model.getCount());
                    req.setProduct_id(model.getProduct_id());
                    req.setProduct_type(model.getProduct_type());
                    req.setStore_id(model.getStore_id());
                    String variant_string = getVariantString(model);
                    req.setVariant_string(variant_string);
                    if (isDeliver == 0) {
                        CartApi.addShoppingCart(req);
                    } else if (isDeliver == 1) {
                        CartApi.addDeliverCart(req);
                    } else {
                        if (model.getFeed_type().equalsIgnoreCase("ClickCollectDeal")) {
                            CartApi.addShoppingCart(req);
                        } else {
                            CartApi.addDeliverCart(req);
                        }
                    }

                    mList.get(position).setCount(1);
                    notifyItemChanged(position);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("refresh_count"));
                } else {
                    Toast.makeText(mContext, R.string.msg_offline, Toast.LENGTH_LONG).show();
                }

            });
            btn_in_cart.setOnClickListener(v -> {
                if (G.isNetworkAvailable(mContext)) {
                    CartReq req = new CartReq();
                    req.setCount(-model.getCount());
                    req.setProduct_id(model.getProduct_id());
                    req.setProduct_type(model.getProduct_type());
                    req.setStore_id(model.getStore_id());
                    String variant_string = getVariantString(model);
                    req.setVariant_string(variant_string);
                    if (model.getFeed_type().equalsIgnoreCase("ClickCollectDeal")) {
                        CartApi.addShoppingCart(req);
                    } else {
                        CartApi.addDeliverCart(req);
                    }
                    mList.remove(position);
                    notifyDataSetChanged();
                    mListener.onRemoveCart(position, model);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("refresh_count"));
                } else {
                    Toast.makeText(mContext, R.string.msg_offline, Toast.LENGTH_LONG).show();
                }
            });
            imgPlus.setOnClickListener(v -> {
                if (G.isNetworkAvailable(mContext)) {
                    int count = model.getCount() + 1;
                    mList.get(position).setCount(count);
                    notifyItemChanged(position);
                    if (model.isCart()) {
                        CartReq req = new CartReq();
                        req.setCount(1);
                        req.setProduct_id(model.getProduct_id());
                        req.setProduct_type(model.getProduct_type());
                        req.setStore_id(model.getStore_id());
                        String variant_string = getVariantString(model);
                        req.setVariant_string(variant_string);
                        if (model.getFeed_type().equalsIgnoreCase("ClickCollectDeal")) {
                            CartApi.addShoppingCart(req);
                        } else {
                            CartApi.addDeliverCart(req);
                        }
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("refresh_count"));
                        mListener.onPlus(position, model);
                    }
                } else {
                    Toast.makeText(mContext, R.string.msg_offline, Toast.LENGTH_LONG).show();
                }

            });
            imgMinus.setOnClickListener(v -> {
                if (G.isNetworkAvailable(mContext)) {
                    if (model.getCount() == 1) return;
                    int count = model.getCount() - 1;
                    mList.get(position).setCount(count);
                    notifyItemChanged(position);
                    if (model.isCart()) {
                        CartReq req = new CartReq();
                        req.setCount(-1);
                        req.setProduct_id(model.getProduct_id());
                        req.setProduct_type(model.getProduct_type());
                        req.setStore_id(model.getStore_id());
                        String variant_string = getVariantString(model);
                        req.setVariant_string(variant_string);
                        if (model.getFeed_type().equalsIgnoreCase("ClickCollectDeal")) {
                            CartApi.addShoppingCart(req);
                        } else {
                            CartApi.addDeliverCart(req);
                        }
                        mListener.onMinus(position, model);
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("refresh_count"));
                    }
                } else {
                    Toast.makeText(mContext, R.string.msg_offline, Toast.LENGTH_LONG).show();
                }

            });
            imgView.setOnClickListener(v -> {
                onViewDetail(model, position);
            });
            imgStar.setOnClickListener(v -> {
                model.setLike(!model.isLike());
                setLikeBtn(position);
                if (G.isNetworkAvailable(mContext)) {
                    String token = G.pref.getString("token", "");
                    Ion.with(mContext)
                            .load(G.SetLikeProductsUrl)
                            .addHeader("Authorization", "Bearer " + token)
                            .setBodyParameter("barcode", model.getBarcode())
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                }
                            });
                } else {
                    Toast.makeText(mContext, R.string.msg_offline, Toast.LENGTH_LONG).show();
                }
            });
        }

        private String getVariantString(ProductOneModel model) {
            String variant_string = "";
            if (model.getProduct_type().equalsIgnoreCase("SingleProduct")) {
                variant_string = String.format("%s:%s", model.getOneProductId(), model.getBarcode());
            } else if (model.getProduct_type().equalsIgnoreCase("ComboDeal")) {
                variant_string = String.format("%s:%s", model.getComboDeals().get(0).getId(), model.getComboDeals().get(0).getProductDetail().getBarcode());
                for (int i = 1; i < model.getComboDeals().size(); i++) {
                    variant_string += "," + String.format("%s:%s", model.getComboDeals().get(i).getId(), model.getComboDeals().get(i).getProductDetail().getBarcode());
                }

            } else if (model.getProduct_type().equalsIgnoreCase("Buy1Get1FreeDeal")) {
                variant_string = String.format("%s:%s", model.getBuyList().get(0).getId(), model.getBuyList().get(0).getProductDetail().getBarcode());
                for (int i = 1; i < model.getBuyList().size(); i++) {
                    variant_string += "," + String.format("%s:%s", model.getBuyList().get(i).getId(), model.getBuyList().get(i).getProductDetail().getBarcode());
                }
                if (model.getGetList().size() > 0) {
                    variant_string += "/" + String.format("%s:%s", model.getGetList().get(0).getId(), model.getGetList().get(0).getProductDetail().getBarcode());
                    for (int i = 1; i < model.getGetList().size(); i++) {
                        variant_string += "," + String.format("%s:%s", model.getGetList().get(i).getId(), model.getGetList().get(i).getProductDetail().getBarcode());
                    }
                }
            }
            return variant_string;
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

        @SuppressLint("UseCompatLoadingForDrawables")
        private void setLikeBtn(int position) {
            ProductOneModel model = mList.get(position);
            if (model.isLike()) {
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_star));
            } else {
                imgStar.setImageDrawable(mContext.getDrawable(R.drawable.ic_unstar));
            }
        }

        void apiCallForGetVariants(int pos, int id) {
            if (G.isNetworkAvailable(mContext)) {
                ArrayList<MCommon> variants = new ArrayList<>();
                G.showLoading(mContext);
                String token = G.pref.getString("token", "");
                String url = String.format(java.util.Locale.US, G.GetVariantsUrl, id);
                Ion.with(mContext)
                        .load(url)
                        .addHeader("Authorization", "Bearer " + token)
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
                                                variants.add(variant);
                                            }
                                            if (variants.size() > 0) {
                                                showVariants(variants, pos);
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
            dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
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
                    dialog.dismiss();
                    mList.get(position).setBarcode(variants.get(vPosition).getBarcode());
                    mList.get(position).setImageUrl(variants.get(vPosition).getImageUrl());
                    mList.get(position).setDescription(variants.get(vPosition).getDescription());
                    mList.get(position).setTitle(variants.get(vPosition).getName());
                    mList.get(position).setUnit(variants.get(vPosition).getUnit());
                    mList.get(position).setPack_ize(variants.get(vPosition).getPackSize());
                    notifyDataSetChanged();
                }

                @Override
                public void onClick(View v, int position, int type) {

                }
            };
            VariantsAdapter variantsAdapter = new VariantsAdapter(mContext, variants, mListner);
            recyclerView.setAdapter(variantsAdapter);
        }
    }
}
