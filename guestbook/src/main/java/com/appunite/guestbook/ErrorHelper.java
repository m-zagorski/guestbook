package com.appunite.guestbook;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.appunite.syncer.AUSyncerStatus;

import butterknife.InjectView;

import static com.google.common.base.Preconditions.checkNotNull;

public class ErrorHelper implements View.OnClickListener {
    private final View mErrorLayout;
    private final TextView mErrorMessage;
    private final View mProgress;
    private final TextView mEmpty;
    private final OnForceReloadListener mListener;

    public static interface OnForceReloadListener {
        public void onForceReload();
    }

    public ErrorHelper(OnForceReloadListener listener, View view) {
        checkNotNull(listener);
        checkNotNull(view);

        mListener = listener;
        mProgress = checkNotNull(view.findViewById(android.R.id.progress));
        mErrorLayout = checkNotNull(view.findViewById(R.id.error_layout));
        mErrorMessage = checkNotNull((TextView) view.findViewById(R.id.error_message));
        mEmpty = checkNotNull((TextView) view.findViewById(android.R.id.empty));

        mErrorLayout.setOnClickListener(this);
    }

    public void onReportStatus(boolean screenEmpty, boolean screenProgress,
                               AUSyncerStatus lastStatus) {
        // TODO: ADD ERROR MESSAGE
        mErrorLayout.setVisibility(lastStatus.isError() ? View.VISIBLE : View.GONE);
        if(lastStatus.isError()){
            mErrorMessage.setText(lastStatus.toString());
        }
        mEmpty.setVisibility(screenEmpty ? View.VISIBLE : View.GONE);
        mProgress.setVisibility((screenProgress ? View.VISIBLE : View.GONE));
    }

    @Override
    public void onClick(View view) {
        mListener.onForceReload();
    }
}
