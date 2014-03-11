package com.appunite.guestbook.api.exceptions;

import com.google.api.client.http.HttpResponse;

public class EmailAlreadyExists extends com.appunite.guestbook.api.exceptions.ServerException {

    public EmailAlreadyExists(HttpResponse response) { super(response) ; }
}
