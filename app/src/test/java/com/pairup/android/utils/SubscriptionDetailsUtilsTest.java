package com.pairup.android.utils;

import com.pairup.android.chat.models.SubscriptionsDetails;
import com.pairup.android.subscription.SubscriptionPresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Klymenko on 27.11.2016.
 */

public class SubscriptionDetailsUtilsTest {

    public static final String SUBSCRIPTION_NAME = "one_month_test_subscription";

    private SubscriptionsDetails subscriptionsDetails;

    @Before
    public void createSubscriptionDetails() {
        subscriptionsDetails = new SubscriptionsDetails();
        subscriptionsDetails.setPlanId(SUBSCRIPTION_NAME);
        subscriptionsDetails.setRenewing(true);
        subscriptionsDetails.setPurchaseDate(Times.now());
    }

    @Test
    public void isActiveTest() {
        Assert.assertTrue(SubscriptionDetailsUtils
            .isActive(subscriptionsDetails));
    }

    @Test
    public void isActiveTest2() {
        SubscriptionsDetails subscriptionsDetails = new SubscriptionsDetails();
        subscriptionsDetails.setPlanId(SUBSCRIPTION_NAME);
        subscriptionsDetails.setRenewing(false);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        subscriptionsDetails.setPurchaseDate(calendar.getTime().getTime());
        Assert.assertFalse(SubscriptionDetailsUtils.isActive(subscriptionsDetails));
    }

    @Test
    public void getExpiredDateTest() {
        Assert.assertNotNull(SubscriptionDetailsUtils
            .getFormattedExpiredDate(subscriptionsDetails));
    }

    @Test
    public void getExpiredDateTest2() {
        SubscriptionsDetails subscriptionsDetails = new SubscriptionsDetails();
        subscriptionsDetails.setPlanId(SubscriptionPresenter.ONE_YEAR_SUBSCRIPTION_ID);
        subscriptionsDetails.setRenewing(true);
        subscriptionsDetails.setPurchaseDate(1480336855249L);
        Assert.assertEquals(SubscriptionDetailsUtils
            .getFormattedExpiredDate(subscriptionsDetails), "2017-11-28T12:40:55Z");
    }
}