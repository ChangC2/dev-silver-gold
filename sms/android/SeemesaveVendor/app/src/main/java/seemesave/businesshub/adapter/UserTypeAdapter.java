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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import seemesave.businesshub.R;
import seemesave.businesshub.model.common.UserPortalModel;

public class UserTypeAdapter extends RecyclerView.Adapter<UserTypeAdapter.ViewHolder>{
    private Context mContext;
    private UserTypeRecyclerListener mListener;
    private ArrayList<UserPortalModel> mList = new ArrayList<>();
    private int selected_index = 0;

    public void setData(ArrayList<UserPortalModel> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface UserTypeRecyclerListener{
        void onItemClicked(int pos, UserPortalModel model);
    }

    public UserTypeAdapter(Context context, ArrayList<UserPortalModel> list, UserTypeRecyclerListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.mList.clear();
        this.mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_user_type, parent, false);
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
        @BindView(R.id.imgLogo)
        CircleImageView imgLogo;
        @BindView(R.id.lytContent)
        LinearLayout lytContent;
        @BindView(R.id.txtName)
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setData(int position) {
            UserPortalModel model = mList.get(position);
            Glide.with(mContext)
                    .load(model.getLogo())
                    .into(imgLogo);
            txtName.setText(model.getName());
            if (model.isCheck()) {
                lytContent.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bk_green_solid_round));
            } else {
                lytContent.setBackground(null);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClicked(position, model);
                }
            });
        }
    }
}
