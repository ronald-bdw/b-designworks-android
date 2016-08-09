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
import com.b_designworks.android.utils.UiInfo;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class SelectProviderScreen extends BaseActivity {

    String[] data = {"one", "two", "three", "four", "five"};

    @Bind(R.id.select_provider_spinner) Spinner uiSelectProviderSpinner;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_select_provider);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        uiSelectProviderSpinner.setAdapter(adapter);
        uiSelectProviderSpinner.setPrompt("Title");
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
        if (uiSelectProviderSpinner.getSelectedItemPosition() >= 0) {
            Navigator.verification(context());
        } else {
            Toast.makeText(SelectProviderScreen.this, "Please select provider", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.not_listed) void onNotListedClick() {
        Navigator.trialPage(context());
    }
}
