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
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import seemesave.businesshub.R;
import seemesave.businesshub.model.common.StoreCategoryModel;
public class StoreCategorySelectAdapter extends RecyclerView.Adapter<StoreCategorySelectAdapter.ViewHolder> {
    private Context mContext;
    private StoreCategorySelectRecyclerListener mListener;
    private ArrayList<StoreCategoryModel> mList = new ArrayList<>();
    

    public void setData(ArrayList<StoreCategoryModel> productList) {
        this.mList.clear();
        this.mList.addAll(productList);
        notifyDataSetChanged();
    }

    public interface StoreCategorySelectRecyclerListener {
        void onItemClicked(int pos, StoreCategoryModel model);

    }

    public StoreCategorySelectAdapter(Context context, ArrayList<StoreCategoryModel> list, StoreCategorySelectAdapter.StoreCategorySelectRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public StoreCategorySelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_store_category_select, parent, false);
        return new StoreCategorySelectAdapter.ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull StoreCategorySelectAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StoreCategoryModel model = mList.get(holder.getLayoutPosition());
        holder.txtTitle.setText(model.getName());
        if (model.isCheck()) {
            holder.imgCheck.setVisibility(View.VISIBLE);
        } else {
            holder.imgCheck.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(holder.getLayoutPosition(), model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCheck;
        private TextView txtTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCheck = itemView.findViewById(R.id.imgCheck);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }
}
