package com.pairup.android.trial;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.login.AccountVerificationType;
import com.pairup.android.utils.ui.UiInfo;
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
        uiPager.setAdapter(new TrialPager(context()));
        uiPagerIndicator.setViewPager(uiPager);
    }

    @OnClick(R.id.start_trial_now) void onSignUpClick() {
        Navigator.enterPhone(context(), AccountVerificationType.IS_NOT_REGISTERED);
    }

    public static class TrialPager extends PagerAdapter {

        private final String[] titles;
        private final String[] descriptions;

        TrialPager(@NonNull Context context) {
            titles = context.getResources().getStringArray(R.array.trial_titles);
            descriptions = context.getResources().getStringArray(R.array.trial_descriptions);
        }

        @DrawableRes int[] images = {
            R.drawable.trial_1, R.drawable.trial_2,
            R.drawable.trial_3, R.drawable.trial_4,
        };

        @Override public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_trial, container, false);
            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView description = (TextView) view.findViewById(R.id.description);
            description.setGravity(Gravity.CENTER);
            ImageView uiImage = (ImageView) view.findViewById(R.id.image);
            titleView.setText(titles[position]);
            description.setText(descriptions[position]);
            uiImage.setImageResource(images[position]);
            container.addView(view, 0);
            return view;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override public int getCount() {
            return titles.length;
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
