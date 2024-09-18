package com.mobile.seemesave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.PromotionModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<PromotionModel> mList = new ArrayList<>();
    private PromotionRecyclerListener mListener;
    private int type = 0; //0 : shopping list, 1 : shopping cart

    public interface PromotionRecyclerListener{
        void onItemClicked(int pos, PromotionModel model);
    }


    public PromotionAdapter(Context context, ArrayList<PromotionModel> list, PromotionRecyclerListener listener) {
        this.mList.clear();
        this.mList.addAll(list);
        this.mListener = listener;
        mContext = context;
    }

    public void setDatas(ArrayList<PromotionModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setType(int type){
        this.type = type;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_promotion1, parent, false);
            ViewHolder1 viewholder = new ViewHolder1(view);
            return viewholder;
        }else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_promotion2, parent, false);
            ViewHolder2 viewholder = new ViewHolder2(view);
            return viewholder;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.setData(Vholder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        PromotionModel shopping = mList.get(position);
        if (shopping.getSingleProducts().size() > 0 )
            return 0;
        else
            return 1;
    }

    // ******************************class ViewHoler redefinition ***************************//
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
        public void setData(int position) {
        }
    }

    public class ViewHolder1 extends ViewHolder {
        @BindView(R.id.imgPhoto)
        ImageView imgPhoto;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtDesc)
        TextView txtDesc;
        @BindView(R.id.txtPackSize)
        TextView txtPackSize;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.txtCount)
        TextView txtCount;
        @BindView(R.id.imgPlus)
        ImageView imgPlus;
        @BindView(R.id.imgMinus)
        ImageView imgMinus;

        @BindView(R.id.imgLike)
        ImageView imgLike;

        @BindView(R.id.lytAdd)
        LinearLayout lytAdd;

        public ViewHolder1(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void setData(int position) {
            PromotionModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getSingleProducts().get(0).getProduct().getProductDetail().getMedia())
                    .fitCenter()
                    .into(imgPhoto);
            txtName.setText(model.getSingleProducts().get(0).getProduct().getProductDetail().getBrand());
            txtDesc.setText(String.format(java.util.Locale.US,"%s", model.getSingleProducts().get(0).getProduct().getProductDetail().getDescription()));
            txtPackSize.setText(String.format(java.util.Locale.US, "%s%s", model.getSingleProducts().get(0).getProduct().getProductDetail().getPackSize(), model.getSingleProducts().get(0).getProduct().getProductDetail().getUnit()));
            txtPrice.setText(String.format(java.util.Locale.US, "%s%.2f", model.getSingleProducts().get(0).getCurrency().getSimple(), Float.valueOf(model.getSingleProducts().get(0).getSelling_price())));
            txtCount.setText(String.valueOf(model.getSingleProducts().get(0).getStock()));

            imgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.getSingleProducts().get(0).setStock(model.getSingleProducts().get(0).getStock()+1);
                    txtCount.setText(String.valueOf(model.getSingleProducts().get(0).getStock()));
                }
            });

            imgMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getSingleProducts().get(0).getStock() > 1) {
                        model.getSingleProducts().get(0).setStock(model.getSingleProducts().get(0).getStock() - 1);
                        txtCount.setText(String.valueOf(model.getSingleProducts().get(0).getStock()));
                    }
                }
            });

            itemView.setOnClickListener(v -> {
                mListener.onItemClicked(position, model);
            });
            lytAdd.setOnClickListener(v -> {
                mListener.onItemClicked(position, model);
            });

            if (model.getSingleProducts().get(0).getProduct().getProductDetail().isLike()){
                Glide.with(mContext)
                        .load(ContextCompat.getDrawable(mContext,R.drawable.ic_like_sel))
                        .fitCenter()
                        .into(imgLike);
            }else{
                Glide.with(mContext)
                        .load(ContextCompat.getDrawable(mContext,R.drawable.ic_like))
                        .fitCenter()
                        .into(imgLike);
            }
            imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.getSingleProducts().get(0).getProduct().getProductDetail().setLike(!model.getSingleProducts().get(0).getProduct().getProductDetail().isLike());
                    notifyDataSetChanged();

                }
            });
        }


    }

    public class ViewHolder2 extends ViewHolder {
        @BindView(R.id.recyclerBuyImage1)
        RecyclerView recyclerBuyImage1;
        @BindView(R.id.recyclerBuyImage2)
        RecyclerView recyclerBuyImage2;
        @BindView(R.id.recyclerGetImage)
        RecyclerView recyclerGetImage;
        @BindView(R.id.txtDesc)
        TextView txtDesc;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.txtCount)
        TextView txtCount;
        @BindView(R.id.lytAdd)
        LinearLayout lytAdd;
        @BindView(R.id.imgMinus)
        ImageView imgMinus;
        @BindView(R.id.imgPlus)
        ImageView imgPlus;

        @BindView(R.id.imgDetail)
        ImageView imgDetail;

        PromotionImageAdapter buyImage1Adapter;
        PromotionImageAdapter buyImage2Adapter;
        PromotionImageAdapter getImageAdapter;

        public ViewHolder2(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void setData(int position) {
            PromotionModel model = mList.get(position);
//            MPromotion2 promotion2;
//            if (model.getComboDeals().size() == 0 && model.getBuy1Get1FreeDeals().size() == 0)
//                return;
//
//            int buyCount = 0;
//            int getCount = 0;
//
//            if (model.getBuy1Get1FreeDeals().size()>0) {
//                buyCount = model.getBuy1Get1FreeDeals().get(0).getBuyProducts().size();
//                getCount = model.getBuy1Get1FreeDeals().get(0).getFreeProducts().size();
//            }
//
//            if (buyCount == 0)
//                return;
//            if (buyCount<4 && buyCount>0){
//                recyclerBuyImage2.setVisibility(View.GONE);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, buyCount, GridLayoutManager.VERTICAL, false);
//                recyclerBuyImage1.setLayoutManager(gridLayoutManager);
//                buyImage1Adapter = new PromotionImageAdapter(mContext, model.getBuy1Get1FreeDeals().get(0).getBuyProducts(), null);
//                recyclerBuyImage1.setAdapter(buyImage1Adapter);
//            }else{
//                recyclerBuyImage2.setVisibility(View.VISIBLE);
//
//                ArrayList<MCommon> buys1 = new ArrayList<>();
//                ArrayList<MCommon> buys2 = new ArrayList<>();
//                for (int i=0;i<buyCount - ((int)buyCount/2);i++){
//                    buys1.add(promotion2.getArryBuyProduct().get(i));
//                }
//
//                for (int i=buyCount - ((int)buyCount/2);i<buyCount;i++){
//                    buys2.add(promotion2.getArryBuyProduct().get(i));
//                }
//
//                GridLayoutManager buy1LayoutManager = new GridLayoutManager(mContext,buyCount - ((int)buyCount/2), GridLayoutManager.VERTICAL, false);
//                recyclerBuyImage1.setLayoutManager(buy1LayoutManager);
//
//                buyImage1Adapter = new RFeedPromotionImageAdapter(mContext, buys1, null);
//                recyclerBuyImage1.setAdapter(buyImage1Adapter);
//
//                GridLayoutManager buy2LayoutManager = new GridLayoutManager(mContext, (int)buyCount/2, GridLayoutManager.VERTICAL, false);
//                recyclerBuyImage2.setLayoutManager(buy2LayoutManager);
//                buyImage2Adapter = new RFeedPromotionImageAdapter(mContext, buys2, null);
//                recyclerBuyImage2.setAdapter(buyImage2Adapter);
//            }
//
//            if (getCount == 0){
//                recyclerGetImage.setVisibility(View.GONE);
//            }else{
//                recyclerGetImage.setVisibility(View.VISIBLE);
//                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, getCount, GridLayoutManager.VERTICAL, false);
//                recyclerGetImage.setLayoutManager(gridLayoutManager);
//                getImageAdapter = new RFeedPromotionImageAdapter(mContext, promotion2.getArryGetProduct(), null);
//                recyclerGetImage.setAdapter(getImageAdapter);
//            }
//
//            String descStr = "";
//
//            if (model.getBuy1get1Products().size()>0)
//                txtDesc.setText(String.format(java.util.Locale.US, "buy %d product(s) for R%s, get %d product(s) for free", promotion2.getArryBuyProduct().size(), promotion2.getPrice(), promotion2.getArryGetProduct().size()));
//            else
//                txtDesc.setText(String.format(java.util.Locale.US, "%d product(s) for R%s", promotion2.getArryBuyProduct().size(), promotion2.getPrice()));
//
//            txtPrice.setText(String.format(java.util.Locale.US, "%s%.2f", promotion2.getCurrency().getSimple(), Float.valueOf(promotion2.getPrice())));
//            txtCount.setText(String.valueOf(promotion2.getQty()));
//
//            imgPlus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    promotion2.setQty(promotion2.getQty()+1);
//                    txtCount.setText(String.valueOf(promotion2.getQty()));
//                }
//            });
//
//            imgMinus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (promotion2.getQty() > 1) {
//                        promotion2.setQty(promotion2.getQty() - 1);
//                        txtCount.setText(String.valueOf(promotion2.getQty()));
//                    }
//                }
//            });

            imgDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }
    }
}