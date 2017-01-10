package com.pairup.android.login;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by sergeyklymenko on 1/6/17.
 */

public interface SelectProviderView {

    void showProviders(@NonNull List<String> providers);

    void showProvidersLoadError();
}
