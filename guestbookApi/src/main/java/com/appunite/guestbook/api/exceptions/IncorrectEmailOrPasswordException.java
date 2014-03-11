package com.appunite.guestbook.api.exceptions;


import com.google.api.client.http.HttpResponse;

public class IncorrectEmailOrPasswordException extends ServerException {
    public IncorrectEmailOrPasswordException(HttpResponse response) { super(response); }
}
