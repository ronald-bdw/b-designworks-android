package com.pairup.android.chat;

import android.support.annotation.NonNull;

import com.pairup.android.Navigator;
import com.pairup.android.UserInteractor;
import com.pairup.android.login.VerifyView;
import com.pairup.android.login.models.UserStatus;
import com.pairup.android.utils.Rxs;

import rx.functions.Action1;

/**
 * Created by sergeyklymenko on 12/23/16.
 */

public class ChatPresenter {

    private final UserInteractor userInteractor;

    private ChatView view;

    public ChatPresenter(UserInteractor userInteractor) {
        this.userInteractor = userInteractor;
    }

    public void attachView(@NonNull ChatView view) {
        this.view = view;
    }

    public void checkUserAuthorization() {
        if (userInteractor.getUser() != null) {
            userInteractor.requestUserStatus(userInteractor.getPhone())
                    .compose(Rxs.doInBackgroundDeliverToUI())
                    .subscribe(new Action1<UserStatus>() {
                        @Override public void call(UserStatus result) {
                            if (!result.isPhoneRegistered() && !result.userHasHbfProvider()) {
                                userInteractor.logout();
                               view.openWelconScreenWithError(result.isPhoneRegistered());
                            }
                        }
                    });
        }
    }
}
