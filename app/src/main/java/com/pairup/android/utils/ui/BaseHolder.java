package com.pairup.android.utils.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder implements CanDraw<T> {

    protected static View inflate(ViewGroup viewGroup, @LayoutRes int layoutId) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
    }

    public BaseHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    protected Context context() { return itemView.getContext(); }

    protected String getString(@StringRes int str, Object... args) {
        return context().getString(str, args);
    }
}