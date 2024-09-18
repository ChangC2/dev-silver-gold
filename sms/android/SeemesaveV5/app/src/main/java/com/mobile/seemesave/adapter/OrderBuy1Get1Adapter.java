package com.mobile.seemesave.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.seemesave.R;
import com.mobile.seemesave.model.common.BuyGetProductCartModel;
import com.mobile.seemesave.model.common.RealDetailsModel;
import com.mobile.seemesave.view.detail.SingleProductDetailActivity;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderBuy1Get1Adapter extends RecyclerView.Adapter<OrderBuy1Get1Adapter.ViewHolder>{
    private Context mContext;
    private OrderBuy1Get1Adapter.OrderBuy1Get1RecyclerListener mListener;
    private ArrayList<BuyGetProductCartModel> mList = new ArrayList<>();

    public void setData(ArrayList<BuyGetProductCartModel> List) {
        this.mList.clear();
        this.mList.addAll(List);
        notifyDataSetChanged();
    }

    public interface OrderBuy1Get1RecyclerListener{
        void onItemClicked(int pos, BuyGetProductCartModel model);
    }

    public OrderBuy1Get1Adapter(Context context, ArrayList<BuyGetProductCartModel> list, OrderBuy1Get1Adapter.OrderBuy1Get1RecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public OrderBuy1Get1Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_order_buy1get1, parent, false);
        return new OrderBuy1Get1Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderBuy1Get1Adapter.ViewHolder holder, int position) {
        holder.setData(holder.getLayoutPosition());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtPrice)
        TextView txtPrice;

        @BindView(R.id.txtCount)
        TextView txtCount;

        @BindView(R.id.recyclerBuy)
        RecyclerView recyclerBuy;

        @BindView(R.id.recyclerGet)
        RecyclerView recyclerGet;

        DealDetailRealAdapter buyAdapter, getAdapter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            BuyGetProductCartModel model = mList.get(position);
            txtCount.setText(String.format(Locale.US, "✕ %d", model.getCount()));
            try {
                txtPrice.setText(String.format(java.util.Locale.US,"%s %.2f", model.getProductDetails().getCurrency().getSimple(), Float.valueOf(model.getProductDetails().getSelling_price())));
            } catch (Exception cu) {
                txtPrice.setText(String.format(java.util.Locale.US,"R %.2f", Float.valueOf(model.getProductDetails().getSelling_price())));
            }
            setRecyclerBuy(position);
            setRecyclerGet(position);
        }
        private void setRecyclerBuy(int mPosition) {
            buyAdapter = new DealDetailRealAdapter(mContext, mList.get(mPosition).getRealBuyDetails(), new DealDetailRealAdapter.DealDetailRealRecyclerListener() {


                @Override
                public void onItemClicked(int pos, RealDetailsModel model) {
                    Intent intent = new Intent(mContext, SingleProductDetailActivity.class);
                    intent.putExtra("barcode", model.getRealProduct().getBarcode());
                    mContext.startActivity(intent);
                }
            });
            recyclerBuy.setAdapter(buyAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            recyclerBuy.setLayoutManager(linearLayoutManager);
        }

        private void setRecyclerGet(int mPosition) {
            getAdapter = new DealDetailRealAdapter(mContext, mList.get(mPosition).getRealFreeDetails(), new DealDetailRealAdapter.DealDetailRealRecyclerListener() {


                @Override
                public void onItemClicked(int pos, RealDetailsModel model) {
                    Intent intent = new Intent(mContext, SingleProductDetailActivity.class);
                    intent.putExtra("barcode", model.getRealProduct().getBarcode());
                    mContext.startActivity(intent);
                }
            });
            recyclerGet.setAdapter(getAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            recyclerGet.setLayoutManager(linearLayoutManager);
        }
    }
}
