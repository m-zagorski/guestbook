package com.appunite.guestbook.api.exceptions;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

public abstract class ServerException extends HttpResponseException {

    public ServerException(HttpResponse response) { super(response);}
}
