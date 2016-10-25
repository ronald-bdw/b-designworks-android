package com.b_designworks.android.sync;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.Integration;
import com.b_designworks.android.utils.Rxs;

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
        userInteractor.integrateFitbit(code)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .doOnTerminate(() -> {
                if (view != null) {
                    view.dismissSendingFitbitCodeProgress();
                }
            })
            .subscribe(fitToken -> userInteractor.saveFitnessTokenLocally(fitToken.getId(), Provider.FITBIT),
                error -> {
                    if (view != null) {
                        view.onError(error);
                    }
                });
    }

    public void detach() {
        view = null;
    }
}
