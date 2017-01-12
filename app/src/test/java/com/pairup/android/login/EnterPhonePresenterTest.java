package com.pairup.android.login;

import com.pairup.android.RxSchedulersOverrideRule;
import com.pairup.android.UserInteractor;
import com.pairup.android.login.models.AuthResponse;
import com.pairup.android.login.models.UserStatus;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.network.RetrofitException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.EOFException;

import rx.Observable;

import static com.pairup.android.login.models.AuthResponse.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sergeyklymenko on 1/10/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class EnterPhonePresenterTest {

    private static final String PHONE_AREA = "+380";
    private static final String NUMBER = "971741312";
    private static final String PHONE_NUMBER = PHONE_AREA + NUMBER;

    @Rule
    public RxSchedulersOverrideRule rxRule = new RxSchedulersOverrideRule();

    @Mock
    private EnterPhoneView enterPhoneView;
    @Mock
    private UserInteractor userInteractor;
    @Mock
    private LoginFlowInteractor loginFlowInteractor;
    @Mock
    private Analytics analytics;

    private EnterPhonePresenter enterPhonePresenter;
    private InnerAuthResponse innerAuthResponse;
    private AuthResponse response;
    private UserStatus userStatus;

    @Before
    public void setUp() {
        enterPhonePresenter = new EnterPhonePresenter(userInteractor, loginFlowInteractor,
            analytics);
        enterPhonePresenter.attachView(enterPhoneView);

        innerAuthResponse = new InnerAuthResponse();
        innerAuthResponse.setId("1");
        innerAuthResponse.setPhoneRegistered(true);

        response = new AuthResponse();
        response.setAuthPhoneCode(innerAuthResponse);

        userStatus = new UserStatus();
        userStatus.setProvider("Test");
        userStatus.setPhoneRegistered(true);
    }

    @Test
    public void getAreaCodeTest() {
        Assert.assertTrue(enterPhonePresenter.getAreaCode("452").startsWith("+"));
    }

    @Test
    public void manageSubmitTest() {
        when(userInteractor.requestCode(PHONE_NUMBER)).thenReturn(Observable.just(response));
        when(userInteractor.requestUserStatus(PHONE_NUMBER))
                .thenReturn(Observable.just(userStatus));

        enterPhonePresenter.manageSubmit(PHONE_AREA, NUMBER,
                AccountVerificationType.HAS_PROVIDER, "Test");

        verify(enterPhoneView).hideKeyboard();
        verify(enterPhoneView).showProgress();
        verify(enterPhoneView).hideProgress();
        verify(enterPhoneView).openVerificationScreen();
    }

    @Test
    public void manageSubmitErrorTest() {
        when(userInteractor.requestCode(PHONE_NUMBER)).thenReturn(Observable.just(response));
        when(userInteractor.requestUserStatus(PHONE_NUMBER))
                .thenReturn(Observable.error(new Exception()));

        enterPhonePresenter.manageSubmit(PHONE_AREA, NUMBER,
                AccountVerificationType.HAS_PROVIDER, "Test");

        verify(enterPhoneView).handleError();
    }

    @Test
    public void manageSubmitMissMatchProviderTest() {
        when(userInteractor.requestCode(PHONE_NUMBER)).thenReturn(Observable.just(response));
        when(userInteractor.requestUserStatus(PHONE_NUMBER))
                .thenReturn(Observable.just(userStatus));
        enterPhonePresenter.manageSubmit(PHONE_AREA, NUMBER,
                AccountVerificationType.HAS_PROVIDER, "Test3");

        verify(enterPhoneView).showErrorDialog();
    }

    @Test
    public void manageSubmitNetworkProblemTest() {
        RetrofitException exception = RetrofitException.networkError(new EOFException());
        when(userInteractor.requestCode(PHONE_NUMBER)).thenReturn(Observable.error(exception));
        when(userInteractor.requestUserStatus(PHONE_NUMBER))
                .thenReturn(Observable.just(userStatus));

        enterPhonePresenter.manageSubmit(PHONE_AREA, NUMBER,
                AccountVerificationType.HAS_PROVIDER, "Test");

        verify(enterPhoneView).showDialogNetworkProblem();
    }

    @Test
    public void manageSubmitPhoneErrorTest() {
        RetrofitException exception = RetrofitException.unexpectedError(new EOFException());
        when(userInteractor.requestCode(PHONE_NUMBER)).thenReturn(Observable.error(exception));
        when(userInteractor.requestUserStatus(PHONE_NUMBER))
                .thenReturn(Observable.just(userStatus));

        enterPhonePresenter.manageSubmit(PHONE_AREA, NUMBER,
                AccountVerificationType.HAS_PROVIDER, "Test");

        verify(enterPhoneView).showPhoneError();
    }
}
