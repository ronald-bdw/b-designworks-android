package com.b_designworks.android.profile;

import android.support.annotation.Nullable;

import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.EmailVerifier;
import com.b_designworks.android.utils.Rxs;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ilya Eremin on 9/12/16.
 */

public class EditProfilePresenter {

    @Nullable private EditProfileView view;

    private final UserInteractor userInteractor;

    public EditProfilePresenter(UserInteractor userInteractor) {
        this.userInteractor = userInteractor;
    }

    public void attachView(EditProfileView view) {
        this.view = view;
    }

    public void showUserInfo() {
        view.showUserInfo(userInteractor.getUser());
    }

    @Nullable private Subscription updateProfileSubscribtion;

    public void updateUser() {
        if (updateProfileSubscribtion != null) return;
        String email = view.getEmail();
        if (email.isEmpty()) {
            view.showEmailError(R.string.error_empty_email);
            return;
        }
        if (!isCorrectEmailAdress(email)) {
            view.showEmailError(R.string.error_incorrect_email);
            return;
        }
        view.showProgressDialog();
        view.hideKeyboard();
        updateProfileSubscribtion = userInteractor.updateUser(view.getFirstName(), view.getLastName(), email)
            .subscribeOn(Schedulers.io())
            .doOnTerminate(() -> {
                view.hideProgress();
                updateProfileSubscribtion = null;
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(result -> {
                view.showUserInfo(result.getUser());
                view.profileHasBeenUpdated();
            }, view::showError);
    }

    private boolean isCorrectEmailAdress(String email) {
        return EmailVerifier.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    public void onScreenShown() {
        if (updateProfileSubscribtion != null) {
            if (view != null) {
                view.showProgressDialog();
            }
        }
    }

    public void onScreenHidden() {
        view.hideProgress();
    }

    public void cancelRequest() {
        if (updateProfileSubscribtion != null && !updateProfileSubscribtion.isUnsubscribed()) {
            updateProfileSubscribtion.unsubscribe();
            updateProfileSubscribtion = null;
        }
    }

    public void detachView() {
        this.view = null;
    }

    @Nullable private Subscription uploadingSubscription;

    public void updateAvatar(@Nullable String imageUrl) {
        if (view != null) {
            view.showAvatar(imageUrl);
            view.showAvatarUploadingProgress();
        }
        if (uploadingSubscription != null) {
            uploadingSubscription.unsubscribe();
        }
        uploadingSubscription = userInteractor.uploadAvatar(imageUrl)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                view.avatarSuccessfullyUploaded();
                view.showAvatar(result.getUser().getAvatar().getThumb());
            }, error -> {
                view.showUploadAvatarError(imageUrl);
            });
    }

    public void userCancelAvatarUploading() {
        view.showAvatar(userInteractor.getUser().getAvatar().getThumb());
    }
}
