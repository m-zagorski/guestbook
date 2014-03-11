package com.appunite.guestbook.api;

import com.appunite.guestbook.api.content.GsonHttpContent;
import com.appunite.guestbook.api.content.GsonObjectParser;
import com.appunite.guestbook.api.model.ResponseEntries;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.UriTemplate;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

public class GuestbookApi {

    @Inject
    Provider<GsonHttpContent> mGsonHttpContentProvider;

    final Gson mGson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    HttpRequestFactory mRequestFactory;

    public GuestbookApi() {
        mRequestFactory = getRequestFactory();
    }

    private HttpRequestFactory getRequestFactory(){
        return new ApacheHttpTransport().createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                final HttpHeaders headers = request.getHeaders();
                headers.setAccept("application/json");
                request.setParser(new GsonObjectParser(mGson));
            }
        });
    }

    public abstract class GuestbookRequest<T> {
        private final String mUriTemplate;
        private final String mRequestMethod;
        private HttpContent mContent;
        private final Class<T> mResponseClass;

        public GuestbookRequest(String requestMethod, String uriTemplate, HttpContent content,
                            Class<T> responseClass) {
            mUriTemplate = checkNotNull(uriTemplate, "Uri could not be null");
            mRequestMethod = checkNotNull(requestMethod);
            mResponseClass = checkNotNull(responseClass);
            setObjectContent(content);
        }

        public void setObjectContent(Object content) {
            setContent(content == null ? null : mGsonHttpContentProvider.get().setObject(content));
        }

        public void setContent(HttpContent content){
            mContent = content;
        }

        public T execute() throws IOException {
            String baseUrl = "http://guestbook.appunite.com/api";

            GenericUrl url = new GenericUrl(UriTemplate.expand(baseUrl, mUriTemplate, this, true));
            final HttpRequest httpRequest = mRequestFactory.buildRequest(mRequestMethod, url, mContent);
            setupRequest(httpRequest);
            final T data = httpRequest.execute().parseAs(mResponseClass);
            return data;
        }

        protected void setupRequest(HttpRequest httpRequest) {

        }

    }


    public EntriesApi entries() { return new EntriesApi(); }

    public class EntriesApi {
        public List list() { return new List(); }
    }

    public class List extends GuestbookRequest<ResponseEntries>{
        private static final String REST_PATH = "v1/entries/entries.json";

        private List(){
            super(HttpMethods.GET, REST_PATH, null, ResponseEntries.class);
        }
    }

}
