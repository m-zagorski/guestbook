package com.appunite.guestbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.appunite.guestbook.helpers.FormValidator;
import com.appunite.guestbook.helpers.MailValidator;
import com.appunite.guestbook.helpers.data.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class EmailLoginFragment extends BaseFragment {

    @InjectView(R.id.email)
    EditText mEmail;
    @InjectView(R.id.password)
    EditText mPassword;

    private FormValidator mFormValidator;

    public static Fragment newInstance() {
        return new EmailLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return checkNotNull(inflater.inflate(R.layout.email_login_fragment, container, false));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmail.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mFormValidator =  new FormValidator(getActivity(), view);
    }

    private void performLogin() {
        boolean valid = true;
        if (!mFormValidator.validatePassword()) {
            valid = false;
        }
        if (!mFormValidator.validateEmail()) {
            valid = false;
        }
        if (!valid) {
            return;
        }
        // TODO Store User details from API to Pref
        Intent intent = new Intent(AppConsts.ACTION_SHOW_SETTINGS);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.signin_button)
    public void onSigninClick() {
        performLogin();
    }
}
