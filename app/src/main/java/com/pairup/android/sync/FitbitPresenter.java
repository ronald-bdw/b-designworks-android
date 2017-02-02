package com.pairup.android.sync;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pairup.android.UserInteractor;
import com.pairup.android.sync.models.Provider;
import com.pairup.android.utils.Rxs;

/**
 * Created by Ilya Eremin on 9/26/16.
 */
public class FitbitPresenter {

    @Nullable private FitbitView     view;
    private final     UserInteractor userInteractor;

    public FitbitPresenter(UserInteractor userInteractor) {
        this.userInteractor = userInteractor;
    }

    public void attach(@NonNull FitbitView view) {
        this.view = view;
    }

    public void handleCode(String code) {
        view.showSendingFitbitCodeProgress();
        userInteractor.integrateFitnessApp(code, Provider.FITBIT)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .doOnTerminate(() -> {
                if (view != null) {
                    view.dismissSendingFitbitCodeProgress();
                }
            })
            .subscribe(fitToken -> userInteractor
                .saveFitnessTokenLocally(fitToken.getId(), Provider.FITBIT), error -> {
                    if (view != null) {
                        view.onError(error);
                    }
                });
    }

    public void detach() {
        view = null;
    }
}
