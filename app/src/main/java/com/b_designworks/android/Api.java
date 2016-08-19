package com.b_designworks.android;

import android.support.annotation.NonNull;

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
    Observable<AuthResponse> sendMeCode(@Field("phone_number") String phone);

    @FormUrlEncoded
    @POST(V1 + "users") Observable<RegisterResponse> register(
        @NonNull @Field(value = "first_name", encoded = true) String firstName,
        @NonNull @Field(value = "last_name", encoded = true) String lastName,
        @NonNull @Field("email") String email,
        @NonNull @Field("sms_code") String code,
        @NonNull @Field("phone_number") String phoneNumber,
        @NonNull @Field("auth_phone_code_id") String phoneCodeId
    );

    @FormUrlEncoded
    @POST(V1 + "users/sign_in") Observable<RegisterResponse> signIn(
        @NonNull @Field("sms_code") String code,
        @NonNull @Field("phone_number") String phoneNumber,
        @NonNull @Field("auth_phone_code_id") String phoneCodeId
        );

}
