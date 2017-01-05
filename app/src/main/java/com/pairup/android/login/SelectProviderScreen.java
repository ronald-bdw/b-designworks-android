package com.pairup.android.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.ui.UiInfo;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class SelectProviderScreen extends BaseActivity {

    private static final int HBF_POSITION = 0;
    private static final int BDW_POSITION = 1;
    private static final int NO_PROVIDER_POSITION = 2;

    @Bind(R.id.select_provider_spinner) Spinner uiSelectProviderSpinner;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_select_provider);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item,
            getResources().getStringArray(R.array.select_provider_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Analytics.logScreenOpened(Analytics.EVENT_OPEN_SELECT_PROVIDER_SCREEN);

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

    @OnClick(R.id.next) void onNextClick() {
        switch (uiSelectProviderSpinner.getSelectedItemPosition()) {
            case HBF_POSITION:
                Navigator.enterPhone(context(), AccountVerificationType.HAS_HBF_PROVIDER);
                break;
            case BDW_POSITION:
                Navigator.enterPhone(context(), AccountVerificationType.HAS_BDW_PROVIDER);
                break;
            case NO_PROVIDER_POSITION:
                TrialDialog.show(this);
                break;
            default:
                Toast.makeText(context(), R.string.provider_not_chosen, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}