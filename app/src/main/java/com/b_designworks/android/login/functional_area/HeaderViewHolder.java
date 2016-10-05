package com.b_designworks.android.login.functional_area;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b_designworks.android.R;
import com.b_designworks.android.utils.ui.BaseHolder;

import rx.functions.Func1;

/**
 * Created by Ilya Eremin on 10/5/16.
 */
class HeaderViewHolder extends BaseHolder<String> {

    public static Func1<ViewGroup, HeaderViewHolder> HOLDER_CREATOR = viewGroup
        -> new HeaderViewHolder(inflate(viewGroup, R.layout.item_header));

    public HeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override public void draw(String symbol) {
        ((TextView) itemView).setText(symbol);
    }
}
