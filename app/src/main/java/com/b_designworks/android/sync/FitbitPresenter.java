package com.b_designworks.android.sync;

import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.Rxs;

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

    public void handleCode(String code) {
        view.showSendingFitbitCodeProgress();
        userInteractor.integrateFitbit(code)
            .doOnTerminate(view::dismissSendingFitbitCodeProgress)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> view.fitbitSuccessfullyIntegrated(), view::showError);
    }
}
