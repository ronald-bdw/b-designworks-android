package com.pairup.android.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentActivity;

import com.pairup.android.R;
import com.pairup.android.login.functional_area.Area;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by almaziskhakov on 05/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class AreasTest {

    @Mock Context mockedContext;

    @Test public void getAreasTest() throws Exception {
        when(mockedContext.getString(R.string.countries)).thenReturn("" +
            "44;GB;United Kingdom;XXXX XXXXXX\n" +
            "20;EG;Egypt;XX XXXX XXXX\n" +
            "7;RU;Russian Federation;XXX XXX XXXX\n" +
            "1;PR;Puerto Rico;XXX XXX XXXX\n" +
            "235;TD;Chad;XX XX XX XX\n" +
            "234;NG;Nigeria\n" +
            "1869;KN;Saint Kitts &amp;amp; Nevis;XXX XXXX\n" +
            "1868;TT;Trinidad &amp; Tobago;XXX XXXX\n" +
            "1784;VC;Saint Vincent &amp; the Grenadines;XXX XXXX\n" +
            "1;US;USA;XXX XXX XXXX");

        List<Area> areas = Areas.getAreas(mockedContext);

        Assert.assertEquals(areas.get(0).getCode().trim(), "44");
        Assert.assertEquals(areas.get(0).getCountry().trim(), "United Kingdom");

        Assert.assertEquals(areas.get(1).getCode().trim(), "20");
        Assert.assertEquals(areas.get(1).getCountry().trim(), "Egypt");

        Assert.assertEquals(areas.get(2).getCode().trim(), "7");
        Assert.assertEquals(areas.get(2).getCountry().trim(), "Russian Federation");

        Assert.assertEquals(areas.get(3).getCode().trim(), "1");
        Assert.assertEquals(areas.get(3).getCountry().trim(), "Puerto Rico");

        Assert.assertEquals(areas.get(4).getCode().trim(), "235");
        Assert.assertEquals(areas.get(4).getCountry().trim(), "Chad");

        Assert.assertEquals(areas.get(5).getCode().trim(), "234");
        Assert.assertEquals(areas.get(5).getCountry().trim(), "Nigeria");

        Assert.assertEquals(areas.get(6).getCode().trim(), "1869");
        Assert.assertEquals(areas.get(6).getCountry().trim(), "Saint Kitts &amp");

        Assert.assertEquals(areas.get(7).getCode().trim(), "1868");
        Assert.assertEquals(areas.get(7).getCountry().trim(), "Trinidad &amp");

        Assert.assertEquals(areas.get(8).getCode().trim(), "1784");
        Assert.assertEquals(areas.get(8).getCountry().trim(), "Saint Vincent &amp");

        Assert.assertEquals(areas.get(9).getCode().trim(), "1");
        Assert.assertEquals(areas.get(9).getCountry().trim(), "USA");
    }
}
