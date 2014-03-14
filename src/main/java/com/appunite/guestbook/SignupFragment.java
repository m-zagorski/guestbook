package com.appunite.guestbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class SignupFragment extends BaseFragment {

    @InjectView(R.id.email)
    EditText mEmail;
    @InjectView(R.id.password)
    EditText mPassword;
    @InjectView(R.id.retyped_password)
    EditText mRetypedPassword;

    private FormValidator mFormValidator;

    public static Fragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return checkNotNull(inflater.inflate(R.layout.signup_fragment, container, false));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmail.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mPassword.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mRetypedPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mFormValidator = new FormValidator(getActivity(), view);
    }

    private void performSignup() {
        boolean valid = true;
        if (!mFormValidator.validatePassword()) {
            valid = false;
        }
        if (!mFormValidator.validateRetypedPassword()) {
            valid = false;
        }
        if (!mFormValidator.validateEmail()) {
            valid = false;
        }
        if (!valid) {
            return;
        }
        // TODO Add data to API and Prefs. Go to ? LoginForm or Profile Settings
    }

    @OnClick(R.id.new_entry_dialog_add)
    public void onSignupClick() {
        performSignup();
    }
}
