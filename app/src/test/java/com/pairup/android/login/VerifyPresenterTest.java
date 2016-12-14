package com.pairup.android.login;

import com.pairup.android.RxSchedulersOverrideRule;
import com.pairup.android.UserInteractor;
import com.pairup.android.login.models.AuthResponse;
import com.pairup.android.utils.storage.RuntimeStorage;

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

    private static final String LONG_REQUEST_FLAG       = "veryLongRequestToVerifyShownHidden";
    private static final String REGISTERED_PHONE_NUMBER = "registered phone number";
    private static final String NEW_PHONE_NUMBER        = "new phone number";
    private static final String CORRECT_SMS_CODE        = "1234";
    private static final String WRONG_SMS_CODE          = "LOLKEKMDA";
    private static final String PHONE_CODE_ID           = "phoneCodeId";

    @Rule public RxSchedulersOverrideRule rxRule = new RxSchedulersOverrideRule();

    @Mock private UserInteractor userInteractor;
    @Mock private VerifyView     view;

    private VerifyPresenter     presenter;
    private LoginFlowInteractor loginFlowInteractor;

    @Before public void setUp() throws Exception {
        when(userInteractor.requestCode(REGISTERED_PHONE_NUMBER))
            .thenReturn(Observable.just(new AuthResponse(true, PHONE_CODE_ID)));
        when(userInteractor.requestCode(NEW_PHONE_NUMBER))
            .thenReturn(Observable.just(new AuthResponse(false, PHONE_CODE_ID)));
        when(userInteractor.requestCode(LONG_REQUEST_FLAG))
            .thenReturn(Observable.just(new AuthResponse(true, PHONE_CODE_ID))
                .delay(100, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.computation()));

        when(userInteractor.login(eq(CORRECT_SMS_CODE), any(), any()))
            .thenReturn(Observable.just(null));
        when(userInteractor.login(eq(WRONG_SMS_CODE), any(), any()))
            .thenReturn(Observable.error(new Exception()));
        when(userInteractor.login(eq(LONG_REQUEST_FLAG), any(), any()))
            .thenReturn(Observable.just(null).delay(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation()));
        loginFlowInteractor = new LoginFlowInteractor(new RuntimeStorage());

        presenter = new VerifyPresenter(userInteractor, loginFlowInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testRegisteredUserFlow() throws Exception {
        loginFlowInteractor.setPhoneNumber(REGISTERED_PHONE_NUMBER);
        presenter.sendCode();
        verify(view).showRequestVerificationCodeProgressDialog();
        verify(userInteractor).requestCode(any());
        verify(view).hideRequestVerificationProgressDialog();
        presenter.handleSmsCode(CORRECT_SMS_CODE);
        verify(view).showAuthorizationProgressDialog();
        verify(view).hideAuthProgressDialog();
        verify(view).openChatScreen();
        verify(view, never()).openRegistrationScreen(any(), any(), any());
    }

    @Test
    public void testUnregisteredUserFlow() throws Exception {
        when(userInteractor.checkVerificationNumber(any(), any()))
            .thenReturn(Observable.just(null));

        loginFlowInteractor.setPhoneNumber(NEW_PHONE_NUMBER);
        presenter.sendCode();
        verify(view).showRequestVerificationCodeProgressDialog();
        verify(userInteractor).requestCode(any());
        verify(view).hideRequestVerificationProgressDialog();
        presenter.handleSmsCode(CORRECT_SMS_CODE);
        verify(view).showAuthorizationProgressDialog();
        verify(userInteractor).checkVerificationNumber(any(), any());
        verify(view).hideAuthProgressDialog();
        verify(view).openRegistrationScreen(NEW_PHONE_NUMBER, CORRECT_SMS_CODE, PHONE_CODE_ID);
    }

    @Test
    public void testIncorrectVerificationCode() throws Exception {
        Throwable error = new Throwable(("invalid code"));
        when(userInteractor.checkVerificationNumber(any(), eq(WRONG_SMS_CODE)))
            .thenReturn(Observable.error(error));

        presenter.handleSmsCode(WRONG_SMS_CODE);
        verify(view).showAuthorizationProgressDialog();
        verify(userInteractor).checkVerificationNumber(any(), eq(WRONG_SMS_CODE));
        verify(view).hideAuthProgressDialog();
        verify(view).showError(error);
    }

    @Test
    public void testOnShownOnHidden() throws Exception {
        loginFlowInteractor.setPhoneNumber(LONG_REQUEST_FLAG);
        presenter.sendCode();
        presenter.onHidden();
        Mockito.reset(view);
        presenter.onShown();
        verify(view).showRequestVerificationCodeProgressDialog();
    }
}