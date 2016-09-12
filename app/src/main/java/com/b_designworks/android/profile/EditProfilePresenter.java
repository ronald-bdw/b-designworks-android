package com.b_designworks.android.profile;

import android.support.annotation.Nullable;

import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.EmailVerifier;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ilya Eremin on 9/12/16.
 */

public class EditProfilePresenter {

    private final EditProfileView editProfileView;
    private final UserInteractor  userInteractor;

    public EditProfilePresenter(EditProfileView editProfileView, UserInteractor userInteractor) {
        this.editProfileView = editProfileView;
        this.userInteractor = userInteractor;
    }

    public void showUserInfo() {
        editProfileView.showUserInfo(userInteractor.getUser());
    }

    @Nullable private Subscription updateProfileSubscribtion;

    public void updateUser() {
        if(updateProfileSubscribtion != null) return;
        editProfileView.showProgressDialog();
        String email = editProfileView.getEmail();
        if (email.isEmpty()) {
            editProfileView.showEmailError(R.string.error_empty_email);
            return;
        }
        if (!isCorrectEmailAdress(email)) {
            editProfileView.showEmailError(R.string.error_incorrect_email);
            return;
        }
        updateProfileSubscribtion = userInteractor.updateUser(editProfileView.getFirstName(), editProfileView.getLastName(), email)
            .subscribeOn(Schedulers.io())
            .doOnTerminate(() -> {
                editProfileView.hideProgress();
                updateProfileSubscribtion = null;
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(result -> {
                editProfileView.showUserInfo(result.getUser());
                editProfileView.profileHasBeenUpdated();
            }, editProfileView::showError);
    }

    private boolean isCorrectEmailAdress(String email) {
        return EmailVerifier.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    public void onScreenShown() {
        if (updateProfileSubscribtion != null) {
            editProfileView.showProgressDialog();
        }
    }

    public void onScreenHidden() {
        editProfileView.hideProgress();
    }

    public void cancelRequest() {
        if (updateProfileSubscribtion != null && !updateProfileSubscribtion.isUnsubscribed()) {
            updateProfileSubscribtion.unsubscribe();
            updateProfileSubscribtion = null;
        }
    }

}
