package seemesave.businesshub.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import seemesave.businesshub.R;
import seemesave.businesshub.listener.RecyclerClickListener;
import seemesave.businesshub.model.common.BaseInfoModel;
import seemesave.businesshub.model.common.BaseInfoModel;

public class BaseInfoAdapter extends RecyclerView.Adapter<BaseInfoAdapter.ViewHolder> {
    private Context mContext;
    private RecyclerClickListener listener;
    private ArrayList<BaseInfoModel> mList = new ArrayList<>();
    private String selType = "";

    public BaseInfoAdapter(Context context, String selType, ArrayList<BaseInfoModel> list, RecyclerClickListener pListener) {
        this.mContext = context;
        this.listener = pListener;
        this.selType = selType;
        this.mList.clear();
        this.mList.addAll(list);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_baseinfo, parent, false);
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

    public void setData(ArrayList<BaseInfoModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgCheck)
        ImageView imgCheck;
        @BindView(R.id.txtTitle)
        TextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int position) {
            BaseInfoModel model = mList.get(position);
            imgCheck.setVisibility(model.isCheck() ? View.VISIBLE : View.GONE);
            if (selType.equalsIgnoreCase(mContext.getString(R.string.txt_category))) {
                txtTitle.setText(model.getTitle());
                if (model.getParent_id() == 0) {
                    txtTitle.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_500));
                } else {
                    txtTitle.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_800));
                }
            } else {
                txtTitle.setText(model.getName());
                txtTitle.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_800));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selType.equalsIgnoreCase(mContext.getString(R.string.txt_category))) {
                        if (model.getParent_id() != 0) {
                            listener.onClick(v, position);
                        }
                    } else {
                        listener.onClick(v, position);
                    }
                }
            });

        }
    }
}
