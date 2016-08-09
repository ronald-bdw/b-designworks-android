package com.multitype_adapter;

import java.util.List;

/**
 * An adapter implementation designed for items organized in a {@link List}. This adapter
 * implementation is ready to go. All you have to do is to add {@link AdapterDelegate}s to the
 * internal {@link AdapterDelegatesManager} i.e. in the constructor:
 * <p>
 * <pre>
 * {@code
 *    class MyAdaper extends AbsDelegationAdapter<List<Foo>>{
 *        public MyAdaper(){
 *            this.delegatesManager.add(new FooAdapterDelegate());
 *            this.delegatesManager.add(new BarAdapterDelegate());
 *        }
 *    }
 * }
 * </pre>
 *
 * @param <T> The type of the items. Must be something that extends from List like List<Foo>
 * @author Hannes Dorfmann
 */
public class ListDelegationAdapter<T> extends AbsDelegationAdapter<T> {

    @Override public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public List<? extends T> getData() {
        return items;
    }
}
