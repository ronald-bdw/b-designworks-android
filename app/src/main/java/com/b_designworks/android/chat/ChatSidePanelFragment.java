package com.b_designworks.android.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.b_designworks.android.DI;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 9/14/16.
 */

public class ChatSidePanelFragment extends Fragment {

    private UserInteractor userInteractor = DI.getInstance().getUserInteractor();

    @Bind(R.id.avatar)    ImageView uiAvatar;
    @Bind(R.id.full_name) TextView  uiFullname;
    @Bind(R.id.email)     TextView  uiEmail;

    @Nullable @Override public View onCreateView(LayoutInflater inflater,
                                                 @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_chat_side_panel, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override public void onResume() {
        super.onResume();
        User user = userInteractor.getUser();
        ImageLoader.load(getContext(), uiAvatar, user.getAvatar().getOriginal());
        uiFullname.setText(getString(R.string.edit_profile_name_surname_pattern, user.getFirstName(), user.getLastName()));
        uiEmail.setText(user.getEmail());
    }

    @OnClick(R.id.profile) void onProfileClick() {
        Bus.event(CloseDrawerEvent.EVENT);
        Navigator.profile(getContext());
    }

    @OnClick(R.id.settings) void onSettingsClick() {
        Bus.event(CloseDrawerEvent.EVENT);
        Navigator.settings(getContext());
    }

    @OnClick(R.id.about_us) void onAboutUsClick() {
        Bus.event(CloseDrawerEvent.EVENT);
        Navigator.aboutUs(getContext());
    }
}