package com.pairup.android.login;

import com.pairup.android.RxSchedulersOverrideRule;
import com.pairup.android.UserInteractor;
import com.pairup.android.login.models.Provider;
import com.pairup.android.login.models.Providers;
import com.pairup.android.login.models.User;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sergeyklymenko on 1/10/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class SelectProvidersPresenterTest {

    @Rule public RxSchedulersOverrideRule rxRule = new RxSchedulersOverrideRule();

    @Mock private SelectProviderView selectProviderView;
    @Mock private UserInteractor  userInteractor;

    private SelectProviderPresenter selectProviderPresenter;

    @Before
    public void setUp() {
        selectProviderPresenter = new SelectProviderPresenter(userInteractor);
        selectProviderPresenter.attachView(selectProviderView);
    }

    @Test
    public void fetchProvidersTest() {
        List<String> providersName = new ArrayList<>();
        providersName.add("Test");

        Provider provider = new Provider();
        provider.setId("001");
        provider.setName("Test");

        List<Provider> providerList = new ArrayList<>();
        providerList.add(provider);

        Providers providers = new Providers();
        providers.setProviders(providerList);

        when(userInteractor.getProviders()).thenReturn(Observable.just(providers));
        selectProviderPresenter.fetchProviders();
        verify(selectProviderView).showProviders(providersName);
    }

    @Test
    public void fetchProvidersErrorTest() {
        when(userInteractor.getProviders()).thenReturn(Observable.error(new Exception()));
        selectProviderPresenter.fetchProviders();
        verify(selectProviderView).showProvidersLoadError();
    }
}
