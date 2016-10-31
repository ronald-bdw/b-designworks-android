package com.pairup.android.utils;

import com.pairup.android.chat.ChatElement;
import com.pairup.android.chat.models.DateTime;
import com.pairup.android.chat.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class Fakes {

    public static List<ChatElement> createMessages() {
        ArrayList<ChatElement> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            if (i % 2 == 0) {
                message.setAvatar("http://i56.tinypic.com/2u61yd0.jpg");
                message.setSenderId("i");
            } else {
                message.setAvatar("https://i.imgflip.com/2/fzomi.jpg");
            }
            message.setMessage("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
            messages.add(message);
        }
        DateTime dateTime = new DateTime();
        dateTime.setTimestamp("2 August 2016");
        messages.add(5, dateTime);

        DateTime dateTime2 = new DateTime();
        dateTime2.setTimestamp("15 August 2016");
        messages.add(9, dateTime2);

        return messages;
    }
}
