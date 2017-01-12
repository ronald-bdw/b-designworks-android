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
                    providers.add("HBF");
                    sortProviders(result.getProviders());
                    for (Provider provider : result.getProviders()) {
                        if (!"HBF".equals(provider.getName())) {
                            providers.add(provider.getName());
                        }
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

    private void sortProviders(List<Provider> providers) {
        Collections.sort(providers, (provider, t1) -> provider.getName().compareTo(t1.getName()));
    }
}
