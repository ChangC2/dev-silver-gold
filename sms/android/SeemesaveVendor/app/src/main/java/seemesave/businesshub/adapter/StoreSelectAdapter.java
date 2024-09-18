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
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.model.common.StoreModel;
import seemesave.businesshub.utils.G;

public class StoreSelectAdapter extends RecyclerView.Adapter<StoreSelectAdapter.ViewHolder> {
    private Context mContext;
    private StoreSelectRecyclerListener mListener;
    private ArrayList<StoreModel> mList = new ArrayList<>();

    public void setData(ArrayList<StoreModel> productList) {
        this.mList.clear();
        this.mList.addAll(productList);
        notifyDataSetChanged();
    }

    public interface StoreSelectRecyclerListener {
        void onItemClicked(int pos, StoreModel model);
    }

    public StoreSelectAdapter(Context context, ArrayList<StoreModel> list, StoreSelectAdapter.StoreSelectRecyclerListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public StoreSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_store_select, parent, false);
        return new StoreSelectAdapter.ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull StoreSelectAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StoreModel model = mList.get(holder.getLayoutPosition());
        Glide.with(mContext)
                .load(model.getLogo())
                .centerCrop()
                .placeholder(R.drawable.ic_me)
                .into(holder.imgProduct);
        holder.txtTitle.setText(model.getName());
        holder.txtAddress.setText(model.getAddress());
        holder.swSelect.setChecked(model.isCheck());
        
        holder.swSelect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(holder.getLayoutPosition(), model);
            }
        });

        holder.swSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtTitle;
        private TextView txtDescription;
        private TextView txtAddress;
        private SwitchCompat swSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            swSelect = itemView.findViewById(R.id.swSelect);

        }
    }
}
