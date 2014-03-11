package com.appunite.guestbook.api;

import com.appunite.guestbook.api.exceptions.EmailAlreadyExists;
import com.appunite.guestbook.api.exceptions.EmailNotFound;
import com.appunite.guestbook.api.exceptions.ServerException;
import com.appunite.guestbook.api.exceptions.UnknownServerException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GuestbookHttpUnsuccessfulResponseHandler implements HttpUnsuccessfulResponseHandler{

    @Override
    public boolean handleResponse(HttpRequest request, HttpResponse response, boolean supportsRetry) throws IOException {
        return false;
    }

    private void getErrors(HttpResponse httpResponse, JSONObject json) throws JSONException,
            ServerException {
        if(!json.isNull("error")) {
            if(json.get("error") instanceof JSONObject){
                final JSONObject errorObject = json.getJSONObject("error");
                throwError(httpResponse, errorObject);
            } else {
                String msg = json.getString("error");
                throw new UnknownServerException(httpResponse);
            }
        }
        if(!json.isNull("errors")) {
            JSONArray errors = json.getJSONArray("errors");
            JSONObject error = errors.getJSONObject(0);
            throwError(httpResponse, error);
        }
    }

    private void throwError(HttpResponse httpResponse, JSONObject error) throws JSONException, ServerException {
        String message = error.isNull("message") ? null : error.getString("message");
        if(!error.isNull("code")) {
            int errorCode = error.getInt("code");
            if(errorCode == 602){
                throw new EmailAlreadyExists(httpResponse);
            } else if(errorCode == 604) {
                throw new EmailNotFound(httpResponse);
            } else {
                throw new UnknownServerException(httpResponse, errorCode, message);
            }
        }
        throw new UnknownServerException(httpResponse, message);
    }
}
