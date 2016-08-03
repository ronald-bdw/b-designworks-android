package com.b_designworks.android.main_screen;

import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.BuildConfig;
import com.b_designworks.android.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainFragmentTest {
    MainFragment fragment;

    @Before
    public void setUp() throws Exception {
        final FragmentManager fm = Robolectric.buildActivity(BaseActivity.class)
                .create()
                .get()
                .getSupportFragmentManager();
        fragment = new MainFragment();
        fm.beginTransaction()
                .add(android.R.id.content, fragment)
                .commit();
    }

    @Test
    public void testOnCreateView() throws Exception {
        assertThat(onCreateView()).isNotNull();
    }

    private View onCreateView() {
        return fragment.onCreateView(LayoutInflater.from(application), new FrameLayout(application), null);
    }

    @Test
    public void testOnViewCreated() throws Exception {
        final Application ctx = application;
        onCreateView();
        fragment.onViewCreated(new View(ctx), null);
        assertThat(fragment.getActivity().getTitle()).isEqualTo(ctx.getString(R.string.app_name));
        assertThat(fragment.image).isNotNull();
        assertThat(fragment.hasOptionsMenu()).isTrue();
    }
}