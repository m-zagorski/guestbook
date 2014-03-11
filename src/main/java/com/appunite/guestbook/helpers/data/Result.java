package com.appunite.guestbook.helpers.data;

import static com.google.api.client.util.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkNotNull;

public class Result<D> {
    protected final D mResult;
    protected final Exception mException;

    protected Result(D result, Exception exception) {
        mResult = result;
        mException = exception;
    }

    public static <D> Result<D> fromError(Exception exception) {
        return new Result<D>(null, checkNotNull(exception));
    }

    public static <D> Result<D> fromResult(D result) {
        return new Result<D>(result, null);
    }

    public boolean isError() {
        return mException != null;
    }

    public boolean isSuccess() {
        return mException == null;
    }

    public Exception getException() {
        checkState(isError(), "You have to check for error before");
        return mException;
    }

    public D getResult() {
        checkState(isSuccess(), "You have to check for error before");
        return mResult;
    }
}
