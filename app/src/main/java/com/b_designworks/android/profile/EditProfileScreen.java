package com.b_designworks.android.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.DI;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.ImageLoader;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.SimpleLoadingDialog;
import com.b_designworks.android.utils.ui.TextViews;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.Bind;
import rx.Subscription;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class EditProfileScreen extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_edit_profile)
            .enableBackButton()
            .setTitleRes(R.string.title_edit_profile)
            .setMenuRes(R.menu.edit_profile);
    }

    @Bind(R.id.avatar)            ImageView uiAvatar;
    @Bind(R.id.current_full_name) TextView  uiCurrentFullName;
    @Bind(R.id.current_email)     TextView  uiCurrentEmail;
    @Bind(R.id.first_name)        EditText  uiFirstName;
    @Bind(R.id.last_name)         EditText  uiLastName;
    @Bind(R.id.email)             EditText  uiEmail;

    private final UserInteractor userInteractor = DI.getInstance().getUserInteractor();

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        showUserInfo();
        ImageLoader.load(context(), uiAvatar, userInteractor.getAvatarThumbUrl());
    }

    private void showUserInfo() {
        uiCurrentFullName.setText(userInteractor.getFirstName() + " " + userInteractor.getLastName());
        uiCurrentEmail.setText(userInteractor.getEmail());
        uiFirstName.setText(userInteractor.getFirstName());
        uiLastName.setText(userInteractor.getLastName());
        uiEmail.setText(userInteractor.getEmail());
    }

    @Nullable private ProgressDialog progressDialog;
    @Nullable private Subscription   updateProfileSubscribtion;

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save && updateProfileSubscribtion == null) {
            showProgressDialog();
            updateProfileSubscribtion = userInteractor.updateUser(TextViews.textOf(uiFirstName), TextViews.textOf(uiLastName), TextViews.textOf(uiEmail))
                .compose(Rxs.doInBackgroundDeliverToUI())
                .doOnEach(i -> {
                    hideProgress();
                    updateProfileSubscribtion = null;
                })
                .subscribe(result -> {
                    showUserInfo();
                    Toast.makeText(this, R.string.edit_profile_profile_updated, Toast.LENGTH_SHORT).show();
                }, ErrorUtils.handle(context()));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (updateProfileSubscribtion != null) {
            showProgressDialog();
        }
    }

    private void showProgressDialog() {
        hideProgress();
        progressDialog = SimpleLoadingDialog.show(context(), getString(R.string.loading_user_info), () -> {
            if (updateProfileSubscribtion != null && !updateProfileSubscribtion.isUnsubscribed()) {
                updateProfileSubscribtion.unsubscribe();
                updateProfileSubscribtion = null;
            }
        });
    }

    @Override protected void onPause() {
        hideProgress();
        super.onPause();
    }
}
