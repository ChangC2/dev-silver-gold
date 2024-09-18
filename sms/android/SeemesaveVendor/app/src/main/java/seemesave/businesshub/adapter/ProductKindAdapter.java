package seemesave.businesshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import seemesave.businesshub.R;
import seemesave.businesshub.model.common.ProductSelectTypeModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductKindAdapter extends RecyclerView.Adapter<ProductKindAdapter.ViewHolder>{
    private Context mContext;
    private ProductSelectRecyclerListener mListener;
    private ArrayList<ProductSelectTypeModel> mList = new ArrayList<>();
    private int selected_index = 0;

    public void setData(ArrayList<ProductSelectTypeModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface ProductSelectRecyclerListener{
        void onItemClicked(int pos, ProductSelectTypeModel model);
    }

    
    public ProductKindAdapter(Context context, ArrayList<ProductSelectTypeModel> list, ProductSelectRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_product_kind, parent, false);
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
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            ProductSelectTypeModel model = mList.get(position);
            txtTitle.setText(model.getName());

            if (model.isChecked()) {
                cardView.setAlpha(1);
            } else {
                cardView.setAlpha(0.6f);
            }
            itemView.setOnClickListener(v -> {
                mListener.onItemClicked(position, model);
            });

        }
    }
}
