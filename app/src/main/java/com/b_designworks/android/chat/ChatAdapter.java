package com.b_designworks.android.chat;

import com.multitype_adapter.ListDelegationAdapter;

import java.util.List;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ChatAdapter extends ListDelegationAdapter<ChatElement> {

    public ChatAdapter(List<ChatElement> messages) {
        delegatesManager.addDelegate(new MessageHolderDelegate(1));
        delegatesManager.addDelegate(new DateTimeHolderDelegate(2));
        setItems(messages);
    }

}
