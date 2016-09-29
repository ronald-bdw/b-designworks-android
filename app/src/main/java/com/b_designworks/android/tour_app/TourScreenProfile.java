package com.b_designworks.android.tour_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;

import static com.b_designworks.android.utils.ui.TextViews.textOf;

/**
 * Created by Ilya Eremin on 9/28/16.
 */

public class TourScreenProfile extends BaseActivity {

    @Bind(R.id.first_name) TextView uiFirstName;
    @Bind(R.id.last_name)  TextView uiLastName;
    @Bind(R.id.email)      TextView uiEmail;

    @Inject UserInteractor userInteractor;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_tour_profile)
            .setTitleRes(R.string.title_tour_1_page)
            .setMenuRes(R.menu.menu_with_next_btn);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        User user = userInteractor.getUser();
        uiFirstName.setText(user.getFirstName());
        uiLastName.setText(user.getLastName());
        uiEmail.setText(user.getEmail());
    }

    @Nullable private Subscription   updateProfileSubs;
    @Nullable private ProgressDialog updateProfileProgress;

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.next) {
            if (updateProfileSubs != null) return true;
            sendInfoAndMoveToNextScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendInfoAndMoveToNextScreen() {
        if (fieldsChanged()) {
            showUpdateProfileProgress();
            updateProfileSubs = userInteractor.updateUser(textOf(uiFirstName), textOf(uiLastName), textOf(uiEmail))
                .doOnTerminate(() -> {
                    updateProfileSubs = null;
                    hideProgress();
                })
                .compose(Rxs.doInBackgroundDeliverToUI())
                .subscribe(result -> {
                    Toast.makeText(context(), R.string.edit_profile_profile_updated, Toast.LENGTH_SHORT).show();
                    Navigator.tourUploadAvatar(context());
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
        updateProfileProgress = ProgressDialog.show(context(), getString(R.string.loading), getString(R.string.loading_user_info));
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

}