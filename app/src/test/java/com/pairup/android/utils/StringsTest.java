package com.pairup.android.utils;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Ilya Eremin on 19.08.2016.
 */
public class StringsTest {

    @Test
    public void testListToString() throws Exception {
        Assert.assertEquals("haha, hoho, hihi", Strings.listToString(Arrays.asList("haha",
            "hoho", "hihi")));
        Assert.assertEquals("ho", Strings.listToString(Arrays.asList("ho")));
        Assert.assertEquals(", , ", Strings.listToString(Arrays.asList("", "", "")));
    }
}