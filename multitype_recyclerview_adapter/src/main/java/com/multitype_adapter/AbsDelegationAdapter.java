package com.multitype_adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * An implementation of an Adapter that already uses a {@link AdapterDelegatesManager} and calls
 * the corresponding {@link AdapterDelegatesManager} methods from Adapter's method like {@link
 * #onCreateViewHolder(ViewGroup, int)}, {@link #onBindViewHolder(RecyclerView.ViewHolder, int)}
 * and {@link #getItemViewType(int)}. So everything is already setup for you. You just have to add
 * the {@link AdapterDelegate}s i.e. in the constructor of a subclass that inheritance from this
 * class:
 * <pre>
 * {@code
 *    class MyAdaper extends AbsDelegationAdapter<MyDataSourceType>{
 *        public MyAdaper(){
 *            this.delegatesManager.add(new FooAdapterDelegate());
 *            this.delegatesManager.add(new BarAdapterDelegate());
 *        }
 *    }
 * }
 * </pre>
 *
 * @param <T> The type of the datasoure / items
 * @author Hannes Dorfmann
 */
public abstract class AbsDelegationAdapter<T> extends RecyclerView.Adapter {

    protected AdapterDelegatesManager<T> delegatesManager = new AdapterDelegatesManager<T>();
    protected List<? extends T> items;

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(items, position, holder);
    }

    @Override public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(items, position);
    }

    /**
     * Get the items / data source of this adapter
     *
     * @return The items / data source
     */
    public List<? extends T> getItems() {
        return items;
    }

    /**
     * Set the items / data source of this adapter
     *
     * @param items The items / data source
     */
    public void setItems(List<? extends T> items) {
        this.items = items;
    }
}