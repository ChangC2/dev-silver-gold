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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import seemesave.businesshub.R;
import seemesave.businesshub.application.App;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.MCommon;
import seemesave.businesshub.model.common.ProductDetailModel;
import seemesave.businesshub.utils.G;

public class ProductVariantSelectAdapter extends RecyclerView.Adapter<ProductVariantSelectAdapter.ViewHolder> {
    private Context mContext;
    private ProductVariantSelectRecyclerListener mListener;
    private ArrayList<ProductDetailModel> mList = new ArrayList<>();

    public void setData(ArrayList<ProductDetailModel> productList) {
        this.mList.clear();
        this.mList.addAll(productList);
        notifyDataSetChanged();
    }

    public interface ProductVariantSelectRecyclerListener {
        void onItemClicked(int pos, ProductDetailModel model);
    }

    public ProductVariantSelectAdapter(Context context, ArrayList<ProductDetailModel> list, ProductVariantSelectAdapter.ProductVariantSelectRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ProductVariantSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product_variant_select, parent, false);
        return new ProductVariantSelectAdapter.ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ProductVariantSelectAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductDetailModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getThumbnail_image())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgProduct);
        holder.txtTitle.setText(model.getBrand());
        holder.txtDescription.setText(model.getDescription());
        holder.txtPrice.setText(model.getPackSize() + model.getUnit());
        holder.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(position, model);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct, imgClose;
        private TextView txtTitle;
        private TextView txtDescription;
        private TextView txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgClose = itemView.findViewById(R.id.imgClose);

        }
    }
}
