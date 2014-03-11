package com.appunite.guestbook.api.content;

import com.google.api.client.util.ObjectParser;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull;

public class GsonObjectParser implements ObjectParser {

    private final Gson mGson;


    public GsonObjectParser(Gson gson) {
        checkNotNull(gson);
        mGson = gson;
    }

    @Override
    public <T> T parseAndClose(InputStream inputStream, Charset charset, Class<T> dataClass) throws IOException {
        if (inputStream == null) return null;
        try {
            final InputStreamReader reader = new InputStreamReader(inputStream, charset);
            return parseAndClose(reader, dataClass);
        } finally {
            inputStream.close();
        }
    }

    @Override
    public Object parseAndClose(InputStream inputStream, Charset charset, Type dataType) throws IOException {
        if (inputStream == null) return null;
        try {
            final InputStreamReader reader = new InputStreamReader(inputStream, charset);
            return parseAndClose(reader, dataType);
        } finally {
            inputStream.close();
        }
    }

    @Override
    public <T> T parseAndClose(Reader reader, Class<T> dataClass) throws IOException {
        if (reader == null) return null;
        try {
            return mGson.fromJson(reader, dataClass);
        } finally {
            reader.close();
        }
    }

    @Override
    public Object parseAndClose(Reader reader, Type dataType) throws IOException {
        if (reader == null) return null;
        try {
            return mGson.fromJson(reader, dataType);
        } finally {
            reader.close();
        }
    }
}
