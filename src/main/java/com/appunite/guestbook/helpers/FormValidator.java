package com.appunite.guestbook.helpers;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.appunite.guestbook.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

public class FormValidator {

    @Optional
    @InjectView(R.id.email)
    EditText mEmail;
    @Optional
    @InjectView(R.id.password)
    EditText mPassword;
    @Optional
    @InjectView(R.id.retyped_password)
    EditText mRetypedPassword;
    @Optional
    @InjectView(R.id.content)
    EditText mContent;
    @Optional
    @InjectView(R.id.username)
    EditText mUsername;

    private Context mContext;

    public FormValidator(Context context, View view) {
        ButterKnife.inject(this, view);
        mContext = context;
    }

    public boolean validatePassword() {
        CharSequence password = mPassword.getText();
        if (password == null || password.length() == 0) {
            mPassword
                    .setError(mContext.getResources().getString(R.string.form_validator_empty_password));
            return false;
        }
        return true;
    }

    public boolean validateRetypedPassword() {
        CharSequence password = mPassword.getText();
        CharSequence retypedPassword = mRetypedPassword.getText();
        if (!password.equals(retypedPassword)) {
            mRetypedPassword.setError(mContext.getResources().getString(R.string.form_validator_different_passwords));
            return false;
        }
        return true;
    }

    public boolean validateEmail() {
        CharSequence email = mEmail.getText();
        if (email == null || email.length() == 0) {
            mEmail.setError(mContext.getResources().getText(R.string.form_validator_empty_email));
            return false;
        }
        Pattern mailPattern = MailValidator.getMailPattern();
        Matcher mailMatcher = mailPattern.matcher(email);
        if (!mailMatcher.matches()) {
            mEmail.setError(mContext.getResources().getText(R.string.form_validator_wrong_email));
            return false;
        }
        return true;
    }

    public boolean validateContent() {
        CharSequence content = mContent.getText();
        if (content == null || content.length() == 0) {
            mContent.setError(mContext.getResources().getString(R.string.form_validator_empty_content));
            return false;
        }
        return true;
    }

    public boolean validateUsername() {
        CharSequence content = mUsername.getText();
        if (content == null || content.length() == 0) {
            mUsername.setError(mContext.getResources().getString(R.string.form_validator_empty_username));
            return false;
        }
        return true;
    }

}
