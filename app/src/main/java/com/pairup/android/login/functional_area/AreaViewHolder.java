package com.pairup.android.login.functional_area;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pairup.android.R;
import com.pairup.android.login.OnAreaSelectedEvent;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.ui.BaseHolder;

import butterknife.Bind;
import rx.functions.Func1;

/**
 * Created by Ilya Eremin on 10/5/16.
 */
public class AreaViewHolder extends BaseHolder<Area> {

    final static Func1<ViewGroup, AreaViewHolder> HOLDER_CREATOR = viewGroup
        -> new AreaViewHolder(inflate(viewGroup, R.layout.item_area));

    @Bind(R.id.country) TextView uiCountry;
    @Bind(R.id.code)    TextView uiCode;
    private             Area     area;

    private AreaViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(onClick -> Bus.event(new OnAreaSelectedEvent(area)));
    }

    @Override public void draw(Area area) {
        this.area = area;
        uiCountry.setText(area.getCountry());
        uiCode.setText(area.getCode());
    }
}
