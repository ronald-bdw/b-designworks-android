package com.pairup.android.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.ui.UiInfo;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class SelectProviderScreen extends BaseActivity implements SelectProviderView{

    @Inject
    SelectProviderPresenter selectProviderPresenter;

    @Bind(R.id.select_provider_spinner) Spinner uiSelectProviderSpinner;
    @Bind(R.id.load_providers_progress_bar) ProgressBar uiLoadProvidersProgressBar;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_select_provider);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);

        selectProviderPresenter.attachView(this);

        selectProviderPresenter.setProvidersList();

    }

    @OnClick(R.id.next) void onNextClick() {

        int providersSize = uiSelectProviderSpinner.getCount() - 1;
        if (uiSelectProviderSpinner.getSelectedItemPosition() != providersSize) {
            Navigator.enterPhone(context(), AccountVerificationType.HAS_PROVIDER);
        } else if (uiSelectProviderSpinner.getSelectedItemPosition() == providersSize) {
            TrialDialog.show(this);
        } else {
            Toast.makeText(context(), R.string.provider_not_chosen, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProvidersList(List<String> providers) {

        providers.add(getResources().getString(R.string.select_providers_list_last_item));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                providers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Analytics.logScreenOpened(Analytics.EVENT_OPEN_SELECT_PROVIDER_SCREEN);

        uiLoadProvidersProgressBar.setVisibility(View.GONE);

        uiSelectProviderSpinner.setAdapter(adapter);
        uiSelectProviderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void closeScreen() {
        finish();
    }

    @Override
    protected void onDestroy() {
        selectProviderPresenter.detachView();
        super.onDestroy();
    }
}