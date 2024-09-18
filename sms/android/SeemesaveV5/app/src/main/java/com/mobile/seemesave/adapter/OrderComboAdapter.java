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
import com.mobile.seemesave.model.common.ComboDealProductCartModel;
import com.mobile.seemesave.model.common.RealDetailsModel;
import com.mobile.seemesave.view.detail.SingleProductDetailActivity;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderComboAdapter extends RecyclerView.Adapter<OrderComboAdapter.ViewHolder>{
    private Context mContext;
    private OrderComboAdapter.ComboRecyclerListener mListener;
    private ArrayList<ComboDealProductCartModel> mList = new ArrayList<>();

    public void setData(ArrayList<ComboDealProductCartModel> List) {
        this.mList.clear();
        this.mList.addAll(List);
        notifyDataSetChanged();
    }

    public interface ComboRecyclerListener{
        void onItemClicked(int pos, ComboDealProductCartModel model);
    }

    public OrderComboAdapter(Context context, ArrayList<ComboDealProductCartModel> list, OrderComboAdapter.ComboRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public OrderComboAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_order_combo, parent, false);
        return new OrderComboAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderComboAdapter.ViewHolder holder, int position) {
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

        @BindView(R.id.recycler)
        RecyclerView recycler;

        DealDetailRealAdapter comboAdapter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            ComboDealProductCartModel model = mList.get(position);
            txtCount.setText(String.format(Locale.US, "✕ %d", model.getCount()));
            try {
                txtPrice.setText(String.format(java.util.Locale.US,"%s %.2f", model.getProductDetails().getCurrency().getSimple(), Float.valueOf(model.getProductDetails().getSelling_price())));
            } catch (Exception cu) {
                txtPrice.setText(String.format(java.util.Locale.US,"R %.2f", Float.valueOf(model.getProductDetails().getSelling_price())));
            }
            setRecyclerBuy(position);
        }
        private void setRecyclerBuy(int mPosition) {
            comboAdapter = new DealDetailRealAdapter(mContext, mList.get(mPosition).getRealDetails(), new DealDetailRealAdapter.DealDetailRealRecyclerListener() {


                @Override
                public void onItemClicked(int pos, RealDetailsModel model) {
                    Intent intent = new Intent(mContext, SingleProductDetailActivity.class);
                    intent.putExtra("barcode", model.getRealProduct().getBarcode());
                    mContext.startActivity(intent);
                }
            });
            recycler.setAdapter(comboAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            recycler.setLayoutManager(linearLayoutManager);
        }

    }
}
