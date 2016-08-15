package com.b_designworks.android;

import com.b_designworks.android.login.models.AuthResponse;
import com.b_designworks.android.login.models.RegisterResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public interface Api {

    String BASE_URL = BuildConfig.API_URL;
    String V1 = "/v1/";

    @FormUrlEncoded
    @POST(V1 + "auth_phone_codes")
    Observable<AuthResponse> getCode(@Field("phone_number") String phone);

    @FormUrlEncoded
    @POST(V1 + "users") Observable<RegisterResponse> register(
        @Field(value = "first_name", encoded = true) String firstName,
        @Field(value = "last_name", encoded = true) String lastName,
        @Field("email") String email,
        @Field("sms_code") String code, @Field("phone_number") String phoneNumber,
        @Field("auth_phone_code_id") String phoneCodeId
    );

}
