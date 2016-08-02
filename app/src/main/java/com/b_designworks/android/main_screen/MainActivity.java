package com.b_designworks.android.main_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new MainFragment())
                .commit();
        }

    }
}