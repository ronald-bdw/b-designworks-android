package com.b_designworks.android.trial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.ui.UiInfo;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class TrialScreen extends BaseActivity {

    @Bind(R.id.pager) ViewPager           uiPager;
    @Bind(R.id.dots)  CirclePageIndicator uiPagerIndicator;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_trial).setTitleRes(R.string.app_name).enableBackButton();
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        uiPager.setAdapter(new TrialPager());
        uiPagerIndicator.setViewPager(uiPager);
    }

    @OnClick(R.id.start_trial_now) void onSignUpClick() {
        Navigator.registration(context());
    }

    public static class TrialPager extends PagerAdapter {

        String[] titles = new String[]{
            "Welcome to Pearup!", "Title 2", "Title 3", "Title 4"
        };

        String[] descriptions = new String[]{
            "Some text about the App... Messenger bag pop-up bicycle rights, brunch cliche freegan vice mixtape wayfarers gentrify kickstarter.",
            "Description 2", "Description 3", "Description 4"
        };

        @Override public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_trial, container, false);
            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView description = (TextView) view.findViewById(R.id.description);
            titleView.setText(titles[position]);
            description.setText(descriptions[position]);
            container.addView(view, 0);
            return view;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override public int getCount() {
            return 4;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
