package com.appunite.guestbook.helpers.data;

import android.content.Context;

import com.crashlytics.android.Crashlytics;

public abstract class ApiAsyncLoader<T> extends BaseAsyncLoader<Result<T>> {

    public ApiAsyncLoader(Context context) {
        super(context);
    }

    @Override
    public Result<T> loadInBackground() {
        try {
            return Result.fromResult(loadFromApi());
        } catch (Exception e) {
            Crashlytics.logException(e);
            return Result.fromError(e);
        }
    }

    protected abstract T loadFromApi() throws Exception;
}
