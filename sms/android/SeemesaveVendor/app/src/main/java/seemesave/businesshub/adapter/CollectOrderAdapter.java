package seemesave.businesshub.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.model.common.CollectOrderModel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.utils.G;
import seemesave.businesshub.view.vendor.detail.CollectOrderDetailActivity;

public class CollectOrderAdapter extends RecyclerView.Adapter<CollectOrderAdapter.ViewHolder>{
    private Context mContext;
    private OrderRecyclerListener mListener;
    private ArrayList<CollectOrderModel> mList = new ArrayList<>();

    public void setData(ArrayList<CollectOrderModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface OrderRecyclerListener{
        void onItemClicked(int pos, CollectOrderModel model);
    }

    public CollectOrderAdapter(Context context, ArrayList<CollectOrderModel> list, OrderRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_collect_order, parent, false);
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
        @BindView(R.id.lytOrder)
        LinearLayout lytOrder;
        @BindView(R.id.btnOrder)
        LinearLayout btnOrder;
        @BindView(R.id.txtOrder)
        TextView txtOrder;
        @BindView(R.id.btnUpdateStock)
        LinearLayout btnUpdateStock;
        @BindView(R.id.imgView)
        ImageView imgView;
        @BindView(R.id.ckBeing)
        CheckBox ckBeing;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            CollectOrderModel model = mList.get(position);
            ckBeing.setChecked(model.isIs_being_picked());
            if (model.isIs_pending()) {
                lytOrder.setVisibility(View.GONE);
                btnUpdateStock.setVisibility(View.GONE);
            } else {
                if (model.isIs_ready() && model.isIs_finished()) {
                    //finish
                    lytOrder.setVisibility(View.GONE);
                    btnUpdateStock.setVisibility(View.GONE);
                } else if (model.isIs_ready() && !model.isIs_finished()) {
                    //ready
                    lytOrder.setVisibility(View.VISIBLE);
                    ckBeing.setVisibility(View.GONE);
                    txtOrder.setText(mContext.getString(R.string.enter_order_number_collection));
                    btnUpdateStock.setVisibility(View.GONE);
                } else {
                    //new
                    lytOrder.setVisibility(View.VISIBLE);
                    btnUpdateStock.setVisibility(View.VISIBLE);
                    ckBeing.setVisibility(View.VISIBLE);
                    txtOrder.setText(mContext.getString(R.string.order_ready));
                    ckBeing.setOnClickListener(v -> {
                        mList.get(position).setIs_being_picked(!mList.get(position).isIs_being_picked());
                        notifyItemChanged(position);
                        Ion.with(mContext)
                                .load("PUT", G.SetBeingPicked)
                                .addHeader("Authorization", App.getToken())
                                .addHeader("Content-Language", App.getPortalToken())
                                .setBodyParameter("id", String.valueOf(model.getId()))
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {

                                    }
                                });
                    });
                }
            }

            btnOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
            txtOrderNo.setText(String.format(Locale.US, mContext.getString(R.string.order_no), model.getSecurity_code()));
            txtCollectionTime.setText(model.getOrder_time());
            txtUser.setText(String.format(Locale.US, "%1$s %2$s", model.getUser().getFirst_name(), model.getUser().getLast_name()));
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, CollectOrderDetailActivity.class);
                    i.putExtra("order_id", model.getId());
                    mContext.startActivity(i);
                }
            });
            btnUpdateStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, CollectOrderDetailActivity.class);
                    i.putExtra("order_id", model.getId());
                    boolean is_change_stock = false;
                    if (!model.isIs_ready() && !model.isIs_finished()) {
                        is_change_stock = true;
                    }
                    i.putExtra("is_change_stock", is_change_stock);
                    mContext.startActivity(i);
                }
            });
        }
    }
}
