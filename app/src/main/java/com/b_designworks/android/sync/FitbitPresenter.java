package com.b_designworks.android.sync;

import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.Rxs;

import rx.Subscription;

/**
 * Created by Ilya Eremin on 9/26/16.
 */
public class FitbitPresenter {

    private final FitbitView     view;
    private final UserInteractor userInteractor;


    public FitbitPresenter(FitbitView view, UserInteractor userInteractor) {
        this.view = view;
        this.userInteractor = userInteractor;
    }

    private Subscription sendingFitbitCodeSubs;

    public void handleCode(String code) {
        view.showSendingFitbitCodeProgress();
        sendingFitbitCodeSubs = userInteractor.integrateFitbit(code)
            .doOnTerminate(() -> {
                sendingFitbitCodeSubs = null;
                view.dismissSendingFitbitCodeProgress();
            })
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> view.fitbitSuccessfullyIntegrated(), view::showError);
    }
}
