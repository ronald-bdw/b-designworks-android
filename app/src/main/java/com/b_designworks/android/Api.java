package com.b_designworks.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.b_designworks.android.login.models.AuthResponse;
import com.b_designworks.android.login.models.UserResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public interface Api {

    String BASE_URL = BuildConfig.API_URL;
    String V1       = "/v1/";

    @FormUrlEncoded
    @POST(V1 + "auth_phone_codes")
    Observable<AuthResponse> sendMeCode(
        @Field("phone_number") String phone);

    @FormUrlEncoded
    @POST(V1 + "users") Observable<UserResponse> register(
        @NonNull @Field(value = "first_name", encoded = true) String firstName,
        @NonNull @Field(value = "last_name", encoded = true) String lastName,
        @NonNull @Field("email") String email,
        @NonNull @Field("sms_code") String code,
        @NonNull @Field("phone_number") String phoneNumber,
        @NonNull @Field("auth_phone_code_id") String phoneCodeId
    );

    @FormUrlEncoded
    @POST(V1 + "users/sign_in") Observable<UserResponse> signIn(
        @NonNull @Field("sms_code") String code,
        @NonNull @Field("phone_number") String phoneNumber,
        @NonNull @Field("auth_phone_code_id") String phoneCodeId
    );

    @Multipart
    @PUT(V1 + "users/{id}") Observable<UserResponse> editProfile(
        @NonNull @Path("id") String id,
        @Nullable @Part("user[first_name]") String firstName,
        @Nullable @Part("user[last_name]") String lastName,
        @Nullable @Part("user[email]") String email
    );

    @FormUrlEncoded
    @POST(V1 + "/users/{id}") Observable<UserResponse> sendSecretCode(
        @NonNull @Part("id") String id,
        @NonNull @Field("provider") String provider,
        @NonNull @Field("code") String code
    );

}
