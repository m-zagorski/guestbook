package com.appunite.guestbook;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appunite.guestbook.content.UserPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    private static final String ERROR_DIALOG = "error_dialog";

    @InjectView(android.R.id.progress)
    ProgressBar mProgressBar;
    @Inject
    UserPreferences mUserPreferences;
    @Inject
    GoogleApiClient mGoogleApiClient;

    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    public static Fragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient.registerConnectionCallbacks(this);
        mGoogleApiClient.registerConnectionFailedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        mProgressBar.setVisibility(View.VISIBLE);
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != getActivity().RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return checkNotNull(inflater.inflate(R.layout.login_fragment, container, false));
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        new FetchGoogleDataTask().execute();
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        mProgressBar.setVisibility(View.GONE);
        if (!result.hasResolution()) {
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                mSignInClicked = false;
                resolveSignInError();
            }
        }
    }

    private boolean checkGoogleServices(){
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if(status != ConnectionResult.SUCCESS){
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
        return true;
    }

    private void launchProfileSettings() {
        Intent intent = new Intent(AppConsts.ACTION_SHOW_ENTRIES);
        startActivity(intent);
    }

    @OnClick(R.id.google_sign_button)
    public void onGoogleSigninClick() {
        if(!checkGoogleServices()){
            return;
        }
        if (!mGoogleApiClient.isConnecting() && !mSignInClicked) {
            mProgressBar.setVisibility(View.VISIBLE);
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    @OnClick(R.id.sign_in_button)
    public void onSigninClick() {
        Intent intent = new Intent(AppConsts.ACTION_SHOW_EMAIL_LOGIN);
        startActivity(intent);
    }

    @OnClick(R.id.sign_up_button)
    public void onSignupClick() {
        Intent intent = new Intent(AppConsts.ACTION_SHOW_SIGNUP);
        startActivity(intent);
    }

    public class FetchGoogleDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            if (mGoogleApiClient.isConnected()) {
                Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                if (person != null) {
                    mUserPreferences.edit()
                            .setLoggedIn(true)
                            .setUserPhoto(person.getImage().getUrl())
                            .setUserName(person.getDisplayName())
                            .setUserEmail(Plus.AccountApi.getAccountName(mGoogleApiClient))
                            .commit();
                    return null;
                } else {
                    return getString(R.string.login_error_connection);
                }
            } else {
                return getString(R.string.login_error_google);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSignInClicked = false;
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            mProgressBar.setVisibility(View.GONE);
            if (response == null) {
                launchProfileSettings();
            } else {
                FetchingDataErrorDialog dialog = new FetchingDataErrorDialog(response);
                dialog.setCancelable(false);
                dialog.show(getFragmentManager(), ERROR_DIALOG);
            }
        }
    }

    public class FetchingDataErrorDialog extends DialogFragment {
        private String mErrorMessage;

        public FetchingDataErrorDialog(String errorMessage) {
            mErrorMessage = errorMessage;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(mErrorMessage)
                    .setTitle(getString(R.string.error_dialog_title))
                    .setPositiveButton(getString(R.string.error_dialog_refresh_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new FetchGoogleDataTask().execute();
                        }
                    })
                    .setNegativeButton(getString(R.string.error_dialog_continue_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(AppConsts.ACTION_SHOW_SETTINGS);
                            getActivity().startActivity(intent);
                        }
                    });
            return builder.create();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.unregisterConnectionCallbacks(this);
        mGoogleApiClient.unregisterConnectionFailedListener(this);
    }
}
