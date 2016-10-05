package com.b_designworks.android.login;


import com.b_designworks.android.login.functional_area.Area;

/**
 * Created by Ilya Eremin on 10/5/16.
 */
public class OnAreaSelectedEvent {
    private final Area area;

    public OnAreaSelectedEvent(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }
}
