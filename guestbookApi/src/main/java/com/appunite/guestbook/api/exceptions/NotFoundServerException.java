package com.appunite.guestbook.api.exceptions;

import com.google.api.client.http.HttpResponse;

public class NotFoundServerException extends ServerException {
    public NotFoundServerException(HttpResponse response) { super(response);
    }
}
