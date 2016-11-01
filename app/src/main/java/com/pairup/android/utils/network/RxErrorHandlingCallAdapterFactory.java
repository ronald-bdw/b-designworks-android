package com.pairup.android.utils.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pairup.android.Navigator;
import com.pairup.android.UserInteractor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJavaCallAdapterFactory original;

    private static UserInteractor userInteractor;
    private static Context        context;

    @Inject
    public RxErrorHandlingCallAdapterFactory(@NonNull UserInteractor userInteractor,
                                             @NonNull Context context) {
        original = RxJavaCallAdapterFactory.create();
        this.userInteractor = userInteractor;
        this.context = context;
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory(userInteractor, context);
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit), userInteractor, context);
    }

    private static class RxCallAdapterWrapper implements CallAdapter<Observable<?>> {
        private final Retrofit       retrofit;
        private final CallAdapter<?> wrapped;

        private UserInteractor userInteractor;
        private Context        context;

        public RxCallAdapterWrapper(Retrofit retrofit,
                                    CallAdapter<?> wrapped,
                                    UserInteractor userInteractor,
                                    Context context) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
            this.userInteractor = userInteractor;
            this.context = context;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Observable<?> adapt(Call<R> call) {
            return ((Observable) wrapped.adapt(call))
                .onErrorResumeNext(new Func1<Throwable, Observable>() {
                    @Override public Observable call(Throwable throwable) {
                        return Observable.error(RxCallAdapterWrapper.this.asRetrofitException(throwable));
                    }
                });
        }

        private RetrofitException asRetrofitException(Throwable throwable) {
            // We had non-200 http error
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                if (response.code() == 401) {
                    userInteractor.logout();
                    Navigator.welcome(context);
                }
                return RetrofitException.httpError(response.raw().request().url().toString(), response, retrofit);
            }
            // A network error happened
            if (throwable instanceof IOException) {
                return RetrofitException.networkError((IOException) throwable);
            }

            // We don't know what happened. We need to simply convert to an unknown error

            return RetrofitException.unexpectedError(throwable);
        }
    }
}
