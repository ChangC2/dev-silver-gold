package com.mobile.seemesave.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rocky.contactfetcher.Contact;

import java.util.ArrayList;
import java.util.Locale;

public abstract class GenRecyclerAdapter
        <ViewHolder extends RecyclerView.ViewHolder, Model>
        extends RecyclerView.Adapter<ViewHolder> {
    private MyClickListener myClickListener;
    private ArrayList<Model> models;
    private ArrayList<Model> mSearchList = new ArrayList<>();
    private String searchKey = "";

    public void setKey(String key){
        this.searchKey = key;
    }

    public GenRecyclerAdapter(ArrayList<Model> models) {
        this.models = models;
        mSearchList.addAll(this.models);
    }

    public MyClickListener getMyClickListener() {
        return myClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return creatingViewHolder(parent, viewType);
    }

    protected abstract ViewHolder creatingViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        bindingViewHolder(holder, position);
    }

    protected abstract void bindingViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    public void addAll(ArrayList<Model> models) {
        int position = getItemCount();
        this.getItems().addAll(models);
        notifyItemRangeInserted(position, models.size());
    }

    public void addItem(Model model, int index) {
        getItems().add(model);
        notifyItemInserted(index);
    }

    public void addItem(Model model) {
        Contact contact = (Contact)model;
//        if (contact.displayName.toLowerCase().contains(searchKey)){
//
//        }
        getItems().add(model);
        getSearchItems().add(model);
        notifyItemInserted(getItemCount() - 1);
    }

    public void searchItems(String text) {

        models.clear();
        if (text.length() == 0) {
            models.addAll(mSearchList);
        } else {
            for (int i = 0; i < mSearchList.size(); i++) {
                Contact contact = (Contact)mSearchList.get(i);
                String name = contact.displayName;
                if (name.toLowerCase(Locale.getDefault()).contains(text)) {
                    models.add(mSearchList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
    private ArrayList<Model> getItems() {
        return models;
    }
    private ArrayList<Model> getSearchItems() {
        return mSearchList;
    }
    public void deleteAll() {
        int itemCount = getItemCount();
        getItems().clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    public void replaceAll(ArrayList<Model> models) {
        int previousSize = getItemCount();
        getItems().clear();
        notifyItemRangeRemoved(0, previousSize);
        getItems().addAll(models);
        notifyItemRangeInserted(0, getItemCount());
//        notifyDataSetChanged();
    }


    public Model getItem(int index) {
        return getItems().get(index);
    }
    public void deleteItem(int index) {
        if (index >= 0 && index < getItemCount()) {
            getItems().remove(index);
            notifyItemRemoved(index);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
