package com.pairup.android.profile;

import android.support.annotation.Nullable;

import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.utils.EmailVerifier;
import com.pairup.android.utils.Rxs;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ilya Eremin on 9/12/16.
 */

public class EditProfilePresenter {

    @Nullable private EditProfileView view;
    @Nullable private Subscription    updateProfileSubscribtion;
    @Nullable private Subscription    uploadingSubscription;

    private final UserInteractor userInteractor;

    public EditProfilePresenter(UserInteractor userInteractor) {
        this.userInteractor = userInteractor;
    }

    public void attachView(EditProfileView view) {
        this.view = view;
    }

    public void showUserInfo() {
        if (view != null) {
            view.showUserInfo(userInteractor.getUser());
        }
    }

    public void updateUser() {
        if (updateProfileSubscribtion != null) return;
        String email = null;
        if (view != null) {
            email = view.getEmail();
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
            updateProfileSubscribtion = userInteractor
                .updateUser(view.getFirstName(), view.getLastName(), email)
                .subscribeOn(Schedulers.io())
                .doOnTerminate(() -> {
                    view.hideProgress();
                    updateProfileSubscribtion = null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    view.showUserInfo(result.getUser());
                    view.profileHasBeenUpdated();
                    view.closeEditor();
                }, view::showError);
        }
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
        if (view != null) {
            view.hideProgress();
        }
    }

    public void cancelRequest() {
        if (updateProfileSubscribtion != null && !updateProfileSubscribtion.isUnsubscribed()) {
            updateProfileSubscribtion.unsubscribe();
            updateProfileSubscribtion = null;
        }
    }

    public void detachView() {
        view = null;
    }

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
        if (view != null) {
            view.showAvatar(userInteractor.getUser().getAvatar().getThumb());
        }
    }
}
