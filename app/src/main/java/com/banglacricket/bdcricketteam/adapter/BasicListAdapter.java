package com.banglacricket.bdcricketteam.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author Ripon
 */
public abstract class BasicListAdapter<T, X extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<X> {

    private List<T> itemList;

    public BasicListAdapter(List<T> itemList) {
        this.itemList = itemList;
    }

    public void updateList(List<T> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public abstract X onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(X holder, int position);

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public T getItem(int position) {
        return itemList.get(position);
    }
}
