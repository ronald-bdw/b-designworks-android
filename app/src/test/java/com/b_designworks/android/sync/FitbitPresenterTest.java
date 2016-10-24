package com.b_designworks.android.sync;

import com.b_designworks.android.RxSchedulersOverrideRule;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.FitToken;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Ilya Eremin on 9/26/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class FitbitPresenterTest {

    private static final String FITBIT_INCORRECT_AUTH_CODE = "FITBIT_AUTH_CODE";
    private static final String FITBIT_CORRECT_AUTH_CODE   = "FITBIT_INCORRECT_AUTH_CODE";

    @Rule public RxSchedulersOverrideRule rxRule = new RxSchedulersOverrideRule();

    @Mock FitbitView     fitbitView;
    @Mock UserInteractor userInteractor;

    private FitbitPresenter fitbitPresenter;

    @Before
    public void setUp() throws Exception {
        when(userInteractor.integrateFitbit(FITBIT_CORRECT_AUTH_CODE)).thenReturn(Observable.just(new FitToken()));
        when(userInteractor.integrateFitbit(FITBIT_INCORRECT_AUTH_CODE)).thenReturn(Observable.error(new Throwable()));
        fitbitPresenter = new FitbitPresenter(fitbitView, userInteractor);
    }

    @Test
    public void testSuccessfulSendingFitbitCode() throws Exception {
        fitbitPresenter.handleCode(FITBIT_CORRECT_AUTH_CODE);
        verify(fitbitView).showSendingFitbitCodeProgress();
        verify(fitbitView).dismissSendingFitbitCodeProgress();
        verify(fitbitView).fitbitSuccessfullyIntegrated();
    }

    @Test
    public void testFailSendingFitbitCode() throws Exception {
        fitbitPresenter.handleCode(FITBIT_INCORRECT_AUTH_CODE);
        verify(fitbitView).showSendingFitbitCodeProgress();
        verify(fitbitView).dismissSendingFitbitCodeProgress();
        verify(fitbitView, never()).fitbitSuccessfullyIntegrated();
        verify(fitbitView).onError(any());
    }

}