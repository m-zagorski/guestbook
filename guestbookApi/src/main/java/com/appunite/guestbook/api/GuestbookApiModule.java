package com.appunite.guestbook.api;

import com.appunite.guestbook.api.content.GsonHttpContent;
import com.appunite.guestbook.api.content.StreamingGsonHttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = GuestbookApi.class,
        complete = false,
        library = true
)
public class GuestbookApiModule {

    @Provides
    @Singleton
    GsonHttpContent provideGsonHttpContent(Gson gson) {
        return new StreamingGsonHttpContent(gson);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    HttpUnsuccessfulResponseHandler providesHttpUnsuccessfulResponseHandler() {
        return new GuestbookHttpUnsuccessfulResponseHandler();
    }


}
