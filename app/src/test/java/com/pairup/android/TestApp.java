package com.pairup.android;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class TestApp extends App {

    @Override protected void setUpServices() {
    }

    public static boolean isValidDate(String result, String demandResult) {
        return result.equals(demandResult);
    }
}
