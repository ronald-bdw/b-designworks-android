package com.pairup.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pairup.android.login.models.AuthResponse;
import com.pairup.android.login.models.FitToken;
import com.pairup.android.login.models.Providers;
import com.pairup.android.login.models.UserResponse;
import com.pairup.android.login.models.UserStatus;
import com.pairup.android.sync.models.ActivityToSend;
import com.pairup.android.sync.models.Provider;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
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
        @NonNull @Field("phone_number") String phone,
        @NonNull @Field("device_type") String deviceType
    );

    @GET(V1 + "users/account") Observable<UserResponse> currentUser();

    @FormUrlEncoded
    @POST(V1 + "auth_phone_codes/{id}/check") Observable<ResponseBody> checkVerificationCode(
        @NonNull @Path("id") String id,
        @NonNull @Field("sms_code") String code
    );

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
    Observable<UserResponse> uploadAvatar(
        @NonNull @Path("id") String id,
        @NonNull @Part MultipartBody.Part data
    );

    @FormUrlEncoded
    @POST(V1 + "fitness_tokens") Observable<FitToken> integrateFitnessApp(
        @NonNull @Field("authorization_code") String code,
        @NonNull @Field("source") Provider provider
    );

    @DELETE(V1 + "fitness_tokens/{id}") Observable<Void> deleteFitnessToken(
        @NonNull @Path("id") String id
    );

    @FormUrlEncoded
    @POST(V1 + "notifications") Observable<ResponseBody> userEnabledPushNotifications(
        @NonNull @Field("notification[kind]") String kind
    );

    @DELETE("v1/notifications/message_push")
    Observable<ResponseBody> userDisabledPushNotificatinos();

    @FormUrlEncoded
    @POST(V1 + "registration_status")
    Observable<UserStatus> getUserStatus(
        @NonNull @Field("phone_number") String phone
    );

    @FormUrlEncoded
    @POST(V1 + "subscriptions")
    Observable<Void> sendSubscriptionStatus(
        @NonNull @Field("plan_name") String plan,
        @NonNull @Field("expires_at") String date,
        @Field("active") boolean isActive
    );

    @PATCH(V1 + "subscriptions/expire")
    Observable<Void> sendSubscriptionExpired();

    @GET(V1 + "providers") Observable<Providers> getProviders();

//    @FormUrlEncoded
//    @POST(V1 + "activities")
//    Observable<ResponseBody> sendActivity(
//        @NonNull @Field("started_at") String startedAt,
//        @NonNull @Field("finished_at") String finishedAt,
//        @Field("steps_count") int stepsCount,
//        @NonNull @Field("source") String source
//    );

//    @FormUrlEncoded
    @POST(V1 + "activities")
    Observable<ResponseBody> sendActivity(
            @NonNull @Body ActivityToSend activityToSend
            );
}
