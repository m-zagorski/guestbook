package com.appunite.guestbook.api.exceptions;

import com.google.api.client.http.HttpResponse;

public class EmailNotFound extends ServerException {
    public EmailNotFound(HttpResponse response) { super(response); }
}
