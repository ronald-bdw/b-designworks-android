package com.pairup.android.login.functional_area;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pairup.android.R;
import com.pairup.android.utils.ui.BaseHolder;

import rx.functions.Func1;

/**
 * Created by Ilya Eremin on 10/5/16.
 */
final class HeaderViewHolder extends BaseHolder<String> {

    public static final Func1<ViewGroup, HeaderViewHolder> HOLDER_CREATOR = viewGroup
        -> new HeaderViewHolder(inflate(viewGroup, R.layout.item_header));

    private HeaderViewHolder(View itemView) {
        super(itemView);
    }

    @Override public void draw(String symbol) {
        ((TextView) itemView).setText(symbol);
    }
}
