package com.appunite.guestbook;

import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.appunite.guestbook.testhelpers.TestHelper;

public class LoginOptionsTest extends ActivityInstrumentationTestCase2<MainActivity>{

    public LoginOptionsTest(){
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




}
