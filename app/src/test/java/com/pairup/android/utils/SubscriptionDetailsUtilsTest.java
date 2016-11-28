package com.pairup.android.utils;

import com.pairup.android.TestApp;
import com.pairup.android.chat.models.SubscriptionsDetails;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Klymenko on 27.11.2016.
 */

public class SubscriptionDetailsUtilsTest {

    public static final String SUBSCRIPTION_NAME = "one_month_test_subscription";
    public static final long SUBSCRIPTION_TEST_PURCHASE_DATE = 1480336855249L;
    public static final String SUBSCRIPTION_PURCHASE_DATE_STAMP = "2016-12-28T14:40:55Z";

    SubscriptionsDetails subscriptionsDetails;

    @Before
    public void createSubscriptionDetails() {
        subscriptionsDetails = new SubscriptionsDetails();
        subscriptionsDetails.setPlanName(SUBSCRIPTION_NAME);
        subscriptionsDetails.setRenewing(true);
        subscriptionsDetails.setPurchaseDate(Times.now());
    }

    @Test
    public void isActiveTest() {
        Assert.assertTrue(SubscriptionDetailsUtils.isActive(subscriptionsDetails.isRenewing(), subscriptionsDetails.getPurchaseDate()));
    }

    @Test
    public void isActiveTest2() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(subscriptionsDetails.getPurchaseDate()));
        calendar.add(Calendar.MONTH, -2);
        Assert.assertFalse(SubscriptionDetailsUtils.isActive(false, calendar.getTime().getTime()));
    }

    @Test
    public void getExpiredDateTest() {
        Assert.assertNotNull(SubscriptionDetailsUtils.getExpiredDate(subscriptionsDetails.getPurchaseDate()));
    }

    @Test
    public void getExpiredDateTest2() {
        Assert.assertTrue(TestApp.isValidDate(SubscriptionDetailsUtils.getExpiredDate(SUBSCRIPTION_TEST_PURCHASE_DATE), SUBSCRIPTION_PURCHASE_DATE_STAMP));
    }
}