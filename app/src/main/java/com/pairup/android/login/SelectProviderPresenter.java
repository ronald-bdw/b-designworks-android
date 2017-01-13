package com.pairup.android.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pairup.android.UserInteractor;
import com.pairup.android.login.models.Provider;
import com.pairup.android.utils.Rxs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sergeyklymenko on 1/6/17.
 */

public class SelectProviderPresenter {

    private static final String HBF_PROVIDER = "HBF";
    private final UserInteractor userInteractor;

    @Nullable
    private SelectProviderView view;

    public SelectProviderPresenter(@NonNull UserInteractor userInteractor) {
        this.userInteractor = userInteractor;
    }

    public void attachView(@NonNull SelectProviderView view) {
        this.view = view;
    }

    public void fetchProviders() {
        userInteractor.getProviders()
                .compose(Rxs.doInBackgroundDeliverToUI())
                .subscribe(result -> {
                    List<String> providers = new ArrayList<>();

                    for (Provider provider : result.getProviders()) {
                        providers.add(provider.getName());
                    }
                    sortProviders(providers);
                    if (providers.contains(HBF_PROVIDER)) {
                        providers.remove(providers.indexOf(HBF_PROVIDER));
                        providers.add(0, HBF_PROVIDER);
                    }
                    if (view != null) {
                        view.showProviders(providers);
                    }
                }, exception -> {
                    if (view != null) {
                        view.showProvidersLoadError();
                    }
                });
    }

    public void detachView() {
        this.view = null;
    }

    private void sortProviders(List<String> providers) {
        Collections.sort(providers, (provider, t1) -> provider.compareTo(t1));
    }
}
