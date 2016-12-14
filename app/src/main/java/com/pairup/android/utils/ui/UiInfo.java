package com.pairup.android.utils.ui;

import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class UiInfo {

    @LayoutRes private final int     layoutRes;

    @MenuRes private         int     menuRes;

    @StringRes private       int     titleRes;

    private                  String  titleStr;
    private                  boolean hasBackButton;

    public UiInfo(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public UiInfo setTitle(@NonNull String title) {
        this.titleStr = title;
        return this;
    }

    public UiInfo setMenuRes(int menuRes) {
        this.menuRes = menuRes;
        return this;
    }

    public UiInfo setTitleRes(int titleRes) {
        this.titleRes = titleRes;
        return this;
    }

    public UiInfo enableBackButton() {
        this.hasBackButton = true;
        return this;
    }

    public int getMenuRes() {
        return menuRes;
    }

    public int getLayoutRes() {
        return layoutRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public String getTitle() {
        return titleStr;
    }

    public boolean isHasBackButton() {
        return hasBackButton;
    }
}