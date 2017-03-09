package com.pairup.android.chat;

/**
 * Created by sergeyklymenko on 12/23/16.
 */

public interface ChatView {

    void openWelcomeScreenWithError(boolean isPhoneRegistered);

    void onError(Throwable error);
}
