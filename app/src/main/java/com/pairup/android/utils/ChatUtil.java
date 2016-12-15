package com.pairup.android.utils;

import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

import io.smooch.core.Message;
import io.smooch.core.Smooch;

/**
 * Created by Klymenko on 15.12.2016.
 */

public class ChatUtil {

    @Nullable public static Date getLastMessageDateFromCoach() {
        Message message = getLastMessageFromCoach();
        if (message != null) {
            return getLastMessageFromCoach().getDate();
        }
        return null;
    }

    @Nullable private static Message getLastMessageFromCoach() {
        List<Message> messages = Smooch.getConversation().getMessages();
        Message message = null;
        for (int i = messages.size() - 1; i >= 0; i--) {
            message = messages.get(i);
            if (!message.isFromCurrentUser()) {
                return message;
            }
        }
        return null;
    }
}
