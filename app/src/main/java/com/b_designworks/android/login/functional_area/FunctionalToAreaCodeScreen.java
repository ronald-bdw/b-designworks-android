package com.b_designworks.android.login.functional_area;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.login.OnAreaSelectedEvent;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.ui.UiInfo;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;


/**
 * Created by Ilya Eremin on 10/5/16.
 */

public class FunctionalToAreaCodeScreen extends BaseActivity {

    public static final String KEY_SELECTED_AREA = "selectedArea";

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_country_code);
    }

    @Bind(R.id.list) RecyclerView uiList;

    private AreaCodesAdapter areaCodesAdapter;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        List<Area> areas = parseAreas();
        Collections.sort(areas, (lhs, rhs) -> lhs.getCountry().compareTo(rhs.getCountry()));
        areaCodesAdapter = new AreaCodesAdapter(areas);
        uiList.setAdapter(areaCodesAdapter);
        uiList.setLayoutManager(new LinearLayoutManager(context()));
        uiList.addItemDecoration(new StickyRecyclerHeadersDecoration(areaCodesAdapter));
    }

    private List<Area> parseAreas() {
        List<Area> areas = new ArrayList<>();
        String[] unparsedCountries = getString(R.string.countries).split("\n");
        for (String unparsedCountry : unparsedCountries) {
            String[] fields = unparsedCountry.split(";");
            areas.add(new Area(fields[2], fields[0]));
        }
        return areas;
    }

    @Override protected void onResume() {
        super.onResume();
        Bus.subscribe(this);
    }

    @Subscribe public void onEvent(OnAreaSelectedEvent event) {
        Intent intent = new Intent();
        intent.putExtra(KEY_SELECTED_AREA, event.getArea());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override protected void onPause() {
        Bus.unsubscribe(this);
        super.onPause();
    }

}