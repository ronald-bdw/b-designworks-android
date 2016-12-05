package com.pairup.android.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pairup.android.R;
import com.pairup.android.login.functional_area.Area;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by almaziskhakov on 05/12/2016.
 */

public class Areas {
    public static List<Area> getAreas(@NonNull Context context){
        List<Area> areas = new ArrayList<>();
        String[] unparsedCountries = context.getString(R.string.countries).split("\n");
        for (String unparsedCountry : unparsedCountries) {
            String[] fields = unparsedCountry.split(";");
            areas.add(new Area(fields[2], fields[0]));
        }
        return areas;
    }
}
