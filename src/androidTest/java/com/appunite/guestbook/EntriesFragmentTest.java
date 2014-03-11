package com.appunite.guestbook;

import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.appunite.guestbook.testhelpers.TestHelper;

import org.hamcrest.Matcher;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;


public class EntriesFragmentTest  extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String ENTRY_AUTHOR = "FirstContent";
    private static final Matcher<View> ENTRY_MATCHER = allOf(withId(R.id.content), withText(ENTRY_AUTHOR));

    public EntriesFragmentTest(){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        final Instrumentation instrumentation = getInstrumentation();
        final Context targetContext = instrumentation.getTargetContext();

        targetContext.getApplicationContext();
        TestHelper.clear(targetContext);
    }

    /**
     * Guest user is able to see log-in information.
     * @throws Exception
     */
    public void testGuestInfoVisibility() throws Exception{
        TestHelper.startEntries(this);

        onView(withId(R.id.guest_information)).check(matches(isDisplayed()));
    }

    /**
     * Guest user is not able to see the "new entry" button.
     * @throws Exception
     */
    public void testGuestEntryButtonVisibility() throws Exception {
        TestHelper.startEntries(this);

        onView(withId(R.id.entry_button)).check(matches(not(isDisplayed())));
    }

    /**
     * Guest user is able to click on the ? button.
     * We are unable to test whether the toast message appeared or not.
     * @throws Exception
     */
    public void testGuestInfoDetails()  throws Exception {
        TestHelper.startEntries(this);

        onView(withId(R.id.not_logged_information)).check(matches(isDisplayed())).perform(click());
    }

    /**
     * User is able to click on the "You are not logged in" label and open login options.
     * @throws Exception
     */
    public void testGuestLoginClick() throws Exception {
        TestHelper.startEntries(this);

        onView(withId(R.id.not_logged_label)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.login_options)).check(matches(isDisplayed()));
    }

    /**
     * User is able to see entries list.
     * @throws Exception
     */
    public void testEntryVisibility()  throws Exception {
        TestHelper.startEntries(this);

        onView(withId(R.id.entries_list)).check(matches(isDisplayed()));
    }

    /**
     * User is able to click on an entry.
     * @throws Exception
     */
    public void testEntryClick() throws Exception {
        TestHelper.startEntries(this);

        onView(ENTRY_MATCHER).check(matches(isDisplayed())).perform(click());
    }

    /**
     * User after clicking on entry is able to see full content in a dialog.
     * @throws Exception
     */
    public void testFullEntryDialog() throws Exception {
        TestHelper.startEntries(this);

        onView(ENTRY_MATCHER).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.entry_dialog_content)).check(matches(isDisplayed()));
    }

}
