package com.b_designworks.android.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.b_designworks.android.chat.models.Message;
import com.b_designworks.android.utils.ui.BaseHolder;
import com.multitype_adapter.AdapterDelegate;

import java.util.List;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class MessageHolderDelegate implements AdapterDelegate<ChatElement> {

    private final int itemViewTypeNumber;

    public MessageHolderDelegate(int itemViewTypeNumber) {
        this.itemViewTypeNumber = itemViewTypeNumber;
    }

    @Override public int getItemViewType() {
        return itemViewTypeNumber;
    }

    @Override
    public boolean isForViewType(@NonNull List<? extends ChatElement> items, int position) {
        return items.get(position).getType() == ChatElement.Type.MESSAGE;
    }

    @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return MessageHolder.CREATOR.call(parent);
    }

    @Override public void onBindViewHolder(
        @NonNull List<? extends ChatElement> items, int position,
        @NonNull RecyclerView.ViewHolder holder) {
        ((BaseHolder<Message>) holder).draw((Message) items.get(position));
    }
}
