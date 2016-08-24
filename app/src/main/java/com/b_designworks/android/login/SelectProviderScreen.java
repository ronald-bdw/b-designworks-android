package com.b_designworks.android.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.ui.UiInfo;

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
            Navigator.trialPage(context());
        } else {
            Toast.makeText(context(), "Please select provider", Toast.LENGTH_SHORT).show();
        }
    }

}
