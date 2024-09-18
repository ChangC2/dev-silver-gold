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

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.DeliverOrderModel;
import seemesave.businesshub.view.vendor.detail.CollectOrderDetailActivity;
import seemesave.businesshub.view.vendor.detail.DeliverOrderDetailActivity;

public class DeliverOrderAdapter extends RecyclerView.Adapter<DeliverOrderAdapter.ViewHolder>{
    private Context mContext;
    private OrderRecyclerListener mListener;
    private ArrayList<DeliverOrderModel> mList = new ArrayList<>();

    public void setData(ArrayList<DeliverOrderModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface OrderRecyclerListener{
        void onItemClicked(int pos, DeliverOrderModel model);
    }

    public DeliverOrderAdapter(Context context, ArrayList<DeliverOrderModel> list, OrderRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_deliver_order, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtOrderNo)
        TextView txtOrderNo;
        @BindView(R.id.txtUser)
        TextView txtUser;
        @BindView(R.id.txtCollectionTime)
        TextView txtCollectionTime;
        @BindView(R.id.txtStore)
        TextView txtStore;
        @BindView(R.id.txtAddress)
        TextView txtAddress;
        @BindView(R.id.txtContactNumber)
        TextView txtContactNumber;

        @BindView(R.id.lytOrder)
        LinearLayout lytOrder;
        @BindView(R.id.btnOrder)
        LinearLayout btnOrder;
        @BindView(R.id.btnUpdateStock)
        LinearLayout btnUpdateStock;
        @BindView(R.id.txtOrder)
        TextView txtOrder;
        @BindView(R.id.imgView)
        ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            DeliverOrderModel model = mList.get(position);

            txtOrderNo.setText(String.format(Locale.US, mContext.getString(R.string.order_no), String.valueOf(model.getId())));
            txtCollectionTime.setText(model.getOrder_time());
            txtUser.setText(String.format(Locale.US, "%1$s %2$s", model.getUser().getFirst_name(), model.getUser().getLast_name()));
            txtAddress.setText(model.getDelivery_street1() + " " + model.getDelivery_street2() + " " + model.getDelivery_suburb() + " " + model.getDelivery_city() + " " + model.getDelivery_state() + " " + model.getDelivery_country());
            txtStore.setText(model.getStore().getName());
            txtContactNumber.setText(model.getDelivery_contact_number());
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, DeliverOrderDetailActivity.class);
                    i.putExtra("order_id", model.getId());
                    mContext.startActivity(i);
                }
            });
            btnUpdateStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, DeliverOrderDetailActivity.class);
                    i.putExtra("order_id", model.getId());
                    boolean is_change_stock = false;
                    if (model.getCart_status() == 2) {
                        is_change_stock = true;
                    }
                    i.putExtra("is_change_stock", is_change_stock);
                    mContext.startActivity(i);
                }
            });
            if (model.getCart_status() == 2) {
                lytOrder.setVisibility(View.VISIBLE);
                txtOrder.setText(mContext.getString(R.string.txt_ready_picking));
                btnUpdateStock.setVisibility(View.VISIBLE);
            } else if (model.getCart_status() == 10) {
                lytOrder.setVisibility(View.GONE);
                btnUpdateStock.setVisibility(View.GONE);
            } else if (model.getCart_status() == 12) {
                lytOrder.setVisibility(View.VISIBLE);
                txtOrder.setText(mContext.getString(R.string.txt_ready_deliver));
                btnUpdateStock.setVisibility(View.GONE);
            } else if (model.getCart_status() == 13) {
                lytOrder.setVisibility(View.VISIBLE);
                txtOrder.setText(mContext.getString(R.string.txt_refresh_status));
                btnUpdateStock.setVisibility(View.GONE);
            } else if (model.getCart_status() == 50) {
                lytOrder.setVisibility(View.GONE);
                btnUpdateStock.setVisibility(View.GONE);
            } else if (model.getCart_status() == 30 || model.getCart_status() == 31){
                lytOrder.setVisibility(View.VISIBLE);
                txtOrder.setText(mContext.getString(R.string.txt_refresh_status));
                btnUpdateStock.setVisibility(View.GONE);
            }
            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
        }
    }
}
