package com.pairup.android.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class Rxs {
    public static <T> Observable.Transformer<T, T> doInBackgroundDeliverToUI() {
        return observable -> observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());

    }
}
