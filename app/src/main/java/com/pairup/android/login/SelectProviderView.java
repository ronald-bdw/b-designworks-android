package com.pairup.android.login;

import java.util.List;

/**
 * Created by sergeyklymenko on 1/6/17.
 */

public interface SelectProviderView {

    void showProviders(List<String> providers);

    void closeScreen();
}
