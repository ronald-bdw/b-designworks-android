package com.pairup.android.chat;

/**
 * Created by sergeyklymenko on 12/23/16.
 */

public interface ChatView {

    void openWelconScreenWithError(boolean isPhoneRegistered);

    void initSidePanel();
}
