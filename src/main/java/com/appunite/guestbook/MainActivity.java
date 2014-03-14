package com.appunite.guestbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MainActivity extends BaseActivity {

    private static final String LOGIN_FRAGMENT = "login_fragment";

    /**
     * Because when activity is recreated there are called onCreate and
     * onNewIntent. We have to ignore second call to omit twice view creation.
     */
    private boolean mIgnoreFirstIntent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        if (savedInstanceState != null) {
            mIgnoreFirstIntent = true;
        }
        final Intent intent = getIntent();
        activate(intent, savedInstanceState);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mIgnoreFirstIntent) {
            mIgnoreFirstIntent = false;
            return;
        }
        setIntent(intent);

        int flags = intent.getFlags();
        if ((flags & Intent.FLAG_ACTIVITY_NEW_TASK) == 0) {
            activate(intent, null);
        }
    }

    private void activate(Intent intent, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            getFragment(ft, intent);
            ft.commit();
        }
    }


    private void getFragment(FragmentTransaction ft, Intent intent) {
        String action = intent.getAction();

        if (AppConsts.ACTION_SHOW_ENTRIES.equals(action)) {
            clearBackStack();
            Fragment fragment = EntriesFragment.newInstance();
            ft.replace(R.id.main_content, fragment);
        } else if (AppConsts.ACTION_SHOW_LOGIN.equals(action)) {
            Fragment fragment = LoginFragment.newInstance();
            ft.replace(R.id.main_content, fragment, LOGIN_FRAGMENT);
            ft.addToBackStack(null);
        } else if (AppConsts.ACTION_SHOW_EMAIL_LOGIN.equals(action)) {
            Fragment fragment = EmailLoginFragment.newInstance();
            ft.replace(R.id.main_content, fragment);
            ft.addToBackStack(null);
        } else if (AppConsts.ACTION_SHOW_SIGNUP.equals(action)) {
            Fragment fragment = SignupFragment.newInstance();
            ft.replace(R.id.main_content, fragment);
            ft.addToBackStack(null);
        } else if (AppConsts.ACTION_SHOW_SETTINGS.equals(action)) {
            Fragment fragment = EditProfileFragment.newInstance();
            ft.replace(R.id.main_content, fragment);
            ft.addToBackStack(null);
        } else if (AppConsts.ACTION_SHOW_NEW_ENTRY.equals(action)) {
            Fragment fragment = NewEntryFragment.newInstance();
            ft.replace(R.id.main_content, fragment);
            ft.addToBackStack(null);
        } else {
            throw new RuntimeException("Unknown action: " + action);
        }
    }

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        Log.e("COUNT: ", "" + fm.getBackStackEntryCount());
        for(int i=0; i< fm.getBackStackEntryCount(); i++){
            fm.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() != 0) {
            fm.popBackStack();
        } else {
            this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(LOGIN_FRAGMENT);
        if (fragment != null) {
           fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
