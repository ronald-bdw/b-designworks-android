package com.pairup.android;

import com.pairup.android.login.models.AuthResponse;
import com.pairup.android.login.models.UserResponse;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.MapperUtils;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.storage.RuntimeStorage;
import com.pairup.android.utils.storage.UserSettings;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rx.Observable;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Ilya Eremin on 15.08.2016.
 */

public class UserManagerTest {

    private static final String MY_FAKE_NUMBER         = "my_fake_number";
    private static final String MY_FAKE_NUMBER_CODE_ID = "1234";
    private static final String FAKE_AUTH_RESPONSE     = "{\"auth_phone_code\":{\"id\":" + MY_FAKE_NUMBER_CODE_ID + ",\"phone_registered\":false}}";
    private static final String FAKE_TOKEN             = "123123fake_token_haha";
    private static final String FAKE_REGISTER_RESPONSE = "{\"user\":{\"id\":1,\"authentication_token\":\"" + FAKE_TOKEN + "\"}}";

    private static AuthResponse getFakeAuthResponse() {
        return MapperUtils.getInstance().fromJson(FAKE_AUTH_RESPONSE, AuthResponse.class);
    }

    private static UserResponse getFakeRegisterResponse() {
        return MapperUtils.getInstance().fromJson(FAKE_REGISTER_RESPONSE, UserResponse.class);
    }

    private UserInteractor userManager;
    private UserSettings   userSettings;

    @Before public void setUp() throws Exception {
        Api mockedApi = mock(Api.class);
        RuntimeStorage storage = new RuntimeStorage();
        userSettings = new UserSettings(storage);
        when(mockedApi.sendMeCode(any())).thenReturn(Observable.just(getFakeAuthResponse()));
        when(mockedApi.register(anyString(), anyString(), anyString(), anyString(), eq(MY_FAKE_NUMBER), eq(MY_FAKE_NUMBER_CODE_ID)))
            .thenReturn(Observable.just(getFakeRegisterResponse()));
        userManager = new UserInteractor(storage, userSettings, mockedApi);
    }

    @Test
    public void testRegister() throws Exception {
        Bus.DISABLE_FOR_TEST = true;
        userManager.requestCode(MY_FAKE_NUMBER)
            .subscribe(ignoreResult -> {
            }, ErrorUtils.onError());
        userManager.register("Danny", "Makaskill", "example@mail.com", "1234", MY_FAKE_NUMBER, MY_FAKE_NUMBER_CODE_ID)
            .subscribe(ignoreResult -> {
            }, ErrorUtils.onError());
        Assert.assertEquals(FAKE_TOKEN, userSettings.getToken());
    }
}