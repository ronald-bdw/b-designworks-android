package com.pairup.android.tour_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.login.models.User;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import rx.Subscription;

import static com.pairup.android.utils.ui.TextViews.textOf;

/**
 * Created by Ilya Eremin on 9/28/16.
 */

public class TourScreenProfile extends BaseActivity{

    @Bind(R.id.first_name) TextView uiFirstName;
    @Bind(R.id.last_name)  TextView uiLastName;
    @Bind(R.id.email)      TextView uiEmail;

    @Inject UserInteractor     userInteractor;

    @Nullable private Subscription   updateProfileSubs;
    @Nullable private ProgressDialog updateProfileProgress;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_tour_profile)
            .setTitleRes(R.string.title_tour_1_page)
            .setMenuRes(R.menu.menu_with_next_btn);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);

        Analytics.logScreenOpened(Analytics.EVENT_OPEN_TOUR_PROFILE_SCREEN);

        User user = userInteractor.getUser();
        uiFirstName.setText(user.getFirstName());
        uiLastName.setText(user.getLastName());
        uiEmail.setText(user.getEmail());
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.next) {
            if (updateProfileSubs != null) return true;
            sendInfoAndMoveToNextScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendInfoAndMoveToNextScreen() {
        Keyboard.hide(this);
        userInteractor.setShowTourForUser();
        if (fieldsChanged()) {
            showUpdateProfileProgress();
            updateProfileSubs = userInteractor
                .updateUser(textOf(uiFirstName), textOf(uiLastName), textOf(uiEmail))
                .doOnTerminate(() -> {
                    updateProfileSubs = null;
                    hideProgress();
                    Navigator.tourUploadAvatar(context());
                })
                .compose(Rxs.doInBackgroundDeliverToUI())
                .subscribe(result -> {
                    Toast.makeText(context(), R.string.edit_profile_profile_updated,
                        Toast.LENGTH_SHORT).show();
                }, ErrorUtils.handle(context()));
        } else {
            Navigator.tourUploadAvatar(context());
        }
    }

    private void hideProgress() {
        if (updateProfileProgress != null) {
            updateProfileProgress.dismiss();
        }
    }

    private void showUpdateProfileProgress() {
        hideProgress();
        updateProfileProgress = ProgressDialog
            .show(context(), getString(R.string.loading), getString(R.string.loading_user_info));
        updateProfileProgress.setOnDismissListener(dialog -> {
            if (updateProfileSubs != null && !updateProfileSubs.isUnsubscribed()) {
                updateProfileSubs.unsubscribe();
            }
        });
    }

    private boolean fieldsChanged() {
        User user = userInteractor.getUser();
        return !textOf(uiFirstName).equals(user.getFirstName()) ||
            !textOf(uiLastName).equals(user.getLastName()) ||
            !textOf(uiEmail).equals(user.getEmail());
    }

    @OnClick(R.id.next) void onSubmitClick() {
        sendInfoAndMoveToNextScreen();
    }

    @OnEditorAction(R.id.email) boolean onEnterClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onSubmitClick();
            return true;
        }
        return false;
    }
}