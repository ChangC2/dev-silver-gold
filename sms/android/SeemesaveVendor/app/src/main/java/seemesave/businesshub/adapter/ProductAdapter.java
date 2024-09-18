package seemesave.businesshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.model.common.ProductDetailModel;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private Context mContext;
    private ProductRecyclerListener mListener;
    private ArrayList<ProductDetailModel> mList = new ArrayList<>();

    public void setData(ArrayList<ProductDetailModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface ProductRecyclerListener{
        void onItemClicked(int pos, ProductDetailModel model);
    }

    public ProductAdapter(Context context, ArrayList<ProductDetailModel> list, ProductRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product, parent, false);
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
        @BindView(R.id.imgProduct)
        RoundedImageView imgProduct;
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtDescription)
        TextView txtDescription;
        @BindView(R.id.txtUnit)
        TextView txtUnit;
        @BindView(R.id.txtCategory)
        TextView txtCategory;
        @BindView(R.id.btnStatusPending)
        LinearLayout btnStatusPending;
        @BindView(R.id.btnStatusActive)
        LinearLayout btnStatusActive;
        @BindView(R.id.imgOption)
        ImageView imgOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            ProductDetailModel model = mList.get(position);

            Glide.with(mContext)
                    .load(model.getThumbnail_image())
                    .into(imgProduct);
            txtName.setText(model.getBrand());
            txtDescription.setText(model.getDescription());
            txtUnit.setText(model.getPackSize() + model.getUnit());
            txtCategory.setText(model.getCategory());
            if (model.isActive()) {
                btnStatusActive.setVisibility(View.VISIBLE);
                btnStatusPending.setVisibility(View.GONE);
            } else {
                btnStatusActive.setVisibility(View.GONE);
                btnStatusPending.setVisibility(View.VISIBLE);
            }

            imgOption.setOnClickListener(v -> {
                mListener.onItemClicked(position, model);
            });

        }
    }
}
