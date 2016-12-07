package com.pairup.android.tour_app;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.pairup.android.sync.GoogleFitPresenter;
import com.pairup.android.sync.GoogleFitView;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.Logger;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.ui.UiInfo;
import com.google.android.gms.common.ConnectionResult;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import rx.Subscription;

import static com.pairup.android.utils.ui.TextViews.textOf;

/**
 * Created by Ilya Eremin on 9/28/16.
 */

public class TourScreenProfile extends BaseActivity implements GoogleFitView {

    @Bind(R.id.first_name) TextView uiFirstName;
    @Bind(R.id.last_name)  TextView uiLastName;
    @Bind(R.id.email)      TextView uiEmail;

    @Inject UserInteractor     userInteractor;
    @Inject GoogleFitPresenter googleFitPresenter;

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
        googleFitPresenter.attachView(this, this);
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
        userInteractor.setShowTourForUser();
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
                    integrateGoogleFit();
                }, ErrorUtils.handle(context()));
        } else {
            integrateGoogleFit();
        }
    }

    private void integrateGoogleFit() {
        googleFitPresenter.startIntegrate(this);
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

    @Override public void codeRetrievedSuccessfull() {
        Toast.makeText(this, R.string.google_fit_token_retrieved, Toast.LENGTH_SHORT).show();
        Navigator.tourUploadAvatar(context());
    }

    @Override public void errorWhileRetrievingCode() {
        Logger.dToast(this, "error while retrieving code");
    }

    @Override public void onGoogleServicesError(ConnectionResult result) {
        Logger.dToast(this, "onGoogleServicesError: " + result.getErrorMessage());
    }

    @Override public void showInternetConnectionError() {
        Logger.dToast(this, "showInternetConnectionError: ");
    }

    @Override public void showGoogleServiceDisconected() {
        Logger.dToast(this, "showGoogleServiceDisconected");
    }

    @Override public void onError(Throwable error) {
        Logger.dToast(this, "On error" + error.getMessage());
    }

    @Override public void userCancelIntegration() {
        Logger.dToast(context(), "User canceled google git integration");
        Navigator.tourUploadAvatar(context());
    }

    @Override protected void onDestroy() {
        googleFitPresenter.detachView(this);
        super.onDestroy();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleFitPresenter.handleResponse(requestCode, resultCode, data);
    }

    @OnEditorAction(R.id.email) boolean onEnterClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Keyboard.hide(this);
            onSubmitClick();
            return true;
        }
        return false;
    }
}