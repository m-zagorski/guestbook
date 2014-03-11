package com.appunite.guestbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.appunite.guestbook.dagger.ActivityGraphProvider;
import com.appunite.guestbook.dagger.ActivityModule;

import dagger.ObjectGraph;

public class BaseActivity extends ActionBarActivity implements ActivityGraphProvider {

    private ObjectGraph mActivityGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityGraph().inject(this);
    }

    @Override
    public ObjectGraph getActivityGraph() {
        if (mActivityGraph == null) {
            mActivityGraph = MainApplication.fromApplication(getApplication())
                    .getApplicationGraph()
                    .plus(new ActivityModule(this));
        }

        return mActivityGraph;
    }
}
