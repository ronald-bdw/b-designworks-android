package com.b_designworks.android.login;

import com.b_designworks.android.RxSchedulersOverrideRule;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.AuthResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Ilya Eremin on 9/21/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class VerifyPresenterTest {

    @Rule public RxSchedulersOverrideRule rxRule = new RxSchedulersOverrideRule();

    private static final String REGISTERED_PHONE_NUMBER = "registered phone number";
    private static final String NEW_PHONE_NUMBER        = "new phone number";
    private static final String CORRECT_SMS_CODE        = "1234";
    private static final String WRONG_SMS_CODE          = "LOLKEKMDA";
    private static final String PHONE_CODE_ID           = "phoneCodeId";

    private static final String LONG_REQUEST_FLAG = "veryLongRequestToVerifyShownHidden";

    private VerifyPresenter presenter;
    @Mock   UserInteractor  userInteractor;
    @Mock   VerifyView      view;

    @Before public void setUp() throws Exception {
        when(userInteractor.requestCode(REGISTERED_PHONE_NUMBER)).thenReturn(Observable.just(new AuthResponse(true, PHONE_CODE_ID)));
        when(userInteractor.requestCode(NEW_PHONE_NUMBER)).thenReturn(Observable.just(new AuthResponse(false, PHONE_CODE_ID)));
        when(userInteractor.requestCode(LONG_REQUEST_FLAG)).thenReturn(Observable.just(new AuthResponse(true, PHONE_CODE_ID)).delay(100, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.computation()));

        when(userInteractor.verifyCode(eq(CORRECT_SMS_CODE), any(), any())).thenReturn(Observable.just(null));
        when(userInteractor.verifyCode(eq(WRONG_SMS_CODE), any(), any())).thenReturn(Observable.error(new Exception()));
        when(userInteractor.verifyCode(eq(LONG_REQUEST_FLAG), any(), any())).thenReturn(Observable.just(null).delay(100, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.computation()));
        presenter = new VerifyPresenter(userInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testRegisteredUserFlow() throws Exception {
        presenter.requestCode(REGISTERED_PHONE_NUMBER);
        verify(view).showRequestVerificationCodeProgressDialog();
        verify(userInteractor).requestCode(any());
        verify(view).hideRequestVerificationProgressDialog();
        verify(view).showWaitingForSmsProgress();
        presenter.handleSmsCode(CORRECT_SMS_CODE);
        verify(view).showAuthorizationProgressDialog();
        verify(view).hideAuthProgressDialog();
        verify(view).openChatScreen();
        verify(view, never()).openRegistrationScreen(any(), any(), any());
    }

    @Test
    public void testUnregisteredUserFlow() throws Exception {
        presenter.requestCode(NEW_PHONE_NUMBER);
        verify(view).showRequestVerificationCodeProgressDialog();
        verify(userInteractor).requestCode(any());
        verify(view).hideRequestVerificationProgressDialog();
        verify(view).showWaitingForSmsProgress();
        presenter.handleSmsCode(CORRECT_SMS_CODE);
        verify(view).openRegistrationScreen(CORRECT_SMS_CODE, NEW_PHONE_NUMBER, PHONE_CODE_ID);
    }

    @Test
    public void testIncorrectSmsCode() throws Exception {
        presenter.handleSmsCode(null);
        verify(view).showVerificationCodeError();
    }

    @Test
    public void testOnShownOnHidden() throws Exception {
        presenter.requestCode(LONG_REQUEST_FLAG);
        presenter.onHidden();
        Mockito.reset(view);
        presenter.onShown();
        verify(view).showRequestVerificationCodeProgressDialog();
    }

}