package seemesave.businesshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.AdsTabModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OneSelectionAdapter extends RecyclerView.Adapter<OneSelectionAdapter.ViewHolder>{
    private Context mContext;
    private OneSelectionRecyclerListener mListener;
    private ArrayList<String> mList = new ArrayList<>();
    private int selected_index = 0;

    public void setData(ArrayList<String> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface OneSelectionRecyclerListener{
        void onItemClicked(int pos, String model);
    }

    public void setSelected_index(int index){
        selected_index = index;
        notifyDataSetChanged();
    }

    public OneSelectionAdapter(Context context, ArrayList<String> list, OneSelectionRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_one_selection, parent, false);
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
        @BindView(R.id.imgClose)
        ImageView imgClose;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            txtTitle.setText(mList.get(position));
            itemView.setOnClickListener(v -> {
                mListener.onItemClicked(position, mList.get(position));
            });

        }
    }
}
