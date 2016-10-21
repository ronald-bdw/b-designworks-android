package com.b_designworks.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.b_designworks.android.login.models.AuthResponse;
import com.b_designworks.android.login.models.FitToken;
import com.b_designworks.android.login.models.UserResponse;
import com.b_designworks.android.sync.Provider;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

import static com.b_designworks.android.Api.V1;

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

    @GET(V1 + "users/account") Observable<UserResponse> currentUser();

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

    @Multipart
    @PUT(V1 + "users/{id}")
    Observable<UserResponse> uploadAvatar(@NonNull @Path("id") String id,
                                          @NonNull @Part MultipartBody.Part data);

    @FormUrlEncoded
    @POST(V1 + "/fitness_tokens") Observable<FitToken> integrateFitnessApp(
        @NonNull @Field("authorization_code") String code,
        @NonNull @Field("source") Provider provider
    );

    @DELETE(V1 + "fitness_tokens/{id}") Observable<FitToken> deleteFitnessToken(
        @NonNull @Path("id") String id
    );
}
