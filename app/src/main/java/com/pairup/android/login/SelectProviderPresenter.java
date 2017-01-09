package com.pairup.android.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pairup.android.UserInteractor;
import com.pairup.android.login.models.Provider;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.network.ErrorUtils;

import java.util.ArrayList;
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
                    for (Provider provider : result.getProviders()) {
                        providers.add(provider.getName());
                    }
                    if (view != null) {
                        view.showProviders(providers);
                    }
                }, exception -> {
                    ErrorUtils.onError();
                    if (view != null) {
                        view.closeScreen();
                    }
                });
    }

    public void detachView() {
        this.view = null;
    }
}
