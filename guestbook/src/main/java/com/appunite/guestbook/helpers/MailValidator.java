package com.appunite.guestbook.helpers;

import android.os.Build;
import android.util.Patterns;

import java.util.regex.Pattern;

public class MailValidator {
    private static final String MAIL_REGEXP = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    private static Pattern sMailPattern = null;

    public static Pattern getMailPattern() {
        if (sMailPattern != null) {
            return sMailPattern;
        }
        if (Build.VERSION.SDK_INT >= 8) {
            return sMailPattern = Patterns.EMAIL_ADDRESS;
        } else {
            return sMailPattern = Pattern.compile(MAIL_REGEXP);
        }
    }
}
