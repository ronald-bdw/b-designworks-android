package com.multitype_adapter;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public abstract class BaseAdapterDelegate<T> implements AdapterDelegate<T> {

    protected int viewType;

    public BaseAdapterDelegate(int viewType) {
        this.viewType = viewType;
    }

    @Override public int getItemViewType() {
        return viewType;
    }

}
