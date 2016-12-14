package com.pairup.android.login.functional_area;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pairup.android.BaseActivity;
import com.pairup.android.R;
import com.pairup.android.login.OnAreaSelectedEvent;
import com.pairup.android.utils.Areas;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.ui.UiInfo;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;


/**
 * Created by Ilya Eremin on 10/5/16.
 */

public class FunctionalToAreaCodeScreen extends BaseActivity {

    public static final String KEY_SELECTED_AREA = "selectedArea";

    @Bind(R.id.list) RecyclerView uiList;

    private AreaCodesAdapter areaCodesAdapter;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_country_code)
            .enableBackButton()
            .setTitleRes(R.string.title_area_code);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        List<Area> areas = Areas.getAreas(this);
        Collections.sort(areas, (lhs, rhs) -> lhs.getCountry().compareTo(rhs.getCountry()));
        areaCodesAdapter = new AreaCodesAdapter(areas);
        uiList.setAdapter(areaCodesAdapter);
        uiList.setLayoutManager(new LinearLayoutManager(context()));
        uiList.addItemDecoration(new StickyRecyclerHeadersDecoration(areaCodesAdapter));
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