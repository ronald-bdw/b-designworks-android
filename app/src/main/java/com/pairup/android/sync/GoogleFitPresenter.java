package com.pairup.android.sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.pairup.android.UserInteractor;
import com.pairup.android.sync.models.Provider;
import com.pairup.android.utils.Rxs;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.pairup.android.utils.Times;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

import static java.text.DateFormat.getDateInstance;

/**
 * Created by Ilya Eremin on 9/13/16.
 */

public class GoogleFitPresenter {

    private static final String TAG                  = "GoogleFitPresenter";
    private static final int    REQUEST_CODE_SIGN_IN = 5599;
    private static final String SERVER_KEY           =
        "1031731430214-cgrtpcni90gh3lmmr3fem5nihb4b3iuk.apps.googleusercontent.com";

    @Nullable private GoogleFitView view;

    private final UserInteractor  userInteractor;
    private final Context         context;
    private       GoogleApiClient mClient;
    private       int             timeZone;

    private List<com.pairup.android.sync.models.Activity> activityList;

    @Inject
    public GoogleFitPresenter(@NonNull UserInteractor userInteractor,
                              @NonNull Context context) {
        this.userInteractor = userInteractor;
        this.context = context;
    }

    public void attachView(GoogleFitView view, @NonNull FragmentActivity activity) {
        this.view = view;
        buildGoogleFitClient(activity);
    }

    private void buildGoogleFitClient(@NonNull FragmentActivity activity) {
        GoogleSignInOptions signInRequest = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(new Scope(Scopes.FITNESS_ACTIVITY_READ))
            .requestServerAuthCode(SERVER_KEY, true)
            .build();
        mClient = new GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInRequest)
            .addApi(Fitness.HISTORY_API)
            .addConnectionCallbacks(
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        view.onClientConnected();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                            if (view != null) {
                                view.showInternetConnectionError();
                            }
                        } else if (i == GoogleApiClient
                            .ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                            if (view != null) {
                                view.showGoogleServiceDisconnected();
                            }
                        }
                    }
                }
            )
            .enableAutoManage(activity, 0, result -> {
                Log.i(TAG, "Google Play services connection failed. Cause: " + result.toString());
                if (view != null) {
                    view.onGoogleServicesError(result);
                }
            })
            .build();
    }

    public void startIntegrate(@NonNull FragmentActivity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mClient);
        activity.startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    public void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount acct = result.getSignInAccount();
                    if (acct != null) {
                        userInteractor
                            .integrateFitnessApp(acct.getServerAuthCode(), Provider.GOOGLE_FIT)
                            .compose(Rxs.doInBackgroundDeliverToUI())
                            .subscribe(
                                fitToken -> userInteractor
                                    .saveFitnessTokenLocally(fitToken.getId(), Provider.GOOGLE_FIT),
                                (error) -> {
                                    if (view != null) {
                                        view.onError(error);
                                    }
                                });
                        if (view != null) {
                            view.integrationSuccessful();
                        }
                    } else {
                        if (view != null) {
                            view.errorWhileRetrievingCode();
                        }
                    }
                }
            } else {
                if (view != null) {
                    view.userCancelIntegration();
                }
            }
        }
    }

    public void sendActivityToServer() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long startTime = cal.getTimeInMillis();

        Log.i(TAG, "Range Start: " + Times.parseDateToString(startTime, "yyyy-MM-dd HH:mm:ss zzz"));
        Log.i(TAG, "Range End: " + Times.parseDateToString(endTime, "yyyy-MM-dd HH:mm:ss zzz"));
        Log.i(TAG, "TimeZone: " + cal.getTimeZone().getRawOffset()/3600000);
        timeZone = cal.getTimeZone().getRawOffset()/3600000;

        DataReadRequest readRequest = new DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.HOURS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build();

        Observable.just(readRequest)
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.newThread())
            .subscribe(readRequest1 ->
                    sendStepsData(Fitness.HistoryApi
                        .readData(mClient, readRequest1).await(1, TimeUnit.MINUTES)),
                error -> {
                    if (view != null) {
                        view.onError(error);
                    }
                });

    }

    private void sendStepsData(DataReadResult result) {
//        Log.i(TAG, "only one or more: " + result.getBuckets().size());
        activityList = new ArrayList<>();
        for (Bucket bucket : result.getBuckets()) {
            List<DataSet> dataSets = bucket.getDataSets();
            for (DataSet dataSet : dataSets) {
                dumpDataSet(dataSet);
            }
        }
        com.pairup.android.sync.models.Activity[] activities = activityList.toArray(new com.pairup.android.sync.models.Activity[activityList.size()]);
        Log.i(TAG, "before sending");
        userInteractor.sendActivity(activityList);
        Log.i(TAG, "after sending");
    }

    private void dumpDataSet(DataSet dataSet) {
//        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());

        for (DataPoint dp : dataSet.getDataPoints()) {
            com.pairup.android.sync.models.Activity activity = new com.pairup.android.sync.models.Activity();
            activity.setStartedAt(Times.parseDateToString(dp.getStartTime(TimeUnit.MILLISECONDS), "yyyy-MM-dd HH:mm:ss") + " GMT+" + timeZone);
            activity.setFinishedAt(Times.parseDateToString(dp.getEndTime(TimeUnit.MILLISECONDS), "yyyy-MM-dd HH:mm:ss") + " GMT+" + timeZone);
            activity.setSource("googlefit");

//            Log.i(TAG, "Data point:");
//            Log.i(TAG, "\tType: " + dp.getDataType().getName());
//            Log.i(TAG, "\tStart: " + Times.parseDateToString(dp.getStartTime(TimeUnit.MILLISECONDS), "yyyy-MM-dd HH:mm:ss")+" GMT+");
//            Log.i(TAG, "\tEnd: " + Times.parseDateToString(dp.getEndTime(TimeUnit.MILLISECONDS), "yyyy-MM-dd HH:mm:ss zzz"));
            for(Field field : dp.getDataType().getFields()) {
//                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                if("steps".equals(field.getName())) {
                    activity.setStepsCount(dp.getValue(field).asInt());
                }
            }
            activityList.add(activity);
        }
    }

    public void detachView(@NonNull FragmentActivity activity) {
        if (mClient != null) {
            mClient.stopAutoManage(activity);
            mClient.disconnect();
            mClient = null;
        }
        view = null;
    }

    public void logout() {
        if (mClient != null) {
            Auth.GoogleSignInApi.signOut(mClient);
        }
    }
}
