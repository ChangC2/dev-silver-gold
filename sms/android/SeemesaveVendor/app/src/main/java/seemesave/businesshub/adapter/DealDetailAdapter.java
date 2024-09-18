package seemesave.businesshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.ProductModel;

import java.util.ArrayList;

public class DealDetailAdapter extends RecyclerView.Adapter<DealDetailAdapter.ViewHolder>{
    private Context mContext;
    private DealDetailRecyclerListener mListener;
    private ArrayList<ProductModel> mList = new ArrayList<>();

    public interface DealDetailRecyclerListener{
        void onItemClicked(int pos, ProductModel model);
        void onItemVariantClicked(int pos, ProductModel model);
    }

    public DealDetailAdapter(Context context, ArrayList<ProductModel> list, DealDetailRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    public void setData(ArrayList<ProductModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_deal_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getProductDetail().getThumbnail_image())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgProduct);
        holder.txtDesc.setText(model.getProductDetail().getDescription());
        holder.txtSize.setText(String.format(java.util.Locale.US,"%s%s", model.getProductDetail().getPackSize(), model.getProductDetail().getUnit()));
        holder.txtCount.setText(String.format(java.util.Locale.US,"✕  %d",  model.getQuantity()));
        if (model.isHasVariants()) {
            holder.imgVariant.setVisibility(View.VISIBLE);
        } else {
            holder.imgVariant.setVisibility(View.GONE);
        }
        holder.imgVariant.setOnClickListener(v -> {
            mListener.onItemVariantClicked(holder.getLayoutPosition(), model);
        });
        holder.itemView.setOnClickListener(v -> {
            mListener.onItemClicked(holder.getLayoutPosition(), model);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProduct, imgVariant;
        private TextView txtSize;
        private TextView txtDesc;
        private TextView txtCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtCount = itemView.findViewById(R.id.txtCount);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            txtSize = itemView.findViewById(R.id.txtSize);
            imgVariant = itemView.findViewById(R.id.imgVariant);

        }
    }
}
