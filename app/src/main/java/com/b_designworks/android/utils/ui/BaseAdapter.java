package com.b_designworks.android.utils.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import rx.functions.Func1;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class BaseAdapter<T, VH extends BaseHolder<T>> extends RecyclerView.Adapter<VH> {
    @NonNull       List<T>              items;
    final @NonNull Func1<ViewGroup, VH> func0;

    public BaseAdapter(@NonNull List<T> items, @NonNull Func1<ViewGroup, VH> func0) {
        this.items = items;
        this.func0 = func0;
    }

    @Override public VH onCreateViewHolder(ViewGroup viewGroup, int i) {
        return func0.call(viewGroup);
    }

    @Override public void onBindViewHolder(VH vh, int position) {
        final T t = items.get(position);
        vh.draw(t);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public List<T> getData() {
        return items;
    }

    public void add(T item) {
        addWithoutNotifying(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addWithoutNotifying(T item) {
        this.items.add(item);
    }

}
