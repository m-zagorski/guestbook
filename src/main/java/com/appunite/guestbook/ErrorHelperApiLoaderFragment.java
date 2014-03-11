package com.appunite.guestbook;

import android.view.View;

import com.appunite.guestbook.helpers.data.Result;
import com.appunite.syncer.AUSyncerStatus;

public abstract class ErrorHelperApiLoaderFragment<R extends Result<?>>
extends ApiLoaderFragment<R>{

    private ErrorHelper mErrorHelper;

    @Override
    protected void setupErrorHelper(View view) {
        mErrorHelper = new ErrorHelper(this, view);
    }

    @Override
    protected void showProgress() {
        mErrorHelper.onReportStatus(false, true, AUSyncerStatus.statusSuccess());
    }

    @Override
    protected void onLoadMainFinished(R result) {
        AUSyncerStatus lastStatus = null;
        boolean screenEmpty;
        if (result.isError()) {
            lastStatus = AUSyncerStatus.statusInternalIssue();
            screenEmpty = false;
        } else {
            lastStatus = AUSyncerStatus.statusSuccess();
            screenEmpty = isScreenEmpty(result);
        }
        mErrorHelper.onReportStatus(screenEmpty, false, lastStatus);
    }

    protected abstract boolean isScreenEmpty(R result);
}
