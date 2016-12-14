package com.pairup.android.profile;

import com.pairup.android.R;
import com.pairup.android.RxSchedulersOverrideRule;
import com.pairup.android.UserInteractor;
import com.pairup.android.login.models.User;
import com.pairup.android.login.models.UserResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Ilya Eremin on 9/12/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class EditProfilePresenterTest {

    private static final String CORRECT_EMAIL = "correct@email.com";

    @Rule public RxSchedulersOverrideRule rxRule = new RxSchedulersOverrideRule();

    @Mock private EditProfileView editProfileView;
    @Mock private UserInteractor  userInteractor;

    private EditProfilePresenter editProfilePresenter;

    @Before public void setUp() throws Exception {
        editProfilePresenter = new EditProfilePresenter(userInteractor);
        editProfilePresenter.attachView(editProfileView);
    }

    @Test
    public void testUpdateUserIncorrectEmail() throws Exception {
        when(editProfileView.getEmail()).thenReturn("cococo");
        editProfilePresenter.updateUser();
        verify(editProfileView).showEmailError(R.string.error_incorrect_email);
        verify(editProfileView, never()).showProgressDialog();
        verify(userInteractor, never()).updateUser(any(), any(), any());
    }

    @Test
    public void testUpdateUserEmptyEmail() throws Exception {
        when(editProfileView.getEmail()).thenReturn("");
        editProfilePresenter.updateUser();
        verify(editProfileView).showEmailError(R.string.error_empty_email);
        verify(editProfileView, never()).showProgressDialog();
        verify(userInteractor, never()).updateUser(any(), any(), any());
    }

    @Test
    public void testUpdateUserCorrectly() throws Exception {
        when(editProfileView.getEmail()).thenReturn(CORRECT_EMAIL);
        User user = new User();
        when(userInteractor.updateUser(any(), any(), any()))
            .thenReturn(Observable.just(new UserResponse(user)));
        editProfilePresenter.updateUser();
        verify(editProfileView).showProgressDialog();
        verify(editProfileView).hideProgress();
        verify(editProfileView).profileHasBeenUpdated();
        verify(editProfileView).showUserInfo(user);
        verify(editProfileView).closeEditor();
    }

    @Test
    public void testUpdateUserOnThrowable() throws Exception {
        when(editProfileView.getEmail()).thenReturn(CORRECT_EMAIL);
        Exception exception = new Exception();
        when(userInteractor.updateUser(any(), any(), any()))
            .thenReturn(Observable.error(exception));
        editProfilePresenter.updateUser();
        verify(editProfileView).showProgressDialog();
        verify(editProfileView).hideProgress();
        verify(editProfileView).showError(exception);
    }

    @Test
    public void testDontSendTwoRequestOnUserUpdate() throws Exception {
        when(editProfileView.getEmail()).thenReturn(CORRECT_EMAIL);
        when(userInteractor.updateUser(any(), any(), anyString()))
            .thenReturn(Observable.just(new UserResponse()).delay(100, TimeUnit.MILLISECONDS));
        editProfilePresenter.updateUser();
        editProfilePresenter.updateUser();
        verify(editProfileView).showProgressDialog();
    }

    @Test public void testAvatarUploadingSuccessfully() throws Exception {
        when(userInteractor.uploadAvatar(any())).thenReturn(Observable.just(new UserResponse()));
        editProfilePresenter.updateAvatar("");
        verify(editProfileView).showAvatarUploadingProgress();
        verify(editProfileView).showAvatar("");
        verify(editProfileView).avatarSuccessfullyUploaded();
    }

    @Test public void testAvatarUploadingFail() throws Exception {
        when(userInteractor.uploadAvatar(any())).thenReturn(Observable.error(new Exception()));
        editProfilePresenter.updateAvatar("");
        verify(editProfileView).showAvatarUploadingProgress();
        verify(editProfileView).showUploadAvatarError("");
    }
}