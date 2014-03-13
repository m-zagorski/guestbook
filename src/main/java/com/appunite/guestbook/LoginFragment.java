package com.appunite.guestbook;


import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appunite.guestbook.content.UserPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoginFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != getActivity().RESULT_OK) {
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
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return checkNotNull(inflater.inflate(R.layout.login_fragment, container, false));
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        mProgressBar.setVisibility(View.GONE);

        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        mUserPreferences.edit()
                .setLoggedIn(true)
                .setUserPhoto(person.getImage().getUrl())
                .setUserName(person.getDisplayName())
                .setUserEmail(Plus.AccountApi.getAccountName(mGoogleApiClient))
                .commit();
        Intent intent = new Intent(AppConsts.ACTION_SHOW_ENTRIES);
        startActivity(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mProgressBar.setVisibility(View.GONE);
        if (!mIntentInProgress) {
            mConnectionResult = connectionResult;
            if (mSignInClicked) {
                resolveSignInErrors();
            }
        }
    }

    private void resolveSignInErrors() {
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

    @OnClick(R.id.google_sign_button)
    public void onGoogleSigninClick() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInErrors();
            mProgressBar.setVisibility(View.VISIBLE);
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

}
