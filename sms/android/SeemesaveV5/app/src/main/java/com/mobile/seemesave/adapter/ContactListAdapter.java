package com.mobile.seemesave.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.seemesave.R;
import com.rocky.contactfetcher.Contact;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends GenRecyclerAdapter<ContactListAdapter.DataObjectHolder, Contact> {
    private final AppCompatActivity activity;
    private ArrayList<Contact> saveForLaterContacts = new ArrayList<>();
    private ArrayList<Contact> mSearchList = new ArrayList<>();

    public void setSearchKey(String key){
        setKey(key);
    }

    public ContactListAdapter(AppCompatActivity activity, ArrayList<Contact> strings) {
        super(strings);
        this.activity = activity;
    }

    @Override
    protected DataObjectHolder creatingViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_contact_fectch, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    protected void bindingViewHolder(DataObjectHolder holder, int position) {
        Contact item = getItem(position);
        holder.txtRowContactName.setText(item.displayName);
        if (item.phoneNumbers.size() > 0)
            holder.txtRowContactMobile.setText(item.phoneNumbers.get(0));
        else
            holder.txtRowContactMobile.setVisibility(View.GONE);

        boolean isImagePresent = item.photo != null && !TextUtils.isEmpty(item.photo.toString());
        holder.imgContact.setVisibility(isImagePresent ? View.VISIBLE : View.GONE);
        holder.txtContactInitial.setVisibility(!isImagePresent ? View.VISIBLE : View.GONE);
        if (!isImagePresent) {
            if (!TextUtils.isEmpty(item.displayName))
                holder.txtContactInitial.setText(String.valueOf(item.displayName.charAt(0)).toUpperCase());
        } else {
            if (item.thumbnail != null && !TextUtils.isEmpty(item.thumbnail.toString())) {
                Glide.with(activity)
                        .load(item.thumbnail)
                        .into(holder.imgContact);

            }
        }
    }

    public void loadSaveForLaterContacts() {
        addAll(saveForLaterContacts);
        saveForLaterContacts.clear();
    }

    class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private AppCompatTextView txtContactInitial;
        private CircleImageView imgContact;
        private AppCompatTextView txtRowContactName;
        private AppCompatTextView txtRowContactMobile;

        DataObjectHolder(View view) {
            super(view);
            txtRowContactName = view.findViewById(R.id.txt_row_contact_name);
            txtRowContactMobile = view.findViewById(R.id.txt_row_contact_mobile);
            imgContact = view.findViewById(R.id.img_contact);
            txtContactInitial = view.findViewById(R.id.txt_contact_initial);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getMyClickListener() != null)
                getMyClickListener().onItemClick(getLayoutPosition(), v);
        }
    }
}
