package com.pairup.android.chat;

import android.support.annotation.NonNull;

import com.pairup.android.UserInteractor;
import com.pairup.android.utils.Rxs;

import java.util.HashMap;
import java.util.Map;

import io.smooch.core.Smooch;
import io.smooch.core.User;

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
        checkUserAuthorization();
    }

    public void checkUserAuthorization() {
        if (userInteractor.getUser() != null) {
            userInteractor.requestUserStatus(userInteractor.getPhone())
                    .compose(Rxs.doInBackgroundDeliverToUI())
                    .subscribe(result -> {
                        if (!result.isPhoneRegistered() &&
                                !result.userHasHbfProvider() &&
                                view != null) {
                            userInteractor.logout();
                            view.openWelcomeScreenWithError(result.isPhoneRegistered());
                        }
                    });
        }
    }

    public void initSmooch() {
        if (userInteractor.firstVisitAfterLogin()) {
            Smooch.logout();
            userInteractor.trackFirstVisit();
        }
        com.pairup.android.login.models.User user = userInteractor.getUser();

        Smooch.login(userInteractor.getUserZendeskId(), null);
        Map<String, Object> additionalPropertyForPushes = new HashMap<>();
        additionalPropertyForPushes.put("isNotDefaultUser", true);
        User.getCurrentUser().addProperties(additionalPropertyForPushes);
        User.getCurrentUser().setEmail(user.getEmail());
        User.getCurrentUser().setFirstName(user.getFirstName());
        User.getCurrentUser().setLastName(user.getId());
        userInteractor.sendNotificationsStatus();
    }

    public void detachView() {
        this.view = null;
    }
}
