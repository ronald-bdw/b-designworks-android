package com.b_designworks.android.login.functional_area;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.b_designworks.android.utils.ui.BaseAdapter;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * Created by Ilya Eremin on 10/5/16.
 */
class AreaCodesAdapter extends BaseAdapter<Area, AreaViewHolder>
    implements StickyRecyclerHeadersAdapter<HeaderViewHolder>,
    FastScrollRecyclerView.SectionedAdapter {

    public AreaCodesAdapter(@NonNull List<Area> items) {
        super(items, AreaViewHolder.HOLDER_CREATOR);
    }

    @Override public long getHeaderId(int position) {
        return getData().get(position).getCountry().charAt(0);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return HeaderViewHolder.HOLDER_CREATOR.call(parent);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        holder.draw(getData().get(position).getCountry().substring(0, 1));
    }

    @NonNull @Override public String getSectionName(int position) {
        return String.valueOf(getData().get(position).getCountry().substring(0, 1));
    }
}
