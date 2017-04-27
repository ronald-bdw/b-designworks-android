package com.pairup.android.chat;

/**
 * Created by sergeyklymenko on 12/23/16.
 */

public interface ChatView {

    void openWelcomeScreenWithError();

    void onError(Throwable error);
}
