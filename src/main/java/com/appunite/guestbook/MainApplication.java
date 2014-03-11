package com.appunite.guestbook;

import android.app.Application;

import com.appunite.guestbook.dagger.ApplicationModule;
import com.crashlytics.android.Crashlytics;

import dagger.ObjectGraph;

public class MainApplication extends Application {

    private ObjectGraph mApplicationGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeDagger();

        if (BuildConfig.RELEASE) {
            Crashlytics.start(this);
        }
    }

    private void initializeDagger() {
        ApplicationModule module = new ApplicationModule(this);
        mApplicationGraph = ObjectGraph.create(module);
        mApplicationGraph.inject(this);
    }

    public ObjectGraph getApplicationGraph() {
        return mApplicationGraph;
    }

    public static MainApplication fromApplication(Application application) {
        return (MainApplication) application;
    }
}
