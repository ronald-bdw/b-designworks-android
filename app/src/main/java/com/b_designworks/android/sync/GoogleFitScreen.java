package com.b_designworks.android.sync;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.ui.UiInfo;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

/**
 * Created by Ilya Eremin on 24.08.2016.
 */
public class GoogleFitScreen extends BaseActivity {

    private static final String TAG         = "GoogleFitScreen";
    private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";

    public static GoogleApiClient mClient = null;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_google_fit);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        buildFitnessClient();
    }

    private void buildFitnessClient() {

        // Create the Google API Client

        mClient = new GoogleApiClient.Builder(this)
            .addApi(Fitness.HISTORY_API)
            .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
            .addConnectionCallbacks(
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.i(TAG, "Connected!!!");
                        // Now you can make calls to the Fitness APIs.  What to do?
                        // Look at some data!!
                        // Begin by creating the query.
                        DataReadRequest readRequest = queryFitnessData();

                        // [START read_dataset]
                        // Invoke the History API to fetch the data with the query and await the result of
                        // the read request.
                        Observable.create((Observable.OnSubscribe<DataReadResult>) subscriber -> {
                            subscriber.onNext(Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES));
                            subscriber.onCompleted();
                        })
                            .compose(Rxs.doInBackgroundDeliverToUI())
                            .subscribe(GoogleFitScreen::printData);
                    }

                    @Override

                    public void onConnectionSuspended(int i) {
                        // If your connection to the sensor gets lost at some point,
                        // you'll be able to determine the reason and react to it here.
                        if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                            Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                        } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                            Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                        }
                    }
                }
            )
            .enableAutoManage(this, 0, result -> {
                Toast.makeText(GoogleFitScreen.this, "Exception while connecting to Google Play services: " +
                    result.getErrorMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, "Google Play services connection failed. Cause: " +
                    result.toString());
            })
            .build();
    }


    /**
     * Log a record of the query result. It's possible to get more constrained data sets by
     * specifying a data source or data type, but for demonstrative purposes here's how one would
     * dump all the data. In this sample, logging also prints to the device screen, so we can see
     * what the query returns, but your app should not log fitness information as a privacy
     * consideration. A better option would be to dump the data you receive to a local data
     * directory to avoid exposing it to other applications.
     */
    public static void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private static void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                    " Value: " + dp.getValue(field));
            }
        }
    }
    // [END parse_dataset]

    /**
     * Return a {@link DataReadRequest} for all step count changes in the past week.
     */
    public static DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
            // The data request can specify multiple data types to return, effectively
            // combining multiple data queries into one call.
            // In this example, it's very unlikely that the request is for several hundred
            // datapoints each consisting of a few steps and a timestamp.  The more likely
            // scenario is wanting to see how many steps were walked per day, for 7 days.
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            // Analogous to a "Group By" in SQL, defines how data should be aggregated.
            // bucketByTime allows for a time span, whereas bucketBySession would allow
            // bucketing by "sessions", which would need to be defined in code.
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build();
        // [END build_read_data_request]

        return readRequest;
    }
}
