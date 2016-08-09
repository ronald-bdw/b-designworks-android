package com.b_designworks.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.b_designworks.android.utils.Keyboard;
import com.b_designworks.android.utils.UiInfo;
import com.f2prateek.dart.Dart;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public static void start(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public abstract @NonNull UiInfo getUiInfo();

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Dart.inject(this);
        setContentView(getUiInfo().getLayoutRes());
        ButterKnife.bind(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            parseArguments(getIntent().getExtras());
        }
        View toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar((Toolbar) toolbar);
            if (getUiInfo().getTitleRes() != 0) {
                setTitle(getUiInfo().getTitleRes());
            } else if (getUiInfo().getTitle() != null) {
                setTitle(getUiInfo().getTitle());
            }
            if (getUiInfo().isHasBackButton()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        if (savedState != null) {
            restoreState(savedState);
        }
    }

    protected void restoreState(@NonNull Bundle savedState) {}

    protected void parseArguments(@NonNull Bundle extras) {}

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        if (getUiInfo().getMenuRes() != 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(getUiInfo().getMenuRes(), menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && getUiInfo().isHasBackButton()) {
            Keyboard.hide(this);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() {
        Keyboard.hide(this);
        super.onBackPressed();
    }

    protected Context context(){
        return this;
    }

    @Override public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
}
