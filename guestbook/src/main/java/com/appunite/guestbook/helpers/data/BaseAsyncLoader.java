package com.appunite.guestbook.helpers.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class BaseAsyncLoader<D> extends AsyncTaskLoader<D> {
    private D mData;

    public BaseAsyncLoader(Context context) {
        super(context);
        mData = null;
    }

    @Override
    public abstract D loadInBackground();

    @Override
    public void deliverResult(D data) {
        mData = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        if (mData != null) {
            super.deliverResult(mData);
        }
    }
}