package com.appunite.guestbook.api.content;

import com.google.api.client.http.HttpContent;
import com.google.api.client.json.Json;
import com.google.gson.Gson;

import static com.google.api.client.util.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class GsonHttpContent implements HttpContent {
    protected Gson mGson;

    private Object mSrc;

    public GsonHttpContent(Gson gson){
        mSrc = null;
        mGson = checkNotNull(gson);
    }

    public GsonHttpContent setObject(Object src) {
        mSrc = checkNotNull(src);
        return this;
    }

    protected Object getObject() {
        checkState(mSrc != null, "You have to call setObject before using GsonHttpContent");
        return mSrc;
    }

    @Override
    public String getType() {
        return Json.MEDIA_TYPE;
    }

    @Override
    public boolean retrySupported() {
        return false;
    }
}
