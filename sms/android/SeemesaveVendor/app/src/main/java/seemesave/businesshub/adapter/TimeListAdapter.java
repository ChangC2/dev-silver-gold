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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.TimeListModel;
import seemesave.businesshub.model.common.TimeListModel;

public class TimeListAdapter extends RecyclerView.Adapter<TimeListAdapter.ViewHolder>{
    private Context mContext;
    private RecyclerClickListener listener;
    private ArrayList<TimeListModel> mList = new ArrayList<>();
    private String selDate = "";
    public TimeListAdapter(Context context, String selDate, ArrayList<TimeListModel> list, RecyclerClickListener pListener){
        this.mContext = context;
        this.listener = pListener;
        this.selDate = selDate;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_timelist, parent, false);
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
            TimeListModel model = mList.get(position);

            txtTitle.setText(model.getName());
            if (model.getName().equalsIgnoreCase(selDate)) {
                txtTitle.setTextColor(ContextCompat.getColor(mContext, R.color.main_blue_color));
            } else {
                txtTitle.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_800));
            }
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mList.get(position).setCheck(!mList.get(position).isCheck());
//                    notifyItemChanged(position);
                    listener.onClick(v, position);
                }
            });

        }
    }
}
