package com.appunite.guestbook.api.exceptions;

import com.google.api.client.http.HttpResponse;

public class UnknownServerException extends ServerException{

    public static final int UNKNOWN_ERROR_CODE = 0;

    private final int mCode;
    private final String mErrorMessage;

    public UnknownServerException(HttpResponse response){
        this(response, UNKNOWN_ERROR_CODE, null);
    }

    public UnknownServerException(HttpResponse response, String errorMessage){
        this(response, UNKNOWN_ERROR_CODE, errorMessage);
    }

    public UnknownServerException(HttpResponse response, int code, String errorMessage){
        super(response);
        mCode = code;
        mErrorMessage = errorMessage;
    }

    public String getErrorMessage() { return mErrorMessage; }

    public int getCode() { return mCode; }


}
