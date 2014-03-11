package com.appunite.guestbook.dagger;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.appunite.guestbook.MainApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MainApplication.class,
        library = true
)
public class ApplicationModule {

    private final Application mMainApplication;

    public ApplicationModule(Application mainApplication) {
        mMainApplication = mainApplication;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return mMainApplication;
    }

}
