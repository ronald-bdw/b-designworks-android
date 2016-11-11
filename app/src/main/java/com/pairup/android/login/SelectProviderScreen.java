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
import com.pairup.android.utils.ui.UiInfo;
import com.pairup.android.login.TrialDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class SelectProviderScreen extends BaseActivity{

    @Bind(R.id.select_provider_spinner) Spinner uiSelectProviderSpinner;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_select_provider);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.select_provider_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        if (uiSelectProviderSpinner.getSelectedItemPosition() == 0) {
            Navigator.enterPhone(context());
        } else if (uiSelectProviderSpinner.getSelectedItemPosition() == 1) {
            TrialDialog.show(this);
        } else {
            Toast.makeText(context(), "Please select provider", Toast.LENGTH_SHORT).show();
        }
    }

}
