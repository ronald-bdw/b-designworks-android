package com.b_designworks.android.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.b_designworks.android.chat.models.DateTime;
import com.b_designworks.android.utils.ui.BaseHolder;
import com.multitype_adapter.BaseAdapterDelegate;

import java.util.List;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class DateTimeHolderDelegate extends BaseAdapterDelegate<ChatElement> {

    public DateTimeHolderDelegate(int viewType) {
        super(viewType);
    }

    @Override
    public boolean isForViewType(@NonNull List<? extends ChatElement> items, int position) {
        return items.get(position).getType() == ChatElement.Type.DATE_TIME;
    }

    @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return DateTimeHolder.CREATOR.call(parent);
    }

    @Override public void onBindViewHolder(
        @NonNull List<? extends ChatElement> items, int position,
        @NonNull RecyclerView.ViewHolder holder) {
        ((BaseHolder<DateTime>) holder).draw((DateTime) items.get(position));
    }
}
