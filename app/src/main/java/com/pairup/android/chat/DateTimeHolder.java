package com.pairup.android.chat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pairup.android.R;
import com.pairup.android.chat.models.DateTime;
import com.pairup.android.utils.ui.BaseHolder;

import rx.functions.Func1;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class DateTimeHolder extends BaseHolder<DateTime> {

    public static final Func1<ViewGroup, DateTimeHolder> CREATOR = viewGroup
        -> new DateTimeHolder(inflate(viewGroup, R.layout.item_date_time));

    public DateTimeHolder(View itemView) {
        super(itemView);
    }

    @Override public void draw(DateTime dateTime) {
        ((TextView) itemView).setText(dateTime.getTimestamp());
    }
}
