package com.appunite.guestbook.testhelpers;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.appunite.guestbook.MainActivity;
import com.appunite.guestbook.R;
import com.appunite.guestbook.content.UserPreferences;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;

public class TestHelper {

    private static final String ACTION_SHOW_ENTRIES = "com.appunite.guestbook.ACTION_SHOW_ENTRIES";


    /**
     * Clear userPreferences
     * @param context
     */
    public static void clear(Context context){
        assertTrue(new UserPreferences(context).edit().clear().commit());
    }

    /**
     * Go To Entries Fragment
     * @param testCase
     */
    public static void startEntries(ActivityInstrumentationTestCase2<MainActivity> testCase) {
        Intent intent = new Intent(ACTION_SHOW_ENTRIES);

        final Instrumentation instrumentation = testCase.getInstrumentation();
        final Context targetContext = instrumentation.getTargetContext();

        intent.setClass(targetContext, MainActivity.class);
        testCase.setActivityIntent(intent);
        testCase.getActivity();
    }

    public static void goToLoginOptions(){
        onView(withId(R.id.not_logged_label)).check(matches(isDisplayed())).perform(click());
    }
}
