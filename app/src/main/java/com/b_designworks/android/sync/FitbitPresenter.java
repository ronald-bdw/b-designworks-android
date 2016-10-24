package com.b_designworks.android.sync;

import android.support.annotation.Nullable;

import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.Integration;
import com.b_designworks.android.utils.Logger;
import com.b_designworks.android.utils.Rxs;
import com.google.android.gms.auth.api.Auth;

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
            .compose(Rxs.doInBackgroundDeliverToUI())
            .doOnTerminate(view::dismissSendingFitbitCodeProgress)
            .subscribe(fitToken -> userInteractor.saveFitBitTokenLocally(fitToken.getId()), view::onError);
    }


    private @Nullable String getFitBitTokenId() {
        String id = null;
        for (Integration integration : userInteractor.getUser().getIntegrations()) {
            if (integration.getName().equals("Fitbit")) {
                id = integration.getFitnessTokenId();
            }
        }
        return id;
    }

    public void logout() {
        String tokenId = getFitBitTokenId();
        if (tokenId!=null) {
            userInteractor.removeFitnessToken(tokenId);
        }
    }

}
