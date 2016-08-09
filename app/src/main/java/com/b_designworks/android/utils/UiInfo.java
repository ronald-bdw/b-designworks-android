package com.b_designworks.android.utils;

import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class UiInfo {
    private final @LayoutRes int     layoutRes;
    private @StringRes       int     titleRes;
    private                  String  titleStr;
    private                  boolean hasBackButton;
    private @MenuRes         int     menuRes;

    public int getMenuRes() {
        return menuRes;
    }

    public UiInfo setMenuRes(int menuRes) {
        this.menuRes = menuRes;
        return this;
    }

    public UiInfo(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public int getLayoutRes() {
        return layoutRes;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public UiInfo setTitleRes(int titleRes) {
        this.titleRes = titleRes;
        return this;
    }

    public UiInfo setTitle(@NonNull String title) {
        this.titleStr = title;
        return this;
    }

    public String getTitle(){
        return titleStr;
    }

    public boolean isHasBackButton() {
        return hasBackButton;
    }

    public UiInfo enableBackButton() {
        this.hasBackButton = true;
        return this;
    }
}
