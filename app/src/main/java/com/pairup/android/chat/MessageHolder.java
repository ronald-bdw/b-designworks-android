package com.pairup.android.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pairup.android.R;
import com.pairup.android.chat.models.Message;
import com.pairup.android.utils.AndroidUtils;
import com.pairup.android.utils.ImageLoader;
import com.pairup.android.utils.ui.BaseHolder;

import butterknife.Bind;
import rx.functions.Func1;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class MessageHolder extends BaseHolder<Message> {

    public static final Func1<ViewGroup, MessageHolder> CREATOR = viewGroup
        -> new MessageHolder(inflate(viewGroup, R.layout.item_message));

    @Bind(R.id.avatar)            ImageView uiAvatar;
    @Bind(R.id.message)           TextView  uiMessage;
    @Bind(R.id.message_container) View      uiMessageContainer;

    public MessageHolder(View itemView) {
        super(itemView);
    }

    @Override public void draw(Message message) {
        LinearLayout uiRoot = (LinearLayout) this.itemView;
        uiRoot.removeView(uiAvatar);
        RecyclerView.LayoutParams layoutParams =
            (RecyclerView.LayoutParams) uiRoot.getLayoutParams();
        LinearLayout.LayoutParams msgParams =
            (LinearLayout.LayoutParams) uiMessageContainer.getLayoutParams();

        if ("i".equals(message.getSenderId())) {
            uiRoot.addView(uiAvatar);
            msgParams.rightMargin = AndroidUtils.dp(8);
            msgParams.leftMargin = 0;
            layoutParams.rightMargin = AndroidUtils.dp(16);
            layoutParams.leftMargin = AndroidUtils.dp(32);
            uiMessage
                .setBackgroundColor(context().getResources().getColor(R.color.coach_message_bg));
        } else {
            uiRoot.addView(uiAvatar, 0);
            msgParams.leftMargin = AndroidUtils.dp(8);
            msgParams.rightMargin = 0;
            layoutParams.rightMargin = AndroidUtils.dp(32);
            layoutParams.leftMargin = AndroidUtils.dp(16);
            uiMessage.setBackgroundColor(context().getResources().getColor(R.color.my_message_bg));
        }
        uiRoot.setLayoutParams(layoutParams);
        uiMessageContainer.setLayoutParams(msgParams);

        ImageLoader.load(context(), uiAvatar, message.getAvatar());
        uiMessage.setText(message.getMessage());
    }
}
